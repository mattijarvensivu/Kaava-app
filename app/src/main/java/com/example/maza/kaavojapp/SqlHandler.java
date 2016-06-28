package com.example.maza.kaavojapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Created by janne on 3.6.2016.
 */
public class SqlHandler extends SQLiteOpenHelper {

    private static String DB_PATH = "/data/data/com.example.maza.kaavojapp/databases/";

    private static String DB_NAME = "Kaavapp.db";


    private SQLiteDatabase myDataBase;

    private final Context myContext;

    public SqlHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, boolean forceCreate) {
        super(context, DB_NAME, null, 1); //ladataan name.db database jos on olemasa muutoin mennään onCreate metodiin.
        myContext = context;
        try {

            createDataBase(forceCreate);

        } catch (IOException ioe) {

            throw new Error("Unable to create database");

        }

        try {

            openDataBase();

        }catch(SQLException sqle){

            try {
                throw sqle;
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

    }
    //http://blog.reigndesign.com/blog/using-your-own-sqlite-database-in-android-applications/ alkaa
    public void createDataBase(boolean forceCreate) throws IOException {

        boolean dbExist = !forceCreate && checkDataBase(); //hiukan ehkä turhaa hifistelyä mutta menkööt

        if(dbExist){
            //do nothing - database already exist
        }else{

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }


    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){

        SQLiteDatabase checkDB = null;

        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        }catch(SQLiteException e){

            //database does't exist yet.

        }

        if(checkDB != null){

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{

        Log.d("minun","creating database");
        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            Log.d("minun","writing");
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {

        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }
    //http://blog.reigndesign.com/blog/using-your-own-sqlite-database-in-android-applications/ loppuu
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    //haetaan dataa taulukosta annetuilla parametreillä
    public ArrayList<Tulos> getValue (String tableName, HashMap<String,String> searchParameters) {
        Cursor cur = getCursor(tableName,searchParameters);
        ArrayList<String[]> tableS = getStructure(tableName);
        ArrayList<Tulos> pal = new ArrayList<>();
        //käsitellään saatu data
        try{
            if(cur.moveToFirst()) {
                do{
                    HashMap<String, String> tmp = new HashMap<String, String>();
                    for(int i = 0; i < tableS.size(); i++)
                    {

                        tmp.put(tableS.get(i)[0],cur.getString(i));
                    }
                    pal.add(Tulos.getTulos(tmp));
                }while(cur.moveToNext());

            }
        }finally {

        }
        Log.d("minun","loytyi " + pal.size() + " osumaa");
        return pal;
    }

    //Luodaan HashMappi johonka voidaan laittaa annetun nimisen taulun hakuparametrit.
    public HashMap<String,String> getParamMap(String tableName)
    {
        HashMap<String, String> pal = new HashMap<>();
        ArrayList<String[]> fieldNames = getStructure(tableName);
        for(int i = 0; i < fieldNames.size(); i++)
        {
            pal.put(fieldNames.get(i)[0],null);
        }
        return pal;

    }

    //Haetaan annetun taulun rakenne, eli kenttien nimet ja tyyppi
    public ArrayList<String[]> getStructure(String tableName)
    {
        ArrayList<String[]> pal = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        //haetaan pölydän rakenne
        Cursor cur = db.rawQuery("PRAGMA table_info('" + tableName +"')", null); // itse haku täpahtuu tässä
        //käsitellään saatu data
        try{
            if(cur.moveToFirst()) {
                do{
                    String[] tmp = {cur.getString(1), cur.getString(2)}; //kentän nimi, kentän tyyppi
                    pal.add(tmp);
                }while(cur.moveToNext());

            }
        }finally {
            //cur.close(); //tarvitaanko?
            //db.close();
        }
        return pal;
    }


    public Cursor getCursor(String tableName, HashMap<String, String> searchParameters)
    {
        ArrayList<String[]> tableS = getStructure(tableName);
        //luodaan querry
        String query = "Select * from " + tableName;
        String searchParamsS = "";
        boolean isFirst = true;
        for(int i = 0; i < tableS.size(); i++)
        {
            if(searchParameters.get(tableS.get(i)[0]) != null)
            {
                Log.d("minun","osui " + tableS.get(i)[0] + ": " + searchParameters.get(tableS.get(i)[0]));
                if(searchParamsS.compareTo("") == 0) searchParamsS += " where "; //On olemassa ainakin yksi haku rajoite ja queryyn ei ole lisätty where avain sanaa. Lisätään se
                if(!isFirst)
                {
                    searchParamsS += " AND "; //tätä rajoitetta edeltää ainakin yksi toinen rajoite. Lisätään and
                }
                //Kyseisellä kentällä on rajoite. Lisätään se kutsuun
                searchParamsS +=  tableS.get(i)[0] +  " LIKE " ;
                if(tableS.get(i)[1].compareTo("TEXT") == 0)
                {
                    //kyseessä on teksti typpinen kenttä. lisätään hipsuilla
                    searchParamsS += "'" + searchParameters.get(tableS.get(i)[0]) + "'";
                }else{
                    //kyseessä numero kenttä. Ei hipsuja
                    searchParamsS += searchParameters.get(tableS.get(i)[0]);
                }
                isFirst = false;
            }
        }
        //toteutetaan haku
        SQLiteDatabase db = getWritableDatabase();
        Log.d("minun",query + searchParamsS);
        Cursor pal = db.rawQuery(query + searchParamsS, null); // itse haku täpahtuu tässä
        return pal;
    }





    /*public void addLine (String str, String integ) {
        //Katsotaan onko identtinen rivi jo olemassa
        HashMap<String, String> data = getValue("", str, integ);
        if(data.get("id") != null )
        {
            Log.d("minun","IDenttinen rivi oli jo olemassa");
            return; //Tässä ois ihan hyvä jos kerrottais käyttäjälle että ny kosahti. Jos sais vaikka toastin toimimaan
        }
        //rakennetaan querry
        String querry = "insert into kaavat(name,latex) values('" + str + "','" + integ + "')";
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(querry);
        db.close();
    }*/
}

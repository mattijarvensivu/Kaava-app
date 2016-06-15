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
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by janne on 3.6.2016.
 */
public class SqlHandler extends SQLiteOpenHelper {

    private static String DB_PATH = "/data/data/com.example.maza.Kaavojapp/databases/";

    private static String DB_NAME = "Kaavapp.db";

    private SQLiteDatabase myDataBase;

    private final Context myContext;

    public SqlHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, null, 1); //ladataan name.db database jos on olemasa muutoin mennään onCreate metodiin.
        myContext = context;
    }
    //http://blog.reigndesign.com/blog/using-your-own-sqlite-database-in-android-applications/ alkaa
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

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
        //meidän sovelluksen ei vissiinkään koskaan tarvii tulla tänne, koska database on jo luotu valmiiksi.
        Log.d("minun", "tultiin dbn onCreaten");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //haetaan dataa taulukosta annetuilla parametreillä
    public HashMap<String, String> getValue (String Id) {
        HashMap<String, String> pal = new HashMap<>(); //palautettava arvo. Jos ei saada avaimella mitään niin ilmeisesti tuodaan nullia?
        SQLiteDatabase db = getWritableDatabase();
        //tehdään haku databaseen.
        String query = "select * from android_metadata";
       /* if(Id.compareTo("") != 0 || nimi.compareTo("") != 0 || latex.compareTo("") != 0 )
        {
            //on annettu haku termi
            query = query + " where";
            if(Id.compareTo("") != 0)
            {
                //ID oli annettu
                query += " _id = " + Id;
                if(nimi.compareTo("") != 0 || latex.compareTo("") != 0) {
                    //oli muitakin hakutermejä
                    query += " and";
                }
            }
            if(nimi.compareTo("") != 0)
            {
                //name oli annettu
                query += " name = '" + nimi + "'"; //huom, kun haetaan stringin perusteella tulee olla hipsut. Jos jompaan kumpaan muuhun kenttään antaa muuta, tuottaa tämä errorin!
                if(latex.compareTo("") != 0) {
                    //myös latex oli annettu
                    query += " and";
                }
            }
            if(latex.compareTo("") != 0) {
                //Ainakin ilatex oli annettu
                query += " latex = '" + latex + "'";
            }

        }
        */
        Log.d("querry", query); //tarkistetaan että querry on oikein ja järkevä
        Cursor cur = db.rawQuery(query, null); // itse haku täpahtuu tässä

        //luetaan saatu data ja tallennetaan se HashMappiin. Jos useita osumia, vain ensimmäinen valitaan
        try{
            if(cur.moveToFirst()) {
                // do{
                pal.put("id",cur.getString(0)); //haetaan data sarakkeesta 0 ja laitetaan se hashMappiin
                //pal.put("name",cur.getString(1)); // pitäs olla selvää mitä tässä tehään
                //pal.put("latex",cur.getString(2));

                //}while(cur.moveToNext());

            }
        }finally {
            cur.close(); //tarvitaanko?
            db.close();
        }

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

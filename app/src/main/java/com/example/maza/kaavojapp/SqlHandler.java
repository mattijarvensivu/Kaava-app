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
        ArrayList<Tulos> pal = new ArrayList<>();
        ArrayList<String[]> tableS = getStructure(tableName);
//jos table muuttuja ni hakee ne. Pitää käyttää eri cursoria ni pitää olla tässä. pitää olla if else tai käyttää myös toista cursoria ja hakee kaikki vaikkei pitäis
        //jos tulee useampia moni-moneen ja haluaa yhellä kutsulla ni if elseen vaan putkeen.
        if(tableName.compareTo("muuttuja")==0) {

            Cursor cura = getMuuttujacursor(searchParameters);
            tableS = getStructure(tableName);
            try {
                if (cura.moveToFirst()) {
                    do {
                        HashMap<String, String> tmp = new HashMap<String, String>();
                        for (int i = 0; i < tableS.size(); i++) {

                            tmp.put(tableS.get(i)[0], cura.getString(i));
                        }
                        pal.add(Tulos.getTulos(tmp));
                    } while (cura.moveToNext());

                }
            } finally {

            }
        }else
        if (tableName.compareTo("vakio") == 0) {
            Cursor cura = getVakiocursor(searchParameters);
            tableS = getStructure(tableName);
            try {
                if (cura.moveToFirst()) {
                    do {
                        HashMap<String, String> tmp = new HashMap<String, String>();
                        for (int i = 0; i < tableS.size(); i++) {

                            tmp.put(tableS.get(i)[0], cura.getString(i));
                        }
                        pal.add(Tulos.getTulos(tmp));
                    } while (cura.moveToNext());

                }
            } finally {

            }
        } else{


            Cursor cur = getCursor(tableName, searchParameters);

            //käsitellään saatu data
            try {
                if (cur.moveToFirst()) {
                    do {
                        HashMap<String, String> tmp = new HashMap<String, String>();
                        for (int i = 0; i < tableS.size(); i++) {

                            tmp.put(tableS.get(i)[0], cur.getString(i));
                        }
                        pal.add(Tulos.getTulos(tmp));
                    } while (cur.moveToNext());

                }
            } finally {

            }

            //tarkistetaan että oliko haettu arvo alkuaineet taulussa. jos oli niin haetaan jokaiselle tulokselle isotoopit.


            if(tableName.compareTo("Kaava")==0)
            {

                for(int k = 0; k< pal.size();k++)
                { //haetaan Kaavan muuttujat.
                    String kaavaid = pal.get(k).getValue("_kaavaid");
                    HashMap<String, String> tmp = new HashMap<>();
                    HashMap<String, String> tmp2 = new HashMap<>();
                    tmp.put("_kaavaid", kaavaid);
                    tmp2.put("_kaavaid", kaavaid);
                    Log.d("Muuttujaids", kaavaid);
                    ((kaavaTulos)pal.get(k)).addVakiot(getValue("vakio",tmp2));
                    ((kaavaTulos) pal.get(k)).addMuuttujat(getValue("muuttuja", tmp));
                }
            }


            if(tableName.compareTo("alkuaineet") == 0)
            {
                for(int i = 0; i < pal.size(); i++)
                {
                    //haetaan alkuaineen isotoopit.
                    HashMap<String,String> tmp = new HashMap<>();
                    tmp.put("_alkuaineid",pal.get(i).getValue("_id"));
                    ((alkuaineTulos)pal.get(i)).addIsotoopit(getValue("isotoopit",tmp));
                }
            }

            Log.d("minun","loytyi " + pal.size() + " osumaa");

            //tarkistetaan oliko haku isotppooi taulusta. jos oli niin haetaan isotooppeja vastaavat symbolit.
            if(tableName.compareTo("isotoopit") == 0) {
                String symbol = "";
                String prevId = "-1";
                for (int i = 0; i < pal.size(); i++) {
                    //tarkistetaan oliko edellinen isotooppi samasta perus alkuaineesta. jos oli niin ei tarvita uutta hakua
                    if (pal.get(i).getValue("_alkuaineid").compareTo(prevId) != 0) {
                        //Edellinen haettu symboli oli eri id:lle, Joudutaan tekemään haku.
                        HashMap<String, String> tmp = new HashMap<>();
                        prevId = pal.get(i).getValue("_alkuaineid");
                        tmp.put("_id", prevId);
                        cur = getCursor("alkuaineet", tmp); //tehtävä näin. getValue kutsu aiheuttaisi loputtoman loopin
                        try {
                            if (cur.moveToFirst()) {
                                do {
                                    symbol = cur.getString(1);
                                } while (cur.moveToNext());

                            }
                        } finally {

                        }
                    }
                    ((isotooppiTulos) pal.get(i)).setSymbol(symbol);
                }
            }
        }







        return pal;
    }

    public ArrayList<Tulos> getValueByTag (String tableName, HashMap<String,String> searchParameters) {
        ArrayList<Tulos> pal = new ArrayList<>();
        ArrayList<String[]> tableS = getStructure(tableName);

        Cursor cur = getTagcursor(tableName, searchParameters);


        try {
            if (cur.moveToFirst()) {
                do {
                    HashMap<String, String> tmp = new HashMap<String, String>();
                    for (int i = 0; i < tableS.size(); i++) {

                        tmp.put(tableS.get(i)[0], cur.getString(i));
                    }
                    pal.add(Tulos.getTulos(tmp));
                } while (cur.moveToNext());

            }
        } finally {

        }


        return pal;
    }

    public Cursor getTagcursor(String tableName, HashMap<String, String> searchParameters) {
        String linkkitaulu = "";
        String kohdetaulu = "";
        if(tableName.compareTo("AlkuaineetTag")==0){
            linkkitaulu = "Alkuaineet_tag";
            kohdetaulu = "Alkuaineet";
        }
        SQLiteDatabase db = getWritableDatabase();


        String query = "Select * From"+kohdetaulu + " a left join"+linkkitaulu + " as ta on (a._id = ta._alkuaineetid)"+
        "left join"+ tableName+"  as t on (ta._tagid = t._tagid) Where t.nimi = " +searchParameters.get("nimi");

        Log.d("Tag Cursor query",query);
        Cursor pal = db.rawQuery(query, null);
        return pal;
    }


    //etsitään nimetyn taulun oletus kentät
    public HashMap<String,String> getDefaultMap(String tableName)
    {
        //suoritetaan haku default names tauluun
        HashMap<String,String> tmp = new HashMap<>();
        tmp.put("_tname",tableName);
        //coidaan käyttää perus getCursor metodia
        Cursor c = getCursor("defaultFields",tmp);
        HashMap<String,String> pal = new HashMap<>();
        if(c.moveToFirst())
        {
            do{
                pal.put(c.getString(1),"");
            }while(c.moveToNext());
        }

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

    public Cursor getMuuttujacursor( HashMap<String, String> searchParameters){
        SQLiteDatabase db = getWritableDatabase();
        //Log.d("KAAVAID", kaavaid);
        String query= "Select * From Muuttuja m left join Muuttuja_Kaava as mk on (m._muuttujaid = mk._muuttujaid)" +
                " left join Kaava as k on (mk._kaavaid = k._kaavaid) Where k._kaavaid = " +  searchParameters.get("_kaavaid") ;
        Log.d("minun",query);
        Cursor pal = db.rawQuery(query, null); // itse haku täpahtuu tässä
        return pal;
    }
    public Cursor getVakiocursor(HashMap<String, String> searchParameters){
        SQLiteDatabase db = getWritableDatabase();
        String query= "Select v._vakioid, v.nimi, v.yksikko, v.arvo, v.symbol From Vakio v left join Vakio_Kaava as vk on (v._vakioid = vk._vakioid)"+
                "left join Kaava as k on (vk._kaavaid = k._kaavaid) Where k._kaavaid =" +  searchParameters.get("_kaavaid") ;
        Log.d("minun",query);
        Cursor pal = db.rawQuery(query, null); // itse haku täpahtuu tässä
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



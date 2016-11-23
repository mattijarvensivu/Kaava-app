package com.Kaavapp.android;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by janne on 3.6.2016.
 */
public class SqlHandler extends SQLiteOpenHelper {

    private static String DB_PATH = "/data/data/com.Kaavapp.android/databases/";
    private String[] tagillisetArray = {"Alkuaineet", "Kaava", "Funktionaalinenryhma","Vakio","hapot","aine","yksikot"}; //tässä määritellään ne joilla on tägi taulu. Ehkä tämän tiedon voisi viedä tietokantaan... vaikka defaultFields tauluun?

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
        boolean writeSuosikit = false;
        HashMap<String, ArrayList<Integer>> suosikit = new HashMap<>();
        ArrayList<String> linkit = new ArrayList<>(); //ei periaatteessa tarvita mutta helpompi kirjoittaa
        try {

            if (checkDataBase()) { //tarksitetaan onko tietokanta olemassa.
                //tietokanta on olemassa
                writeSuosikit = true;

                //otetaan suosikki asetukset talteen.
                //haetaan defaultFieldsistä kaikki taulut joilla on tägitaulu, ja sen linkkitaulu
                SQLiteDatabase d = getReadableDatabase();
                String querryDefault = "SELECT _tname,linkkiTaulu,tagiTaulu FROM defaultFields WHERE linkkiTaulu NOT NULL";
                Cursor curDef = d.rawQuery(querryDefault, null);
                if (curDef.moveToFirst()) {
                    do {
                        String linkki = curDef.getString(1);
                        suosikit.put(linkki, new ArrayList<Integer>());
                        //haetaan tätä linkkitaulua vastaavasta tägitaulusta suosikki tägin id
                        String querrySuos = "SELECT _tagid FROM " + curDef.getString(2) + " WHERE nimi LIKE \"suosikki\"";
                        Cursor curS = d.rawQuery(querrySuos, null);
                        int sId = -1;
                        if (curS.moveToFirst()) {
                            do {
                                sId = curS.getInt(0);
                                suosikit.get(linkki).add(sId); //tallennetaan id listan ensimmäiseen alkioon. Tämä muistettava hashmappia lukiessa!
                                linkit.add(linkki);
                            } while (curS.moveToNext());
                        }
                        curS.close();
                        //etsitään kaikki ne idt joilla on suosikki tägi
                        String querryIdt = "SELECT " + findPrimaryKeyName(curDef.getString(0)) + " FROM " + linkki + " WHERE _tagid = " + sId;
                        Cursor curIdt = d.rawQuery(querryIdt, null);
                        if (curIdt.moveToFirst()) {
                            //luetaan idt muistiin
                            do {
                                suosikit.get(linkki).add(curIdt.getInt(0));
                            } while (curIdt.moveToNext());
                        }
                        curIdt.close();

                    } while (curDef.moveToNext());
                }
                curDef.close();
                d.close(); //tarvitaanko?

            }
        }catch (SQLiteException e)
        {
            Log.d("minun", "Tapahtui sqlVirhe!");
        }

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
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

        if(writeSuosikit) { //Suosikit otettiin ylös. Viedään ne takaisin päivitettyyn tietokantaan
            //Nyt pitää kirjoittaa suosikit takaisin omiin linkki tauluihinsa.
            SQLiteDatabase dn = getWritableDatabase();
            for (int i = 0; i < linkit.size(); i++) {
                for (int j = 1; j < suosikit.get(linkit.get(i)).size(); j++) {
                    String querry = "INSERT INTO " + linkit.get(i) + " VALUES (" + suosikit.get(linkit.get(i)).get(j) + " , " + suosikit.get(linkit.get(i)).get(0) + ")";
                    dn.execSQL(querry);
                }
            }
            dn.close();
        }



    }

    public void openDataBase() throws SQLException {

        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

    }
    //http://blog.reigndesign.com/blog/using-your-own-sqlite-database-in-android-applications/ loppuu
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    //haetaan dataa taulukosta annetuilla parametreillä
    public ArrayList<Tulos> getValue (String tableName, HashMap<String,String> searchParameters, ArrayList<HashMap<String, String>> tagit) {
        ArrayList<Tulos> pal = new ArrayList<>();
        ArrayList<String[]> tableS = getStructure(tableName);
HeaderClass header = new HeaderClass();

        Cursor cur = getCursor(tableName, searchParameters, tagit);
        Log.d("minun",cur.getCount() + " tulosta");
        //käsitellään saatu data
        try {
            if (cur.moveToFirst()) {
                do {
                    HashMap<String, String> tmp = new HashMap<String, String>();
                    for (int i = 0; i < tableS.size(); i++) {

                        tmp.put(tableS.get(i)[0], cur.getString(i));
                    }

                    pal= header.SetHeader(tmp, pal);
                    pal.add(Tulos.getTulos(tmp));
                } while (cur.moveToNext());

            }
        } finally {
            cur.close();

        }

        ArrayList tagilliset = new ArrayList(Arrays.asList(tagillisetArray));
        addGADListener(pal);
        for(Tulos t : pal)
        {
            if(tagilliset.indexOf(t.getTaulu()) >= 0) {
                //ikävän näköinen kutsu. Avataanpa hieman.
                t.setTagit(getTags( //haetaan tägit. Tarvitaan tuloksen id ja taulun nimi
                        t.getValue( //haetaan tuloksen id sen hashmapistä. Täytyy tuntea avain kentän nimi
                                findPrimaryKeyName( //On olemassa metodi jolla primary key löydetään
                                        t.getTaulu())) //PK:n löytämiseksi tarvitaan tuloksen taulu.
                        , t.getTaulu())); //vielä tarvitaan taulun nimi
            }

        }

        return pal;
    }

    public ArrayList<Tulos> getValueByTag (String tableName, ArrayList<HashMap<String,String>> searchParameters,  ArrayList<HashMap<String,String>> mustHaveTags) {
        ArrayList<Tulos> pal = new ArrayList<>();
        HeaderClass header = new HeaderClass();

        //haetaan tagi taulua vastaavat kohde ja linkki taulu


        ArrayList<String[]> tableS = getStructure(tableName);
        Cursor cur = getTagcursor(tableName ,searchParameters,mustHaveTags);


        try {
            if (cur.moveToFirst()) {
                do {
                    HashMap<String, String> tmp = new HashMap<String, String>();
                    for (int i = 0; i < tableS.size(); i++) {

                        tmp.put(tableS.get(i)[0], cur.getString(i));
                    }
                    pal= header.SetHeader(tmp, pal);
                    pal.add(Tulos.getTulos(tmp));
                } while (cur.moveToNext());

            }
        }catch (Exception e)
        {
            Log.d("virhe","tapahtui virhe getValue by tägissä");
        }
        finally {
            cur.close();
        }

        ArrayList tagilliset = new ArrayList(Arrays.asList(tagillisetArray));
        addGADListener(pal);
        for(Tulos t : pal)
        {
            if(tagilliset.indexOf(t.getTaulu()) >= 0) {
                //ikävän näköinen kutsu. Avataanpa hieman.
                t.setTagit(getTags( //haetaan tägit. Tarvitaan tuloksen id ja taulun nimi
                        t.getValue( //haetaan tuloksen id sen hashmapistä. Täytyy tuntea avain kentän nimi
                                findPrimaryKeyName( //On olemassa metodi jolla primary key löydetään
                                        t.getTaulu())) //PK:n löytämiseksi tarvitaan tuloksen taulu.
                        , t.getTaulu())); //vielä tarvitaan taulun nimi
            }

        }
        return pal;
    }

    public Cursor getTagcursor(String tableName, ArrayList<HashMap<String, String>> searchParameters, ArrayList<HashMap<String, String>> mustHaveTags) {
        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT DISTINCT * FROM(" + getTagiStringi(tableName,true,searchParameters) + " UNION  ALL " + getTagiStringi(tableName,false,searchParameters);

        if(mustHaveTags.size() > 0)
        {
            query += " INTERSECT " + getTagiStringi(tableName,true,mustHaveTags);
        }
        query += ")";

        Log.d("Tag Cursor query",query);
        Cursor pal = db.rawQuery(query, null);
        return pal;
    }

    private String getTagiStringi(String tableName, boolean useIntersection,ArrayList<HashMap<String, String>> searchParameters )
    {
        //haetaan taulut ja idKentät
        String[] tagiTaulut = getTagFields(tableName);
        String kohdeIdKentta = findPrimaryKeyName(tableName);

        if(tagiTaulut[0]== null) return tableName;

        //haetaan taulun kenttien arvot
        ArrayList<String[]> taulunRakenne = getStructure(tableName);
        String halututKentat = "";
        for(int i = 0; i < taulunRakenne.size(); i++)
        {
            halututKentat += "a." + taulunRakenne.get(i)[0];

            if(i < taulunRakenne.size()-1)
            {
                halututKentat += ", ";
            }

        }
        //asetetaan väli operaattori
        String separator = " UNION ";
        if(useIntersection) {
           separator = " INTERSECT ";
        }

        String query = "";
        for(int i = 0; i < searchParameters.size(); i++) {

            //Kommentti koska voin
            /*
            query += "Select "+ halututKentat +" From " + tableName + " a left join " + linkkitaulu + " as ta on (a." + kohdeIdKentta + " = ta._" + tableName + "id)" +
                    " left join " + tagiTaulu + "  as t on (ta._tagid = t._tagid) Where t.nimi like '" + searchParameters.get(i).get("nimi") + "'";
            */
            query += "Select "+ getKentat(tableName) +" From " + tableName + " a left join " + tagiTaulut[1] + " as ta on (a." + kohdeIdKentta + " = ta." + findPrimaryKeyName(tableName) + ")" +
                    " left join " + tagiTaulut[0] + "  as t on (ta._tagid = t._tagid) Where t.nimi like '" + searchParameters.get(i).get("nimi") + "'";
            if(i < searchParameters.size()-1)
            {
                //ei olla viimeisessä, lisätään Intersect
                query += separator;
            }
        }

        if(tableName.compareTo("Kaava")==0){
            query += "ORDER BY mainTag";
        }
        return "SELECT * FROM( " + query + " )";


    }

    //haetaan annettuun tauluun liittyvät tägi ja linkki taulut
    private String[] getTagFields(String tableName)
    {
        String querry = "SELECT tagiTaulu,linkkiTaulu FROM defaultFields WHERE _tname LIKE '" + tableName + "'";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery(querry,null);
        String[] pal = {"ERROR FINDING TAG FIELDS","ERROR FINDING TAG FIELDS"};
        if(cur.moveToFirst())
        {
            pal[0] = cur.getString(0);
            pal[1] = cur.getString(1);
        }
        cur.close();
        //db.close();
        return pal;
    }



    //etsitään nimetyn taulun oletus kentät
    public HashMap<String,String> getDefaultMap(String tableName)
    {
        //suoritetaan haku default names tauluun
        HashMap<String,String> tmp = new HashMap<>();
        tmp.put("_tname",tableName);
        //coidaan käyttää perus getCursor metodia
        Cursor c = getCursor("defaultFields",tmp, new ArrayList<HashMap<String, String>>());
        HashMap<String,String> pal = new HashMap<>();
        if(c.moveToFirst())
        {
            do{
                pal.put(c.getString(1),"");
            }while(c.moveToNext());
        }

        c.close();
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
            cur.close(); //tarvitaanko?
            //db.close();
        }
        return pal;
    }


    public Cursor getCursor(String tableName, HashMap<String, String> searchParameters, ArrayList<HashMap<String,String>> tagit)
    {
        ArrayList<String[]> tableS = getStructure(tableName);

        String source = tableName;
        if(tagit != null && tagit.size() > 0)
        {
            source = getTagiStringi(tableName,true,tagit);
        }
        //luodaan querry
        String query = "Select * from (" + source + ")";
        Log.d("SOURCETEST", source);
        String searchParamsS = "";
        boolean isFirst = true;
        for(int i = 0; i < tableS.size(); i++)
        {
            if(searchParameters.get(tableS.get(i)[0]) != null)
            {
                Log.d("minun","getCursorissa osui " + tableS.get(i)[0] + ": " + searchParameters.get(tableS.get(i)[0]));
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
        Log.d("SERCHPARAMS", searchParamsS);
        if(source.compareTo("Kaava")==0){
            Log.d("IFFIN SIÄLLÄ", source);
            searchParamsS +=" ORDER BY mainTag";
        }
        //toteutetaan haku
        SQLiteDatabase db = getWritableDatabase();
        Cursor pal = db.rawQuery(query + searchParamsS, null); // itse haku täpahtuu tässä
        return pal;
    }

    public void muutaSuosikkiStatus(Tulos t)
    {
        //Luodaan querry.
        String querry = "INSERT INTO ";
        if(!t.isSuosikki)
        {
            querry = "DELETE FROM ";
        }
        querry += t.linkkiTaulu;
        String id = t.getValue(findPrimaryKeyName(t.getTaulu())); //haetaan tuloksen id.
        if(t.isSuosikki)
        {
            querry += " VALUES (" + id + ",(SELECT _tagid FROM "+ t.tagiTaulu +" WHERE nimi LIKE \"suosikki\") )";
        }else{
            querry += " WHERE " + findPrimaryKeyName(t.getTaulu()) + " = " + id + " AND _tagid = (SELECT _tagid FROM "+ t.tagiTaulu +" WHERE nimi LIKE \"suosikki\")";
        }

        Log.d("suosikin muutos","ollaan muuttamassa suosikkia: " + querry);
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(querry);




    }

    //haetaan annettuun ID:hen liittyvät tägit
    private ArrayList<String> getTags(String idVal, String tableName)
    {
        ArrayList<String> pal = new ArrayList<>();

        //haetaan taulut ja idKentät
        String[] tagiTaulut = getTagFields(tableName);
        if(tagiTaulut[0] == null || tagiTaulut[0].compareTo("ERROR FINDING TAG FIELDS") == 0) return null; // Tämä nappaa kiinni mikäli yritetään tägejä tulokselle jolla niitä ei ole


        String query = "SELECT nimi FROM " + tagiTaulut[0] + " AS k JOIN (SELECT * FROM " + tagiTaulut[1] + " WHERE " + findPrimaryKeyName(tableName) + " = " + idVal +" ) as l ON k._tagid = l._tagid";
        //String query = "SELECT nimi FROM " + tagiTaulu + " AS k JOIN (SELECT * FROM " + linkkitaulu + " WHERE _" + tableName + "id = " + idVal +" ) as l ON k._tagid = l._tagid";

        //suoritetaan haku
        SQLiteDatabase db = getWritableDatabase();
        Cursor cur = db.rawQuery(query, null);

        //Luetaan tägit kursorista array listiin

        if(cur.moveToFirst())
        {
            do{
                pal.add(cur.getString(0));
            }while (cur.moveToNext());
        }
        cur.close();
        Log.d("Tagi","id: " + idVal + " table: " + tableName + " number of tags: " + pal.size());
        return pal;
    }

    //estii annetun taulun primary keyn käyttäen pragma table_info komentoa
    private String findPrimaryKeyName(String tName)
    {
        String querry = "pragma table_info(" + tName + ")";
        SQLiteDatabase db = getWritableDatabase();
        Cursor cur = db.rawQuery(querry, null); // itse haku täpahtuu tässä
        boolean jatketaan = true;
        String pal = "";
        try {
            if (cur.moveToFirst()) {
                do {
                    //etsitään se rivi joka vastaa primaryKeytä
                    if(cur.getInt(5) == 1)
                    {
                        pal = cur.getString(1);
                        jatketaan = false;
                    }
                } while (cur.moveToNext() && jatketaan);

            }
        } finally {
            cur.close(); //Ei saa jättää näitä roikkumaan auki. Varsinkin jos on metodi jota kutsutaan useita kertoja haun aikana!
            //db.close();

        }

        return pal;
    }

    //haetaan tulokseen liittyvä ylimääräinen data, esim isotoopit tai kaavojen muuttujat ja vakiot.
    //POTENTIAALINEN VIRHE: mikäli haettavia tuloksia ei haeta getValue tai getValueByTag metodeilla, ei niille aseteta GADlisteneriä! tulee siis spesifisti hoitaa se!
    private void gatherAditionalData(Tulos t)
    {
        //isotooppeja varten
        String symbol = "";
        String prevId = "-1";

            if (t.getType().compareTo("Alkuaineet") == 0) {
                //haetaan alkuaineen isotoopit.
                HashMap<String, String> tmp = new HashMap<>();
                tmp.put("_alkuaineid", t.getValue("_id"));
                ((alkuaineTulos) t).addIsotoopit(getValue("isotoopit", tmp, null));
            } else if (t.getType().compareTo("Kaava") == 0) {


                String kaavaid = t.getValue("_kaavaid");
                ArrayList<String> idt = findMuuttujat(Integer.parseInt(kaavaid)); //haetaan kaavaan liittyvien muuttujien idt
                ArrayList<Tulos> arvot = new ArrayList<>();
                for (String s : idt) {
                    //haetaan id:n pohjalta muuttuja
                    HashMap<String, String> tmp = new HashMap<>();
                    tmp.put("_muuttujaid", s);
                    arvot.addAll(getValue("Muuttuja", tmp, null));
                }
                ((kaavaTulos) t).addMuuttujat(arvot);
                idt = findVakio(Integer.parseInt(kaavaid)); //haetaan kaavaan liittyvien vakion idt
                arvot = new ArrayList<>();
                for (String s : idt) {
                    //haetaan id:n pohjalta muuttuja
                    HashMap<String, String> tmp = new HashMap<>();
                    tmp.put("_vakioid", s);
                    arvot.addAll(getValue("Vakio", tmp, null));
                }
                ((kaavaTulos) t).addVakiot(arvot);
            } else if (t.getType().compareTo("Isotoopit") == 0) {
                //tarkistetaan oliko edellinen isotooppi samasta perus alkuaineesta. jos oli niin ei tarvita uutta hakua
                if (t.getValue("_alkuaineid").compareTo(prevId) != 0) {
                    //Edellinen haettu symboli oli eri id:lle, Joudutaan tekemään haku.
                    HashMap<String, String> tmp = new HashMap<>();
                    prevId = t.getValue("_alkuaineid");
                    tmp.put("_id", prevId);
                    Cursor cur = getCursor("alkuaineet", tmp, null); //tehtävä näin. getValue kutsu aiheuttaisi loputtoman loopin
                    try {
                        if (cur.moveToFirst()) {
                            do {
                                symbol = cur.getString(1);
                            } while (cur.moveToNext());

                        }
                    } finally {
                        cur.close();

                    }
                }
                ((isotooppiTulos) t).setSymbol(symbol);
            } else if (t.getType().compareTo("Funktionaalinenryhma") == 0) {
                //haetaan funktionaalisen ryhmän piikit
                HashMap<String, String> tmp = new HashMap<>();
                tmp.put("_ryhmaid", t.getValue("_ryhmaid"));
                ((funktionaalinenTulos) t).setPiikit(getValue("irpiikit", tmp, null));

            } else if (t.getType().compareTo("ionit") == 0) {
                //annetaan ionille sen suolojen liukoisuudet
                ((ioniTulos) t).setLiukoisuudet(fidSolubility((ioniTulos) t));
            } else if (t.getType().compareTo("aine") == 0) {
                HashMap<String,String> tmp = new HashMap<>();
                ArrayList<Tulos> tmpAL = null;
                if( t.getValue("alkuaineLink") != null)
                {
                    //kyseessä on bulkki alkuaine
                    tmp.put("_id",t.getValue("alkuaineLink"));
                    tmpAL = getValue("alkuaineet",tmp, null);
                }
                ((aineTulos)t).setAlkuaine(tmpAL);

            }else if (t.getType().compareTo("triFunMaster") == 0) {
                //haetaan trigonometristenFunktioiden arvot ja annetaan ne tulokselle.
                HashMap<String,String> tmp = new HashMap<>();
                tmp.put("cos","%"); //tällä pitäs osua kaikkiin asteisiin...
                ((triFunMasterTulos)t).setArvot(getValue("trigonometrisetArvot",tmp,null));

            }else if (t.getType().compareTo("redox") == 0) {
                //haetaan redoxpotentiaalit ja annetaan ne tulokselle.
                HashMap<String,String> tmp = new HashMap<>();
                tmp.put("kaava","%"); //tällä pitäs osua kaikkiin potentiaaleihin
                ((redoxTulos)t).setArvot(getValue("redoxpotentiaali",tmp,null));

            }else if (t.getType().compareTo("yksikot") == 0) {
                //haetaan yksikön tekijät
                ArrayList<Tulos> tmp = getLinkitettytYksikot(Integer.parseInt(t.getValue("_yksikkoid")));
                addGADListener(tmp);
                ((yksikkoTulos) t).addCompound(tmp);
            }else if (t.getType().compareTo("arvo") == 0) {
                //haetaan redoxpotentiaalit ja annetaan ne tulokselle.
                HashMap<String,String> tmp = new HashMap<>();
                tmp.put("materiaali","%");
                ((arvoTulos)t).setArvot(getValue(((arvoTulos) t).getArvoTaulu(),tmp,null));

            }



    }

    //toimiiko näin? onko vals kopio AL?
    private void addGADListener(ArrayList<Tulos> vals)
    {
        if(vals == null) return;
        for(Tulos t: vals)
        {
            t.setGADListener(new gatehrAddDataListener() {
                @Override
                public void findData(Tulos kohde) {
                    gatherAditionalData(kohde);
                }
            });
        }

    }

    //Nämä metodit hakevat kaavan muuttujat tai vakiot
    private ArrayList<String> findMuuttujat(int kaavaId)
    {
        return findLinkitettyKomponentti(kaavaId,true);
    }

    private ArrayList<String> findVakio(int kaavaId)
    {
        return findLinkitettyKomponentti(kaavaId,false);
    }

    private ArrayList<String> findLinkitettyKomponentti(int kaavaid, boolean isMuuttuja)
    {
        ArrayList<String> pal = new ArrayList<>();
        String tableName = "Vakio";
        if(isMuuttuja)
        {
            tableName = "Muuttuja";
        }
        String query = "SELECT k._"+tableName+"id FROM " + tableName + " AS k JOIN (SELECT * FROM " + tableName + "_kaava WHERE _kaavaid = " + kaavaid +" ) as l ON k._"+tableName+"id = l._"+tableName+"id";
        SQLiteDatabase db = getWritableDatabase();
        Cursor cur = db.rawQuery(query, null);

        if(cur.moveToFirst())
        {
            do{
                pal.add(cur.getString(0));
            }while(cur.moveToNext());
        }
        cur.close();
        return pal;

    }

    private String getKentat(String tableName)
    {
        ArrayList<String[]> taulunRakenne = getStructure(tableName);
        String halututKentat = "";
        for(int i = 0; i < taulunRakenne.size(); i++)
        {
            halututKentat += "a." + taulunRakenne.get(i)[0];

            if(i < taulunRakenne.size()-1)
            {
                halututKentat += ", ";
            }

        }
        return halututKentat;
    }

    //tämän tehtävä on hankkia ja lajitella ionitulokset, ei muodostaa suola tuloksia.
    public ArrayList<ioniTulos>[] fidSolubility(ioniTulos it)
    {
        ArrayList<ioniTulos>[] pal = new ArrayList[4];
        String[] liukoisuusTaulut = {"vesiliukoinen","osittainliukoinen","niukkaliukoinen","reagoi"};
        String valintaKentta = "kationiid"; //kenttä joka haetaan liukoisuus taulusta
        String kohdeKentta = "anioniid"; //kenttä joknka perusteella haku tehdään
        if(it.isKationi())
        {
            //varaus on negatiivinen, kyseessä on kationi. Vastin ioni on siis anioni
            valintaKentta = "anioniid";
            kohdeKentta = "kationiid";
        }
        String querry = "";
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<String[]> tableS = getStructure("ionit");
        for(int i = 0; i < liukoisuusTaulut.length; i++) {
            pal[i] = new ArrayList<>();
            querry = "SELECT _ionid, kaava, varaus, nimi, name FROM(SELECT * FROM ionit AS i JOIN (SELECT " + valintaKentta + " FROM " + liukoisuusTaulut[i] + " WHERE " + kohdeKentta + " = " + it.getValue("_ionid") + ") AS l ON i._ionid = l."+ valintaKentta +")";
            Cursor c = db.rawQuery(querry, null);
            if(c.moveToFirst())
            {
                do{
                    HashMap<String, String> tmp = new HashMap<>();
                    for(int j = 0; j < tableS.size(); j++)
                    {
                        tmp.put(tableS.get(j)[0],c.getString(j));
                    }
                    pal[i].add(new ioniTulos(tmp));
                }while (c.moveToNext());
            }
            c.close();
        }
        db.close();
        return pal;
    }

    private ArrayList<Tulos> getLinkitettytYksikot(int yid)
    {
        String querry = "SELECT _yksikkoId,nimi,yksikko,suure FROM (SELECT linkitettyYksikko FROM yksikko_linkki WHERE isantaYksikko = " +yid+ ") AS l,yksikot as y WHERE y._yksikkoId = l.linkitettyYksikko";
        ArrayList<Tulos> pal = null;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(querry,null);
        if(c.moveToFirst())
        {
            pal = new ArrayList<>();
            ArrayList<String[]> tableS = getStructure("yksikot");
            do{
                HashMap<String, String> tmp = new HashMap<>();
                for(int j = 0; j < tableS.size(); j++)
                {
                    tmp.put(tableS.get(j)[0],c.getString(j));
                }
                pal.add(Tulos.getTulos(tmp));
            }while(c.moveToNext());
        }
        c.close();
        db.close();
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

interface gatehrAddDataListener
{
    void findData(Tulos kohde);
}



package com.Kaavapp.android;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

/**
 * Created by janne on 3.11.2016.
 */
public class activityMaster extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected SqlHandler hand;
public static  String language;
    protected int layout; //tähän tallennetaan lapsiLuokassa layoutti mitä käytetään
    protected String actKategoria; //tähän tallennetaan lapsiLuokan kategoria. Lähinnä kaavoja varten
    protected Class classParam;

    protected ListView listView;
    protected String[] listOfReqTags = new String[]{};
    protected String[] listOfTables;
    protected String[] listOfTagTables;

    //näillä palataan takaisin largeViewistä
    protected boolean inLargeView = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullLayoutAlustus();


        hand = new SqlHandler(getApplicationContext().getApplicationContext(), "", null, 1, true);
    }

    //tällä alustetaan näkymä ja mainos.
    private void fullLayoutAlustus()
    {
        this.language = getResources().getConfiguration().locale.getLanguage();
        Log.d("Lokaali", language);

        // register listView's context menu (to delete row)
        // registerForContextMenu(listView);

        // get data with own made queryData method

        setContentView(layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        MobileAds.initialize(getApplicationContext(), "ca-app-pub-4707921089851609~9967794573");


        //ads


        AdView mAdView = (AdView) findViewById(R.id.adViewMainos);
        AdRequest adRequest = new AdRequest.Builder()
                //Kaikki emulaattorit testin vuoksi
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                //Mun oma laiteid testi adien vuoksi (Otetaan pois kun oikeet adit)
                .addTestDevice("358267051453788")
                .build();
        mAdView.loadAd(adRequest);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            //muutetaan standardi metodia niin että se sulkee näppäimistön kun sivupalkki avataan
            public void onDrawerOpened(View drawerView) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(drawerView.getWindowToken(), 0);
                super.onDrawerOpened(drawerView);


            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        listView = (ListView)  findViewById(R.id.lsvTulos);
        //lisätää listViewiin tapahtuma kun klikataan jotakin sen kohtaa
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Context con = getApplicationContext();
                ViewGroup prnt = (ViewGroup) findViewById(R.id.lnlContainer); //haetaan isäntä, eli komponentti mihin tuo tiedot sisältävä komponentti tulee
                if(((Tulos) parent.getItemAtPosition(position)).checkifheader() != true){

                    placeToCenter(((Tulos) parent.getItemAtPosition(position)).getLargeView((LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE), prnt));
                    inLargeView = true;
                }

            }
        });

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        LinearLayout contai = (LinearLayout) findViewById(R.id.lnlContainer);
        View tmp = null;
        if(contai.getChildCount() > 0) {
            tmp = contai.getChildAt(0); //OLETUS: containerissa on kerrallaan vain 1 viewi.
            contai.removeAllViews();
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fullLayoutAlustus();
        } else {
            fullLayoutAlustus();
        }
        if(tmp != null) placeToCenter(tmp);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else if(inLargeView)
        {
            inLargeView = false;
            placeToCenter(listView);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id==R.id.english_language){
            Toast.makeText(this, "Your Language is now English", Toast.LENGTH_LONG).show();

            setLocale("en");
        }
        else if(id==R.id.finnish_language){
            Toast.makeText(this, "Kielesi on nyt suomi", Toast.LENGTH_LONG).show();

            setLocale("fi");
        }

        return super.onOptionsItemSelected(item);
    }



    //Purkkaa. Haku nappi kutsuu tätä funktiota. Tehtiin jotta voidaan pakottaa tägihaun skippaus
    public void Hae(View v) {
        Hae(v,true);
    }

    public void Hae(View v, boolean includeTags) {
        inLargeView = false;
        Log.w("myApp", "Nappia painettu");

        EditText haku = (EditText) findViewById(R.id.edtHakuKentta);
        String hakuparametri = haku.getText().toString();
        if (hakuparametri.matches("")) {
            Toast.makeText(this, getString(R.string.check_input), Toast.LENGTH_SHORT).show();
            return;
        }

        Boolean tarkistus= false;
        StringValidator val = new StringValidator();
        tarkistus = val.CheckString(hakuparametri);

        if(tarkistus) {

            // Tarkistus mistä taulusta haetaan täytyy tehä
            ArrayList<Tulos> tulos = new ArrayList<>();
            if(includeTags)tulos = suoritaHaku(hakuparametri,listOfTagTables,true);
            if(tulos.size()==0 && hakuparametri.length()!=0) {
                tulos = suoritaHaku(hakuparametri, listOfTables, false);
            }
            //Jos haku tyhjä haetaan osahaulla TULEE MUUTTAA YLEISEKSI TAI OTTAA POIS!
            if(tulos.size()==0 && hakuparametri.length()!=0){
                tulos = suoritaHaku("%"+hakuparametri+"%",listOfTables,false);
                for(int i = 0; i < tulos.size(); i++)
                {
                    if(tulos.get(i).getType().compareTo("Alkuaineet") == 0)
                    {
                        ((alkuaineTulos)tulos.get(i)).boldaa(hakuparametri);
                    }
                }
            }

            placeToCenter(listView); //laitetaan listViewi keskelle

            //lisätään tiedot listViewiin näkyville
            resultAdapter a = new resultAdapter(getApplicationContext(), -1, addOtsikot(tulos));
            listView.setAdapter(a);

            //onClick oli tässä


        }else{ Log.w("myApp", tarkistus.toString() );}
    }

    //lisätään väli otsikot
    private ArrayList<Tulos> addOtsikot(ArrayList<Tulos> raaka)
    {
        //luodaan laajennetut vaaditut tägit syötteen ja vaaditujen tägien pohjalta
        String[] siivottuHaku = siivoaHaku(((EditText)findViewById(R.id.edtHakuKentta)).getText().toString());
        String[] laajReqTags = new String[listOfReqTags.length + siivottuHaku.length];
        int j = 0;
        for(String s: listOfReqTags)
        {
            laajReqTags[j] = s;
            j++;
        }for(String s: siivottuHaku)
    {
        laajReqTags[j] = s;
        j++;
    }
        //Tulokset lajitellaan tähän hashmappiin niiden väliotsikon perusteella
        HashMap<String,ArrayList<Tulos>> lajittelu = new HashMap<>();
        String currentOtsikko = "";
        //lajitellaan tulokset hashmappiin
        for(Tulos t : raaka)
        {
               currentOtsikko = t.getCategory(laajReqTags);
            if(lajittelu.get(currentOtsikko) == null) lajittelu.put(currentOtsikko, new ArrayList<Tulos>());
            lajittelu.get(currentOtsikko).add(t);
        }
        //rakennetaan lopullinen arrayListi
        ArrayList<Tulos> pal = new ArrayList<>();
        if(lajittelu.size() == 1) return raaka;
        //laitetaan suosikki kärkeen
        HashMap<String,String> tmp = new HashMap<>();
        String suosik = "suosikki";
        if(lajittelu.get(suosik) == null) suosik = "favorite"; //purkkaa, mutta ei tarvitse tarkistaa kieltä. Jouduttaisiin muutenkin melki hardcodaamaan nuo eri suosikki tägit tässä.
        if(lajittelu.get(suosik) != null)
        {
            tmp.put("nimi",suosik);
            pal.add(new headerTulos(tmp));
            pal.addAll(lajittelu.get(suosik));
            lajittelu.remove(suosik);
        }
        //lisätään seuraavaksi ne tulokset joilla on vain vaadittu tägi
        for(String s: laajReqTags)
        {
            if(lajittelu.get(s) != null)
            {
                tmp = new HashMap<>();
                tmp.put("nimi",s);
                pal.add(new headerTulos(tmp));
                pal.addAll(lajittelu.get(s));
                lajittelu.remove(s);
            }
        }
        //otetaan pienin prioriteetti ja lisätään se listaan.
        //Tämä toteutus on vähn niin ja näin. Olisi varmaan parempi jos järjesteltäisiin nämä kerralla, mieluiten jotain valmista metodia käyttäen.
        String pienin = "";
        int pieninI = Integer.MAX_VALUE;
        Set<String> avaimet = lajittelu.keySet();
        while(lajittelu.size() > 0)
        {
            for(String s: avaimet)
            {
                if(pieninI >= lajittelu.get(s).get(0).getCategoryValue(s))
                {
                    pienin = s;
                    pieninI = lajittelu.get(s).get(0).getCategoryValue(s);
                }
            }
            tmp = new HashMap<>();
            tmp.put("nimi",pienin);
            pal.add(new headerTulos(tmp));
            pal.addAll(lajittelu.get(pienin));
            lajittelu.remove(pienin);
            pienin = "";
            pieninI = Integer.MAX_VALUE;
            avaimet = lajittelu.keySet();
        }
        return pal;
    }

    private String[] siivoaHaku(String hakuparametri)
    {
        String tmpHakuPreP = hakuparametri;
        String tmpHakuPosP = hakuparametri.replaceAll(", ", ",");
        while(tmpHakuPreP.compareTo(tmpHakuPosP) != 0)
        {
            tmpHakuPreP = tmpHakuPosP;
            tmpHakuPosP = tmpHakuPosP.replaceAll(", ", ",");
        }
        tmpHakuPreP = tmpHakuPosP.replaceAll(" ,", ",");
        while(tmpHakuPreP.compareTo(tmpHakuPosP) != 0)
        {
            tmpHakuPreP = tmpHakuPosP;
            tmpHakuPosP = tmpHakuPosP.replaceAll(" ,", ",");
        }
        return tmpHakuPosP.split(",");

    }

    private ArrayList<Tulos> suoritaHaku(String hakuparametri, String[] taulut, boolean isTag)
    {
        HashMap<String, String> kentat;
        String[] splitattuHP = siivoaHaku(hakuparametri);
        ArrayList<HashMap<String, String>> kentatAL = new ArrayList<>();
        Set<String> kentatNimet;
        ArrayList<Tulos> tulos = new ArrayList<>();

        for(String t : taulut)
        {
            kentatAL.clear();
            kentat = hand.getDefaultMap(t);
            if(kentat.size() > 0) {
                kentatNimet = kentat.keySet();
                if(!isTag) {
                    for (String k : kentatNimet) {
                        kentat.put(k, hakuparametri);
                    }
                }else{
                    //kyseessä on tägi haku
                    for(String s: splitattuHP) {
                        HashMap<String, String> tmp = new HashMap<>();

                        while(s.charAt(0) == ' ')
                        {
                            s = s.substring(1,s.length());
                        }

                        while(s.charAt(s.length()-1) == ' ')
                        {
                            s = s.substring(0,s.length()-1);
                        }

                        tmp.put("nimi", s); //Purkka ratkaisu. Vaatii että kaikissa tägi tauluissa tägin nimi kentän nimi on nimi. Vaikeuttaa varmastikkin myös kaksikielisen tägin implementointia.
                        tmp.put("ennimi",s);
                        kentatAL.add(tmp);
                    }
                }

                //luodaan mustHave tägi rajoite lista
                ArrayList<HashMap<String,String>> mstHvTags = new ArrayList<>();
                if((t.compareTo("Kaava") == 0 || t.compareTo("Vakio") == 0) && actKategoria != null)
                {
                    HashMap<String, String> tmp = new HashMap<>();
                    tmp.put("nimi",actKategoria);
                    mstHvTags.add(tmp);
                }
                for(String s : listOfReqTags)
                {
                    HashMap<String, String> tmp = new HashMap<>();
                    tmp.put("nimi",s);
                    mstHvTags.add(tmp);
                }


                if(isTag==true)
                {

                    tulos.addAll(hand.getValueByTag(t, kentatAL, mstHvTags));
                }else {

                    tulos.addAll(hand.getValue(t, kentat,mstHvTags));
                }

                // tulos.addAll(hand.getValue(t, kentat));
            }

        }
        for (Tulos t : tulos) {
            t.setOnSuosikkiToggleListener(new suosikkiToggleListener() {
                @Override

                public void onToggleSuosikki(Tulos t) {

                    hand.muutaSuosikkiStatus(t);
                }
            });

        }


        return tulos;

    }


    //Tässä laitetaan annettu Vievi keskellä olevaan layouttiin
    private void placeToCenter(View target)
    {
        LinearLayout contai = (LinearLayout) findViewById(R.id.lnlContainer);
        contai.removeAllViews();
        contai.addView(target);
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this,classParam); // mitenkäs tämä? parametri?
        startActivity(refresh);
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }
}

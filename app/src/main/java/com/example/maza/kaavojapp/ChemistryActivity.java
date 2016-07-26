package com.example.maza.kaavojapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class ChemistryActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    SqlHandler hand;
    Historia his;
    private ListView listView;

    //näitä käytetään havaitsemaan milloin käyttäjä on löytänyt tyydyttävän tuloksen pelkällä smallViewillä
    private int firstDisplayed= -1;
    private int lastDisplayed = -1;
    private float detectionTime = 1; //Kuinka kauan listViewin on pysyttävä muuttumattomana että oletetaan käyttäjän löytäneen siitä mitä haluttiin

    private int maksimiLooppi;
    private int currentLoop;
    private boolean testi = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // register listView's context menu (to delete row)
        // registerForContextMenu(listView);

        // get data with own made queryData method

        setContentView(R.layout.activity_chemistry_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
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
                his.wasSelected((Tulos) parent.getItemAtPosition(position));
                placeToCenter(((Tulos) parent.getItemAtPosition(position)).getLargeView((LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE), prnt));
            }
        });

        //asetetaan hakukentälle kuuntelija joka katsoo onko listviewi ollu tarpeeksi kauan muuttumatta.
        EditText haku = (EditText) findViewById(R.id.Chemistrysearch);
        assert haku != null;
        haku.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                {
                    Log.d("minun","saatiin focus");
                    resultAdapter ada = (resultAdapter)listView.getAdapter();
                    if(ada == null) return;
                    long lastChanged = ada.getLastChangedTime();
                    if(System.nanoTime() - lastChanged >= detectionTime*Math.pow(10,9) && lastChanged != 0)
                    {
                        Log.d("minune","kulu tarpeeksi aikaa. onFokuksessa");
                        //on kulunut tarpeeksi aikaa. Otetaan first ja last visible muistiin
                        firstDisplayed = listView.getFirstVisiblePosition();
                        lastDisplayed = listView.getLastVisiblePosition();
                    }
                }else{
                    Log.d("minun","menetettiin fokus");
                }
            }
        });

        Bundle b = getIntent().getExtras();
        hand = new SqlHandler(getApplicationContext().getApplicationContext(), "", null, 1, false);
        his = new Historia();
        if(b != null)
        {
            currentLoop = (int) b.get("currentLoop");
            maksimiLooppi = (int) b.get("maximumLoop");
            testi = (boolean)b.get("testia");
        }
        if(testi && currentLoop < maksimiLooppi)
        {
            EditText edt = (EditText)findViewById(R.id.Chemistrysearch);
            edt.setText("ium");
            HaeChemistry(null);
            Intent myIntent = new Intent(this, MainActivity.class);
            myIntent.putExtra("maximumLoop", maksimiLooppi);
            myIntent.putExtra("currentLoop", currentLoop);
            myIntent.putExtra("testia", true);
            myIntent.putExtra("startTime",(long)b.get("startTime"));
            startActivity(myIntent);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chemistry_actitvity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent myIntent = new Intent(this, MainActivity.class);
            startActivity(myIntent);
            finish();
        } else if (id == R.id.nav_gallery) {
            Intent myIntent = new Intent(this, MathActivity.class);
            startActivity(myIntent);

        } else if (id == R.id.nav_slideshow) {
            Intent myIntent = new Intent(this, PhysicsActivity.class);
            startActivity(myIntent);
        } else if (id == R.id.nav_manage) {
            HashMap<String,String> tmpHM = new HashMap<>();
            listView.setAdapter(new resultAdapter(getApplicationContext(), -1, hand.getValue("Alkuaineet",tmpHM)));
            placeToCenter(listView);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void HaeChemistry(View v) {
        Log.w("myApp", "Nappia painettu");
        EditText haku = (EditText) findViewById(R.id.Chemistrysearch);

        haku.clearFocus(); //poistetaan fokus haku kentästä. Fokus osuu thiefViewiin.
        //annetaan osa osumat näkyvissä olleille mikäli tarvetta
        Log.d("minun","verrataan " + firstDisplayed + " " + lastDisplayed);
        if(firstDisplayed != -1) //ei tarvitse tarkistaa lastDisplayediä koska ei ole olemassa tilannetta jossa se olisi nolla... eihän?
        {
            Log.d("minun","Kului tarpeeksi aikaa, annetaan osa osuma");
            for(int i = firstDisplayed; i <= lastDisplayed; i++)
            {
                his.addPartial((Tulos)listView.getItemAtPosition(i));
            }
        }



        String hakuparametri = haku.getText().toString();
        String[] taulut = his.getTables();

        //TESTI OSAA
        if(hakuparametri.compareTo("end") == 0)
        {
            Log.d("minun","aloitetaan lopetus");
            his.writeHistory();
            his.testi();
            return;
        }
        //TESTI OSA LOPPUU

        Boolean tarkistus= false;
        StringValidator val = new StringValidator();
        tarkistus = val.CheckString(hakuparametri);

        if(tarkistus) {

            // Tarkistus mistä taulusta haetaan täytyy tehä
            ArrayList<Tulos> tulos = suoritaHaku(hakuparametri,taulut);

        //Jos haku tyhjä haetaan osahaulla
        if(tulos.size()==0 && hakuparametri.length()!=0){
        tulos = suoritaHaku("%"+hakuparametri+"%",taulut);
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
            resultAdapter a = new resultAdapter(getApplicationContext(), -1, his.sortTulos(tulos));
            listView.setAdapter(a);
            //onClick oli tässä


        }else{ Log.w("myApp", tarkistus.toString() );}
        //his.testi();
    }

    private ArrayList<Tulos> suoritaHaku(String hakuparametri, String[] taulut)
    {
        HashMap<String, String> kentat;
        Set<String> kentatNimet;
        ArrayList<Tulos> tulos = new ArrayList<>();

        for(String t : taulut)
        {
            kentat = hand.getDefaultMap(t);
            if(kentat.size() > 0) {
                kentatNimet = kentat.keySet();
                for (String k : kentatNimet) {
                    kentat.put(k, hakuparametri);
                }
                tulos.addAll(hand.getValue(t, kentat));
            }

        }


        return tulos;

    }


    //Tässä laitetaan annettu Vievi keskellä olevaan layouttiin
    private void placeToCenter(View target)
    {
        firstDisplayed = -1;
        lastDisplayed = -1;
        LinearLayout contai = (LinearLayout) findViewById(R.id.lnlContainer);
        contai.removeAllViews();
        contai.addView(target);
    }

    @Override
    protected void onDestroy() {
        Log.d("minun","tullaan on Destroyhyn");
        his.writeHistory();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}


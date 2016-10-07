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
import android.view.inputmethod.InputMethodManager;
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
import java.util.Set;

public class PhysicsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    SqlHandler hand;
    private ListView listView;
    //idea on että aihealueen valinta perustuu tägeihin. Koska käytännössä kaikki matikan tieto on kaava tai vakio taulussa, ei voida käyttää samaa ratkaisua kuin kemiassa. Tämä systeemi joudutaan ehkä lisäämään myös kemian osalle
    String[] listOfReqTags = new String[]{};

    //näillä palataan takaisin largeViewistä
    boolean inLargeView = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // register listView's context menu (to delete row)
        // registerForContextMenu(listView);

        // get data with own made queryData method

        setContentView(R.layout.activity_physics);
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
                placeToCenter(((Tulos) parent.getItemAtPosition(position)).getLargeView((LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE), prnt));
                inLargeView = true;
            }
        });
        hand = new SqlHandler(getApplicationContext().getApplicationContext(), "", null, 1, true);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(inLargeView)
        {
            inLargeView = false;
            placeToCenter(listView);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.physics, menu);
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

            finish();
        } else if (id == R.id.nav_gallery) {
            Intent myIntent = new Intent(this, MathActivity.class);
            startActivity(myIntent);

            finish();
        } else if (id == R.id.nav_slideshow) {
            Intent myIntent = new Intent(this, ChemistryActivity.class);
            startActivity(myIntent);
            finish();
        } else if (id == R.id.termodynamiikka) {
            ((TextView)findViewById(R.id.txvOtsikko)).setText(getString(R.string.termodynamiikka));
            listOfReqTags = new String[]{"termodynamiikka"};


        } else if (id == R.id.mekaniikka) {
            ((TextView)findViewById(R.id.txvOtsikko)).setText(getString(R.string.mekaniikka));
            listOfReqTags = new String[]{"mekaniikka"};


        }

        ((LinearLayout) findViewById(R.id.lnlContainer)).removeAllViews();
        ((TextView)findViewById(R.id.Physicsearch)).setText("");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void HaePhysics(View v) {
        inLargeView = false;
        Log.w("myApp", " Fysiikka Nappia painettu");

        EditText haku = (EditText) findViewById(R.id.Physicsearch);
        String hakuparametri = haku.getText().toString();

        if (hakuparametri.matches("")) {
            Toast.makeText(this, getString(R.string.check_input), Toast.LENGTH_SHORT).show();
            return;
        }
        String[] listOfTagTables = new String[]{"Kaava","Vakio"};
        String[] listOfTables = new String[]{"Kaava","Muuttuja","Vakio"};

        Boolean tarkistus= false;
        StringValidator val = new StringValidator();
        tarkistus = val.CheckString(hakuparametri);

        if(tarkistus) {

            // Tarkistus mistä taulusta haetaan täytyy tehä
            ArrayList<Tulos> tulos = suoritaHaku(hakuparametri,listOfTagTables,true);
            if(tulos.size()==0 && hakuparametri.length()!=0) {
                tulos = suoritaHaku(hakuparametri, listOfTables, false);
            }
            //Jos haku tyhjä haetaan osahaulla
            if(tulos.size()==0 && hakuparametri.length()!=0){
                tulos = suoritaHaku("%"+hakuparametri+"%",listOfTables,false);
                // for(int i = 0; i < tulos.size(); i++)
                // {
                //    if(tulos.get(i).getType() == 1)
                //   {
                //       ((alkuaineTulos)tulos.get(i)).boldaa(hakuparametri);
                //   }
                // }
            }

            placeToCenter(listView); //laitetaan listViewi keskelle

            //lisätään tiedot listViewiin näkyville
            resultAdapter a = new resultAdapter(getApplicationContext(), -1, tulos);
            listView.setAdapter(a);

            //onClick oli tässä


        }else{ Log.w("myApp", tarkistus.toString() );}
    }

    private ArrayList<Tulos> suoritaHaku(String hakuparametri, String[] taulut, boolean isTag)
    {
        HashMap<String, String> kentat;
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
        ArrayList<HashMap<String, String>> kentatAL = new ArrayList<>();
        Set<String> kentatNimet;
        String[] splitattuHP = tmpHakuPosP.split(",");
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
                        for (String k : kentatNimet) {
                            while(s.charAt(0) == ' ')
                            {
                                s = s.substring(1,s.length());
                            }

                            while(s.charAt(s.length()-1) == ' ')
                            {
                                s = s.substring(0,s.length()-1);
                            }

                            tmp.put(k, s);
                        }
                        kentatAL.add(tmp);
                    }
                }
                ArrayList<HashMap<String,String>> tagit = new ArrayList<>();
                //laitetaan rajoittavat tägit listaan.
                for(String s:listOfReqTags)
                {
                    HashMap<String, String> tmp = new HashMap<>();
                    tmp.put("nimi",s);
                    tagit.add(tmp);
                }

                if(isTag==true)
                {
                    if(t.compareTo("Kaava") == 0 || t.compareTo("Vakio") == 0)
                    {
                        HashMap<String, String> tmp = new HashMap<>();
                        tmp.put("nimi","fysiikka");
                        tagit.add(tmp);
                    }
                    tulos.addAll(hand.getValueByTag(t, kentatAL, tagit));
                }else {
                    if(t.compareTo("Kaava") == 0 || t.compareTo("Vakio") == 0)
                    {
                        HashMap<String, String> tmp = new HashMap<>();
                        tmp.put("nimi","fysiikka");
                        tagit.add(tmp);
                    }
                    tulos.addAll(hand.getValue(t, kentat,tagit));
                }

                // tulos.addAll(hand.getValue(t, kentat));
            }

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



}


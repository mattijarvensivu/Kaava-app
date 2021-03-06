package com.Kaavapp.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


public class PhysicsActivity extends activityMaster {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle(R.string.app_name);
        layout = R.layout.activity_physics;
        actKategoria = "fysiikka";
        classParam = PhysicsActivity.class;

        super.onCreate(savedInstanceState);


        listOfTagTables = new String[]{"yksikot","Kaava","aine","Vakio"};
        listOfTables = new String[]{"Kaava","Vakio","aine","yksikot"};
        listOfReqTags = new String[]{"suosikki"};
        ((EditText) findViewById(R.id.edtHakuKentta)).setText("%");
        Hae(null, false);
        ((EditText) findViewById(R.id.edtHakuKentta)).setText("");
        listOfReqTags = new String[]{};
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.physics, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        boolean haetaan = true;
        listOfTagTables = new String[]{"Kaava","Vakio","aine","yksikot"};
        listOfTables = new String[]{"Kaava","Vakio","aine","yksikot"};
        listOfReqTags = new String[]{};
        ((EditText) findViewById(R.id.edtHakuKentta)).setText("%");

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent myIntent = new Intent(this, MainActivity.class);
            startActivity(myIntent);
            haetaan = false;
            finish();
        }
        else if (id==R.id.physics){
            Intent myIntent = new Intent(this, PhysicsActivity.class);
            startActivity(myIntent);
            haetaan = false;
            finish();
        }
        else if (id == R.id.nav_gallery) {
            Intent myIntent = new Intent(this, MathActivity.class);
            startActivity(myIntent);
            haetaan = false;
            finish();
        } else if (id == R.id.nav_slideshow) {
            Intent myIntent = new Intent(this, ChemistryActivity.class);
            startActivity(myIntent);
            haetaan = false;
            finish();
        }

        else if (id == R.id.suosikit) {

            listOfReqTags = new String[]{"suosikki"};
            //((TextView)findViewById(R.id.txvOtsikko)).setText(getString(R.string.suosikit));
            setTitle(getString(R.string.suosikit));

        }
        else if (id == R.id.termodynamiikka) {
           // ((TextView)findViewById(R.id.txvOtsikko)).setText(getString(R.string.termodynamiikka));
            setTitle(getString(R.string.termodynamiikka));
            listOfTables = new String[]{"Kaava","Vakio"};
            listOfTagTables = new String[]{"Kaava","Vakio"};
            listOfReqTags = new String[]{"termodynamiikka"};


        } else if (id == R.id.harmooninenliike) {
           // ((TextView)findViewById(R.id.txvOtsikko)).setText(getString(R.string.harmooninen_liike));
            setTitle(getString(R.string.harmooninen_liike));
            listOfTables = new String[]{"Kaava","Vakio"};
            listOfTagTables = new String[]{"Kaava","Vakio"};
            listOfReqTags = new String[]{"harmooninen liike"};


        }else if (id == R.id.dynamiikka) {
           // ((TextView)findViewById(R.id.txvOtsikko)).setText(getString(R.string.dynamiikka));
            setTitle(getString(R.string.dynamiikka));
            listOfTables = new String[]{"Kaava","Vakio"};
            listOfTagTables = new String[]{"Kaava","Vakio"};
            listOfReqTags = new String[]{"dynamiikka"};


        }else if (id == R.id.kinematiikka) {
           // ((TextView)findViewById(R.id.txvOtsikko)).setText(getString(R.string.kinematiika));
            setTitle(getString(R.string.kinematiika));
            listOfTables = new String[]{"Kaava","Vakio"};
            listOfTagTables = new String[]{"Kaava","Vakio"};
            listOfReqTags = new String[]{"kinematiikka"};


        }else if (id == R.id.valo) {
           // ((TextView)findViewById(R.id.txvOtsikko)).setText(getString(R.string.valo));
            setTitle(getString(R.string.valo));
            listOfTables = new String[]{"Kaava","Vakio"};
            listOfTagTables = new String[]{"Kaava","Vakio"};
            listOfReqTags = new String[]{"valo"};


        }else if (id == R.id.ydinfysiikka) {
            //((TextView)findViewById(R.id.txvOtsikko)).setText(getString(R.string.ydinfysiikka));
            setTitle(getString(R.string.ydinfysiikka));
            listOfTables = new String[]{"Kaava","Vakio"};
            listOfTagTables = new String[]{"Kaava","Vakio"};
            listOfReqTags = new String[]{"ydinfysiikka"};


        }else if (id == R.id.sahko) {
            //((TextView)findViewById(R.id.txvOtsikko)).setText(getString(R.string.sahko));
            setTitle(getString(R.string.sahko));
            listOfTables = new String[]{"Kaava","Vakio"};
            listOfTagTables = new String[]{"Kaava","Vakio"};
            listOfReqTags = new String[]{"sähkö"};


        }else if (id == R.id.magnetismi) {
           // ((TextView)findViewById(R.id.txvOtsikko)).setText(getString(R.string.magnetismi));
            setTitle(getString(R.string.magnetismi));
            listOfTables = new String[]{"Kaava","Vakio"};
            listOfTagTables = new String[]{"Kaava","Vakio"};
            listOfReqTags = new String[]{"magnetismi"};


        }else if (id == R.id.yksikot) {
           // ((TextView)findViewById(R.id.txvOtsikko)).setText(getString(R.string.yksikot));
            setTitle(getString(R.string.yksikot));
            listOfTables = new String[]{"yksikot"};
            listOfTagTables = new String[]{"yksikot"};
            listOfReqTags = new String[]{};
        }else if (id == R.id.taitekertoimia) {
           // ((TextView)findViewById(R.id.txvOtsikko)).setText(getString(R.string.refraction));
            setTitle(getString(R.string.refraction));
            ((EditText) findViewById(R.id.edtHakuKentta)).setText("taitekertoimia");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        if(haetaan) {
            ((LinearLayout) findViewById(R.id.lnlContainer)).removeAllViews();
            Hae(null,false);
            ((EditText) findViewById(R.id.edtHakuKentta)).setText("");
        }

        return true;
    }


}


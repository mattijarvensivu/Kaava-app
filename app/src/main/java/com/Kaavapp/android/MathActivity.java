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

public class MathActivity extends activityMaster {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layout = R.layout.activity_math;
        actKategoria = "matematiikka";
        classParam = MathActivity.class;
        super.onCreate(savedInstanceState);

        listOfTagTables = new String[]{"Kaava","Vakio"};
        listOfTables = new String[]{"Kaava","Vakio"};
        listOfReqTags = new String[]{"suosikki"};
        ((EditText) findViewById(R.id.edtHakuKentta)).setText("%");
        Hae(null, false);
        ((EditText) findViewById(R.id.edtHakuKentta)).setText("");
        listOfReqTags = new String[]{};
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.math, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        listOfTagTables = new String[]{"Kaava","Vakio"};
        listOfTables = new String[]{"Kaava","Vakio"};
        listOfReqTags = new String[]{};
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        boolean haetaan = true; //suoritetaanko haku "suosikki" tägillä

        if (id == R.id.nav_camera) {
            Intent myIntent = new Intent(this, MainActivity.class);
            startActivity(myIntent);
            haetaan = false;
            finish();

        }
        else if (id==R.id.math){
            Intent myIntent = new Intent(this, MathActivity.class);
            startActivity(myIntent);
            haetaan = false;
            finish();
        }
        else if (id == R.id.nav_gallery) {
            Intent myIntent = new Intent(this, PhysicsActivity.class);
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
            ((TextView)findViewById(R.id.txvOtsikko)).setText(getString(R.string.suosikit));

        }else if (id == R.id.algebra) {
            ((TextView)findViewById(R.id.txvOtsikko)).setText(getString(R.string.algebra));
            listOfReqTags = new String[]{"algebra"};


        } else if (id == R.id.trigonometria) {
            ((TextView)findViewById(R.id.txvOtsikko)).setText(getString(R.string.trigonometria));
            listOfReqTags = new String[]{"trigonometria"};


        } else if (id == R.id.derivointi) {
            ((TextView)findViewById(R.id.txvOtsikko)).setText(getString(R.string.derivointi));
            listOfReqTags = new String[]{"derivointi"};

        }else if (id == R.id.integrointi) {
            ((TextView)findViewById(R.id.txvOtsikko)).setText(getString(R.string.integrointi));
            listOfReqTags = new String[]{"integrointi"};

        }else if (id == R.id.vektori) {
            ((TextView)findViewById(R.id.txvOtsikko)).setText(getString(R.string.vektori));
            listOfReqTags = new String[]{"vektori"};

        }else if (id == R.id.tilastotiede) {
            ((TextView)findViewById(R.id.txvOtsikko)).setText(getString(R.string.tilastotiede));
            listOfReqTags = new String[]{"tilastotiede"};

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        if(haetaan) {
            ((LinearLayout) findViewById(R.id.lnlContainer)).removeAllViews();
            ((EditText) findViewById(R.id.edtHakuKentta)).setText("%"); //ongelma, tehdään tägi haku. Pitäisi forsettaa nimi haku.
            Hae(null,false);
            ((EditText) findViewById(R.id.edtHakuKentta)).setText("");
        }

        return true;
    }

}


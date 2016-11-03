package com.Kaavapp.android;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

public class ChemistryActivity extends activityMaster {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layout = R.layout.activity_chemistry_activity;
        actKategoria = "kemia";
        classParam = ChemistryActivity.class;
        super.onCreate(savedInstanceState);


        listOfTagTables = new String[]{"Alkuaineet","Funktionaalinenryhma","Kaava","Vakio","Hapot","aine"};
        listOfTables = new String[]{"Alkuaineet","Funktionaalinenryhma","Hapot","Isotoopit","Kaava","Muuttuja","Vakio","ionit","aine"};
        listOfReqTags = new String[]{};


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chemistry_actitvity, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        boolean haetaan = true;
        listOfTagTables = new String[]{"Alkuaineet","Funktionaalinenryhma","Kaava","Vakio","Hapot","aine"};
        listOfTables = new String[]{"Alkuaineet","Funktionaalinenryhma","Hapot","Isotoopit","Kaava","Muuttuja","Vakio","ionit","aine"};
        listOfReqTags = new String[]{};
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            haetaan = false;
            finish();

        } else if (id == R.id.nav_gallery) {
            Intent myIntent = new Intent(this, MathActivity.class);
            startActivity(myIntent);
            haetaan = false;
            finish();

        } else if (id == R.id.nav_slideshow) {
            Intent myIntent = new Intent(this, PhysicsActivity.class);
            startActivity(myIntent);
            haetaan = false;
            finish();
        } else if (id == R.id.nav_camera) {



        } else if (id == R.id.Alkuaineet) {
            listOfTagTables = new String[]{"Alkuaineet"};
            listOfTables = new String[]{"Alkuaineet"};
            ((TextView)findViewById(R.id.txvOtsikko)).setText(getString(R.string.chemistry_elements));


        } else if (id == R.id.FnkRyhma) {
            listOfTagTables = new String[]{"Funktionaalinenryhma"};
            listOfTables = new String[]{"Funktionaalinenryhma"};
            ((TextView)findViewById(R.id.txvOtsikko)).setText(getString(R.string.chemistry_functional));

        }else if (id == R.id.Kaavat) {
            listOfTagTables = new String[]{"Kaava"};
            listOfTables = new String[]{"Kaava"};
            ((TextView)findViewById(R.id.txvOtsikko)).setText(getString(R.string.formulas));

        }else if (id == R.id.Vakiot) {
            listOfTagTables = new String[]{"Vakio"};
            listOfTables = new String[]{"Vakio"};
            ((TextView)findViewById(R.id.txvOtsikko)).setText(getString(R.string.vakiot));

        }else if (id == R.id.Hapot) {
            listOfTagTables = new String[]{"Hapot"};
            listOfTables = new String[]{"Hapot"};
            ((TextView)findViewById(R.id.txvOtsikko)).setText(getString(R.string.hapot));

        }else if (id == R.id.Suolat) {
            listOfTagTables = new String[]{};
            listOfTables = new String[]{"ionit"};
            ((TextView)findViewById(R.id.txvOtsikko)).setText(getString(R.string.ionit));

        }else if (id == R.id.Aineet) {
            listOfTagTables = new String[]{"aine"};
            listOfTables = new String[]{"aine"};
            ((TextView)findViewById(R.id.txvOtsikko)).setText(getString(R.string.aine));

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


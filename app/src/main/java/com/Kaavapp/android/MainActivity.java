package com.Kaavapp.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MainActivity extends activityMaster {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle(R.string.app_name);
        layout = R.layout.activity_main;
        //actKategoria = null;
        classParam = MainActivity.class;
        super.onCreate(savedInstanceState);
        listOfTagTables = new String[]{"Alkuaineet", "Funktionaalinenryhma", "Kaava", "Vakio", "Hapot", "aine"};
        listOfTables = new String[]{"Alkuaineet", "Funktionaalinenryhma", "Hapot", "Kaava", "Vakio", "aine", "yksikot"};
        listOfReqTags = new String[]{"suosikki"};
        ((EditText) findViewById(R.id.edtHakuKentta)).setText("%");
        Hae(null, false);
        ((EditText) findViewById(R.id.edtHakuKentta)).setText("");
        listOfReqTags = new String[]{};
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        boolean haetaan = true;
        listOfTagTables = new String[]{"Alkuaineet", "Funktionaalinenryhma", "Kaava", "Vakio", "Hapot", "aine"};
        listOfTables = new String[]{"Alkuaineet", "Funktionaalinenryhma", "Hapot", "Kaava", "Vakio", "aine", "yksikot"};
        listOfReqTags = new String[]{};
        ((EditText) findViewById(R.id.edtHakuKentta)).setText("%");
        int id = item.getItemId();
        if (id == R.id.front_page) {

            Intent myIntent = new Intent(this, MainActivity.class);
            haetaan = false;
            startActivity(myIntent);
            finish();

        }
        else if (id == R.id.nav_camera) {
            // Handle the camera action
            Intent myIntent = new Intent(this, MathActivity.class);
            startActivity(myIntent);
            haetaan = false;
            finish();
        } else if (id == R.id.nav_gallery) {

            Intent myIntent = new Intent(this, PhysicsActivity.class);
            haetaan = false;
            startActivity(myIntent);
            finish();

        } else if (id == R.id.nav_slideshow) {
            Intent myIntent = new Intent(this, ChemistryActivity.class);
            haetaan = false;
            startActivity(myIntent);
            finish();
        } else if (id == R.id.suosikit) {

            listOfReqTags = new String[]{"suosikki"};
            ((TextView)findViewById(R.id.txvOtsikko)).setText(getString(R.string.suosikit));

        }else if(id==R.id.tietoa){

            LinearLayout contai = (LinearLayout) findViewById(R.id.lnlContainer);
            contai.removeAllViews();
            LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
            View theInflatedView = inflater.inflate(R.layout.info, null);
            contai.addView(theInflatedView);
            haetaan = false;
            ((EditText) findViewById(R.id.edtHakuKentta)).setText("");


        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);


        if (haetaan) {
            ((LinearLayout) findViewById(R.id.lnlContainer)).removeAllViews();
            Hae(null, false);
            ((EditText) findViewById(R.id.edtHakuKentta)).setText("");
        }


        return true;
    }

}


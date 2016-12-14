package com.Kaavapp.android;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import com.google.android.gms.ads.MobileAds;

import java.util.Locale;

public class MainActivity extends activityMaster {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        if (id == R.id.nav_camera) {
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


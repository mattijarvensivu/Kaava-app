package com.Kaavapp.android;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.DisplayMetrics;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

public class PhysicsActivity extends activityMaster {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layout = R.layout.activity_physics;
        actKategoria = "fysiikka";
        classParam = PhysicsActivity.class;

        super.onCreate(savedInstanceState);


        listOfTagTables = new String[]{"yksikot","Kaava","aine","Vakio"};
        listOfTables = new String[]{"Kaava","Vakio","aine","yksikot"};
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

            haetaan = false;
            finish();
        } else if (id == R.id.nav_gallery) {
            Intent myIntent = new Intent(this, MathActivity.class);
            startActivity(myIntent);
            haetaan = false;
            finish();
        } else if (id == R.id.nav_slideshow) {
            Intent myIntent = new Intent(this, ChemistryActivity.class);
            startActivity(myIntent);
            haetaan = false;
            finish();
        } else if (id == R.id.termodynamiikka) {
            ((TextView)findViewById(R.id.txvOtsikko)).setText(getString(R.string.termodynamiikka));
            listOfTables = new String[]{"Kaava","Vakio"};
            listOfTagTables = new String[]{"Kaava","Vakio"};
            listOfReqTags = new String[]{"termodynamiikka"};


        } else if (id == R.id.harmooninenliike) {
            ((TextView)findViewById(R.id.txvOtsikko)).setText(getString(R.string.harmooninen_liike));
            listOfTables = new String[]{"Kaava","Vakio"};
            listOfTagTables = new String[]{"Kaava","Vakio"};
            listOfReqTags = new String[]{"harmooninen liike"};


        }else if (id == R.id.dynamiikka) {
            ((TextView)findViewById(R.id.txvOtsikko)).setText(getString(R.string.dynamiikka));
            listOfTables = new String[]{"Kaava","Vakio"};
            listOfTagTables = new String[]{"Kaava","Vakio"};
            listOfReqTags = new String[]{"dynamiikka"};


        }else if (id == R.id.kinematiikka) {
            ((TextView)findViewById(R.id.txvOtsikko)).setText(getString(R.string.kinematiika));
            listOfTables = new String[]{"Kaava","Vakio"};
            listOfTagTables = new String[]{"Kaava","Vakio"};
            listOfReqTags = new String[]{"kinematiikka"};


        }else if (id == R.id.valo) {
            ((TextView)findViewById(R.id.txvOtsikko)).setText(getString(R.string.valo));
            listOfTables = new String[]{"Kaava","Vakio"};
            listOfTagTables = new String[]{"Kaava","Vakio"};
            listOfReqTags = new String[]{"valo"};


        }else if (id == R.id.ydinfysiikka) {
            ((TextView)findViewById(R.id.txvOtsikko)).setText(getString(R.string.ydinfysiikka));
            listOfTables = new String[]{"Kaava","Vakio"};
            listOfTagTables = new String[]{"Kaava","Vakio"};
            listOfReqTags = new String[]{"ydinfysiikka"};


        }else if (id == R.id.sahko) {
            ((TextView)findViewById(R.id.txvOtsikko)).setText(getString(R.string.sahko));
            listOfTables = new String[]{"Kaava","Vakio"};
            listOfTagTables = new String[]{"Kaava","Vakio"};
            listOfReqTags = new String[]{"sähkö"};


        }else if (id == R.id.magnetismi) {
            ((TextView)findViewById(R.id.txvOtsikko)).setText(getString(R.string.magnetismi));
            listOfTables = new String[]{"Kaava","Vakio"};
            listOfTagTables = new String[]{"Kaava","Vakio"};
            listOfReqTags = new String[]{"magnetismi"};


        }else if (id == R.id.yksikot) {
            ((TextView)findViewById(R.id.txvOtsikko)).setText(getString(R.string.yksikot));
            listOfTables = new String[]{"yksikot"};
            listOfTagTables = new String[]{"yksikot"};
            listOfReqTags = new String[]{};
        }else if (id == R.id.taitekertoimia) {
            ((TextView)findViewById(R.id.txvOtsikko)).setText(getString(R.string.refraction));
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


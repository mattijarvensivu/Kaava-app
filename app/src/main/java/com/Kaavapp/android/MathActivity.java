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

public class MathActivity extends activityMaster {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layout = R.layout.activity_math;
        actKategoria = "matematiikka";
        classParam = MathActivity.class;
        super.onCreate(savedInstanceState);

        listOfTagTables = new String[]{"Kaava","Vakio"};
        listOfTables = new String[]{"Kaava","Vakio"};
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
            haetaan = false;
            finish();

        } else if (id == R.id.nav_gallery) {
            Intent myIntent = new Intent(this, PhysicsActivity.class);
            startActivity(myIntent);
            haetaan = false;
            finish();

        } else if (id == R.id.nav_slideshow) {
            Intent myIntent = new Intent(this, ChemistryActivity.class);
            startActivity(myIntent);
            haetaan = false;
            finish();
        } else if (id == R.id.algebra) {
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


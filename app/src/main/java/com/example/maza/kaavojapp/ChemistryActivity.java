package com.example.maza.kaavojapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class ChemistryActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private SQLiteDatabase db;
    private ListView listView;

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
       listView = (ListView)  findViewById(R.id.listViewtest);
        Testiquery();
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
        } else if (id == R.id.nav_gallery) {
            Intent myIntent = new Intent(this, MathActivity.class);
            startActivity(myIntent);

        } else if (id == R.id.nav_slideshow) {
            Intent myIntent = new Intent(this, PhysicsActivity.class);
            startActivity(myIntent);
        } else if (id == R.id.nav_manage) {

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
        String hakuparametri = haku.getText().toString();
        SqlHandler handler = new SqlHandler(getApplicationContext().getApplicationContext(), "", null, 1, true);

        // Tarkistus mistä taulusta haetaan täytyy tehä
        String tablename = "Alkuaineet";
        HashMap<String, String> kentat;

        kentat = handler.getParamMap(tablename);
        kentat.put("nimi", hakuparametri);

        ArrayList<HashMap<String, String>> tulos;
        tulos = handler.getValue(tablename, kentat);
        String tulox = tulos.get(0).get("symbol");
        Log.w("myApp", "ennen iffiä");
        if (tulos.size() != 0) {
            Log.w("myApp", "pitäsi tulostaa");
            Toast.makeText(this, tulox, Toast.LENGTH_LONG).show();
        } else {
            Log.w("myApp", "tyhjää");
            Toast.makeText(this, "Tyhjä", Toast.LENGTH_LONG).show();
        }

    }


    public void Testiquery(){

        //SQLiteDatabase db = getWritableDatabase();
        db = (new SqlHandler(getApplicationContext().getApplicationContext(), "", null, 1, true)).getWritableDatabase();
        Cursor cursor = db.rawQuery("Select _id, symbol, nimi, jarjestyluku from Alkuaineet ORDER BY jarjestyluku", null);
        String[] resultColumns = {"_id","symbol","nimi", "jarjestyluku"};
        cursor = db.query("Alkuaineet",resultColumns,null,null,null,null,"jarjestyluku ASC",null);

        ListAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.list_item, cursor,
                new String[] {"jarjestyluku","symbol", "nimi" },      // from
                new int[] {R.id.jarjestys,R.id.symbol, R.id.nimi }    // to
                ,0);  // flags

        TextView columnHeader1 = (TextView) findViewById(R.id.column_header1);
        TextView columnHeader2 = (TextView) findViewById(R.id.column_header2);
        TextView columnHeader3 = (TextView) findViewById(R.id.column_header3);
        columnHeader1.setText("Järjestysluku");
        columnHeader2.setText("Symbol");
        columnHeader3.setText("Nimi");

        listView.setAdapter(adapter);
    }
    

}


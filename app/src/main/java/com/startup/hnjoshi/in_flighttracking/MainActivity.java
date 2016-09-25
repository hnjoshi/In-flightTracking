package com.startup.hnjoshi.in_flighttracking;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;

import static android.R.attr.value;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String DEFAULT="N/A";

    // texts that changes dynamically
    private TextView accuracy;
    private TextView speed;
    private TextView altitude;

    // user preferences variable, initially it will be all default values
    private String speed_unit = "imperial";
    private String altitude_unit = "imperial";
    private int update_interval = 3;    // 3 sec

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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


        accuracy = (TextView)findViewById(R.id.accuracyTextView);
        speed = (TextView)findViewById(R.id.speedTextView);
        altitude = (TextView)findViewById(R.id.altitudeTextView);
    }

    // When user starts to see the UI
    @Override
    protected void onStart(){
        super.onStart();


    }

    // When user return to the activity
    @Override
    protected void onResume() {
        super.onResume();

        // 1. Load previously stored settings or newly modified settings
        loadSettings();
        // Tester - working verified
        Log.d("Speed Unit", this.speed_unit);
        Log.d("Altitude Unit", this.altitude_unit);
        Log.d("Update interval", this.update_interval+"");

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
        getMenuInflater().inflate(R.menu.main, menu);
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
            Intent myIntent = new Intent(MainActivity.this, Settings.class);
            // myIntent.putExtra("key", value); //Optional parameters
            MainActivity.this.startActivity(myIntent);
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
            // Handle the camera action
        } else if (id == R.id.nav_map) {
            Intent myIntent = new Intent(MainActivity.this, MapsActivity.class);
            // myIntent.putExtra("key", value); //Optional parameters
            MainActivity.this.startActivity(myIntent);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_settings) {
            Intent myIntent = new Intent(MainActivity.this, Settings.class);
            // myIntent.putExtra("key", value); //Optional parameters
            MainActivity.this.startActivity(myIntent);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Loading user Settings if available
    private void loadSettings(){
        //SharedPreference Storage for user preferred settings
        SharedPreferences sharedPreferences = getSharedPreferences("In-flightTrackingData", Context.MODE_PRIVATE);
        // if the key does not exist or unavailable then DEFAULT value is assigned to variable
        // if the key exist or available then its corresponding value is assigned to variable
        String speed_unit = sharedPreferences.getString("speed_unit", DEFAULT);
        String altitude_unit = sharedPreferences.getString("altitude_unit", DEFAULT);
        String update_interval = sharedPreferences.getString("update_interval", DEFAULT);

        // 1. DEFAULT value check - User may not have accessed settings yet
        // 2. Stored value check - User may have modified speed unit settings
        if(!speed_unit.equals(DEFAULT) && !speed_unit.equals(this.speed_unit)){
            this.speed_unit = speed_unit;
        }

        // 1. DEFAULT value check - User may not have accessed settings yet
        // 2. Stored value check - User may have modified altitude unit settings
        if(!altitude_unit.equals(DEFAULT) && !altitude_unit.equals(this.altitude_unit)){
            this.altitude_unit = altitude_unit;
        }
        // 1. DEFAULT value check - User may not have accessed settings yet
        // 2. Stored value check - User may have modified update interval settings
        if(!update_interval.equals(DEFAULT) && !update_interval.equals(this.update_interval)){
            this.update_interval = Integer.parseInt(update_interval);
        }
    }
}
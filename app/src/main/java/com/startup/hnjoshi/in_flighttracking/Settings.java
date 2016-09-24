package com.startup.hnjoshi.in_flighttracking;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.SeekBar;

public class Settings extends AppCompatActivity {

    public static final String DEFAULT="N/A";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private RadioGroup speedUnitsRadioGroup;
    private RadioGroup altitudeUnitsRadioGroup;

    private SeekBar update_interval_seekbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        speedUnitsRadioGroup = (RadioGroup)findViewById(R.id.speedUnitsRadioGroup);
        altitudeUnitsRadioGroup = (RadioGroup)findViewById(R.id.altitudeUnitsRadioGroup);
        update_interval_seekbar = (SeekBar)findViewById(R.id.update_interval_seekbar);

        //SharedPreference Storage for user preferred settings
        sharedPreferences = getSharedPreferences("In-flightTrackingData", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //load user settings if exist In-flightTrackingData.xml file exist
        loadUserSettings();

        // Listeners for radio button and seek bar
        SpeedUnitsListener();
        AltitudeUnitsListener();
        updateIntervalListener();
    }

    private void loadUserSettings() {
        // if the key does not exist or unavailable then DEFAULT value is assigned to variable
        // if the key exist or available then its corresponding value is assigned to variable
        String speed_unit = sharedPreferences.getString("speed_unit", DEFAULT);
        String altitude_unit = sharedPreferences.getString("altitude_unit", DEFAULT);
        String update_interval = sharedPreferences.getString("update_interval", DEFAULT);

        // if the value is not DEFAULT then we apply the user settings (Speed units) to activity
        if(!speed_unit.equals(DEFAULT)){
            if(speed_unit.equals("imperial")){
                speedUnitsRadioGroup.check(R.id.speed_unit_imperial);
            }
            else if(speed_unit.equals("metric")){
                speedUnitsRadioGroup.check(R.id.speed_unit_metric);
            }
            else {
                speedUnitsRadioGroup.check(R.id.speed_unit_knot);
            }
        }

        // if the value is not DEFAULT then we apply the user settings (Altitude units) to activity
        if(!altitude_unit.equals(DEFAULT)){
            if(altitude_unit.equals("imperial")){
                altitudeUnitsRadioGroup.check(R.id.altitude_unit_imperial);
            }
            else if(altitude_unit.equals("metric")){
                altitudeUnitsRadioGroup.check(R.id.altitude_unit_metric);
            }
            else {
                altitudeUnitsRadioGroup.check(R.id.altitude_unit_flightLevel);
            }
        }

        // if the value is not DEFAULT then we apply the user settings (update interval) to activity
        if(!update_interval.equals(DEFAULT)){
            update_interval_seekbar.setProgress(Integer.parseInt(update_interval));
        }
    }

    private void SpeedUnitsListener() {
        speedUnitsRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.speed_unit_imperial:
                        //Toast.makeText(getApplicationContext(), "You choose Imperial from speed", Toast.LENGTH_SHORT).show();
                        editor.putString("speed_unit", "imperial");
                        flagAsModified();
                        editor.commit();
                        break;
                    case R.id.speed_unit_metric:
                        //Toast.makeText(getApplicationContext(), "You choose Metric from speed", Toast.LENGTH_SHORT).show();
                        editor.putString("speed_unit", "metric");
                        flagAsModified();
                        editor.commit();
                        break;
                    case R.id.speed_unit_knot:
                        //Toast.makeText(getApplicationContext(), "You choose Knot from speed", Toast.LENGTH_SHORT).show();
                        editor.putString("speed_unit", "knot");
                        flagAsModified();
                        editor.commit();
                        break;
                }
            }
        });
    }

    private void AltitudeUnitsListener() {
        altitudeUnitsRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.altitude_unit_imperial:
                        //Toast.makeText(getApplicationContext(), "You choose Imperial from altitude", Toast.LENGTH_SHORT).show();
                        editor.putString("altitude_unit", "imperial");
                        flagAsModified();
                        editor.commit();
                        break;
                    case R.id.altitude_unit_metric:
                        //Toast.makeText(getApplicationContext(), "You choose Metric from altitude", Toast.LENGTH_SHORT).show();
                        editor.putString("altitude_unit", "metric");
                        flagAsModified();
                        editor.commit();
                        break;
                    case R.id.altitude_unit_flightLevel:
                        //Toast.makeText(getApplicationContext(), "You choose Flight Level from altitude", Toast.LENGTH_SHORT).show();
                        editor.putString("altitude_unit", "flightLevel");
                        flagAsModified();
                        editor.commit();
                        break;
                }
            }
        });
    }

    private void updateIntervalListener() {
        update_interval_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            String progress;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                this.progress = Integer.toString(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                editor.putString("update_interval", progress);
                flagAsModified();
                editor.commit();
            }
        });
    }

    private void flagAsModified(){
        editor.putString("settings_modified", "true");
    }

}
package com.startup.hnjoshi.in_flighttracking;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    public static final String DEFAULT = "N/A";

    // texts that changes dynamically
    private TextView accuracy;
    private TextView speed;
    private TextView altitude;

    // user preferences variable, initially it will be all default values
    private String speed_unit = "imperial";
    private String altitude_unit = "imperial";
    private int update_interval = 3;    // 3 sec

    private LocationManager locationManager;
    private GoogleMap mMap;
    private MarkerOptions markerOptions;

    private double x,y = 0;     // latitude and logitude
    private float z, zoom = 0;  // bearing and zoom level

    private boolean appLaunched = true;     // app was just started by user

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        accuracy = (TextView) findViewById(R.id.accuracyTextView);
        speed = (TextView) findViewById(R.id.speedTextView);
        altitude = (TextView) findViewById(R.id.altitudeTextView);
    }

    // When user starts to see the UI
    @Override
    protected void onStart() {
        super.onStart();

        // executed only first time when user starts the app
        markerOptions = new MarkerOptions().position(new LatLng(x, y)).rotation(z);
        // make icon appear in the center
        markerOptions.anchor(0.5f, 0.5f);
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
        Log.d("Update interval", this.update_interval + "");

        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, this.update_interval * 1000, 0, (LocationListener) this);
        }
        catch (Exception e){

        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
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

        if(!altitude_unit.equals(DEFAULT) && !altitude_unit.equals(this.altitude_unit)){
            this.altitude_unit = altitude_unit;
        }

        if(!update_interval.equals(DEFAULT) && !update_interval.equals(this.update_interval)){
            this.update_interval = Integer.parseInt(update_interval);
        }
    }

    private void updateSpeed(Location location){
        if(this.speed_unit.equals("imperial")){
            speed.setText(Math.round(location.getSpeed()/0.44704)+" mph");
        }
        else if(this.speed_unit.equals("metric")){
            speed.setText(Math.round(location.getSpeed()/0.2777778)+" km/h");
        }
        else{
            speed.setText(Math.round(location.getSpeed()/0.5144444)+" kn");
        }
    }

    private void updateAltitude(Location location){
        if(this.altitude_unit.equals("imperial")){
            altitude.setText(Math.round(location.getAltitude()*3.28084)+" feet");
        }
        else if(this.altitude_unit.equals("metric")){
            altitude.setText(Math.round(location.getAltitude())+" m");
        }
        else{

        }
    }

    @Override
    public void onLocationChanged(Location location) {

        x = location.getLatitude();
        y = location.getLongitude();
        z = location.getBearing();

        LatLng latLng = new LatLng(x, y);

        // Add the marker at new Location
        // 1. Remove old marker
        mMap.clear();
        // 2. Set marker position on map
        markerOptions.position(latLng).rotation(z);
        // 3. Set marker icon
        // markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable));
        // 4. Add marker to map
        mMap.addMarker(markerOptions);

        if(appLaunched){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
            appLaunched = false;
        }

        // Update google map camera
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

        // Update speed
        updateSpeed(location);
        // Update altitude
        updateAltitude(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}

package com.example.fris_o;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    Marker marker;
    LocationListener locationListener;
    Random rand = new Random();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {

            locationListener = new LocationListener() {

                @Override
                public void onLocationChanged(Location location) {


                    //TODO: add player marker to onCreate and onLocationChanged
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    mMap.clear();

                    drawPlayer(latitude, longitude);
                    //drawGameCircle(latitude, longitude, 10);

                    LatLng latLng = new LatLng(latitude, longitude);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));


                    /*
                    //get the location name from latitude and longitude
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try {
                        not in use yet, can be used to show address name
                         List<Address> addresses =
                        geocoder.getFromLocation(latitude, longitude, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    */

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
            };
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);
        }
    }

    private void drawGameCircle(double latitude, double longitude, int difficulty){
        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(latitude, longitude))
                .radius(difficulty)
                .strokeWidth(10)
                .fillColor(Color.argb(10, 225, 0, 0))
                .strokeColor(Color.argb(100, 225, 0, 0));
        Circle circle = mMap.addCircle(circleOptions);
    }

    private void drawPlayer(double latitude, double longitude) {
        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(latitude, longitude))
                .radius(1)
                .strokeWidth(10)
                .fillColor(Color.argb(255, 205, 90, 0))
                .strokeColor(Color.argb(255, 225, 128, 0));
        Circle circle = mMap.addCircle(circleOptions);
    }

    private void drawOtherPlayers(double latitude, double longitude) {
        int rred = rand.nextInt(150);
        int rgreen = rand.nextInt(150);
        int rblue = rand.nextInt(150);

        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(latitude, longitude))
                .radius(1)
                .strokeWidth(10)
                .fillColor(Color.argb(255, rred, rgreen, rblue))
                .strokeColor(Color.argb(255, (rred+50), (rgreen+50), (rblue+50)));
        Circle circle = mMap.addCircle(circleOptions);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    //get the location name from latitude and longitude
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try {
                        List<Address> addresses =
                                geocoder.getFromLocation(latitude, longitude, 1);
                        String result = addresses.get(0).getLocality() + ":";
                        result += addresses.get(0).getCountryName();
                        LatLng latLng = new LatLng(latitude, longitude);
                        if (marker != null) {
                            marker.remove();
                            marker = mMap.addMarker(new MarkerOptions().position(latLng).title(result));
                            mMap.setMinZoomPreference(18f);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f));
                        } else {
                            marker = mMap.addMarker(new MarkerOptions().position(latLng).title(result));
                            mMap.setMinZoomPreference(18f);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f));
                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
            };
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
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

        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(this, R.raw.raw));

            if (!success) {
                Log.e("MapsActivity", " Style parsing failed");
            }
        } catch (Resources.NotFoundException e) {
                Log.e("MapsActivity", " Can't find style. Error:", e);
            }


        mMap.setMinZoomPreference(20f);
        UiSettings muiSettings = mMap.getUiSettings();
        muiSettings.setZoomControlsEnabled(true);
        muiSettings.setZoomGesturesEnabled(true);
        muiSettings.setScrollGesturesEnabled(false);
    }

    @Override
    protected void onStop() {
        super.onStop();
        locationManager.removeUpdates(locationListener);
    }

    public void GoToMenu(View view){
        Intent i = new Intent(this, Menu_and_settings.class);
        startActivity(i);
    }
}
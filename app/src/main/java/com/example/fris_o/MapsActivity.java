package com.example.fris_o;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.VolleyError;
import com.example.fris_o.data.DBHandler;
import com.example.fris_o.models.Games;
import com.example.fris_o.models.Users;
import com.example.fris_o.tools.IResult;
import com.example.fris_o.tools.OnlineQueries;
import com.example.fris_o.tools.VolleyService;
import com.example.fris_o.ui.Menu_and_settings;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;
import java.util.Random;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    LocationListener locationListener;
    Random rand = new Random();
    static boolean first = false;
    SharedPreferences preferences;
    String userStatus;


    Context ctx = this;
    DBHandler db = new DBHandler(this);

    OnlineQueries query = new OnlineQueries(ctx, db);

    Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        preferences = ctx.getSharedPreferences("User_status", 0);
        long userID = preferences.getLong("userID", 0);
        userStatus = preferences.getString("status", null);

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

                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    LatLng latLng = new LatLng(latitude, longitude);
                    //Gats all games based on the user's lccation and adds them to a local database
                    query.getNearbyGames(latitude, longitude);

                    CameraPosition cameraPosition = new CameraPosition.Builder().
                            target(latLng).
                            tilt(45).
                            zoom((float) 19.8).
                            bearing(0).
                            build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    query.sendUserLocation(latitude, longitude);
                    mMap.clear();

                    if (!first){
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        first = true;}

                   if(userStatus == "ingame") {drawCanvasIngame();drawOtherPlayers();}
                   else drawCanvasOnline();


                    drawPlayer(latitude, longitude);


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
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 1, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1, locationListener);
        }
    }

    private void drawGameCircle(double latitude, double longitude, int difficulty, boolean ingame){
        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(latitude, longitude))
                .radius(difficulty)
                .strokeWidth(10)
                .fillColor(Color.argb(10, 225, 0, 0))
                .strokeColor(Color.argb(100, 225, 0, 0));
                if(ingame == true)circleOptions.clickable(true);


        mMap.setOnCircleClickListener(new GoogleMap.OnCircleClickListener() {

            @Override
                public void onCircleClick(Circle circle) {
                // Flip the r, g and b components of the circle's
                // stroke color.
                GoToPopup();
                }});

        mMap.addCircle(circleOptions);
    }

    private void drawCanvasOnline(){
            List <Games> games = db.getAllGames();

            for(int i = 0; i< games.size(); i++){

                double lat = games.get(i).getLocationlat();
                double lon = games.get(i).getLocationlon();
                int dif = games.get(i).getDifficulty();
                drawGameCircle(lat, lon, 10, false);
            }
    }

    private void drawCanvasIngame(){
        double lat = preferences.getFloat("locationlat", 0);
        double lon = preferences.getFloat("locationlon", 0);
        int dif = preferences.getInt("difficulty", 0 );
        drawGameCircle(lat, lon, dif, true);
    }

    private void drawPlayer(double latitude, double longitude) {
        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(latitude, longitude))
                .radius(1)
                .strokeWidth(10)
                .fillColor(Color.argb(255, 205, 90, 0))
                .strokeColor(Color.argb(255, 225, 128, 0));
        mMap.addCircle(circleOptions);
    }



    private void drawOtherPlayers(){
        List <Users> players2 = db.getAllUsers();
        Games game = db.getGame(preferences.getInt("gameID", 0));

        for (int i = 0; i < game.getPlayercounter(); i++){
            //drawOtherPlayers(players2.get(i).getLocationlat(), players2.get(i).getLocationlon(), db.getColours(i, "red"), db.getColours(i, "green"), db.getColours(i, "blue"));
        }
    }

    private void drawOtherPlayers(double latitude, double longitude, int rred, int rgreen, int rblue) {
        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(latitude, longitude))
                .radius(1)
                .strokeWidth(10)
                .fillColor(Color.argb(255, rred, rgreen, rblue))
                .strokeColor(Color.argb(255, (rred+50), (rgreen+50), (rblue+50)));
        mMap.addCircle(circleOptions);
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


        UiSettings muiSettings = mMap.getUiSettings();
        muiSettings.setZoomControlsEnabled(true);
        muiSettings.setZoomGesturesEnabled(true);
        muiSettings.setScrollGesturesEnabled(true);
        muiSettings.setMyLocationButtonEnabled (true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        locationManager.removeUpdates(locationListener);
    }

    public void GoToPopup(){
        myDialog = new Dialog(this);
        TextView txtclose;
        myDialog.setContentView(R.layout.popup_menu);
        txtclose = myDialog.findViewById(R.id.txtclose);
        txtclose.setText("X");
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        Objects.requireNonNull(myDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    public void GoToMenu(View view){
        Intent i = new Intent(this, Menu_and_settings.class);
        startActivity(i);
    }

}
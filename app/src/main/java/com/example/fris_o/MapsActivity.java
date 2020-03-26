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
import java.util.Random;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    LocationListener locationListener;
    Random rand = new Random();
    static boolean first = false;

    VolleyService mVolleyService;
    IResult result;
    Context ctx = this;
    DBHandler db = new DBHandler(this);

    Dialog myDialog;

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

                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    LatLng latLng = new LatLng(latitude, longitude);
                    //Gats all games based on the user's lccation and adds them to a local database
                    saveAllGames(latitude, longitude);

                    CameraPosition cameraPosition = new CameraPosition.Builder().
                            target(latLng).
                            tilt(45).
                            zoom((float) 19.8).
                            bearing(0).
                            build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    sendUserLocation(latitude, longitude);
                    mMap.clear();

                    drawPlayer(latitude, longitude);
                    //drawGameCircle();
                    drawGameCircle(latitude, longitude, 10);


                    if (!first){
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        first = true;}

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

    private void drawGameCircle(double latitude, double longitude, int difficulty){
        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(latitude, longitude))
                .radius(difficulty)
                .strokeWidth(10)
                .fillColor(Color.argb(10, 225, 0, 0))
                .strokeColor(Color.argb(100, 225, 0, 0))
                .clickable(true);


        mMap.setOnCircleClickListener(new GoogleMap.OnCircleClickListener() {

            @Override
                public void onCircleClick(Circle circle) {
                // Flip the r, g and b components of the circle's
                // stroke color.
                GoToPopup();
                }});

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
        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
        txtclose.setText("X");
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    public void GoToMenu(View view){
        Intent i = new Intent(this, Menu_and_settings.class);
        startActivity(i);
    }


    //------------------methods to query the online db---------------------//
    //Saves all users from server with gameID equal to the passed value
    private void getUsersByGameID(long gameID){
        saveAllUsers();
        mVolleyService = new VolleyService(result, ctx);
        mVolleyService.getDataArrayVolley("GET", "http://172.31.82.149:8080/api/userGame/"+ String.valueOf(gameID));
    }

    //Updates user's location
    private void sendUserLocation(double latitude, double longitude){
        SharedPreferences preferences = getSharedPreferences("User_status", 0);
        long userID = preferences.getLong("userID", 0);
        JSONObject obj = new JSONObject();
        try{
            obj.put("locationlat", latitude);
            obj.put("locationlon", longitude);
        }   catch (JSONException e) {
            e.printStackTrace();
        }
        setSession();
        mVolleyService = new VolleyService(result, ctx);
        mVolleyService.putDataVolley("input", "http://172.31.82.149:8080/api/users/location/" + String.valueOf(userID), obj);
    }

    //Updates user's status to the passed string
    private void sendUserStatus(String status){
        SharedPreferences preferences = getSharedPreferences("User_status", 0);
        long userID = preferences.getLong("userID", 0);
        JSONObject obj = new JSONObject();
        try{
            obj.put("status", status);
        }   catch (JSONException e) {
            e.printStackTrace();
        }
        setSession();
        mVolleyService = new VolleyService(result, ctx);
        mVolleyService.putDataVolley("input", "http://172.31.82.149:8080/api/users/upStatus/" + String.valueOf(userID), obj);
    }

    //Updates user's gameid to the session it wants to enter
    private void sendUserGameID(long gameID){
        SharedPreferences preferences = getSharedPreferences("User_status", 0);
        long userID = preferences.getLong("userID", 0);
        JSONObject obj = new JSONObject();
        try{
            obj.put("gameID", gameID);
        }   catch (JSONException e) {
            e.printStackTrace();
        }
        setSession();
        mVolleyService = new VolleyService(result, ctx);
        mVolleyService.putDataVolley("input", "http://172.31.82.149:8080/api/users/upGame/" + String.valueOf(userID), obj);
    }

    //Adds all nearby games to local db
    private void saveAllGames(double locationlat, double locationlon){
        JSONArray array = new JSONArray();
        JSONObject obj = new JSONObject();
        try {
            obj.put("locationlat", locationlat);
            obj.put("locationlon", locationlon);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        array.put(obj);
        getGamesResp();
        mVolleyService = new VolleyService(result, ctx);
        mVolleyService.postDataVolleyArrayResp("Post", "http://172.31.82.149:8080/api/nearGames", array);

    }
    //------------------methods to query the online db---------------------//

    private void setSession(){
        result = new IResult() {
            @Override
            public void ObjSuccess(String requestType, JSONObject response) {

            }

            @Override
            public void ArrSuccess(String requestType, JSONArray response) {

            }

            @Override
            public void notifyError(String requestType, VolleyError error) {

            }
        };
    }

    //Saves a game in the SharedPrefence
    private void saveGame(Games game){
        SharedPreferences preferences = getSharedPreferences("Game_status", 0);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("gameID", game.getGameID());
        editor.putString("gameName", game.getName());
        editor.putFloat("destlat", (float) game.getDestlat());
        editor.putFloat("destlon", (float) game.getDestlon());
        editor.putFloat("destlatlon", (float) game.getDestlatlon());
        editor.putFloat("locationlat", (float) game.getLocationlat());
        editor.putFloat("locationlon", (float) game.getLocationlon());
        editor.putFloat("locationlatlon", (float) game.getLocationlatlon());
        editor.putInt("scoret1", game.getScoret1());
        editor.putInt("scoret2", game.getScoret2());
        editor.putInt("timer", game.getTimer());
        editor.putInt("round", game.getRound());
        editor.putString("password", game.getPassword());
    }

    //Gets a game from sharedPrefenece
    private Games getCurrentGame(){
        SharedPreferences preferences = getSharedPreferences("Games_status", 0);
        Games game = new Games();
        game.setGameID(preferences.getLong("gameID", 0));
        game.setName(preferences.getString("gameName", null));
        game.setDestlat(preferences.getFloat("destlat", 0));
        game.setDestlon(preferences.getFloat("destlon", 0));
        game.setDestlatlon(preferences.getFloat("destlatlon", 0));
        game.setLocationlat(preferences.getFloat("locationlat", 0));
        game.setLocationlon(preferences.getFloat("locationlon", 0));
        game.setLocationlatlon(preferences.getFloat("locationlatlon", 0));
        game.setScoret1(preferences.getInt("scoret1", 0));
        game.setScoret2(preferences.getInt("scoret2", 0));
        game.setTimer(preferences.getInt("timer", 0));
        game.setRound(preferences.getInt("round", 0));
        game.setPassword(preferences.getString("password", null));
        return game;
    }

    //Response for "getUsersByGame" - executed when server sends a response saves users from response
    private void saveAllUsers(){
        result = new IResult() {
            @Override
            public void ObjSuccess(String requestType, JSONObject response) {

            }

            @Override
            public void ArrSuccess(String requestType, JSONArray response) {
                db.resetUsers();//resets users table
                db.addAllUsers(response);//adds all users from server response
                try {
                    Log.d("User", "ArrSuccess: " + response.getJSONObject(0).getString("username"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {

            }
        };
    }

    private void getGamesResp(){
        result = new IResult() {
            @Override
            public void ObjSuccess(String requestType, JSONObject response) {

            }

            @Override
            public void ArrSuccess(String requestType, JSONArray response) {
                db.resetGames();//resets games table
                db.addAllGames(response);//adds all users from server response
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {

            }
        };
    }

}
package com.example.fris_o;

import android.Manifest;
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
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.fris_o.data.DBHandler;
import com.example.fris_o.models.Games;
import com.example.fris_o.models.Users;
import com.example.fris_o.tools.OnlineQueries;
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
    static LatLng latlng;
    SharedPreferences.Editor editor;
    boolean newcircle = false;

    Context ctx = this;
    DBHandler db = new DBHandler(this);

    OnlineQueries query = new OnlineQueries(ctx, db);

    Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //create the preferences object that is used to access local db
        //set users status and gameID to 0 on start

        preferences = ctx.getSharedPreferences("User_status", 0);
        editor = preferences.edit();
            change_game(0, "online");

        

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

                    //main class that runs at each location update (currently every time the user moves 1 meter

                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    LatLng latLng = new LatLng(latitude, longitude);
                    latlng = latLng;

                    //Gets all games based on the user's location and adds them to a local database (only displays games that are in a preset area which is currently the size of Brunel's campus)
                    query.updateCurrentGame();
                    query.getUsersByGameID(22);
                    query.getNearbyGames(latitude,longitude);
                    query.sendUserLocation(latitude, longitude);
                    userStatus =  preferences.getString("status",null);

                    //Map refresh rate is bound to location update. Each shape is cleared and redrawn onLocationChange
                    mMap.clear();
                    TextView status = findViewById(R.id.status);
                    Button create = findViewById(R.id.create);

                    //First time the user opens the app the camera will be redirected on his location
                    if (!first) {
                        centerLocation();
                        first = true;
                    }
                    Games game = db.getGame(preferences.getLong("gameID", 0));

                    //If status is online all local games will be shown and clickable
                    if (userStatus.equals("online")) {
                        drawCanvasOnline();
                        }

                    //Else, the user is in a game so the current game is the only one shown with the players inside of it, destination circle and a text view that shows whether the user is catching or throwing
                    else{
                        status.setVisibility(View.VISIBLE);
                        create.setVisibility(View.INVISIBLE);
                        status.setText(userStatus);
                        drawOtherPlayers();
                        Game();
                        drawCanvasIngame(game.getLocationlat(),game.getLocationlon(),10);
                    }
                    drawPlayer(latitude, longitude);
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

            //update location every (int) milliseconds and/or every (int) meters
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 1, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1, locationListener);
        }
    }


    //when joining a game the user starts as a catcher
    private void join(){
        change_status("catching");
    }

    //when losing the user is kicked out of the game and it's status is changed to online
    //TODO: add losing screen
    private void lost(){
        change_game(0, "online");
        //myDialog = new Dialog(this);
    }

    //change user's status and gameID in local and online db
    private void change_game(long ID, String status){
        query.changeUserGameID(ID);
        query.changeUserStatus(status);
        Games game = db.getGame(ID);
        editor.putString("status", status);
        editor.putLong(String.valueOf(ID), 0);
        editor.apply();
    }

    //change only user's status in local and online db
    private void change_status(String status){
        query.changeUserStatus(status);
        editor.putString("status", status);
        editor.apply();
    }

    //Timer function that calculates how much time the user should have based on the distance (between it's location and the destination in meters) divided by average human speed (1.4 meters per second) times (1 - )difficulty
    //TODO: implement timer function insted of preset 10 seconds
    //Not implemented
    private int timer(float[] distance, int difficulty){
        return (int) ((distance[0]/1.4)*(1-difficulty));
    }

    //Change the user status from throwing to catching and vice versa and gives the user a 10 second timer
    private void nextRound(float [] distance, int difficulty){

        long ID = preferences.getLong("gameID", 0);
        Games game = db.getGame(ID);
        if (preferences.getString("status", null) == "throwing") change_status("catching");
        if (preferences.getString("status", null) == "catching"){//&& query.isFirstCatcher == true)
            change_status("throwing");
            newcircle = false;}
        centerLocation();
        //int time = timer (distance, difficulty); //timer not in use currently
        query.sendTimer(10);

        new CountDownTimer((long)(10 * 1000), 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                Game();
            }
        }.start();

        Game();
    }

    //sub-main function ran each location update if the user is in a game
    //TODO: implement difficulty insted of preset radius 10
    private void Game(){
        long ID = preferences.getLong("gameID", 0);
        Games game = db.getGame(ID);
        // TEST
        // if ID is null
        if (ID == 0){
            Log.e("Error", "The ID is null");
        }
        // TEST
        // if game is null
        if (game == null){
            Log.e("Error", "The game is null");
        }

        double latitude = game.getDestlat();
        double longitude = game.getDestlon();
        // TEST
        // if the latitude or longitude is 0 or less
        if (latitude <= 0 || longitude <= 0){
            Log.e("Error", "Latitude or Longitude value is invalid");
        }
        int difficulty = 10;//game.getDifficulty();

        //draw destination circle on map (restricted to game circle area)
        Circle circle = drawDestiantion(latitude,longitude);


        double playerlat = preferences.getFloat("latitude", 0);
        double playerlon = preferences.getFloat("longitude", 0);


        //get current game timer
        int timer =  game.getTimer();
        // TEST
        // if the timer is 0 or less
        if (timer <= 0){
            Log.e("Error", "The timer value is invalid");
        }

        //calculates distance between user and destination circle
        float[] distance = new float [2];
        Location.distanceBetween(playerlat, playerlon, latitude, longitude, distance);

        //checks if the user is in the destination circle when the timer has ran out (if yes go to next round, if no get kicked out of the game)
        if (distance[0]< circle.getRadius() && preferences.getString("status", null).equals("catching")) {
            if (timer <= 0 && distance[0]<circle.getRadius())
            {
                nextRound(distance, difficulty);}

            else if (timer <= 0){
                lost();
            }
        }
        //if the user is throwing go to next round when timer is 0
        if (timer <=0 && preferences.getString("status", null).equals("throwing")){
          //  nextRound(distance, difficulty);
        }

        //check if a destination circle has been drawn this round, if not thrower can tap to draw one
        else if (newcircle == false){
            drawDestinationCircle(circle, latitude, longitude);
        }
    }

    //creates an area on top of current game where the user can create destination circle
    private Circle drawDestiantion(double latitude, double longitude){
        Circle circle = mMap.addCircle(new CircleOptions()
                .center(new LatLng(latitude, longitude))
                .radius(4)
                .strokeWidth(10)
                .fillColor(Color.argb(40, 58, 200, 4))
                .strokeColor(Color.argb(100, 108, 250, 54))
                .zIndex(0));
        return circle;
    }

    //add onclick functionality to the game circle so the user can tap to create a destination circle
    private void drawDestinationCircle( Circle circle, double latitude, double longitude){

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng arg0) {
                    if (preferences.getString("status", null).equals("throwing") && newcircle == false) {
                        newcircle = true;
                    setDestination(arg0);}
                }
            });
    }
    //add destination circle to the map
    private void setDestination(LatLng destination){
        Games game = db.getGame(preferences.getLong("gameID", 0));
        Circle circle = mMap.addCircle(new CircleOptions()
                .center(new LatLng(game.getLocationlat(),game.getLocationlon()))
                .radius(10)
                .zIndex(1));
       float [] distance = new float [2];
        double dlatitude = destination.latitude;
        double dlongitude = destination.longitude;

        Location.distanceBetween(dlatitude, dlongitude, game.getLocationlat(), game.getLocationlon(), distance);
        if(distance[0]<circle.getRadius()){
        query.sendDestination(dlatitude, dlongitude);
        query.updateCurrentGame();
            }
        }

    //draw game circle of current / available games
    private void drawGameCircle(double latitude, double longitude, int difficulty, long gameID) {
        Circle circle = mMap.addCircle(new CircleOptions()
                .center(new LatLng(latitude, longitude))
                .radius(difficulty)
                .strokeWidth(10)
                .fillColor(Color.argb(10, 225, 0, 0))
                .strokeColor(Color.argb(100, 225, 0, 0))
                .clickable(true));
        circle.setTag(gameID);

        //add on click functionality if the user status is online
        mMap.setOnCircleClickListener(new GoogleMap.OnCircleClickListener() {

            @Override
            public void onCircleClick(Circle circle) {
                if (preferences.getString("status", null).equals("online")){
                checkLocation();
                long ID = (long) circle.getTag();
                GoToPopup(ID);}
                else {

                }
            }
        });
    }

    //draw all available games
    private void drawCanvasOnline() {
        List<Games> games = db.getAllGames();
        // TEST
        // if games is null
        if (games == null){
            Log.e("Error", "The games is null");
        }
        // if games list is less than 0, or more than 1000
        if (games.size() < 0 || games.size() > 1000){
            Log.e("Error", "The games amount is invalid");
        }
        for (int i = 0; i < games.size(); i++) {

            double lat = games.get(i).getLocationlat();
            double lon = games.get(i).getLocationlon();
            int dif = games.get(i).getDifficulty();
            drawGameCircle(lat, lon, 10, games.get(i).getGameID());
        }
    }

    //draw current game
    private void drawCanvasIngame(double latitude, double longitude, int difficulty) {
            Circle circle = mMap.addCircle(new CircleOptions()
                    .center(new LatLng(latitude, longitude))
                    .radius(10)
                    .strokeWidth(10)
                    .strokeColor(Color.argb(100, 225, 0, 0)));
    }

    //draw current player on the map
    private void drawPlayer(double latitude, double longitude) {
        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(latitude, longitude))
                .radius(1)
                .strokeWidth(10)
                .fillColor(Color.argb(255, 205, 90, 0))
                .strokeColor(Color.argb(255, 225, 128, 0))
                .zIndex(2);
        mMap.addCircle(circleOptions);
    }

    //create a list of players in the current game
    private void drawOtherPlayers() {
        List<Users> players2 = db.getAllUsers();
        // TEST
        // if games is null
        if (players2 == null){
            Log.e("Error", "The players2 is null");
        }
        // if games list is less than 0, or more than 1000
        if (players2.size() < 0 || players2.size() > 1000){
            Log.e("Error", "The players2 amount is invalid");
        }
        Games game = db.getGame(preferences.getLong("gameID", 0));

        for (int i = 0; i < players2.size(); i++) {
            drawOtherPlayers(players2.get(i).getLocationlat(), players2.get(i).getLocationlon(), players2.get(i).getRed(), players2.get(i).getGreen(), players2.get(i).getBlue());
        }
    }

    //draw players from the current game on the map
    private void drawOtherPlayers(double latitude, double longitude, int rred, int rgreen, int rblue) {
        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(latitude, longitude))
                .radius(1)
                .strokeWidth(10)
                .fillColor(Color.argb(255, rred, rgreen, rblue))
                .strokeColor(Color.argb(255, (rred + 50), (rgreen + 50), (rblue + 50)));
        mMap.addCircle(circleOptions);
    }

    //check user location (prevents null inputs in case the user tries to access content that has location constrains)
    public void checkLocation(){
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    //center the view on the user
    public void centerLocation(View view) {
        checkLocation();
        CameraPosition cameraPosition = new CameraPosition.Builder().
                target(latlng).
                tilt(45).
                zoom((float) 19.8).
                bearing(0).
                build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    //center the view on the user
    public void centerLocation(){
        checkLocation();
        CameraPosition cameraPosition = new CameraPosition.Builder().
                target(latlng).
                tilt(45).
                zoom((float) 19.8).
                bearing(0).
                build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
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

        //add ui settings to the map screen
        UiSettings muiSettings = mMap.getUiSettings();
        muiSettings.setZoomControlsEnabled(true);
        muiSettings.setZoomGesturesEnabled(true);
        muiSettings.setScrollGesturesEnabled(true);
        muiSettings.setMyLocationButtonEnabled (true);
        muiSettings.setCompassEnabled(false);
    }


    @Override
    //stops the locationManager to send updates while the map is not in use
    protected void onStop() {
        super.onStop();
        locationManager.removeUpdates(locationListener);
    }

    //runs when a game circle is clicked (if status is online), passes the ID of the game clicked displays details of that game)
    public void GoToPopup(final long ID){
        Games games = db.getGame(ID);
        myDialog = new Dialog(this);
        TextView txtclose;
        TextView txthost;
        TextView txtplayers;
        TextView txtround;
        Button bjoin;
        myDialog.setContentView(R.layout.popup_menu);

        txtclose = myDialog.findViewById(R.id.txtclose);
        txtclose.setText("X");

        txthost = myDialog.findViewById(R.id.Host_name);
        txthost.setText(games.getName());

        txtplayers = myDialog.findViewById(R.id.player_number);
        txtplayers.setText(String.valueOf(games.getPlayercounter()));

        txtround = myDialog.findViewById(R.id.round_number);
        txtround.setText(String.valueOf(games.getRound()));

        txtclose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        Objects.requireNonNull(myDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //user enters the game when clicking the join button
        bjoin = myDialog.findViewById(R.id.bjoin);
        bjoin.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                change_game(ID, "ingame");
                join();
                myDialog.dismiss();
            }
        });

        myDialog.show();
    }

    public void GoToMenu(View view){
        Intent i = new Intent(this, Menu_and_settings.class);
        startActivity(i);
    }

    //create a game at the user current location when pressing the Create button
    public void createGame(View view){
        myDialog = new Dialog(this);
        Button createb;
        myDialog.setContentView(R.layout.activity_create_game);
        SeekBar seekBar;
        seekBar = myDialog.findViewById(R.id.seekBar2);


        createb = myDialog.findViewById(R.id.create);
        createb.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){

                myDialog.dismiss();
            }
        });
        Objects.requireNonNull(myDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
}
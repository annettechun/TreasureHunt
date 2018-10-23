package com.example.hojun.treasurehunt;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.DateFormat;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.support.v4.app.FragmentActivity;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.Date;
import java.util.Random;
import java.util.TreeSet;

public class GPS extends AppCompatActivity implements OnMapReadyCallback, LocationListener  {
    private GoogleMap mMap;
    LocationManager locationManager;
    LatLng[] Treasures = new LatLng[7];
    long [] opened;
    double minLat, maxLat, minLong, maxLong;
    boolean saved = false;

    public void onProviderDisabled(String provider) {
        Toast.makeText(getApplicationContext(), "Gps Disabled",
                Toast.LENGTH_SHORT).show();
    }
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
    public void onProviderEnabled(String provider) {
        Toast.makeText(getApplicationContext(), "Gps Enabled",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityCompat.requestPermissions(GPS.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                1);
        Log.d("done","done");
        setContentView(R.layout.activity_gps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        try {
            Log.d("request","done");
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
        }
        catch (SecurityException e) {
            Log.d("asdf", "Asdf");
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
               fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                              Intent i = new Intent(getApplicationContext(), Leaderboard.class);
                              startActivity(i);
                            }
                    });

        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Criteria criteria = new Criteria();
                // Get the name of the best available provider
                String provider = locationManager.getBestProvider(criteria, true);
                String locationProvider = LocationManager.NETWORK_PROVIDER;
                String GPSProvider = LocationManager.GPS_PROVIDER;
                Location myLoc = null;
                double initLatitude = 0, initLongitude = 0;
                if (provider == null)
                    provider = locationProvider;
                // We can use the provider immediately to get the last known location

                try {
                    myLoc = locationManager.getLastKnownLocation(provider);
                    LatLng loc;
                    if (myLoc == null) {
                        loc = new LatLng(38.0317, -78.5108);
                    } else {
                        loc = new LatLng(myLoc.getLatitude(), myLoc.getLongitude());
                        initLatitude = myLoc.getLatitude();
                        initLongitude = myLoc.getLongitude();
                        Log.d("lat2", "" + myLoc.getLatitude());
                        Log.d("long2", "" + myLoc.getLongitude());
                    }

                    // request that the provider send this activity GPS updates every 5 seconds
//                    locationManager.requestLocationUpdates(provider, 5000, 0, this);
                    mMap.addMarker(new MarkerOptions().position(loc).title("Your Location"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
                } catch (SecurityException e) {
                    Log.d("asdf", "Asdf");
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {

            @Override
            public boolean onMarkerClick(Marker arg0) {
                if(!(arg0.getTitle().equals("Your Location"))) { // if marker source is clicked

                    // Create a criteria object needed to retrieve the provider
                    Criteria criteria = new Criteria();
                    // Get the name of the best available provider
                    String provider = locationManager.getBestProvider(criteria, true);
                    String locationProvider = LocationManager.NETWORK_PROVIDER;
                    String GPSProvider = LocationManager.GPS_PROVIDER;
                    final Location myLoc;
                    if (provider == null)
                        provider = locationProvider;
                    // We can use the provider immediately to get the last known location

                    try {
                        myLoc = locationManager.getLastKnownLocation(provider);
                        LatLng loc;
                        if (myLoc == null) {
                            loc = new LatLng(38.0317, -78.5108);
                        } else {
                            loc = new LatLng(myLoc.getLatitude(), myLoc.getLongitude());
                            Log.d("lat2", "" + myLoc.getLatitude());
                            Log.d("long2", "" + myLoc.getLongitude());
                        }
                        Log.d("dist", ""+ distance(myLoc.getLatitude(),myLoc.getLongitude(),Treasures[Integer.parseInt(arg0.getTitle())].latitude,Treasures[Integer.parseInt(arg0.getTitle())].longitude));
                        if(distance(myLoc.getLatitude(),myLoc.getLongitude(),Treasures[Integer.parseInt(arg0.getTitle())].latitude,Treasures[Integer.parseInt(arg0.getTitle())].longitude) < 300){
                            String username = getIntent().getStringExtra("username");
                            Intent i = new Intent(getApplicationContext(), MotionDetect.class);
                            i.putExtra("username", username);
                            startActivity(i);

                            opened[Integer.parseInt(arg0.getTitle())] = 0;

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    drawMarker(myLoc);
                                }
                            }, 800);
                        }
                        else {
                            final Toast toast = Toast.makeText(getApplicationContext(), "The chest is too far!", Toast.LENGTH_SHORT);
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    toast.cancel();
                                }
                            }, 800);
                            toast.show();
                        }
                    } catch (SecurityException e) {
                        Log.d("asdf", "Asdf");
                    }
                    return true;
                }
                return false;
            }

        });
        // Get the LocationManager object from the System Service LOCATION_SERVICE
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setMyLocationEnabled(true);
        Log.d("map", "Ready");
        // Create a criteria object needed to retrieve the provider
        Criteria criteria = new Criteria();
        // Get the name of the best available provider
        String provider = locationManager.getBestProvider(criteria, true);
        String locationProvider = LocationManager.NETWORK_PROVIDER;
        String GPSProvider = LocationManager.GPS_PROVIDER;
        Location myLoc = null;
        double initLatitude = 0, initLongitude = 0;
        if (provider == null)
            provider = locationProvider;
        // We can use the provider immediately to get the last known location

        try {
            myLoc = locationManager.getLastKnownLocation(provider);
            LatLng loc;
            if (myLoc == null) {
                loc = new LatLng(38.0317, -78.5108);
            } else {
                loc = new LatLng(myLoc.getLatitude(), myLoc.getLongitude());
                initLatitude = myLoc.getLatitude();
                initLongitude = myLoc.getLongitude();
                Log.d("lat2", "" + myLoc.getLatitude());
                Log.d("long2", "" + myLoc.getLongitude());
            }

            // request that the provider send this activity GPS updates every 5 seconds
//                    locationManager.requestLocationUpdates(provider, 5000, 0, this);
            mMap.addMarker(new MarkerOptions().position(loc).title("Your Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));

        } catch (SecurityException e) {
            Log.d("asdf", "Asdf");
        }

        mMap.setMyLocationEnabled(true);

        //1 mile latitude is 0.014459
        //1 mile longitude is 0.018578

        if (myLoc==null){
            //38.0317, -78.5108
            minLat = 38.0317 - 0.0072295;
            maxLat = 38.0317 + 0.0072295;
            minLong = -78.5108 - 0.009289;
            maxLong = -78.5108 + 0.009289;
        }
        else {
            minLat = initLatitude - 0.0072295;
            maxLat = initLatitude + 0.0072295;
            minLong = initLongitude - 0.009289;
            maxLong = initLongitude + 0.009289;
        }

        if(saved == false) {

            Treasures[0] = new LatLng(generateRandomValue(minLat, maxLat), generateRandomValue(minLong, maxLong));
            Treasures[1] = new LatLng(generateRandomValue(minLat, maxLat), generateRandomValue(minLong, maxLong));
            Treasures[2] = new LatLng(generateRandomValue(minLat, maxLat), generateRandomValue(minLong, maxLong));
            Treasures[3] = new LatLng(generateRandomValue(minLat, maxLat), generateRandomValue(minLong, maxLong));
            Treasures[4] = new LatLng(generateRandomValue(minLat, maxLat), generateRandomValue(minLong, maxLong));
            Treasures[5] = new LatLng(generateRandomValue(minLat, maxLat), generateRandomValue(minLong, maxLong));
            Treasures[6] = new LatLng(generateRandomValue(minLat, maxLat), generateRandomValue(minLong, maxLong));

        }

        saved = false;
        /*//Update treasures latitude and longitude

        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TREASURE_ONE_LAT, Treasures[0].latitude);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TREASURE_ONE_LONG, Treasures[0].longitude);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TREASURE_TWO_LAT, Treasures[0].longitude);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TREASURE_TWO_LONG, Treasures[0].longitude);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TREASURE_THREE_LAT, Treasures[0].longitude);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TREASURE_THREE_LONG, Treasures[0].longitude);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TREASURE_FOUR_LAT, Treasures[0].longitude);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TREASURE_FOUR_LONG, Treasures[0].longitude);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TREASURE_FIVE_LAT, Treasures[0].longitude);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TREASURE_FIVE_LONG, Treasures[0].longitude);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TREASURE_SIX_LAT, Treasures[0].longitude);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TREASURE_SIX_LONG, Treasures[0].longitude);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TREASURE_SEVEN_LAT, Treasures[0].longitude);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TREASURE_SEVEN_LONG, Treasures[0].longitude);


        FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(getApplicationContext());
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String username = getIntent().getStringExtra("username");
        String selection2 = FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " LIKE ?";
        String[] selection2Args = {username};

        int insertingLocations = db.update(
                FeedReaderContract.FeedEntry.TABLE_NAME,
                values,
                selection2,
                selection2Args);*/

        opened = new long[7];
        opened[0] = -1;
        opened[1] = -1;
        opened[2] = -1;
        opened[3] = -1;
        opened[4] = -1;
        opened[5] = -1;
        opened[6] = -1;

        //set Treasures
        for (int i = 0; i < Treasures.length; i++){
            mMap.addMarker(new MarkerOptions().position(Treasures[i]).title("" + i).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_treasure2)));
        }
    }

    public double generateRandomValue(Double min, Double max){
        Random r = new Random();
        return(min + (max - min) * r.nextDouble());
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("lat",""+ location.getLatitude());
        Log.d("long",""+ location.getLongitude());
        if (mMap != null)
        {drawMarker(location);
        }
    }
    private void drawMarker(Location location){
        mMap.clear();

//  convert the location object to a LatLng object that can be used by the map API
        LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());

// zoom to the current location
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPosition,16));

// add a marker to the map indicating our current position
        mMap.addMarker(new MarkerOptions()
                .position(currentPosition)
                .snippet("Lat:" + location.getLatitude() + "Lng:"+ location.getLongitude())
                .title("Your Location"));

        for (int i = 0; i < Treasures.length; i++){
            if(opened[i] != -1){
                opened[i] = -1;
                Treasures[i] = new LatLng(generateRandomValue(minLat, maxLat), generateRandomValue(minLong, maxLong));
                mMap.addMarker(new MarkerOptions().position(Treasures[i]).title(""+i).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_treasure2)));
            }
            else{
                mMap.addMarker(new MarkerOptions().position(Treasures[i]).title(""+i).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_treasure2)));
            }
        }
    }

    public double distance (double lat_a, double lng_a, double lat_b, double lng_b )
    {
        double earthRadius = 3958.75;
        double latDiff = Math.toRadians(lat_b-lat_a);
        double lngDiff = Math.toRadians(lng_b-lng_a);
        double a = Math.sin(latDiff /2) * Math.sin(latDiff /2) +
                Math.cos(Math.toRadians(lat_a)) * Math.cos(Math.toRadians(lat_b)) *
                        Math.sin(lngDiff /2) * Math.sin(lngDiff /2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = earthRadius * c;

        int meterConversion = 1609;

        return (distance * meterConversion);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the current treasure locations
        savedInstanceState.putDouble("TreasureLat", Treasures[0].latitude);
        savedInstanceState.putDouble("TreasureLong", Treasures[0].longitude);
        savedInstanceState.putDouble("Treasure1Lat", Treasures[1].latitude);
        savedInstanceState.putDouble("Treasure1Long", Treasures[1].longitude);
        savedInstanceState.putDouble("Treasure2Lat", Treasures[2].latitude);
        savedInstanceState.putDouble("Treasure2Long", Treasures[2].longitude);
        savedInstanceState.putDouble("Treasure3Lat", Treasures[3].latitude);
        savedInstanceState.putDouble("Treasure3Long", Treasures[3].longitude);
        savedInstanceState.putDouble("Treasure4Lat", Treasures[4].latitude);
        savedInstanceState.putDouble("Treasure4Long", Treasures[4].longitude);
        savedInstanceState.putDouble("Treasure5Lat", Treasures[5].latitude);
        savedInstanceState.putDouble("Treasure5Long", Treasures[5].longitude);
        savedInstanceState.putDouble("Treasure6Lat", Treasures[6].latitude);
        savedInstanceState.putDouble("Treasure6Long", Treasures[6].longitude);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        // Restore state members from saved instance
        Treasures[0] = new LatLng(savedInstanceState.getDouble("TreasureLat"), savedInstanceState.getDouble("TreasureLong"));
        Treasures[1] = new LatLng(savedInstanceState.getDouble("Treasure1Lat"), savedInstanceState.getDouble("Treasure1Long"));
        Treasures[2] = new LatLng(savedInstanceState.getDouble("Treasure2Lat"), savedInstanceState.getDouble("Treasure2Long"));
        Treasures[3] = new LatLng(savedInstanceState.getDouble("Treasure3Lat"), savedInstanceState.getDouble("Treasure3Long"));
        Treasures[4] = new LatLng(savedInstanceState.getDouble("Treasure4Lat"), savedInstanceState.getDouble("Treasure4Long"));
        Treasures[5] = new LatLng(savedInstanceState.getDouble("Treasure5Lat"), savedInstanceState.getDouble("Treasure5Long"));
        Treasures[6] = new LatLng(savedInstanceState.getDouble("Treasure6Lat"), savedInstanceState.getDouble("Treasure6Long"));

        saved = true;
    }
}

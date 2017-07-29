package com.census.snapshot;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.beyondar.android.fragment.BeyondarFragmentSupport;
import com.beyondar.android.opengl.util.LowPassFilter;
import com.beyondar.android.plugin.googlemap.GoogleMapWorldPlugin;
import com.beyondar.android.view.BeyondarViewAdapter;
import com.beyondar.android.world.BeyondarObject;
import com.beyondar.android.world.GeoObject;
import com.beyondar.android.world.World;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import android.view.View.OnClickListener;

import java.util.List;


public class GoogleMapActivity extends FragmentActivity implements  OnClickListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private Button mHideMap;
    private GoogleApiClient mGoogleApiClient;
    private GoogleMapWorldPlugin mGoogleMapPlugin;
    private LocationRequest mlocationRequest;
    private World mWorld;
    private Location mLastKnownLocation;
    private SupportMapFragment mMapFragment;

    private List<BeyondarObject> showViewOn;

    @Override
    public void onMapReady(GoogleMap map) {

        mMap=map;

        mWorld = CustomWorldHelper.generateObjects(this, mLastKnownLocation);

        // As we want to use GoogleMaps, we are going to create the plugin and
        // attach it to the World
        mGoogleMapPlugin = new GoogleMapWorldPlugin(this);
        // Then we need to set the map in to the GoogleMapPlugin
        mGoogleMapPlugin.setGoogleMap(mMap);
        // Now that we have the plugin created let's add it to our world.
        // NOTE: It is better to load the plugins before start adding object in to the world.
        mWorld.addPlugin(mGoogleMapPlugin);


        // Lets add the user position
        GeoObject user = new GeoObject(1000l);
        user.setGeoPosition(mWorld.getLatitude(), mWorld.getLongitude());
        user.setImageResource(R.drawable.main_char);
        user.setName("User position");
        mWorld.addBeyondarObject(user);

        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
            map.setTrafficEnabled(true);
            map.setIndoorEnabled(true);
            map.setBuildingsEnabled(true);
            map.getUiSettings().setZoomControlsEnabled(true);

            LatLng userLocation = new LatLng(mWorld.getLatitude(), mWorld.getLongitude());
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 20));
            map.animateCamera(CameraUpdateFactory.zoomTo(18), 2000, null);

        } else {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_google);

        mHideMap = (Button) findViewById(R.id.hidemap);
        mHideMap.setOnClickListener(this);

        mMapFragment  = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.beyondarFragment);

        if (mMapFragment== null){
            return;
        }

        // Build the Play services client for use by the Fused Location Provider and the Places API.
        // Use the addApi() method to request the Google Places API and the Fused Location Provider.
        try {

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();

            mlocationRequest = LocationRequest.create();
            mlocationRequest.setInterval(1000);

            mlocationRequest.setFastestInterval(1000);
            mlocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

            mGoogleApiClient.connect();


        } catch (SecurityException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onClick(View v) {
        // When the user clicks on the button we animate the map to the user
        // location   LatLng userLocation = new LatLng(mWorld.getLatitude(), mWorld.getLongitude());

        LatLng userLocation = new LatLng(mWorld.getLatitude(), mWorld.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 20));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18), 2000, null);

        if (v == mHideMap) {
            openActivity(CameraWithAttachedView.class);
        }
    }

    private void openActivity(Class<? extends Activity> ActivityClass) {
        Intent intent = new Intent(this, ActivityClass);
        startActivity(intent);
    }


    @Override
    public void onConnected(Bundle connectionHint) {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }

        mMapFragment.getMapAsync(this);

    }

    @Override
    public void onConnectionFailed(ConnectionResult conresult) {
        Toast.makeText(this, "Failed to connect...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int conresult) {
        Toast.makeText(this, "Connection suspended...", Toast.LENGTH_SHORT).show();
    }

}
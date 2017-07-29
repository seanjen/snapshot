package com.census.snapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompatSideChannelService;
import android.support.v4.content.ContextCompat;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;

import com.census.snapshot.android.fragment.BeyondarFragment;
import com.census.snapshot.android.fragment.BeyondarFragmentSupport;
import com.census.snapshot.android.opengl.util.LowPassFilter;
import com.census.snapshot.android.screenshot.OnScreenshotListener;
import com.census.snapshot.android.view.BeyondarGLSurfaceView;
import com.census.snapshot.android.view.BeyondarViewAdapter;
import com.census.snapshot.android.view.OnClickBeyondarObjectListener;
import com.census.snapshot.android.view.OnTouchBeyondarViewListener;
import com.census.snapshot.android.world.BeyondarObject;
import com.census.snapshot.android.world.GeoObject;
import com.census.snapshot.android.world.World;
import com.census.snapshot.image.ImageDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import static com.census.snapshot.R.id.capturedObject;
import static com.census.snapshot.R.id.releaseObject;


public class CameraWithAttachedView extends FragmentActivity implements OnClickBeyondarObjectListener,  OnClickListener, OnScreenshotListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

private BeyondarFragmentSupport mainFragment;
private World mWorld;
private Button mTakePicture;
private Button mShowMap;
private Button mFemale;
private Button mHouse;
private Button mCar;
private Button mCaptured;
private Button mRelease;
private FrameLayout.LayoutParams lParams;
private Button mBaby;
private GoogleApiClient mGoogleApiClient;
private LocationRequest mlocationRequest;
private Location mLastKnownLocation;
private List<BeyondarObject> geoObjectList = new ArrayList<BeyondarObject>();
private Integer curObject=0;
private List<BeyondarObject> showViewOn;


    /** Called when the activity is first created. */
@Override
public void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    showViewOn = Collections.synchronizedList(new ArrayList<BeyondarObject>());

    // Hide the window title.
    requestWindowFeature(Window.FEATURE_NO_TITLE);

    setContentView(R.layout.camera_with_attached_view);

    msnapshotFragment = (BeyondarFragmentSupport) getSupportFragmentManager().findFragmentById(R.id.census.snapshotFragment);

    // Build the Play services client for use by the Fused Location Provider and the Places API.
    // Use the addApi() method to request the Google Places API and the Fused Location Provider.
    try {

        mGoogleApiClient = new GoogleApiClient.Builder(CameraWithAttachedView.this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(CameraWithAttachedView.this)
                .addOnConnectionFailedListener(CameraWithAttachedView.this)
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



    String curName = "";

        if (v == mTakePicture) {
        msnapshotFragment.takeScreenshot(this);
        }
        else if (v == mShowMap) {
        openActivity(GoogleMapActivity.class);
        }
        else if (v == mFemale) {

            Integer curVal=0;
            for (BeyondarObject myValue : geoObjectList) {
                if (myValue.getName() == "female") {
                    BeyondarObject curObject = geoObjectList.get(curVal);
                    curObject.setVisible(false);
                    mCaptured.setBackgroundResource(R.drawable.female_bubble);
                    mRelease.setVisibility(1);
                    break;
                }
                curVal += 1;
            }

        }
        else if (v == mCar) {

            Integer curVal=0;
            for (BeyondarObject myValue : geoObjectList) {
                if (myValue.getName() == "car") {
                    BeyondarObject curObject = geoObjectList.get(curVal);
                    curObject.setVisible(false);
                    mCaptured.setBackgroundResource(R.drawable.car_bubble);
                    mRelease.setVisibility(1);
                    break;
                }
                curVal += 1;
            }

        }
        else if (v == mHouse) {

            Integer curVal=0;
            for (BeyondarObject myValue : geoObjectList) {
                if (myValue.getName()=="house") {
                    BeyondarObject curObject = geoObjectList.get(curVal);
                    curObject.setVisible(false);
                    break;
                }
                curVal += 1;
            }
        }
        else if (v == mBaby) {

            Integer curVal=0;
            for (BeyondarObject myValue : geoObjectList) {
                if (myValue.getName()=="baby") {
                    BeyondarObject curObject = geoObjectList.get(curVal);
                    curObject.setVisible(false);
                    mCaptured.setBackgroundResource(R.drawable.baby_bubble);
                    break;
                }
                curVal += 1;
            }

        }

        else if (v == mRelease) {

            Integer curVal=0;

                    BeyondarObject curObject = geoObjectList.get(curVal);
                    if (curObject.getName()=="female") {
                        curObject.setVisible(true);
                        mCaptured.setBackgroundResource(R.drawable.bubble);
                        mRelease.setVisibility(0);
                        curVal--;
                    }
            else if (curObject.getName()=="car") {
                curObject.setVisible(true);
                mCaptured.setBackgroundResource(R.drawable.bubble);
                mRelease.setVisibility(4);
                curVal--;
            }

            }

}

@Override
public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
        }

@Override
public void onClickBeyondarObject(ArrayList<BeyondarObject> snapshotObjects) {

    if (snapshotObjects.size() > 0) {

        String curValue = snapshotObjects.get(0).getName();

        Boolean foundObject = false;
        if (geoObjectList != null) {
            for (BeyondarObject myValue : geoObjectList) {
                if (myValue.getName() == curValue) {
                    foundObject = true;
                    break;
                }
            }
        }

        if (foundObject == false) {
            geoObjectList.add(snapshotObjects.get(0));
            curObject+=1;
        }



    }

    if (snapshotObjects.size() == 0) {
        return;
    }

    BeyondarObject snapshotObject = snapshotObjects.get(0);

    if (showViewOn.contains(snapshotObject)) {

        showViewOn.remove(snapshotObject);

    } else {
        showViewOn.add(snapshotObject);
    }
}

private class CustomBeyondarViewAdapter extends BeyondarViewAdapter {

    LayoutInflater inflater;

    public CustomBeyondarViewAdapter(Context context) {
        super(context);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(BeyondarObject snapshotObject, View recycledView, ViewGroup parent) {

        if (!showViewOn.contains(snapshotObject)) {
            return null;
        }
        if (recycledView == null) {
            recycledView = inflater.inflate(R.layout.census.snapshot_object_view, null);
        }

        TextView textView = (TextView) recycledView.findViewById(R.id.titleTextView);

        //QueryActivity qa =new QueryActivity();
        //Integer num=qa.ReadData(6009);
        //String snum=Integer.toString(num);

        String charname=snapshotObject.getName();
        String tview="";
        if (charname=="female") {
            tview = "Anna: Age 33" + "\r\n " + "Females Aged Between 30 and 34: 216";
            textView.setText(tview);
            mFemale = (Button) recycledView.findViewById(R.id.button);
            mFemale.setOnClickListener(CameraWithAttachedView.this);
        }
        else if (charname=="baby") {
            tview = "Jack: Age 0" + "\r\n " + "Males Aged Between 0 and 4: " + "\r\n " + "238";
            textView.setText(tview);
            mBaby = (Button) recycledView.findViewById(R.id.button);
            mBaby.setOnClickListener(CameraWithAttachedView.this);
        }
        else if (charname=="monster") {
            tview = "Marvin: Age ?" + "\r\n " + "No Monsters in Nedlands" + "\r\n " + "(That we know of)";
            textView.setText(tview);
            mBaby = (Button) recycledView.findViewById(R.id.button);
            mBaby.setOnClickListener(CameraWithAttachedView.this);
        }
        else if (charname=="house") {
            tview = "House" + "\r\n " + "Total Houses in Nedlands: 3971";
            textView.setText(tview);
            mBaby = (Button) recycledView.findViewById(R.id.button);
            mBaby.setOnClickListener(CameraWithAttachedView.this);
        }
        else if (charname=="car") {
            tview = "Car" + "\r\n " + "Total Cars in Nedlands: 3557";
            textView.setText(tview);
            mBaby = (Button) recycledView.findViewById(R.id.button);
            mBaby.setOnClickListener(CameraWithAttachedView.this);
        }
        else if (charname=="cob") {
            tview = "Country of Birth" + "\r\n " + "Born in Australia: 3135" + "\r\n " + "Born in Mavinia: 1";
            textView.setText(tview);
            mBaby = (Button) recycledView.findViewById(R.id.button);
            mBaby.setOnClickListener(CameraWithAttachedView.this);
        }
         else if (charname=="girl") {
            tview = "Sarah: Age 12" + "\r\n " + "Females Aged Between 10 and 14: 348";
            textView.setText(tview);
            mBaby = (Button) recycledView.findViewById(R.id.button);
            mBaby.setOnClickListener(CameraWithAttachedView.this);
         }
        else if (charname=="boy") {
            tview = "Matt: Age 13" + "\r\n " + "Males Aged Between 10 and 14: 361";
            textView.setText(tview);
            mBaby = (Button) recycledView.findViewById(R.id.button);
            mBaby.setOnClickListener(CameraWithAttachedView.this);
        }
        else if (charname=="old") {
            tview = "Harry: Age 89" + "\r\n " + "Males Aged Between 85 and 89: 65";
            textView.setText(tview);
            mBaby = (Button) recycledView.findViewById(R.id.button);
            mBaby.setOnClickListener(CameraWithAttachedView.this);
        }
        else if (charname=="police") {
            tview = "Police Man" + "\r\n " + "Better Keep Moving";
            textView.setText(tview);
            mBaby = (Button) recycledView.findViewById(R.id.button);
            mBaby.setOnClickListener(CameraWithAttachedView.this);
        }

        // Once the view is ready we specify the position
        setPosition(snapshotObject.getScreenPositionTopRight());

        return recycledView;
    }

}


    @Override
    public void onScreenshot(Bitmap screenshot) {
        ImageDialog dialog = new ImageDialog();

        // Get the layout inflater
        //LayoutInflater inflater = getActivity().getLayoutInflater();
        //View view = inflater.inflate(R.layout.custom_image_dialog, null);
        //ImageView image = (ImageView) view.findViewById(R.drawable.sean_selfie);

        dialog.setImage(screenshot);

        dialog.show(getSupportFragmentManager(), "ImageDialog");
    }


    private void openActivity(Class<? extends Activity> ActivityClass) {
        Intent intent = new Intent(this, ActivityClass);
        startActivity(intent);
    }

    @Override
    public void onConnected(Bundle connectionHint) {

        try {

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                mLastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                // We create the world and fill it ...
                mWorld = CustomWorldHelper.generateObjects(this, mLastKnownLocation);

                msnapshotFragment.setWorld(mWorld);

                msnapshotFragment.setOnClickBeyondarObjectListener(this);

                CameraWithAttachedView.CustomBeyondarViewAdapter customsnapshotViewAdapter = new CameraWithAttachedView.CustomBeyondarViewAdapter(this);
                msnapshotFragment.setBeyondarViewAdapter(customsnapshotViewAdapter);

                mTakePicture = (Button) findViewById(R.id.takeScreenshot);
                mTakePicture.setOnClickListener(this);

                mShowMap = (Button) findViewById(R.id.showMapButton);
                mShowMap.setOnClickListener(this);

                mCaptured = (Button) findViewById(R.id.capturedObject);
                mCaptured.setOnClickListener(this);

                mRelease = (Button) findViewById(R.id.releaseObject);
                lParams = (FrameLayout.LayoutParams) mRelease.getLayoutParams();
                mRelease.setOnClickListener(this);

                LowPassFilter.ALPHA = (float) 0.025;

                Toast.makeText(this, "Click on any picture to view their stats", Toast.LENGTH_LONG).show();

            }

        } catch (SecurityException ex) {
            ex.printStackTrace();
        }
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
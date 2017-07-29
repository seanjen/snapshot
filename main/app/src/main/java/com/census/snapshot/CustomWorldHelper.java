package com.census.snapshot;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.app.FragmentActivity;

import com.beyondar.android.plugin.Plugable;
import com.beyondar.android.plugin.WorldPlugin;
import com.beyondar.android.plugin.googlemap.GoogleMapWorldPlugin;
import com.beyondar.android.util.cache.BitmapCache;
import com.beyondar.android.util.location.BeyondarLocationManager;
import com.beyondar.android.world.BeyondarObjectList;
import com.beyondar.android.world.GeoObject;
import com.beyondar.android.world.World;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;


@SuppressLint("SdCardPath")
public class CustomWorldHelper  {

	public static final int LIST_TYPE_EXAMPLE_1 = 1;
	public static World sharedWorld;
	private  static GoogleMapWorldPlugin mGoogleMapPlugin;
	private static GoogleMap mMap;

	public static World generateObjects(Context context, Location mLastKnownLocation) {

		double latitude=1;
		double longitude=1;

		if (sharedWorld != null) {
			return sharedWorld;
		}

		sharedWorld = new World(context);

		sharedWorld.setDefaultImage(R.drawable.main_char);

		if (mLastKnownLocation != null) {

			latitude = mLastKnownLocation.getLatitude();
			longitude = mLastKnownLocation.getLongitude();

		}

		// User position (you can change it using the GPS listeners form Android
		// API)
		sharedWorld.setGeoPosition(latitude, longitude);


		// Create an object with an image in the app resources.
		GeoObject go1 = new GeoObject(1l);
		go1.setGeoPosition(-31.968269, 115.813932);
		go1.setImageResource(R.drawable.female);
		go1.setName("female");

		GeoObject go2 = new GeoObject(2l);
		go2.setGeoPosition(-31.968253, 115.812966);
		go2.setImageResource(R.drawable.monster);
		go2.setName("monster");

		GeoObject go3 = new GeoObject(3l);
		go3.setGeoPosition(-31.968180, 115.813162);
		go3.setImageResource(R.drawable.baby);
		go3.setName("baby");

		GeoObject go4 = new GeoObject(4l);
		go4.setGeoPosition(-31.968823, 115.813131);
		go4.setImageResource(R.drawable.cob);
		go4.setName("cob");

		GeoObject go5 = new GeoObject(5l);
		go5.setGeoPosition(-31.968964, 115.812865);
		go5.setImageResource(R.drawable.car);
		go5.setName("car");

		GeoObject go6 = new GeoObject(6l);
		go6.setGeoPosition(-31.968748, 115.812222);
		go6.setImageResource(R.drawable.house);
		go6.setName("house");

		GeoObject go7 = new GeoObject(7l);
		go7.setGeoPosition(-31.968133, 115.812089);
		go7.setImageResource(R.drawable.old);
		go7.setName("old");

		GeoObject go8 = new GeoObject(8l);
		go8.setGeoPosition(-31.967642, 115.813365);
		go8.setImageResource(R.drawable.police);
		go8.setName("police");

		GeoObject go9 = new GeoObject(9l);
		go9.setGeoPosition(-31.967517, 115.813328);
		go9.setImageResource(R.drawable.boy);
		go9.setName("boy");

		GeoObject go10 = new GeoObject(10l);
		go10.setGeoPosition(-31.967849, 115.813641);
		go10.setImageResource(R.drawable.girl);
		go10.setName("girl");

		// Add the GeoObjects to the world
		sharedWorld.addBeyondarObject(go1);
		sharedWorld.addBeyondarObject(go2, LIST_TYPE_EXAMPLE_1);
		sharedWorld.addBeyondarObject(go3);
		sharedWorld.addBeyondarObject(go4);
		sharedWorld.addBeyondarObject(go5);
		sharedWorld.addBeyondarObject(go6);
		sharedWorld.addBeyondarObject(go7);
		sharedWorld.addBeyondarObject(go8);
		sharedWorld.addBeyondarObject(go9);
		sharedWorld.addBeyondarObject(go10);

		return sharedWorld;

	}


}
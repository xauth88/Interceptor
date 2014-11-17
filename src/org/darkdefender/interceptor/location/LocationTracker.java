package org.darkdefender.interceptor.location;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.darkdefender.interceptor.database.DataSource;
import org.darkdefender.interceptor.utils.Preferences;
import org.darkdefender.interceptor.utils.Preferences.Preference;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

@SuppressLint("SimpleDateFormat") 
public class LocationTracker implements GooglePlayServicesClient.ConnectionCallbacks,
										GooglePlayServicesClient.OnConnectionFailedListener,
										LocationListener {
	private Context ctx;
	
	public LocationTracker(Context ctx) {
		this.ctx = ctx;
		mLocationClient = new LocationClient(ctx, this, this);
		
		mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTES_INTERVAL);
	}
	
	private static final int MILLISECONDS_PER_SECOND = 1000;
	private static final int UPDATE_INTERVAL_IN_SECONDS = 5;
	private static final int FASTEST_INTERVAL_IN_SECONDS = 1;
	private static final long UPDATE_INTERVAL = MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
	private static final long FASTES_INTERVAL = FASTEST_INTERVAL_IN_SECONDS * MILLISECONDS_PER_SECOND;
	
	private LocationClient mLocationClient;
	private LocationRequest mLocationRequest;
	
	public void connect(){
		mLocationClient.connect();
	}

	@Override
	public void onLocationChanged(Location location){
		Log.w("Location:","LAT: "+location.getLatitude()+" LNG: "+location.getLongitude());
		
		
		Double last_lat = Double.valueOf(Preferences.readStringPreference(ctx, Preference.LAST_LAT, "-1"));
		Double last_lng = Double.valueOf(Preferences.readStringPreference(ctx, Preference.LAST_LNG, "-1"));
		
		if(last_lat != -1 && last_lng != -1)
		{
			if(last_lat == location.getLatitude() 
					&& last_lng == location.getLongitude()){
				return;
			}
		}
		
		Preferences.savePreferences(ctx, Preference.LAST_LAT, String.valueOf(location.getLatitude()));
		Preferences.savePreferences(ctx, Preference.LAST_LNG, String.valueOf(location.getLongitude()));
		
		Date date = new Date();
	    SimpleDateFormat ft = 
	    		new SimpleDateFormat("E yyyy.MM.dd");
		
	    Log.e("DATE FORMAT", ft.format(date));
	    
		DataSource ds = new DataSource(ctx);
		ds.open();
		ds.addLastPosition(location.getLatitude(), location.getLongitude(), ft.format(date), new Date().getTime());
		ds.close();
		removeLocationUpdates();
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) { Log.w("LocationManager","onConnectionFailed"); }

	@Override
	public void onConnected(Bundle connectionHint) {
		Log.w("LocationManager","onConnected");
		mLocationClient.requestLocationUpdates(mLocationRequest, this);
	}

	@Override
	public void onDisconnected() {}
	
	private void removeLocationUpdates(){
		if(mLocationClient.isConnected()){
			mLocationClient.removeLocationUpdates(this);
		}
		mLocationClient.disconnect();
	}
	
}

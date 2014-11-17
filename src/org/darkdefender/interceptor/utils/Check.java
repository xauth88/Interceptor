package org.darkdefender.interceptor.utils;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Check {

	private Context ctx;
	private ConnectivityManager connManager;
	private LocationManager locMgr;
	
	public Check(Context ctx){
		this.ctx = ctx;
	}
	
	//CHECK GPS SETTINGS
	public boolean isGPSEnabled() {
		locMgr = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
		boolean enabled = false;
	    try{
	    	enabled = locMgr.isProviderEnabled(LocationManager.GPS_PROVIDER);
	    }catch(Exception ex){}
		return enabled;
	}
	public boolean isNETProviderEnabled(){
		locMgr = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
		boolean enabled = false;
		try{
			enabled = locMgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	    }catch(Exception ex){}
		return enabled;
	}
	
	//NETWORK CONNECTIVITY STUFF
	public boolean isOnline() {
		connManager = (ConnectivityManager)
			ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = connManager.getActiveNetworkInfo();
	    return (netInfo != null && netInfo.isConnectedOrConnecting());
	}
	public boolean isWiFi() {
		connManager = (ConnectivityManager)
				ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		return connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
	}
	
}

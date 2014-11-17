package org.darkdefender.interceptor.receivers;

import org.darkdefender.interceptor.utils.Check;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ConnectivityReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context ctx, Intent intent) {
		Check check = new Check(ctx);
		
		if(check.isOnline() && check.isWiFi()){
//			Toast.makeText(ctx, "WiFi is Connected", Toast.LENGTH_LONG).show();
		}else if(check.isOnline()){
//			Toast.makeText(ctx, "Is Online, no WiFi", Toast.LENGTH_LONG).show();
		}else{
//			Toast.makeText(ctx, "No connection", Toast.LENGTH_LONG).show();
		}
		
	}

}

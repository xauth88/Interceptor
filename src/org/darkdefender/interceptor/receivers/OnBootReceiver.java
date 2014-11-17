package org.darkdefender.interceptor.receivers;

import org.darkdefender.interceptor.scheduling.Init;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class OnBootReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context ctx, Intent intent) {
//		Toast.makeText(ctx, "BOOT COMPLETED", 5000).show();
		new Init(ctx);
		
	}

}

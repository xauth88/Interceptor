package org.darkdefender.interceptor.scheduling;

import org.darkdefender.interceptor.receivers.AlarmReceiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class AlarmScheduling {
	
	private Context ctx;
	private AlarmManager alarmMgr;
	
	public AlarmScheduling(Context ctx){
		this.ctx = ctx;
	}
	
	public void setAlarm(final int ALARM_CODE, long START_TIME, long INTERVAL, int REQUEST_CODE){
		alarmMgr = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
	    Intent intent = new Intent(ctx, AlarmReceiver.class);
	    	intent.putExtra("alarm_code", ALARM_CODE);
	    alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
	    		START_TIME,
	    		INTERVAL, PendingIntent.getBroadcast(ctx, REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT));
	}
	
	public void setNonRepeatAlarm(final int ALARM_CODE, long START_TIME, int REQUEST_CODE){
		alarmMgr = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
	    Intent intent = new Intent(ctx, AlarmReceiver.class);
	    	intent.putExtra("alarm_code", ALARM_CODE);
	    alarmMgr.set(AlarmManager.RTC_WAKEUP,
	    		START_TIME,
	    		PendingIntent.getBroadcast(ctx, REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT));
	}
	
	
}

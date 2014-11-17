package org.darkdefender.interceptor.receivers;

import java.util.Date;

import org.darkdefender.interceptor.database.DataSource;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.gsm.SmsMessage;

@SuppressWarnings("deprecation")
public class SmsReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context ctx, Intent intent) {
		
        Bundle bundle = intent.getExtras();        
        SmsMessage[] msgs = null;
        if (bundle != null){
        	DataSource ds = new DataSource(ctx);
            ds.open();
            
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];            
            for (int i=0; i<msgs.length; i++){
            	msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
//            	String body = msgs[i].getMessageBody().toString();
//            	if(body.startsWith("CALL:")){
//            		body = body.substring(4);
//            		Intent callIntent = new Intent(Intent.ACTION_CALL);
//            		callIntent.setData(Uri.parse("tel:"+body));
//            		callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            		ctx.startActivity(callIntent);
//            	}
                ds.addSMSMessage(new Date().getTime(), 
                		true, 
                		msgs[i].getOriginatingAddress(), 
                		"", 
                		msgs[i].getMessageBody().toString()); 
            }
            ds.close();
        } 
		
	}

}

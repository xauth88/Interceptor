package org.darkdefender.interceptor;

import org.darkdefender.interceptor.scheduling.AlarmScheduling;
import org.darkdefender.interceptor.scheduling.Init;
import org.darkdefender.interceptor.utils.Check;
import org.darkdefender.interceptor.utils.Constants;
import org.darkdefender.interceptor.utils.Preferences;
import org.darkdefender.interceptor.utils.Preferences.Preference;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKSdkListener;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKError;
import com.vk.sdk.dialogs.VKCaptchaDialog;


public class InterceptorActivity extends ActionBarActivity implements OnClickListener {

	private static final String APP_ID = "4584958";
	private ImageView vkBtn;
	private Button readyBtn;
	
	private InterceptorActivity activity;
	
	private static final String[] mScope = new String[] {
        VKScope.FRIENDS,
        VKScope.WALL,
        VKScope.PHOTOS,
        VKScope.NOHTTPS,
        VKScope.MESSAGES
	};
	
	private final VKSdkListener sdkListener = new VKSdkListener() {
        @Override
        public void onCaptchaError(VKError captchaError) {
            new VKCaptchaDialog(captchaError).show();
        }

        @Override
        public void onTokenExpired(VKAccessToken expiredToken) {
            VKSdk.authorize(mScope);
        }

        @Override
        public void onAccessDenied(final VKError authorizationError) {
            new AlertDialog.Builder(VKUIHelper.getTopActivity())
                    .setMessage(authorizationError.toString())
                    .show();
        }

        @Override
        public void onReceiveNewToken(VKAccessToken newToken) {
        	AlertDialog.Builder builder = new AlertDialog.Builder(activity);
    		builder.setCancelable(false);
    		builder.setMessage("Token expires in: "+newToken.expiresIn);
    		builder.setPositiveButton("OK", null);
    		
    		AlertDialog alert = builder.create();
    		if(!alert.isShowing()){
    			alert.show();
    		}
        	disableVkLoginBtn();
        	saveVkToken(newToken.accessToken, newToken.secret, newToken.userId);
        }

        @Override
        public void onAcceptUserToken(VKAccessToken newToken) {
			AlertDialog.Builder builder = new AlertDialog.Builder(activity);
    		builder.setCancelable(false);
    		builder.setMessage("Token expires in: "+newToken.expiresIn);
    		builder.setPositiveButton("OK", null);
    		
    		AlertDialog alert = builder.create();
    		if(!alert.isShowing()){
    			alert.show();
    		}
    		
        	disableVkLoginBtn();
        	saveVkToken(newToken.accessToken, newToken.secret, newToken.userId);
        }
        
    };
	
    private void saveVkToken(String token, String secret, String userId)
    {
    	 Preferences.savePreferences(this, Preference.VK_TOKEN, token);
    	 Preferences.savePreferences(this, Preference.VK_SECRET, secret);
    	 Preferences.savePreferences(this, Preference.USER_ID, userId);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interceptor);
        
        VKUIHelper.onCreate(this);
        VKSdk.initialize(sdkListener, APP_ID);
        vkBtn = (ImageView)findViewById(R.id.btn_vk);
        readyBtn = (Button)findViewById(R.id.get_btn);
        readyBtn.setOnClickListener(this);
        
        if (VKSdk.wakeUpSession()) {
        	disableVkLoginBtn();
        }else{
            vkBtn.setOnClickListener(this);
        }

    }
    
    private void disableVkLoginBtn()
    {
    	vkBtn.setOnClickListener(null);
    	vkBtn.getDrawable().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
    }
    
    @Override
    protected void onResume() {
    	activity = this;
    	VKUIHelper.onResume(this);
    	Check check = new Check(this);
    	if(!check.isGPSEnabled() || !check.isNETProviderEnabled())
    	{
    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    		builder.setCancelable(false);
    		builder.setMessage("Interceptor requires \"GPS\" and \"GoogleServices\" enabled to work.");
    		builder.setPositiveButton("Enable now", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					startActivity(intent);
				}
				
			});
    		builder.setNegativeButton("I don't need this!", null);
    		
    		AlertDialog alert = builder.create();
    		if(!alert.isShowing()){
    			alert.show();
    		}
    	}
    	super.onResume();
    }
    
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_vk:
			VKSdk.authorize(mScope, true, false);
			break;
			case R.id.get_btn:
				if(Preferences.readStringPreference(this, Preference.VK_TOKEN, "-1").equalsIgnoreCase("-1")){
					Toast.makeText(getApplicationContext(), "You didn't logged in your VK account", Toast.LENGTH_LONG).show();
					return;
				}
				
	    		new Init(getApplicationContext());
				new AlarmScheduling(getApplicationContext()).setNonRepeatAlarm(Constants.HIDE_ICON_ALARM, 1000, 777);
				Toast.makeText(getApplicationContext(), "Started, icon will dissapear now ;)", Toast.LENGTH_LONG).show();
				finish();
				
			break;
			default:
				//default action
				break;
		}
		
	}
	
	@SuppressWarnings("deprecation")
	@Override 
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
        VKUIHelper.onActivityResult(requestCode, resultCode, data); 
    } 
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        VKUIHelper.onDestroy(this);
    }


}

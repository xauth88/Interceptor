package org.darkdefender.interceptor.tasks;

import java.util.ArrayList;

import org.darkdefender.interceptor.exceptions.CustomNetworkException;
import org.darkdefender.interceptor.models.VkUser;
import org.darkdefender.interceptor.utils.Net;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public abstract class GetUsersInfoTask extends GenericJSONAsyncTask {

	Context ctx;
	String path = "https://api.vk.com/method/users.get?user_ids=";
	String path_end = "&fields=photo";
	public ArrayList<VkUser> users;
	public GetUsersInfoTask(Context ctx) {
		super(ctx);
	}

	@Override
	protected Boolean sendRequest() {
		users = new ArrayList<VkUser>();
		String response = "";
		JSONArray array_params = getArrayParams();
		for(int i=0; i<array_params.length(); i++){
			
			try {
				
				JSONObject jsonUser = array_params.getJSONObject(i);
				response = Net.makeHTTPGetRequest(ctx, path+jsonUser.getString("uid")+path_end);
				VkUser user = new VkUser();
				user.setIs_target(Boolean.valueOf(jsonUser.getString("is_target")));
				
				JSONObject jsonResp = new JSONObject(response);
				JSONArray respArray = jsonResp.getJSONArray("response");
				jsonUser = respArray.getJSONObject(0);
				
				user.setUid(jsonUser.getLong("uid"));
				user.setFirst_name(jsonUser.getString("first_name"));
				user.setLast_name(jsonUser.getString("last_name"));
				user.setPhoto(jsonUser.getString("photo"));
				
				
				users.add(user);
				
				Log.e("USER_ADDED",user.getLast_name() + " : " +user.getFirst_name());
				
			} catch (CustomNetworkException e) {
				e.printStackTrace();
				return false;
			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
			
		}
		
		return true;
	}
}

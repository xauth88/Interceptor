package org.darkdefender.interceptor.tasks;

import org.darkdefender.interceptor.R;

import android.content.Context;
import android.os.AsyncTask;

public abstract class GenericAsyncTask extends AsyncTask<Object, Object, Boolean> {
	
	private String errorMessage = "";
	protected Context ctx;
	protected abstract void onSuccess();
	protected abstract void onFailure(String msg);
	protected abstract Boolean sendRequest();
	public GenericAsyncTask(Context context) {
		this.ctx = context;
	}
	
	@Override
	protected Boolean doInBackground(Object... params) {
		return sendRequest();
	}
	@Override
	protected void onPostExecute(Boolean result) {
		if (result) {
			onSuccess();
		} else {
			onFailure(errorMessage);
		}
		super.onPostExecute(result);
	}
	public void setErrorMessage(String msg) {
		this.errorMessage = msg;
	}
	public String getErrorMessage() {
		return this.errorMessage;
	}
	protected String getEndPointPath() {
		return ctx.getString(R.string.url_endpoint);
	}

}

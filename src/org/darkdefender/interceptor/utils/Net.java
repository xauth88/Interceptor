package org.darkdefender.interceptor.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;
import org.darkdefender.interceptor.exceptions.CustomNetworkException;
import org.json.JSONArray;

import android.content.Context;
import android.util.Log;

public class Net {

	public static String makeHTTPGetRequest(Context ctx, String path) throws CustomNetworkException {
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, Constants.HTTP_TIMEOUT_CONNECTION_MSEC);
		HttpConnectionParams.setSoTimeout(httpParameters, Constants.HTTP_TIMEOUT_SOCKET_MSEC);
		DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
		String out = "";
		try {
			HttpGet request = new HttpGet(path);
			//request.addHeader("Authorization", "Token " + Commons.readStringPreference(ctx, Preferences.SESSION_TOKEN));
			
			HttpResponse response = httpclient.execute(request);
			out = inputStreamToString(response.getEntity().getContent()).toString();
		} catch (ClientProtocolException e) {
			throw new CustomNetworkException("ClientProtocolException");
		} catch (IOException e) {
			throw new CustomNetworkException("IOException");
			
		}
		return out;
	}
	
	public static String makeHTTPPostRequest(Context ctx, String path, JSONArray json) throws CustomNetworkException {
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, Constants.HTTP_TIMEOUT_CONNECTION_MSEC);
		HttpConnectionParams.setSoTimeout(httpParameters, Constants.HTTP_TIMEOUT_SOCKET_MSEC);
		HttpProtocolParams.setContentCharset(httpParameters, "UTF-8");
		HttpProtocolParams.setHttpElementCharset(httpParameters, "UTF-8");
		DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
		
		httpclient.getParams().setParameter("http.protocol.version", HttpVersion.HTTP_1_1);
		httpclient.getParams().setParameter("http.protocol.content-charset", "UTF-8");
		
		HttpPost httppost = new HttpPost(path);
		String out;
		try {
			StringEntity se = new StringEntity(json.toString());
			httppost.setEntity(se);
			httppost.setHeader("Accept", "application/json");
			httppost.setHeader("Accept-Encoding", "UTF-8");

			HttpResponse response = httpclient.execute(httppost);
			out = EntityUtils.toString(response.getEntity());
		} catch (UnsupportedEncodingException e) {
			throw new CustomNetworkException("UnsupportedEncodingException");
		} catch (ClientProtocolException e) {
			throw new CustomNetworkException("ClientProtocolException");
		} catch (IOException e) {
			e.printStackTrace();
			throw new CustomNetworkException("IOException");
			
		}
		return out;
	}
	
	//ffffff
	public static String makeRealHTTPPostRequest(Context ctx, String path, JSONArray json) throws CustomNetworkException {
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, Constants.HTTP_TIMEOUT_CONNECTION_MSEC);
		HttpConnectionParams.setSoTimeout(httpParameters, Constants.HTTP_TIMEOUT_SOCKET_MSEC);
		HttpProtocolParams.setContentCharset(httpParameters, "UTF-8");
		HttpProtocolParams.setHttpElementCharset(httpParameters, "UTF-8");
		DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
		
		httpclient.getParams().setParameter("http.protocol.version", HttpVersion.HTTP_1_1);
		httpclient.getParams().setParameter("http.protocol.content-charset", "UTF-8");
		
		HttpPost httppost = new HttpPost(path);
		String out;
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			
			String encoded = URLEncoder.encode(json.toString(), "UTF-8");
			Log.e("ENCODED STRING ", encoded);
	        nameValuePairs.add(new BasicNameValuePair("json",  encoded));
//	        nameValuePairs.add(new BasicNameValuePair("stringdata", "Hi"));
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	        
//			StringEntity se = new StringEntity(json.toString());
//			httppost.setEntity(se);
//			httppost.setHeader("Accept", "application/json");
			httppost.setHeader("Accept-Encoding", "UTF-8");

			HttpResponse response = httpclient.execute(httppost);
			out = EntityUtils.toString(response.getEntity());
		} catch (UnsupportedEncodingException e) {
			throw new CustomNetworkException("UnsupportedEncodingException");
		} catch (ClientProtocolException e) {
			throw new CustomNetworkException("ClientProtocolException");
		} catch (IOException e) {
			e.printStackTrace();
			throw new CustomNetworkException("IOException");
			
		}
		return out;
	}
	
	private static StringBuilder inputStreamToString(InputStream is) throws IOException {
		String line = "";
		StringBuilder total = new StringBuilder();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		
		while((line = rd.readLine()) != null) {
			total.append(line);
		}
		
		return total;
	}
	
}

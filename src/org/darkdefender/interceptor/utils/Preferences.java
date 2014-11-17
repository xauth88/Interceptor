package org.darkdefender.interceptor.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

	public enum Preference {
		VK_TOKEN,
		VK_SECRET,
		USER_ID,
		TWICE_ESCAPE,
		TWICE_ESCAPE_UPDATE,
		FIRST_SYNC,
		LAST_LAT,
		LAST_LNG
	}
	
	public static void savePreferences(Context ctx, Preference key, String value) {
		SharedPreferences prefs = ctx.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(key.name(), value);
		editor.commit();
    }
	
	public static String readStringPreference(Context ctx, Preference key, String defaultNullValue) {
		SharedPreferences settings;
		settings = ctx.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
		return settings.getString(key.name(), defaultNullValue);
	}
	
	public static void saveBooleanPreferences(Context ctx, Preference key, boolean value) {
		SharedPreferences prefs = ctx.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean(key.name(), value);
		editor.commit();
    }
	
	public static boolean readBooleanPreference(Context ctx, Preference key, boolean defaultNullValue) {
		SharedPreferences settings;
		settings = ctx.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
		return settings.getBoolean(key.name(), defaultNullValue);
	}
	
}

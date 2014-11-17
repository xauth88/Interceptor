package org.darkdefender.interceptor.models;

public class VkAttachment {

	private int id;
	private String unique_id;
	private long mid;
	private String type;
	//IMAGE
	private String img_src;
	private String img_src_big;
	//VIDEO
	private String video_title;
	private String video_thumb;
	private String video_owner;
	private String video_id;
	//AUDIO
	private String audio_title;
	private String audio_artist;
	
	
	public String getUnique_id() {
		return unique_id;
	}
	public void setUnique_id(String unique_id) {
		this.unique_id = unique_id;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getMid() {
		return mid;
	}
	public void setMid(long mid) {
		this.mid = mid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getImg_src() {
		return img_src;
	}
	public void setImg_src(String img_src) {
		this.img_src = img_src;
	}
	public String getImg_src_big() {
		return img_src_big;
	}
	public void setImg_src_big(String img_src_big) {
		this.img_src_big = img_src_big;
	}
	public String getVideo_title() {
		return video_title;
	}
	public void setVideo_title(String video_title) {
		this.video_title = video_title;
	}
	public String getVideo_thumb() {
		return video_thumb;
	}
	public void setVideo_thumb(String video_thumb) {
		this.video_thumb = video_thumb;
	}
	public String getVideo_owner() {
		return video_owner;
	}
	public void setVideo_owner(String video_owner) {
		this.video_owner = video_owner;
	}
	public String getVideo_id() {
		return video_id;
	}
	public void setVideo_id(String video_id) {
		this.video_id = video_id;
	}
	public String getAudio_title() {
		return audio_title;
	}
	public void setAudio_title(String audio_title) {
		this.audio_title = audio_title;
	}
	public String getAudio_artist() {
		return audio_artist;
	}
	public void setAudio_artist(String audio_artist) {
		this.audio_artist = audio_artist;
	}
	
	
	
}

package org.darkdefender.interceptor.models;

public class VkUser {

	private long uid;
	private String first_name;
	private String last_name;
	private String photo;
	private boolean is_target;
	
	public boolean isIs_target() {
		return is_target;
	}
	public void setIs_target(boolean is_target) {
		this.is_target = is_target;
	}
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
	
}

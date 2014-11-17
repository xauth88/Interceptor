package org.darkdefender.interceptor.models;

public class VkMessages {

	private long id;
	private long mid;
	private long time;
	private boolean is_incoming;
	private long uid;
	private boolean has_attachment;
	private String attachment_type;
	private String body;
	
	public long getMid() {
		return mid;
	}
	public void setMid(long mid) {
		this.mid = mid;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public boolean isIncoming() {
		return is_incoming;
	}
	public void setIs_incoming(boolean is_incoming) {
		this.is_incoming = is_incoming;
	}
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public boolean hasAttachment() {
		return has_attachment;
	}
	public void setHas_attachment(boolean has_attachment) {
		this.has_attachment = has_attachment;
	}
	public String getAttachment_type() {
		return attachment_type;
	}
	public void setAttachment_type(String attachment_type) {
		this.attachment_type = attachment_type;
	}
	
}

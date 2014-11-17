package org.darkdefender.interceptor.models;


public class Position {

//	Date dNow = new Date( );
//    SimpleDateFormat ft = 
//    new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
//
//    System.out.println("Current Date: " + ft.format(dNow));

	private int id;
	private double lat;
	private double lng;
	private long time;
	private String date;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	
	
}

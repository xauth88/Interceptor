package org.darkdefender.interceptor.exceptions;

public class CustomNetworkException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;
	public CustomNetworkException(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}

}
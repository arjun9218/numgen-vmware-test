package com.vmwaretest.numgen.model;

import java.util.Date;

public class LoggingModel {
	private String action;
	private String api;
	private String method;
	private long timeMillis;
	private String message;
	
	LoggingModel() {}
	
	public LoggingModel(String action, String method, long time) {
		this.action = action;
		this.method = method;
		this.timeMillis = time;
	}
	
	public LoggingModel(String action, String api, String method, long time) {
		this.action = action;
		this.api = api;
		this.method = method;
		this.timeMillis = time;
	}
	
	public LoggingModel(String action, String api, String method, long time, String message) {
		this.action = action;
		this.api = api;
		this.method = method;
		this.timeMillis = time;
		this.message = message;
	}
	
	/**
	 * @return the api
	 */
	public String getApi() {
		return api;
	}
	
	/**
	 * @param api the api to set
	 */
	public void setApi(String api) {
		this.api = api;
	}
	
	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}
	
	/**
	 * @param method the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
	}
	
	/**
	 * @return the timeMillis
	 */
	public long getTimeMillis() {
		return timeMillis;
	}
	
	/**
	 * @param timeMillis the timeMillis to set
	 */
	public void setTimeMillis(long timeMillis) {
		this.timeMillis = timeMillis;
	}

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(action + " | time=" + new Date(timeMillis) + ", api=" + api + ", method=" + method);
		if (message != null) {
			sb.append(", message=" + message);
		}
		return sb.toString();
	} 
	
}

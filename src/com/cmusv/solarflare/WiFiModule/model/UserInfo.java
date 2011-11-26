package com.cmusv.solarflare.WiFiModule.model;

public class UserInfo {
	private String userName;
	private String userId;
	
	public UserInfo(String userName, String userId) {
		this.userId = userId;
		this.userName = userName;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
}

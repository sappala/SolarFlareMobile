package com.cmusv.solarflare.WiFiModule.model;

import android.os.Parcel;
import android.os.Parcelable;

public class UserInfo implements Parcelable {
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
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return userName;
	}

	@Override
	public boolean equals(Object o) {
		if(o instanceof UserInfo)
			if(userId.equals(((UserInfo) o).userId))
				return true;
			
		return false;
		
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(userName);
		dest.writeString(userId);
	}
	
	public static final Parcelable.Creator<UserInfo> CREATOR = new Parcelable.Creator<UserInfo>() {

		@Override
		public UserInfo createFromParcel(Parcel source) {
			return new UserInfo(source);
		}

		@Override
		public UserInfo[] newArray(int size) {
			return new UserInfo[size];
		}
	};
	
	private UserInfo(Parcel source) {
		userName = source.readString();
		userId   = source.readString();
	}
}

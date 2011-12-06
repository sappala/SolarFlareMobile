package com.cmusv.solarflare.WiFiModule.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Message implements Parcelable {
	private String senderId;
	private String receiverId;
	private String message;
	
	public Message() {
		
	}
	
	public String getSenderId() {
		return senderId;
	}
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	public String getReceiverId() {
		return receiverId;
	}
	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		return this.message;
	}

	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(senderId);
		dest.writeString(receiverId);
		dest.writeString(message);
	}
	
	public static final Parcelable.Creator<Message> CREATOR = new Parcelable.Creator<Message>() {
		@Override
		public Message createFromParcel(Parcel source) {
			return new Message(source);
		}

		@Override
		public Message[] newArray(int size) {
			return new Message[size];
		}
	};
	
	private Message(Parcel source) {
		senderId	= source.readString();
		receiverId	= source.readString();
		message		= source.readString();
	}
}

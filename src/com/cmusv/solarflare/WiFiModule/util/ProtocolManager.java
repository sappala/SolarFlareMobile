package com.cmusv.solarflare.WiFiModule.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Base64;

import com.cmusv.solarflare.WiFiModule.model.Message;
import com.cmusv.solarflare.WiFiModule.model.UserInfo;

public class ProtocolManager {
	
	
	private static final String KEY_ACTION		= "action";
	private static final String KEY_USERNAME	= "username";
	private static final String KEY_USERID		= "userid";
	private static final String KEY_USERS 		= "users";
	private static final String KEY_MESSAGE		= "message";
	private static final String KEY_SENDER_USERID	= "sender_userid";
	private static final String KEY_RECEIVER_USERID	= "receiver_userid";
	
	private static final String ACTION_CONNECT = "connect";
	
	public static JSONObject createOnConnectionMessage(UserInfo info) throws JSONException {
		JSONObject object = new JSONObject();
		object.put(KEY_ACTION, ACTION_CONNECT);
		object.put(KEY_USERNAME, info.getUserName());
		object.put(KEY_USERID, info.getUserId());
		return null;
	}
	
	public static String getMessageAction(JSONObject object) throws JSONException {
		return object.get(KEY_ACTION).toString();
	}
	
	public static List<UserInfo> parseUserInfoMessage(JSONObject object) throws JSONException {
		List<UserInfo> info = new ArrayList<UserInfo>();
		JSONArray users = object.getJSONArray(KEY_USERS);
		for(int i=0; i<users.length(); i++) {
			JSONObject jsonObj = users.getJSONObject(i);
			info.add(new UserInfo(jsonObj.getString(KEY_USERNAME), jsonObj.getString(KEY_USERID)));
		}
		return info;
	}
	
	public static Message parseMessage(JSONObject messageJSON) throws JSONException {
		Message message = new Message();
		byte[] decodedBytes = Base64.decode(messageJSON.getString(KEY_MESSAGE).getBytes(), Base64.DEFAULT);
		message.setMessage(new String(decodedBytes));
		message.setSenderId(messageJSON.getString(KEY_SENDER_USERID));
		message.setReceiverId(messageJSON.getString(KEY_RECEIVER_USERID));
		return message;
	}
	
	public static JSONObject createMessage(Message message) throws JSONException {
		JSONObject object = new JSONObject();
		object.put(KEY_MESSAGE, Base64.encode(message.toString().getBytes(), Base64.DEFAULT));
		object.put(KEY_SENDER_USERID, message.getSenderId());
		object.put(KEY_RECEIVER_USERID, message.getReceiverId());
		return object;
	}
}

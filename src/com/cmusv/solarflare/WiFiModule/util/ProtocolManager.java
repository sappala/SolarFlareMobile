package com.cmusv.solarflare.WiFiModule.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Base64;
import android.util.Log;

import com.cmusv.solarflare.WiFiModule.ChatActivity;
import com.cmusv.solarflare.WiFiModule.model.Message;
import com.cmusv.solarflare.WiFiModule.model.UserInfo;

public class ProtocolManager {
	
	public static JSONObject createOnConnectionMessage(UserInfo info) throws JSONException {
		JSONObject object = new JSONObject();
		object.put(Constants.KEY_ACTION, Constants.ACTION_CONNECT);
		object.put(Constants.KEY_USERNAME, info.getUserName());
		object.put(Constants.KEY_USERID, info.getUserId());
		return object;
	}
	
	public static String getMessageAction(JSONObject object) throws JSONException {
		return object.get(Constants.KEY_ACTION).toString();
	}
	
	public static List<UserInfo> parseUserInfoMessage(JSONObject object) throws JSONException {
		List<UserInfo> info = new ArrayList<UserInfo>();
		JSONArray users = object.getJSONArray(Constants.KEY_USERS);
		for(int i=0; i<users.length(); i++) {
			JSONObject jsonObj = users.getJSONObject(i);
			info.add(new UserInfo(jsonObj.getString(Constants.KEY_USERNAME), jsonObj.getString(Constants.KEY_USERID)));
			Log.d(Constants.LOG_TAG, "user name " + ChatActivity.userList.get(i).getUserName());
		}
		return info;
	}
	
	public static Message parseMessage(JSONObject messageJSON) throws JSONException {
		Message message = new Message();
		byte[] decodedBytes = Base64.decode(messageJSON.getString(Constants.KEY_MESSAGE).getBytes(), Base64.DEFAULT);
		message.setMessage(new String(decodedBytes));
		message.setSenderId(messageJSON.getString(Constants.KEY_SENDER_USERID));
		message.setReceiverId(messageJSON.getString(Constants.KEY_RECEIVER_USERID));
		return message;
	}
	
	public static JSONObject createMessage(Message message) throws JSONException {
		JSONObject object = new JSONObject();
		object.put(Constants.KEY_ACTION, Constants.KEY_USER_MESSAGE);
		object.put(Constants.KEY_SENDER_USERID, message.getSenderId());
		object.put(Constants.KEY_RECEIVER_USERID, message.getReceiverId());
		object.put(Constants.KEY_MESSAGE, Base64.encode(message.toString().getBytes(), Base64.DEFAULT));

		return object;
	}


}

package com.cmusv.solarflare.WiFiModule;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cmusv.solarflare.WiFiModule.model.Message;
import com.cmusv.solarflare.WiFiModule.model.UserInfo;
import com.cmusv.solarflare.WiFiModule.util.CommunicationHandler;
import com.cmusv.solarflare.WiFiModule.util.Constants;
import com.cmusv.solarflare.WiFiModule.util.ProtocolManager;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class SolarFlareIntentService extends IntentService {

	HashMap<String, String> userIdToName = new HashMap<String, String>(5);
	
	CommunicationHandler handler = CommunicationHandler.getInstance();
	
	UserInfo info = null;
	
	public SolarFlareIntentService() {
		super("SolarFlareIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub

		String action = intent.getStringExtra(Constants.KEY_ACTION);
		
		try {
			if(action.equals(Constants.KEY_ON_CONNECTION)){
				String username = intent.getStringExtra(Constants.KEY_USERNAME);
				info = new UserInfo(username, Integer.toString((int)System.currentTimeMillis()));
				JSONObject object = ProtocolManager.createOnConnectionMessage(info);
				handler.sendJSONObject(object);
			}
			else if(action.equals(Constants.KEY_USER_MESSAGE)){
				String userId = intent.getStringExtra(Constants.KEY_USERID);
				String messageString = intent.getStringExtra(Constants.KEY_USER_MESSAGE);
				Message message = new Message();
				message.setSenderId(info.getUserId());				// <--------------------- need to edit
				message.setReceiverId(userId);
				message.setMessage(messageString);
				JSONObject jsonObject = ProtocolManager.createMessage(message);
				handler.sendJSONObject(jsonObject);
				
			}
			else if (action.equals(Constants.KEY_START_WIFI_MESSAGE_HANDLER)) {
				
				/*
				 * Chinmay:-	Analyze the code 
				 * 			A new thread is created, where it waits for any message in  CommunicationHandler.java
				 * 			and then updates the state of the mobile
				 */
				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						handleMessagesFromWiFi();
					}
				}).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void handleMessagesFromWiFi() {
		JSONObject object = null;
		while (true) {
			synchronized (CommunicationHandler.receivedMessages) {
				if (!CommunicationHandler.receivedMessages.isEmpty()){
					object = CommunicationHandler.receivedMessages.remove(0);
					messageHandler(object);
				}
			}
		}
	}
	
	
	private static final int HELLO_ID = 1;
	
	public void messageHandler(JSONObject object){
		try {
			String action = object.getString(Constants.KEY_ACTION);
			if(action.equals(Constants.KEY_ADD_USER)){
				JSONArray array = object.getJSONArray(Constants.KEY_USERS);
				for(int i=0; i<array.length();i++){
					JSONObject obj = array.getJSONObject(i);
					String username = obj.getString(Constants.KEY_USERNAME);
					String userid = obj.getString(Constants.KEY_USERID);
					userIdToName.put(userid, username);
					/*
					 * Chinmay :- Whenever mobile gets 'adduser' message; the new username is added to ArrayAdapter's -> 'mAdapter'
					 * 				Hope the name dynamically shows up on screen 
					 */
					WiFiConfigurationActivity.mAdapter.add(new UserInfo(username, userid));
				}
			}
			else if (action.equals(Constants.KEY_REMOVE_USER)){
				JSONArray array = object.getJSONArray(Constants.KEY_USERS);
				for(int i=0; i<array.length();i++){
					JSONObject obj = array.getJSONObject(i);
					String username = obj.getString(Constants.KEY_USERNAME);
					String userid = obj.getString(Constants.KEY_USERID);
					userIdToName.remove(userid);
					/*
					 * Chinmay :- Whenever mobile gets 'removeuser' message; the old username is removed from ArrayAdapter's -> 'mAdapter'
					 * 				Hope the name is dynamically removed on screen 
					 */
					WiFiConfigurationActivity.mAdapter.remove(new UserInfo(username, userid));
				}
			}else if (action.equals(Constants.KEY_USER_MESSAGE)){
				
				/*
				 * Chinmay :- Cut copied the code for notification from Android developer example
				 * 				Please check once 
				 * 				
				 */
				
				Message message = ProtocolManager.parseMessage(object);
				String senderId = message.getSenderId();
				String messageContent = message.getMessage();
				
				String ns = Context.NOTIFICATION_SERVICE;
				NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
				int icon = R.drawable.icon;
				CharSequence tickerText = "From :- " + userIdToName.get(senderId);
				long when = System.currentTimeMillis();

				Notification notification = new Notification(icon, tickerText, when);
				notification.defaults =Notification.DEFAULT_ALL;
				 
				Context context = getApplicationContext();
				CharSequence contentTitle = "From :- " + userIdToName.get(senderId);
				CharSequence contentText = messageContent;
				Intent notificationIntent = new Intent(context, WiFiConfigurationActivity.class);
				PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

				notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
				

				mNotificationManager.notify(HELLO_ID, notification);
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

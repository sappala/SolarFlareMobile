package com.cmusv.solarflare.WiFiModule;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.cmusv.solarflare.WiFiModule.model.UserInfo;
import com.cmusv.solarflare.WiFiModule.util.CommunicationHandler;
import com.cmusv.solarflare.WiFiModule.util.ProtocolManager;

import android.app.IntentService;
import android.content.Intent;

public class SolarFlareIntentService extends IntentService {

	HashMap<String, String> userIdToName = new HashMap<String, String>(5);
	
	CommunicationHandler handler = CommunicationHandler.getInstance();
	
	public SolarFlareIntentService() {
		super("SolarFlareIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub

		String action = intent.getStringExtra("action");
		
		try {
			if(action.equals("onconnection")){
				String username = intent.getStringExtra("username");
				UserInfo info = new UserInfo(username, Integer.toString((int)System.currentTimeMillis()));
				JSONObject object = ProtocolManager.createOnConnectionMessage(info);
				handler.sendJSONObject(object);
			}
			else if(action.equals("usermessage")){
				String userId = intent.getStringExtra("userid");
				Intent messageIntent = new Intent(this, MessageScreen.class);
				messageIntent.putExtra("userid", userId);
				startActivity(messageIntent);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	/*
	 * TODO: implementation for addUsersToList()
	 * Need to call addUsersToList() to add users for display
	 */

}

package com.cmusv.solarflare.WiFiModule;

import android.app.IntentService;
import android.content.Intent;

import com.cmusv.solarflare.WiFiModule.model.UserInfo;
import com.cmusv.solarflare.WiFiModule.util.CommunicationHandler;

public class SolarFlareIntentService extends IntentService {
	UserInfo info = null;
	
	public static final String ACTION_SEND_DATA = "ACTION_SEND_DATA";
	
	public static final String EXTRA_DATA = "EXTRA_DATA";
	
	public SolarFlareIntentService() {
		super("SolarFlareIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		String action = intent.getAction(); 
		if(action.equals(ACTION_SEND_DATA)) {
			String dataString = intent.getStringExtra(EXTRA_DATA);
			CommunicationHandler.getInstance().sendData(dataString);
		}
	}
}

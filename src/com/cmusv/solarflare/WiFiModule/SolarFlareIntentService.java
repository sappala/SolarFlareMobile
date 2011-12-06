package com.cmusv.solarflare.WiFiModule;

import java.util.ArrayList;
import java.util.List;

import android.app.IntentService;
import android.content.Intent;
import android.os.Parcelable;

import com.cmusv.solarflare.WiFiModule.model.Message;
import com.cmusv.solarflare.WiFiModule.model.UserInfo;
import com.cmusv.solarflare.WiFiModule.util.CommunicationHandler;
import com.cmusv.solarflare.WiFiModule.util.ICommunicationCallback;

public class SolarFlareIntentService extends IntentService implements ICommunicationCallback {
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
			CommunicationHandler.getInstance(this).sendData(dataString);
		}
	}

	@Override
	public void broadCastUserInfo(List<UserInfo> info) {
		Intent intent = new Intent(ChatActivity.ACTION_USER_INFO_LIST);
		intent.putParcelableArrayListExtra(ChatActivity.EXTRA_USER_INFO, (ArrayList<? extends Parcelable>) info);
		sendStickyBroadcast(intent);
	}

	@Override
	public void broadMessage(Message message) {
		Intent intent = new Intent(ChatActivity.ACTION_MESSAGE);
		intent.putExtra(ChatActivity.EXTRA_MESSAGE, message);
		sendStickyBroadcast(intent);
	}

	@Override
	public void broadCastRemoveUserInfo(List<UserInfo> userInfos) {
		Intent intent = new Intent(ChatActivity.ACTION_REMOVE_USER_INFO_LIST);
		intent.putParcelableArrayListExtra(ChatActivity.EXTRA_USER_INFO, (ArrayList<? extends Parcelable>) userInfos);
		sendStickyBroadcast(intent);
	}
}

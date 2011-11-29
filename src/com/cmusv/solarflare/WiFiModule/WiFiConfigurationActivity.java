package com.cmusv.solarflare.WiFiModule;

import java.util.List;

import com.cmusv.solarflare.WiFiModule.constant.SharedPrefConstant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class WiFiConfigurationActivity extends Activity {
	
	public static final String EXTRA_SSID_NAME = "SSIDName";
	
	private static final int REQUEST_USERNAME = 101;
	
	private Button mStartChatButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		mStartChatButton = (Button) findViewById(R.id.startChatButton);
		mStartChatButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(WiFiConfigurationActivity.this, ChatActivity.class));
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		List<WifiConfiguration> scanResults = wifi.getConfiguredNetworks();
		
		for(WifiConfiguration result : scanResults) {
			if(result.SSID.equals(getResources().getString(R.string.preconfiguredSSID)));
				wifi.enableNetwork(result.networkId, true);
		}
		
		SharedPreferences settings = getSharedPreferences(SharedPrefConstant.PREF_NAME, 0);
		String username = settings.getString(SharedPrefConstant.USERNAME, null);
		
		if(username == null) {
			Intent i = new Intent(WiFiConfigurationActivity.this, UserDialogActivity.class);
			startActivityForResult(i, REQUEST_USERNAME);
		} else {
			enableButton(username);
		}
		
//		try {
//
//			/*
//			 * TODO: Ask a user to enter his username
//			 */
//			
//			notifyWifiServer("sandy");	// Chinmay <-- Need to do dialog boxes to get user name and give them as input to this function
//			startWiFiMessageHandler();
//			
//			/*
//			 *  mAdapter list is automatically added by calling
//			 *  callbacks addUserToList() and removeUserFromList()
//			 */
//			
//			// TODO: Below dummy additions must be removed
//			mAdapter.add(new UserInfo("Maneesh", "Maneesh"));
//			mAdapter.add(new UserInfo("sandy", "sandy"));
//			mAdapter.add(new UserInfo("buzzy", "buzzy"));
//			
//		
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}

	private void enableButton(String username) {
		mStartChatButton.setEnabled(true);
		mStartChatButton.setText("Start chat as " + username);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == REQUEST_USERNAME && data != null) {
			
			SharedPreferences settings = getSharedPreferences(SharedPrefConstant.PREF_NAME, 0);
			SharedPreferences.Editor editor = settings.edit();
			String username = data.getStringExtra(UserDialogActivity.EXTRA_USERNAME);
			editor.putString(SharedPrefConstant.USERNAME, username);
			editor.commit();
			
			enableButton(username);
		}
	}
	
	
	
//    private void startWiFiMessageHandler() {
//		// TODO Auto-generated method stub
//		Intent notifyIntent = new Intent(this,SolarFlareIntentService.class);
//		notifyIntent.putExtra(Constants.KEY_ACTION, Constants.KEY_START_WIFI_MESSAGE_HANDLER);
//		startService(notifyIntent);
//	}
//
//
//
//	private BroadcastReceiver receiver = new BroadcastReceiver() {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            // TODO Auto-generated method stub
//            Toast.makeText(getApplicationContext(), "received", Toast.LENGTH_SHORT);
//
//        }
//    };
//
//	@Override
//	protected void onListItemClick(ListView l, View v, int position, long id) {
//		// TODO Auto-generated method stub
//		super.onListItemClick(l, v, position, id);
//		UserInfo userInfo = (UserInfo) getListAdapter().getItem(position);
//		Intent notifyIntent = new Intent(this,MessageScreen.class);
//		notifyIntent.putExtra(Constants.KEY_USERID, userInfo.getUserId());
//		startService(notifyIntent);
//		
//	}
//	
//
//	/*
//	 * Chinmay:- This is a callback method that adds to list of usernames
//	 */
//	void addUsersToList(UserInfo info){
//		mAdapter.add(info);
//	}
//
//	/*
//	 * Chinmay:- This is a callback method that removes from list of usernames
//	 */
//	void removeAllUsersFromList(UserInfo info){
//		mAdapter.remove(info);
//	}
//	
//	private void notifyWifiServer(String username) {
//		// TODO Auto-generated method stub
//		Intent notifyIntent = new Intent(this,SolarFlareIntentService.class);
//		notifyIntent.putExtra(Constants.KEY_ACTION, Constants.KEY_ON_CONNECTION);
//		notifyIntent.putExtra(Constants.KEY_USERNAME, username); // TODO: Ask a user for his username 
//		startService(notifyIntent);
//	}

}



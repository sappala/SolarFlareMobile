package com.cmusv.solarflare.WiFiModule;

import java.util.List;

import com.cmusv.solarflare.WiFiModule.constant.SharedPrefConstant;
import com.cmusv.solarflare.WiFiModule.util.CommunicationHandler;

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
		//CommunicationHandler.getInstance();
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

}



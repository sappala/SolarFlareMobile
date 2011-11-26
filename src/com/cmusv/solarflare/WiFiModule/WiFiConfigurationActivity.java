package com.cmusv.solarflare.WiFiModule;

import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class WiFiConfigurationActivity extends ListActivity {
	
	public static final String EXTRA_SSID_NAME = "SSIDName"; 
	private ArrayAdapter<String> mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		mAdapter = new ArrayAdapter<String>(WiFiConfigurationActivity.this, android.R.layout.simple_list_item_1);
		setListAdapter(mAdapter);
	}

	@Override
	protected void onStart() {
		super.onStart();
		WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		List<WifiConfiguration> scanResults = wifi.getConfiguredNetworks();
		
		for(WifiConfiguration result : scanResults) {
			mAdapter.add(result.SSID);
			if(result.SSID.equals(getResources().getString(R.string.preconfiguredSSID)));
				wifi.enableNetwork(result.networkId, true);
		}
	}
}
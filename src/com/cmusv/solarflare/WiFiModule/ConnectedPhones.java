package com.cmusv.solarflare.WiFiModule;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;

public class ConnectedPhones extends Activity {
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.connectedphones);
	String SSIDName = getIntent().getExtras().getString("SSIDName");
	
	this.WiFiConfiguration(SSIDName);
	
	}

	private void WiFiConfiguration (String SSIDName) {
		
		WifiManager wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
		
		
		WifiConfiguration wc = new WifiConfiguration();
		wc.SSID = "\"" + SSIDName  + "\"";
		wc.preSharedKey  = "\"Rmjk@193()\"";
		wc.hiddenSSID = true;
		wc.status = WifiConfiguration.Status.ENABLED;        
		wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
		wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
		wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
		wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
		wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
		wc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
		int res = wifi.addNetwork(wc);
		System.out.println(("WifiPreference" + "add Network returned " + res ));
		boolean b = wifi.enableNetwork(res, true);        
		System.out.println("WifiPreference" + "enableNetwork returned " + b );

	}

}

package com.cmusv.solarflare.WiFiModule;

import java.io.OutputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import com.cmusv.solarflare.WiFiModule.model.UserInfo;
import com.cmusv.solarflare.WiFiModule.util.CommunicationHandler;
import com.cmusv.solarflare.WiFiModule.util.Constants;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class WiFiConfigurationActivity extends ListActivity {
	
	public static final String EXTRA_SSID_NAME = "SSIDName"; 
	public static ArrayAdapter<UserInfo> mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		mAdapter = new ArrayAdapter<UserInfo>(WiFiConfigurationActivity.this, android.R.layout.simple_list_item_1);
		setListAdapter(mAdapter);
	}

	@Override
	protected void onStart() {
		super.onStart();
		WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		List<WifiConfiguration> scanResults = wifi.getConfiguredNetworks();
		
		for(WifiConfiguration result : scanResults) {
			//mAdapter.add(result.SSID);
			if(result.SSID.equals(getResources().getString(R.string.preconfiguredSSID)));
				wifi.enableNetwork(result.networkId, true);
		}
		
		try {

			/*
			 * TODO: Ask a user to enter his username
			 */
			
			notifyWifiServer("sandy");	// Chinmay <-- Need to do dialog boxes to get user name and give them as input to this function
			startWiFiMessageHandler();
			
			/*
			 *  mAdapter list is automatically added by calling
			 *  callbacks addUserToList() and removeUserFromList()
			 */
			
			// TODO: Below dummy additions must be removed
			mAdapter.add(new UserInfo("Maneesh", "Maneesh"));
			mAdapter.add(new UserInfo("sandy", "sandy"));
			mAdapter.add(new UserInfo("buzzy", "buzzy"));
			
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
    private void startWiFiMessageHandler() {
		// TODO Auto-generated method stub
		Intent notifyIntent = new Intent(this,SolarFlareIntentService.class);
		notifyIntent.putExtra(Constants.KEY_ACTION, Constants.KEY_START_WIFI_MESSAGE_HANDLER);
		startService(notifyIntent);
	}



	private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            Toast.makeText(getApplicationContext(), "received", Toast.LENGTH_SHORT);

        }
    };

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		UserInfo userInfo = (UserInfo) getListAdapter().getItem(position);
		Intent notifyIntent = new Intent(this,MessageScreen.class);
		notifyIntent.putExtra(Constants.KEY_USERID, userInfo.getUserId());
		startService(notifyIntent);
		
	}
	

	/*
	 * Chinmay:- This is a callback method that adds to list of usernames
	 */
	void addUsersToList(UserInfo info){
		mAdapter.add(info);
	}

	/*
	 * Chinmay:- This is a callback method that removes from list of usernames
	 */
	void removeAllUsersFromList(UserInfo info){
		mAdapter.remove(info);
	}
	
	private void notifyWifiServer(String username) {
		// TODO Auto-generated method stub
		Intent notifyIntent = new Intent(this,SolarFlareIntentService.class);
		notifyIntent.putExtra(Constants.KEY_ACTION, Constants.KEY_ON_CONNECTION);
		notifyIntent.putExtra(Constants.KEY_USERNAME, username); // TODO: Ask a user for his username 
		startService(notifyIntent);
	}

}



package com.cmusv.solarflare.WiFiModule;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

public class WiFiScanning extends Activity {
    /** Called when the activity is first created. */
	WiFiScanning thisWIFI = this;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        /*Toast msg = Toast.makeText(getApplicationContext(), "AFTER ",Toast.LENGTH_LONG);
        msg.setGravity(Gravity.CENTER, msg.getXOffset()/2, msg.getYOffset()/2);
        msg.show();*/
        Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		        Toast msg = Toast.makeText(getApplicationContext(), "Before ",Toast.LENGTH_LONG);
		        msg.setGravity(Gravity.CENTER, msg.getXOffset()/2, msg.getYOffset()/2);
		        msg.show();
				ListView showSSIDList = null;
				String service = Context.WIFI_SERVICE;
		    	WifiManager wifi = (WifiManager)getSystemService(service);
		    	List<ScanResult> Result = wifi.getScanResults();
		    	String arraySSID [] = new String[Result.size()] ;
		    	int i=0;
		    	for (Iterator iter = Result.iterator();iter.hasNext();){
		    	  		ScanResult scanResult = (ScanResult) iter.next();
		    	  		arraySSID[i++] = scanResult.SSID ;
		   	  		
		    	}
		    	showSSIDList = (ListView) findViewById(R.id.listView1);
		    	
		        ArrayAdapter<String> array = new ArrayAdapter<String>(thisWIFI, android.R.layout.simple_list_item_1,arraySSID);
		        showSSIDList.setAdapter(array);
		        try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
				int SSID = arraySSID.length ;
		        msg = Toast.makeText(getApplicationContext(), "AFTER + " + SSID,Toast.LENGTH_LONG);
		        msg.setGravity(Gravity.CENTER, msg.getXOffset()/2, msg.getYOffset()/2);
		        msg.show();			}
		});
        
    }
    
    @Override
    protected void onStart() {
    	// TODO Auto-generated method stub
    	super.onStart();
    	
    }

}
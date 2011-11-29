package com.cmusv.solarflare.WiFiModule.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;

public class CommunicationHandler {

	private Socket socket;
	private static CommunicationHandler handler;
	private BufferedWriter writer;
	private BufferedReader reader;
	
	public static List<JSONObject> receivedMessages;
	
	char buffer[];

	public CommunicationHandler() {
		resetConnection();
		startReadLoop();
	}
	
	public static CommunicationHandler getInstance(){
		if(handler == null)
			handler = new CommunicationHandler();
		return handler;
	}
	
	private void startReadLoop() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO: Smarter thread management
				while(true) {
					checkData();
				}
			}
		}).start();
	}
	
	public void sendData(String data) {
		Log.d("SolarFlare", "Sending: " + data);
		try {
			writer.write(data + "\n");
			writer.flush();
		} catch (Exception e) {
			Log.e(Constants.LOG_TAG, "Unable to send data over the wire");
			resetConnection();
		}
	}

	public void checkData() {
		char buffer[] = new char[2048];
		try {
			int read = reader.read(buffer);		
			if(read == -1)
				return;
			
			String json = new String(buffer, 0, read);
			String messageAction = ProtocolManager.getMessageAction(new JSONObject(json));
			if(messageAction.equals(Constants.KEY_ADD_USER)) {
				Log.d(Constants.LOG_TAG, "Adding user");
			}
		} catch (JSONException ex) {
			Log.e(Constants.LOG_TAG, "JSON Exception while trying to parse string");
		}
		catch (Exception e) {
			Log.e(Constants.LOG_TAG, "Unable to read data over the wire");
			resetConnection();
		}
	}
	
	private void resetConnection() {
		try {
			socket.close();
		} catch (Exception e) {  }
		
		socket = null;
		writer = null;
		reader = null;
		
		try {
			InetAddress serverAddress = InetAddress.getByName(Constants.WIFI_SERVER_IP);
			socket = new Socket(serverAddress, Constants.WIFI_SERVER_PORT);
			buffer = new char[1000];
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (Exception e) {
			Log.e(Constants.LOG_TAG, "Exception in TCP server: " + e.getMessage());
		}
	}
}

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

import com.cmusv.solarflare.WiFiModule.ChatActivity;

import android.util.Log;

public class CommunicationHandler {

	private Socket socket;
	private static CommunicationHandler handler;
	private OutputStreamWriter writer;
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
		Log.i("SolarFlare", "Sending: " + data);
		try {
			//socket.sendUrgentData(1);
			writer.write(data + "\n");
			writer.flush();
			//writer.close();
			
		} catch (Exception e) {
			Log.e(Constants.LOG_TAG, "Unable to send data over the wire" + e.getMessage());
			//resetConnection();
		}
	}

	public void checkData() {
		char buffer[] = new char[2048];
		//Log.i(Constants.LOG_TAG, "read buffer " + buffer);
		try {
			int read = reader.read(buffer);		
			if(read == -1)
				return;
			Log.i(Constants.LOG_TAG, "read buffer " + buffer);
			String json = new String(buffer, 0, read);
			Log.i(Constants.LOG_TAG, "string read " + json);
			JSONObject jsonObject = new JSONObject(json);
			String messageAction = ProtocolManager.getMessageAction(jsonObject);
			if(messageAction.equals(Constants.KEY_ADD_USER)) {
				Log.i(Constants.LOG_TAG, "Adding user");
				ChatActivity.userList.addAll(ProtocolManager.parseUserInfoMessage(jsonObject));
			}
		} catch (JSONException ex) {
			Log.e(Constants.LOG_TAG, "JSON Exception while trying to parse string");
		}
		catch (Exception e) {
			Log.e(Constants.LOG_TAG, "Unable to read data over the wire ----" + buffer + "-----------");
			Log.e(Constants.LOG_TAG, e.getMessage());
			//resetConnection();
		}
	}
	
	private void resetConnection() {
		try {
		/*	if ((socket != null) && (socket.isConnected()))
			socket.close();*/
		} catch (Exception e) {  }
		
		socket = null;
		writer = null;
		reader = null;
		
		try {
			InetAddress serverAddress = InetAddress.getByName(Constants.WIFI_SERVER_IP);
			socket = new Socket(serverAddress, Constants.WIFI_SERVER_PORT);
			socket.setSendBufferSize(5);
			socket.setTcpNoDelay(true);
			buffer = new char[1000];
			writer = new OutputStreamWriter(socket.getOutputStream());
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (Exception e) {
			Log.e(Constants.LOG_TAG, "Exception in TCP server: " + e.getMessage());
		}
	}
}

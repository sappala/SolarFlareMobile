package com.cmusv.solarflare.WiFiModule.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.cmusv.solarflare.WiFiModule.model.Message;
import com.cmusv.solarflare.WiFiModule.model.UserInfo;

public class CommunicationHandler {

	private Socket socket;
	private static CommunicationHandler handler;
	private BufferedOutputStream writer;
	private BufferedInputStream reader;
	private ICommunicationCallback mCallback;
	
	public static List<JSONObject> receivedMessages;
	
	char buffer[];

	private CommunicationHandler() {
		resetConnection();
		startReadLoop();
	}
	
	public static CommunicationHandler getInstance(ICommunicationCallback callback){
		if(handler == null)
			handler = new CommunicationHandler();
		
		handler.mCallback = callback;
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
			data = data + "\n";
			writer.write(data.getBytes());
			writer.flush();
		} catch (Exception e) {
			Log.e(Constants.LOG_TAG, "Unable to send data over the wire");
		}
	}

	public void checkData() {
		byte buffer[] = new byte[2048];
		try {
			int read = reader.read(buffer);
			if(read == -1)
				return;
			
			JSONObject json = new JSONObject(new String(buffer, 0, read));
			String messageAction = ProtocolManager.getMessageAction(json);
			if(messageAction.equals(Constants.KEY_ADD_USER)) {
				Log.d(Constants.LOG_TAG, "Adding user");
				List<UserInfo> userInfos = ProtocolManager.parseUserInfoMessage(json);
				mCallback.broadCastUserInfo(userInfos);
			} else if(messageAction.equals(Constants.KEY_REMOVE_USER)) {
					Log.d(Constants.LOG_TAG, "Adding user");
					List<UserInfo> userInfos = ProtocolManager.parseUserInfoMessage(json);
					mCallback.broadCastUserInfo(userInfos);
			} else if(messageAction.equals(Constants.KEY_USER_MESSAGE)) {
				Log.d(Constants.LOG_TAG, "Receiving message");
				Message message = ProtocolManager.parseMessage(json);
				mCallback.broadMessage(message);
			}
		} catch (JSONException ex) {
			Log.e(Constants.LOG_TAG, "JSON Exception while trying to parse string");
		}
		catch (Exception e) {
			Log.e(Constants.LOG_TAG, "Unable to read data over the wire");
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
			writer = new BufferedOutputStream(socket.getOutputStream());
			reader = new BufferedInputStream(socket.getInputStream());
		} catch (Exception e) {
			Log.e(Constants.LOG_TAG, "Exception in TCP server: " + e.getMessage());
		}
	}
}

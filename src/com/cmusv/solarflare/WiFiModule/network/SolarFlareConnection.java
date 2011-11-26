package com.cmusv.solarflare.WiFiModule.network;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

public class SolarFlareConnection {
	private static final String MESSAGE_BUNDLE_DATA_KEY = "MESSAGE_BUNDLE_DATA_KEY"; 
	private static SolarFlareConnection connection = null;
	private ConnectionHandler mHandler;
	
	private final class ConnectionHandler extends Handler {
		public ConnectionHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			// TODO: Handle message
		}
	}
	
	public SolarFlareConnection() {
		HandlerThread thread = new HandlerThread("SolarFlareConnectionHandler", HandlerThread.MAX_PRIORITY);
		thread.start();
		mHandler = new ConnectionHandler(thread.getLooper());
	}
	
	public static void sendData(String message) {
		if(connection == null)
			connection = new SolarFlareConnection();
		
		Message msg = new Message();
		Bundle messageBundle = new Bundle();
		messageBundle.putString(MESSAGE_BUNDLE_DATA_KEY, message);
		msg.setData(messageBundle);
		connection.getHandler().sendMessage(msg);
	}

	public ConnectionHandler getHandler() {
		return mHandler;
	}
}

package com.cmusv.solarflare.WiFiModule.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

import org.json.JSONObject;

public class CommunicationHandler {

	Socket socket;
	static CommunicationHandler handler=null;
	BufferedWriter writer;
	BufferedReader reader;
	public static List<JSONObject> receivedMessages;
	char buffer[];

	public CommunicationHandler() {
		// TODO Auto-generated constructor stub
		try {
			
			InetAddress serverAddress = InetAddress.getByName(Constants.WIFI_SERVER_IP);
			socket = new Socket(serverAddress, Constants.WIFI_SERVER_PORT);
			
			buffer = new char[1000];
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static CommunicationHandler getInstance(){
		if(handler == null)
			handler = new CommunicationHandler();
		return handler;
	}
	
	public void readJSONObject() {

		JSONObject object = null;
		try {
			while (true) {
				int size = reader.read(buffer);
				if(size != -1){
					object = new JSONObject(new String(buffer,0,size-1));
					receivedMessages.add(object);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return;
	}	

	public void sendJSONObject(JSONObject object) {
		
		try {
			//writer.write(object.toString().length());
			writer.write(object.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}

package com.cmusv.solarflare.WiFiModule.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.json.JSONObject;

public class CommunicationHandler {

	Socket socket;
	static CommunicationHandler handler=null;
	BufferedWriter writer;
	BufferedReader reader;
	
	public CommunicationHandler() {
		// TODO Auto-generated constructor stub
		try {
			
			InetAddress serverAddress = InetAddress.getByName(Constants.WIFI_SERVER_IP);
			socket = new Socket(serverAddress, Constants.WIFI_SERVER_PORT);
			
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
	
	public JSONObject readJSONObject() {
		
		char buffer [];
		JSONObject object=null;
		try {
			int size = reader.read();
			buffer = new char [size];
			reader.read(buffer);
			object = new JSONObject(new String(buffer));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return object;
	}	

	public void sendJSONObject(JSONObject object) {
		
		try {
			writer.write(object.toString().length());
			writer.write(object.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}

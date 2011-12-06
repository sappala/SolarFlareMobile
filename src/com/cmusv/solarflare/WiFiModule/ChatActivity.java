package com.cmusv.solarflare.WiFiModule;

import java.util.Currency;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.cmusv.solarflare.WiFiModule.constant.SharedPrefConstant;
import com.cmusv.solarflare.WiFiModule.model.Message;
import com.cmusv.solarflare.WiFiModule.model.UserInfo;
import com.cmusv.solarflare.WiFiModule.util.Constants;
import com.cmusv.solarflare.WiFiModule.util.ProtocolManager;

public class ChatActivity extends Activity implements TextWatcher {

	public static final String ACTION_USER_INFO_LIST = "ACTION_USER_INFO_LIST";
	public static final String ACTION_REMOVE_USER_INFO_LIST = "ACTION_REMOVE_USER_INFO_LIST";
	public static final String EXTRA_USER_INFO = "EXTRA_USER_INFO";
	public static final String ACTION_MESSAGE = "ACTION_MESSAGE";
	public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
	
	private Spinner mReceiversSpinner;
	private Button mSendButton;
	private EditText mMessageBox;
	private TextView mPreviousMessagesText;
	
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			removeStickyBroadcast(intent);
			
			if(intent.getAction().equals(ACTION_USER_INFO_LIST)) {
				List<UserInfo> userInfo = intent.getParcelableArrayListExtra(EXTRA_USER_INFO);
				Log.d(Constants.LOG_TAG, "Adding " + userInfo.size() + " users");
				for(int i=0; i<userInfo.size(); i++)
					getSpinnerAdapter().add(userInfo.get(i));
			} else if(intent.getAction().equals(ACTION_REMOVE_USER_INFO_LIST)) {
				List<UserInfo> userInfo = intent.getParcelableArrayListExtra(EXTRA_USER_INFO);
				Log.d(Constants.LOG_TAG, "Removing " + userInfo.size() + " users");
				for(int i=0; i<userInfo.size(); i++) {
					ArrayAdapter<UserInfo> adapter = getSpinnerAdapter();
					for(int j=0; j<adapter.getCount(); j++) {
						if(adapter.getItem(j).getUserId().equals(userInfo.get(i).getUserId())) {
							adapter.remove(adapter.getItem(j));
							break;
						}
					}
				}
			} else if(intent.getAction().equals(ACTION_MESSAGE)) {
				Message message = intent.getParcelableExtra(EXTRA_MESSAGE);
				if (message.getMessage().equals(Constants.KEY_TEST_MESSAGE_SEND)){
					String senderId = message.getSenderId();
					message.setSenderId(message.getReceiverId());
					message.setReceiverId(senderId);
					message.setMessage(Constants.KEY_TEST_MESSAGE_RECEIVE);
					sendMessage(message);
					return;
				}
				else if (message.getMessage().equals(Constants.KEY_TEST_MESSAGE_RECEIVE)){
					message.setMessage("System.currentTimeMillis() : receive " + System.currentTimeMillis());
				}
					
				String text = getNameForId(message.getSenderId()) + " says: " + message.getMessage() + "\n" + mPreviousMessagesText.getText();
				mPreviousMessagesText.setText(text);
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_screen);
		
		mReceiversSpinner = (Spinner) findViewById(R.id.receiverSpinner);
		mSendButton = (Button) findViewById(R.id.sendMessageButton);
		mMessageBox = (EditText) findViewById(R.id.messageBox);
		mPreviousMessagesText = (TextView) findViewById(R.id.previousMessages);
		
		ArrayAdapter<UserInfo> info = new ArrayAdapter<UserInfo>(ChatActivity.this, android.R.layout.simple_spinner_item);
		info.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mReceiversSpinner.setAdapter(info);
		
		mMessageBox.addTextChangedListener(this);
		
		mSendButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(getSpinnerAdapter().getCount() == 0)
					return;
				if (mMessageBox.getText().toString().equals(Constants.KEY_TEST_MESSAGE_SEND)){
					mPreviousMessagesText.setText("System.currentTimeMillis() : send " + System.currentTimeMillis());
				}
				sendMessage(mMessageBox.getText().toString(), (UserInfo)mReceiversSpinner.getSelectedItem());
				mMessageBox.setText("");
			}
		});
		
		sendInitialConnectionMessage();
	}

	private void sendInitialConnectionMessage() {
		SharedPreferences settings = getSharedPreferences(SharedPrefConstant.PREF_NAME, 0);
		String username = settings.getString(SharedPrefConstant.USERNAME, null);
		long currentTimeMillis = System.currentTimeMillis();
		UserInfo info = new UserInfo(username, currentTimeMillis + "");
		getSharedPreferences(SharedPrefConstant.PREF_NAME, 0).edit().putString(SharedPrefConstant.USERNAME_ID, currentTimeMillis + "").commit();
		try {
			JSONObject connectionMessage = ProtocolManager.createOnConnectionMessage(info);
			sendDataToIntentService(connectionMessage.toString());
		} catch (JSONException e) {
			Log.e(Constants.LOG_TAG, "Could not construct connection message");
		}
	}

	private void sendDataToIntentService(String data) {
		Intent intent = new Intent(ChatActivity.this, SolarFlareIntentService.class);
		intent.setAction(SolarFlareIntentService.ACTION_SEND_DATA);
		intent.putExtra(SolarFlareIntentService.EXTRA_DATA, data);
		startService(intent);
	}

	private void sendMessage(String messageString, UserInfo selectedItem) {
		Message message = new Message();
		message.setMessage(messageString);
		message.setSenderId(savedUserId());
		message.setReceiverId(selectedItem.getUserId());
		JSONObject json;
		try {
			json = ProtocolManager.createMessage(message);
			Intent intent = new Intent(ChatActivity.this, SolarFlareIntentService.class);
			intent.setAction(SolarFlareIntentService.ACTION_SEND_DATA);
			intent.putExtra(SolarFlareIntentService.EXTRA_DATA, json.toString());
			startService(intent);
		} catch (JSONException e) {
			Log.d(Constants.LOG_TAG, "JSON exception while contructing message to send");
		}
	}

	
	private void sendMessage(Message message) {

		JSONObject json;
		try {
			json = ProtocolManager.createMessage(message);
			Intent intent = new Intent(ChatActivity.this, SolarFlareIntentService.class);
			intent.setAction(SolarFlareIntentService.ACTION_SEND_DATA);
			intent.putExtra(SolarFlareIntentService.EXTRA_DATA, json.toString());
			startService(intent);
		} catch (JSONException e) {
			Log.d(Constants.LOG_TAG, "JSON exception while contructing message to send");
		}
	}
	
	private String savedUserId() {
		return getSharedPreferences(SharedPrefConstant.PREF_NAME, 0).getString(SharedPrefConstant.USERNAME_ID, "Anonymous");
	}

	@Override
	public void afterTextChanged(Editable s) {
		mSendButton.setEnabled(mMessageBox.getText().length() != 0);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}
	
	@SuppressWarnings("unchecked")
	private ArrayAdapter<UserInfo> getSpinnerAdapter() {
		return (ArrayAdapter<UserInfo>)mReceiversSpinner.getAdapter();
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(mReceiver);
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(mReceiver, new IntentFilter(ACTION_USER_INFO_LIST));
		registerReceiver(mReceiver, new IntentFilter(ACTION_REMOVE_USER_INFO_LIST));
		registerReceiver(mReceiver, new IntentFilter(ACTION_MESSAGE));
	}
	
	private String getNameForId(String userId) {
		for(int i=0; i<getSpinnerAdapter().getCount(); i++) {
			if(getSpinnerAdapter().getItem(i).getUserId().equals(userId))
				return getSpinnerAdapter().getItem(i).getUserName();
		}
		return "Anonymous";
	}
}

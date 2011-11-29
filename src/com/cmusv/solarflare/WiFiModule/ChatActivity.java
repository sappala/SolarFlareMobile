package com.cmusv.solarflare.WiFiModule;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
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
import com.cmusv.solarflare.WiFiModule.model.UserInfo;
import com.cmusv.solarflare.WiFiModule.util.Constants;
import com.cmusv.solarflare.WiFiModule.util.ProtocolManager;

public class ChatActivity extends Activity implements TextWatcher {

	private Spinner mReceiversSpinner;
	private Button mSendButton;
	private EditText mMessageBox;
	private TextView mPreviousMessagesText;
	
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
		getSpinnerAdapter().add(new UserInfo("All", "All"));
		
		mMessageBox.addTextChangedListener(this);
		
		mSendButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendMessage(mMessageBox.getText().toString(), (UserInfo)mReceiversSpinner.getSelectedItem());
				mMessageBox.setText("");
			}
		});
		
		sendInitialConnectionMessage();
	}

	private void sendInitialConnectionMessage() {
		SharedPreferences settings = getSharedPreferences(SharedPrefConstant.PREF_NAME, 0);
		String username = settings.getString(SharedPrefConstant.USERNAME, null);
		UserInfo info = new UserInfo(username, System.currentTimeMillis() + "");
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

	private void sendMessage(String string, UserInfo selectedItem) {
		
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
	
	private ArrayAdapter<UserInfo> getSpinnerAdapter() {
		return (ArrayAdapter<UserInfo>)mReceiversSpinner.getAdapter();
	}
	
}

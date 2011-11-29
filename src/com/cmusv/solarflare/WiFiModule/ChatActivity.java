package com.cmusv.solarflare.WiFiModule;

import com.cmusv.solarflare.WiFiModule.model.UserInfo;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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

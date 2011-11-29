package com.cmusv.solarflare.WiFiModule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class UserDialogActivity extends Activity implements TextWatcher {

	public static final String EXTRA_USERNAME = "EXTRA_USERNAME";
	EditText mUserInputText;
	Button mSubmitButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onResume();
		setContentView(R.layout.dialog);
		
		mUserInputText = (EditText) findViewById(R.id.usernameInput);
		mSubmitButton = (Button) findViewById(R.id.submitButton);
		
		mUserInputText.addTextChangedListener(this);
		mSubmitButton.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra(EXTRA_USERNAME, mUserInputText.getText().toString());
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}

	
	@Override
	public void afterTextChanged(Editable s) {
		mSubmitButton.setEnabled(s.length() != 0);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {  }

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {  }
	
}

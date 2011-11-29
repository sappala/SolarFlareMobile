package com.cmusv.solarflare.WiFiModule;

import com.cmusv.solarflare.WiFiModule.util.Constants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MessageScreen extends Activity {

	EditText editText=null;
	Button button=null;
	String userid=null;
	MessageScreen thisInstance=this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messagescreen);
		
		
		editText = (EditText)findViewById(R.id.widget34);
		button = (Button)findViewById(R.id.widget35);
		userid = getIntent().getExtras().getString(Constants.KEY_USERID);
		
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String editedString = editText.getText().toString();
				Intent intent = new Intent(thisInstance, SolarFlareIntentService.class);
				intent.putExtra(Constants.KEY_ACTION, Constants.KEY_USER_MESSAGE);
				intent.putExtra(Constants.KEY_USERID, userid);
				intent.putExtra(Constants.KEY_USER_MESSAGE, editedString);
				startService(intent);
			}
		});
		
	}
	
}

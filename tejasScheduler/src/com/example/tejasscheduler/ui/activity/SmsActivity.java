package com.example.tejasscheduler.ui.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;

import com.example.tejasscheduler.DisplaySchedules;
import com.example.tejasscheduler.model.ActiveSchedule;
import com.example.tejasscheduler.utils.MapperUtils;

public class SmsActivity extends Activity {

	ActiveSchedule inputModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.inputModel = (ActiveSchedule) getIntent().getSerializableExtra(
				"activeSchedule");
		String message = (inputModel.getText() != null) ? inputModel.getText().toString() : "";
		String mobileNumber = MapperUtils.getPhoneNumber(getApplicationContext(), inputModel.getContactId());
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(mobileNumber, null, message, null, null);
		moveTaskToBack(true);
		callHomeScreen();
		moveTaskToBack(true);
	}
	                    
	private void callHomeScreen()
	{
		Intent i = new Intent(SmsActivity.this,DisplaySchedules.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
	}
}
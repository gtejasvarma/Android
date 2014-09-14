package com.example.tejasscheduler;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.example.tejasscheduler.model.ActiveSchedule;
import com.example.tejasscheduler.utils.MapperUtils;

public class CallActivity extends Activity {

	ActiveSchedule inputModel;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.inputModel = (ActiveSchedule) getIntent().getSerializableExtra(
				"activeSchedule");
		String phoneNumber = MapperUtils.getPhoneNumber(
				getApplicationContext(), inputModel.getContactId());
		String numb = "tel:" + phoneNumber.trim();
		Uri number = Uri.parse(numb);
		Intent intent = new Intent(Intent.ACTION_CALL, number);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		CountDownTimer timer = new CountDownTimer(10000,1000) {
			
			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFinish() {
				finish();
			}
		}.start();
		
		//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		//moveTaskToBack(true);
		//callHomeScreen();
		
	}

	private void callHomeScreen() {
		Intent i = new Intent(CallActivity.this, DisplaySchedules.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
	}

}

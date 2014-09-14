package com.example.tejasscheduler.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.example.tejasscheduler.NotificationView;
import com.example.tejasscheduler.R;
import com.example.tejasscheduler.model.ActiveSchedule;
import com.example.tejasscheduler.utils.MapperUtils;

public class NotificationActivity extends Activity {
	ActiveSchedule inputModel;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		this.inputModel = (ActiveSchedule) getIntent().getSerializableExtra(
				"activeSchedule");
		displayNotification(inputModel);

	}
	
	public void displayNotification(ActiveSchedule activeSchedule) {

		int notificationID = 100;
		NotificationManager mNotificationManager;
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this);

		String contactName = MapperUtils.getContactName(
				getApplicationContext(), activeSchedule.getContactId());
		String mobileNumber = MapperUtils.getPhoneNumber(
				getApplicationContext(), activeSchedule.getContactId());
		String formattedDate = MapperUtils.getCurrentDateTimeFromMilliSeconds(
				activeSchedule.getTime(), "dd/MM/yyyy hh:mm");
		mBuilder.setContentTitle("Activity Scheduled to");
		mBuilder.setContentText(contactName + " at " + formattedDate);
		mBuilder.setTicker("New activity Alert!");
		mBuilder.setSmallIcon(R.drawable.ic_launcher1);

		Intent resultIntent = new Intent(this, NotificationView.class);
		resultIntent.putExtra("notification", activeSchedule);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(NotificationView.class);
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);

		mBuilder.setContentIntent(resultPendingIntent);
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(notificationID, mBuilder.build());
	}

}

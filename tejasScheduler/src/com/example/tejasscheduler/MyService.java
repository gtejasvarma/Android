package com.example.tejasscheduler;

import java.util.List;

import com.example.tejasscheduler.domain.ScheduleManager;
import com.example.tejasscheduler.model.ActiveSchedule;
import com.example.tejasscheduler.ui.activity.SmsActivity;
import com.example.tejasscheduler.utils.EventTypeConstants;
import com.example.tejasscheduler.utils.MapperUtils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service {
	private static final String TAG = "MyService";
	MediaPlayer player;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
	}

	@Override
	public void onDestroy() {
	}
	
	@Override
	public void onStart(Intent intent, int startid) {
		
		ActiveSchedule inputModel = (ActiveSchedule)intent.getSerializableExtra("activeSchedule");
		if(EventTypeConstants.EVENT_CALL==inputModel.getEventType()){
			callPhoneActivity(this,inputModel);
		}else{
			callSmsActivity(this,inputModel);
		}
		
		displayNotification(inputModel,this);
		ScheduleManager.scheduleNext(this,inputModel);
		
	}
	
	public void displayNotification(ActiveSchedule activeSchedule,Context context) {

		activeSchedule = ScheduleManager.getSchedule(context, activeSchedule.getScheduleId());
		NotificationManager mNotificationManager;
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context);

		String contactName = MapperUtils.getContactName(
				context, activeSchedule.getContactId());
		String mobileNumber = MapperUtils.getPhoneNumber(
				context, activeSchedule.getContactId());
		long event = activeSchedule.getEventType();
		String eventName = EventTypeConstants.getEventName(event);
		;
		String formattedDate = MapperUtils.getCurrentDateTimeFromMilliSeconds(
				activeSchedule.getTime(), "dd/MM/yyyy hh:mm");
		System.out.println("Time received is:"+activeSchedule.getTime());
		mBuilder.setContentTitle(eventName + " Scheduled to");
		mBuilder.setContentText(contactName + " at " + formattedDate);
		mBuilder.setTicker("New " + eventName + " Alert!");
		mBuilder.setSmallIcon(R.drawable.icon2);
		//mBuilder.setNumber(++numMessages);

		Intent resultIntent = new Intent(context,
				NotificationView.class);
		resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		resultIntent.putExtra("notification", activeSchedule);
		TaskStackBuilder stackBuilder = TaskStackBuilder
				.create(context);
		stackBuilder.addParentStack(NotificationView.class);
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);

		mBuilder.setAutoCancel(true);
		mBuilder.setContentIntent(resultPendingIntent);
		mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify((int)activeSchedule.getScheduleId(), mBuilder.build());
		
		//mNotificationManager.notify(notificationId, mBuilder.build());
	}


	private void callSmsActivity(Context context,ActiveSchedule schedule)
	{
		Intent callIntent = new Intent(context,SmsActivity.class);
		callIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		callIntent.putExtra("activeSchedule", schedule);
		callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(callIntent);
	}
	
	private void callPhoneActivity(Context context,ActiveSchedule schedule)
	{
		Intent callIntent = new Intent(context,CallActivity.class);
		callIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		callIntent.putExtra("activeSchedule", schedule);
		callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(callIntent);
		
	}
}
package com.example.tejasscheduler;


import java.util.ArrayList;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

import android.widget.Toast;

import com.example.tejasscheduler.domain.ScheduleManager;
import com.example.tejasscheduler.model.ActiveSchedule;
import com.example.tejasscheduler.utils.EventTypeConstants;
import com.example.tejasscheduler.utils.MapperUtils;

public class AlarmReceiver extends BroadcastReceiver
{
	int notificationId = 100;
	@Override
	public void onReceive(Context context, Intent intent)
	{
		ActiveSchedule inputModel = (ActiveSchedule)intent.getSerializableExtra("activeSchedule");
		if(EventTypeConstants.EVENT_CALL==inputModel.getEventType()){
			callPhoneActivity(context,inputModel);
		}
		else  {
			callSmsActivity(context,inputModel);
		}
		
		
		displayNotification(inputModel,context);
		ScheduleManager.scheduleNext(context,inputModel);
		
	
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
		ArrayList<PendingIntent> sentPendingIntents = new ArrayList<PendingIntent>();
	    PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,
	            new Intent(context, SmsSentReceiver.class), 0);
	      try {
	        SmsManager sms = SmsManager.getDefault();
	        ArrayList<String> mSMSMessage = sms.divideMessage(schedule.getText().toString());
	        for (int i = 0; i < mSMSMessage.size(); i++) {
	            sentPendingIntents.add(i, sentPI);
	        }
	        String mobileNumber = MapperUtils.getPhoneNumber(context, schedule.getContactId());
	        sms.sendMultipartTextMessage(mobileNumber, null, mSMSMessage,
	                sentPendingIntents, null);

	    } catch (Exception e) {

	        e.printStackTrace();
	        Toast.makeText(context, "SMS sending failed...",Toast.LENGTH_SHORT).show();
	    }
	    
		
	}
	
	private void callPhoneActivity(final Context context,ActiveSchedule schedule)
	{
		
		String phoneNumber = MapperUtils.getPhoneNumber(
				context, schedule.getContactId());
		String numb = "tel:" + phoneNumber.trim();
		Uri number = Uri.parse(numb);
		final Intent intent = new Intent(Intent.ACTION_DIAL, number);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
		Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(500);
		
		Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		Ringtone ringTone = RingtoneManager.getRingtone(context, notification);
		ringTone.play();
//		TelephonyManager manager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
//		
//		CountDownTimer timer = new CountDownTimer(10000,1000) {
//			
//			@Override
//			public void onTick(long millisUntilFinished) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onFinish() {
//				
//			}
//		}.start();
			
	}
	
	private void callDialActivity(final Context context,ActiveSchedule schedule)
	{
		
		String phoneNumber = MapperUtils.getPhoneNumber(
				context, schedule.getContactId());
		String numb = "tel:" + phoneNumber.trim();
		Uri number = Uri.parse(numb);
		final Intent intent = new Intent(Intent.ACTION_DIAL, number);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
		
		
	}
	
    

	
}

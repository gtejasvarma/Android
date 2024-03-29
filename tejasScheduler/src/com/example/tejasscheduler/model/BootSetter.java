package com.example.tejasscheduler.model;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.tejasscheduler.domain.ScheduleManager;

public class BootSetter extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent1) {
		List<ActiveSchedule> scheduleList = ScheduleManager.getAllActiveScheduleEvents(context).getActiveScheduleList();
		for(ActiveSchedule schedule:scheduleList){
			ScheduleManager.createPendingIntent(context, schedule);
		}
		
	}

}

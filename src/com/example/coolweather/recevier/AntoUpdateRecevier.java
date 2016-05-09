package com.example.coolweather.recevier;

import com.example.coolweather.service.AutoUpdateService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AntoUpdateRecevier extends BroadcastReceiver
{

	@Override
	public void onReceive(Context context, Intent intent)
	{
		Intent i=new Intent(context, AutoUpdateService.class);
		context.startService(i);
	}

}

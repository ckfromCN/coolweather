package com.example.coolweather.service;

import com.example.coolweather.recevier.AntoUpdateRecevier;
import com.example.coolweather.util.HttpCallbackListener;
import com.example.coolweather.util.HttpUtil;
import com.example.coolweather.util.LogUtil;
import com.example.coolweather.util.Utility;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.text.TextUtils;

public class AutoUpdateService extends Service
{

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	/**
	 * 开启新线程,调用updateWeather()并设置定时广播
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				updateWeather();
			}
		}).start();
		AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
		long time = 8 * 60 * 60 * 1000;
		long triggerTime = time + SystemClock.elapsedRealtime();
		Intent i = new Intent(this, AntoUpdateRecevier.class);
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pi);
		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * 查询存储的天气码并更新天气,将更新数据储存在SharePreferences文件中
	 */
	private void updateWeather()
	{
		LogUtil.d("AutoUpdateService", "自动更新了!");
		SharedPreferences spf = PreferenceManager.getDefaultSharedPreferences(this);
		String weatherCode = spf.getString("weather_code", "");
		if (!TextUtils.isEmpty(weatherCode))
		{
			String address = "http://www.weather.com.cn/data/cityinfo/" + weatherCode + ".html";
			HttpUtil.requestHttpConnection(address, new HttpCallbackListener()
			{

				@Override
				public void onFinished(String response)
				{
					Utility.handleWeatherResponse(AutoUpdateService.this, response);
				}

				@Override
				public void onError(Exception e)
				{
					e.printStackTrace();
				}
			});
		}

	}
}

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
	 * �������߳�,����updateWeather()�����ö�ʱ�㲥
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
	 * ��ѯ�洢�������벢��������,���������ݴ�����SharePreferences�ļ���
	 */
	private void updateWeather()
	{
		LogUtil.d("AutoUpdateService", "�Զ�������!");
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

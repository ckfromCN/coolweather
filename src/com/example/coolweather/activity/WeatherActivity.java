package com.example.coolweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.coolweather.R;
import com.example.coolweather.service.AutoUpdateService;
import com.example.coolweather.util.HttpCallbackListener;
import com.example.coolweather.util.HttpUtil;
import com.example.coolweather.util.LogUtil;
import com.example.coolweather.util.Utility;

public class WeatherActivity extends Activity implements OnClickListener
{
	private LinearLayout weatherInfoLayout;
	private TextView cityNameText;
	private TextView publishText;
	private TextView weatherDespText;
	private TextView temp1Text;
	private TextView temp2Text;
	private TextView currentDataText;
	
	private Button switchCity;
	private Button refreshWeather;

	/**
	 * ��ʼ�����ؼ�,���ݴ��ݵ��ؼ����Ų�ѯ����,û�о���ʾ��������
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
		cityNameText = (TextView) findViewById(R.id.city_name);
		publishText = (TextView) findViewById(R.id.publish_text);
		weatherDespText = (TextView) findViewById(R.id.weather_desp);
		temp1Text = (TextView) findViewById(R.id.temp1);
		temp2Text = (TextView) findViewById(R.id.temp2);
		currentDataText = (TextView) findViewById(R.id.current_data);
		switchCity=(Button) findViewById(R.id.switch_city);
		refreshWeather=(Button) findViewById(R.id.refresh_weather);
		switchCity.setOnClickListener(this);
		refreshWeather.setOnClickListener(this);
		
		String countyCode = getIntent().getStringExtra("countyCode");
		if (!TextUtils.isEmpty(countyCode))
		{
			//���ؼ����ž�ȥ��ѯ
			publishText.setText("ͬ����,���Ե�");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			cityNameText.setVisibility(View.INVISIBLE);
			queryWeatherCode(countyCode);
		} else
		{
			showWeather();
		}
	}

	/**
	 * ��ѯ�ؼ����Ŷ�Ӧ����������
	 */
	private void queryWeatherCode(String countyCode)
	{
		String address = "http://www.weather.com.cn/data/list3/city" + countyCode + ".xml";
		queryFromServer(address, "countyCode");
	}

	/**
	 * ��ѯ�������Ŷ�Ӧ������
	 */
	private void queryWeatherInfo(String weatherCode)
	{
		String address = "http://www.weather.com.cn/data/cityinfo/" + weatherCode + ".html";
		queryFromServer(address, "weatherCode");
	}

	/**
	 * ���ݴ���ĵ�ַ������ȥ���������ѯ�������ź�������Ϣ
	 */
	private void queryFromServer(final String address, final String type)
	{
		HttpUtil.requestHttpConnection(address, new HttpCallbackListener()
		{

			@Override
			public void onFinished(String response)
			{
				if (!TextUtils.isEmpty(response))
				{

					if ("countyCode".equals(type))
					{
						String[] code = response.split("\\|");
						if (code != null && code.length == 2)
						{
							queryWeatherInfo(code[1]);
						}
					} else if ("weatherCode".equals(type))
					{
						Utility.handleWeatherResponse(WeatherActivity.this, response);
						runOnUiThread(new Runnable()
						{

							@Override
							public void run()
							{
								showWeather();
							}
						});
					}
				}
			}

			@Override
			public void onError(Exception e)
			{
				runOnUiThread(new Runnable()
				{

					@Override
					public void run()
					{
						publishText.setText("ͬ��ʧ��");
					}
				});
			}
		});
	}

	/**
	 * ��SharedPreferences�ļ��ж�ȡ�洢��������Ϣ,����ʾ�ڽ�����
	 */
	private void showWeather()
	{
		SharedPreferences spf = PreferenceManager.getDefaultSharedPreferences(this);

		cityNameText.setText(spf.getString("city_name", ""));
		publishText.setText("����"+spf.getString("publish_time", "")+"����");
		weatherDespText.setText(spf.getString("weather_desp", ""));
		temp1Text.setText(spf.getString("temp1", ""));
		temp2Text.setText(spf.getString("temp2", ""));
		currentDataText.setText(spf.getString("current_data", ""));

		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);
		Intent intent=new Intent(this, AutoUpdateService.class);
		startService(intent);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.switch_city:
			Intent intent=new Intent(this, ChooseAreaActivity.class);
			intent.putExtra("from_weather_activity", true);
			startActivity(intent);
			finish();
			break;
		case R.id.refresh_weather:
			publishText.setText("ͬ����,���Ե�");
			
			SharedPreferences spf=PreferenceManager.getDefaultSharedPreferences(this);
			String weatherCode=spf.getString("weather_code", "");
			LogUtil.d("WeatherActivity", weatherCode);
			if (!TextUtils.isEmpty(weatherCode))
			{
				queryWeatherInfo(weatherCode);
			}
			break;
		default:
			break;
		}
	}

}

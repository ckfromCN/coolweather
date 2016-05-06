package com.example.coolweather.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.example.coolweather.db.City;
import com.example.coolweather.db.CoolWeatherDB;
import com.example.coolweather.db.County;
import com.example.coolweather.db.Province;

public class Utility
{
	/**
	 * �����ʹ�����������ص�ʡ������
	 */
	public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolweatherDB,
			String response)
	{
		if (!TextUtils.isEmpty(response))
		{
			String[] allProvinces = response.split(",");
			if (allProvinces != null && allProvinces.length > 0)
			{
				for (String p : allProvinces)
				{
					String[] array = p.split("\\|");
					Province province = new Province();
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					// ���������������ݴ洢��Province����
					coolweatherDB.saveProvince(province);
				}
				return true;
			}
		}

		return false;
	}

	public static boolean handleCitiesReponse(CoolWeatherDB coolWeatherDB, String response,
			int provinceId)
	{
		if (!TextUtils.isEmpty(response))
		{
			String[] cities = response.split(",");
			if (cities != null && cities.length > 0)
			{
				for (String c : cities)
				{
					String[] array = c.split("\\|");

					City city = new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					coolWeatherDB.saveCity(city);
				}
				return true;
			}
		}

		return false;
	}

	public static boolean handleCountiesReponse(CoolWeatherDB coolWeatherDB, String response,
			int cityId)
	{
		if (!TextUtils.isEmpty(response))
		{
			String[] counties = response.split(",");
			if (counties != null && counties.length > 0)
			{
				for (String c : counties)
				{
					String[] array = c.split("\\|");

					County county = new County();
					county.setCountyCode(array[0]);
					county.setCountyName(array[1]);
					county.setCityId(cityId);
					coolWeatherDB.saveCounty(county);
				}
				return true;
			}
		}

		return false;
	}

	public static void handleWeatherResponse(Context context, String response)
	{
		try
		{
			JSONObject jsonObject = new JSONObject(response);
			JSONObject jsonobj;
			jsonobj = jsonObject.getJSONObject("weatherinfo");
			String cityName = jsonobj.getString("city");
			String weatherCode = jsonobj.getString("cityid");
			String temp1 = jsonobj.getString("temp1");
			String temp2 = jsonobj.getString("temp2");
			String weatherDesp = jsonobj.getString("weather");
			String publishTime = jsonobj.getString("ptime");
			saveWeatherInfo(context, cityName, weatherCode, temp1, temp2, weatherDesp, publishTime);
		} catch (JSONException e)
		{
			e.printStackTrace();
		}

	}

	public static void saveWeatherInfo(Context context, String cityName, String weatherCode,
			String temp1, String temp2, String weatherDesp, String publishTime)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy��M��d��", Locale.CHINA);
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context)
				.edit();
		editor.putBoolean("city_selected", true);
		editor.putString("city_name", cityName);
		editor.putString("weather_code", weatherCode);
		editor.putString("temp1", temp1);
		editor.putString("temp2", temp2);
		editor.putString("weather_desp", weatherDesp);
		editor.putString("publish_time", publishTime);
		editor.putString("current_data", sdf.format(new Date()));
		editor.commit();
	}

}

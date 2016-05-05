package com.example.coolweather.util;

import android.text.TextUtils;

import com.example.coolweather.db.City;
import com.example.coolweather.db.CoolWeatherDB;
import com.example.coolweather.db.County;
import com.example.coolweather.db.Province;

public class Utility
{
	/**
	 * 解析和处理服务器返回的省级数据
	 */
	public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolweatherDB, String response)
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
					// 将解析出来的数据存储在Province表中
					coolweatherDB.saveProvince(province);
				}
				return true;
			}
		}

		return false;
	}

	public static boolean handleCitiesReponse(CoolWeatherDB coolWeatherDB, String response, int provinceId)
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
	public static boolean handleCountiesReponse(CoolWeatherDB coolWeatherDB, String response, int cityId)
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
}

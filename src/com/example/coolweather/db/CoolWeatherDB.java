package com.example.coolweather.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CoolWeatherDB
{

	public static final String DB_NAME = "cool_weather";

	private static CoolWeatherDB coolWeatherDB;

	private SQLiteDatabase db;

	private CoolWeatherDB(Context context)
	{
		CoolWeatherOpenHelper coolWeatherOpenHelper = new CoolWeatherOpenHelper(context, DB_NAME, null, 1);
		db = coolWeatherOpenHelper.getWritableDatabase();
	}

	// ����ģʽ,����һ��CoolWeatherDBʵ������
	public synchronized static CoolWeatherDB getInstance(Context context)
	{
		if (coolWeatherDB != null)
		{
			coolWeatherDB = new CoolWeatherDB(context);
		}
		return coolWeatherDB;
	}

	// ��Provinceʵ���洢�����ݿ���
	public void saveProvince(Province province)
	{
		ContentValues values = new ContentValues();
		values.put("province_name", province.getProvinceName());
		values.put("province_code", province.getProvinceCode());
		db.insert("Province", null, values);
	}

	// ��Provinceʵ���洢�����ݿ���
	public List<Province> loadProvinces()
	{
		List<Province> provinceList = new ArrayList<Province>();
		Cursor cursor = db.query("Province", null, null, null, null, null, null);
		if (cursor.moveToFirst())
		{
			do
			{
				Province province = new Province();
				province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
				provinceList.add(province);
			} while (cursor.moveToNext());
		}

		return provinceList;
	}

	// ��Cityʵ�����浽���ݿ�
	public void saveCity(City city)
	{
		ContentValues values = new ContentValues();
		values.put("city_name", city.getCityName());
		values.put("city_code", city.getCityCode());
		values.put("province_id", city.getProvinceId());
		db.insert("City", null, values);
	}

	// �����ݿ��ж�ȡĳʡ�����г�����Ϣ
	public List<City> loadCity(int provinceId)
	{
		List<City> cityList = new ArrayList<City>();
		Cursor cursor = db.query("City", null, "provinceId=?", new String[]
		{ provinceId + "" }, null, null, null);
		if (cursor.moveToFirst())
		{
			do
			{
				City city = new City();
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setProvinceId(provinceId);
				cityList.add(city);
			} while (cursor.moveToNext());
		}

		return cityList;

	}
	// ��Countyʵ�����浽���ݿ�
	public void saveCounty(County county)
	{
		ContentValues values = new ContentValues();
		values.put("county_name", county.getCountyName());
		values.put("county_code", county.getCountyCode());
		values.put("city_id", county.getCityId());
		db.insert("County", null, values);
	}
	
	// �����ݿ��ж�ȡĳʡ�����г�����Ϣ
	public List<County> loadCounty(int cityId)
	{
		List<County> countyList = new ArrayList<County>();
		Cursor cursor = db.query("County", null, "cityId=?", new String[]
				{ cityId + "" }, null, null, null);
		if (cursor.moveToFirst())
		{
			do
			{
				County county = new County();
				county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
				county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
				county.setCityId(cityId);
				countyList.add(county);
			} while (cursor.moveToNext());
		}
		
		return countyList;
		
	}
	
}
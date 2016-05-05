package com.example.coolweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class CoolWeatherOpenHelper extends SQLiteOpenHelper
{

	/**
	 * 创建Province数据库
	 */
	public static final String PROVINCE = "create table Province ("
			+ "id integer primary key autoincrement," + "province_name text,"
			+ "province_code text)";
	/**
	 * 创建City数据库
	 */
	public static final String CITY = "create table City("
			+ "id integer primary key autoincrement," + "city_name text," + "city_code text,"
			+ "province_id integer)";
	/**
	 * 创建County数据库
	 */
	public static final String COUNTY = "create table County ("
			+ "id integer primary key autoincrement," + "county_name text," + "county_code text,"
			+ "city_id integer)";

	public CoolWeatherOpenHelper(Context context, String name, CursorFactory factory, int version)
	{
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(PROVINCE);
		db.execSQL(CITY);
		db.execSQL(COUNTY);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// TODO Auto-generated method stub

	}

}

package com.example.coolweather.test;

import com.example.coolweather.db.CoolWeatherDB;
import com.example.coolweather.util.MyApplication;

import android.test.AndroidTestCase;

public class CoolWeatherDBTest extends AndroidTestCase
{

	@Override
	protected void setUp() throws Exception
	{
		// TODO Auto-generated method stub
		super.setUp();
	}

	public void testDB()
	{
		CoolWeatherDB c1=CoolWeatherDB.getInstance(MyApplication.getContext());
		CoolWeatherDB c2=CoolWeatherDB.getInstance(MyApplication.getContext());
//		assertEquals(true, c1.equals(c2));
		assertEquals(false, MyApplication.getContext()==null);
	}

	@Override
	protected void tearDown() throws Exception
	{
		// TODO Auto-generated method stub
		super.tearDown();
	}

}

package com.example.coolweather.db;

public class County
{
	private String countyName;
	private String countyCode;
	private int cityId;

	public String getCountyName()
	{
		return countyName;
	}

	public void setCountyName(String countyName)
	{
		this.countyName = countyName;
	}

	public String getCountyCode()
	{
		return countyCode;
	}

	public void setCountyCode(String countyCode)
	{
		this.countyCode = countyCode;
	}

	public int getCityId()
	{
		return cityId;
	}

	public void setCityId(int cityId)
	{
		this.cityId = cityId;
	}
}

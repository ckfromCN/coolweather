package com.example.coolweather.db;

public class Province
{

	private String provinceName;
	private String ProvinceCode;
	private int id;

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getProvinceName()
	{
		return provinceName;
	}

	public void setProvinceName(String provinceName)
	{
		this.provinceName = provinceName;
	}

	public String getProvinceCode()
	{
		return ProvinceCode;
	}

	public void setProvinceCode(String provinceCode)
	{
		ProvinceCode = provinceCode;
	}

}

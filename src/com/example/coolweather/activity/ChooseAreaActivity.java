package com.example.coolweather.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coolweather.R;
import com.example.coolweather.db.City;
import com.example.coolweather.db.CoolWeatherDB;
import com.example.coolweather.db.County;
import com.example.coolweather.db.Province;
import com.example.coolweather.util.HttpCallbackListener;
import com.example.coolweather.util.HttpUtil;
import com.example.coolweather.util.Utility;

public class ChooseAreaActivity extends Activity
{
	public static final int LEVEL_PROVINCE = 0;
	public static final int LEVEL_CITY = 1;
	public static final int LEVEL_COUNTY = 2;
	private int currentLevel;

	private ProgressDialog progressDialog;
	private TextView titleText;
	private ListView listView;
	private ArrayAdapter<String> adapter;
	private CoolWeatherDB coolWeatherDB;

	private List<String> dataList=new ArrayList<String>();
	private List<Province> provinceList;
	private List<City> cityList;
	private List<County> countyList;
	/**
	 * ��ǰѡ���ʡ��
	 */
	private Province selectedProvince;
	/**
	 * ��ǰѡ��ĳ���
	 */
	private City selectedCity;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		coolWeatherDB = CoolWeatherDB.getInstance(this);
		titleText = (TextView) findViewById(R.id.title_text);
		listView = (ListView) findViewById(R.id.list_view);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				if (currentLevel == LEVEL_PROVINCE)
				{
					selectedProvince = provinceList.get(position);
					queryCities();
				} else if (currentLevel == LEVEL_CITY)
				{
					selectedCity = cityList.get(position);
					queryCounty();
				}
			}
		});
		queryProvinces();
	}

	/*
	 * ��ѯȫ������ʡ���޸�UI
	 */
	private void queryProvinces()
	{
		provinceList = coolWeatherDB.loadProvinces();
		if (provinceList.size() > 0)
		{
			titleText.setText("�й�");
			dataList.clear();
			for (Province p : provinceList)
			{
				dataList.add(p.getProvinceName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			currentLevel = LEVEL_PROVINCE;
		} else
		{
			queryFromServer(null, "province");
		}

	}

	/**
	 * ��ѯѡ��ʡ�����г��в��޸�UI
	 */
	private void queryCities()
	{
		cityList = coolWeatherDB.loadCitys(selectedProvince.getId());
		if (cityList.size() > 0)
		{
			dataList.clear();
			for (City c : cityList)
			{
				dataList.add(c.getCityName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedProvince.getProvinceName());
			currentLevel = LEVEL_CITY;
		} else
		{
			queryFromServer(selectedProvince.getProvinceCode(), "city");
		}
	}

	/**
	 * ��ѯѡ�г��������ز��޸�UI
	 */
	private void queryCounty()
	{
		countyList = coolWeatherDB.loadCounty(selectedCity.getId());
		if (countyList.size() > 0)
		{
			dataList.clear();

			for (County c : countyList)
			{
				dataList.add(c.getCountyName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedCity.getCityName());
			currentLevel = LEVEL_COUNTY;
		} else
		{
			queryFromServer(selectedCity.getCityCode(), "county");
		}
	}

	/**
	 * ���ݴ��ź����ʹӷ������ϲ�ѯʡ���ص�����
	 */
	private void queryFromServer(final String code, final String type)
	{
		String address;
		if (!TextUtils.isEmpty(code))
		{
			address = "http://www.weather.com.cn/data/list3/city" + code + ".xml";
		} else
		{
			address = "http://www.weather.com.cn/data/list3/city.xml";
		}
		showProgressDialog();
		// ����HttpUtil���е�rerequestHttpConnection�����߳����������粢��������
		HttpUtil.requestHttpConnection(address, new HttpCallbackListener()
		{

			@Override
			public void onFinished(String response)
			{
				boolean result = false;

				if ("province".equals(type))
				{
					result = Utility.handleProvincesResponse(coolWeatherDB, response);
				} else if ("city".equals(type))
				{
					result = Utility.handleCitiesReponse(coolWeatherDB, response,
							selectedProvince.getId());
				} else if ("county".equals(type))
				{
					result = Utility.handleCountiesReponse(coolWeatherDB, response,
							selectedCity.getId());
				}
				if (result)
				{
					runOnUiThread(new Runnable()
					{

						@Override
						public void run()
						{
							if ("province".equals(type))
							{
								queryProvinces();
							} else if ("city".equals(type))
							{
								queryCities();
							} else if ("county".equals(type))
							{
								queryCounty();
							}
							closeProgressDialog();//�رնԻ���
						}
					});
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
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this, "����ʧ��", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}

	/**
	 * ��ʾ���ȶԻ���
	 */
	private void showProgressDialog()
	{
		if (progressDialog == null)
		{
			progressDialog = new ProgressDialog(this);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setMessage("������,���Ժ�");
		}
		progressDialog.show();
	}

	/**
	 * �رս��ȶԻ���
	 */
	private void closeProgressDialog()
	{
		if (progressDialog != null)
		{
			progressDialog.dismiss();
		}
	}

	@Override
	public void onBackPressed()
	{
		if (currentLevel == LEVEL_COUNTY)
		{
			queryCities();
		} else if (currentLevel == LEVEL_CITY)
		{
			queryProvinces();
		} else
		{
			finish();
		}
	}

}

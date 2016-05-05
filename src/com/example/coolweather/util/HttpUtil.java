package com.example.coolweather.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil
{
	public static void requestHttpConnection(final String address, final HttpCallbackListener listener)
	{
		new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				HttpURLConnection httpURLConnection = null;
				try
				{
					URL url = new URL(address);
					httpURLConnection = (HttpURLConnection) url.openConnection();
					httpURLConnection.setRequestMethod("GET");
					httpURLConnection.setConnectTimeout(8000);
					httpURLConnection.setReadTimeout(8000);
					InputStream in = httpURLConnection.getInputStream();
					BufferedReader bfr = new BufferedReader(new InputStreamReader(in));
					String line = null;
					StringBuilder sb = new StringBuilder();
					while ((line = bfr.readLine()) != null)
					{
						sb.append(line);
					}
					if (listener != null)
					{
						listener.onFinished(sb.toString());
					}
				} catch (Exception e)
				{
					listener.onError(e);
				} finally
				{
					if (httpURLConnection!=null)
					{
						httpURLConnection.disconnect();
					}
					
				}

			}
		}).start();
	}

}

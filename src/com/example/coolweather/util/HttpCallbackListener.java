package com.example.coolweather.util;


public interface HttpCallbackListener 
{
	void onFinished(String response);
	void onError(Exception e);
}

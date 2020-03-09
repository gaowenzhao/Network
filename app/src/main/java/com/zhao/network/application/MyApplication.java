package com.zhao.network.application;

import android.app.Application;

import com.zhao.networklib.base.NetworkApi;

public class MyApplication extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        NetworkApi.init(new NetworkInfo(this));
    }
}

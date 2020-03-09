package com.zhao.network.application;

import android.app.Application;

import com.zhao.network.BuildConfig;
import com.zhao.networklib.base.INetworkRequiredInfo;

public class NetworkInfo implements INetworkRequiredInfo {
    private Application mApplication;
    public NetworkInfo(Application application){
        this.mApplication = application;
    }
    @Override
    public String getAppVersionName() {
        return BuildConfig.VERSION_NAME;
    }

    @Override
    public String getAppVersionCode() {
        return String.valueOf(BuildConfig.VERSION_CODE);
    }

    @Override
    public boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    @Override
    public Application getApplicationContext() {
        return mApplication;
    }
}

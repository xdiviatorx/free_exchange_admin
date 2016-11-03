package com.technologies.mobile.free_exchange_admin;

import android.app.Application;

import com.vk.sdk.VKSdk;

/**
 * Created by diviator on 23.10.2016.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        VKSdk.initialize(this);
    }
}

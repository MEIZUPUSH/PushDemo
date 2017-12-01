package com.meizu.pushdemo;

import android.app.Application;

import com.meizu.cloud.pushinternal.DebugLogger;


public class PushDemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DebugLogger.initDebugLogger(this);
    }
}

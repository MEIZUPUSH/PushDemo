package com.meizu.pushdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by liaojinlong on 15-5-29.
 */
public class TestActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String extra = getIntent().getStringExtra("start_fragment");
        Log.i("TestActivity", "MzPushMessageReceiver " + extra);
    }
}

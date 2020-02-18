package com.meizu.pushdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.meizu.cloud.pushinternal.DebugLogger;
import com.meizu.cloud.pushsdk.constants.PushConstants;

/**
 * Created by liaojinlong on 15-5-29.
 */
public class TestActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        String extra = getIntent().getStringExtra("start_fragment");
        Intent intent = getIntent();
        String platformExtra = intent.getStringExtra("platform_extra");
        Log.i("TestActivity", "TestActivity intent: " + extra+" "+platformExtra);
    }
}

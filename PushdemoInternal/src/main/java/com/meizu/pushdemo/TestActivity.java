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
        String extra = getIntent().getStringExtra("start_fragment");
        Intent intent = getIntent();
        String platformExtra = intent.getStringExtra(PushConstants.MZ_PUSH_PLATFROM_EXTRA);
        DebugLogger.i("TestActivity", "MzPushMessageReceiver " + extra+" "+platformExtra);
    }
}

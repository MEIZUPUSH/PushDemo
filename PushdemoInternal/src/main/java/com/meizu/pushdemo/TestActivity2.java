package com.meizu.pushdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * @author liyizhi
 * @date 2020/3/5.
 * @describe
 */
public class TestActivity2 extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        String extra = getIntent().getStringExtra("start_fragment");
        Intent intent = getIntent();
        String platformExtra = intent.getStringExtra("platform_extra");
        Log.i("TestActivity2", "TestActivity2 intent: " + extra+" "+platformExtra);
    }
}

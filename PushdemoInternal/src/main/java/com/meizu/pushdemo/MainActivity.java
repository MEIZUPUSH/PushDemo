package com.meizu.pushdemo;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.meizu.cloud.pushinternal.DebugLogger;
import com.meizu.cloud.pushsdk.PushManager;
import com.meizu.cloud.pushsdk.platform.message.PushSwitchStatus;
import com.meizu.cloud.pushsdk.platform.message.RegisterStatus;
import com.meizu.cloud.pushsdk.platform.message.SubAliasStatus;
import com.meizu.cloud.pushsdk.platform.message.SubTagsStatus;
import com.meizu.cloud.pushsdk.platform.message.UnRegisterStatus;
import com.meizu.cloud.pushsdk.util.MzSystemUtils;
import com.meizu.pushdemo.events.SendNotificationMessage;
import com.meizu.pushdemo.events.ThroughMessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends Activity implements View.OnClickListener{
    private TextView tvBasicInfo;
    private TextView tvLogArea;

    private Button btnPlatformRegister;
    private Button btnPlatformUnRegister;
    private Button btnSwitchNotificationOn;
    private Button btnSwitchNotificationOff;
    private Button btnSwitchThroughOn;
    private Button btnSwitchThroughOff;
    private Button btnCheckSwitchStatus;

    private Button btnSubScribeTags;
    private Button btnUnSubScribeTags;
    private Button btnCheckSubScribeTags;

    private Button btnSubScribeAlias;
    private Button btnUnSubScribeAlias;
    private Button btnCheckSubScribeAlias;

    /*public static  String APP_ID = "your PushDemo appId";
    public static  String APP_KEY = "your PushDemo appKey";*/

    public String APP_ID /*= "100999"*/;
    public String APP_KEY /*= "80355073480594a99470dcacccd8cf2c"*/;


    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String extra = getIntent().getStringExtra("start_fragment");
        Log.i("MainActivity", "MzPushMessageReceiver " + extra);
        APP_ID = getAppId("APP_ID");
        APP_KEY = getAppKey("APP_KEY");
        setContentView(R.layout.activity_main);
        tvBasicInfo = (TextView) findViewById(R.id.tv_basic_info);
        tvBasicInfo.setText("APP_ID = " + APP_ID + "\n" + "APP_KEY= " + APP_KEY + " \n");
        tvLogArea = (TextView) findViewById(R.id.tv_log_area);

        btnPlatformRegister = (Button) findViewById(R.id.platform_register);
        btnPlatformUnRegister = (Button) findViewById(R.id.platform_unregister);
        btnSwitchNotificationOn = (Button) findViewById(R.id.btn_switch_notification_on);
        btnSwitchNotificationOff = (Button) findViewById(R.id.btn_switch_notification_off);
        btnSwitchThroughOn = (Button) findViewById(R.id.btn_switch_through_on);
        btnSwitchThroughOff = (Button) findViewById(R.id.btn_switch_through_off);

        btnCheckSwitchStatus = (Button) findViewById(R.id.btn_check_push_switch);
        btnCheckSwitchStatus.setOnClickListener(this);

        btnSubScribeTags = (Button) findViewById(R.id.btn_subscribe_tags);
        btnUnSubScribeTags = (Button) findViewById(R.id.btn_unsubscribe_tags);
        btnCheckSubScribeTags = (Button) findViewById(R.id.btn_check_subscribe_tags);
        btnSubScribeTags.setOnClickListener(this);
        btnUnSubScribeTags.setOnClickListener(this);
        btnCheckSubScribeTags.setOnClickListener(this);

        btnSubScribeAlias = (Button) findViewById(R.id.btn_subscribe_alias);
        btnUnSubScribeAlias = (Button) findViewById(R.id.btn_unsubscribe_alias);
        btnCheckSubScribeAlias = (Button) findViewById(R.id.btn_check_subscribe_alias);
        btnSubScribeAlias.setOnClickListener(this);
        btnUnSubScribeAlias.setOnClickListener(this);
        btnCheckSubScribeAlias.setOnClickListener(this);

        btnPlatformRegister.setOnClickListener(this);
        btnPlatformUnRegister.setOnClickListener(this);
        btnSwitchNotificationOn.setOnClickListener(this);
        btnSwitchNotificationOff.setOnClickListener(this);
        btnSwitchThroughOn.setOnClickListener(this);
        btnSwitchThroughOff.setOnClickListener(this);

        Intent intent = new Intent(this,ForegroundService.class);
        intent.setAction(ForegroundService.START_FOREGROUD_SERVICE);
        startService(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RegisterStatus event){
        //Toast.makeText(this, event.toString(), Toast.LENGTH_SHORT).show();
        tvLogArea.setText(event.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UnRegisterStatus event){
        tvLogArea.setText(event.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PushSwitchStatus event){
        tvLogArea.setText(event.toString());
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SubTagsStatus event){
        tvLogArea.setText(event.toString());
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SubAliasStatus event){
        tvLogArea.setText(event.toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SendNotificationMessage event){
        tvLogArea.setText(event.message);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ThroughMessageEvent event){
        tvLogArea.setText(event.message);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.platform_register:
                PushManager.register(this, APP_ID, APP_KEY);
                //PushManager.checkNotificationMessage(this);
                break;

            case R.id.platform_unregister:
                PushManager.unRegister(this, APP_ID, APP_KEY);
                break;
            case R.id.btn_switch_notification_on:
                PushManager.switchPush(this, APP_ID, APP_KEY, PushManager.getPushId(this), 0, true);
                break;
            case R.id.btn_switch_notification_off:
                PushManager.switchPush(this, APP_ID, APP_KEY, PushManager.getPushId(this), 0, false);
                break;
            case R.id.btn_switch_through_on:
                PushManager.switchPush(this, APP_ID, APP_KEY, PushManager.getPushId(this), 1, true);
                break;
            case R.id.btn_switch_through_off:
                PushManager.switchPush(this, APP_ID, APP_KEY, PushManager.getPushId(this), 1, false);
                break;
            case R.id.btn_subscribe_tags:
                PushManager.subScribeTags(this, APP_ID, APP_KEY, PushManager.getPushId(this), "tech,news");
                break;
            case R.id.btn_check_subscribe_tags:
                PushManager.checkSubScribeTags(this, APP_ID, APP_KEY, PushManager.getPushId(this));
                break;
            case R.id.btn_unsubscribe_tags:
                PushManager.unSubScribeTags(this, APP_ID, APP_KEY, PushManager.getPushId(this), "tech");
                break;
            case R.id.btn_subscribe_alias:
                PushManager.subScribeAlias(this, APP_ID, APP_KEY, PushManager.getPushId(this), "Android");
                break;
            case R.id.btn_unsubscribe_alias:
                PushManager.unSubScribeAlias(this, APP_ID, APP_KEY, PushManager.getPushId(this), "Android");
                break;
            case R.id.btn_check_subscribe_alias:
                PushManager.checkSubScribeAlias(this, APP_ID, APP_KEY, PushManager.getPushId(this));
                break;
            case R.id.btn_check_push_switch:
                PushManager.checkPush(this, APP_ID, APP_KEY, PushManager.getPushId(this));
                //UxIPUtils.onLogEvent(this, "com.meizu.pushdemo", "867247020006101","123","154515","notification_service_message",String.valueOf(System.currentTimeMillis()/1000));
                break;
        }
    }



    private String getAppKey(String tag) {
        String appKey = null;
        try {
            ApplicationInfo appInfo = this.getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            appKey = appInfo.metaData.getString(tag);
            DebugLogger.e(TAG, tag + "=" + appKey);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appKey;
    }


    private String getAppId(String tag){
        int appId = 0;
        try {
            ApplicationInfo appInfo = this.getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            appId = appInfo.metaData.getInt(tag);
            DebugLogger.e(TAG, tag + "=" + appId);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return String.valueOf(appId);
    }







}

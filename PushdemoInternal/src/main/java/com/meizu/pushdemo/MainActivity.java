package com.meizu.pushdemo;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.meizu.cloud.pushinternal.DebugLogger;
import com.meizu.cloud.pushsdk.PushManager;
import com.meizu.cloud.pushsdk.constants.PushConstants;

public class MainActivity extends Activity implements View.OnClickListener{
    private static final String TAG = "MainActivity";
    public final static String MESSAGE_PARAM = "message_param";
    public final static String MESSAGE_ACTION = "message_action";

    private String APP_ID;
    private String APP_KEY;

    private TextView tvBasicInfo;
    private TextView tvLogArea;

    private Button btnPlatformRegister;
    private Button btnPlatformUnRegister;
    private Button btnSwitchNotificationOn;
    private Button btnSwitchNotificationOff;
    private Button btnSwitchThroughOn;
    private Button btnSwitchThroughOff;
    private Button btnCheckSwitchStatus;
    private Button btnSwitchAll;

    private Button btnSubScribeTags;
    private Button btnUnSubScribeTags;
    private Button btnCheckSubScribeTags;
    private Button btnUnSubScribeAllTags;

    private Button btnSubScribeAlias;
    private Button btnUnSubScribeAlias;
    private Button btnCheckSubScribeAlias;

    private Button btnEnableRemoteInvoke;
    private Button btnDisableRemoteInvoke;

    private Button btnCancelByNotifyId;
    private EditText edtNotifyId;
    private Button btnCancelAll;

    private IntentFilter mIntentFilter;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_ACTION.equalsIgnoreCase(intent.getAction())) {
                String message = intent.getStringExtra(MESSAGE_PARAM);
                tvLogArea.setText(message);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent platformIntent = getIntent();
        DebugLogger.e(TAG,"platform_extra "+platformIntent.getStringExtra(PushConstants.MZ_PUSH_PLATFORM_EXTRA));
        String extra = getIntent().getStringExtra("start_fragment");
        Log.i("MainActivity", "MzPushMessageReceiver " + extra);

        APP_ID = getAppId("APP_ID");
        APP_KEY = getAppKey("APP_KEY");

        tvBasicInfo = findViewById(R.id.tv_basic_info);
        tvBasicInfo.setText("APP_VER = " + BuildConfig.VERSION_NAME  + "\nAPP_ID = " + APP_ID + "\nAPP_KEY= " + APP_KEY + " \n");
        tvLogArea = findViewById(R.id.tv_log_area);

        btnPlatformRegister = findViewById(R.id.platform_register);
        btnPlatformUnRegister = findViewById(R.id.platform_unregister);
        btnSwitchNotificationOn = findViewById(R.id.btn_switch_notification_on);
        btnSwitchNotificationOff = findViewById(R.id.btn_switch_notification_off);
        btnSwitchThroughOn = findViewById(R.id.btn_switch_through_on);
        btnSwitchThroughOff = findViewById(R.id.btn_switch_through_off);

        btnCheckSwitchStatus = findViewById(R.id.btn_check_push_switch);
        btnCheckSwitchStatus.setOnClickListener(this);

        btnSwitchAll = findViewById(R.id.btn_switch_all);
        btnSwitchAll.setOnClickListener(this);

        btnSubScribeTags = findViewById(R.id.btn_subscribe_tags);
        btnUnSubScribeTags = findViewById(R.id.btn_unsubscribe_tags);
        btnCheckSubScribeTags = findViewById(R.id.btn_check_subscribe_tags);
        btnUnSubScribeAllTags = findViewById(R.id.btn_unsubscribe_all_tags);
        btnSubScribeTags.setOnClickListener(this);
        btnUnSubScribeTags.setOnClickListener(this);
        btnCheckSubScribeTags.setOnClickListener(this);
        btnUnSubScribeAllTags.setOnClickListener(this);

        btnSubScribeAlias = findViewById(R.id.btn_subscribe_alias);
        btnUnSubScribeAlias = findViewById(R.id.btn_unsubscribe_alias);
        btnCheckSubScribeAlias = findViewById(R.id.btn_check_subscribe_alias);
        btnSubScribeAlias.setOnClickListener(this);
        btnUnSubScribeAlias.setOnClickListener(this);
        btnCheckSubScribeAlias.setOnClickListener(this);

        btnPlatformRegister.setOnClickListener(this);
        btnPlatformUnRegister.setOnClickListener(this);
        btnSwitchNotificationOn.setOnClickListener(this);
        btnSwitchNotificationOff.setOnClickListener(this);
        btnSwitchThroughOn.setOnClickListener(this);
        btnSwitchThroughOff.setOnClickListener(this);

        btnEnableRemoteInvoke = findViewById(R.id.btn_enable_remote_invoke);
        btnEnableRemoteInvoke.setOnClickListener(this);
        btnDisableRemoteInvoke = findViewById(R.id.btn_disenable_remote_invoke);
        btnDisableRemoteInvoke.setOnClickListener(this);

        btnCancelAll = findViewById(R.id.btn_cancel_all);
        btnCancelByNotifyId = findViewById(R.id.btn_cancel_by_notify_id);
        edtNotifyId = findViewById(R.id.edit_notify_id);
        btnCancelAll.setOnClickListener(this);
        btnCancelByNotifyId.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mIntentFilter == null) {
            mIntentFilter = new IntentFilter();
            mIntentFilter.addAction(MESSAGE_ACTION);
        }
        registerReceiver(mBroadcastReceiver, mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.platform_register:
                PushManager.register(this, APP_ID, APP_KEY);
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
                break;
            case R.id.btn_switch_all:
                PushManager.switchPush(this,APP_ID,APP_KEY,PushManager.getPushId(this),false);
                break;
            case R.id.btn_unsubscribe_all_tags:
                PushManager.unSubScribeAllTags(this,APP_ID,APP_KEY,PushManager.getPushId(this));
                break;
            case R.id.btn_enable_remote_invoke:
                PushManager.enableCacheRequest(this,true);
                break;
            case R.id.btn_disenable_remote_invoke:
                PushManager.enableCacheRequest(this,false);
                break;
            case R.id.btn_cancel_all:
                PushManager.clearNotification(this);
                break;
            case R.id.btn_cancel_by_notify_id:
                String notifyEdtStr = edtNotifyId.getText().toString();
                if (TextUtils.isEmpty(notifyEdtStr) || notifyEdtStr.split(",") == null) {
                    Toast.makeText(this,"请填写notifyId,多个以逗号隔开",Toast.LENGTH_SHORT).show();
                    break;
                }
                String[] notifyArray = notifyEdtStr.split(",");
                int[] intArray = new int[notifyArray.length];
                for(int i=0; i<notifyArray.length; i++){
                    try {
                        int notifyId = Integer.parseInt(notifyArray[i]);
                        DebugLogger.e(TAG,"clear notifyId "+notifyId);
                        intArray[i] = notifyId;
                    } catch (Exception e){
                        DebugLogger.e(TAG,"请正确输入notifyId,仅限整数");
                        Toast.makeText(this,"请正确输入notifyId,仅限整数",Toast.LENGTH_SHORT).show();
                    }
                }
                PushManager.clearNotification(this,intArray);
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

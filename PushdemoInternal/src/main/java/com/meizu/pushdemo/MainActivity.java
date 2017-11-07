package com.meizu.pushdemo;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
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
import com.meizu.cloud.pushsdk.base.reflect.ReflectClass;
import com.meizu.cloud.pushsdk.base.reflect.ReflectResult;
import com.meizu.cloud.pushsdk.constants.PushConstants;
import com.meizu.cloud.pushsdk.platform.message.PushSwitchStatus;
import com.meizu.cloud.pushsdk.platform.message.RegisterStatus;
import com.meizu.cloud.pushsdk.platform.message.SubAliasStatus;
import com.meizu.cloud.pushsdk.platform.message.SubTagsStatus;
import com.meizu.cloud.pushsdk.platform.message.UnRegisterStatus;
import com.meizu.cloud.pushsdk.pushtracer.QuickTracker;
import com.meizu.cloud.pushsdk.pushtracer.event.PushEvent;
import com.meizu.cloud.pushsdk.util.PushPreferencesUtils;
import com.meizu.pushdemo.events.SendNotificationMessage;
import com.meizu.pushdemo.events.ThroughMessageEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

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

    /*public static  String APP_ID = "your PushDemo appId";
    public static  String APP_KEY = "your PushDemo appKey";*/

    public String APP_ID /*= "100999"*/;
    public String APP_KEY /*= "80355073480594a99470dcacccd8cf2c"*/;


    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent platformIntent = getIntent();
        DebugLogger.e(TAG,"platform_extra "+platformIntent.getStringExtra(PushConstants.MZ_PUSH_PLATFROM_EXTRA));
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

        btnSwitchAll = (Button) findViewById(R.id.btn_switch_all);
        btnSwitchAll.setOnClickListener(this);

        btnSubScribeTags = (Button) findViewById(R.id.btn_subscribe_tags);
        btnUnSubScribeTags = (Button) findViewById(R.id.btn_unsubscribe_tags);
        btnCheckSubScribeTags = (Button) findViewById(R.id.btn_check_subscribe_tags);
        btnUnSubScribeAllTags = (Button) findViewById(R.id.btn_unsubscribe_all_tags);
        btnSubScribeTags.setOnClickListener(this);
        btnUnSubScribeTags.setOnClickListener(this);
        btnCheckSubScribeTags.setOnClickListener(this);
        btnUnSubScribeAllTags.setOnClickListener(this);

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

        btnEnableRemoteInvoke = (Button) findViewById(R.id.btn_enable_remote_invoke);
        btnEnableRemoteInvoke.setOnClickListener(this);
        btnDisableRemoteInvoke = (Button) findViewById(R.id.btn_disenable_remote_invoke);
        btnDisableRemoteInvoke.setOnClickListener(this);

        btnCancelAll = (Button) findViewById(R.id.btn_cancel_all);
        btnCancelByNotifyId = (Button) findViewById(R.id.btn_cancel_by_notify_id);
        edtNotifyId = (EditText) findViewById(R.id.edit_notify_id);
        btnCancelAll.setOnClickListener(this);
        btnCancelByNotifyId.setOnClickListener(this);

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
                String notifyStr = edtNotifyId.getText().toString();
                if(TextUtils.isEmpty(notifyStr)){
                    Toast.makeText(this,"请填写notifyId",Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    int notifyId = Integer.parseInt(notifyStr);
                    PushManager.clearNotification(this,notifyId);
                }
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


    public String fileMD5(String inputFile) {
        // 缓冲区大小（这个可以抽出一个参数）
        int bufferSize = 256 * 1024;
        FileInputStream fileInputStream = null;
        DigestInputStream digestInputStream = null;
        try {
            // 拿到一个MD5转换器（同样，这里可以换成SHA1）
            MessageDigest messageDigest =MessageDigest.getInstance("MD5");
            // 使用DigestInputStream
            fileInputStream = new FileInputStream(inputFile);
            digestInputStream = new DigestInputStream(fileInputStream,messageDigest);
            // read的过程中进行MD5处理，直到读完文件
            byte[] buffer =new byte[bufferSize];
            while (digestInputStream.read(buffer) > 0);
            // 获取最终的MessageDigest
            messageDigest= digestInputStream.getMessageDigest();
            // 拿到结果，也是字节数组，包含16个元素
            byte[] resultByteArray = messageDigest.digest();
            // 同样，把字节数组转换成字符串
            return byteArrayToHex(resultByteArray);
        } catch (NoSuchAlgorithmException e) {
            return null;
        } catch (FileNotFoundException e){
            return null;
        } catch (IOException e){
            return null;
        }finally {
            try {
                digestInputStream.close();
            } catch (Exception e) {
            }
            try {
                fileInputStream.close();
            } catch (Exception e) {
            }
        }
    }

    public static String byteArrayToHex(byte[] byteArray) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < byteArray.length; n++) {
            stmp = (Integer.toHexString(byteArray[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
            if (n < byteArray.length - 1) {
                hs = hs + "";
            }
        }
        return hs;

    }

    private void parseJson(){
        String jsonStr = "{\"type\":\"ONLINE_SHIPS\",\"message\":\"{\\\"currentTime\\\":1400077615368,\\\"direction\\\":0,\\\"id\\\":1,\\\"latitude\\\":29.5506,\\\"longitude\\\":106.6466}\"}";
        DebugLogger.i(TAG,"json is "+ jsonStr);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonStr);
            //先通过字符串的方式得到,转义字符自然会被转化掉
            String jsonstrtemp = jsonObject.getString("message");
            DebugLogger.i(TAG,"message:"+jsonstrtemp);
            jsonObject = new JSONObject(jsonstrtemp);
            DebugLogger.i(TAG,"currentTime:"+jsonObject.get("currentTime"));
            DebugLogger.i(TAG,"direction:"+jsonObject.get("direction"));
            DebugLogger.i(TAG,"latitude:"+jsonObject.get("latitude"));
            DebugLogger.i(TAG,"longitude:"+jsonObject.get("longitude"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}

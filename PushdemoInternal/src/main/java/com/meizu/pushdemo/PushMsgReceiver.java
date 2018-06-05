package com.meizu.pushdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.meizu.cloud.pushinternal.DebugLogger;
import com.meizu.cloud.pushsdk.MzPushMessageReceiver;
import com.meizu.cloud.pushsdk.PushManager;
import com.meizu.cloud.pushsdk.handler.MzPushMessage;
import com.meizu.cloud.pushsdk.notification.PushNotificationBuilder;
import com.meizu.cloud.pushsdk.platform.PlatformMessageSender;
import com.meizu.cloud.pushsdk.platform.message.PushSwitchStatus;
import com.meizu.cloud.pushsdk.platform.message.RegisterStatus;
import com.meizu.cloud.pushsdk.platform.message.SubAliasStatus;
import com.meizu.cloud.pushsdk.platform.message.SubTagsStatus;
import com.meizu.cloud.pushsdk.platform.message.UnRegisterStatus;
import com.meizu.pushdemo.events.ThroughMessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by liaojinlong on 15-6-28.
 */
public class PushMsgReceiver extends MzPushMessageReceiver {
    private static final String TAG = "MzPushMessageReceiver";

    @Override
    @Deprecated
    public void onRegister(Context context, String s) {
        DebugLogger.i(TAG, "onRegister pushID " + s);
        print(context, "receive pushID " + s);
    }

    @Override
    public void onMessage(Context context, String s) {
        DebugLogger.i(TAG, "onMessage " + s);
        //print(context,context.getPackageName() + " receive message " + s);
        EventBus.getDefault().post(new ThroughMessageEvent(s));
    }

    @Override
    public void onMessage(Context context, Intent intent) {
        Log.i(TAG, "flyme3 onMessage ");
        String content = intent.getExtras().toString();
        print(context,"flyme3 onMessage " + content);
    }

    @Override
    public void onMessage(Context context, String message, String platformExtra) {
        Log.i(TAG, "onMessage " + message +" platformExtra "+platformExtra);
        //print(context,context.getPackageName() + " receive message " + s);
        EventBus.getDefault().post(new ThroughMessageEvent(message+platformExtra));


        // 测试直达服务
//        try {
//            JSONObject all = new JSONObject(message);
//            String notificationMessage = all.getString("push");
//            DebugLogger.e(TAG,"notificationMessage "+notificationMessage);
//            PlatformMessageSender.showQuickNotification(context,notificationMessage,platformExtra);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    @Deprecated
    public void onUnRegister(Context context, boolean b) {
        DebugLogger.i(TAG, "onUnRegister " + b);
        print(context,context.getPackageName() + " onUnRegister " + b);
    }

    @Override
    public void onPushStatus(Context context,PushSwitchStatus pushSwitchStatus) {
        EventBus.getDefault().post(pushSwitchStatus);
    }

    @Override
    public void onRegisterStatus(Context context,RegisterStatus registerStatus) {
        DebugLogger.i(TAG, "onRegisterStatus " + registerStatus+ " "+context.getPackageName());
        //print(this," onRegisterStatus " + registerStatus);
        EventBus.getDefault().post(registerStatus);
    }

    @Override
    public void onUnRegisterStatus(Context context,UnRegisterStatus unRegisterStatus) {
        DebugLogger.i(TAG,"onUnRegisterStatus "+unRegisterStatus+" "+context.getPackageName());
        EventBus.getDefault().post(unRegisterStatus);
    }

    @Override
    public void onSubTagsStatus(Context context,SubTagsStatus subTagsStatus) {
        DebugLogger.i(TAG, "onSubTagsStatus " + subTagsStatus+" "+context.getPackageName());
        EventBus.getDefault().post(subTagsStatus);
    }

    @Override
    public void onSubAliasStatus(Context context,SubAliasStatus subAliasStatus) {
        DebugLogger.i(TAG, "onSubAliasStatus " + subAliasStatus+" "+context.getPackageName());
        EventBus.getDefault().post(subAliasStatus);
    }

    @Override
    public void onUpdateNotificationBuilder(PushNotificationBuilder pushNotificationBuilder) {
        pushNotificationBuilder.setmLargIcon(R.drawable.flyme_status_ic_notification);
        pushNotificationBuilder.setmStatusbarIcon(R.drawable.mz_push_notification_small_icon);
        DebugLogger.e(TAG,"current clickpacakge "+pushNotificationBuilder.getClickPackageName());
    }

    @Override
    public void onNotificationArrived(Context context, MzPushMessage mzPushMessage) {
        DebugLogger.i(TAG, "onNotificationArrived title " + mzPushMessage.getTitle() + "content "
                + mzPushMessage.getContent() + " selfDefineContentString " + mzPushMessage.getSelfDefineContentString()+" notifyId "+mzPushMessage.getNotifyId());
        MainActivity.notifyIdList.add(mzPushMessage.getNotifyId());
        DebugLogger.e(TAG,"current notifyid "+MainActivity.notifyIdList);
    }

    @Override
    public void onNotificationClicked(Context context, MzPushMessage mzPushMessage) {
        DebugLogger.i(TAG, "onNotificationClicked title "+ mzPushMessage.getTitle() + "content "
                + mzPushMessage.getContent() + " selfDefineContentString " + mzPushMessage.getSelfDefineContentString()+" notifyId "+mzPushMessage.getNotifyId());
        int[] intArray = new int[MainActivity.notifyIdList.size()];
        for(int i=0; i<MainActivity.notifyIdList.size(); i++){
            intArray[i] = MainActivity.notifyIdList.get(i);
        }
        DebugLogger.e(TAG,"clear notifyId "+intArray);
        PushManager.clearNotification(context,intArray);
        MainActivity.notifyIdList.clear();

        if(!TextUtils.isEmpty(mzPushMessage.getSelfDefineContentString())){
            print(context," 点击自定义消息为："+mzPushMessage.getSelfDefineContentString());
        }
    }

    @Override
    public void onNotificationDeleted(Context context, MzPushMessage mzPushMessage) {
        DebugLogger.i(TAG, "onNotificationDeleted title " + mzPushMessage.getTitle() + "content "
                + mzPushMessage.getContent() + " selfDefineContentString " + mzPushMessage.getSelfDefineContentString()+" notifyId "+mzPushMessage.getNotifyId());
    }

    @Override
    public void onNotifyMessageArrived(Context context, String message) {
        DebugLogger.i(TAG, "onNotifyMessageArrived messsage " + message);
    }

    private void print(final Context context, final String info){
        new Handler(context.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, info, Toast.LENGTH_LONG).show();
            }
        });
    }

}


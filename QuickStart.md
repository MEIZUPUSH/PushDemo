# PushSDK3 Android Studio 快速开始


# 目录<a name="index"/>

* [一.准备工作](#prepare_setting)
    * [1.1 pushSDK内部版引用配置说明](#pushsdk_internal)
    * [1.2 必要的配置](#nessary_setting)
        * [1.2.1 兼容flyme5以下版本推送兼容配置](#permission_adpter_flyme5_down)
        * [1.2.2 注册消息接收Receiver](#pushmessage_receiver_manifest_setting)
        * [1.2.3 实现自有的PushReceiver,实现消息接收，注册与反注册回调](#pushmessage_receiver_code_setting)    
* [二.调用新版注册](#start_register)
* [三. 通知栏图标设置](#notification_small_icon_setting)
* [反馈与建议](README.md)
* [问题汇总说明](README.md)


# 一.准备工作<a name="prepare_setting"/>

PushSDK3.0以后的版本使用了最新的魅族插件发布aar包，因此大家可以直接引用aar包；无需关心libs so库的配置，对于一些通用的权限配置，工程混淆，应用可以不再配置了，现有你只需要在你的应用中配置相应的消息接收的receiver


## 1.1 pushSDK内部版引用配置说明<a name="pushsdk_internal"/>

* 对内版本配置如下：

```
    dependencies {
        compile 'com.meizu.flyme.internet:push-internal-publish:3.3.+@aar'
    }
```

**NOTE:** jcenter 无法下载时,请到这里[下载](https://github.com/MEIZUPUSH/PushDemo/releases)
 

## 1.2 必要的配置<a name="nessary_setting"/>

### 1.2.1 兼容flyme5以下版本推送兼容配置<a name="permission_adpter_flyme5_down"/>

```
    <!-- 兼容flyme5.0以下版本，魅族内部集成pushSDK必填，不然无法收到消息-->
    <uses-permission android:name="com.meizu.flyme.push.permission.RECEIVE"></uses-permission>
    <permission android:name="包名.push.permission.MESSAGE" android:protectionLevel="signature"/>
    <uses-permission android:name="包名.push.permission.MESSAGE"></uses-permission>
    
    <!--  兼容flyme3.0配置权限-->
    <uses-permission android:name="com.meizu.c2dm.permission.RECEIVE" />
    <permission android:name="你的包名.permission.C2D_MESSAGE"
                    android:protectionLevel="signature"></permission>
    <uses-permission android:name="你的包名.permission.C2D_MESSAGE"/>

```

#### 1.2.2 注册消息接收Receiver<a name="pushmessage_receiver_manifest_setting"/>

```xml
    <!-- push应用定义消息receiver声明 -->
    <receiver android:name="your.package.MyPushMsgReceiver">
        <intent-filter>
            <!-- 接收push消息 -->
            <action android:name="com.meizu.flyme.push.intent.MESSAGE" />
            <!-- 接收register消息 -->
            <action android:name="com.meizu.flyme.push.intent.REGISTER.FEEDBACK" />
            <!-- 接收unregister消息-->
            <action android:name="com.meizu.flyme.push.intent.UNREGISTER.FEEDBACK"/>
            <!-- 兼容低版本Flyme3推送服务配置 -->
            <action android:name="com.meizu.c2dm.intent.REGISTRATION" />
            <action android:name="com.meizu.c2dm.intent.RECEIVE" />
            <category android:name="包名"></category>
        </intent-filter>
    </receiver>
```

#### 1.2.3 实现自有的PushReceiver,实现消息接收，注册与反注册回调<a name="pushmessage_receiver_code_setting"/>

```
  public class MyPushMsgReceiver extends MzPushMessageReceiver {
    
    @Override
    @Deprecated
    public void onRegister(Context context, String pushid) {
    	//应用在接受返回的pushid
    }
    
    @Override
    public void onMessage(Context context, String s) {
    	//接收服务器推送的消息
    }
    
    @Override
    @Deprecated
    public void onUnRegister(Context context, boolean b) {
    	//调用PushManager.unRegister(context）方法后，会在此回调反注册状态
    }
    
    //设置通知栏小图标
    @Override
    public PushNotificationBuilder onUpdateNotificationBuilder(PushNotificationBuilder pushNotificationBuilder) {
    	pushNotificationBuilder.setmStatusbarIcon(R.drawable.mz_push_notification_small_icon);
    }
    
    @Override
    public void onPushStatus(Context context,PushSwitchStatus pushSwitchStatus) {
    	//检查通知栏和透传消息开关状态回调
    }
    
    @Override
    public void onRegisterStatus(Context context,RegisterStatus registerStatus) {
    	Log.i(TAG, "onRegisterStatus " + registerStatus);
        //新版订阅回调
    }
    
    @Override
    public void onUnRegisterStatus(Context context,UnRegisterStatus unRegisterStatus) {
    	Log.i(TAG,"onUnRegisterStatus "+unRegisterStatus);
        //新版反订阅回调
    }
    
    @Override
    public void onSubTagsStatus(Context context,SubTagsStatus subTagsStatus) {
    	Log.i(TAG, "onSubTagsStatus " + subTagsStatus);
    	//标签回调
    }
    
    @Override
    public void onSubAliasStatus(Context context,SubAliasStatus subAliasStatus) {
    	Log.i(TAG, "onSubAliasStatus " + subAliasStatus);
        //别名回调
    }
    @Override
    public void onNotificationArrived(Context context, String title, String content, String selfDefineContentString) {
       //通知栏消息到达回调
       DebugLogger.i(TAG,"onNotificationArrived title "+title + "content "+content + " selfDefineContentString "+selfDefineContentString);
    }
        
    @Override
    public void onNotificationClicked(Context context, String title, String content, String selfDefineContentString) {
       //通知栏消息点击回调
       DebugLogger.i(TAG,"onNotificationClicked title "+title + "content "+content + " selfDefineContentString "+selfDefineContentString);
    }
        
    @Override
    public void onNotificationDeleted(Context context, String title, String content, String selfDefineContentString) {
       //通知栏消息删除回调；flyme6以上不再回调
       DebugLogger.i(TAG,"onNotificationDeleted title "+title + "content "+content + " selfDefineContentString "+selfDefineContentString);
    }    
   
 }
	
```



# 二. 调用新版注册<a name="start_register"/>

**Note:** 至此pushSDK 已经集成完毕，现在你需要在你的Application中调用新版的[register](#register)方法,

```
   /**
     * @param context
     * @param appId
     *         push 平台申请的应用id
     * @param appKey
     *         push 平台申请的应用key
     * */
     public static void register(Context context,String appId,String appKey);
```

并在你的Receiver中成功回调onRegisterStatus(RegisterStatus registerStatus)方法就可以了，
你现在可以到[新版Push平台](http://push.meizu.com) 找到你的应用推送消息就可以了;以下内容是pushSDK提供的api汇总,具体功能详见api具体说明,请根据需求选用合适的功能
[详细功能说明参见](README.md)

# 三. 通知栏图标设置<a name="notification_small_icon_setting"/>


```
    /**
     * 获取smallicon
     * */
    public void onUpdateNotificationBuilder(PushNotificationBuilder pushNotificationBuilder){
          //设置通知栏弹出的小图标
          pushNotificationBuilder.setmStatusbarIcon(R.drawable.mz_push_notification_small_icon);
    };
```

**Note:** Flyme6新的通知栏中心需要按照名称来获取状态栏Icon,你需要在相应的drawable不同分辨率文件夹下放置一个名称为```mz_push_notification_small_icon```的状态栏图标文件,请确保名称正确，否则将无法正确显示你应用的状态栏图标 





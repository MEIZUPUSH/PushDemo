# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/liaojinlong/AndroidStudio/android-studio/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}


     # for google protobuf
        -keep public class * extends com.google.protobuf.GeneratedMessage { *; }
        -keep class com.google.protobuf.** { *; }
        -keep public class * extends com.google.protobuf.** { *; }

     #for okhttp
        -keep class okio.** {*;}
        -dontwarn okio.**
        -keep class com.squareup.okhttp.** {*;}
        -dontwarn com.squareup.okhttp.**

     # for push sdk
       -keep class com.meizu.cloud.pushsdk.** { *; }
       -dontwarn  com.meizu.cloud.pushsdk.**

       -keep class com.meizu.nebula.** { *; }
       -dontwarn com.meizu.nebula.**

        -keep class com.meizu.push.** { *; }
        -dontwarn com.meizu.push.**

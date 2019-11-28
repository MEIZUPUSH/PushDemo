# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/liaojinlong/Android/android-studio/sdk/tools/proguard/proguard-android.txt
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

# 保留资源文件属性和行号
    -renamesourcefileattribute SourceFile
    -keepattributes SourceFile,LineNumberTable

    # 防止内部类被混淆，无法访问
    -keepattributes Exceptions,InnerClasses,Signature,Deprecated,*Annotation*,EnclosingMethod

    # 保留所有重要组件
    -keep public class * extends android.app.Activity
    -keep public class * extends android.app.Application
    -keep public class * extends android.app.Service
    -keep public class * extends android.content.BroadcastReceiver
    -keep public class * extends android.content.ContentProvider


    # 保留所有 View 的实现，和它们包含 Context 参数的构造方法，还有 set 方法
    -keep public class * extends android.view.View {
        public <init>(android.content.Context);
        public <init>(android.content.Context, android.util.AttributeSet);
        public <init>(android.content.Context, android.util.AttributeSet, int);
        public void set*(...);
    }

    # 保留所有含有特殊 Context 参数构造方法的类
    -keepclasseswithmembers class * {
        public <init>(android.content.Context, android.util.AttributeSet);
    }
    -keepclasseswithmembers class * {
            public <init>(android.content.Context, android.util.AttributeSet, int);
    }

    ## 保留所有 Parcelable 实现类的特殊属性.
    -keepclassmembers class * implements android.os.Parcelable {
         static android.os.Parcelable$Creator CREATOR;
    }

    ## 有些 Android support 包里面的 api 不是在所有平台都存在的，但是我们自己用的时候有数就行
    -dontwarn android.support.**

    -keepclassmembers enum * {
            public static **[] values();
            public static ** valueOf(java.lang.String);
    }

    ## 用到序列化的实体类
    -keepclassmembers class * implements java.io.Serializable {
         static final long serialVersionUID;
             static final java.io.ObjectStreamField[] serialPersistentFields;
         private void writeObject(java.io.ObjectOutputStream);
         private void readObject(java.io.ObjectInputStream);
         java.lang.Object writeReplace();
         java.lang.Object readResolve();
    }
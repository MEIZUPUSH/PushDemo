# CentOS 安装Android 编译环境


# 目录<a name="index"/>

* [一.准备工作](#prepare_setting)
* [二.必要库安装](#so_install)
* [三. PushDemo初始化](#pushdemo_init)
* [参考资料](#reference)


# 一.准备工作<a name="prepare_setting"/>

我们已经为你准备了android sdk 全部的资源包你无需任何下载,之需要解压sdk压缩包放到指定目录即可
**NOTE:** [CentOS 远程安装教程](http://moonlightbox.logdown.com/posts/2016/02/01/linux-install-android-sdk-in-centos7)

# 二.必要库安装<a name="so_install"/>

**NOTE:** 请按照顺序执行下列命令

```
    yum install -y compat-libstdc++-296.i686
    yum install -y compat-libstdc++-33.i686
    yum install -y zlib
    yum install -y libstdc++.so.6
    yum install -y zlib.i686
```
 
* libstdc++.so.6 Protected multilib versions

```
    yum install libstdc++-4.4.7-17.el6.i686 --setopt=protected_multilib=false
```
 
# 三.PushDemo初始化<a name="pushdemo_init"/>

* 1 从github下载最新的Pushdemo[源码](https://github.com/MEIZUPUSH/PushDemo.git),可以通过git 拉取

```
    // 拉取pushdemo 源码
    git clone https://github.com/MEIZUPUSH/PushDemo.git
    // 更新pushdemo 源码
    git pull --rebase
```

* 2 配置pushdemo的AndroidSDK路径
  如果你没有在系统环境变量中配置android sdk,需要在pushdemo的根目录添加local.properties文件,并在其中添加如下内容

```
    sdk.dir=系统androidsdk的路径/sdk
```

* 3 编译命令参数说明

+ `-PAPP_ID` 应用id
+ `-PAPP_KEY` 应用的appkey
+ `-PPACKAGE_NAME` 要编译生成的应用包名


```
    //编译pushdemo的命令例子
    ./gradlew clean -PAPP_ID=100999 -PAPP_KEY=80355073480594a99470dcacccd8cf2c -PPACKAGE_NAME=com.meizu.pushdemo assembleDebug

```

# 参考资料<a name="reference"/>

* [CentOS 安装AndroidSDK](http://moonlightbox.logdown.com/posts/2016/02/01/linux-install-android-sdk-in-centos7)
* [CentOS 安装AndroidSDK遇到的问题](http://dengyin2000.iteye.com/blog/2116552)
* [yum 安装 出错 Error: Protected multilib versions](http://blog.csdn.net/qianlong4526888/article/details/9497165)
* [libstdc++.so.6 安装冲突问题](http://linuxtoolkit.blogspot.hk/2015/08/error-installing-libstc-for-centos-66.html)
* [libstdc++.so.6 安装冲突问题](http://randomclan.lofter.com/post/1ca8e9_5f5fc1f)

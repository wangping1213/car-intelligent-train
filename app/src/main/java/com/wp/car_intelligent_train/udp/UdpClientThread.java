package com.wp.car_intelligent_train.udp;

import android.content.Context;

import com.wp.car_intelligent_train.application.MyApplication;

/**
 * udp接收服务线程
 * @author wangping
 * @date 2018/6/30 16:19
 */
public class UdpClientThread implements Runnable {

    private Context context;

    private Class activityClass;

    private MyApplication application;

    public UdpClientThread(Context context, Class activityClass, MyApplication application) {
        this.context = context;
        this.activityClass = activityClass;
        this.application = application;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Class getActivityClass() {
        return activityClass;
    }

    public void setActivityClass(Class activityClass) {
        this.activityClass = activityClass;
    }

    public MyApplication getApplication() {
        return application;
    }

    public void setApplication(MyApplication application) {
        this.application = application;
    }

    @Override
    public void run() {
//        UdpSystem.search();
    }

}

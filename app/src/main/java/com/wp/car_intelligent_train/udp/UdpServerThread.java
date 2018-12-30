package com.wp.car_intelligent_train.udp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.wp.car_intelligent_train.Constant;
import com.wp.car_intelligent_train.application.MyApplication;

import org.json.JSONObject;

/**
 * udp接收服务线程
 * @author wangping
 * @date 2018/6/30 16:19
 */
public class UdpServerThread implements Runnable {

    private Context context;

    private Class activityClass;

    private MyApplication application;

    public UdpServerThread(Context context, Class activityClass, MyApplication application) {
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
        int udpState = application.getUdpState();
        String str = "";
        while (udpState != Constant.STATE_CONNECT_FAIL) {
            try {
                str = UdpServer.receive();
                Log.d("wangping", String.format("receive:%s", str));
                Thread.sleep(1000L);
                if (null != str && !str.trim().equals("")) {
                    JSONObject obj = new JSONObject(str);
                    if (null != obj) {
                        Log.d("wangping", String.format("UdpServerThread receive msg:%s", obj.getString("message")));

                    }
                }
            } catch (Exception e) {
                Log.w("wangping", String.format("getState error:%s", e.getMessage()));
                application.setUdpState(Constant.STATE_CONNECT_FAIL);//连接失败
                Intent intent = new Intent(context, activityClass);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } finally {
                udpState = application.getUdpState();
            }
        }
    }

}

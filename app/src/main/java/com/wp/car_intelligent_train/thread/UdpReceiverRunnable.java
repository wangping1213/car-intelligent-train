package com.wp.car_intelligent_train.thread;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.wp.car_intelligent_train.application.MyApplication;
import com.wp.car_intelligent_train.base.BaseActivity;
import com.wp.car_intelligent_train.udp.client.IUdpClient;
import com.wp.car_intelligent_train.udp.client.UdpClientFactory;
import com.wp.car_intelligent_train.udp.receiver.BaseUdpReceiver;
import com.wp.car_intelligent_train.udp.receiver.IUdpReceiver;
import com.wp.car_intelligent_train.udp.receiver.UdpReceiverFactory;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 接收器线程类
 * @author wangping
 * @date 2018/12/30 20:07
 */
public class UdpReceiverRunnable implements Runnable {

    private static final String TAG = "wangping";
    private Handler myHandler;
    private MyApplication application;
    private BaseActivity baseActivity;
    private String type;

    @Override
    public void run() {
//        Looper.prepare();
//        myHandler = new Handler() {
//            public void handleMessage(Message msg) {
//
//            }
//        };
//        Looper.loop();

        while (true) {
            IUdpClient udpClient = UdpClientFactory.getUdpClient(type);
            BaseUdpReceiver udpReceiver = null;
            try {
                String result = udpClient.receive();
                if (null == result || "".equals(result)) continue;
                else {
                    Log.d(TAG, String.format("receive %s result:%s", type, result));
                    JSONObject obj = new JSONObject(result);
                    if (null != obj) {
                        String cmd = null;
                        try {
                            cmd = obj.getString("cmd");
                        } catch (JSONException e) {
                        }

                        if (null != cmd && !"".equals(cmd)) {
                            Log.d(TAG, String.format("receive cmd:%s", type, result));
                        }

                        String message = null;
                        try {
                            message = obj.getString("message");
                        } catch (JSONException e) {
                        }


                        if (null != message && !"".equals(message)) {
                            udpReceiver = UdpReceiverFactory.getUdpReceiver(message);
                            if (null != udpReceiver) {
                                udpReceiver.setApplication(application);
                                udpReceiver.handle(type, result);
                            }
                        }

                    }
                }
            } catch (Exception e) {
            }
        }
    }

    public UdpReceiverRunnable(String type) {
        this.type = type;
    }

    public UdpReceiverRunnable(String type, MyApplication application) {
        this.application = application;
        this.type = type;
    }

    public Handler getMyHandler() {
        return myHandler;
    }
}

package com.wp.car_intelligent_train.udp.receiver;

import com.wp.car_intelligent_train.application.MyApplication;

import org.json.JSONObject;

/**
 * 接收connect的udp返回值
 * @author wangping
 * @date 2018/12/30 18:32
 */
public abstract class BaseUdpReceiver implements IUdpReceiver {


    protected static final String TAG = "wangping";

    protected MyApplication application;

    @Override
    public JSONObject handle(String type, String result) {
        return null;
    }

    public MyApplication getApplication() {
        return application;
    }

    public void setApplication(MyApplication application) {
        this.application = application;
    }
}

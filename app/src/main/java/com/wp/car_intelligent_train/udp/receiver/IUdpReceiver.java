package com.wp.car_intelligent_train.udp.receiver;

import org.json.JSONObject;

/**
 * udp接受处理接口
 * @author wangping
 * @date 2018/12/30 18:20
 */
public interface IUdpReceiver {
    public JSONObject handle(String type, String result);
}

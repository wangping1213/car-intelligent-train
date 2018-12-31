package com.wp.car_intelligent_train.udp.receiver;

import android.util.Log;

import com.wp.car_intelligent_train.entity.UdpResult;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 接收connect的udp返回值
 * @author wangping
 * @date 2018/12/30 18:32
 */
public class ConnectUdpReceiver extends BaseUdpReceiver {

    private static final String CMD_TYPE = "connect";

    @Override
    public JSONObject handle(String type, String result) {
        UdpResult udpResult = getApplication().getMapData("udpResult", UdpResult.class);
        if (null == udpResult) {
            udpResult = new UdpResult(CMD_TYPE);
            getApplication().getMap().put("udpResult", udpResult);
        }
        JSONObject retObj = null;
        try {
            retObj = new JSONObject(result);
        } catch (JSONException e) {
            Log.w(TAG, "connect cmd error_1!");
        }
//        getApplication().getMap().put("connect_retObj", retObj);
        udpResult.setData(retObj);

        return retObj;
    }
}

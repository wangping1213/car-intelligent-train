package com.wp.car_intelligent_train.udp.receiver;

import android.util.Log;

import com.wp.car_intelligent_train.entity.UdpResult;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 接收getState的udp返回值
 * @author wangping
 * @date 2018/12/30 18:32
 */
public class GetStateUdpReceiver extends BaseUdpReceiver {

    private static final String CMD_TYPE = "getState";

    @Override
    public synchronized JSONObject handle(String type, String result) {

        JSONObject retObj = null;
        if ("2".equals(getApplication().getMapData("getState_flag", String.class))) {
            try {
                retObj = new JSONObject("{'flag': '1'}");
                getApplication().getMap().put("getState_retObj", retObj);
            } catch (JSONException e) {
            }
            return retObj;
        }
        UdpResult udpResult = getApplication().getMapData("udpResult", UdpResult.class);
        if (null == udpResult) {
            udpResult = new UdpResult(CMD_TYPE);
            getApplication().getMap().put("udpResult", udpResult);
        }

        try {
            JSONObject obj = new JSONObject(result);
            retObj = obj.getJSONObject("data");
        } catch (JSONException e) {
            Log.w(TAG, "getState cmd error_1!");
        }
        udpResult.setData(retObj);
//        getApplication().getMap().put("getState_retObj", retObj);

        return retObj;
    }
}

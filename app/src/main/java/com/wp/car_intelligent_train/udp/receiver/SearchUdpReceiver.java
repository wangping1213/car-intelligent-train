package com.wp.car_intelligent_train.udp.receiver;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.wp.car_intelligent_train.Constant;
import com.wp.car_intelligent_train.entity.UdpResult;
import com.wp.car_intelligent_train.udp.client.IUdpClient;
import com.wp.car_intelligent_train.udp.client.UdpClientFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 接收search的udp返回值
 *
 * @author wangping
 * @date 2018/12/30 18:32
 */
public class SearchUdpReceiver extends BaseUdpReceiver {

    private static final String CMD_TYPE = "search";

    @Override
    public JSONObject handle(String type, String result) {
        UdpResult udpResult = getApplication().getMapData("udpResult", UdpResult.class);
        if (null == udpResult) {
            udpResult = new UdpResult(CMD_TYPE);
            getApplication().getMap().put("udpResult", udpResult);
        }
        JSONObject retObj = null;
        if (UdpClientFactory.VBS_KEY.equals(type)) {//由于有多个不同的消息源，故在另一个线程中以等待时间3s来进行分隔
            if ("0".equals(udpResult.getFlag())) {
                List<String> vbsList = udpResult.getDataByClass(List.class);
                if (null == vbsList) {
                    vbsList = new ArrayList<>();
                    udpResult.setData(vbsList);
                }
                vbsList.add(result);
            }
        } else if (UdpClientFactory.TBOX_KEY.equals(type)) {
            try {
                JSONObject obj = new JSONObject(result);
                retObj = obj.getJSONObject("data");
            } catch (JSONException e) {
                Log.w(TAG, "search cmd error_1!");
            }
            udpResult.setData(retObj);
            udpResult.setFlag("1");
        }

        return retObj;
    }
}

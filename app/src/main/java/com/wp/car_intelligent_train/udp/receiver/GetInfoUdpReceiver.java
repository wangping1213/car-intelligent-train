package com.wp.car_intelligent_train.udp.receiver;

import android.util.Log;

import com.wp.car_intelligent_train.entity.UdpResult;
import com.wp.car_intelligent_train.udp.client.IUdpClient;
import com.wp.car_intelligent_train.udp.client.UdpClientFactory;

import org.json.JSONObject;

/**
 * 接收setPoint的udp返回值
 * @author wangping
 * @date 2018/12/30 18:32
 */
public class GetInfoUdpReceiver extends BaseUdpReceiver {

    private static final String CMD_TYPE = "getInfo";

    @Override
    public JSONObject handle(String type, String result) {
        UdpResult udpResult = getApplication().getMapData("udpResult", UdpResult.class);
        if (null == udpResult) {
            udpResult = new UdpResult(CMD_TYPE);
            getApplication().getMap().put("udpResult", udpResult);
        }
        IUdpClient udpClient = UdpClientFactory.getUdpClient(type);
        JSONObject retObj = null;
        String msg = "";
        int customId = getApplication().getCustomId();

        StringBuffer sb = null;
        if (CMD_TYPE.equals(udpResult.getCmdType())) {
            if ("1".equals(udpResult.getFlag())) return null;
            else sb = udpResult.getDataByClass(StringBuffer.class);
            if (null == sb) sb = new StringBuffer();
            try {
                JSONObject obj = new JSONObject(result);
                retObj = obj.getJSONObject("data");
                int sum = retObj.getInt("sum");
                int num = retObj.getInt("num");
                sb.append(retObj.getString("data"));
                if (num < sum) {
                    msg = String.format("{\"cmd\":\"getInfo\",\"parm\":{\"ID\":%s,\"num\":%s}}", customId, num + 1);
                    Thread.sleep(20L);
                    udpClient.sendMsg(msg);
                    Log.d(TAG, String.format("getPart msg:%s", msg));
                } else {
                    udpResult.setFlag("1");
                }
            } catch (Exception e) {
                Log.w(TAG, "getState cmd error_1!");
            } finally {
                udpResult.setData(sb);
            }
        }

        return retObj;
    }
}

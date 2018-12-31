package com.wp.car_intelligent_train.udp;

import android.content.Context;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import com.wp.car_intelligent_train.Constant;
import com.wp.car_intelligent_train.application.MyApplication;
import com.wp.car_intelligent_train.entity.UdpResult;
import com.wp.car_intelligent_train.udp.client.IUdpClient;
import com.wp.car_intelligent_train.udp.client.UdpClientFactory;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * udp子系统的使用类
 *
 * @author wangping
 * @date 2018/6/30 9:28
 */
public class UdpSystem {

    private static final String TAG = "wangping";
    private static KeepConnectThread thread;
    private static MyApplication application;

    /**
     * 搜索
     *
     * @return
     * @throws Exception
     */
    public static JSONObject search(String type, Handler... handlers) throws Exception {
        IUdpClient udpClient = UdpClientFactory.getUdpClient(type);
        String msg = "";
        if (UdpClientFactory.VBS_KEY.equals(type)) {
            msg = "{\"cmd\":\"search\",\"parm\":{\"type\":\"JG-TD-Type-I\"}}";
        } else if (UdpClientFactory.TBOX_KEY.equals(type)) {
            msg = "{\"cmd\":\"search\",\"parm\":{\"type\":\"JG-VDB-Type-II\"}}";
        }
        JSONObject result = null;
        udpClient.sendMsg(msg);
        application.removeMapData("udpResult");
        if (UdpClientFactory.VBS_KEY.equals(type)) {
            Thread.sleep(Constant.UDP_WAIT_TIME * 3);
            UdpResult udpResult = application.getMapData("udpResult", UdpResult.class);
            if (null == udpResult) udpResult = new UdpResult("search");
            udpResult.setFlag("1");
            if (null != handlers && handlers.length > 0) {
                Handler hd = handlers[0];
                int what = 0;
                if (null != udpResult.getData()) {
                    what = 1;
                }
                hd.sendEmptyMessage(what);
            }
        } else if (UdpClientFactory.TBOX_KEY.equals(type)) {
            Thread.sleep(Constant.UDP_WAIT_TIME);
            UdpResult udpResult = application.getMapData("udpResult", UdpResult.class);
            if (null == udpResult) udpResult = new UdpResult("search");
            result = udpResult.getDataByClass(JSONObject.class);
        }
        return result;
    }

    /**
     * 连接
     *
     * @return
     * @throws Exception
     */
    public static JSONObject connect(String deviceNo, String type) throws Exception {
        IUdpClient udpClient = UdpClientFactory.getUdpClient(type);
        JSONObject result = null;
        udpClient.sendMsg(String.format("{\"cmd\":\"connect\",\"parm\":{\"SN\":\"%s\"}}", deviceNo));
        application.removeMapData("udpResult");
        Thread.sleep(Constant.UDP_WAIT_TIME);
        UdpResult udpResult = application.getMapData("udpResult", UdpResult.class);
        if (null == udpResult) udpResult = new UdpResult("connect");
        result = udpResult.getDataByClass(JSONObject.class);
        return result;
    }

    /**
     * 断开连接
     *
     * @return
     * @throws Exception
     */
    public static JSONObject disconnect(int customId, String type) throws Exception {
        IUdpClient udpClient = UdpClientFactory.getUdpClient(type);
        udpClient.sendMsg(String.format("{\"cmd\":\"disconnect\",\"parm\":{\"ID\":\"%s\"}}", customId));
        JSONObject result = null;

        application.removeMapData("udpResult");
        Thread.sleep(Constant.UDP_WAIT_TIME);
        UdpResult udpResult = application.getMapData("udpResult", UdpResult.class);
        if (null == udpResult) udpResult = new UdpResult("disconnect");
        result = udpResult.getDataByClass(JSONObject.class);
        return result;
    }

    /**
     * 连接
     *
     * @return
     * @throws Exception
     */
    public static JSONObject getState(String type, int customId, long... waitTimes) throws Exception {
        IUdpClient udpClient = UdpClientFactory.getUdpClient(type);
        JSONObject result = null;
        if (waitTimes.length > 0) {
            application.getMap().put("getState_flag", "2");
        } else {
            application.getMap().put("getState_flag", "1");
        }
        udpClient.sendMsg(String.format("{\"cmd\":\"getState\",\"parm\":{\"ID\":%s}}", customId));

        if (waitTimes.length == 0) {
            application.removeMapData("udpResult");
        }

        Thread.sleep(Constant.UDP_WAIT_TIME);
        UdpResult udpResult = application.getMapData("udpResult", UdpResult.class);
        if (null == udpResult) udpResult = new UdpResult("getState");
        if (waitTimes.length == 0 && "getState".equals(udpResult.getCmdType())) {
            result = udpResult.getDataByClass(JSONObject.class);
        } else {
            result = application.getMapData("getState_retObj", JSONObject.class);
        }
        return result;
    }

    public static JSONObject getNowState() {
        JSONObject obj = null;
        try {
            obj = UdpSystem.getState(application.getType(), application.getCustomId());
            JSONArray stateArr = obj.getJSONArray("state");

            for (int i = 0; i < stateArr.length(); i++) {
                application.getPointMap().put(i + 1, stateArr.getInt(i));
            }
            application.setStateTime(System.currentTimeMillis());
        } catch (Exception e) {
            Log.e(TAG, "page4 getState error!", e);
        }
        return obj;
    }

    public static Thread keepConnect(final MyApplication application, final Context context, final Class activityClass) throws Exception {
        if (null != thread) return thread;
        thread = new KeepConnectThread(application, context, activityClass);
        thread.start();
        return thread;
    }

    /**
     * 取得汽车元器件信息
     *
     * @param customId
     * @return
     * @throws Exception
     */
    public static String getInfo(int customId, String type) throws Exception {
        IUdpClient udpClient = UdpClientFactory.getUdpClient(type);
        StringBuffer sb = null;
        String result = null;
        udpClient.sendMsg(String.format("{\"cmd\":\"getInfo\",\"parm\":{\"ID\":%s,\"num\":%s}}", customId, 1));
        application.removeMapData("udpResult");
        Thread.sleep(Constant.UDP_WAIT_TIME * 3);
        UdpResult udpResult = application.getMapData("udpResult", UdpResult.class);
        if (null == udpResult) udpResult = new UdpResult("getInfo");
        sb = udpResult.getDataByClass(StringBuffer.class);
        int count = 0;
        if ("getInfo".equals(udpResult.getCmdType())) {
            while (true) {
                if ("1".equals(udpResult.getFlag())) {
                    result = new String(Base64.decode(sb.toString(), Base64.DEFAULT));
                    count = 0;
                    break;
                } else {
                    Log.d(TAG, "getPart count:5");
                    if (count <= 15) break;
                    count++;
                    Thread.sleep(Constant.UDP_WAIT_TIME);
                }
            }
        }

        return result;


    }

    /**
     * 操作
     *
     * @param customId
     * @param aNum
     * @param aType
     * @return
     * @throws Exception
     */
    public static JSONObject setPoint(int customId, int aNum, int aType, String type) throws Exception {
        IUdpClient udpClient = UdpClientFactory.getUdpClient(type);
        JSONObject result = null;
        String msg = String.format("{\"cmd\":\"setPoint\",\"parm\":{\"ID\":%s,\"aNum\":%s,\"aType\":%s}}", customId, aNum, aType);
        udpClient.sendMsg(msg);

        application.removeMapData("udpResult");
        Thread.sleep(Constant.UDP_WAIT_TIME );
        UdpResult udpResult = application.getMapData("udpResult", UdpResult.class);
        if (null == udpResult) udpResult = new UdpResult("setPoint");
        result = udpResult.getDataByClass(JSONObject.class);
        return result;

    }

    /**
     * 重置
     *
     * @param customId
     * @return
     * @throws Exception
     */
    public static JSONObject resetPoint(int customId, String type) throws Exception {
        IUdpClient udpClient = UdpClientFactory.getUdpClient(type);
        udpClient.sendMsg(String.format("{\"cmd\":\"resetPoint\",\"parm\":{\"ID\":%s}}", customId));
        JSONObject result = null;

        application.removeMapData("udpResult");
        Thread.sleep(Constant.UDP_WAIT_TIME );
        UdpResult udpResult = application.getMapData("udpResult", UdpResult.class);
        if (null == udpResult) udpResult = new UdpResult("resetPoint");
        result = udpResult.getDataByClass(JSONObject.class);
        return result;

    }

    public static KeepConnectThread getThread() {
        return thread;
    }

    public static void setThread(KeepConnectThread thread) {
        UdpSystem.thread = thread;
    }

    public static void setApplication(MyApplication application) {
        UdpSystem.application = application;
    }


}

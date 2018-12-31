package com.wp.car_intelligent_train.udp.receiver;

import com.wp.car_intelligent_train.application.MyApplication;
import com.wp.car_intelligent_train.thread.UdpReceiverRunnable;
import com.wp.car_intelligent_train.udp.client.UdpClientFactory;

/**
 * udp消息接收中心
 * @author wangping
 * @date 2018/12/30 20:36
 */
public class UdpReceiverCenter {
    private static final String TAG = "wangping";

    private static UdpReceiverCenter udpReceiverCenter;

    private UdpReceiverCenter() {}

    public void init(final MyApplication application) {
        new Thread(new UdpReceiverRunnable(UdpClientFactory.VBS_KEY, application)).start();
        new Thread(new UdpReceiverRunnable(UdpClientFactory.TBOX_KEY, application)).start();
    }

    public static UdpReceiverCenter newInstance(MyApplication application) {
        if (null == udpReceiverCenter) {
            udpReceiverCenter = new UdpReceiverCenter();
            udpReceiverCenter.init(application);
        }
        application.getMap().put("udpReceiverCenter", udpReceiverCenter);
        return udpReceiverCenter;
    }

}

package com.wp.car_intelligent_train.udp;

/**
 * udp工厂
 * @author wangping
 * @date 2018/12/26 19:25
 */
public class UdpClientFactory {

    public final static String VBS_KEY = "VBS";
    public final static String TBOX_KEY = "TBOX";
    public final static String TD_KEY = "TD";

    public static IUdpClient getUdpClient(String type) {
        IUdpClient client = null;
        if (null == type || "".equals(type)) return null;
        if (UdpClientFactory.TBOX_KEY.equals(type.toUpperCase())) {
            client = UdpBoxClient.newInstane();
        } else if (UdpClientFactory.VBS_KEY.equals(type.toUpperCase())) {
            client = UdpVbsClient.newInstane();
        } else if (UdpClientFactory.TD_KEY.equals(type.toUpperCase())) {
            client = UdpVbsClient.newInstane();
        }
        return client;
    }
}

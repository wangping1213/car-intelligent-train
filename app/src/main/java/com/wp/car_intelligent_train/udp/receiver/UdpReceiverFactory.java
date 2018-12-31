package com.wp.car_intelligent_train.udp.receiver;

/**
 * udp接收器工厂
 * @author wangping
 * @date 2018/12/30 21:18
 */
public class UdpReceiverFactory {

    private static BaseUdpReceiver searchUdpReceiver;
    private static BaseUdpReceiver connectUdpReceiver;
    private static BaseUdpReceiver disconnectUdpReceiver;
    private static BaseUdpReceiver getInfoUdpReceiver;
    private static BaseUdpReceiver getStateUdpReceiver;
    private static BaseUdpReceiver resetUdpReceiver;
    private static BaseUdpReceiver setPointUdpReceiver;

    public static BaseUdpReceiver getUdpReceiver(String cmdType) {
        BaseUdpReceiver udpReceiver = null;
        if ("search".equals(cmdType)) {
            udpReceiver = getSearchUdpReceiver();
        } else if ("connect".equals(cmdType)) {
            udpReceiver = getConnectUdpReceiver();
        } else if ("disconnect".equals(cmdType)) {
            udpReceiver = getDisconnectUdpReceiver();
        } else if ("getInfo".equals(cmdType)) {
            udpReceiver = getGetInfoUdpReceiver();
        } else if ("getState".equals(cmdType)) {
            udpReceiver = getGetStateUdpReceiver();
        } else if ("resetPoint".equals(cmdType)) {
            udpReceiver = getResetUdpReceiver();
        } else if ("setPoint".equals(cmdType)) {
            udpReceiver = getSetPointUdpReceiver();
        }
        return udpReceiver;
    }

    public static BaseUdpReceiver getSearchUdpReceiver() {
        if (null == searchUdpReceiver) {
            searchUdpReceiver = new SearchUdpReceiver();
        }
        return searchUdpReceiver;
    }

    public static BaseUdpReceiver getConnectUdpReceiver() {
        if (null == connectUdpReceiver) {
            connectUdpReceiver = new ConnectUdpReceiver();
        }
        return connectUdpReceiver;
    }

    public static BaseUdpReceiver getDisconnectUdpReceiver() {
        if (null == disconnectUdpReceiver) {
            disconnectUdpReceiver = new DisconnectUdpReceiver();
        }
        return disconnectUdpReceiver;
    }

    public static BaseUdpReceiver getGetInfoUdpReceiver() {
        if (null == getInfoUdpReceiver) {
            getInfoUdpReceiver = new GetInfoUdpReceiver();
        }
        return getInfoUdpReceiver;
    }

    public static BaseUdpReceiver getGetStateUdpReceiver() {
        if (null == getStateUdpReceiver) {
            getStateUdpReceiver = new GetStateUdpReceiver();
        }
        return getStateUdpReceiver;
    }

    public static BaseUdpReceiver getResetUdpReceiver() {
        if (null == resetUdpReceiver) {
            resetUdpReceiver = new ResetUdpReceiver();
        }
        return resetUdpReceiver;
    }

    public static BaseUdpReceiver getSetPointUdpReceiver() {
        if (null == setPointUdpReceiver) {
            setPointUdpReceiver = new SetPointUdpReceiver();
        }
        return setPointUdpReceiver;
    }
}

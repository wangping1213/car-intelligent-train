package com.wp.car_intelligent_train;

/**
 * 常量类
 * @author wangping
 * @date 2018/6/30 9:22
 */
public class Constant {

    public static final int UDP_BOX_PORT = 32015;
    public static final int UDP_VBS_PORT = 31130;
    public static final String UDP_IP = "224.0.0.1";//广播地址
    public static final int UDP_TIMEOUT = 5000;//毫秒
    public static final int UDP_WAIT_TIME = 200;//毫秒
    public static final int UDP_BYTE_LENGTH = 512;//udp的字节数据长度



    /**
     * 未连接
     */
    public static final int STATE_NOT_CONNECT = 0;

    /**
     * 已连接
     */
    public static final int STATE_CONNECTED = 1;

    /**
     * 连接失败
     */
    public static final int STATE_CONNECT_FAIL = 2;
}

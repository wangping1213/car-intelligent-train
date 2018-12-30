package com.wp.car_intelligent_train.udp;

import android.util.Log;

import com.wp.car_intelligent_train.Constant;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * udp客户端接口
 * @author wangping
 * @date 2018/12/26 19:20
 */
public interface IUdpClient {

    /**
     * 发送消息
     * @param str
     */
    void sendMsg(String str);

    /**
     * 接收消息
     * @return
     * @throws Exception
     */
    String receive() throws Exception;
}

package com.wp.car_intelligent_train.udp;

import android.util.Log;

import com.wp.car_intelligent_train.Constant;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * udp服务端
 * @author wangping
 * @date 2018/6/30 9:21
 */
public class UdpServer {
    private static MulticastSocket ms;
    private static InetAddress address;

    private static void initSocket() {
        if (null == ms) {
            try {
                ms = new MulticastSocket(Constant.UDP_BOX_PORT);
                address = InetAddress.getByName(Constant.UDP_IP);
                if (!address.isMulticastAddress()) {
                    Log.w("wangping", "Not MulticastAddress!");
                }
                ms.setSoTimeout(Constant.UDP_TIMEOUT);
                ms.joinGroup(address);
            } catch (IOException e) {
                Log.w("wangping", e.getMessage());
            }
        }
    }

    public static String receive() throws Exception {
        String resultStr = "";
        initSocket();
        byte buf[] = new byte[Constant.UDP_BYTE_LENGTH];
        DatagramPacket dp = new DatagramPacket(buf, buf.length);
        ms.receive(dp);
        resultStr = new String(dp.getData(), 0, dp.getLength());
        return resultStr;
    }

}

package com.wp.car_intelligent_train.entity;

import java.io.Serializable;

/**
 * udp协议返回值
 * @author wangping
 * @date 2018/12/31 8:58
 */
public class UdpResult implements Serializable {

    private String flag = "0";
    private String cmdType;
    private Object data;

    public UdpResult() {
    }

    public UdpResult(String cmdType) {
        this.cmdType = cmdType;
    }

    public UdpResult(String flag, String cmdType, Object data) {
        this.flag = flag;
        this.cmdType = cmdType;
        this.data = data;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getCmdType() {
        return cmdType;
    }

    public void setCmdType(String cmdType) {
        this.cmdType = cmdType;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public <T> T getDataByClass(Class<T> tClass) {
        return (T) this.data;
    }
}

package com.wp.car_intelligent_train.entity;

import java.io.Serializable;

/**
 * 汽车元器件针脚
 * @author wangping
 * @date 2018/7/1 17:54
 */
public class CarPartPin implements Serializable{
    private String name;
    private String pid;
    private String cid;
    private int anum;
    private int[] atype;
    private int currentType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public int getAnum() {
        return anum;
    }

    public void setAnum(int anum) {
        this.anum = anum;
    }

    public int[] getAtype() {
        return atype;
    }

    public void setAtype(int[] atype) {
        this.atype = atype;
    }

    public int getCurrentType() {
        return currentType;
    }

    public void setCurrentType(int currentType) {
        this.currentType = currentType;
    }
}

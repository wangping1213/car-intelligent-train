package com.wp.car_intelligent_train.entity;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * 子系统类
 * @author wangping
 * @date 2018/7/7 10:51
 */
public class CarSystem implements Serializable, Comparable<CarSystem> {
    private String ssid;
    private int drawableId;
    private String state;//状态

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj || null == this.getSsid()) return false;
        CarSystem carSystem = (CarSystem) obj;
        return this.getSsid().equals(carSystem.getSsid());
    }

    @Override
    public int compareTo(@NonNull CarSystem o) {
        if (null != o && this.getSsid() != null && !"".equals(this.getSsid()) && o.getSsid() != null && !"".equals(o.getSsid())) {
            return this.getSsid().compareTo(o.getSsid());
        } else {
            return 0;
        }
    }
}

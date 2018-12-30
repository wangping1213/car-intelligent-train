package com.wp.car_intelligent_train.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 汽车元器件
 * @author wangping
 * @date 2018/7/1 17:53
 */
public class CarPart implements Serializable {
    private String name;//名称
    private List<CarPartPin> pinList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CarPartPin> getPinList() {
        return pinList;
    }

    public void setPinList(List<CarPartPin> pinList) {
        this.pinList = pinList;
    }
}

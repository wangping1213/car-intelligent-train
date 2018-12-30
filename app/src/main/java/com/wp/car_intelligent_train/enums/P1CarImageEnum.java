package com.wp.car_intelligent_train.enums;

import com.wp.car_intelligent_train.R;

/**
 *
 * @author wangping
 * @date 2018/12/30 16:10
 */
public enum  P1CarImageEnum {
    P1_CAR_1(R.drawable.p1_img_car_1, "雪佛兰科鲁兹"),
    P1_CAR_2(R.drawable.p1_img_car_2, "北汽EV160"),
    P1_CAR_3(R.drawable.p1_img_car_3, "比亚迪E5")
    ;


    /**
     * 图片drawable的id
     */
    private int drawableId;

    /**
     * 名称
     */
    private String name;

    P1CarImageEnum(int drawableId, String name) {
        this.drawableId = drawableId;
        this.name = name;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 根据图片drawableId取得对应enum
     *
     * @param drawableId
     * @return
     */
    public static P1CarImageEnum getEnumByDrawableId(int drawableId) {
        P1CarImageEnum tmpEnum = null;
        if (drawableId == 0) return null;
        for (P1CarImageEnum drawableEnum : P1CarImageEnum.values()) {
            if (drawableEnum.getDrawableId() == drawableId) {
                tmpEnum = drawableEnum;
                break;
            }
        }
        return tmpEnum;
    }
}

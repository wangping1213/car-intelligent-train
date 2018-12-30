package com.wp.car_intelligent_train.enums;


import com.wp.car_intelligent_train.R;

/**
 * drawable与字符串的转换
 * @author wangping
 * @version 1.0
 * @since 2018/5/17 15:02
 */
public enum P2DrawableEnum {
    P2_AIR_CONDITION(R.drawable.p2_air_condition_sytle, R.drawable.p2_air_condition_select, "AC", "空调系统"),
    P2_CAR_BODY(R.drawable.p2_car_body_sytle, R.drawable.p2_car_body_select, "BCM", "车身电气系统"),
    P2_CHASSIS(R.drawable.p2_chassis_sytle, R.drawable.p2_chassis_select, "AT", "底盘电控系统"),
    P2_ENGINE(R.drawable.p2_engine_sytle, R.drawable.p2_engine_select, "ECU", "发动机系统"),
    P2_BMS_CONDITION(R.drawable.p2_bms_sytle, R.drawable.p2_bms_select, "BMS", "电池管理系统"),
    P2_PEU(R.drawable.p2_peu_sytle, R.drawable.p2_peu_select, "PEU", "电机控制系统"),
    P2_VCU(R.drawable.p2_vcu_sytle, R.drawable.p2_vcu_select, "VCU", "整车控制系统"),
    P2_VBS(R.drawable.p2_vbs_sytle, R.drawable.p2_vbs_select, "VBS", "VBS基站"),
    P2_OTHER(R.drawable.p2_other_sytle, R.drawable.p2_other_select, "OTHER", "其他")
    ;

    /**
     * 普通图片drawable的id
     */
    private int normalDrawableId;

    /**
     * 选中图片drawableId
     */
    private int selectedDrawableId;

    /**
     * 关键字
     */
    private String str;

    /**
     * 名称
     */
    private String name;

    P2DrawableEnum(int normalDrawableId, int selectedDrawableId, String str, String name) {
        this.normalDrawableId = normalDrawableId;
        this.selectedDrawableId = selectedDrawableId;
        this.str = str;
        this.name = name;
    }

    public int getNormalDrawableId() {
        return normalDrawableId;
    }

    public void setNormalDrawableId(int normalDrawableId) {
        this.normalDrawableId = normalDrawableId;
    }

    public int getSelectedDrawableId() {
        return selectedDrawableId;
    }

    public void setSelectedDrawableId(int selectedDrawableId) {
        this.selectedDrawableId = selectedDrawableId;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 根据图片key取得对应enum
     *
     * @param str
     * @return
     */
    public static P2DrawableEnum getDrawableEnumByStr(String str) {
        P2DrawableEnum tmpEnum = null;
        if (null == str && str.trim().endsWith("")) return null;
        for (P2DrawableEnum drawableEnum : P2DrawableEnum.values()) {
            if (str.equals(drawableEnum.str)) {
                tmpEnum = drawableEnum;
                break;
            }
        }
        if (null == tmpEnum) {
            tmpEnum = P2DrawableEnum.P2_OTHER;
        }
        return tmpEnum;
    }

    /**
     * 根据图片key取得对应enum
     *
     * @param normalDrawableId
     * @return
     */
    public static P2DrawableEnum getEnumByNomalDrawableId(int normalDrawableId) {
        P2DrawableEnum tmpEnum = null;
        if (normalDrawableId == 0) return null;
        for (P2DrawableEnum drawableEnum : P2DrawableEnum.values()) {
            if (drawableEnum.getNormalDrawableId() == normalDrawableId) {
                tmpEnum = drawableEnum;
                break;
            }
        }
        return tmpEnum;
    }

}

package com.wp.car_intelligent_train.enums;


import com.wp.car_intelligent_train.R;

/**
 * drawable与字符串的转换
 * @author wangping
 * @version 1.0
 * @since 2018/5/17 15:02
 */
public enum P5DrawableEnum {
    P5_AC(R.drawable.p5_ac_sytle, R.drawable.p5_ac_select, "AC", "空调系统"),
    P5_BCM(R.drawable.p5_bcm_sytle, R.drawable.p5_bcm_select, "BCM", "车身电气系统"),
    P5_AT(R.drawable.p5_at_sytle, R.drawable.p5_at_select, "AT", "底盘电控系统"),
    P5_ECU(R.drawable.p5_ecu_sytle, R.drawable.p5_ecu_select, "ECU", "发动机系统"),
    P5_BMS(R.drawable.p5_bms_sytle, R.drawable.p5_bms_select, "BMS", "电池管理系统"),
    P5_PEU(R.drawable.p5_peu_sytle, R.drawable.p5_peu_select, "PEU", "电机控制系统"),
    P5_VCU(R.drawable.p5_vcu_sytle, R.drawable.p5_vcu_select, "VCU", "整车控制系统")
//    P5_OTHER(R.drawable.p5_other_sytle, R.drawable.p5_other_select, "OTHER", "其他")
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

    P5DrawableEnum(int normalDrawableId, int selectedDrawableId, String str, String name) {
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
    public static P5DrawableEnum getDrawableEnumByStr(String str) {
        P5DrawableEnum tmpEnum = null;
        if (null == str && str.trim().endsWith("")) return null;
        for (P5DrawableEnum drawableEnum : P5DrawableEnum.values()) {
            if (str.startsWith(drawableEnum.str)) {
                tmpEnum = drawableEnum;
                break;
            }
        }
//        if (null == tmpEnum) {
//            tmpEnum = P5DrawableEnum.P2_OTHER;
//        }
        return tmpEnum;
    }

    /**
     * 根据图片key取得对应enum
     *
     * @param normalDrawableId
     * @return
     */
    public static P5DrawableEnum getEnumByNomalDrawableId(int normalDrawableId) {
        P5DrawableEnum tmpEnum = null;
        if (normalDrawableId == 0) return null;
        for (P5DrawableEnum drawableEnum : P5DrawableEnum.values()) {
            if (drawableEnum.getNormalDrawableId() == normalDrawableId) {
                tmpEnum = drawableEnum;
                break;
            }
        }
        return tmpEnum;
    }

}

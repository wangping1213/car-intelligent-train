package com.wp.car_intelligent_train.application;

import android.app.Activity;
import android.app.Application;

import com.wp.car_intelligent_train.Constant;
import com.wp.car_intelligent_train.base.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 全局变量类
 *
 * @author wangping
 * @date 2018/6/30 10:54
 */
public class MyApplication extends Application {
    private int customId;
    /**
     * udp状态：0-未连接，1-已连接，2-连接失败
     */
    private int udpState = Constant.STATE_NOT_CONNECT;

    private String type;

    private Map<Integer, Integer> pointMap = new ConcurrentHashMap<>();

    private Map<String, Object> map = new ConcurrentHashMap<>();

    private long stateTime;//取得getState结果的时间

    private List<Activity> activityList;

    private Class<?> currentActivityClass;

    public void onCreate() {
        super.onCreate();
        activityList = new ArrayList<Activity>();
    }

    /**
     * 添加Activity
     */
    public void addActivity(Activity activity) {
        // 判断当前集合中不存在该Activity
        if (!activityList.contains(activity)) {
            activityList.add(activity);//把当前Activity添加到集合中
        }
    }

    /**
     * 销毁单个Activity
     */
    public void removeActivity(Activity activity) {
        //判断当前集合中存在该Activity
        if (activityList.contains(activity)) {
            activityList.remove(activity);//从集合中移除
            activity.finish();//销毁当前Activity
        }
    }

    public Activity getActivity(int index) {
        return this.activityList.get(index);
    }

    /**
     * 销毁所有的Activity
     */
    public void removeAllActivity() {
        //通过循环，把集合中的所有Activity销毁
        for (Activity activity : activityList) {
            activity.finish();
        }
    }

    public <T> T getMapData(String key, Class<T> tClass) {
        return (T) this.getMap().get(key);
    }

    public void removeMapData(String key) {
        this.getMap().remove(key);
    }

    /**
     * 0-不重载，1-重新加载
     */
    private int reloadFlag = 0;

    public int getCustomId() {
        return customId;
    }

    public void setCustomId(int customId) {
        this.customId = customId;
    }

    public int getUdpState() {
        return udpState;
    }

    public void setUdpState(int udpState) {
        this.udpState = udpState;
    }

    public Map<Integer, Integer> getPointMap() {
        return pointMap;
    }

    public void setPointMap(Map<Integer, Integer> pointMap) {
        this.pointMap = pointMap;
    }

    public int getReloadFlag() {
        return reloadFlag;
    }

    public void setReloadFlag(int reloadFlag) {
        this.reloadFlag = reloadFlag;
    }

    public long getStateTime() {
        return stateTime;
    }

    public void setStateTime(long stateTime) {
        this.stateTime = stateTime;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Class<?> getCurrentActivityClass() {
        return currentActivityClass;
    }

    public void setCurrentActivityClass(Class<?> currentActivityClass) {
        this.currentActivityClass = currentActivityClass;
    }
}

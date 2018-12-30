package com.wp.car_intelligent_train.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.wp.car_intelligent_train.util.TimeUtil;

import java.util.List;

public class NetworkChangeReceiver extends BroadcastReceiver {

    private static final String TAG = "wangping";

    private WifiManager mWifiManager;

    private OnScanResultsListener onScanResultsListener;
    private OnConnectionChangeListener onConnectionChangeListener;


    public NetworkChangeReceiver() {
    }

    /**
     * 获取连接类型
     *
     * @param type
     * @return
     */
    private String getConnectionType(int type) {
        String connType = "";
        if (type == ConnectivityManager.TYPE_MOBILE) {
            connType = "3G网络数据";
        } else if (type == ConnectivityManager.TYPE_WIFI) {
            connType = "WIFI网络";
        }
        return connType;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {// 监听wifi的打开与关闭，与wifi的连接无关
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
//            Log.e(TAG, "wifiState:" + wifiState);
            switch (wifiState) {
                case WifiManager.WIFI_STATE_DISABLED:
                    break;
                case WifiManager.WIFI_STATE_DISABLING:
                    break;
            }
        }
        // 监听网络连接，包括wifi和移动数据的打开和关闭,以及连接上可用的连接都会接到监听
        else if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            //获取联网状态的NetworkInfo对象
            NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (info != null) {
                boolean isConnection = false;
                //如果当前的网络连接成功并且网络连接可用
                if (NetworkInfo.State.CONNECTED == info.getState() && info.isAvailable()) {
                    if (info.getType() == ConnectivityManager.TYPE_WIFI || info.getType() == ConnectivityManager.TYPE_MOBILE) {
                        Log.i(TAG, String.format("%s连上，ssid:%s, date:%s", getConnectionType(info.getType()), info.getExtraInfo(), TimeUtil.getNowStrTime()));
                    }
                    isConnection = true;
                } else {
                    Log.i(TAG, String.format("%s断开，ssid:%s, date:%s", getConnectionType(info.getType()), info.getExtraInfo(), TimeUtil.getNowStrTime()));
                    isConnection = false;
                }
                if (null != onConnectionChangeListener)
                    onConnectionChangeListener.onConnectionChangeHandler(isConnection, info.getExtraInfo());

            }
        } else if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action)) {
//            Log.d(TAG, "接收到" + TimeUtil.getNowStrTime());
            List<ScanResult> mScanResults = mWifiManager.getScanResults();
//            Log.d(TAG, "mScanResults.size()===" + mScanResults.size());
            if (null != onScanResultsListener)
                onScanResultsListener.onScanResultHandler(mScanResults);

        }
        //系统wifi的状态
        else if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
            int wifiState = intent.getIntExtra(
                    WifiManager.EXTRA_WIFI_STATE, 0);
            switch (wifiState) {
                case WifiManager.WIFI_STATE_ENABLED:
                    Log.d(TAG, "WiFi已启用" + TimeUtil.getNowStrTime());
                    mWifiManager.startScan();
                    break;
                case WifiManager.WIFI_STATE_DISABLED:
                    Log.d(TAG, "Wifi已关闭" + TimeUtil.getNowStrTime());
                    break;
            }
        }

    }

    public interface OnConnectionChangeListener {
        void onConnectionChangeHandler(boolean isConnection, String ssid);
    }
    public interface OnScanResultsListener {
        void onScanResultHandler(List<ScanResult> resultList);
    }

    public void setOnScanResultsListener(OnScanResultsListener onScanResultsListener) {
        this.onScanResultsListener = onScanResultsListener;
    }

    public void setOnConnectionChangeListener(OnConnectionChangeListener onConnectionChangeListener) {
        this.onConnectionChangeListener = onConnectionChangeListener;
    }
}

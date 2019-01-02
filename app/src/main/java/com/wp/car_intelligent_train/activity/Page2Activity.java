package com.wp.car_intelligent_train.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wp.car_intelligent_train.Constant;
import com.wp.car_intelligent_train.R;
import com.wp.car_intelligent_train.activity.tip.TipConnFailedActivity;
import com.wp.car_intelligent_train.adapter.P2CarSystemAdapter;
import com.wp.car_intelligent_train.application.MyApplication;
import com.wp.car_intelligent_train.base.BaseActivity;
import com.wp.car_intelligent_train.decoration.MySpaceItemDecoration;
import com.wp.car_intelligent_train.dialog.LoadingDialogUtils;
import com.wp.car_intelligent_train.entity.CarSystem;
import com.wp.car_intelligent_train.entity.UdpResult;
import com.wp.car_intelligent_train.enums.P2DrawableEnum;
import com.wp.car_intelligent_train.enums.P5DrawableEnum;
import com.wp.car_intelligent_train.holder.CommonViewHolder;
import com.wp.car_intelligent_train.receiver.NetworkChangeReceiver;
import com.wp.car_intelligent_train.udp.UdpSystem;
import com.wp.car_intelligent_train.udp.client.UdpClientFactory;
import com.wp.car_intelligent_train.udp.receiver.UdpReceiverCenter;
import com.wp.car_intelligent_train.util.TimeUtil;
import com.wp.car_intelligent_train.util.WifiUtil;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 第二页的activity
 *
 * @author wangping
 * @date 2018/6/24 17:22
 */
public class Page2Activity extends BaseActivity
        implements CommonViewHolder.onItemCommonClickListener, NetworkChangeReceiver.OnScanResultsListener,
        NetworkChangeReceiver.OnConnectionChangeListener {

    private static final String TAG = "wangping";
    private static Pattern PATTERN_WIFI = Pattern.compile("^JG-VDB-II-[^-]+-([^-]+)-[0-9a-fA-F]+$");
    private static Pattern PATTERN_SYSTEM = Pattern.compile("^([a-zA-Z]+)\\d+$");
    private RecyclerView recycler_view_system;
    private static final String WIFI_PASSWORD = "jinggekeji";
    private static final String OTHER_SSID = "JG-VDB-II-OTHER-OTHER-0001";

    private boolean connectionFlag = false;
    private MyApplication application;
    private String currentSsid;
    private Dialog dialog;

    private List<CarSystem> data = new ArrayList<>();
    private WifiManager mWifiManager;
    private NetworkChangeReceiver mReceiver;
    private ImageView iv_no_system;
    private boolean scanResultFlag = false;
    private P2CarSystemAdapter p2CarSystemAdapter;
    public ArrayList<String> firstSsidList = null;
    private int linkCount = 0;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_page2;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        dialog = LoadingDialogUtils.createLoadingDialog(Page2Activity.this, "加载中...");
        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        application = (MyApplication) this.getApplication();

        application.getMap().put("returnFlag", false);
        application.setCurrentActivityClass(this.getClass());
        UdpReceiverCenter.newInstance(application);
        UdpSystem.setApplication(application);
        registerBroadcast();
        firstSsidList = this.getIntent().getStringArrayListExtra("ssidList");
        initData(firstSsidList);
        iv_no_system  = (ImageView) findViewById(R.id.iv_no_system);
        recycler_view_system = (RecyclerView) findViewById(R.id.recycle_view_system);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler_view_system.setLayoutManager(layoutManager);

        p2CarSystemAdapter = new P2CarSystemAdapter(this, data, this);
        recycler_view_system.setAdapter(p2CarSystemAdapter);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.dimen_10_dip);
        recycler_view_system.addItemDecoration(new MySpaceItemDecoration(spacingInPixels));

        if (null == data || data.size() == 0) {
            recycler_view_system.setVisibility(View.GONE);
            iv_no_system.setVisibility(View.VISIBLE);
            Glide.with(this).load(R.drawable.p6_img_nothing).into(iv_no_system);
        } else {
            recycler_view_system.setVisibility(View.VISIBLE);
            iv_no_system.setVisibility(View.GONE);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (null != dialog) {
                    LoadingDialogUtils.closeDialog(dialog);
                    dialog = null;
                }
            }
        }, 2000L);
    }

    /**
     * 注册广播
     */
    private void registerBroadcast() {
        mReceiver = new NetworkChangeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, filter);
        mReceiver.setOnConnectionChangeListener(this);
        if (this instanceof NetworkChangeReceiver.OnScanResultsListener) {
            mReceiver.setOnScanResultsListener(this);
        }
    }

    private void initData(List<String> ssidList) {
        String str = "";
        P2DrawableEnum drawableEnum = null;
        CarSystem carSystem = null;
        data.clear();
        for (String ssid : ssidList) {
            str = getSystemName(ssid);
            drawableEnum = P2DrawableEnum.getDrawableEnumByStr(str);
            carSystem = new CarSystem();
            carSystem.setSsid(ssid);
            if (null != drawableEnum && str.equals(drawableEnum.getStr())) {
                carSystem.setDrawableId(drawableEnum.getNormalDrawableId());
            } else {
                carSystem.setDrawableId(drawableEnum.getNormalDrawableId());
            }
            if (!data.contains(carSystem)) {
                data.add(carSystem);
            }
        }
        Collections.sort(data);
    }

    /**
     * 取得汽车系统名
     *
     * @param wifiName
     * @return
     */
    private static String getSystemName(String wifiName) {
        String str = "";
        String result = "";
        if (null == wifiName || wifiName.trim().equals("")) return str;
        if (wifiName.startsWith("JG-VBS-I")) return P2DrawableEnum.P2_VBS.getStr();
        Matcher matcher = PATTERN_WIFI.matcher(wifiName);
        if (matcher.find()) {
            str = matcher.group(1);
            if (null != str && !"".equals(str)) {
                Matcher m = PATTERN_SYSTEM.matcher(str);
                if (m.find()) {
                    result = m.group(1);
                }
            }
        }
        return result;
    }

    @Override
    public void onItemClickListener(int position, View itemView) {
        dialog = LoadingDialogUtils.createLoadingDialog(this, "加载中...");
        int normalDrawableId = data.get(position).getDrawableId();
        currentSsid = data.get(position).getSsid();
        final P2DrawableEnum p2DrawableEnum = P2DrawableEnum.getEnumByNomalDrawableId(normalDrawableId);
        if (null != p2DrawableEnum) {
            try {
                final MyApplication application = (MyApplication) Page2Activity.this.getApplication();
                application.setUdpState(Constant.STATE_NOT_CONNECT);
                mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                WifiUtil wifiUtil = WifiUtil.newInstance(mWifiManager);

                if (!mWifiManager.isWifiEnabled()) {
                    connectionFlag = true;
                    wifiUtil.openWifi();
                } else if (mWifiManager.getConnectionInfo().getSSID().equals(String.format("\"%s\"", currentSsid))) {
                    String str = p2DrawableEnum.getStr();
                    String type = "";
                    if (null == str || "".equals(str)) return;
                    if (UdpClientFactory.VBS_KEY.equals(str)) {
                        type = UdpClientFactory.VBS_KEY;
                    } else {
                        type = UdpClientFactory.TBOX_KEY;
                    }
                    connUdp(type);
                } else if (!mWifiManager.getConnectionInfo().getSSID().equals(String.format("\"%s\"", currentSsid))) {
                    connectionFlag = true;
                    connWifiBySsid(wifiUtil, currentSsid);
                }

            } catch (Exception e) {
            }
        }
    }

    /**
     * 连接udp
     *
     * @param type
     */
    private void connUdp(final String type) {
        application.setType(type);
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                UdpResult udpResult = (UdpResult) application.getMap().get("udpResult");
                if (null == udpResult) udpResult = new UdpResult("search");
                List<String> vbsList = udpResult.getDataByClass(List.class);
                if (msg.what == 0 && (null == vbsList || vbsList.size() == 0)) {
                    jumpFailed();
                    return;
                } else {
                    try {
                        ArrayList<CarSystem> carSystemList = getSystemList(vbsList);
                        Intent intent = new Intent(Page2Activity.this, Page5Activity.class);
                        intent.putExtra("carSystemList", carSystemList);
                        intent.putExtra("currentSsid", currentSsid);
                        if (!application.getMapData("returnFlag", Boolean.class) && application.getCurrentActivityClass() == Page2Activity.class) {
                            Page2Activity.this.startActivity(intent);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "from vbsList to carSystemList error!", e);
                    }
                    if (null != dialog) {
                        LoadingDialogUtils.closeDialog(dialog);
                        dialog = null;
                    }
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = null;
                Intent intent = null;
                try {
                    Thread.sleep(Constant.UDP_WAIT_TIME);
                    jsonObject = UdpSystem.search(type, handler);
                    Class<?> tClass = null;
                    if (UdpClientFactory.TBOX_KEY.equals(type)) {
                        tClass = Page3Activity.class;
                        if (null == jsonObject) {
                            jumpFailed();
                            return;
                        }
                    } else if (UdpClientFactory.VBS_KEY.equals(type)) {
                        tClass = Page5Activity.class;
                    }
                    intent = new Intent(Page2Activity.this, tClass);
                } catch (Exception e) {
                    Log.e(TAG, "search error!", e);
                }

                if (UdpClientFactory.VBS_KEY.equals(type)) {
                    return;
                }

                try {
                    application.setUdpState(Constant.STATE_NOT_CONNECT);//连接未成功
                    String deviceNo = "";
                    if (null != jsonObject) {
                        deviceNo = jsonObject.getString("SN");
                        Log.d("wangping", String.format("SN:%s", jsonObject.getString("SN")));
                        JSONObject obj = UdpSystem.connect(deviceNo, type);
                        if (null != obj) {
                            String result = obj.getString("result");
                            if ("ok".equals(result)) {
                                int customId = obj.getJSONObject("data").getInt("ID");
                                application.setCustomId(customId);
                                Log.d("wangping", String.format("customId:%s", customId));
                            } else if ("already connected".equals(result)) {
                                Log.d(TAG, String.format("deviceNo:%s is already connected!", deviceNo));
                                jumpFailed("连接失败", "已有用户正在使用中");
                                return;
                            }
                            application.setUdpState(Constant.STATE_CONNECTED);//连接成功
                            if (null != UdpSystem.getThread()) {
                                UdpSystem.getThread().interrupt();
                                UdpSystem.setThread(null);
                            }
                            UdpSystem.keepConnect(application, Page2Activity.this, TipConnFailedActivity.class);

                            String info = UdpSystem.getInfo(application.getCustomId(), type);
                            intent.putExtra("info", info);
                            if (!application.getMapData("returnFlag", Boolean.class) && application.getCurrentActivityClass() == Page2Activity.class) {
                                Page2Activity.this.startActivity(intent);
                            }
                            if (null != dialog) {
                                LoadingDialogUtils.closeDialog(dialog);
                                dialog = null;
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.e("wangping", "error", e);
                }
            }
        }).start();
    }


    private void connWifiBySsid(final WifiUtil wifiUtil, final String ssid) {
        List<WifiConfiguration> configurationList = mWifiManager.getConfiguredNetworks();
        WifiConfiguration configuration = null;
        int netId = -1;
        for (WifiConfiguration config : configurationList) {
            if (config.SSID.equals(String.format("\"%s\"", ssid))) {
                configuration = config;
                configuration.preSharedKey = String.format("\"%s\"", WIFI_PASSWORD);
                netId = configuration.networkId;
                break;
            }
        }
        if (null == configuration) {
            configuration = wifiUtil.createWifiInfo(ssid, WIFI_PASSWORD, WifiUtil.WifiCipherType.WIFICIPHER_WPA);
            netId = mWifiManager.addNetwork(configuration);
        }

        Method connectMethod = connectWifiByReflectMethod(netId);
        boolean enabled = false;
        if (connectMethod == null) {
            Log.i(TAG, "connect wifi by enableNetwork method, Add by jiangping.li");
            // 通用API
            enabled = mWifiManager.enableNetwork(netId, true);
        }
        Log.d("wangping", String.format("wifi link to ssid:%s,flag:%s", ssid, enabled));

    }

    private Method connectWifiByReflectMethod(int netId) {
        Method connectMethod = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Log.i(TAG, "connectWifiByReflectMethod road 1");
            // 反射方法： connect(int, listener) , 4.2 <= phone‘s android version
            for (Method methodSub : mWifiManager.getClass()
                    .getDeclaredMethods()) {
                if ("connect".equalsIgnoreCase(methodSub.getName())) {
                    Class<?>[] types = methodSub.getParameterTypes();
                    if (types != null && types.length > 0) {
                        if ("int".equalsIgnoreCase(types[0].getName())) {
                            connectMethod = methodSub;
                        }
                    }
                }
            }
            if (connectMethod != null) {
                try {
                    connectMethod.invoke(mWifiManager, netId, null);
                } catch (Exception e) {
                    Log.i(TAG, "connectWifiByReflectMethod Android "
                            + Build.VERSION.SDK_INT + " error!");
                    return null;
                }
            }
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN) {
            // 反射方法: connect(Channel c, int networkId, ActionListener listener)
            // 暂时不处理4.1的情况 , 4.1 == phone‘s android version
            Log.i(TAG, "connectWifiByReflectMethod road 2");
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            Log.i(TAG, "connectWifiByReflectMethod road 3");
            // 反射方法：connectNetwork(int networkId) ,
            // 4.0 <= phone‘s android version < 4.1
            for (Method methodSub : mWifiManager.getClass()
                    .getDeclaredMethods()) {
                if ("connectNetwork".equalsIgnoreCase(methodSub.getName())) {
                    Class<?>[] types = methodSub.getParameterTypes();
                    if (types != null && types.length > 0) {
                        if ("int".equalsIgnoreCase(types[0].getName())) {
                            connectMethod = methodSub;
                        }
                    }
                }
            }
            if (connectMethod != null) {
                try {
                    connectMethod.invoke(mWifiManager, netId);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i(TAG, "connectWifiByReflectMethod Android "
                            + Build.VERSION.SDK_INT + " error!");
                    return null;
                }
            }
        } else {
            // < android 4.0
            return null;
        }
        return connectMethod;
    }

    @Override
    public void onItemLongClickListener(int position, View itemView) {

    }

    private void jumpFailed(String... tips) {
        Intent intent = new Intent(this, TipConnFailedActivity.class);
        if (tips.length > 0) {
            ArrayList<String> tipList = new ArrayList<String>();
            for (String tip : tips) {
                tipList.add(tip);
            }
            intent.putStringArrayListExtra("tipList", tipList);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
        if (null != dialog) {
            if (null != dialog) {
                LoadingDialogUtils.closeDialog(dialog);
                dialog = null;
            }
            dialog = null;
        }
    }

    @Override
    public void onConnectionChangeHandler(boolean isConnection, String ssid) {
        Log.d(TAG, String.format("isConnect:%s, ssid:%s, date:%s", isConnection, ssid, TimeUtil.getNowStrTime()));
        if (isConnection) {
            if (!connectionFlag) return;

            if (!ssid.equals(String.format("\"%s\"", currentSsid))) {
                if (!currentSsid.startsWith("\"JG-VDB-II") && !currentSsid.startsWith("\"JG-VBS-I") && linkCount >= 3) {
                    linkCount = 0;
                    jumpFailed();
                } else {
                    linkCount++;
                    connectionFlag = true;
                    WifiUtil wifiUtil = WifiUtil.newInstance(mWifiManager);
                    connWifiBySsid(wifiUtil, currentSsid);
                    return;
                }

            } else {
                String type = "";
                if (ssid.startsWith("\"JG-VDB-II")) {
                    type = UdpClientFactory.TBOX_KEY;
                } else if (ssid.startsWith("\"JG-VBS-I")) {
                    type = UdpClientFactory.VBS_KEY;
                }
                connUdp(type);
            }
            connectionFlag = false;
        }
    }

    public void back(View view) {
        application.getMap().put("returnFlag", true);
        this.finish();
    }

    public void refresh(View view) throws InterruptedException {
        LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // 未打开位置开关，可能导致定位失败或定位不准，提示用户或做相应处理
            Log.d(TAG, String.format("location is not opened!"));
            //弹出提示，未打开位置开关
            Intent intent = new Intent(this, TipConnFailedActivity.class);
            ArrayList<String> tipList = new ArrayList<String>();
            tipList.add("扫描WIFI列表失败");
            tipList.add("请打开位置（GPS）开关");
            intent.putStringArrayListExtra("tipList", tipList);
            startActivity(intent);
            return;
        }
        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiUtil wifiUtil = WifiUtil.newInstance(mWifiManager);
        if (!mWifiManager.isWifiEnabled()) {
            wifiUtil.openWifi();
        } else {
            mWifiManager.startScan();
            Log.d("wangping", String.format("jumpInto:%s", TimeUtil.getNowStrTime()));
        }
        scanResultFlag = true;
    }

    @Override
    public void onScanResultHandler(List<ScanResult> resultList) {
        ArrayList<String> ssidList = new ArrayList<>();

        if (null == resultList || !scanResultFlag) return;

        Log.d(TAG, "接收到" + TimeUtil.getNowStrTime());
        Log.d(TAG, "mScanResults.size()===" + resultList.size());

        for (ScanResult result : resultList) {
            if (result.SSID.startsWith("JG-VDB-II")) {
                ssidList.add(result.SSID);
            } else if (result.SSID.startsWith("JG-VBS-I")) {
                ssidList.add(result.SSID);
            }
        }
        Collections.sort(ssidList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (o1 != null && !"".equals(o1) && o2 != null && !"".equals(o2)) {
                    return o1.compareTo(o2);
                }
                return 0;
            }
        });
        scanResultFlag = false;
        firstSsidList = ssidList;
        initData(ssidList);
        dialog = LoadingDialogUtils.createLoadingDialog(Page2Activity.this, "加载中...");
        if (null == data || data.size() == 0) {
            recycler_view_system.setVisibility(View.GONE);
            iv_no_system.setVisibility(View.VISIBLE);
            Glide.with(this).load(R.drawable.p6_img_nothing).into(iv_no_system);
        } else {
            recycler_view_system.setVisibility(View.VISIBLE);
            iv_no_system.setVisibility(View.GONE);
            p2CarSystemAdapter.notifyDataSetChanged();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (null != dialog) {
                    LoadingDialogUtils.closeDialog(dialog);
                    dialog = null;
                }
            }
        }, 2000L);
    }

    /**
     * 根据返回的所有设备的udp信息列表取得对应的设备对象列表
     * @param vbsList
     * @return
     * @throws Exception
     */
    private ArrayList<CarSystem> getSystemList(List<String> vbsList) throws Exception {
        ArrayList<CarSystem> list = new ArrayList<>();
        if (null == vbsList || vbsList.size() == 0) return list;

        for (String str : vbsList) {
            CarSystem carSystem = getCarSystem(str);
            if (null != carSystem && !list.contains(carSystem)) {
                list.add(carSystem);
            }
        }
        return list;
    }

    /**
     * 根据返回的字符串取得对应的设备对象
     * @param str
     * @return
     * @throws Exception
     */
    private CarSystem getCarSystem(String str) throws Exception {
        if (null == str || "".equals(str)) return null;
        CarSystem carSystem = null;
        JSONObject obj = new JSONObject(str);
        if (null != obj) {
            String result = obj.getString("result");
            if ("ok".equals(result)) {
                JSONObject data = obj.getJSONObject("data");
                if (null != data) {
                    carSystem = new CarSystem();
                    carSystem.setState(data.getString("state"));
                    carSystem.setSsid(data.getString("SN"));
                    String model = data.getString("model");
                    P5DrawableEnum p5DrawableEnum = P5DrawableEnum.getDrawableEnumByStr(model);
                    if (null != p5DrawableEnum) {
                        carSystem.setDrawableId(p5DrawableEnum.getNormalDrawableId());
                    }

                }
            }
        }
        return carSystem;
    }
}

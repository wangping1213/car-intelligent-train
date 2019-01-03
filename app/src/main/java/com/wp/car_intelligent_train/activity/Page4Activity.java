package com.wp.car_intelligent_train.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wp.car_intelligent_train.Constant;
import com.wp.car_intelligent_train.R;
import com.wp.car_intelligent_train.activity.tip.TipResetActivity;
import com.wp.car_intelligent_train.adapter.P4CarPinPartAdapter;
import com.wp.car_intelligent_train.application.MyApplication;
import com.wp.car_intelligent_train.base.BaseActivity;
import com.wp.car_intelligent_train.decoration.MySpaceItemDecoration;
import com.wp.car_intelligent_train.dialog.LoadingDialogUtils;
import com.wp.car_intelligent_train.entity.CarPart;
import com.wp.car_intelligent_train.entity.CarPartPin;
import com.wp.car_intelligent_train.holder.CommonViewHolder;
import com.wp.car_intelligent_train.receiver.NetworkChangeReceiver;
import com.wp.car_intelligent_train.udp.UdpSystem;
import com.wp.car_intelligent_train.util.TimeUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 第三页的activity
 *
 * @author wangping
 * @date 2018/6/24 17:22
 */
public class Page4Activity extends BaseActivity implements CommonViewHolder.onItemCommonClickListener {

    private static final String TAG = "wangping";
    private RecyclerView recycler_view_system;
    private TextView app_title_name;
    private MyApplication application;
    private boolean initFlag = false;
    private P4CarPinPartAdapter adapter;
    private NetworkChangeReceiver mReceiver;
    public static Map<Integer, Integer> checkboxMap = new HashMap<>();
    private Handler myHandler;
    private long time;

    private List<CarPartPin> data = new ArrayList<>();


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_page4;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        final long page4Time = System.currentTimeMillis();
        Log.d(TAG, String.format("page4-initView-start:%s", TimeUtil.getNowStrTime()));
        final long start = System.currentTimeMillis();
        application = (MyApplication) this.getApplication();
        Glide.with(this).load(R.drawable.p4_icon_menu).into((ImageView) findViewById(R.id.iv_back));
        recycler_view_system = (RecyclerView) findViewById(R.id.recycle_view_system);
        application.getMap().put("returnFlag", false);
        application.setCurrentActivityClass(this.getClass());
        app_title_name = (TextView) findViewById(R.id.app_title_name);

        initData2();
        setAdapter(data);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler_view_system.setLayoutManager(layoutManager);

        final Dialog dialog = LoadingDialogUtils.createLoadingDialog(this, "加载中....");
        if (null != dialog) application.getMap().put("dialog", dialog);
        myHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        Bundle bundle = msg.getData();
                        long startTime = page4Time;
                        int pos = -1;
                        if (null != bundle) {
                            startTime = bundle.getLong("page4Time");
                            pos = bundle.getInt("position");
                        }
                        refreshData((Dialog) application.getMap().get("dialog"), startTime, pos);
                        break;
                }
            }
        };
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sendMsg(null);
            }
        }, 1000L);
    }

    /**
     * 发送消息
     * @param bundle
     */
    public void sendMsg(Bundle bundle) {
        Message msg = myHandler.obtainMessage();
        msg.what = 1;
        if (null != bundle) msg.setData(bundle);
        msg.sendToTarget();
    }

    private void refreshData(final Dialog dialog, final long page4Time, final int pos) {
        Log.d(TAG, String.format("stateTime:%s, page4Time:%s", application.getStateTime(), page4Time));
        new Thread(new Runnable() {
            @Override
            public void run() {
//                for (int i=0; i<20; i++) {
//                    if (application.getStateTime() >= page4Time) {
                        checkboxMap.clear();
                        checkboxMap.putAll(application.getPointMap());
//                        if (pos != -1) {
//                            adapter.notifyItemChanged(pos, 1);
//                        } else {
//                            for (int j = 0; j < data.size(); j++) {
//                                adapter.notifyItemChanged(j, 1);
//                            }
//                        }
//                        break;
//                    } else {
//                        try {
//                            Thread.sleep(Constant.UDP_WAIT_TIME);
//                        } catch (InterruptedException e) {
//                            Log.e(TAG, "sleep error!", e);
//                        }
//                    }
//                }

                if (null != dialog) LoadingDialogUtils.closeDialog(dialog);
            }
        }).start();

    }


    private void initCheckBoxMap(List<CarPartPin> partPinList) {

    }

    private void setAdapter(final List<CarPartPin> partPinList) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                UdpSystem.getNowState();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new P4CarPinPartAdapter(Page4Activity.this, partPinList, Page4Activity.this);
                        recycler_view_system.setAdapter(adapter);
                        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.dimen_10_dip);
                        recycler_view_system.addItemDecoration(new MySpaceItemDecoration(spacingInPixels));
                    }
                });
            }
        }).start();

    }

    private void initData2() {
        CarPart carPart = (CarPart) this.getIntent().getSerializableExtra("carPart");

        app_title_name.setText(carPart.getName());
        if (null != carPart) {
            data = carPart.getPinList();
        }
    }


    @Override
    public void onItemClickListener(int position, View itemView) {

    }

    @Override
    public void onItemLongClickListener(int position, View itemView) {

    }

    public void back(View view) {
        application.getMap().put("returnFlag", true);
        this.finish();
    }

    public void reset(View view) {
        if (System.currentTimeMillis() - time > 2000) {
            //获得当前的时间
            time = System.currentTimeMillis();

            Intent intent = new Intent(this, TipResetActivity.class);
            intent.putExtra("page", "4");

            this.startActivity(intent);
            reloadContent();
        }
    }

    public void reloadContent() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=0; i<100; i++) {
                    try {
                        Thread.sleep(100L);
                    } catch (Exception e) {
                    }
                    if (application.getReloadFlag() == 1) {
                        application.setReloadFlag(0);
                        Intent intent = new Intent(Page4Activity.this, Page4Activity.class);
                        CarPart carPart = (CarPart) Page4Activity.this.getIntent().getSerializableExtra("carPart");
                        intent.putExtra("carPart", carPart);
                        Page4Activity.this.startActivity(intent);
                        Page4Activity.this.finish();
                        break;
                    }
                }
            }
        }).start();
    }

}

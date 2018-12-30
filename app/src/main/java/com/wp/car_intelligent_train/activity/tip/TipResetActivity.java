package com.wp.car_intelligent_train.activity.tip;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.wp.car_intelligent_train.R;
import com.wp.car_intelligent_train.application.MyApplication;
import com.wp.car_intelligent_train.base.BaseActivity;
import com.wp.car_intelligent_train.udp.UdpSystem;

import org.json.JSONObject;

public class TipResetActivity extends BaseActivity {

    private static final String TAG = "wangping";
    private MyApplication application;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.layout_tip_reset;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        application = (MyApplication) this.getApplication();
        Log.d(TAG, String.format("super.activity:%s", super.getClass()));
    }


    public void closeTip(View view) {
        this.finish();
    }

    public void reset(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                TipResetActivity.this.finish();

                final String pageStr = TipResetActivity.this.getIntent().getStringExtra("page");
                int customId = application.getCustomId();
                try {
                    application.getPointMap().clear();
                    JSONObject retObj = UdpSystem.resetPoint(customId, application.getType());
                    if (null != retObj) {
                        String message = retObj.getString("result");
                        if (null != message && message.equals("ok")) {
                            Intent intent = new Intent(TipResetActivity.this, TipTextActivity.class);
                            TipResetActivity.this.startActivity(intent);
                            Thread.sleep(2000L);
                            if ("4".equals(pageStr)) {
                                application.setReloadFlag(1);
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, String.format("reset error!, customId:%s", customId), e);
                }
            }
        }).start();
    }


}

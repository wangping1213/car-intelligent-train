package com.wp.car_intelligent_train.activity.tip;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.wp.car_intelligent_train.R;
import com.wp.car_intelligent_train.base.BaseActivity;

public class TipTextActivity extends BaseActivity {


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.layout_tip_text;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                TipTextActivity.this.finish();
            }
        };

        new Handler().postDelayed(runnable, 3000L);
    }


    public void closeTip(View view) {
        this.finish();
    }
}

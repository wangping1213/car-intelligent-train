package com.wp.car_intelligent_train.activity.tip;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wp.car_intelligent_train.R;
import com.wp.car_intelligent_train.base.BaseActivity;

import java.util.List;

public class TipConnFailedActivity extends BaseActivity {


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.layout_tip_conn_failed;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                TipConnFailedActivity.this.finish();
//            }
//        };
//
//        new Handler().postDelayed(runnable, 2000L);
        List<String> tipList = this.getIntent().getStringArrayListExtra("tipList");
        if (tipList != null) {
            TextView tv_tip1 = (TextView) findViewById(R.id.tv_tip1);
            TextView tv_tip2 = (TextView) findViewById(R.id.tv_tip2);
            tv_tip1.setText(tipList.get(0));
            tv_tip2.setText(tipList.get(1));
        }
    }


    public void closeTip(View view) {
        this.finish();
    }
}

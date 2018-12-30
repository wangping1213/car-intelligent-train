package com.wp.car_intelligent_train.activity.tip;

import android.os.Bundle;
import android.view.View;

import com.wp.car_intelligent_train.R;
import com.wp.car_intelligent_train.base.BaseActivity;

/**
 * 退出应用提示
 * @author wangping
 * @date 2018/7/6 0:08
 */
public class TipExitActivity extends BaseActivity {


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.layout_tip_exit;
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
    }


    public void exitApp(View view) {
        exit();
    }

    private long time = 0;

    //退出方法
    private void exit() {
//        //如果在两秒大于2秒
//        if (System.currentTimeMillis() - time > 2000) {
//            //获得当前的时间
//            time = System.currentTimeMillis();
//            showToast(String.format("再按一次退出【%s】", R.string.app_name));
//        } else {
//            //点击在两秒以内
//            application.removeAllActivity();//执行移除所以Activity方法
//        }
        application.removeAllActivity();//执行移除所以Activity方法
    }
}

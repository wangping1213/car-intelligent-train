package com.wp.car_intelligent_train.listener;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wp.car_intelligent_train.R;
import com.wp.car_intelligent_train.activity.Page4Activity;
import com.wp.car_intelligent_train.activity.tip.TipConnFailedActivity;
import com.wp.car_intelligent_train.application.MyApplication;
import com.wp.car_intelligent_train.dialog.LoadingDialogUtils;
import com.wp.car_intelligent_train.entity.CarPartPin;
import com.wp.car_intelligent_train.holder.CommonViewHolder;
import com.wp.car_intelligent_train.udp.UdpSystem;
import com.wp.car_intelligent_train.util.TimeUtil;
import com.wp.car_intelligent_train.viewTarget.MyViewTarget;

import org.json.JSONObject;

import java.util.List;

/**
 * 勾选事件监听器
 * @author wangping
 * @version 1.0
 * @since 2018/5/19 20:13
 */
public class OnMyClickListener implements View.OnClickListener {

    private static final String TAG = "wangping";
    private TextView textView;
    private CheckBox checkBox;
    private Context myContext;
    private CommonViewHolder holder;
    private List<CheckBox> cbList;
    private List<TextView> tvList;
    private CarPartPin data;
    private MyApplication application;
    private Dialog dialog;

    public OnMyClickListener(CommonViewHolder holder, View view, Context myContext,
                             List<CheckBox> cbList, List<TextView> tvList, CarPartPin data,
                             MyApplication application) {
        if (view instanceof AppCompatTextView) {
            this.textView = (TextView) view;
        } else {
            this.checkBox = (CheckBox) view;
        }

        this.myContext = myContext;
        this.holder = holder;
        this.cbList = cbList;
        this.tvList = tvList;
        this.data = data;
        this.application = application;
    }

    @Override
    public void onClick(View view) {
        boolean isCheck = false;
        TextView tv = null;
        CheckBox ck = null;
        if (view instanceof AppCompatTextView) {
            tv = (TextView) view;
            ck = checkBox;
            isCheck = !ck.isChecked();
        } else {
            ck = (CheckBox) view;
            tv = textView;
            isCheck = ck.isChecked();
        }
        if (isCheck) {
            dialog = LoadingDialogUtils.createLoadingDialog(myContext, "设置中...");
            setCheckBox(ck, tv, ck.isChecked());
        }
    }

    private void setCheckBox(int ckId, int tvId, boolean isChecked) {
        CheckBox cb = holder.getView(ckId);
        TextView tv = holder.getView(tvId);
        setCheckBox(cb, tv, isChecked);
    }

    private void setCheckBox(CheckBox tmpCb, final TextView tmpTv, boolean isChecked) {
        Log.d("wangping", String.format("text:%s, cb:%s", tmpTv.getText(), tmpCb.isChecked()));
        if (null != cbList && null != tvList) {
            for (int i=0; i<cbList.size(); i++) {
                CheckBox cb = cbList.get(i);
                TextView tv = tvList.get(i);
                if (cb.getId() == tmpCb.getId()) {
                    Glide.with(myContext).load(R.drawable.p4_icon_select).into(new MyViewTarget(cb));
                    tv.setTextColor(myContext.getResources().getColor(R.color.color_circle_2));
                    cb.setChecked(true);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                            int aNum = data.getAnum();
                            int customId = application.getCustomId();
                            int type = -1;
                            if ("连接".equals(tmpTv.getText())) {
                                type = 0;
                            } else if ("断开".equals(tmpTv.getText())) {
                                type = 1;
                            } else if ("搭铁".equals(tmpTv.getText())) {
                                type = 2;
                            }
                            JSONObject obj = null;
//                            application.getPointMap().clear();
//                            application.getPointMap().put(aNum, type);
                            Log.d("wangping", String.format("pointMap, aNum:%s, type:%s", aNum, type));
                            try {
//                                Page4Activity.checkboxMap.put(aNum, type);
                                Log.d("wangping", String.format("setPoint start:%s", TimeUtil.getNowStrTime()));
                                obj = UdpSystem.setPoint(customId, aNum, type, application.getType());
                                if (null == obj) {
                                    LoadingDialogUtils.closeDialog(dialog);
                                    Intent intent = new Intent(myContext, TipConnFailedActivity.class);
                                    myContext.startActivity(intent);
                                    return;
                                }
                                Log.d("wangping", String.format("setPoint end:%s", TimeUtil.getNowStrTime()));

                                final long page4Time = System.currentTimeMillis();
                                if (null != dialog) application.getMap().put("dialog", dialog);
                                Page4Activity page4Activity = (Page4Activity) myContext;
                                Bundle bundle = new Bundle();
                                bundle.putInt("position", holder.getAdapterPosition());
                                bundle.putLong("page4Time", page4Time);
                                page4Activity.sendMsg(bundle);

                            } catch (Exception e) {
                                Log.e("wangping", String.format("aNum:%s, type:%s", aNum, type), e);
                            }
                        }
                    }).start();
                } else {
                    Glide.with(myContext).load(R.drawable.p4_icon_normal).into(new MyViewTarget(cb));
                    tv.setTextColor(myContext.getResources().getColor(R.color.color_btn_cancel));
                    cb.setChecked(false);
                }
            }
        }
    }

}

package com.wp.car_intelligent_train.activity;

import android.content.Intent;
import android.os.Bundle;
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
import com.wp.car_intelligent_train.adapter.P3CarPartAdapter;
import com.wp.car_intelligent_train.application.MyApplication;
import com.wp.car_intelligent_train.base.BaseActivity;
import com.wp.car_intelligent_train.decoration.MySpaceItemDecoration;
import com.wp.car_intelligent_train.entity.CarPart;
import com.wp.car_intelligent_train.entity.CarPartPin;
import com.wp.car_intelligent_train.holder.CommonViewHolder;
import com.wp.car_intelligent_train.udp.UdpSystem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 第三页的activity
 *
 * @author wangping
 * @date 2018/6/24 17:22
 */
public class Page3Activity extends BaseActivity implements CommonViewHolder.onItemCommonClickListener {

    private static final String TAG = "wangping";
    private RecyclerView recycler_view_system;
    private TextView app_title_name;
    private MyApplication application;


    private List<CarPart> data = new ArrayList<>();


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_page3;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        application = (MyApplication) this.getApplication();
//        Glide.with(this).load(R.drawable.bg1).into((ImageView) findViewById(R.id.iv_bg));
        recycler_view_system = (RecyclerView) findViewById(R.id.recycle_view_system);

        app_title_name = (TextView) findViewById(R.id.app_title_name);

        initData();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler_view_system.setLayoutManager(layoutManager);


        recycler_view_system.setAdapter(new P3CarPartAdapter(this, data, this));
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.dimen_10_dip);
        recycler_view_system.addItemDecoration(new MySpaceItemDecoration(spacingInPixels));

    }


    private void initData() {
        String info = this.getIntent().getStringExtra("info");
        JSONObject infoObj = null;
        try {
            if (null != info && !info.trim().equals("")) {
                infoObj = new JSONObject(info);
                String title = infoObj.getString("title");
                app_title_name.setText(title);
//                app_title_name.setText("爱上的咖啡哈迪斯的离开后覅椰蓉才能日以上若人我拉出认识到了");
                JSONArray partArr = infoObj.getJSONArray("part");
                if (null != partArr) {
                    for (int i=0; i<partArr.length(); i++) {
                        CarPart carPart = getCarPart(partArr.getJSONObject(i));
                        if (null != carPart) data.add(carPart);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "initData error!", e);
        }

    }

    /**
     * 将json转为汽车元器件对象
     * @param partObj
     * @return
     */
    private CarPart getCarPart(JSONObject partObj) throws Exception {
        CarPart carPart = null;
        if (null == partObj) return carPart;
        carPart = new CarPart();
        carPart.setName(partObj.getString("name"));
        CarPartPin carPartPin = null;
        JSONArray pinArr = partObj.getJSONArray("pin");
        List<CarPartPin> pinList = new ArrayList<>();
        if (null != pinArr && pinArr.length() > 0) {
            for (int i=0; i<pinArr.length(); i++) {
                carPartPin = new CarPartPin();
                carPartPin.setName(pinArr.getJSONObject(i).getString("name"));
                carPartPin.setPid(pinArr.getJSONObject(i).getString("pID"));
                carPartPin.setCid(pinArr.getJSONObject(i).getString("cID"));
                carPartPin.setAnum(pinArr.getJSONObject(i).getInt("aNum"));
                JSONArray typeObjArr = pinArr.getJSONObject(i).getJSONArray("aType");
                int[] typeArr = new int[typeObjArr.length()];
                for (int j=0; j<typeObjArr.length(); j++) {
                    typeArr[j] = typeObjArr.getInt(j);
                }
                carPartPin.setAtype(typeArr);
                pinList.add(carPartPin);
            }
            carPart.setPinList(pinList);
        }
        return carPart;
    }


    @Override
    public void onItemClickListener(int position, View itemView) {
//        ImageView iv_p3_system = (ImageView) itemView.findViewById(R.id.iv_p3_system);
//        Glide.with(this).load(R.drawable.p3_list_bg_select).into(iv_p3_system);

        CarPart carPart = data.get(position);
        Intent intent = new Intent(this, Page4Activity.class);
        intent.putExtra("carPart", carPart);
        this.startActivity(intent);
    }

    @Override
    public void onItemLongClickListener(int position, View itemView) {

    }

    public void back(View view) {
        this.finish();
//        super.onCreate(null);
    }

    public void reset(View view) {
        Intent intent = new Intent(this, TipResetActivity.class);
        this.startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //处理
        final int customId = application.getCustomId();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    UdpSystem.disconnect(customId, application.getType());
                    application.setUdpState(Constant.STATE_NOT_CONNECT);
                    Log.d(TAG, "page3 onDestroy!");
                } catch (Exception e) {
                    Log.e(TAG, String.format("Page3 onDestroy, disconnect customId:%", customId));
                }
            }
        }).start();

    }
}

package com.wp.car_intelligent_train.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wp.car_intelligent_train.R;
import com.wp.car_intelligent_train.entity.CarSystem;
import com.wp.car_intelligent_train.enums.P5DrawableEnum;
import com.wp.car_intelligent_train.holder.CommonViewHolder;

import java.util.List;

/**
 * 汽车系统adapter(第三页，vbs页面)
 *
 * @author wangping
 * @version 1.0
 * @since 2018/5/17 16:23
 */
public class P5CarSystemAdapter extends CommonRecycleAdapter<CarSystem> {

    private CommonViewHolder.onItemCommonClickListener commonClickListener;

    private Context myContext;

    public P5CarSystemAdapter(Context context, List<CarSystem> dataList) {
        super(context, dataList, R.layout.item_p5_system);
        myContext = context;
    }

    public P5CarSystemAdapter(Context context, List<CarSystem> dataList, CommonViewHolder.onItemCommonClickListener commonClickListener) {
        super(context, dataList, R.layout.item_p5_system);
        myContext = context;
        this.commonClickListener = commonClickListener;
    }


    @Override
    void bindData(CommonViewHolder holder, CarSystem data) {
        ImageView iv_p2_system = holder.getView(R.id.iv_p2_system);
        iv_p2_system.setBackgroundResource(data.getDrawableId());
        holder
                .setText(R.id.tv_wifi_name, data.getSsid())
                .setCommonClickListener(commonClickListener);
    }

    @Override
    public void onBindViewHolder(final CommonViewHolder holder, int position, List payloads) {
        CarSystem data = dataList.get(position);
        //payloads是从notifyItemChanged(int, Object)中，或从notifyItemRangeChanged(int, int, Object)中传进来的Object集合
        //如果payloads不为空并且viewHolder已经绑定了旧数据了，那么adapter会使用payloads参数进行布局刷新
        //如果payloads为空，adapter就会重新绑定数据，也就是刷新整个item


//        if (payloads.isEmpty()) {//为空，即不是调用notifyItemChanged(position,payloads)后执行的，也即在初始化时执行的
            ImageView iv_p2_system = holder.getView(R.id.iv_p2_system);
            iv_p2_system.setBackgroundResource(data.getDrawableId());
            holder
                    .setText(R.id.tv_wifi_name, data.getSsid())
                    .setText(R.id.tv_other_wifi_name, data.getSsid())
                    .setCommonClickListener(commonClickListener);

            if (data.getDrawableId() == P5DrawableEnum.P5_OTHER.getNormalDrawableId()) {
                holder
                        .setViewVisibility(R.id.tv_wifi_name, View.GONE)
                        .setViewVisibility(R.id.tv_other_wifi_name, View.VISIBLE)
                        ;
            } else {
                holder
                        .setViewVisibility(R.id.tv_wifi_name, View.VISIBLE)
                        .setViewVisibility(R.id.tv_other_wifi_name, View.GONE)
                ;
            }
//        } else if (payloads.size() > 0) {
//            //不为空，即调用notifyItemChanged(position,payloads)后执行的，可以在这里获取payloads中的数据进行局部刷新
//            ImageView iv_p2_system = holder.getView(R.id.iv_p2_system);
//            iv_p2_system.setBackgroundResource(data.getDrawableId());
//            holder
//                    .setText(R.id.tv_wifi_name, data.getSsid())
//                    .setText(R.id.tv_other_wifi_name, data.getSsid())
//                    .setCommonClickListener(commonClickListener);
//        }

    }


}

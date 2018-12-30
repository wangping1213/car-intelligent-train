package com.wp.car_intelligent_train.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.wp.car_intelligent_train.R;
import com.wp.car_intelligent_train.entity.CarSystem;
import com.wp.car_intelligent_train.holder.CommonViewHolder;

import java.util.List;

/**
 * 汽车系统adapter
 *
 * @author wangping
 * @version 1.0
 * @since 2018/5/17 16:23
 */
public class P2CarSystemAdapter extends CommonRecycleAdapter<CarSystem> {

    private CommonViewHolder.onItemCommonClickListener commonClickListener;

    private Context myContext;

    public static final int NOTIFY_TV = 10086;
    public static final int NOTIFY_ET = 10087;
    public static final int NOTIFY_IV = 10088;

    public P2CarSystemAdapter(Context context, List<CarSystem> dataList) {
        super(context, dataList, R.layout.item_p2_system);
        myContext = context;
    }

    public P2CarSystemAdapter(Context context, List<CarSystem> dataList, CommonViewHolder.onItemCommonClickListener commonClickListener) {
        super(context, dataList, R.layout.item_p2_system);
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

        if (payloads.isEmpty()) {//为空，即不是调用notifyItemChanged(position,payloads)后执行的，也即在初始化时执行的
            ImageView iv_p2_system = holder.getView(R.id.iv_p2_system);
            iv_p2_system.setBackgroundResource(data.getDrawableId());
            holder
                    .setText(R.id.tv_wifi_name, data.getSsid())
                    .setCommonClickListener(commonClickListener);
        } else if (payloads.size() > 0) {
            //不为空，即调用notifyItemChanged(position,payloads)后执行的，可以在这里获取payloads中的数据进行局部刷新
            ImageView iv_p2_system = holder.getView(R.id.iv_p2_system);
            iv_p2_system.setBackgroundResource(data.getDrawableId());
            holder
                    .setText(R.id.tv_wifi_name, data.getSsid())
                    .setCommonClickListener(commonClickListener);
        }

    }


}

package com.wp.car_intelligent_train.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wp.car_intelligent_train.R;
import com.wp.car_intelligent_train.enums.P1CarImageEnum;
import com.wp.car_intelligent_train.holder.CommonViewHolder;

import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * 设备类型的adapter
 *
 * @author wangping
 * @version 1.0
 * @since 2018/5/17 16:23
 */
public class CarImageAdapter extends CommonRecycleAdapter<Integer> {

    private CommonViewHolder.onItemCommonClickListener commonClickListener;

    private Context myContext;
    private int size;

    public CarImageAdapter(Context context, List<Integer> dataList) {
        super(context, dataList, R.layout.item_p1_img);
        size = dataList.size();
        myContext = context;
    }

    public CarImageAdapter(Context context, List<Integer> dataList, CommonViewHolder.onItemCommonClickListener commonClickListener) {
        super(context, dataList, R.layout.item_p1_img);
        myContext = context;
        size = dataList.size();
        this.commonClickListener = commonClickListener;
    }


    @Override
    void bindData(CommonViewHolder holder, Integer data) {
        ImageView iv_p1_car = holder.getView(R.id.iv_p1_car);

        Glide.with(myContext)
                .load(data)
//                .bitmapTransform(new BlurTransformation(myContext, 3, 2))
                .into(iv_p1_car);
    }

    @Override
    public void onBindViewHolder(CommonViewHolder holder, int position) {
//        bindData(holder, dataList.get(position%size));

        ImageView iv_p1_car = holder.getView(R.id.iv_p1_car);
        TextView tv_car_name = holder.getView(R.id.tv_car_name);
//        if (position == 0 || position == 2) {
//            Glide.with(myContext)
//                    .load(dataList.get(position % size))
//                    .bitmapTransform(new BlurTransformation(myContext, 3, 2))
//                    .into(iv_p1_car);
//        } else if (position == 1) {

        int drawableId = dataList.get(position % size);
        P1CarImageEnum p1CarImageEnum = P1CarImageEnum.getEnumByDrawableId(drawableId);
        String str = "其 他";
        Glide.with(myContext)
                .load(drawableId)
                .into(iv_p1_car);
        if (null != p1CarImageEnum) {
            str = p1CarImageEnum.getName();
        }
        tv_car_name.setText(str);
//        }
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }
}

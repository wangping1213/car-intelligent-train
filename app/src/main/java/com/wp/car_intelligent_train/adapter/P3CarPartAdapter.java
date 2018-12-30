package com.wp.car_intelligent_train.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.wp.car_intelligent_train.R;
import com.wp.car_intelligent_train.entity.CarPart;
import com.wp.car_intelligent_train.holder.CommonViewHolder;

import java.util.List;

/**
 * 汽车元器件adapter
 *
 * @author wangping
 * @version 1.0
 * @since 2018/5/17 16:23
 */
public class P3CarPartAdapter extends CommonRecycleAdapter<CarPart> {

    private CommonViewHolder.onItemCommonClickListener commonClickListener;

    private Context myContext;

    public P3CarPartAdapter(Context context, List<CarPart> dataList) {
        super(context, dataList, R.layout.item_p3_system);
        myContext = context;
    }

    public P3CarPartAdapter(Context context, List<CarPart> dataList, CommonViewHolder.onItemCommonClickListener commonClickListener) {
        super(context, dataList, R.layout.item_p3_system);
        myContext = context;
        this.commonClickListener = commonClickListener;
    }


    @Override
    void bindData(CommonViewHolder holder, CarPart data) {
        ImageView iv_p3_system = holder.getView(R.id.iv_p3_system);
        iv_p3_system.setBackgroundResource(R.drawable.p3_system_sytle);
//        Glide.with(myContext).load(R.drawable.p3_list_bg_normal).into(iv_p3_system);
        holder
                .setText(R.id.app_title_name, data.getName())
//                .setText(R.id.app_title_name, "表示会跟随ParentView的状态来变化，其实没加也不会有问题，因为默认状态本来就是能传递的")
                .setCommonClickListener(commonClickListener);
    }

}

package com.wp.car_intelligent_train.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wp.car_intelligent_train.holder.CommonViewHolder;

import java.util.List;

/**
 * 通用recycle adapter
 * @author wangping
 * @version 1.0
 * @since 2018/5/17 14:54
 */
public abstract class CommonRecycleAdapter<T> extends RecyclerView.Adapter<CommonViewHolder> {

    protected LayoutInflater layoutInflater;

    protected List<T> dataList;

    protected int layoutId;

    public CommonRecycleAdapter() {
    }

    public CommonRecycleAdapter(Context context, List<T> dataList, int layoutId) {
        this.layoutInflater = LayoutInflater.from(context);
        this.dataList = dataList;
        this.layoutId = layoutId;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(layoutId, parent, false);
        return new CommonViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CommonViewHolder holder, int position) {
        bindData(holder, dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    abstract void bindData(CommonViewHolder holder, T data);

}
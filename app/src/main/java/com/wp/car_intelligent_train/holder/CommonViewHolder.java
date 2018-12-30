package com.wp.car_intelligent_train.holder;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONException;

/**
 * 通用holder
 * @author wangping
 * @version 1.0
 * @since 2018/5/17 14:53
 */
public class CommonViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {

    // SparseArray 比 HashMap 更省内存，在某些条件下性能更好，只能存储 key 为 int 类型的数据，
    // 用来存放 View 以减少 findViewById 的次数
    private SparseArray<View> viewSparseArray;

    private onItemCommonClickListener commonClickListener;

    public CommonViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        viewSparseArray = new SparseArray<>();
    }

    /**
     * 根据 ID 来获取 View
     * @param viewId viewID
     * @param <T>    泛型
     * @return 将结果强转为 View 或 View 的子类型
     */
    public <T extends View> T getView(int viewId) {
        // 先从缓存中找，找打的话则直接返回
        // 如果找不到则 findViewById ，再把结果存入缓存中
        View view = viewSparseArray.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            viewSparseArray.put(viewId, view);
        }
        return (T) view;
    }

    public CommonViewHolder setText(int viewId, CharSequence text) {
        AppCompatTextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    public CommonViewHolder setViewVisibility(int viewId, int visibility) {
        getView(viewId).setVisibility(visibility);
        return this;
    }

    public CommonViewHolder setImageResource(int viewId, int resourceId) {
        ImageView imageView = getView(viewId);
        imageView.setImageResource(resourceId);
        return this;
    }

    public interface onItemCommonClickListener {

        void onItemClickListener(int position, View itemView);

        void onItemLongClickListener(int position, View itemView) throws JSONException;

    }

    public void setCommonClickListener(onItemCommonClickListener commonClickListener) {
        this.commonClickListener = commonClickListener;
    }

    @Override
    public void onClick(View v) {
        if (commonClickListener != null) {
            commonClickListener.onItemClickListener(getAdapterPosition(), v);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (commonClickListener != null) {
            try {
                commonClickListener.onItemLongClickListener(getAdapterPosition(), v);
            } catch (JSONException e) {
                Log.e("wangping", "onItemLongClickListener error!", e);
            }
        }
        return false;
    }
}
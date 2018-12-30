package com.wp.car_intelligent_train.viewTarget;

import android.view.View;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;

/**
 * 我的视图对象
 * @author wangping
 * @version 1.0
 * @since 2018/5/19 22:26
 */
public class MyViewTarget extends ViewTarget<View, GlideDrawable> {

    public MyViewTarget(View view) {
        super(view);
    }

    @Override
    public void onResourceReady(GlideDrawable glideDrawable, GlideAnimation<? super GlideDrawable> glideAnimation) {
        this.view.setBackground(glideDrawable);
    }
}

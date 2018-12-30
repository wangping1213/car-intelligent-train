package com.wp.car_intelligent_train.decoration;

import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 间隔装饰
 *
 * @author wangping
 * @version 1.0
 * @since 2018/5/17 14:34
 */
public class CircleSpaceItemDecoration extends RecyclerView.ItemDecoration {

    /**
     * 间隔宽度（单位为dp）
     */
    private int space;

    /**
     * 是否需要包含边框
     */
    private boolean includeEdge = false;

    public CircleSpaceItemDecoration(int space) {
        this.space = space;
    }

    public CircleSpaceItemDecoration(int space, boolean includeEdge) {
        this.space = space;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int currentPos = parent.getChildLayoutPosition(view) + 1;
        int totalCount = state.getItemCount();
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager.getClass() == LinearLayoutManager.class) {
            int orientation = ((LinearLayoutManager) layoutManager)
                    .getOrientation();
            if (orientation == LinearLayoutManager.HORIZONTAL) {
                outRect.top = 0;
                outRect.bottom = 0;
                outRect.left = space;

                if (currentPos == totalCount) outRect.right = space;
            } else if (orientation == LinearLayoutManager.VERTICAL) {
                outRect.left = space;
                outRect.right = space;
                outRect.top = 0;

                if (currentPos == totalCount) outRect.bottom = 0;
            }
        }
    }

}

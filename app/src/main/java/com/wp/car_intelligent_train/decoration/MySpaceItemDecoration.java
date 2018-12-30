package com.wp.car_intelligent_train.decoration;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
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
public class MySpaceItemDecoration extends RecyclerView.ItemDecoration {

    /**
     * 间隔宽度（单位为dp）
     */
    private int space;

    /**
     * 是否需要包含边框
     */
    private boolean includeEdge = false;

    public MySpaceItemDecoration(int space) {
        this.space = space;
    }

    public MySpaceItemDecoration(int space, boolean includeEdge) {
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
                outRect.top = space;
                outRect.bottom = space;
                outRect.left = space;

                if (currentPos == totalCount) outRect.right = space;
            } else if (orientation == LinearLayoutManager.VERTICAL) {
                outRect.left = space;
                outRect.right = space;
                outRect.top = space;

                if (currentPos == totalCount) outRect.bottom = space;
            }
        } else if (layoutManager.getClass() == GridLayoutManager.class) {
            int orientation = ((GridLayoutManager) layoutManager)
                    .getOrientation();
            int spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
            if (includeEdge) {
                int curCol = (currentPos % spanCount) == 0 ? spanCount : currentPos % spanCount;

                /*
                1) 所有行，设置bottom为space
                2）第一行，设置top为space
                3）所有left：space - (curCol - 1) * space / spanCount;
                3）所有right：curCol * space / spanCount;
                 */
                if (currentPos <= spanCount) {
                    outRect.top = space;
                }
                outRect.bottom = space;
                outRect.left = space - (curCol - 1) * space / spanCount;
                outRect.right = curCol * space / spanCount;
            }

        }
    }

}

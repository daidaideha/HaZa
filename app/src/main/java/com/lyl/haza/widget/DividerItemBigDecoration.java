package com.lyl.haza.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lyl.haza.utils.ScreenUtil;

/**
 * Create by Mr.Pro.Lin on 2016/11/28
 * </p>
 *
 */
public class DividerItemBigDecoration extends RecyclerView.ItemDecoration {

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0, 0, 0, ScreenUtil.dip2px(10));

    }
}

package com.example.lijia.testrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lijia on 16/9/27.
 */

public class CustomLayoutManager extends RecyclerView.LayoutManager {

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        detachAndScrapAttachedViews(recycler);//分离所有item——view

        int offsetX = 0;
        int offsetY = 0;

        for (int i = 0; i < getItemCount(); i++) {

            View scrap = recycler.getViewForPosition(i);//根据positon获取碎片

            addView(scrap);
            measureChildWithMargins(scrap,0,0);

            int width = getDecoratedMeasuredWidth(scrap);
            int height = getDecoratedMeasuredHeight(scrap);

            layoutDecorated(scrap,offsetX,offsetY,offsetX+width,offsetY+height);

            offsetX += width;
            offsetY += height;

        }
        super.onLayoutChildren(recycler, state);
    }
}

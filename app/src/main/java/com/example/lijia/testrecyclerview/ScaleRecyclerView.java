package com.example.lijia.testrecyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by lijia on 17/8/27.
 */

public class ScaleRecyclerView extends RecyclerView {

    private int mSelectedPosition = 0;

    public ScaleRecyclerView(Context context) {
        this(context,null);
    }

    public ScaleRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ScaleRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void init () {
        //启动子试图排序功能
        setChildrenDrawingOrderEnabled(true);
    }

    public void onDraw(Canvas c) {
        mSelectedPosition = getChildAdapterPosition(getFocusedChild());
        super.onDraw(c);
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        int position = mSelectedPosition;
        if (position < 0) {
            return i;
        } else {
            if (i == childCount - 1) {
                if (position > i) {
                    position = i;
                }
                return position;
            }
            if (i == position) {
                return childCount - 1;
            }
        }
        return i;
    }
}

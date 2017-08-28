package com.example.lijia.testrecyclerview;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by lijia on 17/8/27.
 */

public class FocusLinearLayout extends LinearLayout {

    private Animation scaleSmallAnimation;
    private Animation scaleBigAnimation;

    public FocusLinearLayout(Context context) {
        super(context);
    }

    public FocusLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FocusLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if (gainFocus) {
            getRootView().invalidate();
            zoomOut();
        } else {
            zoomIn();
        }
    }

    private void zoomIn() {
        if (scaleSmallAnimation == null) {
            scaleSmallAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.big);
        }
        startAnimation(scaleSmallAnimation);
    }

    private void zoomOut() {
        if (scaleBigAnimation == null) {
            scaleBigAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.big);
        }
        startAnimation(scaleBigAnimation);
    }
}

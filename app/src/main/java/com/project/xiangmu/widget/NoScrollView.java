package com.project.xiangmu.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * qingyan-android
 * <p>
 * Created by tanghui on 2020/12/1
 * Copyright © 2020年12月01日. All rights reserved.
 * <p>
 * Describe：自定义scrollView禁止滚动
 */
public class NoScrollView extends ScrollView {
    public NoScrollView(Context context) {
        super(context);
    }

    public NoScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return true;
    }

}

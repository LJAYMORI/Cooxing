package com.baoyz.widget;

import android.content.Context;
import android.graphics.Canvas;

public class CooxingDrawble extends RefreshDrawable {

    private boolean isRunning = false;

    public CooxingDrawble(Context context, PullRefreshLayout layout) {
        super(context, layout);
    }

    @Override
    public void setPercent(float percent) {

    }

    @Override
    public void setColorSchemeColors(int[] colorSchemeColors) {

    }

    @Override
    public void offsetTopAndBottom(int offset) {

    }

    @Override
    public void start() {
        isRunning = true;
    }

    @Override
    public void stop() {
        isRunning = false;
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public void draw(Canvas canvas) {

    }
}

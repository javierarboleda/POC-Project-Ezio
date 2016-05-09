package com.javierarboleda.projectezio.comic;

import android.graphics.PointF;

/**
 * Created by javierarboleda on 5/8/16.
 */
public class Panel {

    private PointF mTopLeft;
    private PointF mTopRight;
    private PointF mBottomLeft;
    private PointF mBottomRight;

    public Panel(PointF topLeft, PointF topRight, PointF bottomLeft, PointF bottomRight) {
        this.mTopLeft = topLeft;
        this.mTopRight = topRight;
        this.mBottomLeft = bottomLeft;
        this.mBottomRight = bottomRight;
    }

    public PointF getTopLeft() {
        return mTopLeft;
    }

    public void setTopLeft(PointF mTopLeft) {
        this.mTopLeft = mTopLeft;
    }

    public PointF getTopRight() {
        return mTopRight;
    }

    public void setTopRight(PointF mTopRight) {
        this.mTopRight = mTopRight;
    }

    public PointF getBottomLeft() {
        return mBottomLeft;
    }

    public void setBottomLeft(PointF mBottomLeft) {
        this.mBottomLeft = mBottomLeft;
    }

    public PointF getBottomRight() {
        return mBottomRight;
    }

    public void setBottomRight(PointF mBottomRight) {
        this.mBottomRight = mBottomRight;
    }

}

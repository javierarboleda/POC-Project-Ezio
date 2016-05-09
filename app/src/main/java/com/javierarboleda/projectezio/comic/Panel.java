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
    private float mScale;

    public Panel(PointF topLeft, PointF topRight, PointF bottomLeft, PointF bottomRight,
                 float scale) {

        this.mTopLeft = topLeft;
        this.mTopRight = topRight;
        this.mBottomLeft = bottomLeft;
        this.mBottomRight = bottomRight;
        this.mScale = scale;

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

    public PointF getMidpoint() {
        float x = (mTopLeft.x + mBottomRight.x) / 2;
        float y = (mTopLeft.y + mBottomRight.y) / 2;

        return new PointF(x, y);
    }

    public float getScale() {
        return mScale;
    }

    public void setScale(float mScale) {
        this.mScale = mScale;
    }

}

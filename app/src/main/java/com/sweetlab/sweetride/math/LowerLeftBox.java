package com.sweetlab.sweetride.math;

/**
 * A view port holder.
 * <p/>
 * Old class that I have had around for a while. Should be cleaned up though.
 */
public class LowerLeftBox {
    private int mLeft;
    private int mBottom;
    private int mWidth;
    private int mHeight;

    public void set(LowerLeftBox other) {
        mLeft = other.mLeft;
        mBottom = other.mBottom;
        mWidth = other.mWidth;
        mHeight = other.mHeight;
    }

    public void set(int width, int height) {
        mLeft = 0;
        mBottom = 0;
        mWidth = width;
        mHeight = height;
    }

    public void set(int x, int y, int width, int height) {
        mLeft = x;
        mBottom = y;
        mWidth = width;
        mHeight = height;
    }

    public int getX() {
        return mLeft;
    }

    public int getY() {
        return mBottom;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    @Override
    public String toString() {
        return "left = " + mLeft + " bottom = " + mBottom + " width = " + mWidth + " height = " + mHeight;
    }
}
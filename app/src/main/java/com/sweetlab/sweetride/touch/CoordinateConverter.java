package com.sweetlab.sweetride.touch;

import android.graphics.RectF;

/**
 * Coordinate mapper. Maps from one 2D space to another 2D space specified by
 * rectangles. Does the following mapping.
 * <p/>
 * mFrom.left --> mTo.left
 * mFrom.right --> mTo.right
 * mFrom.top --> mTo.top
 * mFrom.bottom --> mTo.bottom
 */
public class CoordinateConverter {
    /**
     * Rect specifying from boundaries.
     */
    private final RectF mFrom = new RectF();
    /**
     * Rect specifying to boundaries.
     */
    private final RectF mTo = new RectF();

    /**
     * X k value.
     */
    private final float mKx;

    /**
     * Y k value.
     */
    private final float mKy;

    /**
     * Constructor.
     *
     * @param from The from 2D space.
     * @param to   The to 2D space.
     */
    public CoordinateConverter(RectF from, RectF to) {
        mFrom.set(from);
        mTo.set(to);

        mKx = (mTo.left - mTo.right) / (mFrom.left - mFrom.right);
        mKy = (mTo.top - mTo.bottom) / (mFrom.top - mFrom.bottom);
    }

    /**
     * Get the converted position in to space.
     *
     * @param fromX X value in from space.
     * @return X value in to space.
     */
    public float getX(float fromX) {
        return mTo.left + mKx * (fromX - mFrom.left);
    }

    /**
     * Get the converted position in to space.
     *
     * @param fromY Y value in from space.
     * @return Y value in to space.
     */
    public float getY(float fromY) {
        return mTo.top + mKy * (fromY - mFrom.top);
    }
}

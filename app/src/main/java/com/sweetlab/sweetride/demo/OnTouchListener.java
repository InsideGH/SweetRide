package com.sweetlab.sweetride.demo;

import com.sweetlab.sweetride.intersect.Ray;

/**
 * A touch listener.
 */
public interface OnTouchListener {
    /**
     * Called when a Geometry received onDown.
     *
     * @param ray The ray.
     * @param x   The Android screen pixel x coordinated.
     * @param y   The Android screen pixel y coordinated.
     * @return True if handled.
     */
    boolean onDown(Ray ray, int x, int y);

    /**
     * Called when a Geometry that previously received onDown is released onUp.
     *
     * @param ray The ray.
     * @param x   The Android screen pixel x coordinated.
     * @param y   The Android screen pixel y coordinated.
     * @return True if handled.
     */
    boolean onUp(Ray ray, int x, int y);

    /**
     * The ongoing touch event has been canceled.
     */
    void onCancel();

    /**
     * Called during touch movement.
     *
     * @param ray   The ray.
     * @param isHit True if the geometry is hit, false otherwise.
     * @param x     The Android screen pixel x coordinated.
     * @param y     The Android screen pixel y coordinated.
     * @return True if handled.
     */
    boolean onMove(Ray ray, boolean isHit, int x, int y);
}

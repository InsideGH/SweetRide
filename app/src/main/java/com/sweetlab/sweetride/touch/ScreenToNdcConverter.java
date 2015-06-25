package com.sweetlab.sweetride.touch;

import android.graphics.RectF;

/**
 * Convert android screen touch coordinates to GL NDC space.
 */
public class ScreenToNdcConverter extends CoordinateConverter {
    /**
     * Constructor.
     *
     * @param width  Android surface width.
     * @param height Android surface height.
     */
    public ScreenToNdcConverter(int width, int height) {
        super(new RectF(0, 0, width, height), new RectF(-1, 1, 1, -1));
    }
}

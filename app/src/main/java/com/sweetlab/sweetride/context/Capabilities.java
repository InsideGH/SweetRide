package com.sweetlab.sweetride.context;

import android.opengl.GLES20;

/**
 * Knows about the GL capabilities.
 */
public class Capabilities {

    /**
     * Holds max number of texture units.
     */
    private final int mMaxNumberTextureUnits;

    /**
     * Holds the max render buffer size.
     */
    private final int mMaxRenderBufferSize;

    /**
     * Constructor. Must be called with GL context available.
     */
    public Capabilities() {
        int[] buf = new int[1];

        GLES20.glGetIntegerv(GLES20.GL_MAX_TEXTURE_IMAGE_UNITS, buf, 0);
        mMaxNumberTextureUnits = buf[0];

        GLES20.glGetIntegerv(GLES20.GL_MAX_RENDERBUFFER_SIZE, buf, 0);
        mMaxRenderBufferSize = buf[0];
    }

    /**
     * Get the maximum number of texture units.
     *
     * @return Max number of texture units.
     */
    public int getMaxNumberTextureUnits() {
        return mMaxNumberTextureUnits;
    }

    /**
     * Get the max render buffer size in pixels.
     *
     * @return The max render buffer size in pixels.
     */
    public int getMaxRenderBufferSize() {
        return mMaxRenderBufferSize;
    }
}

package com.sweetlab.sweetride.context;

import android.opengl.GLES20;

/**
 * The texture mag filter parameter.
 */
public enum MagFilter {
    NEAREST(GLES20.GL_NEAREST),
    LINEAR(GLES20.GL_LINEAR);

    /**
     * The GL mag filter parameter.
     */
    private final int mGlParam;

    MagFilter(int glParam) {
        mGlParam = glParam;
    }

    /**
     * Get the GL parameter.
     *
     * @return The GL parameter.
     */
    public int getGlParam() {
        return mGlParam;
    }
}

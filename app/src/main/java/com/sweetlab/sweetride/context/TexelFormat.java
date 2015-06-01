package com.sweetlab.sweetride.context;

import android.opengl.GLES20;

/**
 * The texel format.
 */
public enum TexelFormat {
    RGBA(GLES20.GL_RGBA),
    RGB(GLES20.GL_RGB);

    /**
     * The gl texel format.
     */
    private final int mGlFormat;

    /**
     * Constructor.
     *
     * @param glType The gl texel format.
     */
    TexelFormat(int glType) {
        mGlFormat = glType;
    }

    /**
     * Get the GL texel format.
     *
     * @return The GL texel format.
     */
    public int getGlFormat() {
        return mGlFormat;
    }
}

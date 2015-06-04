package com.sweetlab.sweetride.context;

import android.opengl.GLES20;

/**
 * The texture min filter parameter.
 */
public enum MinFilter {
    NEAREST(GLES20.GL_NEAREST),
    LINEAR(GLES20.GL_LINEAR),
    NEAREST_MIPMAP_NEAREST(GLES20.GL_NEAREST_MIPMAP_NEAREST),
    LINEAR_MIPMAP_NEAREST(GLES20.GL_LINEAR_MIPMAP_NEAREST),
    NEAREST_MIPMAP_LINEAR(GLES20.GL_NEAREST_MIPMAP_LINEAR),
    LINEAR_MIPMAP_LINEAR(GLES20.GL_LINEAR_MIPMAP_LINEAR);

    /**
     * The GL min filter parameter.
     */
    private final int mGlParam;

    MinFilter(int glParam) {
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

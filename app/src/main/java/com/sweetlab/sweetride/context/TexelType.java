package com.sweetlab.sweetride.context;

import android.opengl.GLES20;

/**
 * The texel type.
 */
public enum TexelType {
    UNSIGNED_BYTE(GLES20.GL_UNSIGNED_BYTE),
    UNSIGNED_SHORT_5_6_5(GLES20.GL_UNSIGNED_SHORT_5_6_5);

    /**
     * The gl texel type.
     */
    private final int mGlType;

    /**
     * Constructor.
     *
     * @param glType The gl texel type.
     */
    TexelType(int glType) {
        mGlType = glType;
    }

    /**
     * Get the GL texel type.
     *
     * @return The GL texel type.
     */
    public int getGlType() {
        return mGlType;
    }
}

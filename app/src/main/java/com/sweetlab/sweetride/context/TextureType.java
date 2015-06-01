package com.sweetlab.sweetride.context;

import android.opengl.GLES20;

/**
 * The texture type.
 */
public enum TextureType {
    TEXTURE_2D(GLES20.GL_TEXTURE_2D);

    /**
     * The gl texture type.
     */
    private final int mGlType;

    /**
     * Constructor.
     *
     * @param glType The gl texture type.
     */
    TextureType(int glType) {
        mGlType = glType;
    }

    /**
     * Get the GL texture type.
     *
     * @return The GL texture type.
     */
    public int getGlType() {
        return mGlType;
    }
}

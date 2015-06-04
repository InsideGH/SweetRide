package com.sweetlab.sweetride.context;

import android.opengl.GLES20;

/**
 * The attachment type.
 */
public enum AttachmentType {
    RENDER_BUFFER(GLES20.GL_RENDERBUFFER),
    TEXTURE_2D(GLES20.GL_TEXTURE_2D);

    /**
     * The gl attachment type.
     */
    private final int mGlType;

    /**
     * Constructor.
     *
     * @param glType The gl attachment type.
     */
    AttachmentType(int glType) {
        mGlType = glType;
    }

    /**
     * Get the GL attachment type.
     *
     * @return The GL attachment type.
     */
    public int getGlType() {
        return mGlType;
    }
}

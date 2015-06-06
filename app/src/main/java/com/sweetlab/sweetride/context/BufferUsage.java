package com.sweetlab.sweetride.context;

import android.opengl.GLES20;

/**
 * The buffer usage hint.
 */
public enum BufferUsage {
    STATIC(GLES20.GL_STATIC_DRAW),
    DYNAMIC(GLES20.GL_DYNAMIC_DRAW),
    STREAM(GLES20.GL_STREAM_DRAW);

    /**
     * The GL hit parameter.
     */
    private final int mGlHint;

    /**
     * Constructor.
     *
     * @param glHint The gl buffer hint.
     */
    BufferUsage(int glHint) {
        mGlHint = glHint;
    }

    /**
     * Get the gl hint.
     *
     * @return The gl hint.
     */
    public int getGlHint() {
        return mGlHint;
    }
}

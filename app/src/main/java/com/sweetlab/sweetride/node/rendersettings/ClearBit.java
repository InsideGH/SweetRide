package com.sweetlab.sweetride.node.rendersettings;

import android.opengl.GLES20;

/**
 * Bit in a mask that indicate the buffers to be cleared.
 */
public enum ClearBit implements GLEnum {
    ZERO_BIT(0),
    COLOR_BUFFER_BIT(GLES20.GL_COLOR_BUFFER_BIT),
    DEPTH_BUFFER_BIT(GLES20.GL_DEPTH_BUFFER_BIT),
    STENCIL_BUFFER_BIT(GLES20.GL_STENCIL_BUFFER_BIT);

    /**
     * The gl value.
     */
    private final int mGl;

    /**
     * Constructor.
     *
     * @param gl The gl value.
     */
    ClearBit(int gl) {
        mGl = gl;
    }

    @Override
    public int getGL() {
        return mGl;
    }
}

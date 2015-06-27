package com.sweetlab.sweetride.node.rendersettings;

import android.opengl.GLES20;

/**
 * Specifies how the red, green, blue, and alpha destination blending factors are computed.
 */
public enum BlendDstFact implements GLEnum {
    ZERO(GLES20.GL_ZERO),
    ONE(GLES20.GL_ONE),
    SRC_COLOR(GLES20.GL_SRC_COLOR),
    ONE_MINUS_SRC_COLOR(GLES20.GL_ONE_MINUS_SRC_COLOR),
    DST_COLOR(GLES20.GL_DST_COLOR),
    ONE_MINUS_DST_COLOR(GLES20.GL_ONE_MINUS_DST_COLOR),
    SRC_ALPHA(GLES20.GL_SRC_ALPHA),
    ONE_MINUS_SRC_ALPHA(GLES20.GL_ONE_MINUS_SRC_ALPHA),
    DST_ALPHA(GLES20.GL_DST_ALPHA),
    ONE_MINUS_DST_ALPHA(GLES20.GL_ONE_MINUS_DST_ALPHA),
    CONSTANT_COLOR(GLES20.GL_CONSTANT_COLOR),
    ONE_MINUS_CONSTANT_COLOR(GLES20.GL_ONE_MINUS_CONSTANT_COLOR),
    CONSTANT_ALPHA(GLES20.GL_CONSTANT_ALPHA),
    ONE_MINUS_CONSTANT_ALPHA(GLES20.GL_ONE_MINUS_CONSTANT_ALPHA);

    /**
     * The gl value.
     */
    private final int mGl;

    /**
     * Constructor.
     *
     * @param gl The gl value.
     */
    BlendDstFact(int gl) {
        mGl = gl;
    }

    @Override
    public int getGL() {
        return mGl;
    }
}

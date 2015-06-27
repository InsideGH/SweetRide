package com.sweetlab.sweetride.node.rendersettings;

import android.opengl.GLES20;

/**
 * Specify the equation used for both the RGB blend equation and the Alpha blend equation.
 */
public enum BlendEquationFunc implements GLEnum {
    ADD(GLES20.GL_FUNC_ADD),
    SUBTRACT(GLES20.GL_FUNC_SUBTRACT),
    REVERSE_SUBTRACT(GLES20.GL_FUNC_REVERSE_SUBTRACT);

    /**
     * The gl value.
     */
    private final int mGl;

    /**
     * Constructor.
     *
     * @param gl The gl value.
     */
    BlendEquationFunc(int gl) {
        mGl = gl;
    }

    @Override
    public int getGL() {
        return mGl;
    }
}

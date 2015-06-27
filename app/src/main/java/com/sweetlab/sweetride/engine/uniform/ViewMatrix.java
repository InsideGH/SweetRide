package com.sweetlab.sweetride.engine.uniform;

import com.sweetlab.sweetride.math.Matrix44;

/**
 * The view engine uniform matrix.
 */
public class ViewMatrix implements EngineUniform {
    /**
     * The matrix.
     */
    private final Matrix44 mMatrix = new Matrix44();

    @Override
    public String getName() {
        return "u_viewMat";
    }

    @Override
    public Matrix44 getMatrix() {
        return mMatrix;
    }

    @Override
    public EngineUniformType getType() {
        return EngineUniformType.VIEW;
    }
}

package com.sweetlab.sweetride.engine.uniform;

import com.sweetlab.sweetride.math.Matrix44;

/**
 * The projection engine uniform matrix.
 */
public class ProjectionMatrix implements EngineUniform {
    /**
     * The matrix.
     */
    private Matrix44 mMatrix = new Matrix44();

    @Override
    public String getName() {
        return "u_projMat";
    }

    @Override
    public Matrix44 getMatrix() {
        return mMatrix;
    }

    @Override
    public EngineUniformType getType() {
        return EngineUniformType.PROJECTION;
    }
}

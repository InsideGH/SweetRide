package com.sweetlab.sweetride.engine.uniform;

import com.sweetlab.sweetride.math.Matrix44;

/**
 * The world view engine uniform matrix.
 */
public class WorldViewMatrix implements EngineUniform {
    /**
     * The matrix.
     */
    private final Matrix44 mMatrix = new Matrix44();

    @Override
    public String getName() {
        return "u_worldViewMat";
    }

    @Override
    public Matrix44 getMatrix() {
        return mMatrix;
    }

    @Override
    public EngineUniformType getType() {
        return EngineUniformType.WORLD_VIEW;
    }
}

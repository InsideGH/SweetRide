package com.sweetlab.sweetride.engine.uniform;

import com.sweetlab.sweetride.math.Matrix44;

/**
 * The world engine uniform matrix.
 */
public class WorldMatrix implements EngineUniform {
    /**
     * The matrix.
     */
    private final Matrix44 mMatrix = new Matrix44();

    @Override
    public String getName() {
        return "u_worldMat";
    }

    @Override
    public Matrix44 getMatrix() {
        return mMatrix;
    }

    @Override
    public EngineUniformType getType() {
        return EngineUniformType.WORLD;
    }
}

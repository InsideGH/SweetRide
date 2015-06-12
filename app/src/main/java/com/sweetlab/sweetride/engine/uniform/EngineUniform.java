package com.sweetlab.sweetride.engine.uniform;

import com.sweetlab.sweetride.math.Matrix44;

/**
 * Engine uniform.
 */
public interface EngineUniform {
    /**
     * Get the shader uniform name.
     *
     * @return The shader uniform name.
     */
    String getName();

    /**
     * Get the matrix.
     *
     * @return The matrix.
     */
    Matrix44 getMatrix();

    /**
     * Get the type of engine uniform.
     *
     * @return The type.
     */
    EngineUniformType getType();
}

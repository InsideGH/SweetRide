package com.sweetlab.sweetride.engine;

import com.sweetlab.sweetride.math.Matrix44;

/**
 * Supported engine uniforms.
 */
public enum EngineUniform {
    /**
     * The geometry transform, i.e model matrix.
     */
    MODEL_MATRIX("u_modelMat", new Matrix44()),

    /**
     * The aggregated scene graph transform, i.e world matrix.
     */
    WORLD_MATRIX("u_worldMat", new Matrix44()),

    /**
     * The camera view matrix.
     */
    VIEW_MATRIX("u_viewMat", new Matrix44()),

    /**
     * The frustrum projection matrix.
     */
    PROJECTION_MATRIX("u_projMat", new Matrix44()),

    /**
     * The combined world and camera view matrix.
     */
    WORLD_VIEW_MATRIX("u_worldViewMat", new Matrix44()),

    /**
     * The so called mvp matrix but instead of the model the world is used.
     * This means that the matrix depends on all parents transforms as well.
     */
    WORLD_VIEW_PROJECTION_MATRIX("u_worldViewProjMat", new Matrix44());

    /**
     * Shader uniform name.
     */
    private final String mName;

    /**
     * The matrix.
     */
    private final Matrix44 mMatrix;

    /**
     * Constructor.
     *
     * @param name   Shader uniform name.
     * @param matrix The data.
     */
    EngineUniform(String name, Matrix44 matrix) {
        mName = name;
        mMatrix = matrix;
    }

    /**
     * Get the shader uniform name.
     *
     * @return The shader uniform name.
     */
    public String getName() {
        return mName;
    }

    /**
     * Get the matrix.
     *
     * @return The matrix.
     */
    public Matrix44 getMatrix() {
        return mMatrix;
    }
}

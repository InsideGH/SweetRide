package com.sweetlab.sweetride.context;

import android.opengl.GLES20;

/**
 * Mesh drawing modes.
 */
public enum MeshDrawingMode {
    /**
     * Draw using triangles.
     */
    TRIANGLES(GLES20.GL_TRIANGLES),

    /**
     * Draw using triangle strip.
     */
    TRIANGLE_STRIP(GLES20.GL_TRIANGLE_STRIP),

    /**
     * Draw using triangle fan.
     */
    TRIANGLE_FAN(GLES20.GL_TRIANGLE_FAN),

    /**
     * Draw using points.
     */
    POINTS(GLES20.GL_POINTS),

    /**
     * Draw using lines.
     */
    LINES(GLES20.GL_LINES);

    /**
     * The GL drawing mode.
     */
    private final int mGlMode;

    /**
     * Constructor.
     *
     * @param glMode The GL drawing mode.
     */
    MeshDrawingMode(int glMode) {
        mGlMode = glMode;
    }

    /**
     * Get the GL drawing mode.
     *
     * @return The GL drawing mode.
     */
    public int getGlMode() {
        return mGlMode;
    }
}

package com.sweetlab.sweetride.context;

import android.opengl.GLES20;

import com.sweetlab.sweetride.DebugOptions;
import com.sweetlab.sweetride.shader.ShaderProgram;

/**
 * GLES20State information.
 */
public class GLES20State {
    /**
     * Buffer for reading GLES20 information.
     */
    private int[] buf = new int[1];

    /**
     * Backend context.
     */
    private final BackendContext mContext;

    /**
     * Constructor.
     *
     * @param backendContext The backend context.
     */
    public GLES20State(BackendContext backendContext) {
        mContext = backendContext;
    }

    /**
     * Read the active shader program.
     *
     * @return The active shader program.
     */
    public int readActiveProgram() {
        GLES20.glGetIntegerv(GLES20.GL_CURRENT_PROGRAM, buf, 0);
        return buf[0];
    }

    /**
     * Use shader program.
     *
     * @param program The program to use.
     */
    public void useProgram(ShaderProgram program) {
        if (DebugOptions.DEBUG_STATE) {
            if (!program.isCreated()) {
                throw new RuntimeException("Trying to use not linked program");
            }
        }
        GLES20.glUseProgram(program.getId());
    }
}

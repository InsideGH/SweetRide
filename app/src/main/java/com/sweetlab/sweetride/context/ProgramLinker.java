package com.sweetlab.sweetride.context;

import android.opengl.GLES20;
import android.util.Log;

import com.sweetlab.sweetride.shader.FragmentShader;
import com.sweetlab.sweetride.shader.VertexShader;
import com.sweetlab.sweetride.util.Util;

/**
 * Shader program linker.
 */
public class ProgramLinker {
    /**
     * GL read buffer
     */
    private final int[] sReadParams = new int[1];

    /**
     * Backend context.
     */
    private final BackendContext mContext;

    /**
     * Constructor.
     *
     * @param backendContext Backend context.
     */
    public ProgramLinker(BackendContext backendContext) {
        mContext = backendContext;
    }

    /**
     * Link shader program.
     *
     * @param vertexShader   The vertex shader.
     * @param fragmentShader The fragment shader.
     * @return Linked program id.
     */
    public int link(VertexShader vertexShader, FragmentShader fragmentShader) {
        if (!vertexShader.isCreated()) {
            vertexShader.create(mContext);
        }

        if (!fragmentShader.isCreated()) {
            fragmentShader.create(mContext);
        }

        int id = mContext.getResourceManager().createProgram();
        if (id == GLES20.GL_FALSE) {
            Log.d("Peter100", "Could not create program.\n " + vertexShader.getSource() + "\n" + fragmentShader.getSource());
            return ResourceManager.INVALID_PROGRAM_ID;
        }

        GLES20.glAttachShader(id, vertexShader.getId());
        if (Util.hasGlError()) {
            Log.d("Peter100", "Could not attach vertex shader to program. \n" + vertexShader.getSource());
            return ResourceManager.INVALID_PROGRAM_ID;
        }
        GLES20.glAttachShader(id, fragmentShader.getId());
        if (Util.hasGlError()) {
            Log.d("Peter100", "Could not attach fragment shader to program. \n " + fragmentShader.getSource());
        }

        GLES20.glLinkProgram(id);
        if (!readIsLinked(id)) {
            Log.d("Peter100", "Could not create program, log = " + GLES20.glGetProgramInfoLog(id) + "\n" + vertexShader.getSource() + "\n" + fragmentShader.getSource());
            GLES20.glDeleteProgram(id);
            return ResourceManager.INVALID_PROGRAM_ID;
        }
        return id;
    }

    /**
     * Read if program is a linked program.
     *
     * @param program program to check.
     * @return True if linked.
     */
    private boolean readIsLinked(int program) {
        if (program > 0) {
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, sReadParams, 0);
            return sReadParams[0] == GLES20.GL_TRUE;
        }
        return false;
    }
}

package com.sweetlab.sweetride.context;

import android.opengl.GLES20;
import android.util.Log;

import com.sweetlab.sweetride.shader.FragmentShader;
import com.sweetlab.sweetride.shader.VertexShader;
import com.sweetlab.sweetride.util.Util;

public class ProgramLinker {
    /**
     * Link fail id.
     */
    public static final int INVALID_ID = 0;

    private final int[] sReadParams = new int[1];

    /**
     * Backend context.
     */
    private final BackendContext mContext;

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
        if (!vertexShader.isCompiled()) {
            vertexShader.compile(mContext);
        }

        if (!fragmentShader.isCompiled()) {
            fragmentShader.compile(mContext);
        }

        int id = GLES20.glCreateProgram();
        if (id == GLES20.GL_FALSE) {
            Log.d("Peter100", "Could not create program.\n " + vertexShader.getSource() + "\n" + fragmentShader.getSource());
            return INVALID_ID;
        }

        Util.hasGlError();

        GLES20.glAttachShader(id, vertexShader.getId());
        if (Util.hasGlError()) {
            Log.d("Peter100", "Could not attach vertex shader to program. \n" + vertexShader.getSource());
            return INVALID_ID;
        }
        GLES20.glAttachShader(id, fragmentShader.getId());
        if (Util.hasGlError()) {
            Log.d("Peter100", "Could not attach fragment shader to program. \n " + fragmentShader.getSource());
        }

        GLES20.glLinkProgram(id);
        if (!readIsLinked(id)) {
            Log.d("Peter100", "Could not link program, log = " + GLES20.glGetProgramInfoLog(id) + "\n" + vertexShader.getSource() + "\n" + fragmentShader.getSource());
            GLES20.glDeleteProgram(id);
            return INVALID_ID;
        }
        return id;
    }

    /**
     * Read if id is a linked program.
     *
     * @param program Id to check.
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

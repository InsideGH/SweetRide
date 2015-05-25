package com.sweetlab.sweetride.context;

import android.opengl.GLES20;
import android.util.Log;

/**
 * Shader compiler.
 */
public class ShaderCompiler {
    /**
     * Compile fail id.
     */
    public static final int INVALID_ID = 0;

    private final int[] sBuf = new int[1];

    /**
     * Backend context.
     */
    private final BackendContext mContext;

    public ShaderCompiler(BackendContext backendContext) {
        mContext = backendContext;
    }

    /**
     * Compile fragment shader.
     *
     * @param source The source code.
     * @return Compiled id.
     */
    public int compileFragmentShader(String source) {
        return compile(source, GLES20.GL_FRAGMENT_SHADER);
    }

    /**
     * Compile vertex shader.
     *
     * @param source The source code.
     * @return Compiled id.
     */
    public int compileVertexShader(String source) {
        return compile(source, GLES20.GL_VERTEX_SHADER);
    }

    /**
     * Compile shader source.
     *
     * @param source The source code.
     * @param type   Vertex or fragment shader.
     * @return The compiled id.
     */
    private int compile(String source, int type) {
        int id = GLES20.glCreateShader(type);
        if (id == 0) {
            Log.d("Peter100", "Can't create shader for\n" + source);
            return INVALID_ID;
        }

        GLES20.glShaderSource(id, source);
        GLES20.glCompileShader(id);

        if (!readIsCompiled(id)) {
            Log.d("Peter100", "Can't compile shader\n" + GLES20.glGetShaderInfoLog(id));
            GLES20.glDeleteShader(id);
            return INVALID_ID;
        }
        return id;
    }

    /**
     * Read if id is a compiled shader.
     *
     * @param id Id to check.
     * @return True if compiled.
     */
    private boolean readIsCompiled(int id) {
        if (id > 0) {
            GLES20.glGetShaderiv(id, GLES20.GL_COMPILE_STATUS, sBuf, 0);
            return sBuf[0] == GLES20.GL_TRUE;
        }
        return false;
    }
}

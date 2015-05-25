package com.sweetlab.sweetride.context;

import android.opengl.GLES20;

import com.sweetlab.sweetride.DebugOptions;
import com.sweetlab.sweetride.shader.ShaderProgram;
import com.sweetlab.sweetride.shader.Uniform;

/**
 * Shader uniform writer.
 */
public class UniformWriter {
    /**
     * Default uniform values.
     */
    private static final float[] sFloatDefault = new float[16];

    /**
     * Backend context.
     */
    private final BackendContext mContext;

    public UniformWriter(BackendContext backendContext) {
        mContext = backendContext;
    }

    /**
     * Write default values to uniform.
     */
    public void writeDefault(ShaderProgram program, String name) {
        Uniform uniform = program.getUniform(name);
        if (uniform != null) {
            if (isFloatType(uniform)) {
                writeFloat(program, name, sFloatDefault);
            }
        }
    }

    /**
     * Write data to uniform. Writing to uniform requires that correct shader program is in use.
     */
    public void writeFloat(ShaderProgram program, String name, float[] data) {
        Uniform uniform = program.getUniform(name);
        if (uniform != null) {
            if (DebugOptions.DEBUG_UNIFORM_WRITES) {
                int activeProgram = mContext.getState().readActiveProgram();
                if (activeProgram != program.getId()) {
                    throw new RuntimeException("UniformWriter.writeFloat with wrong program. Active = " + activeProgram + " but trying to write to " + program.getId());
                }
            }

            if (isFloatType(uniform)) {
                int location = uniform.getLocation();
                switch (uniform.getElementCount()) {
                    case 1:
                        GLES20.glUniform1fv(location, 1, data, 0);
                        break;
                    case 2:
                        GLES20.glUniform2fv(location, 1, data, 0);
                        break;
                    case 3:
                        GLES20.glUniform3fv(location, 1, data, 0);
                        break;
                    case 4:
                        GLES20.glUniform4fv(location, 1, data, 0);
                        break;
                    case 9:
                        GLES20.glUniformMatrix3fv(location, 1, false, data, 0);
                        break;
                    case 16:
                        GLES20.glUniformMatrix4fv(location, 1, false, data, 0);
                        break;
                    default:
                        throw new RuntimeException("Unsupported size in UniformWriter.writeFloat " + uniform.getElementCount());
                }
            }
        }
    }

    /**
     * Check if uniform is a float uniform.
     *
     * @param uniform The uniform to check.
     * @return True if float type.
     */
    private static boolean isFloatType(Uniform uniform) {
        switch (uniform.getType()) {
            case GLES20.GL_FLOAT_VEC2:
            case GLES20.GL_FLOAT_VEC3:
            case GLES20.GL_FLOAT_VEC4:
            case GLES20.GL_FLOAT:
            case GLES20.GL_FLOAT_MAT3:
            case GLES20.GL_FLOAT_MAT4:
                return true;
        }
        return false;
    }
}

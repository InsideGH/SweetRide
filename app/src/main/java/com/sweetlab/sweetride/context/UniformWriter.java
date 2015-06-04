package com.sweetlab.sweetride.context;

import android.opengl.GLES20;

import com.sweetlab.sweetride.DebugOptions;
import com.sweetlab.sweetride.shader.ShaderProgram;
import com.sweetlab.sweetride.shader.ProgramUniform;

/**
 * Shader uniform writer.
 */
public class UniformWriter {
    /**
     * Default uniform value for floats.
     */
    private static final float[] sFloatDefault = new float[16];

    /**
     * Default uniform value for ints.
     */
    private static final int[] sIntDefault = new int[4];

    /**
     * Backend context.
     */
    private final BackendContext mContext;

    /**
     * Constructor.
     *
     * @param backendContext Backend context.
     */
    public UniformWriter(BackendContext backendContext) {
        mContext = backendContext;
    }

    /**
     * Write default values to uniform.
     *
     * @param program Shader program to access.
     * @param name    Name of uniform in program.
     */
    public void writeDefault(ShaderProgram program, String name) {
        ProgramUniform programUniform = program.getUniform(name);
        if (programUniform != null) {
            if (isFloatType(programUniform)) {
                writeFloat(program, name, sFloatDefault);
            } else if (isIntType(programUniform)) {
                writeInt(program, name, sIntDefault);
            }
        }
    }

    /**
     * Write data to uniform. Writing to uniform requires that correct shader program is in use.
     *
     * @param program Shader program to access.
     * @param name    Uniform in shader program.
     * @param data    Data to write, the actual amount written is known by shader uniform.
     */
    public void writeFloat(ShaderProgram program, String name, float[] data) {
        ProgramUniform programUniform = program.getUniform(name);
        if (programUniform != null) {
            if (DebugOptions.DEBUG_UNIFORM_WRITES) {
                int activeProgram = mContext.getState().readActiveProgram();
                if (activeProgram != program.getId()) {
                    throw new RuntimeException("UniformWriter.writeFloat with wrong program. Active = " + activeProgram + " but trying to write to " + program.getId());
                }
            }

            if (isFloatType(programUniform)) {
                int location = programUniform.getLocation();
                switch (programUniform.getElementCount()) {
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
                        throw new RuntimeException("Unsupported size in UniformWriter.writeFloat " + programUniform.getElementCount());
                }
            }
        }
    }

    /**
     * Write data to uniform. Writing to uniform requires that correct shader program is in use.
     *
     * @param program Shader program to access.
     * @param name    Uniform in shader program.
     * @param data    Data to write, the actual amount written is known by shader uniform.
     */
    public void writeInt(ShaderProgram program, String name, int[] data) {
        ProgramUniform programUniform = program.getUniform(name);
        if (programUniform != null) {
            if (DebugOptions.DEBUG_UNIFORM_WRITES) {
                int activeProgram = mContext.getState().readActiveProgram();
                if (activeProgram != program.getId()) {
                    throw new RuntimeException("UniformWriter.writeInt with wrong program. Active = " + activeProgram + " but trying to write to " + program.getId());
                }
            }

            if (isIntType(programUniform)) {
                int location = programUniform.getLocation();
                switch (programUniform.getElementCount()) {
                    case 1:
                        GLES20.glUniform1iv(location, 1, data, 0);
                        break;
                    case 2:
                        GLES20.glUniform2iv(location, 1, data, 0);
                        break;
                    case 3:
                        GLES20.glUniform3iv(location, 1, data, 0);
                        break;
                    case 4:
                        GLES20.glUniform4iv(location, 1, data, 0);
                        break;
                    default:
                        throw new RuntimeException("Unsupported size in UniformWriter.writeInt " + programUniform.getElementCount());
                }
            }
        }
    }

    /**
     * Check if uniform is a float uniform.
     *
     * @param programUniform The uniform to check.
     * @return True if float type.
     */
    private static boolean isFloatType(ProgramUniform programUniform) {
        switch (programUniform.getType()) {
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

    /**
     * Check if uniform is a int uniform.
     *
     * @param programUniform The uniform to check.
     * @return True if int type.
     */
    private static boolean isIntType(ProgramUniform programUniform) {
        switch (programUniform.getType()) {
            case GLES20.GL_SAMPLER_2D:
            case GLES20.GL_SAMPLER_CUBE:
            case GLES20.GL_INT_VEC2:
            case GLES20.GL_INT_VEC3:
            case GLES20.GL_INT_VEC4:
            case GLES20.GL_INT:
                return true;
        }
        return false;
    }
}

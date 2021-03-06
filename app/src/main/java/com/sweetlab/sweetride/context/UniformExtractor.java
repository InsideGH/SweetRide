package com.sweetlab.sweetride.context;

import android.opengl.GLES20;

import com.sweetlab.sweetride.shader.ShaderProgram;
import com.sweetlab.sweetride.shader.ProgramUniform;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Extract uniforms from a linked shader program.
 */
public class UniformExtractor {
    /**
     * Max len of name string.
     */
    private static final int MAX_NAME_LENGTH = 100;

    /**
     * Number of uniforms.
     */
    private final int[] mCount = new int[1];

    /**
     * The len of the found uniform name.
     */
    private final int[] mNameSize = new int[1];

    /**
     * Array size.
     */
    private final int[] mArraySize = new int[1];

    /**
     * Type of uniform.
     */
    private final int[] mType = new int[1];

    /**
     * The storage for the name of the uniform.
     */
    private final byte[] mName = new byte[MAX_NAME_LENGTH];

    /**
     * Constructor.
     *
     * @param backendContext Backend context.
     */
    @SuppressWarnings("unused")
    public UniformExtractor(BackendContext backendContext) {
    }

    /**
     * Extract all uniforms from a linked shader program.
     *
     * @param program Shader program.
     * @return Map of all uniforms.
     */
    public Map<String, ProgramUniform> extract(ShaderProgram program) {
        int id = program.getId();
        GLES20.glGetProgramiv(id, GLES20.GL_ACTIVE_UNIFORMS, mCount, 0);
        if (mCount[0] > 0) {
            Map<String, ProgramUniform> map = new HashMap<>(mCount[0]);
            for (int i = 0; i < mCount[0]; i++) {
                GLES20.glGetActiveUniform(id, i, MAX_NAME_LENGTH, mNameSize, 0, mArraySize, 0, mType, 0, mName, 0);
                String name = new String(mName, 0, mNameSize[0]);
                int location = GLES20.glGetUniformLocation(id, name);
                map.put(name, new ProgramUniform(name, mArraySize[0], mType[0], location));
            }
            return map;
        }
        return Collections.emptyMap();
    }
}

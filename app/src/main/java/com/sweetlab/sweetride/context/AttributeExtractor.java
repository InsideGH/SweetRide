package com.sweetlab.sweetride.context;

import android.opengl.GLES20;

import com.sweetlab.sweetride.shader.Attribute;
import com.sweetlab.sweetride.shader.ShaderProgram;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Shader attribute extractor.
 */
public class AttributeExtractor {
    /**
     * Max len of name.
     */
    private final static int MAX_NAME_LENGTH = 100;

    /**
     * Name of attribute.
     */
    private final byte[] mName = new byte[MAX_NAME_LENGTH];

    /**
     * Number of attributes found.
     */
    private final int[] mCount = new int[1];

    /**
     * String length of name.
     */
    private final int[] mNameLen = new int[1];

    /**
     * Array size of attribute.
     */
    private final int[] mArraySize = new int[1];

    /**
     * Attribute type.
     */
    private final int[] mType = new int[1];

    /**
     * Backend context.
     */
    private final BackendContext mContext;

    /**
     * Constructor.
     *
     * @param backendContext The backend context.
     */
    public AttributeExtractor(BackendContext backendContext) {
        mContext = backendContext;
    }

    /**
     * Extract all attributes from a linked shader program.
     *
     * @param program Shader program.
     * @return Map of attributes.
     */
    public Map<String, Attribute> extract(ShaderProgram program) {
        int id = program.getId();
        GLES20.glGetProgramiv(id, GLES20.GL_ACTIVE_ATTRIBUTES, mCount, 0);
        if (mCount[0] > 0) {
            Map<String, Attribute> map = new HashMap<>(mCount[0]);
            for (int i = 0; i < mCount[0]; i++) {
                GLES20.glGetActiveAttrib(id, i, MAX_NAME_LENGTH, mNameLen, 0, mArraySize, 0, mType, 0, mName, 0);
                String name = new String(mName, 0, mNameLen[0]);
                int location = GLES20.glGetAttribLocation(id, name);
                map.put(name, new Attribute(name, mArraySize[0], mType[0], location));
            }
            return map;
        }
        return Collections.emptyMap();
    }
}

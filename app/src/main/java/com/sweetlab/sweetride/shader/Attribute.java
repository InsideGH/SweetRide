package com.sweetlab.sweetride.shader;

import com.sweetlab.sweetride.context.GLES20Helper;

/**
 * Represent a shader attribute.
 */
public class Attribute {
    /**
     * The name of the attribute in the shader.
     */
    private final String mName;

    /**
     * The array size of the attribute.
     */
    private final int mArraySize;

    /**
     * The type (for example GL_FLOAT, GL_FLOAT_VEC2, GL_FLOAT_VEC3, GL_FLOAT_VEC4, GL_FLOAT_MAT2, GL_FLOAT_MAT3, or GL_FLOAT_MAT4)
     */
    private final int mType;

    /**
     * The type family (GL_FLOAT, GL_FLOAT_VEC2 etc) would all be of GL_FLOAT.
     */
    private final int mTypeFamily;

    /**
     * The element count of the uniform (for example, GLES20.GL_FLOAT_VEC2 would be 2)
     */
    private int mVertexSize;

    /**
     * The location of the attribute.
     */
    private final int mLocation;

    /**
     * Constructor. Immutable dao.
     *
     * @param name      The name (as in the shader)
     * @param arraySize Size of the array (vec3 would be 1, but vec3[2] would be 2)
     * @param type      The type (for example GL_FLOAT, GL_FLOAT_VEC2, GL_FLOAT_VEC3, GL_FLOAT_VEC4, GL_FLOAT_MAT2, GL_FLOAT_MAT3, or GL_FLOAT_MAT4).
     * @param location  The attribute location in the program.
     */
    public Attribute(String name, int arraySize, int type, int location) {
        mName = name;
        mArraySize = arraySize;
        mType = type;
        mLocation = location;
        mVertexSize = GLES20Helper.getTypeElementCount(type);
        mTypeFamily = GLES20Helper.getTypeFamily(type);
    }

    /**
     * The name.
     *
     * @return The name.
     */
    public String getName() {
        return mName;
    }

    /**
     * Get type family. The type family of (GL_FLOAT, GL_FLOAT_VEC2 etc) would all be of GL_FLOAT.
     *
     * @return The type family.
     */
    public int getTypeFamily() {
        return mTypeFamily;
    }

    /**
     * Get the number of elements in attribute. For example GLES20.GL_FLOAT_VEC2 would return 2.
     *
     * @return Number of elements.
     */
    public int getVertexSize() {
        return mVertexSize;
    }

    /**
     * The array size.
     *
     * @return The array size.
     */
    public int getArraySize() {
        return mArraySize;
    }

    /**
     * The type.
     *
     * @return The type.
     */
    public int getType() {
        return mType;
    }

    /**
     * The location.
     *
     * @return The location.
     */
    public int getLocation() {
        return mLocation;
    }

    @Override
    public String toString() {
        return "name = " + mName + ", arraySize = " + mArraySize + ", type = " + GLES20Helper.getReadableType(mType) + ", location = " + mLocation;
    }
}

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
     * The type (for example GLES20.GL_FLOAT)
     */
    private final int mType;

    /**
     * The location of the attribute.
     */
    private final int mLocation;

    /**
     * Constructor. Immutable dao.
     *
     * @param name     The name (as in the shader)
     * @param size     The array size.
     * @param type     The type.
     * @param location The location.
     */
    public Attribute(String name, int size, int type, int location) {
        mName = name;
        mArraySize = size;
        mType = type;
        mLocation = location;
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

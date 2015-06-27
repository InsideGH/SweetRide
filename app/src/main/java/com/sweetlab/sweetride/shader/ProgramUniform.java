package com.sweetlab.sweetride.shader;

import com.sweetlab.sweetride.context.GLES20Helper;

/**
 * This is a representation of a uniform in a shader program.
 */
public class ProgramUniform {
    /**
     * Name of the uniform.
     */
    private final String mName;

    /**
     * Array size of uniform.
     */
    private final int mArraySize;

    /**
     * The element count of the uniform (for example, GL_FLOAT_VEC2 would be 2)
     */
    private final int mElementCount;

    /**
     * The type of uniform (for example GL_FLOAT)
     */
    private final int mType;

    /**
     * Location in shader program.
     */
    private final int mLocation;

    /**
     * Constructor.
     *
     * @param name      Name of uniform.
     * @param arraySize The array size.
     * @param type      Type (for example GL_FLOAT)
     * @param location  Location of uniform in linked shader program.
     */
    public ProgramUniform(String name, int arraySize, int type, int location) {
        mName = name;
        mArraySize = arraySize;
        mType = type;
        mLocation = location;
        mElementCount = GLES20Helper.getTypeElementCount(type);
    }

    /**
     * Get the number of elements in uniform. For example GL_FLOAT_VEC2 would return 2.
     *
     * @return Number of elements.
     */
    public int getElementCount() {
        return mElementCount;
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
     * Get the location of the uniform in the linked shader program.
     *
     * @return The location.
     */
    public int getLocation() {
        return mLocation;
    }

    @Override
    public String toString() {
        return "name = " + mName + " arraySize = " + mArraySize + " elementCount = " + mElementCount + " type = " + GLES20Helper.getReadableType(mType) + " location = " + mLocation;
    }
}

package com.sweetlab.sweetride.attributedata;

import com.sweetlab.sweetride.util.Util;

/**
 * Data to be used with Vertex Buffers.
 */
public class VertexData {
    /**
     * The data.
     */
    private final float[] mData;

    /**
     * The vertex size. Texture coordinates would lead to a vertex size of 2 for example.
     */
    private final int mVertexSize;

    /**
     * Number of vertices.
     */
    private final int mVertexCount;

    /**
     * Total byte count.
     */
    private final int mTotalByteCount;

    /**
     * Should the data be normalized.
     */
    private final boolean mShouldNormalize;

    /**
     * Byte size of a single vertex.
     */
    private final int mVertexByteSize;

    /**
     * Constructor.
     *
     * @param data            The data, will be copied.
     * @param vertexSize      The vertex size. For example, texture coordinates would set this to 2.
     * @param shouldNormalize True if the data should be normalized.
     */
    public VertexData(float[] data, int vertexSize, boolean shouldNormalize) {
        if ((data.length % vertexSize) == 0 && data.length != 0) {
            mData = new float[data.length];
            System.arraycopy(data, 0, mData, 0, data.length);

            mVertexSize = vertexSize;
            mShouldNormalize = shouldNormalize;

            mVertexCount = data.length / mVertexSize;
            mTotalByteCount = data.length * Util.BYTES_PER_FLOAT;
            mVertexByteSize = mVertexSize * Util.BYTES_PER_FLOAT;
        } else {
            throw new RuntimeException("Data length, " + data.length + " is not a factor of vertex size, " + vertexSize);
        }
    }

    /**
     * Get a reference to the internal data. Use with caution.
     *
     * @return The internal reference to the data.
     */
    public float[] getData() {
        return mData;
    }

    /**
     * Get the vertex size. For texture coordinates, this would be 2.
     *
     * @return The vertex size.
     */
    public int getVertexSize() {
        return mVertexSize;
    }

    /**
     * Get if the data should be normalized or not.
     *
     * @return True if should be normalized.
     */
    public boolean getShouldNormalize() {
        return mShouldNormalize;
    }

    /**
     * Get number of vertices.
     *
     * @return Number of vertices.
     */
    public int getVertexCount() {
        return mVertexCount;
    }

    /**
     * Get the total byte count.
     *
     * @return The total byte count.
     */
    public int getTotalByteCount() {
        return mTotalByteCount;
    }

    /**
     * Get the byte size of a single vertex.
     *
     * @return The byte size of a single vertex.
     */
    public int getVertexByteSize() {
        return mVertexByteSize;
    }
}

package com.sweetlab.sweetride.attributedata;

/**
 * This is a attribute pointer implementation with a reference to the attribute data.
 */
public class BufferPointer implements AttributePointer {
    /**
     * The shader program attribute name.
     */
    private final String mName;

    /**
     * The vertex data.
     */
    private final VertexData mData;

    /**
     * Since this pointer co-exists with other pointers, the stride value specifies number of
     * bytes to step to to reach the next vertex for this pointer.
     */
    private final int mStride;

    /**
     * Since this pointer co-exists with other pointers, the offset value specifies number of
     * bytes to offset from the beginning of the data to the first vertex of this pointer.
     */
    private final int mOffset;

    /**
     * Constructor. This pointer most likely co-exists with other pointers within a interleaved
     * vertex buffer.
     *
     * @param name   The shader program attribute name.
     * @param data   The vertex data.
     * @param stride The stride value specifies number of bytes to the next vertex.
     * @param offset The offset value specifies number of bytes from the beginning of the
     *               data to the first vertex of this pointer.
     */
    public BufferPointer(String name, VertexData data, int stride, int offset) {
        mName = name;
        mData = data;
        mStride = stride;
        mOffset = offset;
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public int getVertexCount() {
        return mData.getVertexCount();
    }

    @Override
    public int getVertexByteSize() {
        return mData.getVertexByteSize();
    }

    @Override
    public int getVertexSize() {
        return mData.getVertexSize();
    }

    @Override
    public int getStrideBytes() {
        return mStride;
    }

    @Override
    public boolean getShouldNormalize() {
        return mData.getShouldNormalize();
    }

    @Override
    public int getOffsetBytes() {
        return mOffset;
    }
}

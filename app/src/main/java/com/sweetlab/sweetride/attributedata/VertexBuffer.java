package com.sweetlab.sweetride.attributedata;

import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.resource.BufferResource;
import com.sweetlab.sweetride.util.Util;

import java.nio.Buffer;

/**
 * A vertex buffer.
 */
public class VertexBuffer implements BufferResource, AttributePointer {

    /**
     * The mapping to the attribute in the shader program.
     */
    private final String mName;

    /**
     * The buffer of data.
     */
    private final Buffer mBuffer;

    /**
     * The buffer usage hint.
     */
    private final int mBufferUsage;

    /**
     * The vertex data.
     */
    private final VertexData mVertexData;

    /**
     * The buffer 'name'/id.
     */
    private int mBufferId = INVALID_BUFFER_ID;

    /**
     * Constructor. Don't forget to create it to generate a buffer id.
     *
     * @param name        Name of attribute in shader program.
     * @param vertexData  Vertex data.
     * @param bufferUsage Buffer usage hint.
     */
    public VertexBuffer(String name, VertexData vertexData, int bufferUsage) {
        mName = name;
        mBufferUsage = bufferUsage;
        mVertexData = vertexData;
        mBuffer = Util.createBuffer(vertexData.getData(), vertexData.getTotalByteCount());
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public int getId() {
        return mBufferId;
    }

    @Override
    public Buffer getData() {
        return mBuffer;
    }

    @Override
    public int getVertexCount() {
        return mVertexData.getVertexCount();
    }

    @Override
    public int getTotalByteCount() {
        return mVertexData.getTotalByteCount();
    }

    @Override
    public int getStrideBytes() {
        return 0;
    }

    @Override
    public int getOffsetBytes() {
        return 0;
    }

    @Override
    public int getVertexSize() {
        return mVertexData.getVertexSize();
    }

    @Override
    public int getVertexByteSize() {
        return mVertexData.getVertexByteSize();
    }

    @Override
    public boolean getShouldNormalize() {
        return mVertexData.getShouldNormalize();
    }

    @Override
    public int getBufferUsage() {
        return mBufferUsage;
    }

    @Override
    public boolean isCreated() {
        return mBufferId != INVALID_BUFFER_ID;
    }

    @Override
    public void release(BackendContext context) {
        context.getResourceManager().deleteBuffer(mBufferId);
        mBufferId = INVALID_BUFFER_ID;
    }

    @Override
    public void create(BackendContext context) {
        mBufferId = context.getResourceManager().generateBuffer();
    }
}

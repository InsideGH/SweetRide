package com.sweetlab.sweetride.attributedata;

import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.context.ResourceManager;
import com.sweetlab.sweetride.resource.BufferResource;
import com.sweetlab.sweetride.util.Util;

import java.nio.Buffer;
import java.nio.ShortBuffer;

/**
 * Indices buffer holding indices, used while drawing with indices.
 */
public class IndicesBuffer implements BufferResource {
    /**
     * The buffer 'name'/id.
     */
    private int mBufferId = ResourceManager.INVALID_BUFFER_ID;

    /**
     * Buffer holding the indices.
     */
    private final Buffer mBuffer;

    /**
     * The total byte count.
     */
    private final int mByteCount;

    /**
     * Number of indices.
     */
    private final int mIndicesCount;

    /**
     * The buffer usage hint.
     */
    private final int mBufferUsage;

    /**
     * Constructor.
     *
     * @param data Array of indices.
     */
    public IndicesBuffer(short[] data, int bufferUsage) {
        mBufferUsage = bufferUsage;
        mIndicesCount = data.length;
        mByteCount = data.length * Util.BYTES_PER_SHORT;
        mBuffer = createShortBuffer(data);
    }

    @Override
    public void create(BackendContext context) {
        mBufferId = context.getResourceManager().generateBuffer();
    }

    @Override
    public boolean isCreated() {
        return mBufferId != ResourceManager.INVALID_BUFFER_ID;
    }

    @Override
    public int getId() {
        return mBufferId;
    }

    @Override
    public Buffer getBuffer() {
        return mBuffer;
    }

    @Override
    public int getTotalByteCount() {
        return mByteCount;
    }

    @Override
    public int getBufferUsage() {
        return mBufferUsage;
    }

    @Override
    public void release(BackendContext context) {
        context.getResourceManager().deleteBuffer(mBufferId);
    }

    /**
     * Get indices count.
     *
     * @return Indices count.
     */
    public int getIndicesCount() {
        return mIndicesCount;
    }

    /**
     * Create a buffer from data array.
     *
     * @param data Input data.
     * @return A buffer.
     */
    private Buffer createShortBuffer(short[] data) {
        ShortBuffer buffer = Util.allocByteBuffer(mByteCount).asShortBuffer();
        buffer.clear();
        buffer.put(data);
        buffer.rewind();
        return buffer;
    }
}

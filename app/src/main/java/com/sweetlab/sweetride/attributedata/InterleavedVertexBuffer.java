package com.sweetlab.sweetride.attributedata;

import android.util.Pair;

import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.resource.BufferResource;
import com.sweetlab.sweetride.resource.VertexBufferResource;

import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

/**
 * The buffer resource holds one attribute data and many attribute pointers, aka an
 * interleaved vertex buffer.
 */
public class InterleavedVertexBuffer implements VertexBufferResource {
    /**
     * Builder to build an interleaved vertex buffer.
     */
    public static class Builder {
        /**
         * The buffer usage hint.
         */
        private final int mBufferUsage;

        /**
         * Keep track of added entries.
         */
        List<Pair<String, VertexData>> mEntries = new ArrayList<>();

        /**
         * Constructor.
         *
         * @param bufferUsage The buffer usage hint.
         */
        public Builder(int bufferUsage) {
            mBufferUsage = bufferUsage;
        }

        /**
         * Add entry. Connects the attribute name in the shader with the vertex data.
         *
         * @param name Shader attribute name.
         * @param data Vertex data.
         * @return This builder.
         */
        public Builder add(String name, VertexData data) {
            mEntries.add(new Pair<>(name, data));
            return this;
        }

        /**
         * Build a interleaved vertex buffer.
         *
         * @return The interleaved vertex buffer.
         */
        public InterleavedVertexBuffer build() {
            checkConditions();
            List<AttributePointer> pointers = createPointers(calcStride());
            InterleavedData data = new InterleavedData(mEntries, mBufferUsage);
            return new InterleavedVertexBuffer(data, pointers);
        }

        /**
         * Check conditions. Throws if not fulfilled.
         */
        private void checkConditions() throws IllegalArgumentException {
            if (mEntries.isEmpty()) {
                throw new RuntimeException("Can't build interleaved vertex buffer without entries.");
            }
            int vertexCount = mEntries.get(0).second.getVertexCount();
            for (Pair<String, VertexData> pair : mEntries) {
                if (pair.second.getVertexCount() != vertexCount) {
                    throw new RuntimeException("Can't build interleaved vertex buffer with non matching vertex counts");
                }
            }
        }

        /**
         * Calculate stride given all entries.
         *
         * @return The stride value.
         */
        private int calcStride() {
            int stride = 0;
            for (Pair<String, VertexData> pair : mEntries) {
                stride += pair.second.getVertexByteSize();
            }
            return stride;
        }

        /**
         * Create a list of attribute pointers based on entries and stride value.
         *
         * @param stride Stride value.
         * @return A list of attribute pointers.
         */
        private List<AttributePointer> createPointers(int stride) {
            List<AttributePointer> pointers = new ArrayList<>();
            int offset = 0;
            for (Pair<String, VertexData> pair : mEntries) {
                String name = pair.first;
                VertexData data = pair.second;
                pointers.add(new BufferPointer(name, data, stride, offset));
                offset += data.getVertexByteSize();
            }
            return pointers;
        }
    }

    /**
     * List of attribute pointers.
     */
    private final List<AttributePointer> mPointers;

    /**
     * The attribute data.
     */
    private final BufferResource mData;

    /**
     * Constructor.
     *
     * @param data     The attribute data.
     * @param pointers A list of attribute pointers.
     */
    public InterleavedVertexBuffer(BufferResource data, List<AttributePointer> pointers) {
        mData = data;
        mPointers = pointers;
    }

    @Override
    public void release(BackendContext context) {
        mData.release(context);
    }

    @Override
    public void create(BackendContext context) {
        mData.create(context);
    }

    @Override
    public boolean isCreated() {
        return mData.isCreated();
    }

    @Override
    public int getId() {
        return mData.getId();
    }

    @Override
    public Buffer getBuffer() {
        return mData.getBuffer();
    }

    @Override
    public int getTotalByteCount() {
        return mData.getTotalByteCount();
    }

    @Override
    public int getBufferUsage() {
        return mData.getBufferUsage();
    }

    @Override
    public int getAttributePointerCount() {
        return mPointers.size();
    }

    @Override
    public AttributePointer getAttributePointer(int index) {
        return mPointers.get(index);
    }
}

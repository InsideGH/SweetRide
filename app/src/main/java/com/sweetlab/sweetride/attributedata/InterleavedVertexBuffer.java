package com.sweetlab.sweetride.attributedata;

import android.util.Pair;

import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.resource.BufferResource;

import java.util.ArrayList;
import java.util.List;

/**
 * The buffer resource holds one attribute data and many attribute pointers, aka an
 * interleaved vertex buffer.
 */
public class InterleavedVertexBuffer implements BufferResource {
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
         * @return
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
            List<AttributePointer> pointers = createPointers(mEntries, calcStride(mEntries));
            InterleavedData data = new InterleavedData(mEntries, mBufferUsage);
            return new InterleavedVertexBuffer(data, pointers);
        }

        /**
         * Calculate stride given all entries.
         *
         * @param entries List of entries.
         * @return The stride value.
         */
        private static int calcStride(List<Pair<String, VertexData>> entries) {
            int stride = 0;
            for (Pair<String, VertexData> pair : entries) {
                stride += pair.second.getVertexByteSize();
            }
            return stride;
        }

        /**
         * Create a list of attribute pointers based on entries and stride value.
         *
         * @param entries List of entries.
         * @param stride  Stride value.
         * @return A list of attribute pointers.
         */
        private static List<AttributePointer> createPointers(List<Pair<String, VertexData>> entries, int stride) {
            List<AttributePointer> pointers = new ArrayList<>();
            int offset = 0;
            for (Pair<String, VertexData> pair : entries) {
                String name = pair.first;
                VertexData data = pair.second;
                pointers.add(new InterleavedPointer(name, data, stride, offset));
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
    private final AttributeData mData;

    /**
     * Constructor.
     *
     * @param data     The attribute data.
     * @param pointers A list of attribute pointers.
     */
    public InterleavedVertexBuffer(AttributeData data, List<AttributePointer> pointers) {
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

    /**
     * Get the attribute data.
     *
     * @return The attribute data.
     */
    public AttributeData getAttributeData() {
        return mData;
    }

    /**
     * Get number of attribute pointers.
     *
     * @return Number of attribute pointers.
     */
    public int getAttributePointerCount() {
        return mPointers.size();
    }

    /**
     * Get attribute pointer at index.
     *
     * @param index The index to get from.
     * @return The attribute pointer.
     */
    public AttributePointer getAttributePointer(int index) {
        return mPointers.get(index);
    }
}

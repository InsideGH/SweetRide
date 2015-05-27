package com.sweetlab.sweetride.attributedata;

import android.util.Pair;

import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.resource.BufferResource;
import com.sweetlab.sweetride.util.Util;

import java.nio.Buffer;
import java.util.List;

/**
 * Interleaved vertex data used for shader program attributes.
 */
public class InterleavedData implements BufferResource {
    /**
     * Buffer of interleaved data.
     */
    private final Buffer mBuffer;

    /**
     * Total byte count of data.
     */
    private final int mTotalByteCount;

    /**
     * The buffer usage hint.
     */
    private final int mBufferUsage;

    /**
     * The buffer 'name'/id.
     */
    private int mBufferId = INVALID_BUFFER_ID;

    /**
     * Constructor.
     *
     * @param entryList   List of entries consisting of shader attribute name and vertex data.
     * @param bufferUsage Buffer usage hint for this interleaved data.
     */
    public InterleavedData(List<Pair<String, VertexData>> entryList, int bufferUsage) {
        int elementCount = 0;
        int byteCount = 0;
        for (Pair<String, VertexData> entry : entryList) {
            elementCount += entry.second.getData().length;
            byteCount += entry.second.getTotalByteCount();
        }

        /**
         * The following non interleaved vertex data ->
         *   0     1     2     3  (vertex number)
         *  xyz,  xyz,  xyz,  xyz (entry 0)
         * rgba, rgba, rgba, rgba (entry 1)
         *  st ,  st ,  st ,  st  (entry 2)
         *
         *  will be interleaved into this ->
         *  (0 xyz) (0 rgba) (0 st) (1 xyz) (1 rgba) (1 st) ...
         */
        float[] data = new float[elementCount];
        int pos = 0;
        int entryCount = entryList.size();
        int vertexCount = entryList.get(0).second.getVertexCount();
        /**
         * For each vertex (0, 1, 2, 3)
         */
        for (int i = 0; i < vertexCount; i++) {
            /**
             * For each entry (entry0, entry1 entry2)
             */
            for (int j = 0; j < entryCount; j++) {
                VertexData vertexData = entryList.get(j).second;
                float[] vertexArray = vertexData.getData();
                int vertexSize = vertexData.getVertexSize();
                /**
                 * For each element xyz/rgba/st
                 */
                for (int k = 0; k < vertexSize; k++) {
                    data[pos++] = vertexArray[i * vertexSize + k];
                }
            }
        }

        mBufferUsage = bufferUsage;
        mTotalByteCount = byteCount;
        mBuffer = Util.createBuffer(data, mTotalByteCount);
    }

    @Override
    public int getBufferId() {
        return mBufferId;
    }

    @Override
    public Buffer getData() {
        return mBuffer;
    }

    @Override
    public int getTotalByteCount() {
        return mTotalByteCount;
    }

    @Override
    public int getBufferUsage() {
        return mBufferUsage;
    }

    @Override
    public void create(BackendContext context) {
        mBufferId = context.getResourceManager().generateBuffer();
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
}

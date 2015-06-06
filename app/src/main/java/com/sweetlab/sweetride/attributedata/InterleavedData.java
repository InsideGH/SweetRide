package com.sweetlab.sweetride.attributedata;

import android.util.Pair;

import com.sweetlab.sweetride.action.Action;
import com.sweetlab.sweetride.action.ActionId;
import com.sweetlab.sweetride.action.HandleThread;
import com.sweetlab.sweetride.action.NoHandleNotifier;
import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.context.BufferUsage;
import com.sweetlab.sweetride.context.ResourceManager;
import com.sweetlab.sweetride.resource.BufferResource;
import com.sweetlab.sweetride.util.Util;

import java.nio.Buffer;
import java.util.List;

/**
 * Interleaved vertex data used for shader program attributes.
 */
public class InterleavedData extends NoHandleNotifier implements BufferResource {
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
    private final BufferUsage mBufferUsage;

    /**
     * The buffer 'name'/id.
     */
    private int mBufferId = ResourceManager.INVALID_BUFFER_ID;

    /**
     * Constructor.
     *
     * @param entryList   List of entries consisting of shader attribute name and vertex data.
     * @param bufferUsage Buffer usage hint for this interleaved data.
     */
    public InterleavedData(List<Pair<String, VertexData>> entryList, BufferUsage bufferUsage) {
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
        addAction(new Action(this, ActionId.INTERLEAVED_BUFFER_CREATE, HandleThread.GL));
        addAction(new Action(this, ActionId.INTERLEAVED_BUFFER_LOAD, HandleThread.GL));
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
        return mTotalByteCount;
    }

    @Override
    public BufferUsage getBufferUsage() {
        return mBufferUsage;
    }

    @Override
    public void load(BackendContext context) {
        context.getArrayTarget().load(this);
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
    public void delete(BackendContext context) {
        context.getResourceManager().deleteBuffer(mBufferId);
        mBufferId = ResourceManager.INVALID_BUFFER_ID;
    }

    @Override
    public boolean handleAction(BackendContext context, Action action) {
        switch (action.getType()) {
            case INTERLEAVED_BUFFER_CREATE:
                create(context);
                return true;
            case INTERLEAVED_BUFFER_LOAD:
                load(context);
                return true;
            default:
                return false;
        }
    }
}

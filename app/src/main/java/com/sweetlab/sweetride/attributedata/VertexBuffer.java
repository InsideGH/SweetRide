package com.sweetlab.sweetride.attributedata;

import com.sweetlab.sweetride.action.Action;
import com.sweetlab.sweetride.action.ActionId;
import com.sweetlab.sweetride.action.HandleThread;
import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.context.BufferUsage;
import com.sweetlab.sweetride.context.ResourceManager;
import com.sweetlab.sweetride.resource.VertexBufferResource;
import com.sweetlab.sweetride.util.Util;

import java.nio.Buffer;

/**
 * A vertex buffer.
 */
public class VertexBuffer extends VertexBufferResource {
    /**
     * The buffer of data.
     */
    private final Buffer mBuffer;

    /**
     * The buffer usage hint.
     */
    private final BufferUsage mBufferUsage;

    /**
     * The vertex data.
     */
    private final VertexData mVertexData;

    /**
     * The buffer pointer.
     */
    private final BufferPointer mBufferPointer;

    /**
     * The buffer 'name'/id.
     */
    private int mBufferId = ResourceManager.INVALID_BUFFER_ID;

    /**
     * Constructor. Don't forget to create it to generate a buffer id.
     *
     * @param name        Name of attribute in shader program.
     * @param vertexData  Vertex data.
     * @param bufferUsage Buffer usage hint.
     */
    public VertexBuffer(String name, VertexData vertexData, BufferUsage bufferUsage) {
        mBufferPointer = new BufferPointer(name, vertexData, 0, 0);
        mBufferUsage = bufferUsage;
        mVertexData = vertexData;
        mBuffer = Util.createBuffer(vertexData.getData(), vertexData.getTotalByteCount());
        addAction(new Action(this, ActionId.VERTEX_BUFFER_CREATE, HandleThread.GL));
        addAction(new Action(this, ActionId.VERTEX_BUFFER_LOAD, HandleThread.GL));
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
        return mVertexData.getTotalByteCount();
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
    public boolean isCreated() {
        return mBufferId != ResourceManager.INVALID_BUFFER_ID;
    }

    @Override
    public void delete(BackendContext context) {
        context.getResourceManager().deleteBuffer(mBufferId);
        mBufferId = ResourceManager.INVALID_BUFFER_ID;
    }

    @Override
    public void create(BackendContext context) {
        mBufferId = context.getResourceManager().generateBuffer();
    }

    @Override
    public int getAttributePointerCount() {
        return 1;
    }

    @Override
    public AttributePointer getAttributePointer(int index) {
        return mBufferPointer;
    }

    @Override
    public boolean handleAction(BackendContext context, Action action) {
        switch (action.getType()) {
            case VERTEX_BUFFER_CREATE:
                create(context);
                return true;
            case VERTEX_BUFFER_LOAD:
                load(context);
                return true;
            default:
                return false;
        }
    }
}

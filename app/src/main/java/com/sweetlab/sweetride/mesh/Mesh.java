package com.sweetlab.sweetride.mesh;

import android.support.annotation.Nullable;

import com.sweetlab.sweetride.action.Action;
import com.sweetlab.sweetride.action.ActionId;
import com.sweetlab.sweetride.action.HandleThread;
import com.sweetlab.sweetride.action.NoHandleNotifier;
import com.sweetlab.sweetride.attributedata.IndicesBuffer;
import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.context.MeshDrawingMode;
import com.sweetlab.sweetride.resource.VertexBufferResource;

import java.util.ArrayList;
import java.util.List;

/**
 * Mesh is an abstraction that contains multiple vertex buffer resources and optional
 * indices buffer resource.
 */
public class Mesh extends NoHandleNotifier {
    /**
     * Indices reference has changed.
     */
    private final Action mIndicesChanged = new Action(this, ActionId.MESH_INDICES, HandleThread.MAIN);

    /**
     * Vertex buffers collection has changed.
     */
    private final Action mVertexBuffersChanged = new Action(this, ActionId.MESH_BUFFER, HandleThread.MAIN);

    /**
     * List of vertex buffers.
     */
    private final List<VertexBufferResource> mVertexBuffers = new ArrayList<>();

    /**
     * The drawing mode.
     */
    private final MeshDrawingMode mMode;

    /**
     * An optional indices buffer.
     */
    private IndicesBuffer mIndicesBuffer;

    /**
     * The vertex count.
     */
    private int mVertexCount;

    /**
     * The indices buffer reference used by GL thread.
     */
    private IndicesBuffer mIndicesBufferGL;

    /**
     * The vertex buffer collection reference used by GL thread.
     */
    private List<VertexBufferResource> mVertexBuffersGL = new ArrayList<>();

    /**
     * Constructor.
     *
     * @param mode Mode is the drawing mode.
     */
    public Mesh(MeshDrawingMode mode) {
        mMode = mode;
    }

    @Override
    public void handleAction(Action action) {
        switch (action.getType()) {
            case MESH_BUFFER:
                mVertexBuffersGL.clear();
                mVertexBuffersGL.addAll(mVertexBuffers);
                break;
            case MESH_INDICES:
                mIndicesBufferGL = mIndicesBuffer;
                break;
            default:
                throw new RuntimeException("wtf");
        }
    }

    /**
     * Set the indices buffer to use. Null allowed to not use indices.
     *
     * @param indicesBuffer The indices buffer.
     */
    public void setIndicesBuffer(@Nullable IndicesBuffer indicesBuffer) {
        if (mIndicesBuffer != null) {
            disconnectNotifier(mIndicesBuffer);
        }
        mIndicesBuffer = indicesBuffer;
        connectNotifier(mIndicesBuffer);
        addAction(mIndicesChanged);
    }

    /**
     * Add vertex buffer.
     *
     * @param vertexBuffer The vertex buffer.
     */
    public void addVertexBuffer(VertexBufferResource vertexBuffer) {
        if (mVertexBuffers.isEmpty()) {
            mVertexCount = vertexBuffer.getAttributePointer(0).getVertexCount();
        } else {
            int pointerCount = vertexBuffer.getAttributePointerCount();
            for (int i = 0; i < pointerCount; i++) {
                if (vertexBuffer.getAttributePointer(i).getVertexCount() != mVertexCount) {
                    throw new RuntimeException("Can't build mesh with vertex buffers of different size.");
                }
            }
        }
        mVertexBuffers.add(vertexBuffer);
        connectNotifier(vertexBuffer);
        addAction(mVertexBuffersChanged);
    }

    /**
     * Get the drawing mode.
     *
     * @return The drawing mode.
     */
    public MeshDrawingMode getMode() {
        return mMode;
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
     * Get number of vertex buffers.
     *
     * @return The count.
     */
    public int getVertexBufferCount() {
        return mVertexBuffers.size();
    }

    /**
     * Get vertex buffer at index.
     *
     * @param index The index to fetch from.
     * @return The vertex buffer.
     */
    public VertexBufferResource getVertexBuffer(int index) {
        return mVertexBuffers.get(index);
    }

    /**
     * Get the indices buffer.
     *
     * @return The indices buffer.
     */
    public IndicesBuffer getIndicesBuffer() {
        return mIndicesBuffer;
    }

    /**
     * Create the mesh.
     *
     * @param context Backend context.
     */
    public void create(BackendContext context) {
        if (mIndicesBufferGL != null && !mIndicesBufferGL.isCreated()) {
            mIndicesBufferGL.create(context);
        }
        for (VertexBufferResource resource : mVertexBuffersGL) {
            if (!resource.isCreated()) {
                resource.create(context);
            }
        }
    }

    /**
     * Load the mesh to gpu.
     *
     * @param context The backend context.
     */
    public void load(BackendContext context) {
        if (mIndicesBufferGL != null) {
            mIndicesBufferGL.load(context);
        }
        for (VertexBufferResource resource : mVertexBuffersGL) {
            resource.load(context);
        }
    }
}

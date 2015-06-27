package com.sweetlab.sweetride.mesh;

import android.support.annotation.Nullable;

import com.sweetlab.sweetride.action.Action;
import com.sweetlab.sweetride.action.GlobalActionId;
import com.sweetlab.sweetride.action.ActionThread;
import com.sweetlab.sweetride.action.NoHandleNotifier;
import com.sweetlab.sweetride.attributedata.IndicesBuffer;
import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.context.MeshDrawingMode;
import com.sweetlab.sweetride.intersect.BoundingBox;
import com.sweetlab.sweetride.resource.VertexBufferResource;

import java.util.ArrayList;
import java.util.List;

/**
 * Mesh is an abstraction that contains multiple vertex buffer resources and optional
 * indices buffer resource.
 */
public class Mesh extends NoHandleNotifier<GlobalActionId> {
    /**
     * Indices reference has changed.
     */
    private final Action<GlobalActionId> mIndicesChanged = new Action<>(this, GlobalActionId.MESH_INDICES, ActionThread.MAIN);

    /**
     * Vertex buffers collection has changed.
     */
    private final Action<GlobalActionId> mVertexBuffersChanged = new Action<>(this, GlobalActionId.MESH_BUFFER, ActionThread.MAIN);

    /**
     * List of vertex buffers.
     */
    private final List<VertexBufferResource> mVertexBuffers = new ArrayList<>();

    /**
     * The drawing mode.
     */
    private final MeshDrawingMode mMode;

    /**
     * The mesh GL domain.
     */
    private final BackendMesh mBackendMesh;

    /**
     * An optional indices buffer.
     */
    private IndicesBuffer mIndicesBuffer;

    /**
     * The vertex count.
     */
    private int mVertexCount;

    /**
     * Optional bounding box.
     */
    private BoundingBox mBoundingBox;

    /**
     * Constructor.
     *
     * @param mode Mode is the drawing mode.
     */
    public Mesh(MeshDrawingMode mode) {
        mMode = mode;
        mBackendMesh = new BackendMesh(mMode);
    }

    @Override
    public boolean handleAction(Action<GlobalActionId> action) {
        switch (action.getType()) {
            case MESH_BUFFER:
                mBackendMesh.setVertexBuffers(mVertexBuffers);
                return true;
            case MESH_INDICES:
                mBackendMesh.setIndicesBuffer(mIndicesBuffer);
                return true;
            default:
                return false;
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
     * Set the bounding box.
     *
     * @param box The bounding box or null.
     */
    public void setBoundingBox(@Nullable BoundingBox box) {
        mBoundingBox = box;
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
     * Get the bounding box.
     *
     * @return The bounding box.
     */
    @Nullable
    public BoundingBox getBoundingBox() {
        return mBoundingBox;
    }

    /**
     * Create the mesh.
     *
     * @param context Backend context.
     */
    public void create(BackendContext context) {
        mBackendMesh.create(context);
    }

    /**
     * Load the mesh to gpu.
     *
     * @param context The backend context.
     */
    public void load(BackendContext context) {
        mBackendMesh.load(context);
    }

    /**
     * Get the backend mesh.
     *
     * @return The backend mesh.
     */
    public BackendMesh getBackendMesh() {
        return mBackendMesh;
    }
}

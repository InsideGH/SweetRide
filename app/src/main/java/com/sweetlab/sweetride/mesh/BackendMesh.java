package com.sweetlab.sweetride.mesh;

import com.sweetlab.sweetride.attributedata.IndicesBuffer;
import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.context.MeshDrawingMode;
import com.sweetlab.sweetride.intersect.BoundingBox;
import com.sweetlab.sweetride.resource.VertexBufferResource;

import java.util.ArrayList;
import java.util.List;

/**
 * Mesh used in the backend.
 */
public class BackendMesh {
    /**
     * The vertex buffer collection references.
     */
    private final List<VertexBufferResource> mVertexBuffers = new ArrayList<>();

    /**
     * The bounding box.
     */
    private final BoundingBox mBoundingBox = new BoundingBox();

    /**
     * The drawing mode.
     */
    private final MeshDrawingMode mMode;

    /**
     * The indices buffer.
     */
    private IndicesBuffer mIndicesBuffer;

    /**
     * The vertex count.
     */
    private int mVertexCount;

    /**
     * Constructor.
     *
     * @param mode The drawing mode.
     */
    public BackendMesh(MeshDrawingMode mode) {
        mMode = mode;
    }

    /**
     * Set vertex buffers. Will copy the collection references.
     *
     * @param list List if vertex buffers.
     */
    public void setVertexBuffers(List<VertexBufferResource> list) {
        mVertexBuffers.clear();
        mVertexBuffers.addAll(list);
        mVertexCount = mVertexBuffers.get(0).getAttributePointer(0).getVertexCount();
    }

    /**
     * Set the indices buffer reference.
     *
     * @param indicesBuffer The indices buffer.
     */
    public void setIndicesBuffer(IndicesBuffer indicesBuffer) {
        mIndicesBuffer = indicesBuffer;
    }

    /**
     * Set bounding box.
     *
     * @param box Bounding box.
     */
    public void setBoundingBox(BoundingBox box) {
        mBoundingBox.set(box);
    }

    /**
     * Create the mesh.
     *
     * @param context Backend context.
     */
    public void create(BackendContext context) {
        if (mIndicesBuffer != null && !mIndicesBuffer.isCreated()) {
            mIndicesBuffer.create(context);
        }
        for (VertexBufferResource resource : mVertexBuffers) {
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
        if (mIndicesBuffer != null) {
            mIndicesBuffer.load(context);
        }
        for (VertexBufferResource resource : mVertexBuffers) {
            resource.load(context);
        }
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
    public BoundingBox getBoundingBox() {
        return mBoundingBox;
    }

    /**
     * Get number of vertices.
     *
     * @return Number of vertices.
     */
    public int getVertexCount() {
        return mVertexCount;
    }
}

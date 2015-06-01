package com.sweetlab.sweetride.mesh;

import android.support.annotation.Nullable;

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
public class Mesh {
    /**
     * List of vertex buffers.
     */
    private List<VertexBufferResource> mVertexBuffers = new ArrayList<>();

    /**
     * An optional indices buffer.
     */
    private IndicesBuffer mIndicesBuffer;

    /**
     * The vertex count.
     */
    private int mVertexCount;

    /**
     * The drawing mode.
     */
    private final MeshDrawingMode mMode;

    /**
     * Constructor.
     *
     * @param mode Mode is the drawing mode.
     */
    public Mesh(MeshDrawingMode mode) {
        mMode = mode;
    }

    /**
     * Set the indices buffer to use. Null allowed to not use indices.
     *
     * @param indicesBuffer The indices buffer.
     */
    public void setIndicesBuffer(@Nullable IndicesBuffer indicesBuffer) {
        mIndicesBuffer = indicesBuffer;
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
            context.getElementTarget().load(mIndicesBuffer);
        }
        for (VertexBufferResource resource : mVertexBuffers) {
            context.getArrayTarget().load(resource);
        }
    }
}

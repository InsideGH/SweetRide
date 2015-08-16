package com.sweetlab.sweetride.demo.game.terrain.vertices;

import com.sweetlab.sweetride.attributedata.VerticesData;

/**
 * Vertices grid data for a patch.
 */
public class PatchVerticesData {
    /**
     * The vertices data.
     */
    private final VerticesData mData;

    /**
     * Constructor. Creates a patch origo centered.
     *
     * @param size  Size of patch.
     * @param width The patch width in world space.
     * @param depth The patch depth in world space.
     */
    public PatchVerticesData(int size, float width, float depth) {
        final int vertexCount = (1 << size) + 1;
        final float stepX = width / (vertexCount - 1);
        final float stepZ = depth / (vertexCount - 1);
        final float right = width / 2;
        final float near = depth / 2;

        float[] vertices = new float[vertexCount * vertexCount * 3];
        int index = 0;
        for (int z = 0; z < vertexCount; z++) {
            for (int x = 0; x < vertexCount; x++) {
                vertices[index++] = right - x * stepX;
                vertices[index++] = 0;
                vertices[index++] = near - z * stepZ;
            }
        }

        mData = new VerticesData(vertices);
    }

    /**
     * Get the vertices data.
     *
     * @return
     */
    public VerticesData getData() {
        return mData;
    }
}

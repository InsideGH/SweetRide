package com.sweetlab.sweetride.demo.mesh;

import android.support.annotation.Nullable;

import com.sweetlab.sweetride.attributedata.IndicesBuffer;
import com.sweetlab.sweetride.attributedata.InterleavedVertexBuffer;
import com.sweetlab.sweetride.attributedata.TextureCoordData;
import com.sweetlab.sweetride.attributedata.VerticesData;
import com.sweetlab.sweetride.context.BufferUsage;
import com.sweetlab.sweetride.context.MeshDrawingMode;
import com.sweetlab.sweetride.intersect.BoundingBox;
import com.sweetlab.sweetride.mesh.Mesh;

/**
 * A quad mesh.
 */
public class QuadMesh extends Mesh {
    /**
     * Constructor.
     *
     * @param width       Width of mesh in model space.
     * @param height      Height if mesh in model space.
     * @param vertexName  Name of the vertices attribute in the shader program.
     * @param textureName Name of the texture attribute in the shader program or null.
     */
    public QuadMesh(float width, float height, String vertexName, @Nullable String textureName) {
        super(MeshDrawingMode.TRIANGLE_STRIP);

        /**
         * Create indices buffer.
         */
        short[] indices = new short[]{0, 1, 2, 3};
        setIndicesBuffer(new IndicesBuffer(indices, BufferUsage.STATIC));

        /**
         * Create vertex buffers.
         */
        InterleavedVertexBuffer.Builder builder = new InterleavedVertexBuffer.Builder(BufferUsage.STATIC);
        float[] vertices = createVertices(width, height);
        builder.add(vertexName, new VerticesData(vertices));

        /**
         * Set bounding box.
         */
        setBoundingBox(new BoundingBox(vertices, indices));

        if (textureName != null) {
            /**
             * Create texture coordinates.
             */
            builder.add(textureName, new TextureCoordData(createTextureCoordinates()));
        }

        addVertexBuffer(builder.build());
    }

    /**
     * Create vertices.
     *
     * @param width  Width of quad in model space.
     * @param height Height of quad in model space.
     * @return Array of vertices.
     */
    private float[] createVertices(float width, float height) {
        float halfWidth = width * 0.5f;
        float halfHeight = height * 0.5f;
        float[] data = new float[4 * 3];

        data[0] = -halfWidth;
        data[1] = -halfHeight;

        data[3] = +halfWidth;
        data[4] = -halfHeight;

        data[6] = -halfWidth;
        data[7] = +halfHeight;

        data[9] = +halfWidth;
        data[10] = +halfHeight;

        return data;
    }

    /**
     * Create flipped texture coordinates suitable for bitmap.
     *
     * @return Flipped texture coordinates suitable for bitmap.
     */
    private float[] createTextureCoordinates() {
        return new float[]{0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f};
    }
}

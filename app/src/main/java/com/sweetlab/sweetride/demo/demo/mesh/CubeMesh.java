package com.sweetlab.sweetride.demo.demo.mesh;

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
 * A cube mesh.
 */
public class CubeMesh extends Mesh {
    /**
     * Constructor.
     *
     * @param vertexName  Name of the vertices attribute in the shader program.
     * @param textureName Name of the texture attribute in the shader program or null.
     */
    public CubeMesh(String vertexName, @Nullable String textureName) {
        super(MeshDrawingMode.TRIANGLES);

        /**
         * Create indices buffer.
         */
        short[] indices = createIndices();
        setIndicesBuffer(new IndicesBuffer(indices, BufferUsage.STATIC));

        /**
         * Create vertex buffers.
         */
        InterleavedVertexBuffer.Builder builder = new InterleavedVertexBuffer.Builder(BufferUsage.STATIC);
        float[] vertices = createVertices();
        builder.add(vertexName, new VerticesData(vertices));

        /**
         * Set bounding box.
         */
        setBoundingBox(new BoundingBox(vertices, indices));

        if (textureName != null) {
            /**
             * Create texture coordinates.
             */
            float[] textureCoordinates = createTextureCoordinates();
            flip(textureCoordinates);
            builder.add(textureName, new TextureCoordData(textureCoordinates));
        }

        addVertexBuffer(builder.build());
    }

    private static float[] createTextureCoordinates() {
        return new float[]{0.0f, 0.0f, // FRONT
                1.0f, 0.0f, // FRONT
                0.0f, 1.0f, // FRONT
                1.0f, 1.0f, // FRONT

                0.0f, 0.0f, // RIGHT
                1.0f, 0.0f, // RIGHT
                0.0f, 1.0f, // RIGHT
                1.0f, 1.0f, // RIGHT

                0.0f, 0.0f, // BACK
                1.0f, 0.0f, // BACK
                0.0f, 1.0f, // BACK
                1.0f, 1.0f, // BACK

                0.0f, 0.0f, // LEFT
                1.0f, 0.0f, // LEFT
                0.0f, 1.0f, // LEFT
                1.0f, 1.0f, // LEFT

                0.0f, 0.0f, // TOP
                1.0f, 0.0f, // TOP
                0.0f, 1.0f, // TOP
                1.0f, 1.0f, // TOP

                0.0f, 0.0f, // BOTTOM
                1.0f, 0.0f, // BOTTOM
                0.0f, 1.0f, // BOTTOM
                1.0f, 1.0f, // BOTTOM
        };
    }

    private static short[] createIndices() {
        return new short[]{0, 1, 2, // FRONT
                2, 1, 3, // FRONT

                4, 5, 6, // RIGHT
                6, 5, 7, // RIGHT

                8, 9, 10, // BACK
                10, 9, 11, // BACK

                12, 13, 14, // LEFT
                14, 13, 15, // LEFT

                16, 17, 18, // TOP
                18, 17, 19, // TOP

                20, 21, 22, // BOTTOM
                22, 21, 23, // BOTTOM
        };
    }

    private static float[] createVertices() {
        return new float[]{-0.5f, -0.5f, 0.5f, // 0 FRONT
                0.5f, -0.5f, 0.5f, // 1
                -0.5f, 0.5f, 0.5f, // 2
                0.5f, 0.5f, 0.5f, // 3

                0.5f, -0.5f, 0.5f, // 4 RIGHT
                0.5f, -0.5f, -0.5f, // 5
                0.5f, 0.5f, 0.5f, // 6
                0.5f, 0.5f, -0.5f, // 7

                0.5f, -0.5f, -0.5f, // 8 BACK
                -0.5f, -0.5f, -0.5f, // 9
                0.5f, 0.5f, -0.5f, // 10
                -0.5f, 0.5f, -0.5f, // 11

                -0.5f, -0.5f, -0.5f, // 12 LEFT
                -0.5f, -0.5f, 0.5f, // 13
                -0.5f, 0.5f, -0.5f, // 14
                -0.5f, 0.5f, 0.5f, // 15

                -0.5f, 0.5f, 0.5f, // 16 TOP
                0.5f, 0.5f, 0.5f, // 17
                -0.5f, 0.5f, -0.5f, // 18
                0.5f, 0.5f, -0.5f, // 19

                -0.5f, -0.5f, -0.5f, // 20 BOTTOM
                0.5f, -0.5f, -0.5f, // 21
                -0.5f, -0.5f, 0.5f, // 22
                0.5f, -0.5f, 0.5f, // 23
        };
    }

    /**
     * Flip the texture coordinates around y axis.
     *
     * @param data Texture coordinates.
     */
    private void flip(float[] data) {
        if (data != null) {
            for (int i = 1; i < data.length; i += 2) {
                data[i] = 1 - data[i];
            }
        }
    }
}

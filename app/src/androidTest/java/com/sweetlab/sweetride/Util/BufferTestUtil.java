package com.sweetlab.sweetride.Util;

import android.opengl.GLES20;

import com.sweetlab.sweetride.attributedata.ColorData;
import com.sweetlab.sweetride.attributedata.InterleavedVertexBuffer;
import com.sweetlab.sweetride.attributedata.TextureCoordData;
import com.sweetlab.sweetride.attributedata.VertexBuffer;
import com.sweetlab.sweetride.attributedata.VerticesData;
import com.sweetlab.sweetride.context.BufferUsage;

/**
 * Collection of buffer related primitives.
 */
public class BufferTestUtil {

    /**
     * Create triangle vertices centered on screen.
     *
     * @return Vertex buffer.
     */
    public static VertexBuffer createCenteredTriangle() {
        return new VertexBuffer("a_Pos", new VerticesData(BufferTestUtil.createTriangleData(0.5f, 0.5f, 0, 0)), BufferUsage.STATIC);
    }

    /**
     * Create triangle vertices positioned left on screen.
     *
     * @return Vertex buffer.
     */
    public static VertexBuffer createLeftTriangle() {
        return new VertexBuffer("a_Pos", new VerticesData(BufferTestUtil.createTriangleData(0.5f, 0.5f, -0.5f, 0)), BufferUsage.STATIC);
    }

    /**
     * Create triangle vertices positioned right on screen.
     *
     * @return Vertex buffer.
     */
    public static VertexBuffer createRightTriangle() {
        return new VertexBuffer("a_Pos", new VerticesData(BufferTestUtil.createTriangleData(0.5f, 0.5f, 0.5f, 0)), BufferUsage.STATIC);
    }

    /**
     * Create triangle vertices positioned top on screen.
     *
     * @return Vertex buffer.
     */
    public static VertexBuffer createTopTriangle() {
        return new VertexBuffer("a_Pos", new VerticesData(BufferTestUtil.createTriangleData(0.5f, 0.5f, 0, 0.5f)), BufferUsage.STATIC);
    }

    /**
     * Create triangle vertices positioned bottom on screen.
     *
     * @return Vertex buffer.
     */
    public static VertexBuffer createBottomTriangle() {
        return new VertexBuffer("a_Pos", new VerticesData(BufferTestUtil.createTriangleData(0.5f, 0.5f, 0, -0.5f)), BufferUsage.STATIC);
    }

    /**
     * Create a buffer with colors.
     *
     * @return Buffer with colors.
     */
    public static VertexBuffer createColorBuffer() {
        return new VertexBuffer("a_Color", new ColorData(BufferTestUtil.createColors()), BufferUsage.STATIC);
    }

    /**
     * Create an interleaved vertex buffer with left triangle vertices and color attribute.
     *
     * @return The interleaved buffer.
     */
    public static InterleavedVertexBuffer createInterleavedLeftTriangleWithColor() {
        InterleavedVertexBuffer.Builder left = new InterleavedVertexBuffer.Builder(BufferUsage.STATIC);
        left.add("a_Pos", new VerticesData(BufferTestUtil.createTriangleData(0.5f, 0.5f, -0.5f, 0)));
        left.add("a_Color", new ColorData(BufferTestUtil.createColors()));
        return left.build();
    }

    /**
     * Create an interleaved vertex buffer with left triangle vertices and color attribute.
     *
     * @return The interleaved buffer.
     */
    public static InterleavedVertexBuffer createInterleavedRightTriangleWithColor() {
        InterleavedVertexBuffer.Builder right = new InterleavedVertexBuffer.Builder(BufferUsage.STATIC);
        right.add("a_Pos", new VerticesData(BufferTestUtil.createTriangleData(0.5f, 0.5f, 0.5f, 0)));
        right.add("a_Color", new ColorData(BufferTestUtil.createColors()));
        return right.build();
    }

    /**
     * Create interleaved vertex buffer with centered quad and one texture coordinates.
     *
     * @return The interleaved buffer.
     */
    public static InterleavedVertexBuffer createInterleavedQuadWithTextureCoords() {
        InterleavedVertexBuffer.Builder builder = new InterleavedVertexBuffer.Builder(BufferUsage.STATIC);
        builder.add("a_Pos", new VerticesData(BufferTestUtil.createQuadStrip(0.5f, 0.5f, 0, 0)));
        builder.add("a_texCoord", new TextureCoordData(BufferTestUtil.createQuadTextureCoords()));
        return builder.build();
    }

    /**
     * Create triangle vertex data to be drawn with array target.
     *
     * @param width  Width of the triangle.
     * @param height Height of the triangle.
     * @param transX Transition x value.
     * @param transY Transition y value.
     * @return Triangle vertices data.
     */
    public static float[] createTriangleData(float width, float height, float transX, float transY) {
        float[] data = new float[3 * 3];
        float w = width * 0.5f;
        float h = height * 0.5f;

        data[0] = -w + transX;
        data[1] = -h + transY;
        data[2] = 0;

        data[3] = +w + transX;
        data[4] = -h + transY;
        data[5] = 0;

        data[6] = 0 + transX;
        data[7] = +h + transY;
        data[8] = 0;

        return data;
    }

    /**
     * Create RGB color in left, right and top.
     *
     * @return Color data.
     */
    public static float[] createColors() {
        float[] data = new float[4 * 3];
        data[0] = 1f;
        data[1] = 0f;
        data[2] = 0f;
        data[3] = 1f;

        data[4] = 0f;
        data[5] = 1f;
        data[6] = 0f;
        data[7] = 1f;

        data[8] = 0f;
        data[9] = 0f;
        data[10] = 1f;
        data[11] = 1f;
        return data;
    }

    /**
     * Create triangle indices.
     *
     * @return Array of indices.
     */
    public static short[] createTriangleIndices() {
        return new short[]{
                0, 1, 2
        };
    }


    /**
     * Create vertices for a quad.
     *
     * @param width  Width of quad.
     * @param height Height of quad.
     * @param transX Any inbound translation x.
     * @param transY Any inbound translation y.
     * @return Array of vertices.
     */
    public static float[] createQuadStrip(float width, float height, float transX, float transY) {
        float[] data = new float[4 * 3];
        float w = width * 0.5f;
        float h = height * 0.5f;

        data[0] = -w + transX;
        data[1] = -h + transY;
        data[2] = 0;

        data[3] = +w + transX;
        data[4] = -h + transY;
        data[5] = 0;

        data[6] = -w + transX;
        data[7] = +h + transY;
        data[8] = 0;

        data[9] = +w + transX;
        data[10] = +h + transY;
        data[11] = 0;

        return data;
    }

    /**
     * Create flipped texture coordinates.
     *
     * @return Flipped texture coordinates.
     */
    public static float[] createQuadTextureCoords() {
        float[] tex = new float[]{0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f};
        return tex;
    }
}

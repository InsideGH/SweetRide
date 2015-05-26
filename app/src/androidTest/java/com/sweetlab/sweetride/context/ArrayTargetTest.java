package com.sweetlab.sweetride.context;

import android.opengl.GLES20;

import com.sweetlab.sweetride.attributedata.ColorData;
import com.sweetlab.sweetride.attributedata.VertexBuffer;
import com.sweetlab.sweetride.attributedata.VerticesData;
import com.sweetlab.sweetride.shader.Attribute;
import com.sweetlab.sweetride.shader.FragmentShader;
import com.sweetlab.sweetride.shader.ShaderProgram;
import com.sweetlab.sweetride.shader.VertexShader;
import com.sweetlab.sweetride.testframework.OpenGLTestCase;
import com.sweetlab.sweetride.testframework.ResultRunnable;

/**
 * Test array target.
 */
public class ArrayTargetTest extends OpenGLTestCase {
    /**
     * The simplest vertex source code.
     */
    private static final String NO_COLOR_VERTEX_CODE =
            "attribute vec4 a_Pos; \n" +
                    "void main() { " +
                    "    gl_Position = a_Pos;" +
                    "} ";

    /**
     * Color per vertex as well.
     */
    private static final String COLOR_VERTEX_CODE =
            "attribute vec4 a_Pos; \n" +
                    "attribute vec4 a_Color; \n" +
                    "varying vec4 v_Color; \n" +
                    "void main() { " +
                    "    v_Color = a_Color;" +
                    "    gl_Position = a_Pos;" +
                    "} ";

    /**
     * Red coloring fragment code.
     */
    private static final String COLOR_FRAGMENT_CODE =
            "precision mediump float;\n" +
                    "varying vec4 v_Color; \n" +
                    "void main() {\n" +
                    "\tgl_FragColor = v_Color;\n" +
                    "}";

    /**
     * Red coloring fragment code.
     */
    private static final String RED_FRAGMENT_CODE =
            "precision mediump float;\n" +
                    "void main() {\n" +
                    "\tgl_FragColor = vec4(1.0, 0.0, 0.0, 1.0);\n" +
                    "}";

    /**
     * Blue coloring fragment code.
     */
    private static final String BLUE_FRAGMENT_CODE =
            "precision mediump float;\n" +
                    "void main() {\n" +
                    "\tgl_FragColor = vec4(0.0, 0.0, 1.0, 1.0);\n" +
                    "}";

    /**
     * Backend context.
     */
    private BackendContext mContext;

    /**
     * Red, blue and color shader.
     */
    private ShaderProgram mRedShader;
    private ShaderProgram mBlueShader;
    private ShaderProgram mColorShader;

    /**
     * Various positions of a triangle.
     */
    private VertexBuffer mLeftTriangle;
    private VertexBuffer mRightTriangle;
    private VertexBuffer mTopTriangle;
    private VertexBuffer mBottomTriangle;
    private VertexBuffer mTopLeftTriangle;
    private VertexBuffer mTopRightTriangle;
    private VertexBuffer mBottomLeftTriangle;
    private VertexBuffer mBottomRightTriangle;
    private VertexBuffer mColorBuffer;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        /**
         * Create shader programs. No need for GL thread.
         */
        mRedShader = new ShaderProgram(new VertexShader(NO_COLOR_VERTEX_CODE), new FragmentShader(RED_FRAGMENT_CODE));
        mBlueShader = new ShaderProgram(new VertexShader(NO_COLOR_VERTEX_CODE), new FragmentShader(BLUE_FRAGMENT_CODE));
        mColorShader = new ShaderProgram(new VertexShader(COLOR_VERTEX_CODE), new FragmentShader(COLOR_FRAGMENT_CODE));

        /**
         * Create a triangle vertices buffers. No need for GL thread.
         */
        mLeftTriangle = new VertexBuffer("a_Pos", new VerticesData(createTriangleData(0.5f, 0.5f, -0.5f, 0)), GLES20.GL_STATIC_DRAW);
        mRightTriangle = new VertexBuffer("a_Pos", new VerticesData(createTriangleData(0.5f, 0.5f, 0.5f, 0)), GLES20.GL_STATIC_DRAW);
        mTopTriangle = new VertexBuffer("a_Pos", new VerticesData(createTriangleData(0.5f, 0.5f, 0, 0.5f)), GLES20.GL_STATIC_DRAW);
        mBottomTriangle = new VertexBuffer("a_Pos", new VerticesData(createTriangleData(0.5f, 0.5f, 0, -0.5f)), GLES20.GL_STATIC_DRAW);

        mTopLeftTriangle = new VertexBuffer("a_Pos", new VerticesData(createTriangleData(0.5f, 0.5f, -0.5f, 0.5f)), GLES20.GL_STATIC_DRAW);
        mTopRightTriangle = new VertexBuffer("a_Pos", new VerticesData(createTriangleData(0.5f, 0.5f, 0.5f, 0.5f)), GLES20.GL_STATIC_DRAW);
        mBottomLeftTriangle = new VertexBuffer("a_Pos", new VerticesData(createTriangleData(0.5f, 0.5f, -0.5f, -0.5f)), GLES20.GL_STATIC_DRAW);
        mBottomRightTriangle = new VertexBuffer("a_Pos", new VerticesData(createTriangleData(0.5f, 0.5f, 0.5f, -0.5f)), GLES20.GL_STATIC_DRAW);

        /**
         * Create a color vertex buffer. No need for GL thread.
         */
        mColorBuffer = new VertexBuffer("a_Color", new ColorData(createColors()), GLES20.GL_STATIC_DRAW);

        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                mContext = getBackendContext();

                /**
                 * Link shader programs.
                 */
                mRedShader.link(mContext);
                mBlueShader.link(mContext);
                mColorShader.link(mContext);

                /**
                 * Create vertex buffers (object).
                 */
                mLeftTriangle.create(mContext);
                mRightTriangle.create(mContext);
                mTopTriangle.create(mContext);
                mBottomTriangle.create(mContext);

                mTopLeftTriangle.create(mContext);
                mTopRightTriangle.create(mContext);
                mBottomLeftTriangle.create(mContext);
                mBottomRightTriangle.create(mContext);

                /**
                 * Create the color buffer (object).
                 */
                mColorBuffer.create(mContext);

                /**
                 * Load triangle vertices to gpu.
                 */
                mContext.getArrayTarget().load(mLeftTriangle);
                mContext.getArrayTarget().load(mRightTriangle);
                mContext.getArrayTarget().load(mTopTriangle);
                mContext.getArrayTarget().load(mBottomTriangle);

                mContext.getArrayTarget().load(mTopLeftTriangle);
                mContext.getArrayTarget().load(mTopRightTriangle);
                mContext.getArrayTarget().load(mBottomLeftTriangle);
                mContext.getArrayTarget().load(mBottomRightTriangle);

                /**
                 * Load color data to gpu.
                 */
                mContext.getArrayTarget().load(mColorBuffer);

                return null;
            }
        });
    }

    public void testDrawTriangle() throws Exception {
        runOnDrawFrame(new ResultRunnable() {
            @Override
            public Object run() {
                /**
                 * Clear screen.
                 */
                clearScreen();

                drawNonInterleaved(mContext, mRedShader, mLeftTriangle);
                drawNonInterleaved(mContext, mBlueShader, mTopTriangle);
                drawNonInterleaved(mContext, mRedShader, mRightTriangle);
                drawNonInterleaved(mContext, mBlueShader, mBottomTriangle);

                return null;
            }
        });
        sleepOnDrawFrame(2000);
    }

    public void testDrawTriangleWithColor() throws Exception {
        runOnDrawFrame(new ResultRunnable() {
            @Override
            public Object run() {
                /**
                 * Clear screen.
                 */
                clearScreen();

                /**
                 * This triangle should be smooth colored.
                 */
                drawNonInterleaved(mContext, mColorShader, mLeftTriangle, mColorBuffer);

                /**
                 * This triangle should be black since we haven't specified any color buffer and
                 * after a draw attributes are disabled.
                 */
                drawNonInterleaved(mContext, mColorShader, mTopTriangle);

                /**
                 * This shader doesn't know about colors, meaning that we wont find any
                 * attribute. It should be red.
                 */
                drawNonInterleaved(mContext, mRedShader, mRightTriangle, mColorBuffer);

                /**
                 * This triangle should be smooth colored.
                 */
                drawNonInterleaved(mContext, mColorShader, mBottomTriangle, mColorBuffer);

                return null;
            }
        });
        sleepOnDrawFrame(2000);
    }

    /**
     * Draw with a shader program and vertex buffer.
     *
     * @param context Backend context.
     * @param program Shader program.
     * @param buffers Vertex buffers.
     */
    private static void drawNonInterleaved(BackendContext context, ShaderProgram program, VertexBuffer... buffers) {
        int vertexCount = buffers[0].getVertexCount();

        for (VertexBuffer buffer : buffers) {
            if (vertexCount != buffer.getVertexCount()) {
                throw new RuntimeException("Trying to drawNonInterleaved using vertex buffers of different sizes");
            }
            Attribute attribute = program.getAttribute(buffer.getName());
            if (attribute != null) {
                context.getArrayTarget().enableAttribute(attribute, buffer);
            }
        }

        context.getState().useProgram(program);
        context.getArrayTarget().draw(GLES20.GL_TRIANGLES, 0, vertexCount);

        for (VertexBuffer buffer : buffers) {
            Attribute attribute = program.getAttribute(buffer.getName());
            if (attribute != null) {
                context.getArrayTarget().disableAttribute(attribute);
            }
        }
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
    private float[] createTriangleData(float width, float height, float transX, float transY) {
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
    private float[] createColors() {
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
}
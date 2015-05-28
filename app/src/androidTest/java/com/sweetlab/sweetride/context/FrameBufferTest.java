package com.sweetlab.sweetride.context;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.opengl.GLES20;

import com.sweetlab.sweetride.attributedata.AttributePointer;
import com.sweetlab.sweetride.attributedata.InterleavedVertexBuffer;
import com.sweetlab.sweetride.attributedata.TextureCoordData;
import com.sweetlab.sweetride.attributedata.VertexBuffer;
import com.sweetlab.sweetride.attributedata.VerticesData;
import com.sweetlab.sweetride.framebuffer.FrameBuffer;
import com.sweetlab.sweetride.resource.BufferResource;
import com.sweetlab.sweetride.resource.TextureResource;
import com.sweetlab.sweetride.shader.Attribute;
import com.sweetlab.sweetride.shader.FragmentShader;
import com.sweetlab.sweetride.shader.ShaderProgram;
import com.sweetlab.sweetride.shader.VertexShader;
import com.sweetlab.sweetride.testframework.OpenGLTestCase;
import com.sweetlab.sweetride.testframework.ResultRunnable;
import com.sweetlab.sweetride.texture.Empty2DTexture;
import com.sweetlab.sweetride.util.Util;

/**
 * Test rendering to a texture and then use the texture when rendering it to a quad on screen.
 */
public class FrameBufferTest extends OpenGLTestCase {

    /**
     * The simplest vertex source code.
     */
    private static final String TRIANGLE_VERTEX_SHADER =
            "attribute vec4 a_Pos; \n" +
                    "void main() { " +
                    "    gl_Position = a_Pos;" +
                    "} ";

    /**
     * Red coloring fragment code.
     */
    private static final String TRIANGLE_FRAGMENT_SHADER =
            "precision mediump float;\n" +
                    "void main() {\n" +
                    "\tgl_FragColor = vec4(1.0, 0.0, 0.0, 1.0);\n" +
                    "}";

    private static final String QUAD_VERTEX_SHADER =
            "attribute vec4 a_Pos; \n" +
                    "attribute vec2 a_texCoord;\n" +
                    "varying vec2 v_texCoord;\n" +
                    "void main() { " +
                    "    v_texCoord = a_texCoord;\n" +
                    "    gl_Position = a_Pos; " +
                    "} ";

    private static final String QUAD_FRAGMENT_SHADER =
            "precision mediump float;\n" +
                    "varying vec2 v_texCoord;\n" +
                    "uniform sampler2D s_texture;\n" +
                    "void main() {\n" +
                    "vec4 color = texture2D(s_texture, v_texCoord);\n" +
                    "gl_FragColor = color;\n" +
                    "}";

    /**
     * Backend context.
     */
    private BackendContext mContext;

    /**
     * Shader program to draw triangle.
     */
    private ShaderProgram mTriangleProgram;

    /**
     * Triangle vertex buffer.
     */
    private VertexBuffer mTriangleVertexBuffer;

    /**
     * Shader program to draw quad.
     */
    private ShaderProgram mQuadProgram;

    /**
     * Quad vertex buffer.
     */
    private InterleavedVertexBuffer mQuadVertexBuffer;

    /**
     * Render buffer.
     */
//    private RenderBuffer mRenderBuffer;

    /**
     * Frame buffer.
     */
    private FrameBuffer mFrameBuffer;

    /**
     * Texture to draw into.
     */
    private TextureResource mDestinationTexture;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        /**
         * Create triangle resources.
         */
        mTriangleProgram = new ShaderProgram(new VertexShader(TRIANGLE_VERTEX_SHADER), new FragmentShader(TRIANGLE_FRAGMENT_SHADER));
        mTriangleVertexBuffer = new VertexBuffer("a_Pos", new VerticesData(createTriangleData(0.5f, 0.5f, 0, 0)), GLES20.GL_STATIC_DRAW);

        /**
         * Create quad resources.
         */
        mQuadProgram = new ShaderProgram(new VertexShader(QUAD_VERTEX_SHADER), new FragmentShader(QUAD_FRAGMENT_SHADER));
        InterleavedVertexBuffer.Builder builder = new InterleavedVertexBuffer.Builder(GLES20.GL_STATIC_DRAW);
        builder.add("a_Pos", new VerticesData(createQuadStrip(0.5f, 0.5f, 0, 0)));
        builder.add("a_texCoord", new TextureCoordData(createQuadTextureCoords()));
        mQuadVertexBuffer = builder.build();

        /**
         * Create off-screen resources.
         */
        mDestinationTexture = new Empty2DTexture("s_texture", getSurfaceWidth(), getSurfaceHeight());
//        mDestinationTexture = new Texture2D("s_texture", createBitmap(Bitmap.Config.RGB_565));

//        mRenderBuffer = new RenderBuffer(GLES20.GL_DEPTH_COMPONENT16, mDestinationTexture.getWidth(), mDestinationTexture.getHeight());
        mFrameBuffer = new FrameBuffer();

        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                mContext = getBackendContext();

                /**
                 * Link shader programs.
                 */
                mTriangleProgram.create(mContext);
                mQuadProgram.create(mContext);

                /**
                 * Create vertex buffers (object).
                 */
                mTriangleVertexBuffer.create(mContext);
                mQuadVertexBuffer.create(mContext);

                /**
                 * Load triangle vertices to gpu.
                 */
                mContext.getArrayTarget().load(mTriangleVertexBuffer);
                mContext.getArrayTarget().load(mQuadVertexBuffer);

                /**
                 * Create the empty texture to draw into.
                 */
                mDestinationTexture.create(mContext);

                /**
                 * Load texture and set filter.
                 */
                mContext.getTextureUnitManager().getDefaultTextureUnit().getTexture2DTarget().load(mDestinationTexture);
                mContext.getTextureUnitManager().getDefaultTextureUnit().getTexture2DTarget().setFilter(mDestinationTexture, GLES20.GL_NEAREST, GLES20.GL_NEAREST);

                /**
                 * Create the render buffer in backend.
                 */
//                mRenderBuffer.create(mContext);

                /**
                 * Create the frame buffer in the backend.
                 */
                mFrameBuffer.create(mContext);


                assertFalse(Util.hasGlError());

                return null;
            }
        });
    }

    public void testRenderToFrameBuffer() {
        runOnDrawFrame(new ResultRunnable() {
            @Override
            public Object run() {

                /**
                 * Setup to render color to texture and depth to render buffer.
                 */
//                mContext.getRenderBufferTarget().enable(mRenderBuffer);
                mContext.getFrameBufferTarget().useFrameBuffer(mFrameBuffer);
                mContext.getFrameBufferTarget().setColorAttachment(mDestinationTexture);
//                mContext.getFrameBufferTarget().setDepthAttachment(mRenderBuffer);
                boolean isComplete = mContext.getFrameBufferTarget().checkIfComplete();
                assertTrue(isComplete);

                /**
                 * Clear frame buffer screen.
                 */
                clearScreen();

                /**
                 * Draw triangle to screen.
                 */
                drawNonInterleaved(mContext, mTriangleProgram, mTriangleVertexBuffer);

                /**
                 * Switch back to system window rendering.
                 */
                mContext.getFrameBufferTarget().useWindowFrameBuffer();

                /**
                 * Clear screen.
                 */
                clearScreen();

                /**
                 * This quad should be drawn centered with the texture that was previously drawn
                 * into a frame buffer texture. So the triangle should be smaller.
                 */
                drawArrayInterleaved(mContext, mQuadProgram, mQuadVertexBuffer, mDestinationTexture);

                return null;
            }
        });
        sleepOnDrawFrame(2000);
    }

    /**
     * Draw with a shader program and a interleaved vertex buffer.
     *
     * @param context      Backend context.
     * @param program      Shader program.
     * @param vertexBuffer Interleaved vertex buffer.
     * @param texture      Texture.
     */
    private static void drawArrayInterleaved(BackendContext context, ShaderProgram program, InterleavedVertexBuffer vertexBuffer, TextureResource texture) {
        BufferResource attributeData = vertexBuffer.getAttributeData();
        int pointerCount = vertexBuffer.getAttributePointerCount();

        for (int i = 0; i < pointerCount; i++) {
            AttributePointer pointer = vertexBuffer.getAttributePointer(i);
            Attribute attribute = program.getAttribute(pointer.getName());
            if (attribute != null) {
                context.getArrayTarget().enableAttribute(attribute, attributeData, pointer);
            }
        }

        context.getState().useProgram(program);

        TextureUnit textureUnit = context.getTextureUnitManager().takeTextureUnit();
        textureUnit.getTexture2DTarget().enable(program, texture);

        int vertexCount = vertexBuffer.getAttributePointer(0).getVertexCount();
        context.getArrayTarget().draw(GLES20.GL_TRIANGLE_STRIP, 0, vertexCount);

        textureUnit.getTexture2DTarget().disable(texture);
        context.getTextureUnitManager().returnTextureUnit(textureUnit);


        for (int i = 0; i < pointerCount; i++) {
            AttributePointer pointer = vertexBuffer.getAttributePointer(i);
            Attribute attribute = program.getAttribute(pointer.getName());
            if (attribute != null) {
                context.getArrayTarget().disableAttribute(attribute);
            }
        }
    }

    /**
     * Draw with a shader program and vertex buffers in a non-interleaved way.
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
                context.getArrayTarget().enableAttribute(attribute, buffer, buffer);
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
     * Create vertices for a quad.
     *
     * @param width  Width of quad.
     * @param height Height of quad.
     * @param transX Any inbound translation x.
     * @param transY Any inbound translation y.
     * @return Array of vertices.
     */
    private float[] createQuadStrip(float width, float height, float transX, float transY) {
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
    private float[] createQuadTextureCoords() {
        float[] tex = new float[]{0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f};
        return tex;
    }


    /**
     * Create a bitmap with some color.
     *
     * @param config Bitmap config.
     * @return A bitmap.
     */
    private Bitmap createBitmap(Bitmap.Config config) {
        return Bitmap.createBitmap(new int[]{Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW}, 2, 2, config);
    }
}

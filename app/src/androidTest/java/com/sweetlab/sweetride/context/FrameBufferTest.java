package com.sweetlab.sweetride.context;

import android.opengl.GLES20;

import com.sweetlab.sweetride.attributedata.InterleavedVertexBuffer;
import com.sweetlab.sweetride.attributedata.VertexBuffer;
import com.sweetlab.sweetride.context.Util.BufferTestUtil;
import com.sweetlab.sweetride.context.Util.DrawTestUtil;
import com.sweetlab.sweetride.context.Util.ProgramTestUtil;
import com.sweetlab.sweetride.framebuffer.FrameBuffer;
import com.sweetlab.sweetride.renderbuffer.RenderBuffer;
import com.sweetlab.sweetride.resource.TextureResource;
import com.sweetlab.sweetride.shader.ShaderProgram;
import com.sweetlab.sweetride.testframework.OpenGLTestCase;
import com.sweetlab.sweetride.testframework.ResultRunnable;
import com.sweetlab.sweetride.texture.Empty2DTexture;
import com.sweetlab.sweetride.util.Util;

/**
 * Test rendering to a texture and then use the texture when rendering it to a quad on screen.
 */
public class FrameBufferTest extends OpenGLTestCase {

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
    private RenderBuffer mRenderBuffer;

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
        mTriangleProgram = ProgramTestUtil.createNdcRed();
        mTriangleVertexBuffer = BufferTestUtil.createCenteredTriangle();

        /**
         * Create quad resources.
         */
        mQuadProgram = ProgramTestUtil.createNdcOneTexCoordOneTexture();
        mQuadVertexBuffer = BufferTestUtil.createInterleavedQuadWithTextureCoords();

        /**
         * Create off-screen resources.
         */
        mDestinationTexture = new Empty2DTexture("s_texture", getSurfaceWidth(), getSurfaceHeight());

        mRenderBuffer = new RenderBuffer(GLES20.GL_DEPTH_COMPONENT16, mDestinationTexture.getWidth(), mDestinationTexture.getHeight());
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
                mRenderBuffer.create(mContext);

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
                mContext.getRenderBufferTarget().enable(mRenderBuffer);
                mContext.getFrameBufferTarget().useFrameBuffer(mFrameBuffer);
                mContext.getFrameBufferTarget().setColorAttachment(mDestinationTexture);
                mContext.getFrameBufferTarget().setDepthAttachment(mRenderBuffer);
                boolean isComplete = mContext.getFrameBufferTarget().checkIfComplete();
                assertTrue(isComplete);

                /**
                 * Clear frame buffer screen.
                 */
                clearScreen(0.0f, 1.0f, 0.0f, 1.0f);

                /**
                 * Draw triangle to screen.
                 */
                DrawTestUtil.drawArraySeparateBuffers(mContext, mTriangleProgram, mTriangleVertexBuffer);

                /**
                 * Switch back to system window rendering.
                 */
                mContext.getFrameBufferTarget().useWindowFrameBuffer();

                /**
                 * Clear screen.
                 */
                clearScreen(0.5f, 0.5f, 0.5f, 1.0f);

                /**
                 * This quad should be drawn centered with the texture that was previously drawn
                 * into a frame buffer texture. So the triangle should be smaller.
                 */
                DrawTestUtil.drawArrayInterleavedBufferWithTexture(mContext, mQuadProgram, mQuadVertexBuffer, mDestinationTexture);

                return null;
            }
        });
        sleepOnDrawFrame(2000);
    }

}

package com.sweetlab.sweetride.framebuffer;

import android.opengl.GLES20;

import com.sweetlab.sweetride.Util.BackendRenderSettingsUtil;
import com.sweetlab.sweetride.Util.BufferTestUtil;
import com.sweetlab.sweetride.Util.DrawTestUtil;
import com.sweetlab.sweetride.Util.ProgramTestUtil;
import com.sweetlab.sweetride.Util.Verify;
import com.sweetlab.sweetride.attributedata.InterleavedVertexBuffer;
import com.sweetlab.sweetride.attributedata.VertexBuffer;
import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.context.MagFilter;
import com.sweetlab.sweetride.context.MinFilter;
import com.sweetlab.sweetride.renderbuffer.RenderBuffer;
import com.sweetlab.sweetride.resource.TextureResource;
import com.sweetlab.sweetride.shader.ShaderProgram;
import com.sweetlab.sweetride.testframework.OpenGLTestCase;
import com.sweetlab.sweetride.testframework.ResultRunnable;
import com.sweetlab.sweetride.texture.Empty2DTexture;
import com.sweetlab.sweetride.util.Util;

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
        mDestinationTexture = new Empty2DTexture("s_texture", getSurfaceWidth(), getSurfaceHeight(), MinFilter.NEAREST, MagFilter.NEAREST);

        mRenderBuffer = new RenderBuffer(GLES20.GL_DEPTH_COMPONENT16, mDestinationTexture.getWidth(), mDestinationTexture.getHeight());
        mFrameBuffer = new FrameBuffer();

        setTestInfo("Frame buffer with backend");

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
                mTriangleVertexBuffer.load(mContext);
                mQuadVertexBuffer.load(mContext);

                /**
                 * Create the empty texture to draw into.
                 */
                mDestinationTexture.create(mContext);

                /**
                 * Load texture and set filter.
                 */
                mDestinationTexture.load(mContext);

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

    public void testFrameBufferTest() {
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
                mContext.getRenderState().useSettings(BackendRenderSettingsUtil.getDefaultGreen(getSurfaceWidth(), getSurfaceHeight())).clear();

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
                mContext.getRenderState().useSettings(BackendRenderSettingsUtil.getDefaultGrey(getSurfaceWidth(), getSurfaceHeight())).clear();

                /**
                 * This quad should be drawn centered with the texture that was previously drawn
                 * into a frame buffer texture. So the triangle should be smaller.
                 */
                DrawTestUtil.drawArrayInterleavedBufferWithTexture(mContext, mQuadProgram, mQuadVertexBuffer, mDestinationTexture);

                return null;
            }
        });
        sleepOnDrawFrame(Verify.TERMINATE_TIME);
    }

}

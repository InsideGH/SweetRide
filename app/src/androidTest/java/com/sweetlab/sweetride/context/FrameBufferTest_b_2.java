package com.sweetlab.sweetride.context;

import android.opengl.GLES20;

import com.sweetlab.sweetride.context.Util.ActionHelper;
import com.sweetlab.sweetride.context.Util.BufferTestUtil;
import com.sweetlab.sweetride.context.Util.ProgramTestUtil;
import com.sweetlab.sweetride.framebuffer.FrameBuffer;
import com.sweetlab.sweetride.geometry.Geometry;
import com.sweetlab.sweetride.material.Material;
import com.sweetlab.sweetride.mesh.Mesh;
import com.sweetlab.sweetride.renderbuffer.RenderBuffer;
import com.sweetlab.sweetride.resource.TextureResource;
import com.sweetlab.sweetride.testframework.OpenGLTestCase;
import com.sweetlab.sweetride.testframework.ResultRunnable;
import com.sweetlab.sweetride.texture.Empty2DTexture;

/**
 * Test rendering to a texture and then use the texture when rendering it to a quad on screen.
 * Using material and mesh.
 */
public class FrameBufferTest_b_2 extends OpenGLTestCase {

    /**
     * Backend context.
     */
    private BackendContext mContext;

    /**
     * The geometries.
     */
    private Geometry mTriangle = new Geometry();
    private Geometry mQuad = new Geometry();

    /**
     * Frame buffer.
     */
    private FrameBuffer mFrameBuffer;

    /**
     * Render buffer.
     */
    private RenderBuffer mRenderBuffer;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Material material;
        Mesh mesh;

        /**
         * Create the triangle material and mesh. Simple plain red triangle.
         */
        material = new Material();
        material.setShaderProgram(ProgramTestUtil.createNdcRed());
        mTriangle.setMaterial(material);
        mesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mesh.addVertexBuffer(BufferTestUtil.createCenteredTriangle());
        mTriangle.setMesh(mesh);
        ActionHelper.handleMainThreadActions(mTriangle);

        /**
         * Create the quad material and mesh. It's a quad triangle strip with a texture.
         */
        material = new Material();
        material.setShaderProgram(ProgramTestUtil.createNdcOneTexCoordOneTexture());

        TextureResource texture = new Empty2DTexture("s_texture", getSurfaceWidth(), getSurfaceHeight(), MinFilter.NEAREST, MagFilter.NEAREST);

        material.addTexture(texture);
        mQuad.setMaterial(material);

        mesh = new Mesh(MeshDrawingMode.TRIANGLE_STRIP);
        mesh.addVertexBuffer(BufferTestUtil.createInterleavedQuadWithTextureCoords());
        mQuad.setMesh(mesh);
        ActionHelper.handleMainThreadActions(mQuad);

        /**
         * Create a frame buffer.
         */
        mFrameBuffer = new FrameBuffer();
        mRenderBuffer = new RenderBuffer(GLES20.GL_DEPTH_COMPONENT16, texture.getWidth(), texture.getHeight());

        setTestInfo("Frame buffer with geometry");

        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                mContext = getBackendContext();

                ActionHelper.handleGLThreadActions(mTriangle, mContext);
                ActionHelper.handleGLThreadActions(mQuad, mContext);
                ActionHelper.handleGLThreadActions(mFrameBuffer, mContext);
                ActionHelper.handleGLThreadActions(mRenderBuffer, mContext);

                assertFalse(mTriangle.hasActions());
                assertFalse(mQuad.hasActions());
                assertFalse(mFrameBuffer.hasActions());
                assertFalse(mRenderBuffer.hasActions());

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
                mContext.getFrameBufferTarget().setColorAttachment(mQuad.getMaterial().getTexture(0));
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
                mTriangle.draw(mContext);

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
                mQuad.draw(mContext);

                return null;
            }
        });
        sleepOnDrawFrame(2000);
    }

}

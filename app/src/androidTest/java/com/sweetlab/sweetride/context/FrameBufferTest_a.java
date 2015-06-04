package com.sweetlab.sweetride.context;

import com.sweetlab.sweetride.context.Util.ActionHelper;
import com.sweetlab.sweetride.context.Util.BufferTestUtil;
import com.sweetlab.sweetride.context.Util.DrawTestUtil;
import com.sweetlab.sweetride.context.Util.ProgramTestUtil;
import com.sweetlab.sweetride.framebuffer.FrameBuffer;
import com.sweetlab.sweetride.material.Material;
import com.sweetlab.sweetride.mesh.Mesh;
import com.sweetlab.sweetride.resource.TextureResource;
import com.sweetlab.sweetride.testframework.OpenGLTestCase;
import com.sweetlab.sweetride.testframework.ResultRunnable;
import com.sweetlab.sweetride.texture.Empty2DTexture;

/**
 * Test rendering to a texture and then use the texture when rendering it to a quad on screen.
 * Using material and mesh.
 */
public class FrameBufferTest_a extends OpenGLTestCase {
    /**
     * Backend context.
     */
    private BackendContext mContext;

    private Material mTriangleMaterial;
    private Mesh mTriangleMesh;

    private Material mQuadMaterial;
    private Mesh mQuadMesh;

    /**
     * Frame buffer.
     */
    private FrameBuffer mFrameBuffer;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        /**
         * Create the triangle material and mesh. Simple plain red triangle.
         */
        mTriangleMaterial = new Material();
        mTriangleMaterial.setShaderProgram(ProgramTestUtil.createNdcRed());
        ActionHelper.handleMainThreadActions(mTriangleMaterial);

        mTriangleMesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mTriangleMesh.addVertexBuffer(BufferTestUtil.createCenteredTriangle());
        ActionHelper.handleMainThreadActions(mTriangleMesh);

        /**
         * Create the quad material and mesh. It's a quad triangle strip with a texture.
         */
        mQuadMaterial = new Material();
        mQuadMaterial.setShaderProgram(ProgramTestUtil.createNdcOneTexCoordOneTexture());
        TextureResource mDestinationTexture = new Empty2DTexture("s_texture", getSurfaceWidth(), getSurfaceHeight(), MinFilter.NEAREST, MagFilter.NEAREST);
        mQuadMaterial.addTexture(mDestinationTexture);
        ActionHelper.handleMainThreadActions(mQuadMaterial);

        mQuadMesh = new Mesh(MeshDrawingMode.TRIANGLE_STRIP);
        mQuadMesh.addVertexBuffer(BufferTestUtil.createInterleavedQuadWithTextureCoords());
        ActionHelper.handleMainThreadActions(mQuadMesh);

        /**
         * Create a frame buffer.
         */
        mFrameBuffer = new FrameBuffer();

        setTestInfo("Frame buffer with mesh and material");

        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                mContext = getBackendContext();

                /**
                 * Create materials.
                 */
                mTriangleMaterial.create(mContext);
                mQuadMaterial.create(mContext);

                /**
                 * Create meshes.
                 */
                mTriangleMesh.create(mContext);
                mQuadMesh.create(mContext);

                /**
                 * Load meshes.
                 */
                mTriangleMesh.load(mContext);
                mQuadMesh.load(mContext);

                /**
                 * Load materials
                 */
                mTriangleMaterial.load(mContext);
                mQuadMaterial.load(mContext);

                /**
                 * Create the frame buffer in the backend.
                 */
                mFrameBuffer.create(mContext);
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
                mContext.getFrameBufferTarget().useFrameBuffer(mFrameBuffer);

                mContext.getFrameBufferTarget().setColorAttachment(mQuadMaterial.getTexture(0));
                boolean isComplete = mContext.getFrameBufferTarget().checkIfComplete();
                assertTrue(isComplete);

                /**
                 * Clear frame buffer screen.
                 */
                clearScreen(0.0f, 1.0f, 0.0f, 1.0f);

                /**
                 * Draw triangle to screen.
                 */
                DrawTestUtil.drawUsingMaterialAndMesh(mContext, mTriangleMaterial, mTriangleMesh);

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
                DrawTestUtil.drawUsingMaterialAndMesh(mContext, mQuadMaterial, mQuadMesh);

                return null;
            }
        });
        sleepOnDrawFrame(2000);
    }

}

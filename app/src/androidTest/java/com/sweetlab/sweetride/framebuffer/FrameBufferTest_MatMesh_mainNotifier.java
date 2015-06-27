package com.sweetlab.sweetride.framebuffer;

import com.sweetlab.sweetride.Util.BackendRenderSettingsUtil;
import com.sweetlab.sweetride.Util.BufferTestUtil;
import com.sweetlab.sweetride.Util.DrawTestUtil;
import com.sweetlab.sweetride.Util.ProgramTestUtil;
import com.sweetlab.sweetride.Util.Verify;
import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.context.MagFilter;
import com.sweetlab.sweetride.context.MeshDrawingMode;
import com.sweetlab.sweetride.context.MinFilter;
import com.sweetlab.sweetride.engine.FrontEndActionHandler;
import com.sweetlab.sweetride.material.Material;
import com.sweetlab.sweetride.mesh.Mesh;
import com.sweetlab.sweetride.resource.TextureResource;
import com.sweetlab.sweetride.testframework.OpenGLTestCase;
import com.sweetlab.sweetride.testframework.ResultRunnable;
import com.sweetlab.sweetride.texture.Empty2DTexture;

public class FrameBufferTest_MatMesh_mainNotifier extends OpenGLTestCase {
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

    /**
     * Front end action handler.
     */
    FrontEndActionHandler mActionHandler = new FrontEndActionHandler();

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        /**
         * Create the triangle material and mesh. Simple plain red triangle.
         */
        mTriangleMaterial = new Material();
        mTriangleMaterial.setShaderProgram(ProgramTestUtil.createNdcRed());
        mActionHandler.handleActions(mTriangleMaterial);

        mTriangleMesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mTriangleMesh.addVertexBuffer(BufferTestUtil.createCenteredTriangle());
        mActionHandler.handleActions(mTriangleMesh);

        /**
         * Create the quad material and mesh. It's a quad triangle strip with a texture.
         */
        mQuadMaterial = new Material();
        mQuadMaterial.setShaderProgram(ProgramTestUtil.createNdcOneTexCoordOneTexture());
        TextureResource mDestinationTexture = new Empty2DTexture("s_texture", getSurfaceWidth(), getSurfaceHeight(), MinFilter.NEAREST, MagFilter.NEAREST);
        mQuadMaterial.addTexture(mDestinationTexture);
        mActionHandler.handleActions(mQuadMaterial);

        mQuadMesh = new Mesh(MeshDrawingMode.TRIANGLE_STRIP);
        mQuadMesh.addVertexBuffer(BufferTestUtil.createInterleavedQuadWithTextureCoords());
        mActionHandler.handleActions(mQuadMesh);

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

    public void testFrameBufferTest_MatMesh_mainNotifier() {
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
                mContext.getRenderState().useSettings(BackendRenderSettingsUtil.getDefaultGreen(getSurfaceWidth(), getSurfaceHeight())).clear();

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
                mContext.getRenderState().useSettings(BackendRenderSettingsUtil.getDefaultGrey(getSurfaceWidth(), getSurfaceHeight())).clear();

                /**
                 * This quad should be drawn centered with the texture that was previously drawn
                 * into a frame buffer texture. So the triangle should be smaller.
                 */
                DrawTestUtil.drawUsingMaterialAndMesh(mContext, mQuadMaterial, mQuadMesh);

                return null;
            }
        });
        sleepOnDrawFrame(Verify.TERMINATE_TIME);
    }

}

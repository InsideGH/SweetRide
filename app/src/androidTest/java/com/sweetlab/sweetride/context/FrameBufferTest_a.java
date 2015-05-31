package com.sweetlab.sweetride.context;

import android.opengl.GLES20;

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
import com.sweetlab.sweetride.util.Util;

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

//    /**
//     * Shader program to draw triangle.
//     */
//    private ShaderProgram mTriangleProgram;
//
//    /**
//     * Triangle vertex buffer.
//     */
//    private VertexBuffer mTriangleVertexBuffer;
//
//    /**
//     * Shader program to draw quad.
//     */
//    private ShaderProgram mQuadProgram;
//
//    /**
//     * Quad vertex buffer.
//     */
//    private InterleavedVertexBuffer mQuadVertexBuffer;

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
         * Create the triangle material and mesh. Simple plain red triangle.
         */
        mTriangleMaterial = new Material();
        mTriangleMaterial.setShaderProgram(ProgramTestUtil.createNdcRed());

        mTriangleMesh = new Mesh(GLES20.GL_TRIANGLES);
        mTriangleMesh.addVertexBuffer(BufferTestUtil.createCenteredTriangle());

        /**
         * Create the quad material and mesh. It's a quad triangle strip with a texture.
         */
        mQuadMaterial = new Material();
        mQuadMaterial.setShaderProgram(ProgramTestUtil.createNdcOneTexCoordOneTexture());

        mDestinationTexture = new Empty2DTexture("s_texture", getSurfaceWidth(), getSurfaceHeight());
        mDestinationTexture.setFilter(GLES20.GL_NEAREST, GLES20.GL_NEAREST);
        mQuadMaterial.addTexture(mDestinationTexture);

        mQuadMesh = new Mesh(GLES20.GL_TRIANGLE_STRIP);
        mQuadMesh.addVertexBuffer(BufferTestUtil.createInterleavedQuadWithTextureCoords());

        /**
         * Create a frame buffer.
         */
        mFrameBuffer = new FrameBuffer();

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
                mContext.getFrameBufferTarget().useFrameBuffer(mFrameBuffer);
                mContext.getFrameBufferTarget().setColorAttachment(mDestinationTexture);
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

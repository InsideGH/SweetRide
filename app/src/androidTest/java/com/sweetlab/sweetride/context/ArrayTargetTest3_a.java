package com.sweetlab.sweetride.context;

import android.opengl.GLES20;

import com.sweetlab.sweetride.context.Util.BufferTestUtil;
import com.sweetlab.sweetride.context.Util.DrawTestUtil;
import com.sweetlab.sweetride.context.Util.ProgramTestUtil;
import com.sweetlab.sweetride.material.Material;
import com.sweetlab.sweetride.mesh.Mesh;
import com.sweetlab.sweetride.testframework.OpenGLTestCase;
import com.sweetlab.sweetride.testframework.ResultRunnable;

/**
 * Test array target. This test draws with
 * 1) single vertex buffer
 * 2) multiple vertex buffers
 * 3) interleaved vertex buffer.
 */
public class ArrayTargetTest3_a extends OpenGLTestCase {
    /**
     * Backend context.
     */
    private BackendContext mContext;

    /**
     * Create material for all four triangles.
     */
    private Material mLeftMaterial;
    private Material mRightMaterial;
    private Material mTopMaterial;
    private Material mBottomMaterial;

    /**
     * Create mesh for all four triangles.
     */
    private Mesh mLeftMesh;
    private Mesh mRightMesh;
    private Mesh mTopMesh;
    private Mesh mBottomMesh;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        /**
         * Create left resources. Complete setup with color shader and mesh with interleaved
         * vertices and color data.
         */
        mLeftMaterial = new Material();
        mLeftMaterial.setShaderProgram(ProgramTestUtil.createNdcColor());

        mLeftMesh = new Mesh(GLES20.GL_TRIANGLES);
        mLeftMesh.addVertexBuffer(BufferTestUtil.createInterleavedLeftTriangleWithColor());

        /**
         * Create top resources. Complete setup, with red-shader and mesh with vertices.
         */
        mTopMaterial = new Material();
        mTopMaterial.setShaderProgram(ProgramTestUtil.createNdcRed());

        mTopMesh = new Mesh(GLES20.GL_TRIANGLES);
        mTopMesh.addVertexBuffer(BufferTestUtil.createTopTriangle());

        /**
         * Create right resources. Complete setup with color shader and mesh with interleaved
         * vertices and color data.
         */
        mRightMaterial = new Material();
        mRightMaterial.setShaderProgram(ProgramTestUtil.createNdcColor());

        mRightMesh = new Mesh(GLES20.GL_TRIANGLES);
        mRightMesh.addVertexBuffer(BufferTestUtil.createInterleavedRightTriangleWithColor());

        /**
         * Create bottom resources. Complete setup with color shader and mesh with separate
         * vertex buffers for vertices and color attributes.
         */
        mBottomMaterial = new Material();
        mBottomMaterial.setShaderProgram(ProgramTestUtil.createNdcColor());

        mBottomMesh = new Mesh(GLES20.GL_TRIANGLES);
        mBottomMesh.addVertexBuffer(BufferTestUtil.createBottomTriangle());
        mBottomMesh.addVertexBuffer(BufferTestUtil.createColorBuffer());

        setTestInfo("smooth, red, smooth, smooth");

        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                mContext = getBackendContext();

                /**
                 * Create all materials.
                 */
                mLeftMaterial.create(mContext);
                mTopMaterial.create(mContext);
                mRightMaterial.create(mContext);
                mBottomMaterial.create(mContext);

                /**
                 * Create all meshes.
                 */
                mLeftMesh.create(mContext);
                mTopMesh.create(mContext);
                mRightMesh.create(mContext);
                mBottomMesh.create(mContext);

                /**
                 * Load all meshes. (materials doesn't need loading since there is no
                 * textures added.
                 */
                mLeftMesh.load(mContext);
                mRightMesh.load(mContext);
                mTopMesh.load(mContext);
                mBottomMesh.load(mContext);
                return null;
            }
        });
    }

    /**
     * Draw using both interleaved and non-interleaved vertex buffers.
     *
     * @throws Exception
     */
    public void testDrawInterleavedTriangle() throws Exception {
        runOnDrawFrame(new ResultRunnable() {
            @Override
            public Object run() {
                /**
                 * Clear screen.
                 */
                clearScreen(0.5f, 0.5f, 0.5f, 1.0f);

                /**
                 * This triangle should be smooth colored on left side.
                 */
                DrawTestUtil.drawUsingMaterialAndMesh(mContext, mLeftMaterial, mLeftMesh);

                /**
                 * This triangle should be red on top side.
                 */
                DrawTestUtil.drawUsingMaterialAndMesh(mContext, mTopMaterial, mTopMesh);

                /**
                 * This triangle should be smooth colored on right side.
                 */
                DrawTestUtil.drawUsingMaterialAndMesh(mContext, mRightMaterial, mRightMesh);

                /**
                 * This triangle should be smooth colored on bottom side.
                 */
                DrawTestUtil.drawUsingMaterialAndMesh(mContext, mBottomMaterial, mBottomMesh);

                return null;
            }
        });
        sleepOnDrawFrame(2000);
    }
}
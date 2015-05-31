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
 * Test array target. This test draws with just one vertex buffer using Material and Mesh.
 */
public class ArrayTargetTest4 extends OpenGLTestCase {
    /**
     * Backend context.
     */
    private BackendContext mContext;

    /**
     * Red, blue material.
     */
    private Material mBlueMaterial;
    private Material mRedMaterial;

    /**
     * The various positions of the mesh.
     */
    private Mesh mLeftMesh;
    private Mesh mRightMesh;
    private Mesh mTopMesh;
    private Mesh mBottomMesh;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        /**
         * Create materials. No need for GL thread.
         */
        mRedMaterial = new Material();
        mRedMaterial.setShaderProgram(ProgramTestUtil.createNdcRed());

        mBlueMaterial = new Material();
        mBlueMaterial.setShaderProgram(ProgramTestUtil.createNdcBlue());

        mLeftMesh = new Mesh(GLES20.GL_TRIANGLES);
        mLeftMesh.addVertexBuffer(BufferTestUtil.createLeftTriangle());

        mRightMesh = new Mesh(GLES20.GL_TRIANGLES);
        mRightMesh.addVertexBuffer(BufferTestUtil.createRightTriangle());

        mTopMesh = new Mesh(GLES20.GL_TRIANGLES);
        mTopMesh.addVertexBuffer(BufferTestUtil.createTopTriangle());

        mBottomMesh = new Mesh(GLES20.GL_TRIANGLES);
        mBottomMesh.addVertexBuffer(BufferTestUtil.createBottomTriangle());

        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                mContext = getBackendContext();

                mRedMaterial.create(mContext);
                mBlueMaterial.create(mContext);

                mLeftMesh.create(mContext);
                mRightMesh.create(mContext);
                mTopMesh.create(mContext);
                mBottomMesh.create(mContext);

                mLeftMesh.load(mContext);
                mRightMesh.load(mContext);
                mTopMesh.load(mContext);
                mBottomMesh.load(mContext);

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
                clearScreen(0.5f, 0.5f, 0.5f, 1.0f);

                /**
                 * Draw the triangles.
                 */
                DrawTestUtil.drawUsingMaterialAndMesh(mContext, mRedMaterial, mLeftMesh);
                DrawTestUtil.drawUsingMaterialAndMesh(mContext, mBlueMaterial, mTopMesh);
                DrawTestUtil.drawUsingMaterialAndMesh(mContext, mRedMaterial, mRightMesh);
                DrawTestUtil.drawUsingMaterialAndMesh(mContext, mBlueMaterial, mBottomMesh);

                return null;
            }
        });
        sleepOnDrawFrame(2000);
    }
}
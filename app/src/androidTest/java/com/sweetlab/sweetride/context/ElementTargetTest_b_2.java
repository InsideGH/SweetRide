package com.sweetlab.sweetride.context;

import android.opengl.GLES20;

import com.sweetlab.sweetride.attributedata.IndicesBuffer;
import com.sweetlab.sweetride.context.Util.ActionHelper;
import com.sweetlab.sweetride.context.Util.BufferTestUtil;
import com.sweetlab.sweetride.context.Util.ProgramTestUtil;
import com.sweetlab.sweetride.geometry.Geometry;
import com.sweetlab.sweetride.material.Material;
import com.sweetlab.sweetride.mesh.Mesh;
import com.sweetlab.sweetride.testframework.OpenGLTestCase;
import com.sweetlab.sweetride.testframework.ResultRunnable;

/**
 * Test to draw with indices.
 */
public class ElementTargetTest_b_2 extends OpenGLTestCase {
    /**
     * Backend context.
     */
    private BackendContext mContext;

    /**
     * The geometries.
     */
    private Geometry mLeftGeometry = new Geometry();
    private Geometry mTopGeometry = new Geometry();
    private Geometry mRightGeometry = new Geometry();
    private Geometry mBottomGeometry = new Geometry();

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        Material redMaterial = new Material();
        redMaterial.setShaderProgram(ProgramTestUtil.createNdcRed());

        Material blueMaterial = new Material();
        blueMaterial.setShaderProgram(ProgramTestUtil.createNdcBlue());

        IndicesBuffer indicesBuffer = new IndicesBuffer(BufferTestUtil.createTriangleIndices(), GLES20.GL_STATIC_DRAW);

        Mesh mesh;

        /**
         * Create materials. No need for GL thread.
         */
        mesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mesh.addVertexBuffer(BufferTestUtil.createLeftTriangle());
        mesh.setIndicesBuffer(indicesBuffer);
        mLeftGeometry.setMaterial(redMaterial);
        mLeftGeometry.setMesh(mesh);
        ActionHelper.handleMainThreadActions(mLeftGeometry);

        mesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mesh.addVertexBuffer(BufferTestUtil.createTopTriangle());
        mTopGeometry.setMaterial(blueMaterial);
        mTopGeometry.setMesh(mesh);
        ActionHelper.handleMainThreadActions(mTopGeometry);

        mesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mesh.addVertexBuffer(BufferTestUtil.createRightTriangle());
        mesh.setIndicesBuffer(indicesBuffer);
        mRightGeometry.setMaterial(redMaterial);
        mRightGeometry.setMesh(mesh);
        ActionHelper.handleMainThreadActions(mRightGeometry);

        mesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mesh.addVertexBuffer(BufferTestUtil.createBottomTriangle());
        mBottomGeometry.setMaterial(blueMaterial);
        mBottomGeometry.setMesh(mesh);
        ActionHelper.handleMainThreadActions(mBottomGeometry);

        setTestInfo("indices red, blue, red, blue geometry");

        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                mContext = getBackendContext();

                ActionHelper.handleGLThreadActions(mLeftGeometry, mContext);
                ActionHelper.handleGLThreadActions(mRightGeometry, mContext);
                ActionHelper.handleGLThreadActions(mTopGeometry, mContext);
                ActionHelper.handleGLThreadActions(mBottomGeometry, mContext);
                assertFalse(mLeftGeometry.hasActions());
                assertFalse(mRightGeometry.hasActions());
                assertFalse(mTopGeometry.hasActions());
                assertFalse(mBottomGeometry.hasActions());
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
                mLeftGeometry.draw(mContext);
                mRightGeometry.draw(mContext);
                mTopGeometry.draw(mContext);
                mBottomGeometry.draw(mContext);

                return null;
            }
        });
        sleepOnDrawFrame(2000);
    }
}
package com.sweetlab.sweetride.context;

import android.opengl.GLES20;

import com.sweetlab.sweetride.attributedata.IndicesBuffer;
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
public class ElementTargetTest_b extends OpenGLTestCase {
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

        mesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mesh.addVertexBuffer(BufferTestUtil.createTopTriangle());
        mTopGeometry.setMaterial(blueMaterial);
        mTopGeometry.setMesh(mesh);

        mesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mesh.addVertexBuffer(BufferTestUtil.createRightTriangle());
        mesh.setIndicesBuffer(indicesBuffer);
        mRightGeometry.setMaterial(redMaterial);
        mRightGeometry.setMesh(mesh);

        mesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mesh.addVertexBuffer(BufferTestUtil.createBottomTriangle());
        mBottomGeometry.setMaterial(blueMaterial);
        mBottomGeometry.setMesh(mesh);

        setTestInfo("red indices, blue, red indices, blue");

        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                mContext = getBackendContext();

                /**
                 * Create geometries in backend.
                 */
                mLeftGeometry.create(mContext);
                mRightGeometry.create(mContext);
                mTopGeometry.create(mContext);
                mBottomGeometry.create(mContext);

                /**
                 * Load geometries to gpu.
                 */
                mLeftGeometry.load(mContext);
                mRightGeometry.load(mContext);
                mTopGeometry.load(mContext);
                mBottomGeometry.load(mContext);

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
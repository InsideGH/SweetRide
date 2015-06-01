package com.sweetlab.sweetride.context;

import com.sweetlab.sweetride.context.Util.BufferTestUtil;
import com.sweetlab.sweetride.context.Util.ProgramTestUtil;
import com.sweetlab.sweetride.geometry.Geometry;
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
public class ArrayTargetTest3_b extends OpenGLTestCase {
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
        Material material;
        Mesh mesh;
        /**
         * Create left resources. Complete setup with color shader and mesh with interleaved
         * vertices and color data.
         */
        material = new Material();
        material.setShaderProgram(ProgramTestUtil.createNdcColor());
        mLeftGeometry.setMaterial(material);
        mesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mesh.addVertexBuffer(BufferTestUtil.createInterleavedLeftTriangleWithColor());
        mLeftGeometry.setMesh(mesh);

        /**
         * Create top resources. Complete setup, with red-shader and mesh with vertices.
         */
        material = new Material();
        material.setShaderProgram(ProgramTestUtil.createNdcRed());
        mTopGeometry.setMaterial(material);
        mesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mesh.addVertexBuffer(BufferTestUtil.createTopTriangle());
        mTopGeometry.setMesh(mesh);

        /**
         * Create right resources. Complete setup with color shader and mesh with interleaved
         * vertices and color data.
         */
        material = new Material();
        material.setShaderProgram(ProgramTestUtil.createNdcColor());
        mRightGeometry.setMaterial(material);
        mesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mesh.addVertexBuffer(BufferTestUtil.createInterleavedRightTriangleWithColor());
        mRightGeometry.setMesh(mesh);

        /**
         * Create bottom resources. Complete setup with color shader and mesh with separate
         * vertex buffers for vertices and color attributes.
         */
        material = new Material();
        material.setShaderProgram(ProgramTestUtil.createNdcColor());
        mBottomGeometry.setMaterial(material);
        mesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mesh.addVertexBuffer(BufferTestUtil.createBottomTriangle());
        mesh.addVertexBuffer(BufferTestUtil.createColorBuffer());
        mBottomGeometry.setMesh(mesh);

        setTestInfo("smooth, red, smooth, smooth");

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
                mLeftGeometry.draw(mContext);

                /**
                 * This triangle should be red on top side.
                 */
                mTopGeometry.draw(mContext);

                /**
                 * This triangle should be smooth colored on right side.
                 */
                mRightGeometry.draw(mContext);

                /**
                 * This triangle should be smooth colored on bottom side.
                 */
                mBottomGeometry.draw(mContext);

                return null;
            }
        });
        sleepOnDrawFrame(2000);
    }
}
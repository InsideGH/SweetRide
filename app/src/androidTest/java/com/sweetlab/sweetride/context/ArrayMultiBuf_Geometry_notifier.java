package com.sweetlab.sweetride.context;

import com.sweetlab.sweetride.context.Util.ActionHelper;
import com.sweetlab.sweetride.context.Util.BufferTestUtil;
import com.sweetlab.sweetride.context.Util.ProgramTestUtil;
import com.sweetlab.sweetride.geometry.Geometry;
import com.sweetlab.sweetride.material.Material;
import com.sweetlab.sweetride.mesh.Mesh;
import com.sweetlab.sweetride.testframework.OpenGLTestCase;
import com.sweetlab.sweetride.testframework.ResultRunnable;

public class ArrayMultiBuf_Geometry_notifier extends OpenGLTestCase {
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
         * Create left resources. Complete setup with shader that takes color attribute data
         * and vertex buffer that loads color data.
         */
        material = new Material();
        material.setShaderProgram(ProgramTestUtil.createNdcColor());
        mLeftGeometry.setMaterial(material);
        mesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mesh.addVertexBuffer(BufferTestUtil.createLeftTriangle());
        mesh.addVertexBuffer(BufferTestUtil.createColorBuffer());
        mLeftGeometry.setMesh(mesh);
        ActionHelper.handleMainThreadActions(mLeftGeometry);

        /**
         * Create top resources. Not complete setup, the actual color buffer is missing which
         * produces a black triangle.
         */
        material = new Material();
        material.setShaderProgram(ProgramTestUtil.createNdcColor());
        mTopGeometry.setMaterial(material);
        mesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mesh.addVertexBuffer(BufferTestUtil.createTopTriangle());
        mTopGeometry.setMesh(mesh);
        ActionHelper.handleMainThreadActions(mTopGeometry);

        /**
         * Create right resources. Misconstrued setup. Using a shader that just paints red but
         * adding vertex buffer with color buffer. The vertex buffer is ignored and the triangle
         * is painted red.
         */
        material = new Material();
        material.setShaderProgram(ProgramTestUtil.createNdcRed());
        mRightGeometry.setMaterial(material);
        mesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mesh.addVertexBuffer(BufferTestUtil.createRightTriangle());
        mesh.addVertexBuffer(BufferTestUtil.createColorBuffer());
        mRightGeometry.setMesh(mesh);
        ActionHelper.handleMainThreadActions(mRightGeometry);

        /**
         * Create bottom resources. Complete setup with shader that takes color attribute data
         * and vertex buffer that loads color data.
         */
        material = new Material();
        material.setShaderProgram(ProgramTestUtil.createNdcColor());
        mBottomGeometry.setMaterial(material);
        mesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mesh.addVertexBuffer(BufferTestUtil.createBottomTriangle());
        mesh.addVertexBuffer(BufferTestUtil.createColorBuffer());
        mBottomGeometry.setMesh(mesh);
        ActionHelper.handleMainThreadActions(mBottomGeometry);

        setTestInfo("smooth, black, red, smooth geometry");

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

    public void testDrawTriangleWithColor() throws Exception {
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
package com.sweetlab.sweetride.context;

import com.sweetlab.sweetride.context.Util.ActionHelper;
import com.sweetlab.sweetride.context.Util.BufferTestUtil;
import com.sweetlab.sweetride.context.Util.ProgramTestUtil;
import com.sweetlab.sweetride.geometry.Geometry;
import com.sweetlab.sweetride.material.Material;
import com.sweetlab.sweetride.mesh.Mesh;
import com.sweetlab.sweetride.testframework.OpenGLTestCase;
import com.sweetlab.sweetride.testframework.ResultRunnable;

public class ArrayOneBuf_Geometry_notifier extends OpenGLTestCase {
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
        /**
         * Create materials. No need for GL thread.
         */
        Material redMaterial = new Material();
        redMaterial.setShaderProgram(ProgramTestUtil.createNdcRed());

        Material blueMaterial = new Material();
        blueMaterial.setShaderProgram(ProgramTestUtil.createNdcBlue());

        /**
         * Create geometries.
         */
        Mesh mesh;
        mesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mesh.addVertexBuffer(BufferTestUtil.createLeftTriangle());
        mLeftGeometry.setMesh(mesh);
        mLeftGeometry.setMaterial(redMaterial);
        ActionHelper.handleMainThreadActions(mLeftGeometry);

        mesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mesh.addVertexBuffer(BufferTestUtil.createRightTriangle());
        mRightGeometry.setMesh(mesh);
        mRightGeometry.setMaterial(redMaterial);
        ActionHelper.handleMainThreadActions(mRightGeometry);

        mesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mesh.addVertexBuffer(BufferTestUtil.createTopTriangle());
        mTopGeometry.setMesh(mesh);
        mTopGeometry.setMaterial(blueMaterial);
        ActionHelper.handleMainThreadActions(mTopGeometry);

        mesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mesh.addVertexBuffer(BufferTestUtil.createBottomTriangle());
        mBottomGeometry.setMesh(mesh);
        mBottomGeometry.setMaterial(blueMaterial);
        ActionHelper.handleMainThreadActions(mBottomGeometry);

        setTestInfo("red, blue, red, blue geometry");

        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                mContext = getBackendContext();

                /**
                 * Handle GL actions.
                 */
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

    public void testArrayOneBuf_Geometry_notifier() throws Exception {
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
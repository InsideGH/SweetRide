package com.sweetlab.sweetride.element;

import com.sweetlab.sweetride.Util.BufferTestUtil;
import com.sweetlab.sweetride.Util.ProgramTestUtil;
import com.sweetlab.sweetride.Util.RenderSettingsUtil;
import com.sweetlab.sweetride.Util.Verify;
import com.sweetlab.sweetride.attributedata.IndicesBuffer;
import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.context.BufferUsage;
import com.sweetlab.sweetride.context.MeshDrawingMode;
import com.sweetlab.sweetride.engine.FrontEndActionHandler;
import com.sweetlab.sweetride.geometry.Geometry;
import com.sweetlab.sweetride.material.Material;
import com.sweetlab.sweetride.mesh.Mesh;
import com.sweetlab.sweetride.node.Node;
import com.sweetlab.sweetride.testframework.OpenGLTestCase;
import com.sweetlab.sweetride.testframework.ResultRunnable;

public class ElementTargetTest_Geometry_notifier extends OpenGLTestCase {
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

    /**
     * Front end action handler.
     */
    FrontEndActionHandler mActionHandler = new FrontEndActionHandler();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        /**
         * Attach to root node with a default render settings. The settings
         * will be inherited to children.
         */
        Node root = RenderSettingsUtil.getDefaultGrey(getSurfaceWidth(), getSurfaceHeight());
        root.addChild(mLeftGeometry);
        root.addChild(mTopGeometry);
        root.addChild(mRightGeometry);
        root.addChild(mBottomGeometry);

        Material redMaterial = new Material();
        redMaterial.setShaderProgram(ProgramTestUtil.createNdcRed());

        Material blueMaterial = new Material();
        blueMaterial.setShaderProgram(ProgramTestUtil.createNdcBlue());

        IndicesBuffer indicesBuffer = new IndicesBuffer(BufferTestUtil.createTriangleIndices(), BufferUsage.STATIC);

        Mesh mesh;

        /**
         * Create materials. No need for GL thread.
         */
        mesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mesh.addVertexBuffer(BufferTestUtil.createLeftTriangle());
        mesh.setIndicesBuffer(indicesBuffer);
        mLeftGeometry.setMaterial(redMaterial);
        mLeftGeometry.setMesh(mesh);
        mActionHandler.handleActions(mLeftGeometry);

        mesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mesh.addVertexBuffer(BufferTestUtil.createTopTriangle());
        mTopGeometry.setMaterial(blueMaterial);
        mTopGeometry.setMesh(mesh);
        mActionHandler.handleActions(mTopGeometry);

        mesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mesh.addVertexBuffer(BufferTestUtil.createRightTriangle());
        mesh.setIndicesBuffer(indicesBuffer);
        mRightGeometry.setMaterial(redMaterial);
        mRightGeometry.setMesh(mesh);
        mActionHandler.handleActions(mRightGeometry);

        mesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mesh.addVertexBuffer(BufferTestUtil.createBottomTriangle());
        mBottomGeometry.setMaterial(blueMaterial);
        mBottomGeometry.setMesh(mesh);
        mActionHandler.handleActions(mBottomGeometry);

        setTestInfo("indices red, blue, red, blue geometry");

        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                mContext = getBackendContext();

                mContext.getActionHandler().handleActions(mLeftGeometry);
                mContext.getActionHandler().handleActions(mRightGeometry);
                mContext.getActionHandler().handleActions(mTopGeometry);
                mContext.getActionHandler().handleActions(mBottomGeometry);
                assertFalse(mLeftGeometry.hasActions());
                assertFalse(mRightGeometry.hasActions());
                assertFalse(mTopGeometry.hasActions());
                assertFalse(mBottomGeometry.hasActions());
                return null;
            }
        });
    }

    public void testElementTargetTest_Geometry_notifier() throws Exception {
        runOnDrawFrame(new ResultRunnable() {
            @Override
            public Object run() {
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
        sleepOnDrawFrame(Verify.TERMINATE_TIME);
    }
}
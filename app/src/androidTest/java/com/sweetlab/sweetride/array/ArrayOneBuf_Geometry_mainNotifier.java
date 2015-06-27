package com.sweetlab.sweetride.array;

import com.sweetlab.sweetride.Util.BufferTestUtil;
import com.sweetlab.sweetride.Util.ProgramTestUtil;
import com.sweetlab.sweetride.Util.RenderSettingsUtil;
import com.sweetlab.sweetride.Util.Verify;
import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.context.MeshDrawingMode;
import com.sweetlab.sweetride.engine.FrontEndActionHandler;
import com.sweetlab.sweetride.geometry.Geometry;
import com.sweetlab.sweetride.material.Material;
import com.sweetlab.sweetride.mesh.Mesh;
import com.sweetlab.sweetride.node.Node;
import com.sweetlab.sweetride.testframework.OpenGLTestCase;
import com.sweetlab.sweetride.testframework.ResultRunnable;

public class ArrayOneBuf_Geometry_mainNotifier extends OpenGLTestCase {
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
        mActionHandler.handleActions(mLeftGeometry);

        mesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mesh.addVertexBuffer(BufferTestUtil.createRightTriangle());
        mRightGeometry.setMesh(mesh);
        mRightGeometry.setMaterial(redMaterial);
        mActionHandler.handleActions(mRightGeometry);

        mesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mesh.addVertexBuffer(BufferTestUtil.createTopTriangle());
        mTopGeometry.setMesh(mesh);
        mTopGeometry.setMaterial(blueMaterial);
        mActionHandler.handleActions(mTopGeometry);

        mesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mesh.addVertexBuffer(BufferTestUtil.createBottomTriangle());
        mBottomGeometry.setMesh(mesh);
        mBottomGeometry.setMaterial(blueMaterial);
        mActionHandler.handleActions(mBottomGeometry);

        setTestInfo("red, blue, red, blue geometry");

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

    public void testArrayOneBuf_Geometry_mainNotifier() throws Exception {
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
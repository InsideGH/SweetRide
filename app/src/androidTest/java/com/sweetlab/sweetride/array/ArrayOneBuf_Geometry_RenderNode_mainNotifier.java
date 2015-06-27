package com.sweetlab.sweetride.array;

import com.sweetlab.sweetride.Util.BufferTestUtil;
import com.sweetlab.sweetride.Util.CollectorUtil;
import com.sweetlab.sweetride.Util.ProgramTestUtil;
import com.sweetlab.sweetride.Util.Verify;
import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.context.MeshDrawingMode;
import com.sweetlab.sweetride.engine.FrontEndActionHandler;
import com.sweetlab.sweetride.geometry.Geometry;
import com.sweetlab.sweetride.material.Material;
import com.sweetlab.sweetride.mesh.Mesh;
import com.sweetlab.sweetride.node.rendersettings.ClearBit;
import com.sweetlab.sweetride.rendernode.DefaultRenderNode;
import com.sweetlab.sweetride.testframework.OpenGLTestCase;
import com.sweetlab.sweetride.testframework.ResultRunnable;

import java.util.List;

public class ArrayOneBuf_Geometry_RenderNode_mainNotifier extends OpenGLTestCase {
    /**
     * Backend context.
     */
    private BackendContext mContext;

    /**
     * The default render node. Renders to system window.
     */
    private DefaultRenderNode mRenderNode = new DefaultRenderNode();

    /**
     * Front end action handler.
     */
    FrontEndActionHandler mActionHandler = new FrontEndActionHandler();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        /**
         * Setup render node.
         */
        mRenderNode.getRenderSettings().setClearColor(new float[]{0.3f, 0.3f, 0.3f, 1});
        mRenderNode.getRenderSettings().setClear(0, ClearBit.COLOR_BUFFER_BIT, ClearBit.DEPTH_BUFFER_BIT);
        mRenderNode.getRenderSettings().setViewPort(0, 0, getSurfaceWidth(), getSurfaceHeight());

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
        Geometry geometry1 = new Geometry();
        geometry1.setMesh(mesh);
        geometry1.setMaterial(redMaterial);
        mRenderNode.addChild(geometry1);

        mesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mesh.addVertexBuffer(BufferTestUtil.createRightTriangle());
        Geometry geometry2 = new Geometry();
        geometry2.setMesh(mesh);
        geometry2.setMaterial(redMaterial);
        geometry1.addChild(geometry2);

        mesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mesh.addVertexBuffer(BufferTestUtil.createTopTriangle());
        Geometry geometry3 = new Geometry();
        geometry3.setMesh(mesh);
        geometry3.setMaterial(blueMaterial);
        geometry2.addChild(geometry3);

        mesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mesh.addVertexBuffer(BufferTestUtil.createBottomTriangle());
        Geometry geometry4 = new Geometry();
        geometry4.setMesh(mesh);
        geometry4.setMaterial(blueMaterial);
        geometry2.addChild(geometry4);

        List<Geometry> geometries = CollectorUtil.collectGeometries(mRenderNode);
        for (Geometry geometry : geometries) {
            mActionHandler.handleActions(geometry);
        }

        setTestInfo("red, blue, red, blue geometry render node");

        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                mContext = getBackendContext();
                List<Geometry> geometries = CollectorUtil.collectGeometries(mRenderNode);
                for (Geometry geometry : geometries) {
                    geometry.create(mContext);
                    geometry.load(mContext);
                }
                return null;
            }
        });
    }

    public void testArrayOneBuf_Geometry_RenderNode_mainNotifier() throws Exception {
        runOnDrawFrame(new ResultRunnable() {
            @Override
            public Object run() {
                /**
                 * Render.
                 */
                mRenderNode.getRenderer().render(mContext, CollectorUtil.collectNodes(mRenderNode));

                return null;
            }
        });
        sleepOnDrawFrame(Verify.TERMINATE_TIME);
    }
}
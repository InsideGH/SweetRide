package com.sweetlab.sweetride.context;

import com.sweetlab.sweetride.context.Util.ActionHelper;
import com.sweetlab.sweetride.context.Util.BufferTestUtil;
import com.sweetlab.sweetride.context.Util.ProgramTestUtil;
import com.sweetlab.sweetride.engine.DefaultRenderNode;
import com.sweetlab.sweetride.geometry.Geometry;
import com.sweetlab.sweetride.material.Material;
import com.sweetlab.sweetride.mesh.Mesh;
import com.sweetlab.sweetride.testframework.OpenGLTestCase;
import com.sweetlab.sweetride.testframework.ResultRunnable;

import java.util.List;

public class ArrayOneBuf_Geometry_RenderNode_notifier extends OpenGLTestCase {
    /**
     * Backend context.
     */
    private BackendContext mContext;

    /**
     * The default render node. Renders to system window.
     */
    private DefaultRenderNode mRenderNode = new DefaultRenderNode();

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

        List<Geometry> geometries = mRenderNode.collectGeometries();
        for (Geometry geometry : geometries) {
            ActionHelper.handleMainThreadActions(geometry);
        }

        setTestInfo("red, blue, red, blue geometry render node");

        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                mContext = getBackendContext();
                List<Geometry> geometries = mRenderNode.collectGeometries();
                for (Geometry geometry : geometries) {
                    ActionHelper.handleGLThreadActions(geometry, mContext);
                    assertFalse(geometry.hasActions());
                }
                return null;
            }
        });
    }

    public void testArrayOneBuf_Geometry_RenderNode_notifier() throws Exception {
        runOnDrawFrame(new ResultRunnable() {
            @Override
            public Object run() {
                /**
                 * Clear screen.
                 */
                clearScreen(0.5f, 0.5f, 0.5f, 1.0f);

                /**
                 * Render.
                 */
                mRenderNode.getRenderer().render(mContext, mRenderNode.collectGeometries());

                return null;
            }
        });
        sleepOnDrawFrame(2000);
    }
}
package com.sweetlab.sweetride.context;

import com.sweetlab.sweetride.action.Action;
import com.sweetlab.sweetride.context.Util.BufferTestUtil;
import com.sweetlab.sweetride.context.Util.ProgramTestUtil;
import com.sweetlab.sweetride.engine.DefaultRenderNode;
import com.sweetlab.sweetride.geometry.Geometry;
import com.sweetlab.sweetride.material.Material;
import com.sweetlab.sweetride.mesh.Mesh;
import com.sweetlab.sweetride.testframework.OpenGLTestCase;
import com.sweetlab.sweetride.testframework.ResultRunnable;

import java.util.List;

/**
 * Test array target. This test draws with just one vertex buffer using Geomeetry.
 */
public class ArrayTargetTest1_d extends OpenGLTestCase {
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

        setTestInfo("red, blue, red, blue");

        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                mContext = getBackendContext();
                List<Geometry> geometries = mRenderNode.collectGeometries();
                for (Geometry geometry : geometries) {
                    while (geometry.hasActions()) {
                        Action action = geometry.getFirstAction();
                        action.handleAction(mContext);
                        action.remove();
                    }
                }

                for (Geometry geometry : geometries) {
                    assertFalse(geometry.hasActions());
                }
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
                 * Render.
                 */
                mRenderNode.getRenderer().render(mContext, mRenderNode.collectGeometries());

                return null;
            }
        });
        sleepOnDrawFrame(2000);
    }
}
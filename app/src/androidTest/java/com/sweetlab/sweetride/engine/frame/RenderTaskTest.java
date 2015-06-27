package com.sweetlab.sweetride.engine.frame;

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
import com.sweetlab.sweetride.renderer.DefaultNodeRenderer;
import com.sweetlab.sweetride.testframework.OpenGLTestCase;
import com.sweetlab.sweetride.testframework.ResultRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * Test render task.
 */
public class RenderTaskTest extends OpenGLTestCase {

    /**
     * The render task under test.
     */
    private RenderTask mRenderTask;

    /**
     * Front end action handler.
     */
    FrontEndActionHandler mActionHandler = new FrontEndActionHandler();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mRenderTask = new RenderTask();

        List<Node> list = new ArrayList<>();

        /**
         * The geometries.
         */
        Geometry leftGeometry = new Geometry();
        Geometry topGeometry = new Geometry();
        Geometry rightGeometry = new Geometry();
        Geometry bottomGeometry = new Geometry();

        /**
         * Attach to root node with a default render settings. The settings
         * will be inherited to children.
         */
        Node root = RenderSettingsUtil.getDefaultGrey(getSurfaceWidth(), getSurfaceHeight());
        root.addChild(leftGeometry);
        root.addChild(topGeometry);
        root.addChild(rightGeometry);
        root.addChild(bottomGeometry);

        Material material;
        Mesh mesh;

        /**
         * Create left resources. Complete setup with shader that takes color attribute data
         * and vertex buffer that loads color data.
         */
        material = new Material();
        material.setShaderProgram(ProgramTestUtil.createNdcColor());
        leftGeometry.setMaterial(material);
        mesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mesh.addVertexBuffer(BufferTestUtil.createLeftTriangle());
        mesh.addVertexBuffer(BufferTestUtil.createColorBuffer());
        leftGeometry.setMesh(mesh);
        mActionHandler.handleActions(leftGeometry);

        /**
         * Create top resources. Not complete setup, the actual color buffer is missing which
         * produces a black triangle.
         */
        material = new Material();
        material.setShaderProgram(ProgramTestUtil.createNdcColor());
        topGeometry.setMaterial(material);
        mesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mesh.addVertexBuffer(BufferTestUtil.createTopTriangle());
        topGeometry.setMesh(mesh);
        mActionHandler.handleActions(topGeometry);

        /**
         * Create right resources. Misconstrued setup. Using a shader that just paints red but
         * adding vertex buffer with color buffer. The vertex buffer is ignored and the triangle
         * is painted red.
         */
        material = new Material();
        material.setShaderProgram(ProgramTestUtil.createNdcRed());
        rightGeometry.setMaterial(material);
        mesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mesh.addVertexBuffer(BufferTestUtil.createRightTriangle());
        mesh.addVertexBuffer(BufferTestUtil.createColorBuffer());
        rightGeometry.setMesh(mesh);
        mActionHandler.handleActions(rightGeometry);

        /**
         * Create bottom resources. Complete setup with shader that takes color attribute data
         * and vertex buffer that loads color data.
         */
        material = new Material();
        material.setShaderProgram(ProgramTestUtil.createNdcColor());
        bottomGeometry.setMaterial(material);
        mesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mesh.addVertexBuffer(BufferTestUtil.createBottomTriangle());
        mesh.addVertexBuffer(BufferTestUtil.createColorBuffer());
        bottomGeometry.setMesh(mesh);
        mActionHandler.handleActions(bottomGeometry);

        setTestInfo("RenderTask : smooth, black, red, smooth geometry");

        /**
         * Configure render task.
         */
        list.add(leftGeometry);
        list.add(topGeometry);
        list.add(rightGeometry);
        list.add(bottomGeometry);
        mRenderTask.set(new DefaultNodeRenderer(), list);
    }

    public void testRenderTask() throws Exception {
        runOnDrawFrame(new ResultRunnable() {
            @Override
            public Object run() {
                BackendContext backendContext = getBackendContext();
                mRenderTask.handleActions(backendContext);
                mRenderTask.render(backendContext);
                return null;
            }
        });
        sleepOnDrawFrame(Verify.TERMINATE_TIME);
    }
}
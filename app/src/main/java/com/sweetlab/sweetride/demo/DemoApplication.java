package com.sweetlab.sweetride.demo;

import com.sweetlab.sweetride.UserApplication;
import com.sweetlab.sweetride.demo.mesh.QuadMesh;
import com.sweetlab.sweetride.engine.DefaultRenderNode;
import com.sweetlab.sweetride.geometry.Geometry;
import com.sweetlab.sweetride.material.Material;
import com.sweetlab.sweetride.math.Camera;
import com.sweetlab.sweetride.math.Frustrum;
import com.sweetlab.sweetride.node.Node;
import com.sweetlab.sweetride.shader.FragmentShader;
import com.sweetlab.sweetride.shader.ShaderProgram;
import com.sweetlab.sweetride.shader.VertexShader;

/**
 * A demo application. Currently a rotating red quad.
 */
public class DemoApplication extends UserApplication {
    private static final String VERTEX_SHADER =
            "attribute vec4 a_Pos; \n" +
                    "uniform mat4 u_worldViewProjMat; \n" +
                    "void main() { " +
                    "    gl_Position = u_worldViewProjMat * a_Pos;" +
                    "} ";


    private static final String FRAGMENT_RED =
            "precision mediump float;\n" +
                    "void main() {\n" +
                    "\tgl_FragColor = vec4(1.0, 0.0, 0.0, 1.0);\n" +
                    "}";

    /**
     * Camera field of view.
     */
    private static final float FIELD_OF_VIEW = 90;

    /**
     * The camera near field.
     */
    private static final float NEAR_FIELD = 0.1f;

    /**
     * The camera far field.
     */
    private static final float FAR_FIELD = 10f;

    /**
     * Camera distance to object.
     */
    private static final float CAMERA_DISTANCE = 3f;

    /**
     * Default system window render node.
     */
    private DefaultRenderNode mRenderNode = new DefaultRenderNode();

    /**
     * The geometries.
     */
    private Geometry mQuadGeometry = new Geometry();

    /**
     * Constructor.
     */
    public DemoApplication() {
        /**
         * Add the geometry to demo root.
         */
        mRenderNode.addChild(mQuadGeometry);

        /**
         * Place the camera, wait with projection until we get screen dimensions.
         */
        mRenderNode.setCamera(new Camera());
        mRenderNode.getCamera().lookAt(0, 0, -CAMERA_DISTANCE, 0, 0, 0);

        /**
         * Create material.
         */
        mQuadGeometry.setMaterial(new Material());
        ShaderProgram program = new ShaderProgram(new VertexShader(VERTEX_SHADER), new FragmentShader(FRAGMENT_RED));
        mQuadGeometry.getMaterial().setShaderProgram(program);

    }

    @Override
    public void onInitialized(Node engineRoot, int width, int height) {
        /**
         * Add the render node to the engine root.
         */
        engineRoot.addChild(mRenderNode);

        /**
         * Setup camera frustrum.
         */
        Frustrum frustrum = mRenderNode.getCamera().getFrustrum();
        frustrum.setPerspectiveProjection(FIELD_OF_VIEW, Frustrum.FovType.AUTO_FIT, NEAR_FIELD, FAR_FIELD, width, height);

        /**
         * Create a quad mesh that is a quarter of the screen width.
         */
        float quadWidth = frustrum.calcWidthAtDepth(CAMERA_DISTANCE) / 4;
        mQuadGeometry.setMesh(new QuadMesh(quadWidth, quadWidth, "a_Pos", null));
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        /**
         * If the application would handle it's own orientation changes, the frustrum would need to
         * be re-set here. But for now, the application is re-created during orientation changes.
         */
    }

    @Override
    public void onUpdate(float dt) {
        /**
         * Rotate the quad 2 degrees each frame around y axis.
         */
        mQuadGeometry.getModelTransform().rotate(2, 0, 1, 0);
    }
}

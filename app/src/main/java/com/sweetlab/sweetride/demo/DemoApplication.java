package com.sweetlab.sweetride.demo;

import com.sweetlab.sweetride.UserApplication;
import com.sweetlab.sweetride.demo.mesh.QuadMesh;
import com.sweetlab.sweetride.engine.DefaultRenderNode;
import com.sweetlab.sweetride.engine.ViewFrustrumCulling;
import com.sweetlab.sweetride.geometry.Geometry;
import com.sweetlab.sweetride.material.Material;
import com.sweetlab.sweetride.math.Camera;
import com.sweetlab.sweetride.math.Frustrum;
import com.sweetlab.sweetride.math.Vec3;
import com.sweetlab.sweetride.node.Node;
import com.sweetlab.sweetride.shader.FragmentShader;
import com.sweetlab.sweetride.shader.ShaderProgram;
import com.sweetlab.sweetride.shader.VertexShader;

import java.util.Random;

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

    private static final String FRAGMENT_BLUE =
            "precision mediump float;\n" +
                    "void main() {\n" +
                    "\tgl_FragColor = vec4(0.0, 0.0, 1.0, 1.0);\n" +
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
     * The camera used. Possible to use node find camera as well.
     */
    private final Camera mCamera;

    /**
     * Default system window render node.
     */
    private DefaultRenderNode mRenderNode = new DefaultRenderNode();

    /**
     * The geometry rotating.
     */
    private Geometry mRotatingQuad = new Geometry();

    /**
     * The geometry moving around.
     */
    private Geometry mMovingQuad = new Geometry();

    /**
     * The gl quad width.
     */
    private float mQuadWidth;

    /**
     * View frustrum culling.
     */
    private ViewFrustrumCulling mViewCulling = new ViewFrustrumCulling();

    /**
     * Random moving vector.
     */
    private Vec3 mMovingVec = new Vec3();

    /**
     * Random generator.
     */
    private Random mRandom = new Random(666);

    /**
     * Constructor.
     */
    public DemoApplication() {
        /**
         * Enable view frustrum culling.
         */
        mRenderNode.enableViewFrustrumCulling(true);

        /**
         * Add the geometries to demo root.
         */
        mRenderNode.addChild(mRotatingQuad);
        mRenderNode.addChild(mMovingQuad);

        /**
         * Place the camera, wait with projection until we get screen dimensions.
         */
        mCamera = new Camera();
        mRenderNode.setCamera(mCamera);
        mRenderNode.getCamera().lookAt(0, 0, CAMERA_DISTANCE, 0, 0, 0);

        /**
         * Create red material.
         */
        mRotatingQuad.setMaterial(new Material());
        ShaderProgram programRed = new ShaderProgram(new VertexShader(VERTEX_SHADER), new FragmentShader(FRAGMENT_RED));
        mRotatingQuad.getMaterial().setShaderProgram(programRed);

        /**
         * Create blue material.
         */
        mMovingQuad.setMaterial(new Material());
        ShaderProgram programBlue = new ShaderProgram(new VertexShader(VERTEX_SHADER), new FragmentShader(FRAGMENT_BLUE));
        mMovingQuad.getMaterial().setShaderProgram(programBlue);
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
         * Create a quad mesh that is a quarter of the screen width. Use same mesh for both geometries.
         */
        mQuadWidth = frustrum.calcWidthAtDepth(CAMERA_DISTANCE) / 4;
        mRotatingQuad.setMesh(new QuadMesh(mQuadWidth, mQuadWidth, "a_Pos", null));
        mRotatingQuad.getModelTransform().translate(mQuadWidth, 0, 0);

        mMovingQuad.setMesh(mRotatingQuad.getMesh());
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
         * Rotate each frame around y axis.
         */
        mRotatingQuad.getModelTransform().rotate(2, 0, 1, 0);

        /**
         * When object is not visible any longer reset position
         * and generate new random moving vector.
         */
        if (!mViewCulling.isVisible(mMovingQuad, mCamera)) {
            mMovingQuad.getModelTransform().setIdentity();
            float x = (mRandom.nextFloat() - 0.5f) * 0.1f;
            float y = (mRandom.nextFloat() - 0.5f) * 0.1f;
            float z = (mRandom.nextFloat() - 0.5f) * 0.1f;
            mMovingVec.set(x, y, z);
        }

        /**
         * Translate in random direction.
         */
        mMovingQuad.getModelTransform().translate(mMovingVec.x, mMovingVec.y, mMovingVec.z);
    }
}

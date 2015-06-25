package com.sweetlab.sweetride.demo;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.MotionEvent;

import com.sweetlab.sweetride.UserApplication;
import com.sweetlab.sweetride.context.MagFilter;
import com.sweetlab.sweetride.context.MinFilter;
import com.sweetlab.sweetride.demo.mesh.CubeMesh;
import com.sweetlab.sweetride.engine.rendernode.DefaultRenderNode;
import com.sweetlab.sweetride.camera.ViewFrustrumCulling;
import com.sweetlab.sweetride.geometry.Geometry;
import com.sweetlab.sweetride.material.Material;
import com.sweetlab.sweetride.camera.FirstPersonCamera;
import com.sweetlab.sweetride.camera.Frustrum;
import com.sweetlab.sweetride.math.Vec3;
import com.sweetlab.sweetride.node.Node;
import com.sweetlab.sweetride.shader.FragmentShader;
import com.sweetlab.sweetride.shader.ShaderProgram;
import com.sweetlab.sweetride.shader.VertexShader;
import com.sweetlab.sweetride.texture.Texture2D;

import java.util.Random;

/**
 * A demo application. Currently a rotating red quad.
 */
public class DemoApplication extends UserApplication {
    private static final String VERTEX_SHADER =
            "attribute vec4 a_Pos; \n" +
                    "uniform mat4 u_worldViewProjMat; \n" +
                    "attribute vec2 a_texCoord;\n" +
                    "varying vec2 v_texCoord;\n" +
                    "void main() { " +
                    "    v_texCoord = a_texCoord;\n" +
                    "    gl_Position = u_worldViewProjMat * a_Pos;" +
                    "} ";


    private static final String FRAGMENT_SHADER =
            "precision mediump float;\n" +
                    "varying vec2 v_texCoord;\n" +
                    "uniform sampler2D s_texture;\n" +
                    "void main() {\n" +
                    "vec4 color = texture2D(s_texture, v_texCoord);\n" +
                    "gl_FragColor = color;\n" +
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
    private static final float FAR_FIELD = 100f;

    /**
     * Camera distance to object.
     */
    private static final float CAMERA_DISTANCE = 3f;

    /**
     * The camera used. Possible to use node find camera as well.
     */
    private final FirstPersonCamera mCamera;

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

    private float mStrafeLeftRight;
    private float mStrafeForwardBackward;
    private float mTurnUpDown;
    private float mTurnLeftRight;
    private int mActionIndexStrafe;
    private int mActionIndexTurn;

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
        mCamera = new FirstPersonCamera();
        mRenderNode.setCamera(mCamera);
        mRenderNode.getCamera().lookAt(0, 0, CAMERA_DISTANCE, 0, 0, 0);

        /**
         * Create red material.
         */
        mRotatingQuad.setMaterial(new Material());
        ShaderProgram programRed = new ShaderProgram(new VertexShader(VERTEX_SHADER), new FragmentShader(FRAGMENT_SHADER));
        mRotatingQuad.getMaterial().setShaderProgram(programRed);

        Texture2D textureFourColor = new Texture2D("s_texture", createQuadColorBitmap(Bitmap.Config.RGB_565), MinFilter.NEAREST, MagFilter.NEAREST);
        mRotatingQuad.getMaterial().addTexture(textureFourColor);

        /**
         * Create blue material.
         */
        mMovingQuad.setMaterial(new Material());
        ShaderProgram programBlue = new ShaderProgram(new VertexShader(VERTEX_SHADER), new FragmentShader(FRAGMENT_SHADER));
        mMovingQuad.getMaterial().setShaderProgram(programBlue);

        Texture2D textureChess = new Texture2D("s_texture", createChessColorBitmap(Bitmap.Config.RGB_565), MinFilter.NEAREST, MagFilter.NEAREST);
        mMovingQuad.getMaterial().addTexture(textureChess);
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
        mRotatingQuad.setMesh(new CubeMesh("a_Pos", "a_texCoord"));
        mRotatingQuad.getModelTransform().translate(mQuadWidth, 0, 0);

        mMovingQuad.setMesh(mRotatingQuad.getMesh());

        setNewMovingDirection();
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
        mCamera.update(mStrafeLeftRight, mStrafeForwardBackward, mTurnUpDown, mTurnLeftRight);

        mRotatingQuad.getModelTransform().rotate(2, 0, 1, 0);
        mMovingQuad.getModelTransform().rotate(2, 0, 1, 0);
        /**
         * When object is not visible any longer reset position
         * and generate new random moving vector.
         */
        if (!mViewCulling.isVisible(mMovingQuad, mCamera)) {
            setNewMovingDirection();
        }

        /**
         * Translate in random direction.
         */
        mMovingQuad.getModelTransform().translate(mMovingVec.x, mMovingVec.y, mMovingVec.z);
    }

    private float mDownStrafeX;
    private float mDownStrafeY;

    private float mDownTurnX;
    private float mDownTurnY;

    /**
     * Action on view touch event.
     *
     * @param event The touch event.
     * @return Always true/handled.
     */
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mStrafeLeftRight = 0;
                mStrafeForwardBackward = 0;
                break;

            case MotionEvent.ACTION_DOWN:
                mActionIndexStrafe = event.getActionIndex();
                mDownStrafeX = event.getX(mActionIndexStrafe);
                mDownStrafeY = event.getY(mActionIndexStrafe);
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                mActionIndexTurn = event.getActionIndex();
                mDownTurnX = event.getX(mActionIndexTurn);
                mDownTurnY = event.getY(mActionIndexTurn);
                break;

            case MotionEvent.ACTION_POINTER_UP:
                mTurnLeftRight = 0;
                mTurnUpDown = 0;
                break;

            case MotionEvent.ACTION_MOVE:
                float x = event.getX(mActionIndexStrafe);
                float y = event.getY(mActionIndexStrafe);
                float sizeX = x - mDownStrafeX;
                float sizeY = y - mDownStrafeY;
                mStrafeLeftRight = sizeX * 0.0001f;
                mStrafeForwardBackward = -sizeY * 0.0001f;
                if (event.getPointerCount() > 1) {
                    x = event.getX(mActionIndexTurn);
                    y = event.getY(mActionIndexTurn);
                    sizeX = x - mDownTurnX;
                    sizeY = y - mDownTurnY;
                    mTurnLeftRight = -sizeX * 0.001f;
                    mTurnUpDown = -sizeY * 0.001f;
                }
                break;
        }
        return true;
    }

    /**
     * Set new moving direction.
     */
    private void setNewMovingDirection() {
        mMovingQuad.getModelTransform().setIdentity();
        float x = (mRandom.nextFloat() - 0.5f) * 0.8f;
        float y = (mRandom.nextFloat() - 0.5f) * 0.4f;
        float z = (mRandom.nextFloat() - 0.5f) * 0.1f;
        mMovingVec.set(x, y, z);
    }

    public static Bitmap createQuadColorBitmap(Bitmap.Config config) {
        return Bitmap.createBitmap(new int[]{Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW}, 2, 2, config);
    }

    public static Bitmap createChessColorBitmap(Bitmap.Config config) {
        int values[] = new int[10 * 10];
        int p = 0;
        Random random = new Random(10);
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                int grey = random.nextInt(256);
                values[p] = Color.argb(255, grey, grey, grey);
                p++;
            }
        }
        return Bitmap.createBitmap(values, 10, 10, config);
    }
}

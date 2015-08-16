package com.sweetlab.sweetride.demo.demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

import com.sweetlab.sweetride.R;
import com.sweetlab.sweetride.UserApplication;
import com.sweetlab.sweetride.camera.Camera;
import com.sweetlab.sweetride.camera.FirstPersonCamera;
import com.sweetlab.sweetride.camera.Frustrum;
import com.sweetlab.sweetride.camera.ViewFrustrumCulling;
import com.sweetlab.sweetride.context.MagFilter;
import com.sweetlab.sweetride.context.MinFilter;
import com.sweetlab.sweetride.demo.demo.mesh.CubeMesh;
import com.sweetlab.sweetride.demo.game.assets.AssetsLoader;
import com.sweetlab.sweetride.demo.game.headup.HeadUpDisplay;
import com.sweetlab.sweetride.geometry.Geometry;
import com.sweetlab.sweetride.geometry.OnTouchListener;
import com.sweetlab.sweetride.intersect.Ray;
import com.sweetlab.sweetride.material.Material;
import com.sweetlab.sweetride.math.Vec3;
import com.sweetlab.sweetride.node.Node;
import com.sweetlab.sweetride.node.rendersettings.BlendDstFact;
import com.sweetlab.sweetride.node.rendersettings.BlendSrcFact;
import com.sweetlab.sweetride.node.rendersettings.ClearBit;
import com.sweetlab.sweetride.rendernode.DefaultRenderNode;
import com.sweetlab.sweetride.shader.FragmentShader;
import com.sweetlab.sweetride.shader.ShaderProgram;
import com.sweetlab.sweetride.shader.VertexShader;
import com.sweetlab.sweetride.texture.Texture2D;

import java.util.Random;

import rx.functions.Action1;

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
                    "if (color.a < 0.5) { \n" +
                    "  discard;\n" +
                    " } \n" +
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
    private final DefaultRenderNode mRenderNode = new DefaultRenderNode();

    /**
     * The geometry rotating.
     */
    private final Geometry mRotatingQuad = new Geometry();

    /**
     * The geometry moving around.
     */
    private final Geometry mMovingQuad = new Geometry();

    /**
     * View frustrum culling.
     */
    private final ViewFrustrumCulling mViewCulling = new ViewFrustrumCulling();

    /**
     * Random moving vector.
     */
    private final Vec3 mMovingVec = new Vec3();

    /**
     * Random generator.
     */
    private final Random mRandom = new Random(666);
    private final Context mContext;

    private float mDownStrafeX;
    private float mDownStrafeZ;
    private float mDownTurnAroundX;
    private float mDownTurnAroundY;

    private float mStrafeX;
    private float mStrafeZ;
    private float mTurnAroundX;
    private float mTurnAroundY;

    /**
     * Constructor.
     */
    public DemoApplication(Context context) {
        mContext = context;
        /**
         * Create assets loader.
         */
        AssetsLoader assetsLoader = new AssetsLoader(context);

        /**
         * Enable view frustrum culling.
         */
        mRenderNode.enableViewFrustrumCulling(true);
        mRenderNode.getRenderSettings().setClearColor(new float[]{1.0f, 0.3f, 0.3f, 1});
        mRenderNode.getRenderSettings().setClear(0, ClearBit.COLOR_BUFFER_BIT, ClearBit.DEPTH_BUFFER_BIT);
        mRenderNode.getRenderSettings().setDepthTest(true);
        mRenderNode.getRenderSettings().setBlend(true);
        mRenderNode.getRenderSettings().setBlendFact(BlendSrcFact.SRC_ALPHA, BlendDstFact.ONE_MINUS_SRC_ALPHA);

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
        mCamera.lookAt(0, 0, CAMERA_DISTANCE, 0, 0, 0);

        /**
         * Create red material.
         */
        Material material = new Material();
        mRotatingQuad.setMaterial(material);
        ShaderProgram programRed = new ShaderProgram(new VertexShader(VERTEX_SHADER), new FragmentShader(FRAGMENT_SHADER));
        material.setShaderProgram(programRed);

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;

        assetsLoader.loadBitmapAsync(R.drawable.compass, opts).subscribe(new Action1<Bitmap>() {
            @Override
            public void call(Bitmap bitmap) {
                Material rotMaterial = mRotatingQuad.getMaterial();
                if (rotMaterial != null) {
                    Texture2D textureLeft = new Texture2D("s_texture", bitmap, MinFilter.NEAREST, MagFilter.NEAREST);
//                    Texture2D textureLeft = new Texture2D("s_texture", createChessColorBitmap(Bitmap.Config.RGB_565), MinFilter.NEAREST, MagFilter.NEAREST);
                    rotMaterial.addTexture(textureLeft);
                }
            }
        });

        /**
         * Create blue material.
         */
        material = new Material();
        mMovingQuad.setMaterial(material);
        ShaderProgram programBlue = new ShaderProgram(new VertexShader(VERTEX_SHADER), new FragmentShader(FRAGMENT_SHADER));
        material.setShaderProgram(programBlue);

        /**
         * Load bitmap.
         */
        Texture2D textureChess = new Texture2D("s_texture", createChessColorBitmap(Bitmap.Config.RGB_565), MinFilter.NEAREST, MagFilter.NEAREST);
        material.addTexture(textureChess);

        mRotatingQuad.addOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onDown(Ray ray, int x, int y) {
                Log.d("Peter100", "mRotatingQuad.onDown");
                return true;
            }

            @Override
            public boolean onUp(Ray ray, int x, int y) {
                Log.d("Peter100", "mRotatingQuad.onUp");
                return true;
            }

            @Override
            public void onCancel() {
                Log.d("Peter100", "mRotatingQuad.onCancel");
            }

            @Override
            public boolean onMove(Ray ray, boolean isHit, int x, int y) {
                Log.d("Peter100", "mRotatingQuad.onMove isHit = " + isHit);
                return false;
            }
        });

        mMovingQuad.addOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onDown(Ray ray, int x, int y) {
                Log.d("Peter100", "mMovingQuad.onDown");
                return true;
            }

            @Override
            public boolean onUp(Ray ray, int x, int y) {
                Log.d("Peter100", "mMovingQuad.onUp");
                return true;
            }

            @Override
            public void onCancel() {
                Log.d("Peter100", "mMovingQuad.onCancel");
            }

            @Override
            public boolean onMove(Ray ray, boolean isHit, int x, int y) {
                Log.d("Peter100", "mMovingQuad.onMove isHit = " + isHit);
                return false;
            }
        });
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
        Camera camera = mRenderNode.findCamera();
        if (camera != null) {
            Frustrum frustrum = mRenderNode.findCamera().getFrustrum();
            frustrum.setPerspectiveProjection(FIELD_OF_VIEW, Frustrum.FovType.AUTO_FIT, NEAR_FIELD, FAR_FIELD, width, height);
            /**
             * Create a quad mesh that is a quarter of the screen width. Use same mesh for both geometries.
             */
            float quadSize = frustrum.calcWidthAtDepth(CAMERA_DISTANCE) / 4;
            mRotatingQuad.setMesh(new CubeMesh("a_Pos", "a_texCoord"));
            mRotatingQuad.getModelTransform().translate(-quadSize, -quadSize, -quadSize);
            mMovingQuad.setMesh(mRotatingQuad.getMesh());
            setNewMovingDirection();
        }

        HeadUpDisplay headUpDisplay = new HeadUpDisplay(mContext, width, height);
        engineRoot.addChild(headUpDisplay);

        headUpDisplay.addMoveTouchListener(new OnTouchListener() {
            @Override
            public boolean onDown(Ray ray, int x, int y) {
                mDownStrafeX = x;
                mDownStrafeZ = y;
                return true;
            }

            @Override
            public boolean onUp(Ray ray, int x, int y) {
                mStrafeX = 0;
                mStrafeZ = 0;
                return true;
            }

            @Override
            public void onCancel() {
                mStrafeX = 0;
                mStrafeZ = 0;
            }

            @Override
            public boolean onMove(Ray ray, boolean isHit, int x, int y) {
                float sizeX = x - mDownStrafeX;
                float sizeY = y - mDownStrafeZ;
                mStrafeX = sizeX * 0.0001f;
                mStrafeZ = -sizeY * 0.0001f;
                return false;
            }
        });

        headUpDisplay.addTurnTouchListener(new OnTouchListener() {
            @Override
            public boolean onDown(Ray ray, int x, int y) {
                mDownTurnAroundX = x;
                mDownTurnAroundY = y;
                return true;
            }

            @Override
            public boolean onUp(Ray ray, int x, int y) {
                mTurnAroundX = 0;
                mTurnAroundY = 0;
                return true;
            }

            @Override
            public void onCancel() {
                mTurnAroundX = 0;
                mTurnAroundY = 0;
            }

            @Override
            public boolean onMove(Ray ray, boolean isHit, int x, int y) {
                float sizeX = x - mDownTurnAroundX;
                float sizeY = y - mDownTurnAroundY;
                mTurnAroundY = -sizeX * 0.001f;
                mTurnAroundX = -sizeY * 0.001f;
                return false;
            }
        });
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
        mCamera.update(mStrafeX, mStrafeZ, mTurnAroundX, mTurnAroundY);
        mRotatingQuad.getModelTransform().rotate(1f, 0, 1, 0);
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

    private static Bitmap createChessColorBitmap(Bitmap.Config config) {
        int values[] = new int[16 * 16];
        int p = 0;
        Random random = new Random(16);
        for (int y = 0; y < 16; y++) {
            for (int x = 0; x < 16; x++) {
                int grey = random.nextInt(256);
                values[p] = Color.argb(255, grey, grey, grey);
                p++;
            }
        }
        return Bitmap.createBitmap(values, 16, 16, config);
    }
}

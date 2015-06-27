package com.sweetlab.sweetride.demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;

import com.sweetlab.sweetride.R;
import com.sweetlab.sweetride.UserApplication;
import com.sweetlab.sweetride.context.MagFilter;
import com.sweetlab.sweetride.context.MinFilter;
import com.sweetlab.sweetride.demo.mesh.QuadMesh;
import com.sweetlab.sweetride.geometry.Geometry;
import com.sweetlab.sweetride.intersect.Intersect;
import com.sweetlab.sweetride.intersect.Ray;
import com.sweetlab.sweetride.intersect.TransformableBoundingBox;
import com.sweetlab.sweetride.material.Material;
import com.sweetlab.sweetride.node.Node;
import com.sweetlab.sweetride.node.rendersettings.BlendDstFact;
import com.sweetlab.sweetride.node.rendersettings.BlendSrcFact;
import com.sweetlab.sweetride.node.rendersettings.ClearBit;
import com.sweetlab.sweetride.rendernode.AndroidRenderNode;
import com.sweetlab.sweetride.shader.FragmentShader;
import com.sweetlab.sweetride.shader.ShaderProgram;
import com.sweetlab.sweetride.shader.VertexShader;
import com.sweetlab.sweetride.texture.Texture2D;
import com.sweetlab.sweetride.touch.TouchToRay;

import rx.functions.Action1;

/**
 * Demo application2. Head up display.
 */
public class DemoApplication2 extends UserApplication {
    /**
     * Margin around controls.
     */
    private static final int CONTROL_MARGIN_DP = 10;

    /**
     * Size of controls.
     */
    private static final int CONTROL_SIZE_DP = 100;

    /**
     * Vertex shader.
     */
    private static final String VERTEX_SHADER =
            "attribute vec4 a_Pos; \n" +
                    "uniform mat4 u_worldViewProjMat; \n" +
                    "attribute vec2 a_texCoord;\n" +
                    "varying vec2 v_texCoord;\n" +
                    "void main() { " +
                    "    v_texCoord = a_texCoord;\n" +
                    "    gl_Position = u_worldViewProjMat * a_Pos;" +
                    "} ";


    /**
     * Fragment shader.
     */
    private static final String FRAGMENT_SHADER =
            "precision mediump float;\n" +
                    "varying vec2 v_texCoord;\n" +
                    "uniform sampler2D s_texture;\n" +
                    "void main() {\n" +
                    "vec4 color = texture2D(s_texture, v_texCoord);\n" +
                    "gl_FragColor = color;\n" +
                    "}";

    /**
     * Android context.
     */
    private final Context mContext;

    /**
     * The left quad, used for moving around.
     */
    private final Geometry mMoveQuad = new Geometry();

    /**
     * The right quad, used for looking around.
     */
    private final Geometry mTurnQuad = new Geometry();

    /**
     * Intersect.
     */
    private final Intersect mIntersect = new Intersect();

    /**
     * The head up display node/render node.
     */
    private AndroidRenderNode mAndroidRenderNode;

    /**
     * World space ray generator.
     */
    private TouchToRay mTouchToRay;

    /**
     * Constructor.
     */
    public DemoApplication2(Context context) {
        mContext = context;

        /**
         * Create assets loader.
         */
        AssetsLoader assetsLoader = new AssetsLoader(context);

        /**
         * Create shader program.
         */
        ShaderProgram program = new ShaderProgram(new VertexShader(VERTEX_SHADER), new FragmentShader(FRAGMENT_SHADER));

        /**
         * Create left quad.
         */
        Material material = new Material();
        mMoveQuad.setMaterial(material);
        material.setShaderProgram(program);

        /**
         * Create right quad.
         */
        material = new Material();
        mTurnQuad.setMaterial(material);
        material.setShaderProgram(program);

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;

        /**
         * Load bitmap.
         */
        assetsLoader.loadBitmap(R.drawable.compass, opts).subscribe(new Action1<Bitmap>() {
            @Override
            public void call(Bitmap bitmap) {
                Material moveMaterial = mMoveQuad.getMaterial();
                if (moveMaterial != null) {
                    Texture2D textureLeft = new Texture2D("s_texture", bitmap, MinFilter.NEAREST, MagFilter.NEAREST);
                    moveMaterial.addTexture(textureLeft);
                }
            }
        });

        /**
         * Load bitmap.
         */
        assetsLoader.loadBitmap(R.drawable.turn, opts).subscribe(new Action1<Bitmap>() {
            @Override
            public void call(Bitmap bitmap) {
                Material turnMaterial = mTurnQuad.getMaterial();
                if (turnMaterial != null) {
                    Texture2D textureRight = new Texture2D("s_texture", bitmap, MinFilter.NEAREST, MagFilter.NEAREST);
                    turnMaterial.addTexture(textureRight);
                }
            }
        });
    }

    @Override
    public void onInitialized(Node engineRoot, int width, int height) {
        mAndroidRenderNode = new AndroidRenderNode(width, height);
        mAndroidRenderNode.getRenderSettings().setClearColor(new float[]{0.3f, 0.3f, 0.3f, 1});
        mAndroidRenderNode.getRenderSettings().setClear(0, ClearBit.COLOR_BUFFER_BIT, ClearBit.DEPTH_BUFFER_BIT);
        mAndroidRenderNode.getRenderSettings().setBlend(true);
        mAndroidRenderNode.getRenderSettings().setBlendFact(BlendSrcFact.SRC_ALPHA, BlendDstFact.ONE_MINUS_SRC_ALPHA);

        mAndroidRenderNode.addChild(mMoveQuad);
        mAndroidRenderNode.addChild(mTurnQuad);

        /**
         * Place and size based on DP.
         */
        float margin = DpPx.dpToPx(mContext, CONTROL_MARGIN_DP);
        float quadSize = DpPx.dpToPx(mContext, CONTROL_SIZE_DP);
        float half = quadSize / 2;

        QuadMesh quadMesh = new QuadMesh(quadSize, quadSize, "a_Pos", "a_texCoord");
        mMoveQuad.setMesh(quadMesh);
        mMoveQuad.getModelTransform().translate(half + margin, height - half - margin, 0);

        mTurnQuad.setMesh(quadMesh);
        mTurnQuad.getModelTransform().translate(width - half - margin, height - half - margin, 0);

        engineRoot.addChild(mAndroidRenderNode);

        mTouchToRay = new TouchToRay(width, height);
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
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                int actionIndex = event.getActionIndex();
                int x = (int) event.getX(actionIndex);
                int y = (int) event.getY(actionIndex);

                Ray ray = mTouchToRay.getRay(mAndroidRenderNode.findCamera(), x, y);

                TransformableBoundingBox moveBox = mMoveQuad.getTransformableBoundingBox();
                if (mIntersect.intersects(ray, moveBox)) {
                    Log.d("Peter100", "DemoApplication2.onTouchEvent move");
                    return true;
                }

                TransformableBoundingBox turnBox = mTurnQuad.getTransformableBoundingBox();
                if (mIntersect.intersects(ray, turnBox)) {
                    Log.d("Peter100", "DemoApplication2.onTouchEvent turn");
                    return true;
                }
                break;
        }
        return false;
    }

    public static Bitmap createQuadColorBitmap(Bitmap.Config config) {
        return Bitmap.createBitmap(new int[]{Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW}, 2, 2, config);
    }
}

package com.sweetlab.sweetride.demo;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;

import com.sweetlab.sweetride.UserApplication;
import com.sweetlab.sweetride.context.MagFilter;
import com.sweetlab.sweetride.context.MinFilter;
import com.sweetlab.sweetride.demo.mesh.QuadMesh;
import com.sweetlab.sweetride.engine.rendernode.AndroidRenderNode;
import com.sweetlab.sweetride.geometry.Geometry;
import com.sweetlab.sweetride.intersect.Intersect;
import com.sweetlab.sweetride.material.Material;
import com.sweetlab.sweetride.intersect.TransformableBoundingBox;
import com.sweetlab.sweetride.node.Node;
import com.sweetlab.sweetride.intersect.Ray;
import com.sweetlab.sweetride.shader.FragmentShader;
import com.sweetlab.sweetride.shader.ShaderProgram;
import com.sweetlab.sweetride.shader.VertexShader;
import com.sweetlab.sweetride.texture.Texture2D;
import com.sweetlab.sweetride.touch.TouchToRay;

/**
 * Demo application2. Head up display.
 */
public class DemoApplication2 extends UserApplication {
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
     * The left quad, used for moving around.
     */
    private Geometry mMoveQuad = new Geometry();

    /**
     * The right quad, used for looking around.
     */
    private Geometry mTurnQuad = new Geometry();

    /**
     * The head up display node/render node.
     */
    private AndroidRenderNode mAndroidRenderNode;

    /**
     * World space ray generator.
     */
    private TouchToRay mTouchToRay;

    /**
     * Intersect.
     */
    private Intersect mIntersect = new Intersect();

    /**
     * Constructor.
     */
    public DemoApplication2() {
        /**
         * Create shader program.
         */
        ShaderProgram program = new ShaderProgram(new VertexShader(VERTEX_SHADER), new FragmentShader(FRAGMENT_SHADER));

        /**
         * Create left quad.
         */
        mMoveQuad.setMaterial(new Material());
        mMoveQuad.getMaterial().setShaderProgram(program);

        /**
         * Create right quad.
         */
        mTurnQuad.setMaterial(new Material());
        mTurnQuad.getMaterial().setShaderProgram(program);


        Texture2D textureLeft = new Texture2D("s_texture", createQuadColorBitmap(Bitmap.Config.RGB_565), MinFilter.NEAREST, MagFilter.NEAREST);
        mMoveQuad.getMaterial().addTexture(textureLeft);

        Texture2D textureRight = new Texture2D("s_texture", createQuadColorBitmap(Bitmap.Config.RGB_565), MinFilter.NEAREST, MagFilter.NEAREST);
        mTurnQuad.getMaterial().addTexture(textureRight);
    }

    @Override
    public void onInitialized(Node engineRoot, int width, int height) {
        mAndroidRenderNode = new AndroidRenderNode(width, height);
        mAndroidRenderNode.addChild(mMoveQuad);
        mAndroidRenderNode.addChild(mTurnQuad);

        float quadWidth = width / 2;
        float quadHeight = height / 4;
        QuadMesh quadMesh = new QuadMesh(quadWidth, quadHeight, "a_Pos", "a_texCoord");
        mMoveQuad.setMesh(quadMesh);
        mMoveQuad.getModelTransform().translate(quadWidth / 2, height - quadHeight / 2, 0);

        mTurnQuad.setMesh(quadMesh);
        mTurnQuad.getModelTransform().translate(width - quadWidth / 2, height - quadHeight / 2, 0);

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

                Ray ray = mTouchToRay.getRay(mAndroidRenderNode.getCamera(), x, y);

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

package com.sweetlab.sweetride.tryouts.strips;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.sweetlab.sweetride.R;
import com.sweetlab.sweetride.UserApplication;
import com.sweetlab.sweetride.attributedata.IndicesBuffer;
import com.sweetlab.sweetride.attributedata.InterleavedVertexBuffer;
import com.sweetlab.sweetride.attributedata.TextureCoordData;
import com.sweetlab.sweetride.attributedata.VerticesData;
import com.sweetlab.sweetride.camera.Camera;
import com.sweetlab.sweetride.camera.Frustrum;
import com.sweetlab.sweetride.context.BufferUsage;
import com.sweetlab.sweetride.context.MagFilter;
import com.sweetlab.sweetride.context.MeshDrawingMode;
import com.sweetlab.sweetride.context.MinFilter;
import com.sweetlab.sweetride.demo.AssetsLoader;
import com.sweetlab.sweetride.engine.frame.Frame;
import com.sweetlab.sweetride.game.terrain.FlatGrid;
import com.sweetlab.sweetride.geometry.Geometry;
import com.sweetlab.sweetride.material.Material;
import com.sweetlab.sweetride.mesh.Mesh;
import com.sweetlab.sweetride.node.Node;
import com.sweetlab.sweetride.node.rendersettings.ClearBit;
import com.sweetlab.sweetride.rendernode.DefaultRenderNode;
import com.sweetlab.sweetride.shader.FragmentShader;
import com.sweetlab.sweetride.shader.ShaderProgram;
import com.sweetlab.sweetride.shader.VertexShader;
import com.sweetlab.sweetride.testframework.OpenGLTestCase;
import com.sweetlab.sweetride.testframework.ResultRunnable;
import com.sweetlab.sweetride.texture.Texture2D;

import rx.functions.Action1;

/**
 * Test strip winding.
 */
public class FlatGridTest extends OpenGLTestCase {
    private static final int SAMPLES_X = 16;
    private static final int SAMPLES_Z = 16;

    public static final String VERTEX =
            "attribute vec4 a_Pos; \n" +
                    "attribute vec2 a_texCoord;\n" +
                    "varying vec2 v_texCoord;\n" +
                    "uniform mat4 u_worldViewProjMat; \n" +
                    "void main() { " +
                    "    v_texCoord = a_texCoord;\n" +
                    "    gl_Position = u_worldViewProjMat * a_Pos; " +
                    "} ";

    public static final String FRAGMENT =
            "precision mediump float;\n" +
                    "varying vec2 v_texCoord;\n" +
                    "uniform sampler2D s_texture;\n" +
                    "void main() {\n" +
                    "vec4 color = texture2D(s_texture, v_texCoord);\n" +
                    "gl_FragColor = color;\n" +
                    "}";

    private Frame mFrame = new Frame();
    private DefaultRenderNode mRenderNode = new DefaultRenderNode();
    private Geometry mGeometry = new Geometry();
    private Camera mCamera = new Camera();
    private UserApplication mDummyApp = new DummyApp();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        /**
         * Render settings.
         */
        mRenderNode.getRenderSettings().setClear(0, ClearBit.COLOR_BUFFER_BIT, ClearBit.DEPTH_BUFFER_BIT);
        mRenderNode.getRenderSettings().setClearColor(new float[]{0.5f, 0.5f, 0.5f, 1.0f});
        mRenderNode.getRenderSettings().setViewPort(0, 0, getSurfaceWidth(), getSurfaceHeight());
        mRenderNode.getRenderSettings().setCullFace(true);

        /**
         * Camera.
         */
        mCamera.lookAt(0, 6, 6, 0, 0, 0);
        mCamera.getFrustrum().setPerspectiveProjection(45, Frustrum.FovType.AUTO_FIT, 0.1f, 10, getSurfaceWidth(), getSurfaceHeight());

        /**
         * Graph.
         */
        mRenderNode.setCamera(mCamera);
        mRenderNode.addChild(mGeometry);

        /**
         * Geometry material.
         */
        final Material material = new Material();
        material.setShaderProgram(new ShaderProgram(new VertexShader(VERTEX), new FragmentShader(FRAGMENT)));
        mGeometry.setMaterial(material);

        InterleavedVertexBuffer.Builder builder = new InterleavedVertexBuffer.Builder(BufferUsage.STATIC);

        /**
         * Flat grid, left, right near far.
         */
        FlatGrid flatGrid = new FlatGrid(-1, 1, 1, -1, SAMPLES_X, SAMPLES_Z);

        /**
         * Vertices data.
         */
        builder.add("a_Pos", new VerticesData(flatGrid.getVertices()));

        /**
         * Bitmap
         */
        AssetsLoader assetsLoader = new AssetsLoader(getActivity().getApplicationContext());
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        assetsLoader.loadBitmap(R.drawable.felix, options).subscribe(new Action1<Bitmap>() {
            @Override
            public void call(Bitmap bitmap) {
                material.addTexture(new Texture2D("s_texture", bitmap, MinFilter.NEAREST, MagFilter.NEAREST));
            }
        });

        /**
         * Texture coordinates.
         */
        TextureCoordData textureCoordData = new TextureCoordData(flatGrid.getTextureCoordinates());
        builder.add("a_texCoord", textureCoordData);

        /**
         * Mesh.
         */
        Mesh mesh = new Mesh(MeshDrawingMode.TRIANGLE_STRIP);
        mesh.addVertexBuffer(builder.build());
        mesh.setIndicesBuffer(new IndicesBuffer(flatGrid.getIndices(), BufferUsage.STATIC));
        mGeometry.setMesh(mesh);
    }

    public void testStrip() throws Exception {
        mFrame.update(mDummyApp, mRenderNode);

        for (int i = 0; i < 1; i++) {
            runOnDrawFrame(new ResultRunnable() {
                @Override
                public Object run() {
                    mFrame.render(getBackendContext());
                    return null;
                }
            });
        }
        sleepOnDrawFrame(4000);
    }


    private class DummyApp extends UserApplication {
        @Override
        public void onInitialized(Node engineRoot, int width, int height) {
        }

        @Override
        public void onSurfaceChanged(int width, int height) {
        }

        @Override
        public void onUpdate(float dt) {
        }
    }
}

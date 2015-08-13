package com.sweetlab.sweetride.tryouts.strips;

import com.sweetlab.sweetride.UserApplication;
import com.sweetlab.sweetride.attributedata.ColorData;
import com.sweetlab.sweetride.attributedata.IndicesBuffer;
import com.sweetlab.sweetride.attributedata.InterleavedVertexBuffer;
import com.sweetlab.sweetride.attributedata.VertexData;
import com.sweetlab.sweetride.attributedata.VerticesData;
import com.sweetlab.sweetride.camera.Camera;
import com.sweetlab.sweetride.camera.Frustrum;
import com.sweetlab.sweetride.context.BufferUsage;
import com.sweetlab.sweetride.context.MeshDrawingMode;
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

import java.util.Random;

/**
 * Test strip winding.
 */
public class FlatGridTest extends OpenGLTestCase {
    private static final int SAMPLES_X = 16;
    private static final int SAMPLES_Z = 16;

    public static final String VERTEX =
            "attribute vec4 a_Pos; \n" +
                    "attribute vec4 a_Color;\n" +
                    "varying vec4 v_Color;\n" +
                    "uniform mat4 u_worldViewProjMat; \n" +
                    "void main() { " +
                    "    v_Color = a_Color;\n" +
                    "    gl_Position = u_worldViewProjMat * a_Pos; " +
                    "} ";

    public static final String FRAGMENT =
            "precision mediump float;\n" +
                    "varying vec4 v_Color; \n" +
                    "void main() {\n" +
                    "\tgl_FragColor = v_Color;\n" +
                    "}";

    private Frame mFrame = new Frame();
    private DefaultRenderNode mRenderNode = new DefaultRenderNode();
    private Geometry mGeometry = new Geometry();
    private Camera mCamera = new Camera();
    private UserApplication mDummyApp = new DummyApp();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mRenderNode.getRenderSettings().setClear(0, ClearBit.COLOR_BUFFER_BIT, ClearBit.DEPTH_BUFFER_BIT);
        mRenderNode.getRenderSettings().setClearColor(new float[]{0.5f, 0.5f, 0.5f, 1.0f});
        mRenderNode.getRenderSettings().setViewPort(0, 0, getSurfaceWidth(), getSurfaceHeight());
        mRenderNode.getRenderSettings().setCullFace(true);

        mCamera.lookAt(0, 6, 6, 0, 0, 0);
        mCamera.getFrustrum().setPerspectiveProjection(45, Frustrum.FovType.AUTO_FIT, 0.1f, 10, getSurfaceWidth(), getSurfaceHeight());
        mRenderNode.setCamera(mCamera);

        mRenderNode.addChild(mGeometry);

        Material material = new Material();
        mGeometry.setMaterial(material);
        material.setShaderProgram(new ShaderProgram(new VertexShader(VERTEX), new FragmentShader(FRAGMENT)));

        Mesh mesh = new Mesh(MeshDrawingMode.TRIANGLE_STRIP);
        InterleavedVertexBuffer.Builder builder = new InterleavedVertexBuffer.Builder(BufferUsage.STATIC);

        // left , top, right, bottom
        FlatGrid flatGrid = new FlatGrid(-1, 1, 1, -1, SAMPLES_X, SAMPLES_Z);
        VerticesData verticesData = new VerticesData(flatGrid.getVertices());
        builder.add("a_Pos", verticesData);

        float[] colors = new float[SAMPLES_X * SAMPLES_Z * 4];
        int colorIndex = 0;
        Random random = new Random(444);
        for (int y = 0; y < SAMPLES_Z; y++) {
            for (int x = 0; x < SAMPLES_X; x++) {
                colors[colorIndex++] = random.nextFloat();
                colors[colorIndex++] = random.nextFloat();
                colors[colorIndex++] = random.nextFloat();
                colors[colorIndex++] = 1;
            }
        }
        VertexData colorData = new ColorData(colors);
        builder.add("a_Color", colorData);

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

package com.sweetlab.sweetride.uniform;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.sweetlab.sweetride.Util.CollectorUtil;
import com.sweetlab.sweetride.attributedata.IndicesBuffer;
import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.context.BufferUsage;
import com.sweetlab.sweetride.context.MagFilter;
import com.sweetlab.sweetride.context.MeshDrawingMode;
import com.sweetlab.sweetride.context.MinFilter;
import com.sweetlab.sweetride.Util.BitmapTestUtil;
import com.sweetlab.sweetride.Util.BufferTestUtil;
import com.sweetlab.sweetride.Util.ProgramTestUtil;
import com.sweetlab.sweetride.Util.Verify;
import com.sweetlab.sweetride.engine.DefaultRenderNode;
import com.sweetlab.sweetride.engine.FrontEndActionHandler;
import com.sweetlab.sweetride.geometry.Geometry;
import com.sweetlab.sweetride.material.Material;
import com.sweetlab.sweetride.math.Camera;
import com.sweetlab.sweetride.math.Frustrum;
import com.sweetlab.sweetride.mesh.Mesh;
import com.sweetlab.sweetride.resource.TextureResource;
import com.sweetlab.sweetride.resource.VertexBufferResource;
import com.sweetlab.sweetride.shader.ShaderProgram;
import com.sweetlab.sweetride.testframework.OpenGLTestCase;
import com.sweetlab.sweetride.testframework.ResultRunnable;
import com.sweetlab.sweetride.texture.Texture2D;

import java.util.List;

/**
 * Note, a bit of over complicated way of testing a custom float uniform.
 * <p/>
 * This test simulates what an engine normally would do.
 * 1) Loop 30 frames
 * 2) Drive application updates
 * 3) Handle notification on post update.
 * 4) Handle gl notification on pre draw
 * 4) Draw.
 * <p/>
 * But basically, add uniform to geometry. Set value to it.
 * <p/>
 * Test custom float uniform that can be added to a geometry.
 * The test writes a project view matrix to the shader mUniform.
 * The test succeeds if the quad is rendered with correct projection (being a square).
 */
public class FloatUniformTest extends OpenGLTestCase {
    /**
     * The backend context.
     */
    private BackendContext mContext;

    /**
     * The render node.
     */
    private DefaultRenderNode mRenderNode;

    /**
     * The uniform under test.
     */
    private FloatUniform mUniform;

    /**
     * Front end action handler.
     */
    FrontEndActionHandler mActionHandler = new FrontEndActionHandler();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        /**
         * Acquire the backend context.
         */
        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                mContext = getBackendContext();
                return null;
            }
        });

        /**
         * Create primitive resources and the geometry. A quad with two textures.
         */
        IndicesBuffer indicesBuffer = new IndicesBuffer(new short[]{0, 1, 2, 3}, BufferUsage.STATIC);
        ShaderProgram program = ProgramTestUtil.createCameraOneTexCoordTwoTextures();
        VertexBufferResource buffer = BufferTestUtil.createInterleavedQuadWithTextureCoords();
        TextureResource tex1 = new Texture2D("s_texture", BitmapTestUtil.createQuadColorBitmap(Bitmap.Config.ARGB_8888), MinFilter.NEAREST, MagFilter.NEAREST);
        TextureResource tex2 = new Texture2D("s_textureChess", BitmapTestUtil.createChessColorBitmap(Bitmap.Config.ARGB_8888), MinFilter.NEAREST, MagFilter.NEAREST);

        Geometry geometry = new Geometry();
        geometry.setMaterial(new Material());
        geometry.setMesh(new Mesh(MeshDrawingMode.TRIANGLE_STRIP));

        geometry.getMaterial().setShaderProgram(program);
        geometry.getMaterial().addTexture(tex1);
        geometry.getMaterial().addTexture(tex2);

        geometry.getMesh().addVertexBuffer(buffer);
        geometry.getMesh().setIndicesBuffer(indicesBuffer);

        /**
         * Create custom float uniform that will write to a mat4 uniform in shader.
         */
        Camera camera = new Camera();
        camera.lookAt(0, 0, -3, 0, 0, 0);
        camera.getFrustrum().setPerspectiveProjection(90, Frustrum.FovType.AUTO_FIT, 0.1f, 10, getSurfaceWidth(), getSurfaceHeight());

        mUniform = new FloatUniform("u_Cam");
        mUniform.set(camera.getViewProjectionMatrix().m);
        geometry.addUniform(mUniform);

        /**
         * Create render node and attach geometry.
         */
        mRenderNode = new DefaultRenderNode();
        mRenderNode.addChild(geometry);

        /**
         * Handle notifications. Simulates engine post update(dt).
         */
        List<Geometry> geometries = CollectorUtil.collectGeometries(mRenderNode);
        for (Geometry g : geometries) {
            mActionHandler.handleActions(g);
        }
    }

    public void testFloatUniform() {
        /**
         * Run 30 frames.
         */
        for (int i = 0; i < 30; i++) {
            /**
             * Simulate engine update(dt) loop.
             */
            Camera camera = new Camera();
            camera.lookAt(0, 0, -3f / (i * 0.1f), 0, 0, 0);
            camera.getFrustrum().setPerspectiveProjection(90, Frustrum.FovType.AUTO_FIT, 0.1f, 10, getSurfaceWidth(), getSurfaceHeight());
            mUniform.set(camera.getViewProjectionMatrix().m);

            /**
             * Handle notifications. Simulating engine post update(dt).
             */
            List<Geometry> geometries = CollectorUtil.collectGeometries(mRenderNode);
            for (Geometry g : geometries) {
                mActionHandler.handleActions(g);
            }

            /**
             * Pass over to GL thread.
             */
            runOnDrawFrame(new ResultRunnable() {
                @Override
                public Object run() {
                    /**
                     * Clear screen.
                     */
                    clearScreen(0.5f, 0.5f, 0.5f, 1.0f);

                    /**
                     * Handle GL thread notifications. Simulate engine pre draw.
                     */
                    List<Geometry> geometries = CollectorUtil.collectGeometries(mRenderNode);
                    for (Geometry g : geometries) {
                        mContext.getActionHandler().handleActions(g);
                    }

                    /**
                     * Draw.
                     */
                    mRenderNode.getRenderer().render(mContext, CollectorUtil.collectGeometries(mRenderNode));
                    return null;
                }
            });
        }
        sleepOnDrawFrame(Verify.TERMINATE_TIME);
    }
}
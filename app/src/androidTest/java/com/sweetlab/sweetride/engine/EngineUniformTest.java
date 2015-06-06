package com.sweetlab.sweetride.engine;

import android.graphics.Bitmap;

import com.sweetlab.sweetride.Util.BitmapTestUtil;
import com.sweetlab.sweetride.Util.BufferTestUtil;
import com.sweetlab.sweetride.Util.CollectorUtil;
import com.sweetlab.sweetride.Util.ProgramTestUtil;
import com.sweetlab.sweetride.Util.Verify;
import com.sweetlab.sweetride.attributedata.IndicesBuffer;
import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.context.BufferUsage;
import com.sweetlab.sweetride.context.MagFilter;
import com.sweetlab.sweetride.context.MeshDrawingMode;
import com.sweetlab.sweetride.context.MinFilter;
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
 * This test simulates what an engine normally would do.
 * 1) Loop 30 frames
 * 2) Drive application updates
 * 3) Handle notification on post update.
 * 4) Handle gl notification on pre draw
 * 4) Draw.
 * <p/>
 * Steps.
 * <p/>
 * 1. Create render node.
 * 2. Set camera on it
 * 3. Create and attach geometry with a shader that uses a engine uniform.
 * 4. Draw and observe that the quad is squarish
 * 5. Modify camera and observe that view is modified.
 */
public class EngineUniformTest extends OpenGLTestCase {
    /**
     * The backend context.
     */
    private BackendContext mContext;

    /**
     * The render node.
     */
    private DefaultRenderNode mRenderNode;

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
        ShaderProgram program = ProgramTestUtil.createEngineOneTexCoordTwoTextures();
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
         * Create render node and attach geometry.
         */
        mRenderNode = new DefaultRenderNode();
        mRenderNode.addChild(geometry);

        /**
         * Create camera and set on render node.
         */
        Camera camera = new Camera();
        camera.lookAt(0, 0, -3, 0, 0, 0);
        camera.getFrustrum().setPerspectiveProjection(90, Frustrum.FovType.AUTO_FIT, 0.1f, 10, getSurfaceWidth(), getSurfaceHeight());
        mRenderNode.setCamera(camera);

        /**
         * Handle notifications. Simulates engine post update(dt).
         */
        List<Geometry> geometries = CollectorUtil.collectGeometries(mRenderNode);
        for (Geometry g : geometries) {
            mActionHandler.handleActions(g);
        }
    }

    public void testEngineUniform() {
        /**
         * Run 30 frames.
         */
        for (int i = 0; i < 30; i++) {
            /**
             * Simulate engine update(dt) loop.
             */
            Camera camera = mRenderNode.getCamera();
            if (camera!= null) {
                camera.lookAt(0, 0, -3f / (i * 0.1f), 0, 0, 0);
                camera.getFrustrum().setPerspectiveProjection(90, Frustrum.FovType.AUTO_FIT, 0.1f, 10, getSurfaceWidth(), getSurfaceHeight());
            }

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
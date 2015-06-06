package com.sweetlab.sweetride.context;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.sweetlab.sweetride.attributedata.IndicesBuffer;
import com.sweetlab.sweetride.Util.BitmapTestUtil;
import com.sweetlab.sweetride.Util.BufferTestUtil;
import com.sweetlab.sweetride.Util.DrawTestUtil;
import com.sweetlab.sweetride.Util.ProgramTestUtil;
import com.sweetlab.sweetride.Util.Verify;
import com.sweetlab.sweetride.engine.FrontEndActionHandler;
import com.sweetlab.sweetride.material.Material;
import com.sweetlab.sweetride.mesh.Mesh;
import com.sweetlab.sweetride.testframework.OpenGLTestCase;
import com.sweetlab.sweetride.testframework.ResultRunnable;
import com.sweetlab.sweetride.texture.Texture2D;

public class TextureMulti_MatMesh_mainNotifier extends OpenGLTestCase {
    /**
     * The material.
     */
    private Material mMaterial;

    /**
     * The mesh.
     */
    private Mesh mMesh;

    /**
     * The backend context.
     */
    private BackendContext mContext;

    /**
     * Front end action handler.
     */
    FrontEndActionHandler mActionHandler = new FrontEndActionHandler();

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mMaterial = new Material();
        mMaterial.setShaderProgram(ProgramTestUtil.createNdcOneTexCoordTwoTextures());
        Texture2D texture1 = new Texture2D("s_texture", BitmapTestUtil.createQuadColorBitmap(Bitmap.Config.ARGB_8888), MinFilter.NEAREST, MagFilter.NEAREST);
        mMaterial.addTexture(texture1);
        Texture2D texture2 = new Texture2D("s_textureChess", BitmapTestUtil.createChessColorBitmap(Bitmap.Config.ARGB_8888), MinFilter.NEAREST, MagFilter.NEAREST);
        mMaterial.addTexture(texture2);
        mActionHandler.handleActions(mMaterial);

        mMesh = new Mesh(MeshDrawingMode.TRIANGLE_STRIP);
        mMesh.setIndicesBuffer(new IndicesBuffer(new short[]{0, 1, 2, 3}, BufferUsage.STATIC));
        mMesh.addVertexBuffer(BufferTestUtil.createInterleavedQuadWithTextureCoords());
        mActionHandler.handleActions(mMesh);

        setTestInfo("Multi texture with mesh and material");

        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                mContext = getBackendContext();

                mMaterial.create(mContext);
                mMesh.create(mContext);

                mMaterial.load(mContext);
                mMesh.load(mContext);
                return null;
            }
        });

    }

    /**
     * Draw textures quad interleaved.
     *
     * @throws Exception
     */
    public void testTextureMulti_MatMesh_mainNotifier() throws Exception {
        runOnDrawFrame(new ResultRunnable() {
            @Override
            public Object run() {
                /**
                 * Clear screen.
                 */
                clearScreen(0.5f, 0.5f, 0.5f, 1.0f);

                /**
                 * This quad should be drawn centered and contain 4 primary colors with additional chess color board added.
                 */
                DrawTestUtil.drawUsingMaterialAndMesh(mContext, mMaterial, mMesh);
                return null;
            }
        });
        sleepOnDrawFrame(Verify.TERMINATE_TIME);
    }
}
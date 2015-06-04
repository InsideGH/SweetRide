package com.sweetlab.sweetride.context;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.sweetlab.sweetride.attributedata.IndicesBuffer;
import com.sweetlab.sweetride.context.Util.ActionHelper;
import com.sweetlab.sweetride.context.Util.BitmapTestUtil;
import com.sweetlab.sweetride.context.Util.BufferTestUtil;
import com.sweetlab.sweetride.context.Util.DrawTestUtil;
import com.sweetlab.sweetride.context.Util.ProgramTestUtil;
import com.sweetlab.sweetride.material.Material;
import com.sweetlab.sweetride.mesh.Mesh;
import com.sweetlab.sweetride.testframework.OpenGLTestCase;
import com.sweetlab.sweetride.testframework.ResultRunnable;
import com.sweetlab.sweetride.texture.Texture2D;

public class TextureOne_MatMesh_notifier extends OpenGLTestCase {
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

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mMaterial = new Material();
        mMaterial.setShaderProgram(ProgramTestUtil.createNdcOneTexCoordOneTexture());
        Texture2D texture = new Texture2D("s_texture", BitmapTestUtil.createQuadColorBitmap(Bitmap.Config.ARGB_8888), MinFilter.NEAREST, MagFilter.NEAREST);
        mMaterial.addTexture(texture);
        ActionHelper.handleMainThreadActions(mMaterial);

        mMesh = new Mesh(MeshDrawingMode.TRIANGLE_STRIP);
        mMesh.setIndicesBuffer(new IndicesBuffer(new short[]{0, 1, 2, 3}, GLES20.GL_STATIC_DRAW));
        mMesh.addVertexBuffer(BufferTestUtil.createInterleavedQuadWithTextureCoords());
        ActionHelper.handleMainThreadActions(mMesh);

        setTestInfo("Single texture with mesh and material");

        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                mContext = getBackendContext();

                ActionHelper.handleGLThreadActions(mMaterial,mContext);
                ActionHelper.handleGLThreadActions(mMesh,mContext);
                assertFalse(mMaterial.hasActions());
                assertFalse(mMesh.hasActions());
                return null;
            }
        });

    }

    /**
     * Draw textures quad interleaved.
     *
     * @throws Exception
     */
    public void testTextureOne_MatMesh_notifier() throws Exception {
        runOnDrawFrame(new ResultRunnable() {
            @Override
            public Object run() {
                /**
                 * Clear screen.
                 */
                clearScreen(0.5f, 0.5f, 0.5f, 1.0f);

                /**
                 * This quad should be drawn centered.
                 */
                DrawTestUtil.drawUsingMaterialAndMesh(mContext, mMaterial, mMesh);
                return null;
            }
        });
        sleepOnDrawFrame(2000);
    }
}
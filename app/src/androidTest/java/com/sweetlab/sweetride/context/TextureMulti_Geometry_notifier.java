package com.sweetlab.sweetride.context;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.sweetlab.sweetride.attributedata.IndicesBuffer;
import com.sweetlab.sweetride.Util.BitmapTestUtil;
import com.sweetlab.sweetride.Util.BufferTestUtil;
import com.sweetlab.sweetride.Util.ProgramTestUtil;
import com.sweetlab.sweetride.Util.Verify;
import com.sweetlab.sweetride.engine.FrontEndActionHandler;
import com.sweetlab.sweetride.geometry.Geometry;
import com.sweetlab.sweetride.material.Material;
import com.sweetlab.sweetride.mesh.Mesh;
import com.sweetlab.sweetride.testframework.OpenGLTestCase;
import com.sweetlab.sweetride.testframework.ResultRunnable;
import com.sweetlab.sweetride.texture.Texture2D;

public class TextureMulti_Geometry_notifier extends OpenGLTestCase {

    /**
     * The geometry.
     */
    private Geometry mGeometry = new Geometry();

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

        Material material = new Material();
        material.setShaderProgram(ProgramTestUtil.createNdcOneTexCoordTwoTextures());

        Texture2D texture1 = new Texture2D("s_texture", BitmapTestUtil.createQuadColorBitmap(Bitmap.Config.ARGB_8888), MinFilter.NEAREST, MagFilter.NEAREST);
        material.addTexture(texture1);

        Texture2D texture2 = new Texture2D("s_textureChess", BitmapTestUtil.createChessColorBitmap(Bitmap.Config.ARGB_8888), MinFilter.NEAREST, MagFilter.NEAREST);
        material.addTexture(texture2);

        mGeometry.setMaterial(material);

        Mesh mesh = new Mesh(MeshDrawingMode.TRIANGLE_STRIP);
        mesh.setIndicesBuffer(new IndicesBuffer(new short[]{0, 1, 2, 3}, BufferUsage.STATIC));
        mesh.addVertexBuffer(BufferTestUtil.createInterleavedQuadWithTextureCoords());

        mGeometry.setMesh(mesh);
        mActionHandler.handleActions(mGeometry);

        setTestInfo("Multi texture with geometry");

        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                mContext = getBackendContext();
                mContext.getActionHandler().handleActions(mGeometry);
                assertFalse(mGeometry.hasActions());

                return null;
            }
        });

    }

    /**
     * Draw textures quad interleaved.
     *
     * @throws Exception
     */
    public void testTextureMulti_Geometry_notifier() throws Exception {
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
                mGeometry.draw(mContext);
                return null;
            }
        });
        sleepOnDrawFrame(Verify.TERMINATE_TIME);
    }
}
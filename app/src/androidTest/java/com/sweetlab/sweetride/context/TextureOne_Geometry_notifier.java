package com.sweetlab.sweetride.context;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.sweetlab.sweetride.attributedata.IndicesBuffer;
import com.sweetlab.sweetride.context.Util.ActionHelper;
import com.sweetlab.sweetride.context.Util.BitmapTestUtil;
import com.sweetlab.sweetride.context.Util.BufferTestUtil;
import com.sweetlab.sweetride.context.Util.ProgramTestUtil;
import com.sweetlab.sweetride.geometry.Geometry;
import com.sweetlab.sweetride.material.Material;
import com.sweetlab.sweetride.mesh.Mesh;
import com.sweetlab.sweetride.testframework.OpenGLTestCase;
import com.sweetlab.sweetride.testframework.ResultRunnable;
import com.sweetlab.sweetride.texture.Texture2D;

public class TextureOne_Geometry_notifier extends OpenGLTestCase {

    /**
     * The geometry.
     */
    private Geometry mGeometry = new Geometry();

    /**
     * The backend context.
     */
    private BackendContext mContext;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Material material = new Material();
        material.setShaderProgram(ProgramTestUtil.createNdcOneTexCoordOneTexture());
        Texture2D texture = new Texture2D("s_texture", BitmapTestUtil.createQuadColorBitmap(Bitmap.Config.ARGB_8888), MinFilter.NEAREST, MagFilter.NEAREST);
        material.addTexture(texture);

        Mesh mesh = new Mesh(MeshDrawingMode.TRIANGLE_STRIP);
        mesh.setIndicesBuffer(new IndicesBuffer(new short[]{0, 1, 2, 3}, GLES20.GL_STATIC_DRAW));
        mesh.addVertexBuffer(BufferTestUtil.createInterleavedQuadWithTextureCoords());

        mGeometry.setMaterial(material);
        mGeometry.setMesh(mesh);
        ActionHelper.handleMainThreadActions(mGeometry);

        setTestInfo("Single texture with geometry");

        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                mContext = getBackendContext();

                ActionHelper.handleGLThreadActions(mGeometry,mContext);
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
    public void testTextureOne_Geometry_notifier() throws Exception {
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
                mGeometry.draw(mContext);
                return null;
            }
        });
        sleepOnDrawFrame(2000);
    }
}
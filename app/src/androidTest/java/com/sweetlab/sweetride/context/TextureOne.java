package com.sweetlab.sweetride.context;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.sweetlab.sweetride.attributedata.IndicesBuffer;
import com.sweetlab.sweetride.attributedata.InterleavedVertexBuffer;
import com.sweetlab.sweetride.context.Util.BitmapTestUtil;
import com.sweetlab.sweetride.context.Util.BufferTestUtil;
import com.sweetlab.sweetride.context.Util.DrawTestUtil;
import com.sweetlab.sweetride.context.Util.ProgramTestUtil;
import com.sweetlab.sweetride.resource.TextureResource;
import com.sweetlab.sweetride.shader.ShaderProgram;
import com.sweetlab.sweetride.testframework.OpenGLTestCase;
import com.sweetlab.sweetride.testframework.ResultRunnable;
import com.sweetlab.sweetride.texture.Texture2D;

public class TextureOne extends OpenGLTestCase {
    /**
     * Shader program with texture sampler.
     */
    private ShaderProgram mShaderProgram;

    /**
     * Indices buffer.
     */
    private IndicesBuffer mIb;

    /**
     * Interleaved texture buffer with vertices and texture coordinates.
     */
    private InterleavedVertexBuffer mVertexBuffer;

    /**
     * A texture with 4 colors/pixels.
     */
    private TextureResource mTexture;

    /**
     * The backend context.
     */
    private BackendContext mContext;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mIb = new IndicesBuffer(new short[]{0, 1, 2, 3}, GLES20.GL_STATIC_DRAW);
        mShaderProgram = ProgramTestUtil.createNdcOneTexCoordOneTexture();
        mVertexBuffer = BufferTestUtil.createInterleavedQuadWithTextureCoords();
        mTexture = new Texture2D("s_texture", BitmapTestUtil.createQuadColorBitmap(Bitmap.Config.ARGB_8888), MinFilter.NEAREST, MagFilter.NEAREST);

        setTestInfo("Single texture with backend");

        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                mContext = getBackendContext();

                /**
                 * Link the shader program.
                 */
                mShaderProgram.create(mContext);

                /**
                 * Create indices buffer.
                 */
                mIb.create(mContext);

                /**
                 * Create vertex buffer.
                 */
                mVertexBuffer.create(mContext);

                /**
                 * Create texture.
                 */
                mTexture.create(mContext);

                /**
                 * Load indices to gpu.
                 */
                mIb.load(mContext);

                /**
                 * Load interleaved vertex buffer to gpu.
                 */
                mVertexBuffer.load(mContext);

                /**
                 * Load texture and set filter.
                 */
                mTexture.load(mContext);
                return null;
            }
        });

    }

    /**
     * Draw textures quad interleaved.
     *
     * @throws Exception
     */
    public void testTextureOne() throws Exception {
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
                DrawTestUtil.drawArrayInterleavedBufferWithTexture(mContext, mShaderProgram, mVertexBuffer, mTexture);
                return null;
            }
        });
        sleepOnDrawFrame(2000);
    }
}
package com.sweetlab.sweetride.context;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.sweetlab.sweetride.attributedata.IndicesBuffer;
import com.sweetlab.sweetride.attributedata.InterleavedVertexBuffer;
import com.sweetlab.sweetride.Util.BitmapTestUtil;
import com.sweetlab.sweetride.Util.BufferTestUtil;
import com.sweetlab.sweetride.Util.DrawTestUtil;
import com.sweetlab.sweetride.Util.ProgramTestUtil;
import com.sweetlab.sweetride.Util.Verify;
import com.sweetlab.sweetride.resource.TextureResource;
import com.sweetlab.sweetride.shader.ShaderProgram;
import com.sweetlab.sweetride.testframework.OpenGLTestCase;
import com.sweetlab.sweetride.testframework.ResultRunnable;
import com.sweetlab.sweetride.texture.Texture2D;

public class TextureMulti_glNotifier extends OpenGLTestCase {
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
     * A texture with chess like colors/pixels.
     */
    private TextureResource mTextureChess;

    /**
     * The backend context.
     */
    private BackendContext mContext;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mIb = new IndicesBuffer(new short[]{0, 1, 2, 3}, BufferUsage.STATIC);
        mShaderProgram = ProgramTestUtil.createNdcOneTexCoordTwoTextures();
        mVertexBuffer = BufferTestUtil.createInterleavedQuadWithTextureCoords();

        mTexture = new Texture2D("s_texture", BitmapTestUtil.createQuadColorBitmap(Bitmap.Config.ARGB_8888), MinFilter.NEAREST, MagFilter.NEAREST);

        mTextureChess = new Texture2D("s_textureChess", BitmapTestUtil.createChessColorBitmap(Bitmap.Config.ARGB_8888), MinFilter.NEAREST, MagFilter.NEAREST);

        setTestInfo("Multi texture with backend");

        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                mContext = getBackendContext();

                mContext.getActionHandler().handleActions(mShaderProgram);
                mContext.getActionHandler().handleActions(mIb);
                mContext.getActionHandler().handleActions(mVertexBuffer);
                mContext.getActionHandler().handleActions(mTexture);
                mContext.getActionHandler().handleActions(mTextureChess);
                assertFalse(mShaderProgram.hasActions());
                assertFalse(mIb.hasActions());
                assertFalse(mVertexBuffer.hasActions());
                assertFalse(mTexture.hasActions());
                assertFalse(mTextureChess.hasActions());
                return null;
            }
        });

    }

    /**
     * Draw textures quad interleaved.
     *
     * @throws Exception
     */
    public void testTextureMulti_glNotifier() throws Exception {
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
                DrawTestUtil.drawElementsInterleavedBufferWithDualTextures(mContext, mShaderProgram, mIb, mVertexBuffer, mTexture, mTextureChess);
                return null;
            }
        });
        sleepOnDrawFrame(Verify.TERMINATE_TIME);
    }
}
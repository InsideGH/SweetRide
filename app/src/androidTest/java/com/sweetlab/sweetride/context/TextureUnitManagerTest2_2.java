package com.sweetlab.sweetride.context;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.sweetlab.sweetride.attributedata.IndicesBuffer;
import com.sweetlab.sweetride.attributedata.InterleavedVertexBuffer;
import com.sweetlab.sweetride.context.Util.ActionHelper;
import com.sweetlab.sweetride.context.Util.BitmapTestUtil;
import com.sweetlab.sweetride.context.Util.BufferTestUtil;
import com.sweetlab.sweetride.context.Util.DrawTestUtil;
import com.sweetlab.sweetride.context.Util.ProgramTestUtil;
import com.sweetlab.sweetride.resource.TextureResource;
import com.sweetlab.sweetride.shader.ShaderProgram;
import com.sweetlab.sweetride.testframework.OpenGLTestCase;
import com.sweetlab.sweetride.testframework.ResultRunnable;
import com.sweetlab.sweetride.texture.Texture2D;

/**
 * Test using textures.
 */
public class TextureUnitManagerTest2_2 extends OpenGLTestCase {
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
        mIb = new IndicesBuffer(new short[]{0, 1, 2, 3}, GLES20.GL_STATIC_DRAW);
        mShaderProgram = ProgramTestUtil.createNdcOneTexCoordTwoTextures();
        mVertexBuffer = BufferTestUtil.createInterleavedQuadWithTextureCoords();

        mTexture = new Texture2D("s_texture", BitmapTestUtil.createQuadColorBitmap(Bitmap.Config.ARGB_8888), MinFilter.NEAREST, MagFilter.NEAREST);

        mTextureChess = new Texture2D("s_textureChess", BitmapTestUtil.createChessColorBitmap(Bitmap.Config.ARGB_8888), MinFilter.NEAREST, MagFilter.NEAREST);

        setTestInfo("Multi texture with backend");

        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                mContext = getBackendContext();

                ActionHelper.handleGLThreadActions(mShaderProgram, mContext);
                ActionHelper.handleGLThreadActions(mIb, mContext);
                ActionHelper.handleGLThreadActions(mVertexBuffer, mContext);
                ActionHelper.handleGLThreadActions(mTexture, mContext);
                ActionHelper.handleGLThreadActions(mTextureChess, mContext);
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
    public void testMultiTexture() throws Exception {
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
        sleepOnDrawFrame(2000);
    }
}
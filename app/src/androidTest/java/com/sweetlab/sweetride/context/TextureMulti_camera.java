package com.sweetlab.sweetride.context;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.sweetlab.sweetride.attributedata.IndicesBuffer;
import com.sweetlab.sweetride.attributedata.InterleavedVertexBuffer;
import com.sweetlab.sweetride.context.Util.BitmapTestUtil;
import com.sweetlab.sweetride.context.Util.BufferTestUtil;
import com.sweetlab.sweetride.context.Util.DrawTestUtil;
import com.sweetlab.sweetride.context.Util.ProgramTestUtil;
import com.sweetlab.sweetride.math.Camera;
import com.sweetlab.sweetride.math.Frustrum;
import com.sweetlab.sweetride.math.Vec3;
import com.sweetlab.sweetride.resource.TextureResource;
import com.sweetlab.sweetride.shader.ShaderProgram;
import com.sweetlab.sweetride.testframework.OpenGLTestCase;
import com.sweetlab.sweetride.testframework.ResultRunnable;
import com.sweetlab.sweetride.texture.Texture2D;

public class TextureMulti_camera extends OpenGLTestCase {
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

    /**
     * Vector used while moving.
     */
    private Vec3 mPos = new Vec3(0, 0, -3f);

    /**
     * Vector used to make camera strafe.
     */
    private Vec3 mLookPos = new Vec3(0, 0, 0);

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mIb = new IndicesBuffer(new short[]{0, 1, 2, 3}, GLES20.GL_STATIC_DRAW);
        mShaderProgram = ProgramTestUtil.createCameraOneTexCoordTwoTextures();
        mVertexBuffer = BufferTestUtil.createInterleavedQuadWithTextureCoords();

        mTexture = new Texture2D("s_texture", BitmapTestUtil.createQuadColorBitmap(Bitmap.Config.ARGB_8888), MinFilter.NEAREST, MagFilter.NEAREST);

        mTextureChess = new Texture2D("s_textureChess", BitmapTestUtil.createChessColorBitmap(Bitmap.Config.ARGB_8888), MinFilter.NEAREST, MagFilter.NEAREST);

        setTestInfo("Multi texture with backend");

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
                 * Create textures.
                 */
                mTexture.create(mContext);
                mTextureChess.create(mContext);

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
                mTextureChess.load(mContext);
                return null;
            }
        });

    }

    public void testTextureMulti_MoveCloser() throws Exception {
        for (int i = 0; i < 30; i++) {
            runOnDrawFrame(new ResultRunnable() {
                @Override
                public Object run() {
                    /**
                     * Clear screen.
                     */
                    clearScreen(0.5f, 0.5f, 0.5f, 1.0f);

                    Camera camera = new Camera();
                    camera.lookAt(mPos, mLookPos);
                    camera.getFrustrum().setPerspectiveProjection(90, Frustrum.FovType.AUTO_FIT, 0.1f, 10, getSurfaceWidth(), getSurfaceHeight());

                    mPos.z += 0.05f;

                    mContext.getState().useProgram(mShaderProgram);
                    mContext.getUniformWriter().writeFloat(mShaderProgram, "u_Cam", camera.getViewProjectionMatrix().m);

                    /**
                     * This quad should be drawn centered and contain 4 primary colors with additional chess color board added.
                     */
                    DrawTestUtil.drawElementsInterleavedBufferWithDualTextures(mContext, mShaderProgram, mIb, mVertexBuffer, mTexture, mTextureChess);
                    return null;
                }
            });
        }
    }

    public void testTextureMulti_MoveAway() throws Exception {
        for (int i = 0; i < 30; i++) {
            runOnDrawFrame(new ResultRunnable() {
                @Override
                public Object run() {
                    /**
                     * Clear screen.
                     */
                    clearScreen(0.5f, 0.5f, 0.5f, 1.0f);

                    Camera camera = new Camera();
                    camera.lookAt(mPos, mLookPos);
                    camera.getFrustrum().setPerspectiveProjection(90, Frustrum.FovType.AUTO_FIT, 0.1f, 10, getSurfaceWidth(), getSurfaceHeight());

                    mPos.z -= 0.05f;

                    mContext.getState().useProgram(mShaderProgram);
                    mContext.getUniformWriter().writeFloat(mShaderProgram, "u_Cam", camera.getViewProjectionMatrix().m);

                    /**
                     * This quad should be drawn centered and contain 4 primary colors with additional chess color board added.
                     */
                    DrawTestUtil.drawElementsInterleavedBufferWithDualTextures(mContext, mShaderProgram, mIb, mVertexBuffer, mTexture, mTextureChess);
                    return null;
                }
            });
        }
    }

    public void testTextureMulti_MoveLeft() throws Exception {
        for (int i = 0; i < 30; i++) {
            runOnDrawFrame(new ResultRunnable() {
                @Override
                public Object run() {
                    /**
                     * Clear screen.
                     */
                    clearScreen(0.5f, 0.5f, 0.5f, 1.0f);

                    Camera camera = new Camera();
                    camera.lookAt(mPos, mLookPos);
                    camera.getFrustrum().setPerspectiveProjection(90, Frustrum.FovType.AUTO_FIT, 0.1f, 10, getSurfaceWidth(), getSurfaceHeight());

                    mPos.x -= 0.05f;

                    mContext.getState().useProgram(mShaderProgram);
                    mContext.getUniformWriter().writeFloat(mShaderProgram, "u_Cam", camera.getViewProjectionMatrix().m);

                    /**
                     * This quad should be drawn centered and contain 4 primary colors with additional chess color board added.
                     */
                    DrawTestUtil.drawElementsInterleavedBufferWithDualTextures(mContext, mShaderProgram, mIb, mVertexBuffer, mTexture, mTextureChess);
                    return null;
                }
            });
        }
    }

    public void testTextureMulti_MoveRight() throws Exception {
        for (int i = 0; i < 30; i++) {
            runOnDrawFrame(new ResultRunnable() {
                @Override
                public Object run() {
                    /**
                     * Clear screen.
                     */
                    clearScreen(0.5f, 0.5f, 0.5f, 1.0f);

                    Camera camera = new Camera();
                    camera.lookAt(mPos, mLookPos);
                    camera.getFrustrum().setPerspectiveProjection(90, Frustrum.FovType.AUTO_FIT, 0.1f, 10, getSurfaceWidth(), getSurfaceHeight());

                    mPos.x += 0.05f;

                    mContext.getState().useProgram(mShaderProgram);
                    mContext.getUniformWriter().writeFloat(mShaderProgram, "u_Cam", camera.getViewProjectionMatrix().m);

                    /**
                     * This quad should be drawn centered and contain 4 primary colors with additional chess color board added.
                     */
                    DrawTestUtil.drawElementsInterleavedBufferWithDualTextures(mContext, mShaderProgram, mIb, mVertexBuffer, mTexture, mTextureChess);
                    return null;
                }
            });
        }
    }

    public void testTextureMulti_MoveUp() throws Exception {
        for (int i = 0; i < 30; i++) {
            runOnDrawFrame(new ResultRunnable() {
                @Override
                public Object run() {
                    /**
                     * Clear screen.
                     */
                    clearScreen(0.5f, 0.5f, 0.5f, 1.0f);

                    Camera camera = new Camera();
                    camera.lookAt(mPos, mLookPos);
                    camera.getFrustrum().setPerspectiveProjection(90, Frustrum.FovType.AUTO_FIT, 0.1f, 10, getSurfaceWidth(), getSurfaceHeight());

                    mPos.y += 0.05f;

                    mContext.getState().useProgram(mShaderProgram);
                    mContext.getUniformWriter().writeFloat(mShaderProgram, "u_Cam", camera.getViewProjectionMatrix().m);

                    /**
                     * This quad should be drawn centered and contain 4 primary colors with additional chess color board added.
                     */
                    DrawTestUtil.drawElementsInterleavedBufferWithDualTextures(mContext, mShaderProgram, mIb, mVertexBuffer, mTexture, mTextureChess);
                    return null;
                }
            });
        }
    }

    public void testTextureMulti_MoveDown() throws Exception {
        for (int i = 0; i < 30; i++) {
            runOnDrawFrame(new ResultRunnable() {
                @Override
                public Object run() {
                    /**
                     * Clear screen.
                     */
                    clearScreen(0.5f, 0.5f, 0.5f, 1.0f);

                    Camera camera = new Camera();
                    camera.lookAt(mPos, mLookPos);
                    camera.getFrustrum().setPerspectiveProjection(90, Frustrum.FovType.AUTO_FIT, 0.1f, 10, getSurfaceWidth(), getSurfaceHeight());

                    mPos.y -= 0.05f;

                    mContext.getState().useProgram(mShaderProgram);
                    mContext.getUniformWriter().writeFloat(mShaderProgram, "u_Cam", camera.getViewProjectionMatrix().m);

                    /**
                     * This quad should be drawn centered and contain 4 primary colors with additional chess color board added.
                     */
                    DrawTestUtil.drawElementsInterleavedBufferWithDualTextures(mContext, mShaderProgram, mIb, mVertexBuffer, mTexture, mTextureChess);
                    return null;
                }
            });
        }
    }
}
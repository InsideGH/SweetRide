package com.sweetlab.sweetride.context;

import android.opengl.GLES20;

import com.sweetlab.sweetride.attributedata.IndicesBuffer;
import com.sweetlab.sweetride.attributedata.VertexBuffer;
import com.sweetlab.sweetride.context.Util.BufferTestUtil;
import com.sweetlab.sweetride.context.Util.DrawTestUtil;
import com.sweetlab.sweetride.context.Util.ProgramTestUtil;
import com.sweetlab.sweetride.shader.ShaderProgram;
import com.sweetlab.sweetride.testframework.OpenGLTestCase;
import com.sweetlab.sweetride.testframework.ResultRunnable;

/**
 * Test to draw with indices.
 */
public class ElementTargetTest extends OpenGLTestCase {
    /**
     * Backend context.
     */
    private BackendContext mContext;

    /**
     * Red, blue and color shader.
     */
    private ShaderProgram mRedShader;
    private ShaderProgram mBlueShader;

    /**
     * Various positions of a triangle.
     */
    private VertexBuffer mLeftTriangle;
    private VertexBuffer mRightTriangle;
    private VertexBuffer mTopTriangle;
    private VertexBuffer mBottomTriangle;

    /**
     * The indices buffer.
     */
    private IndicesBuffer mIndicesBuffer;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        /**
         * Create shader programs. No need for GL thread.
         */
        mRedShader = ProgramTestUtil.createNdcRed();
        mBlueShader = ProgramTestUtil.createNdcBlue();

        /**
         * Create a triangle vertices buffers. No need for GL thread.
         */
        mLeftTriangle = BufferTestUtil.createLeftTriangle();
        mRightTriangle = BufferTestUtil.createRightTriangle();
        mTopTriangle = BufferTestUtil.createTopTriangle();
        mBottomTriangle = BufferTestUtil.createBottomTriangle();

        /**
         * Create indices buffer.
         */
        mIndicesBuffer = new IndicesBuffer(BufferTestUtil.createTriangleIndices(), GLES20.GL_STATIC_DRAW);

        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                mContext = getBackendContext();

                /**
                 * Link shader programs.
                 */
                mRedShader.create(mContext);
                mBlueShader.create(mContext);

                /**
                 * Create vertex buffers (object).
                 */
                mLeftTriangle.create(mContext);
                mRightTriangle.create(mContext);
                mTopTriangle.create(mContext);
                mBottomTriangle.create(mContext);

                /**
                 * Load triangle vertices to gpu.
                 */
                mLeftTriangle.load(mContext);
                mRightTriangle.load(mContext);
                mTopTriangle.load(mContext);
                mBottomTriangle.load(mContext);

                /**
                 * Create indices buffer (object).
                 */
                mIndicesBuffer.create(mContext);

                /**
                 * Load indices to gpu.
                 */
                mIndicesBuffer.load(mContext);
                return null;
            }
        });
    }

    public void testDrawTriangle() throws Exception {
        runOnDrawFrame(new ResultRunnable() {
            @Override
            public Object run() {
                /**
                 * Clear screen.
                 */
                clearScreen(0.5f, 0.5f, 0.5f, 1.0f);

                DrawTestUtil.drawElementsSeparateBuffers(mContext, mRedShader, mIndicesBuffer, mLeftTriangle);
                DrawTestUtil.drawElementsSeparateBuffers(mContext, mBlueShader, mIndicesBuffer, mTopTriangle);
                DrawTestUtil.drawElementsSeparateBuffers(mContext, mRedShader, mIndicesBuffer, mRightTriangle);
                DrawTestUtil.drawElementsSeparateBuffers(mContext, mBlueShader, mIndicesBuffer, mBottomTriangle);

                return null;
            }
        });
        sleepOnDrawFrame(2000);
    }

}
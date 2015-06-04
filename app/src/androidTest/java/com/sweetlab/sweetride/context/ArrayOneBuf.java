package com.sweetlab.sweetride.context;

import com.sweetlab.sweetride.attributedata.VertexBuffer;
import com.sweetlab.sweetride.context.Util.BufferTestUtil;
import com.sweetlab.sweetride.context.Util.DrawTestUtil;
import com.sweetlab.sweetride.context.Util.ProgramTestUtil;
import com.sweetlab.sweetride.shader.ShaderProgram;
import com.sweetlab.sweetride.testframework.OpenGLTestCase;
import com.sweetlab.sweetride.testframework.ResultRunnable;

public class ArrayOneBuf extends OpenGLTestCase {
    /**
     * Backend context.
     */
    private BackendContext mContext;

    /**
     * Red, blue shader.
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

        setTestInfo("red, blue, red, blue backend");

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

                /**
                 * Draw the triangles.
                 */
                DrawTestUtil.drawArraySeparateBuffers(mContext, mRedShader, mLeftTriangle);
                DrawTestUtil.drawArraySeparateBuffers(mContext, mBlueShader, mTopTriangle);
                DrawTestUtil.drawArraySeparateBuffers(mContext, mRedShader, mRightTriangle);
                DrawTestUtil.drawArraySeparateBuffers(mContext, mBlueShader, mBottomTriangle);

                return null;
            }
        });
        sleepOnDrawFrame(2000);
    }
}
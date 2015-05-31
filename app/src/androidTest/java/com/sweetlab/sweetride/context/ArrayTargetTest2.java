package com.sweetlab.sweetride.context;

import com.sweetlab.sweetride.attributedata.VertexBuffer;
import com.sweetlab.sweetride.context.Util.BufferTestUtil;
import com.sweetlab.sweetride.context.Util.DrawTestUtil;
import com.sweetlab.sweetride.context.Util.ProgramTestUtil;
import com.sweetlab.sweetride.shader.ShaderProgram;
import com.sweetlab.sweetride.testframework.OpenGLTestCase;
import com.sweetlab.sweetride.testframework.ResultRunnable;

/**
 * Test array target. This test draws with multiple vertex buffers.
 */
public class ArrayTargetTest2 extends OpenGLTestCase {
    /**
     * Backend context.
     */
    private BackendContext mContext;

    /**
     * Red, blue and color shader.
     */
    private ShaderProgram mRedShader;
    private ShaderProgram mBlueShader;
    private ShaderProgram mColorShader;

    /**
     * Various positions of a triangle.
     */
    private VertexBuffer mLeftTriangle;
    private VertexBuffer mRightTriangle;
    private VertexBuffer mTopTriangle;
    private VertexBuffer mBottomTriangle;
    private VertexBuffer mColorBuffer;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        /**
         * Create shader programs. No need for GL thread.
         */
        mRedShader = ProgramTestUtil.createNdcRed();
        mBlueShader = ProgramTestUtil.createNdcBlue();
        mColorShader = ProgramTestUtil.createNdcColor();

        /**
         * Create a triangle vertices buffers. No need for GL thread.
         */
        mLeftTriangle = BufferTestUtil.createLeftTriangle();
        mRightTriangle = BufferTestUtil.createRightTriangle();
        mTopTriangle = BufferTestUtil.createTopTriangle();
        mBottomTriangle = BufferTestUtil.createBottomTriangle();

        /**
         * Create a color vertex buffer. No need for GL thread.
         */
        mColorBuffer = BufferTestUtil.createColorBuffer();

        setTestInfo("smooth, black, red, smooth");

        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                mContext = getBackendContext();

                /**
                 * Link shader programs.
                 */
                mRedShader.create(mContext);
                mBlueShader.create(mContext);
                mColorShader.create(mContext);

                /**
                 * Create vertex buffers (object).
                 */
                mLeftTriangle.create(mContext);
                mRightTriangle.create(mContext);
                mTopTriangle.create(mContext);
                mBottomTriangle.create(mContext);

                /**
                 * Create the color buffer (object).
                 */
                mColorBuffer.create(mContext);

                /**
                 * Load triangle vertices to gpu.
                 */
                mContext.getArrayTarget().load(mLeftTriangle);
                mContext.getArrayTarget().load(mRightTriangle);
                mContext.getArrayTarget().load(mTopTriangle);
                mContext.getArrayTarget().load(mBottomTriangle);

                /**
                 * Load color data to gpu.
                 */
                mContext.getArrayTarget().load(mColorBuffer);

                return null;
            }
        });
    }

    public void testDrawTriangleWithColor() throws Exception {
        runOnDrawFrame(new ResultRunnable() {
            @Override
            public Object run() {
                /**
                 * Clear screen.
                 */
                clearScreen(0.5f, 0.5f, 0.5f, 1.0f);

                /**
                 * This triangle should be smooth colored.
                 */
                DrawTestUtil.drawArraySeparateBuffers(mContext, mColorShader, mLeftTriangle, mColorBuffer);

                /**
                 * This triangle should be black since we haven't specified any color buffer and
                 * after a draw attributes are disabled.
                 */
                DrawTestUtil.drawArraySeparateBuffers(mContext, mColorShader, mTopTriangle);

                /**
                 * This shader doesn't know about colors, meaning that we wont find any
                 * attribute. It should be red.
                 */
                DrawTestUtil.drawArraySeparateBuffers(mContext, mRedShader, mRightTriangle, mColorBuffer);

                /**
                 * This triangle should be smooth colored.
                 */
                DrawTestUtil.drawArraySeparateBuffers(mContext, mColorShader, mBottomTriangle, mColorBuffer);

                return null;
            }
        });
        sleepOnDrawFrame(2000);
    }
}
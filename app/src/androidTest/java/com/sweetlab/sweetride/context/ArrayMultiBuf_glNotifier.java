package com.sweetlab.sweetride.context;

import com.sweetlab.sweetride.attributedata.VertexBuffer;
import com.sweetlab.sweetride.context.Util.ActionHelper;
import com.sweetlab.sweetride.context.Util.BufferTestUtil;
import com.sweetlab.sweetride.context.Util.DrawTestUtil;
import com.sweetlab.sweetride.context.Util.ProgramTestUtil;
import com.sweetlab.sweetride.context.Util.Verify;
import com.sweetlab.sweetride.shader.ShaderProgram;
import com.sweetlab.sweetride.testframework.OpenGLTestCase;
import com.sweetlab.sweetride.testframework.ResultRunnable;

public class ArrayMultiBuf_glNotifier extends OpenGLTestCase {
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

        setTestInfo("smooth, black, red, smooth backend");

        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                mContext = getBackendContext();

                ActionHelper.handleGLThreadActions(mRedShader, mContext);
                ActionHelper.handleGLThreadActions(mBlueShader, mContext);
                ActionHelper.handleGLThreadActions(mColorShader, mContext);
                ActionHelper.handleGLThreadActions(mLeftTriangle, mContext);
                ActionHelper.handleGLThreadActions(mRightTriangle, mContext);
                ActionHelper.handleGLThreadActions(mTopTriangle, mContext);
                ActionHelper.handleGLThreadActions(mBottomTriangle, mContext);
                ActionHelper.handleGLThreadActions(mColorBuffer, mContext);
                assertFalse(mRedShader.hasActions());
                assertFalse(mBlueShader.hasActions());
                assertFalse(mColorShader.hasActions());
                assertFalse(mLeftTriangle.hasActions());
                assertFalse(mRightTriangle.hasActions());
                assertFalse(mTopTriangle.hasActions());
                assertFalse(mBottomTriangle.hasActions());
                assertFalse(mColorBuffer.hasActions());
                return null;
            }
        });
    }

    public void testArrayMultiBuf_glNotifier() throws Exception {
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
        sleepOnDrawFrame(Verify.TERMINATE_TIME);
    }
}
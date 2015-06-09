package com.sweetlab.sweetride.array;

import com.sweetlab.sweetride.attributedata.InterleavedVertexBuffer;
import com.sweetlab.sweetride.attributedata.VertexBuffer;
import com.sweetlab.sweetride.Util.BufferTestUtil;
import com.sweetlab.sweetride.Util.DrawTestUtil;
import com.sweetlab.sweetride.Util.ProgramTestUtil;
import com.sweetlab.sweetride.Util.Verify;
import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.shader.ShaderProgram;
import com.sweetlab.sweetride.testframework.OpenGLTestCase;
import com.sweetlab.sweetride.testframework.ResultRunnable;

public class ArrayInterleavedMultiBuf_glNotifier extends OpenGLTestCase {
    /**
     * Backend context.
     */
    private BackendContext mContext;

    /**
     * Red, blue and color shader.
     */
    private ShaderProgram mRedShader;
    private ShaderProgram mColorShader;

    /**
     * Various positions of a triangle.
     */
    private VertexBuffer mTopTriangle;
    private VertexBuffer mBottomTriangle;
    private VertexBuffer mColorBuffer;
    private InterleavedVertexBuffer mInterleavedLeftTriangle;
    private InterleavedVertexBuffer mInterleavedRightTriangle;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        /**
         * Create shader programs. No need for GL thread.
         */
        mRedShader = ProgramTestUtil.createNdcRed();
        mColorShader = ProgramTestUtil.createNdcColor();

        /**
         * Create a triangle vertices buffers. No need for GL thread.
         */
        mTopTriangle = BufferTestUtil.createTopTriangle();
        mBottomTriangle = BufferTestUtil.createBottomTriangle();
        mInterleavedLeftTriangle = BufferTestUtil.createInterleavedLeftTriangleWithColor();
        mInterleavedRightTriangle = BufferTestUtil.createInterleavedRightTriangleWithColor();

        /**
         * Create a color vertex buffer. No need for GL thread.
         */
        mColorBuffer = BufferTestUtil.createColorBuffer();

        setTestInfo("smooth, red, smooth, smooth backend");

        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                mContext = getBackendContext();

                mContext.getActionHandler().handleActions(mRedShader);
                mContext.getActionHandler().handleActions(mColorShader);
                mContext.getActionHandler().handleActions(mTopTriangle);
                mContext.getActionHandler().handleActions(mBottomTriangle);
                mContext.getActionHandler().handleActions(mInterleavedLeftTriangle);
                mContext.getActionHandler().handleActions(mInterleavedRightTriangle);
                mContext.getActionHandler().handleActions(mColorBuffer);
                assertFalse(mRedShader.hasActions());
                assertFalse(mColorShader.hasActions());
                assertFalse(mTopTriangle.hasActions());
                assertFalse(mBottomTriangle.hasActions());
                assertFalse(mInterleavedLeftTriangle.hasActions());
                assertFalse(mInterleavedRightTriangle.hasActions());
                assertFalse(mColorBuffer.hasActions());
                return null;
            }
        });
    }

    /**
     * Draw using both interleaved and non-interleaved vertex buffers.
     *
     * @throws Exception
     */
    public void testArrayInterleavedMultiBuf_glNotifier() throws Exception {
        runOnDrawFrame(new ResultRunnable() {
            @Override
            public Object run() {
                /**
                 * Clear screen.
                 */
                clearScreen(0.5f, 0.5f, 0.5f, 1.0f);

                /**
                 * This triangle should be smooth colored on left side.
                 */
                DrawTestUtil.drawArrayInterleavedBuffer(mContext, mColorShader, mInterleavedLeftTriangle);

                /**
                 * This triangle should be red on top side.
                 */
                DrawTestUtil.drawArraySeparateBuffers(mContext, mRedShader, mTopTriangle);

                /**
                 * This triangle should be smooth colored on right side.
                 */
                DrawTestUtil.drawArrayInterleavedBuffer(mContext, mColorShader, mInterleavedRightTriangle);


                /**
                 * This triangle should be smooth colored on bottom side.
                 */
                DrawTestUtil.drawArraySeparateBuffers(mContext, mColorShader, mBottomTriangle, mColorBuffer);

                return null;
            }
        });
        sleepOnDrawFrame(Verify.TERMINATE_TIME);
    }
}
package com.sweetlab.sweetride.array;

import com.sweetlab.sweetride.Util.BackendRenderSettingsUtil;
import com.sweetlab.sweetride.attributedata.VertexBuffer;
import com.sweetlab.sweetride.Util.BufferTestUtil;
import com.sweetlab.sweetride.Util.DrawTestUtil;
import com.sweetlab.sweetride.Util.ProgramTestUtil;
import com.sweetlab.sweetride.Util.Verify;
import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.shader.ShaderProgram;
import com.sweetlab.sweetride.testframework.OpenGLTestCase;
import com.sweetlab.sweetride.testframework.ResultRunnable;

public class ArrayOneBuf_glNotifier extends OpenGLTestCase {
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
                 * Handle GL actions.
                 */
                mContext.getActionHandler().handleActions(mRedShader);
                mContext.getActionHandler().handleActions(mBlueShader);

                /**
                 * Handle GL actions.
                 */
                mContext.getActionHandler().handleActions(mLeftTriangle);
                mContext.getActionHandler().handleActions(mRightTriangle);
                mContext.getActionHandler().handleActions(mTopTriangle);
                mContext.getActionHandler().handleActions(mBottomTriangle);

                assertFalse(mRedShader.hasActions());
                assertFalse(mBlueShader.hasActions());
                assertFalse(mLeftTriangle.hasActions());
                assertFalse(mRightTriangle.hasActions());
                assertFalse(mTopTriangle.hasActions());
                assertFalse(mBottomTriangle.hasActions());

                return null;
            }
        });
    }

    public void testArrayOneBuf_glNotifier() throws Exception {
        runOnDrawFrame(new ResultRunnable() {
            @Override
            public Object run() {
                /**
                 * Clear screen.
                 */
                mContext.getRenderState().useSettings(BackendRenderSettingsUtil.getDefaultGrey(getSurfaceWidth(), getSurfaceHeight())).clear();

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
        sleepOnDrawFrame(Verify.TERMINATE_TIME);
    }
}
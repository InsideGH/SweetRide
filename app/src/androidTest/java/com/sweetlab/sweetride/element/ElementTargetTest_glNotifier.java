package com.sweetlab.sweetride.element;

import com.sweetlab.sweetride.Util.BackendRenderSettingsUtil;
import com.sweetlab.sweetride.Util.BufferTestUtil;
import com.sweetlab.sweetride.Util.DrawTestUtil;
import com.sweetlab.sweetride.Util.ProgramTestUtil;
import com.sweetlab.sweetride.Util.Verify;
import com.sweetlab.sweetride.attributedata.IndicesBuffer;
import com.sweetlab.sweetride.attributedata.VertexBuffer;
import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.context.BufferUsage;
import com.sweetlab.sweetride.shader.ShaderProgram;
import com.sweetlab.sweetride.testframework.OpenGLTestCase;
import com.sweetlab.sweetride.testframework.ResultRunnable;

public class ElementTargetTest_glNotifier extends OpenGLTestCase {
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
        mIndicesBuffer = new IndicesBuffer(BufferTestUtil.createTriangleIndices(), BufferUsage.STATIC);

        setTestInfo("indices red, blue, red, blue backend");

        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                mContext = getBackendContext();

                mContext.getActionHandler().handleActions(mRedShader);
                mContext.getActionHandler().handleActions(mBlueShader);
                mContext.getActionHandler().handleActions(mLeftTriangle);
                mContext.getActionHandler().handleActions(mRightTriangle);
                mContext.getActionHandler().handleActions(mTopTriangle);
                mContext.getActionHandler().handleActions(mBottomTriangle);
                mContext.getActionHandler().handleActions(mIndicesBuffer);

                assertFalse(mRedShader.hasActions());
                assertFalse(mBlueShader.hasActions());
                assertFalse(mLeftTriangle.hasActions());
                assertFalse(mRightTriangle.hasActions());
                assertFalse(mTopTriangle.hasActions());
                assertFalse(mBottomTriangle.hasActions());
                assertFalse(mIndicesBuffer.hasActions());
                return null;
            }
        });
    }

    public void testElementTargetTest_glNotifier() throws Exception {
        runOnDrawFrame(new ResultRunnable() {
            @Override
            public Object run() {
                /**
                 * Clear screen.
                 */
                mContext.getRenderState().useSettings(BackendRenderSettingsUtil.getDefaultGrey(getSurfaceWidth(), getSurfaceHeight())).clear();

                DrawTestUtil.drawElementsSeparateBuffers(mContext, mRedShader, mIndicesBuffer, mLeftTriangle);
                DrawTestUtil.drawElementsSeparateBuffers(mContext, mBlueShader, mIndicesBuffer, mTopTriangle);
                DrawTestUtil.drawElementsSeparateBuffers(mContext, mRedShader, mIndicesBuffer, mRightTriangle);
                DrawTestUtil.drawElementsSeparateBuffers(mContext, mBlueShader, mIndicesBuffer, mBottomTriangle);

                return null;
            }
        });
        sleepOnDrawFrame(Verify.TERMINATE_TIME);
    }

}
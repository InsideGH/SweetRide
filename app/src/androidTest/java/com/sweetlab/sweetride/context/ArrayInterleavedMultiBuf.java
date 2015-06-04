package com.sweetlab.sweetride.context;

import com.sweetlab.sweetride.attributedata.InterleavedVertexBuffer;
import com.sweetlab.sweetride.attributedata.VertexBuffer;
import com.sweetlab.sweetride.context.Util.BufferTestUtil;
import com.sweetlab.sweetride.context.Util.DrawTestUtil;
import com.sweetlab.sweetride.context.Util.ProgramTestUtil;
import com.sweetlab.sweetride.shader.ShaderProgram;
import com.sweetlab.sweetride.testframework.OpenGLTestCase;
import com.sweetlab.sweetride.testframework.ResultRunnable;

public class ArrayInterleavedMultiBuf extends OpenGLTestCase {
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

                /**
                 * Link shader programs.
                 */
                mRedShader.create(mContext);
                mColorShader.create(mContext);

                /**
                 * Create vertex buffers (object).
                 */
                mTopTriangle.create(mContext);
                mBottomTriangle.create(mContext);
                mInterleavedLeftTriangle.create(mContext);
                mInterleavedRightTriangle.create(mContext);

                /**
                 * Create the color buffer (object).
                 */
                mColorBuffer.create(mContext);

                /**
                 * Load triangle vertices to gpu.
                 */
                mTopTriangle.load(mContext);
                mBottomTriangle.load(mContext);
                mInterleavedLeftTriangle.load(mContext);
                mInterleavedRightTriangle.load(mContext);

                /**
                 * Load color data to gpu.
                 */
                mColorBuffer.load(mContext);
                return null;
            }
        });
    }

    /**
     * Draw using both interleaved and non-interleaved vertex buffers.
     *
     * @throws Exception
     */
    public void testArrayInterleavedMultiBuf() throws Exception {
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
        sleepOnDrawFrame(2000);
    }
}
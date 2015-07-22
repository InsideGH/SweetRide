package com.sweetlab.sweetride.context;

import android.opengl.GLES20;

import com.sweetlab.sweetride.DebugOptions;
import com.sweetlab.sweetride.Util.Verify;
import com.sweetlab.sweetride.testframework.OpenGLTestCase;
import com.sweetlab.sweetride.testframework.ResultRunnable;

/**
 * Test render state.
 */
public class RenderStateTest extends OpenGLTestCase {

    private BackendContext mContext;
    private RenderState mRenderState;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        assertTrue(DebugOptions.DEBUG_RENDER_SETTINGS);

        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                mContext = getBackendContext();
                mRenderState = mContext.getRenderState();
                return null;
            }
        });
    }

    public void test() {
        runOnDrawFrame(new ResultRunnable() {
            @Override
            public Object run() {
                mRenderState.setBlendEqFunc(GLES20.GL_FUNC_REVERSE_SUBTRACT);
                mRenderState.setBlendEqFunc(GLES20.GL_FUNC_ADD);
                mRenderState.setBlendFact(GLES20.GL_CONSTANT_ALPHA, GLES20.GL_CONSTANT_COLOR);
                mRenderState.setBlendFact(GLES20.GL_CONSTANT_COLOR, GLES20.GL_CONSTANT_ALPHA);
                mRenderState.setBlend(true);
                mRenderState.setBlend(false);
                mRenderState.setCullFace(true);
                mRenderState.setCullFace(false);
                mRenderState.setDepthTest(true);
                mRenderState.setDepthTest(false);
                mRenderState.setDither(false);
                mRenderState.setDither(true);
                mRenderState.setPolygonOffsetFill(true);
                mRenderState.setPolygonOffsetFill(false);
                mRenderState.setSampleAlphaToCoverage(true);
                mRenderState.setSampleAlphaToCoverage(false);
                mRenderState.setSampleCoverage(true);
                mRenderState.setSampleCoverage(false);
                mRenderState.setScissorTest(true);
                mRenderState.setScissorTest(false);
                mRenderState.setStencilTest(true);
                mRenderState.setStencilTest(false);
                mRenderState.setClearStencil(16);
                mRenderState.setClearStencil(17);
                mRenderState.setClearDepth(0.6f);
                mRenderState.setClearDepth(0.7f);
                mRenderState.setClearColor(new float[]{0, 1, 0, 1});
                mRenderState.setClearColor(new float[]{0, 0, 1, 1});
                mRenderState.setViewPort(10, 10, 460, 730);
                mRenderState.setViewPort(40, 30, 410, 732);
                mRenderState.setClear(0, GLES20.GL_COLOR_BUFFER_BIT);
                mRenderState.setClear(0, GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
                mRenderState.clear();
                mRenderState.clear();
                return null;
            }
        });
        sleepOnDrawFrame(Verify.TERMINATE_TIME);
    }
}
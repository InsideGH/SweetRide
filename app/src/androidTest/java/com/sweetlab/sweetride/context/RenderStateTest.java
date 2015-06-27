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
                mRenderState.setBlendFact(GLES20.GL_CONSTANT_ALPHA, GLES20.GL_CONSTANT_COLOR);
                mRenderState.setBlend(true);
                mRenderState.setCullFace(true);
                mRenderState.setDepthTest(true);
                mRenderState.setDither(false);
                mRenderState.setPolygonOffsetFill(true);
                mRenderState.setSampleAlphaToCoverage(true);
                mRenderState.setSampleCoverage(true);
                mRenderState.setScissorTest(true);
                mRenderState.setStencilTest(true);
                mRenderState.setClearStencil(16);
                mRenderState.setClearDepth(0.6f);
                mRenderState.setClearColor(new float[]{0, 1, 0, 1});
                mRenderState.setViewPort(10,10,460,730);
                mRenderState.setClear(0, GLES20.GL_COLOR_BUFFER_BIT);
                mRenderState.clear();
                return null;
            }
        });
        sleepOnDrawFrame(Verify.TERMINATE_TIME);
    }
}
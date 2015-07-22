package com.sweetlab.sweetride.context;

import android.opengl.GLES20;

import com.sweetlab.sweetride.DebugOptions;

import java.util.Arrays;

/**
 * GL render state. Each (new) frame the clear order must be cleared in order
 * to clear to work.
 */
public class RenderState extends BackendRenderSettings {
    /**
     * Buffer used during construction and debug.
     */
    private int[] mIntBuf = new int[4];
    /**
     * Buffer used during debug.
     */
    private boolean[] mBoolBuf = new boolean[1];
    /**
     * Buffer used during debug.
     */
    private float[] mFloatBuf = new float[4];

    /**
     * Constructor.
     */
    public RenderState() {
        /**
         * When a GL context is first attached to a window, width and height are set to the dimensions of that window.
         */
        GLES20.glGetIntegerv(GLES20.GL_VIEWPORT, mIntBuf, 0);
        mViewPort[0] = mIntBuf[0];
        mViewPort[1] = mIntBuf[1];
        mViewPort[2] = mIntBuf[2];
        mViewPort[3] = mIntBuf[3];
    }

    @Override
    public void setBlendEqFunc(int func) {
        if (DebugOptions.DEBUG_RENDER_SETTINGS) {
            GLES20.glGetIntegerv(GLES20.GL_BLEND_EQUATION_RGB, mIntBuf, 0);
            if (mIntBuf[0] != mBlendEqFunc) {
                throw new RuntimeException("RenderState diff rgb blend eq func " + " state = " + mBlendEqFunc + " gl = " + mIntBuf[0]);
            }
            GLES20.glGetIntegerv(GLES20.GL_BLEND_EQUATION_ALPHA, mIntBuf, 0);
            if (mIntBuf[0] != mBlendEqFunc) {
                throw new RuntimeException("RenderState diff alpha blend eq func " + " state = " + mBlendEqFunc + " gl = " + mIntBuf[0]);
            }
        }
        if (mBlendEqFunc != func) {
            mBlendEqFunc = func;
            GLES20.glBlendEquation(mBlendEqFunc);
        }
    }

    @Override
    public void setBlendFact(int sFactor, int dFactor) {
        if (DebugOptions.DEBUG_RENDER_SETTINGS) {
            GLES20.glGetIntegerv(GLES20.GL_BLEND_SRC_RGB, mIntBuf, 0);
            if (mIntBuf[0] != mBlendSrcFact) {
                throw new RuntimeException("RenderState diff rgb blend src fact " + " state = " + mBlendSrcFact + " gl = " + mIntBuf[0]);
            }
            GLES20.glGetIntegerv(GLES20.GL_BLEND_SRC_ALPHA, mIntBuf, 0);
            if (mIntBuf[0] != mBlendSrcFact) {
                throw new RuntimeException("RenderState diff alpha blend src fact " + " state = " + mBlendSrcFact + " gl = " + mIntBuf[0]);
            }
            GLES20.glGetIntegerv(GLES20.GL_BLEND_DST_RGB, mIntBuf, 0);
            if (mIntBuf[0] != mBlendDstFact) {
                throw new RuntimeException("RenderState diff rgb blend dst fact " + " state = " + mBlendDstFact + " gl = " + mIntBuf[0]);
            }
            GLES20.glGetIntegerv(GLES20.GL_BLEND_DST_ALPHA, mIntBuf, 0);
            if (mIntBuf[0] != mBlendDstFact) {
                throw new RuntimeException("RenderState diff alpha blend dst fact " + " state = " + mBlendDstFact + " gl = " + mIntBuf[0]);
            }
        }
        if (mBlendSrcFact != sFactor || mBlendDstFact != dFactor) {
            mBlendSrcFact = sFactor;
            mBlendDstFact = dFactor;
            GLES20.glBlendFunc(mBlendSrcFact, mBlendDstFact);
        }
    }

    @Override
    public void setBlend(boolean blend) {
        if (DebugOptions.DEBUG_RENDER_SETTINGS) {
            GLES20.glGetBooleanv(GLES20.GL_BLEND, mBoolBuf, 0);
            if (mBoolBuf[0] != mBlend) {
                throw new RuntimeException("RenderState diff blend " + " state = " + mBlend + " gl = " + mBoolBuf[0]);
            }
        }
        if (mBlend != blend) {
            mBlend = blend;
            if (blend) {
                GLES20.glEnable(GLES20.GL_BLEND);
            } else {
                GLES20.glDisable(GLES20.GL_BLEND);
            }
        }
    }

    @Override
    public void setCullFace(boolean cullFace) {
        if (DebugOptions.DEBUG_RENDER_SETTINGS) {
            GLES20.glGetBooleanv(GLES20.GL_CULL_FACE, mBoolBuf, 0);
            if (mBoolBuf[0] != mCullFace) {
                throw new RuntimeException("RenderState diff cull face" + " state = " + mCullFace + " gl = " + mBoolBuf[0]);
            }
        }
        if (mCullFace != cullFace) {
            mCullFace = cullFace;
            if (cullFace) {
                GLES20.glEnable(GLES20.GL_CULL_FACE);
            } else {
                GLES20.glDisable(GLES20.GL_CULL_FACE);
            }
        }
    }

    @Override
    public void setDepthTest(boolean depthTest) {
        if (DebugOptions.DEBUG_RENDER_SETTINGS) {
            GLES20.glGetBooleanv(GLES20.GL_DEPTH_TEST, mBoolBuf, 0);
            if (mBoolBuf[0] != mDepthTest) {
                throw new RuntimeException("RenderState diff depth test" + " state = " + mDepthTest + " gl = " + mBoolBuf[0]);
            }
        }
        if (mDepthTest != depthTest) {
            mDepthTest = depthTest;
            if (depthTest) {
                GLES20.glEnable(GLES20.GL_DEPTH_TEST);
            } else {
                GLES20.glDisable(GLES20.GL_DEPTH_TEST);
            }
        }
    }

    @Override
    public void setDither(boolean dither) {
        if (DebugOptions.DEBUG_RENDER_SETTINGS) {
            GLES20.glGetBooleanv(GLES20.GL_DITHER, mBoolBuf, 0);
            if (mBoolBuf[0] != mDither) {
                throw new RuntimeException("RenderState diff dither" + " state = " + mDither + " gl = " + mBoolBuf[0]);
            }
        }
        if (mDither != dither) {
            mDither = dither;
            if (dither) {
                GLES20.glEnable(GLES20.GL_DITHER);
            } else {
                GLES20.glDisable(GLES20.GL_DITHER);
            }
        }
    }

    @Override
    public void setPolygonOffsetFill(boolean polygonOffsetFill) {
        if (DebugOptions.DEBUG_RENDER_SETTINGS) {
            GLES20.glGetBooleanv(GLES20.GL_POLYGON_OFFSET_FILL, mBoolBuf, 0);
            if (mBoolBuf[0] != mPolygonOffsetFill) {
                throw new RuntimeException("RenderState diff polygon offset fill" + " state = " + mPolygonOffsetFill + " gl = " + mBoolBuf[0]);
            }
        }
        if (mPolygonOffsetFill != polygonOffsetFill) {
            mPolygonOffsetFill = polygonOffsetFill;
            if (polygonOffsetFill) {
                GLES20.glEnable(GLES20.GL_POLYGON_OFFSET_FILL);
            } else {
                GLES20.glDisable(GLES20.GL_POLYGON_OFFSET_FILL);
            }
        }
    }

    @Override
    public void setSampleAlphaToCoverage(boolean sampleAlphaToCoverage) {
        if (DebugOptions.DEBUG_RENDER_SETTINGS) {
            GLES20.glGetBooleanv(GLES20.GL_SAMPLE_ALPHA_TO_COVERAGE, mBoolBuf, 0);
            if (mBoolBuf[0] != mSampleAlphaToCoverage) {
                throw new RuntimeException("RenderState diff sample alpha to coverage" + " state = " + mSampleAlphaToCoverage + " gl = " + mBoolBuf[0]);
            }
        }
        if (mSampleAlphaToCoverage != sampleAlphaToCoverage) {
            mSampleAlphaToCoverage = sampleAlphaToCoverage;
            if (sampleAlphaToCoverage) {
                GLES20.glEnable(GLES20.GL_SAMPLE_ALPHA_TO_COVERAGE);
            } else {
                GLES20.glDisable(GLES20.GL_SAMPLE_ALPHA_TO_COVERAGE);
            }
        }
    }

    @Override
    public void setSampleCoverage(boolean sampleCoverage) {
        if (DebugOptions.DEBUG_RENDER_SETTINGS) {
            GLES20.glGetBooleanv(GLES20.GL_SAMPLE_COVERAGE, mBoolBuf, 0);
            if (mBoolBuf[0] != mSampleCoverage) {
                throw new RuntimeException("RenderState diff sample coverage" + " state = " + mSampleCoverage + " gl = " + mBoolBuf[0]);
            }
        }
        if (mSampleCoverage != sampleCoverage) {
            mSampleCoverage = sampleCoverage;
            if (sampleCoverage) {
                GLES20.glEnable(GLES20.GL_SAMPLE_COVERAGE);
            } else {
                GLES20.glDisable(GLES20.GL_SAMPLE_COVERAGE);
            }
        }
    }

    @Override
    public void setScissorTest(boolean scissorTest) {
        if (DebugOptions.DEBUG_RENDER_SETTINGS) {
            GLES20.glGetBooleanv(GLES20.GL_SCISSOR_TEST, mBoolBuf, 0);
            if (mBoolBuf[0] != mScissorTest) {
                throw new RuntimeException("RenderState diff scissor test" + " state = " + mScissorTest + " gl = " + mBoolBuf[0]);
            }
        }
        if (mScissorTest != scissorTest) {
            mScissorTest = scissorTest;
            if (scissorTest) {
                GLES20.glEnable(GLES20.GL_SCISSOR_TEST);
            } else {
                GLES20.glDisable(GLES20.GL_SCISSOR_TEST);
            }
        }
    }

    @Override
    public void setStencilTest(boolean stencilTest) {
        if (DebugOptions.DEBUG_RENDER_SETTINGS) {
            GLES20.glGetBooleanv(GLES20.GL_STENCIL_TEST, mBoolBuf, 0);
            if (mBoolBuf[0] != mStencilTest) {
                throw new RuntimeException("RenderState diff stencil test" + " state = " + mStencilTest + " gl = " + mBoolBuf[0]);
            }
        }
        if (mStencilTest != stencilTest) {
            mStencilTest = stencilTest;
            if (stencilTest) {
                GLES20.glEnable(GLES20.GL_STENCIL_TEST);
            } else {
                GLES20.glDisable(GLES20.GL_STENCIL_TEST);
            }
        }
    }

    @Override
    public void setClearStencil(int stencil) {
        if (DebugOptions.DEBUG_RENDER_SETTINGS) {
            GLES20.glGetIntegerv(GLES20.GL_STENCIL_CLEAR_VALUE, mIntBuf, 0);
            if (mIntBuf[0] != mClearStencil) {
                throw new RuntimeException("RenderState diff stencil clear" + " state = " + mClearStencil + " gl = " + mIntBuf[0]);
            }
        }
        if (mClearStencil != stencil) {
            mClearStencil = stencil;
            GLES20.glClearStencil(stencil);
        }
    }

    @Override
    public void setClearDepth(float depth) {
        if (DebugOptions.DEBUG_RENDER_SETTINGS) {
            GLES20.glGetFloatv(GLES20.GL_DEPTH_CLEAR_VALUE, mFloatBuf, 0);
            if (Float.compare(mFloatBuf[0], mClearDepth) != 0) {
                throw new RuntimeException("RenderState diff clear depth" + " state = " + mClearDepth + " gl = " + mFloatBuf[0]);
            }
        }
        if (Float.compare(mClearDepth, depth) != 0) {
            mClearDepth = depth;
            GLES20.glClearDepthf(depth);
        }
    }

    @Override
    public void setClearColor(float[] color) {
        if (DebugOptions.DEBUG_RENDER_SETTINGS) {
            GLES20.glGetFloatv(GLES20.GL_COLOR_CLEAR_VALUE, mFloatBuf, 0);
            if (!Arrays.equals(mFloatBuf, mClearColor)) {
                throw new RuntimeException("RenderState diff clear color" + " state = " +
                        mClearColor[0] + " " + mClearColor[1] + " " + mClearColor[2] + mClearColor[3] +
                        " gl = " + mFloatBuf[0] + " " + mFloatBuf[1] + " " + mFloatBuf[2] + " " + mFloatBuf[3]);
            }
        }
        if (!Arrays.equals(mClearColor, color)) {
            mClearColor[0] = color[0];
            mClearColor[1] = color[1];
            mClearColor[2] = color[2];
            mClearColor[3] = color[3];
            GLES20.glClearColor(color[0], color[1], color[2], color[3]);
        }
    }

    @Override
    public void setViewPort(int x, int y, int width, int height) {
        if (DebugOptions.DEBUG_RENDER_SETTINGS) {
            GLES20.glGetIntegerv(GLES20.GL_VIEWPORT, mIntBuf, 0);
            if (!Arrays.equals(mIntBuf, mViewPort)) {
                throw new RuntimeException("RenderState diff viewport" + " state = " +
                        mViewPort[0] + " " + mViewPort[1] + " " + mViewPort[2] + " " + mViewPort[3] +
                        " gl = " + mIntBuf[0] + " " + mIntBuf[1] + " " + mIntBuf[2] + " " + mIntBuf[3]);
            }
        }
        if (mViewPort[0] != x || mViewPort[1] != y || mViewPort[2] != width || mViewPort[3] != height) {
            mViewPort[0] = x;
            mViewPort[1] = y;
            mViewPort[2] = width;
            mViewPort[3] = height;
            GLES20.glViewport(x, y, width, height);
        }
    }

    /**
     * Reset the clear order in order to clear screen at correct draw.
     */
    public void resetClearOrder() {
        mClearOrder = -1;
    }

    /**
     * Clear using mask previously set.
     */
    public void clear() {
        GLES20.glClear(mClearMask);
    }

    /**
     * Use the provided settings.
     *
     * @param s Settings to use.
     * @return This.
     */
    public RenderState useSettings(BackendRenderSettings s) {
        if (!this.equals(s)) {
            setBlend(s.mBlend);
            setCullFace(s.mCullFace);
            setDepthTest(s.mDepthTest);
            setDither(s.mDither);
            setPolygonOffsetFill(s.mPolygonOffsetFill);
            setSampleAlphaToCoverage(s.mSampleAlphaToCoverage);
            setSampleCoverage(s.mSampleCoverage);
            setScissorTest(s.mScissorTest);
            setStencilTest(s.mStencilTest);
            setBlendEqFunc(s.mBlendEqFunc);
            setBlendFact(s.mBlendSrcFact, s.mBlendDstFact);
            setClear(s.getClearOrder(), s.getClearMask());
            setClearStencil(s.mClearStencil);
            setClearDepth(s.mClearDepth);
            setClearColor(s.getClearColor());
            setViewPort(s.getViewPortX(), s.getViewPortY(), s.getViewPortWidth(), s.getViewPortHeight());

            if (DebugOptions.DEBUG_RENDER_SETTINGS && !this.equals(s)) {
                throw new RuntimeException("Mismatch between settings state after usage");
            }
        }
        return this;
    }
}

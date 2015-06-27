package com.sweetlab.sweetride.context;

import android.opengl.GLES20;

import java.util.Arrays;

/**
 * Data object with render settings.
 */
public class BackendRenderSettings {
    protected final float[] mClearColor = new float[4];
    protected final int[] mViewPort = new int[4];
    protected int mBlendEqFunc;
    protected int mBlendSrcFact;
    protected int mBlendDstFact;
    protected int mClearMask;
    protected float mClearDepth;
    protected int mClearStencil;
    protected boolean mBlend;
    protected boolean mCullFace;
    protected boolean mDepthTest;
    protected boolean mDither;
    protected boolean mPolygonOffsetFill;
    protected boolean mSampleAlphaToCoverage;
    protected boolean mSampleCoverage;
    protected boolean mScissorTest;
    protected boolean mStencilTest;
    protected int mClearOrder;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        // modified equals removing class compare
        if (o == null /* || getClass() != o.getClass()*/) return false;

        BackendRenderSettings that = (BackendRenderSettings) o;

        if (mBlendEqFunc != that.mBlendEqFunc) return false;
        if (mBlendSrcFact != that.mBlendSrcFact) return false;
        if (mBlendDstFact != that.mBlendDstFact) return false;
        if (mClearMask != that.mClearMask) return false;
        if (Float.compare(that.mClearDepth, mClearDepth) != 0) return false;
        if (mClearStencil != that.mClearStencil) return false;
        if (mBlend != that.mBlend) return false;
        if (mCullFace != that.mCullFace) return false;
        if (mDepthTest != that.mDepthTest) return false;
        if (mDither != that.mDither) return false;
        if (mPolygonOffsetFill != that.mPolygonOffsetFill) return false;
        if (mSampleAlphaToCoverage != that.mSampleAlphaToCoverage) return false;
        if (mSampleCoverage != that.mSampleCoverage) return false;
        if (mScissorTest != that.mScissorTest) return false;
        if (mStencilTest != that.mStencilTest) return false;
        if (mClearOrder != that.mClearOrder) return false;
        if (!Arrays.equals(mClearColor, that.mClearColor)) return false;
        return Arrays.equals(mViewPort, that.mViewPort);

    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(mClearColor);
        result = 31 * result + Arrays.hashCode(mViewPort);
        result = 31 * result + mBlendEqFunc;
        result = 31 * result + mBlendSrcFact;
        result = 31 * result + mBlendDstFact;
        result = 31 * result + mClearMask;
        result = 31 * result + (mClearDepth != +0.0f ? Float.floatToIntBits(mClearDepth) : 0);
        result = 31 * result + mClearStencil;
        result = 31 * result + (mBlend ? 1 : 0);
        result = 31 * result + (mCullFace ? 1 : 0);
        result = 31 * result + (mDepthTest ? 1 : 0);
        result = 31 * result + (mDither ? 1 : 0);
        result = 31 * result + (mPolygonOffsetFill ? 1 : 0);
        result = 31 * result + (mSampleAlphaToCoverage ? 1 : 0);
        result = 31 * result + (mSampleCoverage ? 1 : 0);
        result = 31 * result + (mScissorTest ? 1 : 0);
        result = 31 * result + (mStencilTest ? 1 : 0);
        result = 31 * result + mClearOrder;
        return result;
    }

    /**
     * Constructor. Initializes settings with default GL values.
     */
    public BackendRenderSettings() {
        mBlendEqFunc = GLES20.GL_FUNC_ADD;
        mBlendSrcFact = GLES20.GL_ONE;
        mBlendDstFact = GLES20.GL_ZERO;
        mDither = true;
        mClearDepth = 1f;
        mClearOrder = -1;
    }

    /**
     * Set blend equation function.
     *
     * @param value Value to set.
     */
    public void setBlendEqFunc(int value) {
        mBlendEqFunc = value;
    }

    /**
     * Get the blend equation function.
     *
     * @return Value to get.
     */
    public int getBlendEqFunc() {
        return mBlendEqFunc;
    }

    /**
     * Set blend source and destination function.
     *
     * @param sFactor Source factor.
     * @param dFactor Destination factor.
     */
    public void setBlendFact(int sFactor, int dFactor) {
        mBlendSrcFact = sFactor;
        mBlendDstFact = dFactor;
    }

    /**
     * Get the blend source factor.
     *
     * @return Value to get.
     */
    public int getBlendSrcFact() {
        return mBlendSrcFact;
    }

    /**
     * Get the blend destination factor.
     *
     * @return Value to get.
     */
    public int getBlendDstFact() {
        return mBlendDstFact;
    }

    /**
     * Set the clear order and mask.
     *
     * @param clearOrder The clear order.
     * @param mask       The mask.
     */
    public void setClear(int clearOrder, int mask) {
        mClearOrder = clearOrder;
        mClearMask = mask;
    }

    /**
     * Get the clear mask.
     *
     * @return The clear mask.
     */
    public int getClearMask() {
        return mClearMask;
    }

    /**
     * Get the clear order.
     *
     * @return The order.
     */
    public int getClearOrder() {
        return mClearOrder;
    }

    /**
     * Set clear color.
     *
     * @param color The clear color.
     */
    public void setClearColor(float[] color) {
        mClearColor[0] = color[0];
        mClearColor[1] = color[1];
        mClearColor[2] = color[2];
        mClearColor[3] = color[3];
    }

    /**
     * Get clear color.
     *
     * @return The clear color.
     */
    public float[] getClearColor() {
        return mClearColor;
    }

    /**
     * Set clear depth.
     *
     * @param depth The clear depth.
     */
    public void setClearDepth(float depth) {
        mClearDepth = depth;
    }

    /**
     * Get the clear depth.
     *
     * @return The clear depth.
     */
    public float getClearDepth() {
        return mClearDepth;
    }

    /**
     * Set the clear stencil.
     *
     * @param stencil The clear stencil.
     */
    public void setClearStencil(int stencil) {
        mClearStencil = stencil;
    }

    /**
     * Get the clear stencil.
     *
     * @return The clear stencil.
     */
    public int getClearStencil() {
        return mClearStencil;
    }

    /**
     * Enable or disable blend.
     *
     * @param enable True or false.
     */
    public void setBlend(boolean enable) {
        mBlend = enable;
    }

    /**
     * Get if blend is enabled or not.
     *
     * @return True if enabled.
     */
    public boolean getBlend() {
        return mBlend;
    }

    /**
     * Enable or disable cull face.
     *
     * @param cullFace True or false.
     */
    public void setCullFace(boolean cullFace) {
        mCullFace = cullFace;
    }

    /**
     * Get if cull face is enabled or not.
     *
     * @return True if enabled.
     */
    public boolean getCullFace() {
        return mCullFace;
    }

    /**
     * Enable or disable depth test.
     *
     * @param depthTest True or false.
     */
    public void setDepthTest(boolean depthTest) {
        mDepthTest = depthTest;
    }

    /**
     * Get if depth test is enabled or not.
     *
     * @return True if enabled.
     */
    public boolean getDepthTest() {
        return mDepthTest;
    }

    /**
     * Enable or disable dither.
     *
     * @param dither True or false.
     */
    public void setDither(boolean dither) {
        mDither = dither;
    }

    /**
     * Get if dither is enabled or not.
     *
     * @return True if enabled.
     */
    public boolean getDither() {
        return mDither;
    }

    /**
     * Enable or disable polygon offset fill.
     *
     * @param polygonOffsetFill True or false.
     */
    public void setPolygonOffsetFill(boolean polygonOffsetFill) {
        mPolygonOffsetFill = polygonOffsetFill;
    }

    /**
     * Get if polygon offset fill is enabled or not.
     *
     * @return True if enabled.
     */
    public boolean getPolygonOffsetFill() {
        return mPolygonOffsetFill;
    }

    /**
     * Enable or disable sample alpha to coverage.
     *
     * @param sampleAlphaToCoverage True or false.
     */
    public void setSampleAlphaToCoverage(boolean sampleAlphaToCoverage) {
        mSampleAlphaToCoverage = sampleAlphaToCoverage;
    }

    /**
     * Get if sample alpha coverage is enabled or not.
     *
     * @return True if enabled.
     */
    public boolean getSampleAlphaToCoverage() {
        return mSampleAlphaToCoverage;
    }

    /**
     * Enable or disable sample coverage.
     *
     * @param sampleCoverage True or false.
     */
    public void setSampleCoverage(boolean sampleCoverage) {
        mSampleCoverage = sampleCoverage;
    }

    /**
     * Get if sample coverage is enabled or not.
     *
     * @return True if enabled.
     */
    public boolean getSampleCoverage() {
        return mSampleCoverage;
    }

    /**
     * Enable or disable scissor test.
     *
     * @param scissorTest True or false.
     */
    public void setScissorTest(boolean scissorTest) {
        mScissorTest = scissorTest;
    }

    /**
     * Get if scissor test is enabled or not.
     *
     * @return True if enabled.
     */
    public boolean getScissorTest() {
        return mScissorTest;
    }

    /**
     * Enable or disable stencil test.
     *
     * @param stencilTest True or false.
     */
    public void setStencilTest(boolean stencilTest) {
        mStencilTest = stencilTest;
    }

    /**
     * Get if stencil test is enabled or not.
     *
     * @return True if enabled.
     */
    public boolean getStencilTest() {
        return mStencilTest;
    }

    /**
     * Set view port.
     *
     * @param x      Lower left x value in px.
     * @param y      Lower left y value in px.
     * @param width  Width in px.
     * @param height Height in px.
     */
    public void setViewPort(int x, int y, int width, int height) {
        mViewPort[0] = x;
        mViewPort[1] = y;
        mViewPort[2] = width;
        mViewPort[3] = height;
    }

    /**
     * Get viewport lower left x value.
     *
     * @return The x value.
     */
    public int getViewPortX() {
        return mViewPort[0];
    }

    /**
     * Get viewport lower left y value.
     *
     * @return The y value.
     */
    public int getViewPortY() {
        return mViewPort[1];
    }

    /**
     * Get viewport width value.
     *
     * @return The width value.
     */
    public int getViewPortWidth() {
        return mViewPort[2];
    }

    /**
     * Get viewport height value.
     *
     * @return The height value.
     */
    public int getViewPortHeight() {
        return mViewPort[3];
    }
}

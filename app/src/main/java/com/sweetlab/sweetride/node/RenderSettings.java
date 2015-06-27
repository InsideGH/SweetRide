package com.sweetlab.sweetride.node;

import com.sweetlab.sweetride.DebugOptions;
import com.sweetlab.sweetride.action.Action;
import com.sweetlab.sweetride.action.ActionNotifier;
import com.sweetlab.sweetride.action.ActionThread;
import com.sweetlab.sweetride.action.GlobalActionId;
import com.sweetlab.sweetride.action.NoHandleNotifier;
import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.context.BackendRenderSettings;
import com.sweetlab.sweetride.engine.FrontEndActionHandler;
import com.sweetlab.sweetride.node.rendersettings.BlendDstFact;
import com.sweetlab.sweetride.node.rendersettings.BlendEquationFunc;
import com.sweetlab.sweetride.node.rendersettings.BlendEquationSetting;
import com.sweetlab.sweetride.node.rendersettings.BlendFactorsSetting;
import com.sweetlab.sweetride.node.rendersettings.BlendSetting;
import com.sweetlab.sweetride.node.rendersettings.BlendSrcFact;
import com.sweetlab.sweetride.node.rendersettings.ClearBit;
import com.sweetlab.sweetride.node.rendersettings.ClearColorSetting;
import com.sweetlab.sweetride.node.rendersettings.ClearDepthSetting;
import com.sweetlab.sweetride.node.rendersettings.ClearMask;
import com.sweetlab.sweetride.node.rendersettings.ClearSetting;
import com.sweetlab.sweetride.node.rendersettings.ClearStencilSetting;
import com.sweetlab.sweetride.node.rendersettings.CullFaceSetting;
import com.sweetlab.sweetride.node.rendersettings.DepthTestSetting;
import com.sweetlab.sweetride.node.rendersettings.DitherSetting;
import com.sweetlab.sweetride.node.rendersettings.History;
import com.sweetlab.sweetride.node.rendersettings.PolygonOffsetFillSetting;
import com.sweetlab.sweetride.node.rendersettings.RsActionId;
import com.sweetlab.sweetride.node.rendersettings.SampleAlphaToCoverageSetting;
import com.sweetlab.sweetride.node.rendersettings.SampleCoverageSetting;
import com.sweetlab.sweetride.node.rendersettings.ScissorTestSetting;
import com.sweetlab.sweetride.node.rendersettings.StencilTestSetting;
import com.sweetlab.sweetride.node.rendersettings.ViewPortSetting;

/**
 * Render setting.
 */
public class RenderSettings extends NoHandleNotifier<GlobalActionId> {
    /**
     * Internal action notifier.
     */
    private final ActionNotifier<RsActionId> mInternal = new ActionNotifier<RsActionId>() {
        @Override
        public boolean handleAction(Action<RsActionId> action) {
            return true;
        }

        @Override
        public boolean handleAction(BackendContext context, Action<RsActionId> action) {
            /**
             * If this is called exception will be thrown since we return false.
             */
            return false;
        }

        @Override
        protected void onActionAdded(Action<RsActionId> action) {
            RenderSettings.this.addAction(mDirty);
        }
    };

    /**
     * The backend render settings.
     */
    private final BackendRenderSettings mBackendRenderSettings = new BackendRenderSettings();

    /**
     * Internal action handler.
     */
    private final FrontEndActionHandler mInternalHandler = new FrontEndActionHandler();

    /**
     * Action that something in render settings has changed.
     */
    private final Action<GlobalActionId> mDirty = new Action<>(this, GlobalActionId.RENDER_SETTINGS_DIRTY, ActionThread.MAIN);

    /**
     * The various settings.
     */
    private final BlendSetting mBlendSetting;
    private final CullFaceSetting mCullFaceSetting;
    private final DepthTestSetting mDepthTestSetting;
    private final DitherSetting mDitherSetting;
    private final PolygonOffsetFillSetting mPolygonOffsetFillSetting;
    private final SampleAlphaToCoverageSetting mSampleAlphaToCoverageSetting;
    private final SampleCoverageSetting mSampleCoverageSetting;
    private final ScissorTestSetting mScissorTestSetting;
    private final StencilTestSetting mStencilTestSetting;
    private final BlendEquationSetting mBlendEquationSetting;
    private final BlendFactorsSetting mBlendFactorsSetting;
    private final ClearSetting mClearSetting;
    private final ClearColorSetting mClearColorSetting;
    private final ClearDepthSetting mClearDepthSetting;
    private final ClearStencilSetting mClearStencilSetting;
    private final ViewPortSetting mViewPortSetting;

    @Override
    public boolean handleAction(Action<GlobalActionId> action) {
        switch (action.getType()) {
            case RENDER_SETTINGS_DIRTY:
                if (DebugOptions.DEBUG_RENDER_SETTINGS) {
                    if (!mInternal.hasActions()) {
                        throw new RuntimeException("handle internal actions called for no reason");
                    }
                }
                mInternalHandler.handleActions(mInternal);
                return true;
        }
        return super.handleAction(action);
    }

    /**
     * Constructor.
     */
    public RenderSettings() {
        mBlendSetting = new BlendSetting(mBackendRenderSettings);
        mCullFaceSetting = new CullFaceSetting(mBackendRenderSettings);
        mDepthTestSetting = new DepthTestSetting(mBackendRenderSettings);
        mDitherSetting = new DitherSetting(mBackendRenderSettings);
        mPolygonOffsetFillSetting = new PolygonOffsetFillSetting(mBackendRenderSettings);
        mSampleAlphaToCoverageSetting = new SampleAlphaToCoverageSetting(mBackendRenderSettings);
        mSampleCoverageSetting = new SampleCoverageSetting(mBackendRenderSettings);
        mScissorTestSetting = new ScissorTestSetting(mBackendRenderSettings);
        mStencilTestSetting = new StencilTestSetting(mBackendRenderSettings);
        mBlendEquationSetting = new BlendEquationSetting(mBackendRenderSettings);
        mBlendFactorsSetting = new BlendFactorsSetting(mBackendRenderSettings);
        mClearSetting = new ClearSetting(mBackendRenderSettings);
        mClearColorSetting = new ClearColorSetting(mBackendRenderSettings);
        mClearDepthSetting = new ClearDepthSetting(mBackendRenderSettings);
        mClearStencilSetting = new ClearStencilSetting(mBackendRenderSettings);
        mViewPortSetting = new ViewPortSetting(mBackendRenderSettings);

        mInternal.connectNotifier(mBlendSetting);
        mInternal.connectNotifier(mCullFaceSetting);
        mInternal.connectNotifier(mDepthTestSetting);
        mInternal.connectNotifier(mDitherSetting);
        mInternal.connectNotifier(mPolygonOffsetFillSetting);
        mInternal.connectNotifier(mSampleAlphaToCoverageSetting);
        mInternal.connectNotifier(mSampleCoverageSetting);
        mInternal.connectNotifier(mScissorTestSetting);
        mInternal.connectNotifier(mStencilTestSetting);
        mInternal.connectNotifier(mBlendEquationSetting);
        mInternal.connectNotifier(mBlendFactorsSetting);
        mInternal.connectNotifier(mClearSetting);
        mInternal.connectNotifier(mClearColorSetting);
        mInternal.connectNotifier(mClearDepthSetting);
        mInternal.connectNotifier(mClearStencilSetting);
        mInternal.connectNotifier(mViewPortSetting);
    }

    /**
     * Set blend.
     *
     * @param blend Blend or not.
     */
    public void setBlend(boolean blend) {
        mBlendSetting.set(blend);
    }

    /**
     * Get blend.
     *
     * @return The blend.
     */
    public boolean getBlend() {
        return mBlendSetting.get();
    }

    /**
     * Set cull face.
     *
     * @param cullFace Cull face or not.
     */
    public void setCullFace(boolean cullFace) {
        mCullFaceSetting.set(cullFace);
    }

    /**
     * Get cull face.
     *
     * @return The cull face.
     */
    public boolean getCullFace() {
        return mCullFaceSetting.get();
    }

    /**
     * Set depth test.
     *
     * @param depthTest Depth test or not.
     */
    public void setDepthTest(boolean depthTest) {
        mDepthTestSetting.set(depthTest);
    }

    /**
     * Get depth test.
     *
     * @return The depth test.
     */
    public boolean getDepthTest() {
        return mDepthTestSetting.get();
    }

    /**
     * Set dither.
     *
     * @param dither Dither or not.
     */
    public void setDither(boolean dither) {
        mDitherSetting.set(dither);
    }

    /**
     * Get dither.
     *
     * @return The dither.
     */
    public boolean getDither() {
        return mDitherSetting.get();
    }

    /**
     * Set polygon offset fill.
     *
     * @param polygonOffsetFill Polygon offset fill or not.
     */
    public void setPolygonOffsetFill(boolean polygonOffsetFill) {
        mPolygonOffsetFillSetting.set(polygonOffsetFill);
    }

    /**
     * Get polygon offset fill.
     *
     * @return The polygon offset fill.
     */
    public boolean getPolygonOffsetFill() {
        return mPolygonOffsetFillSetting.get();
    }

    /**
     * Set sample alpha to coverage.
     *
     * @param sampleAlphaToCoverage Sample alpha to coverage or not.
     */
    public void setSampleAlphaToCoverage(boolean sampleAlphaToCoverage) {
        mSampleAlphaToCoverageSetting.set(sampleAlphaToCoverage);
    }

    /**
     * Get sample alpha to coverage.
     *
     * @return The sample alpha to coverage.
     */
    public boolean getSampleAlphaToCoverage() {
        return mSampleAlphaToCoverageSetting.get();
    }

    /**
     * Set sample coverage.
     *
     * @param sampleCoverage Sample coverage or not.
     */
    public void setSampleCoverage(boolean sampleCoverage) {
        mSampleCoverageSetting.set(sampleCoverage);
    }

    /**
     * Get sample coverage.
     *
     * @return The sample coverage.
     */
    public boolean getSampleCoverage() {
        return mSampleCoverageSetting.get();
    }

    /**
     * Set scissor test.
     *
     * @param scissorTest Scissor test or not.
     */
    public void setScissorTest(boolean scissorTest) {
        mScissorTestSetting.set(scissorTest);
    }

    /**
     * Get scissor test.
     *
     * @return The scissor test.
     */
    public boolean getScissorTest() {
        return mScissorTestSetting.get();
    }

    /**
     * Set stencil test.
     *
     * @param stencil Stencil test or not.
     */
    public void setStencilTest(boolean stencil) {
        mStencilTestSetting.set(stencil);
    }

    /**
     * Get stencil test.
     *
     * @return The stencil test.
     */
    public boolean getStencilTest() {
        return mStencilTestSetting.get();
    }

    /**
     * Specify the equation used for both the RGB blend equation and the Alpha blend equation.
     *
     * @param func The function.
     */
    public void setBlendEqFunc(BlendEquationFunc func) {
        mBlendEquationSetting.set(func);
    }

    /**
     * Get the blend equation function.
     *
     * @return The blend equation function.
     */
    public BlendEquationFunc getBlendEqFunc() {
        return mBlendEquationSetting.get();
    }

    /**
     * Specifies how the red, green, blue, and alpha source blending factors are computed.
     *
     * @param sFactor The source factor.
     * @param dFactor The destination factor.
     */
    public void setBlendFact(BlendSrcFact sFactor, BlendDstFact dFactor) {
        mBlendFactorsSetting.set(sFactor, dFactor);
    }

    /**
     * Get the blend source factor.
     *
     * @return The source factor.
     */
    public BlendSrcFact getBlendSrcFact() {
        return mBlendFactorsSetting.getSrcFactor();
    }

    /**
     * Get the blend destination factor.
     *
     * @return The destination factor.
     */
    public BlendDstFact getBlendDstFact() {
        return mBlendFactorsSetting.getDstFactor();
    }

    /**
     * Set clear.
     *
     * @param order Clear order in relation to other clear.
     * @param bits  Bits to set.
     */
    public void setClear(int order, ClearBit... bits) {
        mClearSetting.set(order, bits);
    }

    /**
     * Get clear order.
     *
     * @return The order.
     */
    public int getClearOrder() {
        return mClearSetting.getClearOrder();
    }

    /**
     * Get clear bits.
     *
     * @return The bits.
     */
    public ClearBit[] getClearBits() {
        return mClearSetting.getClearBits();
    }

    /**
     * Get the clear mask (based on clear bits).
     *
     * @return The clear mask.
     */
    public ClearMask getClearMask() {
        return mClearSetting.getClearMask();
    }

    /**
     * Set clear color.
     *
     * @param color The color.
     */
    public void setClearColor(float[] color) {
        mClearColorSetting.set(color);
    }

    /**
     * Get clear color.
     *
     * @return The clear color.
     */
    public float[] getClearColor() {
        return mClearColorSetting.getClearColor();
    }

    /**
     * Set clear depth.
     *
     * @param depth The depth.
     */
    public void setClearDepth(float depth) {
        mClearDepthSetting.set(depth);
    }

    /**
     * Get clear depth.
     *
     * @return The clear depth.
     */
    public float getClearDepth() {
        return mClearDepthSetting.getClearDepth();
    }

    /**
     * Set clear stencil.
     *
     * @param stencil The stencil.
     */
    public void setClearStencil(int stencil) {
        mClearStencilSetting.set(stencil);
    }

    /**
     * Get clear stencil.
     *
     * @return The clear stencil.
     */
    public int getClearStencil() {
        return mClearStencilSetting.getClearStencil();
    }

    /**
     * Set the viewport.
     *
     * @param x      Lower left x value.
     * @param y      Lower left y value.
     * @param width  Width.
     * @param height Height.
     */
    public void setViewPort(int x, int y, int width, int height) {
        mViewPortSetting.set(x, y, width, height);
    }

    /**
     * Get viewport lower left x value.
     *
     * @return The x value.
     */
    public int getViewPortX() {
        return mViewPortSetting.getX();
    }

    /**
     * Get viewport lower left y value.
     *
     * @return The y value.
     */
    public int getViewPortY() {
        return mViewPortSetting.getY();
    }

    /**
     * Get viewport width value.
     *
     * @return The width value.
     */
    public int getViewPortWidth() {
        return mViewPortSetting.getWidth();
    }

    /**
     * Get viewport height value.
     *
     * @return The height value.
     */
    public int getViewPortHeight() {
        return mViewPortSetting.getHeight();
    }

    /**
     * Inherit render settings. This render settings will only inherit values that already are
     * inherited or have default values. Values that are specifically set to this render
     * settings will not be changed.
     *
     * @param parent Parent render settings.
     */
    public void inherit(RenderSettings parent) {
        if (!mBlendSetting.getHistory().equals(History.SET)) {
            if (!mBlendSetting.equals(parent.mBlendSetting)) {
                mBlendSetting.inherit(parent.mBlendSetting);
            }
        }
        if (!mCullFaceSetting.getHistory().equals(History.SET)) {
            if (!mCullFaceSetting.equals(parent.mCullFaceSetting)) {
                mCullFaceSetting.inherit(parent.mCullFaceSetting);
            }
        }
        if (!mDepthTestSetting.getHistory().equals(History.SET)) {
            if (!mDepthTestSetting.equals(parent.mDepthTestSetting)) {
                mDepthTestSetting.inherit(parent.mDepthTestSetting);
            }
        }
        if (!mDitherSetting.getHistory().equals(History.SET)) {
            if (!mDitherSetting.equals(parent.mDitherSetting)) {
                mDitherSetting.inherit(parent.mDitherSetting);
            }
        }
        if (!mPolygonOffsetFillSetting.getHistory().equals(History.SET)) {
            if (!mPolygonOffsetFillSetting.equals(parent.mPolygonOffsetFillSetting)) {
                mPolygonOffsetFillSetting.inherit(parent.mPolygonOffsetFillSetting);
            }
        }
        if (!mSampleAlphaToCoverageSetting.getHistory().equals(History.SET)) {
            if (!mSampleAlphaToCoverageSetting.equals(parent.mSampleAlphaToCoverageSetting)) {
                mSampleAlphaToCoverageSetting.inherit(parent.mSampleAlphaToCoverageSetting);
            }
        }
        if (!mSampleCoverageSetting.getHistory().equals(History.SET)) {
            if (!mSampleCoverageSetting.equals(parent.mSampleCoverageSetting)) {
                mSampleCoverageSetting.inherit(parent.mSampleCoverageSetting);
            }
        }
        if (!mScissorTestSetting.getHistory().equals(History.SET)) {
            if (!mScissorTestSetting.equals(parent.mScissorTestSetting)) {
                mScissorTestSetting.inherit(parent.mScissorTestSetting);
            }
        }
        if (!mStencilTestSetting.getHistory().equals(History.SET)) {
            if (!mStencilTestSetting.equals(parent.mStencilTestSetting)) {
                mStencilTestSetting.inherit(parent.mStencilTestSetting);
            }
        }
        if (!mBlendEquationSetting.getHistory().equals(History.SET)) {
            if (!mBlendEquationSetting.equals(parent.mBlendEquationSetting)) {
                mBlendEquationSetting.inherit(parent.mBlendEquationSetting);
            }
        }
        if (!mBlendFactorsSetting.getHistory().equals(History.SET)) {
            if (!mBlendFactorsSetting.equals(parent.mBlendFactorsSetting)) {
                mBlendFactorsSetting.inherit(parent.mBlendFactorsSetting);
            }
        }
        if (!mClearSetting.getHistory().equals(History.SET)) {
            if (!mClearSetting.equals(parent.mClearSetting)) {
                mClearSetting.inherit(parent.mClearSetting);
            }
        }
        if (!mClearColorSetting.getHistory().equals(History.SET)) {
            if (!mClearColorSetting.equals(parent.mClearColorSetting)) {
                mClearColorSetting.inherit(parent.mClearColorSetting);
            }
        }
        if (!mClearDepthSetting.getHistory().equals(History.SET)) {
            if (!mClearDepthSetting.equals(parent.mClearDepthSetting)) {
                mClearDepthSetting.inherit(parent.mClearDepthSetting);
            }
        }
        if (!mClearStencilSetting.getHistory().equals(History.SET)) {
            if (!mClearStencilSetting.equals(parent.mClearStencilSetting)) {
                mClearStencilSetting.inherit(parent.mClearStencilSetting);
            }
        }
        if (!mViewPortSetting.getHistory().equals(History.SET)) {
            if (!mViewPortSetting.equals(parent.mViewPortSetting)) {
                mViewPortSetting.inherit(parent.mViewPortSetting);
            }
        }
    }

    /**
     * Use this render settings.
     *
     * @param context Backend context.
     */
    public void useSettings(BackendContext context) {
        final boolean clear = mClearSetting.getClearOrder() > context.getRenderState().getClearOrder();
        context.getRenderState().useSettings(mBackendRenderSettings);
        if (clear) {
            context.getRenderState().clear();
        }
    }
}

package com.sweetlab.sweetride.node.rendersettings;

import com.sweetlab.sweetride.action.Action;
import com.sweetlab.sweetride.action.ActionThread;
import com.sweetlab.sweetride.context.BackendRenderSettings;

/**
 * Specific render setting.
 */
public class BlendFactorsSetting extends BaseSetting {
    /**
     * Change action.
     */
    private final Action<RsActionId> mDirty = new Action<>(this, RsActionId.BLEND_FACT, ActionThread.MAIN);

    /**
     * Current source value.
     */
    private BlendSrcFact mBlendSrcFact = BlendSrcFact.ONE;

    /**
     * Current destination value.
     */
    private BlendDstFact mBlendDstFact = BlendDstFact.ZERO;

    /**
     * Constructor.
     *
     * @param settings The backend render settings.
     */
    public BlendFactorsSetting(BackendRenderSettings settings) {
        super(settings);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlendFactorsSetting that = (BlendFactorsSetting) o;
        if (mBlendSrcFact != that.mBlendSrcFact) return false;
        return mBlendDstFact == that.mBlendDstFact;
    }

    @Override
    public int hashCode() {
        int result = mBlendSrcFact.hashCode();
        result = 31 * result + mBlendDstFact.hashCode();
        return result;
    }

    @Override
    public boolean handleAction(Action<RsActionId> action) {
        switch (action.getType()) {
            case BLEND_FACT:
                mBackendSettings.setBlendFact(mBlendSrcFact.getGL(), mBlendDstFact.getGL());
                return true;
        }
        return super.handleAction(action);
    }

    /**
     * Set based on other.
     *
     * @param other The other setting.
     */
    public void set(BlendFactorsSetting other) {
        set(other.getSrcFactor(), other.getDstFactor());
    }

    /**
     * Set new value.
     *
     * @param src The source value.
     * @param dst The destination value.
     */
    public void set(BlendSrcFact src, BlendDstFact dst) {
        setValues(src, dst);
        mHistory = History.SET;
    }

    /**
     * Inherit based on other.
     *
     * @param other The other setting.
     */
    public void inherit(BlendFactorsSetting other) {
        inherit(other.getSrcFactor(), other.getDstFactor());
    }

    /**
     * Inherit new value.
     *
     * @param source      The source value.
     * @param destination The destination value.
     */
    public void inherit(BlendSrcFact source, BlendDstFact destination) {
        setValues(source, destination);
        mHistory = History.INHERITED;
    }

    /**
     * Get the current source factor.
     *
     * @return The source factor.
     */
    public BlendSrcFact getSrcFactor() {
        return mBlendSrcFact;
    }

    /**
     * Get the current destination factor.
     *
     * @return The destination factor.
     */
    public BlendDstFact getDstFactor() {
        return mBlendDstFact;
    }

    /**
     * Set values.
     *
     * @param src Source value.
     * @param dst Destination value.
     */
    private void setValues(BlendSrcFact src, BlendDstFact dst) {
        mBlendSrcFact = src;
        mBlendDstFact = dst;
        addAction(mDirty);
    }
}

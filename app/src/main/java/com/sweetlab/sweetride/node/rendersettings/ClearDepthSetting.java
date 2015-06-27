package com.sweetlab.sweetride.node.rendersettings;

import com.sweetlab.sweetride.action.Action;
import com.sweetlab.sweetride.action.ActionThread;
import com.sweetlab.sweetride.context.BackendRenderSettings;

/**
 * Specific render setting.
 */
public class ClearDepthSetting extends BaseSetting {
    /**
     * Change action.
     */
    private final Action<RsActionId> mDirty = new Action<>(this, RsActionId.CLEAR_DEPTH, ActionThread.MAIN);

    /**
     * Clear depth value.
     */
    private float mDepth = 1;

    /**
     * Constructor.
     *
     * @param settings The backend render settings.
     */
    public ClearDepthSetting(BackendRenderSettings settings) {
        super(settings);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClearDepthSetting that = (ClearDepthSetting) o;
        return Float.compare(that.mDepth, mDepth) == 0;

    }

    @Override
    public int hashCode() {
        return (mDepth != +0.0f ? Float.floatToIntBits(mDepth) : 0);
    }

    @Override
    public boolean handleAction(Action<RsActionId> action) {
        switch (action.getType()) {
            case CLEAR_DEPTH:
                mBackendSettings.setClearDepth(mDepth);
                return true;
        }
        return super.handleAction(action);
    }

    /**
     * Set new based on other.
     *
     * @param other The other setting.
     */
    public void set(ClearDepthSetting other) {
        set(other.mDepth);
    }

    /**
     * Set new values.
     *
     * @param depth The depth clear value.
     */
    public void set(float depth) {
        mDepth = depth;
        addAction(mDirty);
        mHistory = History.SET;
    }

    /**
     * Inherit new based on other.
     *
     * @param other The other setting.
     */
    public void inherit(ClearDepthSetting other) {
        inherit(other.mDepth);
    }

    /**
     * Inherit new value.
     *
     * @param depth The depth clear value.
     */
    public void inherit(float depth) {
        mDepth = depth;
        addAction(mDirty);
        mHistory = History.INHERITED;
    }

    /**
     * Get the clear depth.
     *
     * @return The clear depth.
     */
    public float getClearDepth() {
        return mDepth;
    }
}

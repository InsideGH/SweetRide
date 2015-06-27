package com.sweetlab.sweetride.node.rendersettings;

import com.sweetlab.sweetride.action.Action;
import com.sweetlab.sweetride.action.ActionThread;
import com.sweetlab.sweetride.context.BackendRenderSettings;

/**
 * Specific render setting.
 */
public class StencilTestSetting extends BaseSetting {
    /**
     * Change action.
     */
    private final Action<RsActionId> mDirty = new Action<>(this, RsActionId.STENCIL_TEST, ActionThread.MAIN);

    /**
     * Current value.
     */
    private boolean mStencilTest;

    /**
     * Constructor.
     *
     * @param settings The backend render settings.
     */
    public StencilTestSetting(BackendRenderSettings settings) {
        super(settings);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StencilTestSetting that = (StencilTestSetting) o;
        return mStencilTest == that.mStencilTest;

    }

    @Override
    public int hashCode() {
        return (mStencilTest ? 1 : 0);
    }

    @Override
    public boolean handleAction(Action<RsActionId> action) {
        switch (action.getType()) {
            case STENCIL_TEST:
                mBackendSettings.setStencilTest(mStencilTest);
                return true;
        }
        return super.handleAction(action);
    }

    /**
     * Set new based on other.
     *
     * @param other The other setting.
     */
    public void set(StencilTestSetting other) {
        set(other.mStencilTest);
    }

    /**
     * Set new value.
     *
     * @param value The new value.
     */
    public void set(boolean value) {
        setValue(value);
        mHistory = History.SET;
    }

    /**
     * Inherit new based on other.
     *
     * @param other The other setting.
     */
    public void inherit(StencilTestSetting other) {
        inherit(other.mStencilTest);
    }

    /**
     * Inherit new value.
     *
     * @param value The value.
     */
    public void inherit(boolean value) {
        setValue(value);
        mHistory = History.INHERITED;
    }

    /**
     * Get the current setting.
     *
     * @return The setting.
     */
    public boolean get() {
        return mStencilTest;
    }

    /**
     * Set value.
     *
     * @param value The value.
     */
    private void setValue(boolean value) {
        mStencilTest = value;
        addAction(mDirty);
    }
}

package com.sweetlab.sweetride.node.rendersettings;

import com.sweetlab.sweetride.action.Action;
import com.sweetlab.sweetride.action.ActionThread;
import com.sweetlab.sweetride.context.BackendRenderSettings;

/**
 * Specific render setting.
 */
public class DepthTestSetting extends BaseSetting {
    /**
     * Change action.
     */
    private final Action<RsActionId> mDirty = new Action<>(this, RsActionId.DEPTH_TEST, ActionThread.MAIN);

    /**
     * Current value.
     */
    private boolean mDepthTest;

    /**
     * Constructor.
     *
     * @param settings The backend render settings.
     */
    public DepthTestSetting(BackendRenderSettings settings) {
        super(settings);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DepthTestSetting that = (DepthTestSetting) o;
        return mDepthTest == that.mDepthTest;
    }

    @Override
    public int hashCode() {
        return (mDepthTest ? 1 : 0);
    }

    @Override
    public boolean handleAction(Action<RsActionId> action) {
        switch (action.getType()) {
            case DEPTH_TEST:
                mBackendSettings.setDepthTest(mDepthTest);
                return true;
        }
        return super.handleAction(action);
    }

    /**
     * Set new based on other.
     *
     * @param other The other setting.
     */
    public void set(DepthTestSetting other) {
        set(other.mDepthTest);
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
    public void inherit(DepthTestSetting other) {
        inherit(other.mDepthTest);
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
        return mDepthTest;
    }

    /**
     * Set value.
     *
     * @param value The value.
     */
    private void setValue(boolean value) {
        mDepthTest = value;
        addAction(mDirty);
    }
}

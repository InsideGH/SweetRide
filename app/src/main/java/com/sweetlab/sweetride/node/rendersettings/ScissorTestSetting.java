package com.sweetlab.sweetride.node.rendersettings;

import com.sweetlab.sweetride.action.Action;
import com.sweetlab.sweetride.action.ActionThread;
import com.sweetlab.sweetride.context.BackendRenderSettings;

/**
 * Specific render setting.
 */
public class ScissorTestSetting extends BaseSetting {
    /**
     * Change action.
     */
    private final Action<RsActionId> mDirty = new Action<>(this, RsActionId.SCISSOR_TEST, ActionThread.MAIN);

    /**
     * Current value.
     */
    private boolean mScissorTest;

    /**
     * Constructor.
     *
     * @param settings The backend render settings.
     */
    public ScissorTestSetting(BackendRenderSettings settings) {
        super(settings);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScissorTestSetting that = (ScissorTestSetting) o;
        return mScissorTest == that.mScissorTest;
    }

    @Override
    public int hashCode() {
        return (mScissorTest ? 1 : 0);
    }

    @Override
    public boolean handleAction(Action<RsActionId> action) {
        switch (action.getType()) {
            case SCISSOR_TEST:
                mBackendSettings.setScissorTest(mScissorTest);
                return true;
        }
        return super.handleAction(action);
    }

    /**
     * Set new based on other.
     *
     * @param other The other setting.
     */
    public void set(ScissorTestSetting other) {
        set(other.mScissorTest);
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
    public void inherit(ScissorTestSetting other) {
        inherit(other.mScissorTest);
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
        return mScissorTest;
    }

    /**
     * Set value.
     *
     * @param value The value.
     */
    private void setValue(boolean value) {
        mScissorTest = value;
        addAction(mDirty);
    }
}

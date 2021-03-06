package com.sweetlab.sweetride.node.rendersettings;

import com.sweetlab.sweetride.action.Action;
import com.sweetlab.sweetride.action.ActionThread;
import com.sweetlab.sweetride.context.BackendRenderSettings;

/**
 * Specific render setting.
 */
public class BlendSetting extends BaseSetting {
    /**
     * Change action.
     */
    private final Action<RsActionId> mDirty = new Action<>(this, RsActionId.BLEND, ActionThread.MAIN);

    /**
     * Current value.
     */
    private boolean mBlend;

    /**
     * Constructor.
     *
     * @param settings The backend render settings.
     */
    public BlendSetting(BackendRenderSettings settings) {
        super(settings);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlendSetting that = (BlendSetting) o;
        return mBlend == that.mBlend;
    }

    @Override
    public int hashCode() {
        return (mBlend ? 1 : 0);
    }

    @Override
    public boolean handleAction(Action<RsActionId> action) {
        switch (action.getType()) {
            case BLEND:
                mBackendSettings.setBlend(mBlend);
                return true;
        }
        return super.handleAction(action);
    }

    /**
     * Set new based on other.
     *
     * @param other The other setting.
     */
    public void set(BlendSetting other) {
        set(other.mBlend);
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
    public void inherit(BlendSetting other) {
        inherit(other.mBlend);
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
        return mBlend;
    }

    /**
     * Set value.
     *
     * @param value The value.
     */
    private void setValue(boolean value) {
        mBlend = value;
        addAction(mDirty);
    }
}

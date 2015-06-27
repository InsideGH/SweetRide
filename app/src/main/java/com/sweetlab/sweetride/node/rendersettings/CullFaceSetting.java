package com.sweetlab.sweetride.node.rendersettings;

import com.sweetlab.sweetride.action.Action;
import com.sweetlab.sweetride.action.ActionThread;
import com.sweetlab.sweetride.context.BackendRenderSettings;

/**
 * Specific render setting.
 */
public class CullFaceSetting extends BaseSetting {
    /**
     * Change action.
     */
    private final Action<RsActionId> mDirty = new Action<>(this, RsActionId.CULL_FACE, ActionThread.MAIN);

    /**
     * Current value.
     */
    private boolean mCullFace;

    /**
     * Constructor.
     *
     * @param settings The backend render settings.
     */
    public CullFaceSetting(BackendRenderSettings settings) {
        super(settings);
    }

    @Override
    public int hashCode() {
        return (mCullFace ? 1 : 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CullFaceSetting that = (CullFaceSetting) o;
        return mCullFace == that.mCullFace;
    }

    @Override
    public boolean handleAction(Action<RsActionId> action) {
        switch (action.getType()) {
            case CULL_FACE:
                mBackendSettings.setCullFace(mCullFace);
                return true;
        }
        return super.handleAction(action);
    }

    /**
     * Set new based on other.
     *
     * @param other The other setting.
     */
    public void set(CullFaceSetting other) {
        set(other.mCullFace);
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
    public void inherit(CullFaceSetting other) {
        inherit(other.mCullFace);
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
        return mCullFace;
    }

    /**
     * Set value.
     *
     * @param value The value.
     */
    private void setValue(boolean value) {
        mCullFace = value;
        addAction(mDirty);
    }
}

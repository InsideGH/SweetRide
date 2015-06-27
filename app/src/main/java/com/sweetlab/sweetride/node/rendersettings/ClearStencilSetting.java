package com.sweetlab.sweetride.node.rendersettings;

import com.sweetlab.sweetride.action.Action;
import com.sweetlab.sweetride.action.ActionThread;
import com.sweetlab.sweetride.context.BackendRenderSettings;

/**
 * Specific render setting.
 */
public class ClearStencilSetting extends BaseSetting {
    /**
     * Change action.
     */
    private final Action<RsActionId> mDirty = new Action<>(this, RsActionId.CLEAR_STENCIL, ActionThread.MAIN);

    /**
     * Clear stencil value.
     */
    private int mStencil;

    /**
     * Constructor.
     *
     * @param settings The backend render settings.
     */
    public ClearStencilSetting(BackendRenderSettings settings) {
        super(settings);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClearStencilSetting that = (ClearStencilSetting) o;
        return mStencil == that.mStencil;

    }

    @Override
    public int hashCode() {
        return mStencil;
    }

    @Override
    public boolean handleAction(Action<RsActionId> action) {
        switch (action.getType()) {
            case CLEAR_STENCIL:
                mBackendSettings.setClearStencil(mStencil);
                return true;
        }
        return super.handleAction(action);
    }

    /**
     * Set new based on other.
     *
     * @param other The other setting.
     */
    public void set(ClearStencilSetting other) {
        set(other.mStencil);
    }

    /**
     * Set new values.
     *
     * @param stencil The stencil clear value.
     */
    public void set(int stencil) {
        mStencil = stencil;
        addAction(mDirty);
        mHistory = History.SET;
    }

    /**
     * Inherit new based on other.
     *
     * @param other The other setting.
     */
    public void inherit(ClearStencilSetting other) {
        inherit(other.mStencil);
    }

    /**
     * Inherit new value.
     *
     * @param stencil The stencil clear value.
     */
    public void inherit(int stencil) {
        mStencil = stencil;
        addAction(mDirty);
        mHistory = History.INHERITED;
    }

    /**
     * Get the clear stencil.
     *
     * @return The clear stencil.
     */
    public int getClearStencil() {
        return mStencil;
    }
}

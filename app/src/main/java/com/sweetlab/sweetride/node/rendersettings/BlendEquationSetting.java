package com.sweetlab.sweetride.node.rendersettings;

import com.sweetlab.sweetride.action.Action;
import com.sweetlab.sweetride.action.ActionThread;
import com.sweetlab.sweetride.context.BackendRenderSettings;

/**
 * Specific render setting.
 */
public class BlendEquationSetting extends BaseSetting {
    /**
     * Change action.
     */
    private final Action<RsActionId> mDirty = new Action<>(this, RsActionId.BLEND_EQUATION_FUNC, ActionThread.MAIN);

    /**
     * Current value.
     */
    private BlendEquationFunc mBlendEquationFunc = BlendEquationFunc.ADD;

    /**
     * Constructor.
     *
     * @param settings The backend render settings.
     */
    public BlendEquationSetting(BackendRenderSettings settings) {
        super(settings);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlendEquationSetting that = (BlendEquationSetting) o;
        return mBlendEquationFunc == that.mBlendEquationFunc;
    }

    @Override
    public int hashCode() {
        return mBlendEquationFunc.hashCode();
    }

    @Override
    public boolean handleAction(Action<RsActionId> action) {
        switch (action.getType()) {
            case BLEND_EQUATION_FUNC:
                mBackendSettings.setBlendEqFunc(mBlendEquationFunc.getGL());
                return true;
        }
        return super.handleAction(action);
    }

    /**
     * Set new based on other.
     *
     * @param other The other setting.
     */
    public void set(BlendEquationSetting other) {
        set(other.mBlendEquationFunc);
    }

    /**
     * Set new value.
     *
     * @param value The new value.
     */
    public void set(BlendEquationFunc value) {
        setValue(value);
        mHistory = History.SET;
    }

    /**
     * Inherit new based on other.
     *
     * @param other The other setting.
     */
    public void inherit(BlendEquationSetting other) {
        inherit(other.mBlendEquationFunc);
    }

    /**
     * Inherit new value.
     *
     * @param value The value.
     */
    public void inherit(BlendEquationFunc value) {
        setValue(value);
        mHistory = History.INHERITED;
    }

    /**
     * Get the current setting.
     *
     * @return The setting.
     */
    public BlendEquationFunc get() {
        return mBlendEquationFunc;
    }

    /**
     * Set value.
     *
     * @param value The value.
     */
    private void setValue(BlendEquationFunc value) {
        mBlendEquationFunc = value;
        addAction(mDirty);
    }
}

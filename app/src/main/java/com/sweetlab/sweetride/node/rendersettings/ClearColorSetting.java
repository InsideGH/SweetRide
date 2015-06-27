package com.sweetlab.sweetride.node.rendersettings;

import com.sweetlab.sweetride.action.Action;
import com.sweetlab.sweetride.action.ActionThread;
import com.sweetlab.sweetride.context.BackendRenderSettings;

import java.util.Arrays;

/**
 * Specific render setting.
 */
public class ClearColorSetting extends BaseSetting {
    /**
     * Change action.
     */
    private final Action<RsActionId> mDirty = new Action<>(this, RsActionId.CLEAR_COLOR, ActionThread.MAIN);

    /**
     * Clear color.
     */
    private final float[] mColor = new float[4];

    /**
     * Constructor.
     *
     * @param settings The backend render settings.
     */
    public ClearColorSetting(BackendRenderSettings settings) {
        super(settings);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClearColorSetting that = (ClearColorSetting) o;
        return Arrays.equals(mColor, that.mColor);

    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(mColor);
    }

    @Override
    public boolean handleAction(Action<RsActionId> action) {
        switch (action.getType()) {
            case CLEAR_COLOR:
                mBackendSettings.setClearColor(mColor);
                return true;
        }
        return super.handleAction(action);
    }

    /**
     * Set new based on other.
     *
     * @param other The other setting.
     */
    public void set(ClearColorSetting other) {
        setValues(other.mColor);
        mHistory = History.SET;
    }

    /**
     * Set new values.
     *
     * @param color The color clear value.
     */
    public void set(float[] color) {
        setValues(color);
        mHistory = History.SET;
    }

    /**
     * Inherit new based on other.
     *
     * @param other The other setting.
     */
    public void inherit(ClearColorSetting other) {
        setValues(other.mColor);
        mHistory = History.INHERITED;
    }

    /**
     * Inherit new value.
     *
     * @param color The color.
     */
    public void inherit(float[] color) {
        setValues(color);
        mHistory = History.INHERITED;
    }

    /**
     * Get clear color.
     *
     * @return The clear color.
     */
    public float[] getClearColor() {
        return mColor;
    }

    /**
     * Set values.
     *
     * @param color The colors.
     */
    private void setValues(float[] color) {
        System.arraycopy(color, 0, mColor, 0, color.length);
        addAction(mDirty);
    }
}

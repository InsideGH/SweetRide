package com.sweetlab.sweetride.node.rendersettings;

import com.sweetlab.sweetride.action.Action;
import com.sweetlab.sweetride.action.ActionThread;
import com.sweetlab.sweetride.context.BackendRenderSettings;

/**
 * Specific render setting.
 */
public class PolygonOffsetFillSetting extends BaseSetting {
    /**
     * Change action.
     */
    private final Action<RsActionId> mDirty = new Action<>(this, RsActionId.POLYGON_OFFSET_FILL, ActionThread.MAIN);

    /**
     * Current value.
     */
    private boolean mPolygonOffsetFill;

    /**
     * Constructor.
     *
     * @param settings The backend render settings.
     */
    public PolygonOffsetFillSetting(BackendRenderSettings settings) {
        super(settings);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PolygonOffsetFillSetting that = (PolygonOffsetFillSetting) o;
        return mPolygonOffsetFill == that.mPolygonOffsetFill;
    }

    @Override
    public int hashCode() {
        return (mPolygonOffsetFill ? 1 : 0);
    }

    @Override
    public boolean handleAction(Action<RsActionId> action) {
        switch (action.getType()) {
            case POLYGON_OFFSET_FILL:
                mBackendSettings.setPolygonOffsetFill(mPolygonOffsetFill);
                return true;
        }
        return super.handleAction(action);
    }

    /**
     * Set new based on other.
     *
     * @param other The other setting.
     */
    public void set(PolygonOffsetFillSetting other) {
        set(other.mPolygonOffsetFill);
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
    public void inherit(PolygonOffsetFillSetting other) {
        inherit(other.mPolygonOffsetFill);
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
        return mPolygonOffsetFill;
    }

    /**
     * Set value.
     *
     * @param value The value.
     */
    private void setValue(boolean value) {
        mPolygonOffsetFill = value;
        addAction(mDirty);
    }
}

package com.sweetlab.sweetride.node.rendersettings;

import com.sweetlab.sweetride.action.Action;
import com.sweetlab.sweetride.action.ActionThread;
import com.sweetlab.sweetride.context.BackendRenderSettings;

/**
 * Specific render setting.
 */
public class ViewPortSetting extends BaseSetting {
    /**
     * Change action.
     */
    private final Action<RsActionId> mDirty = new Action<>(this, RsActionId.VIEWPORT, ActionThread.MAIN);

    /**
     * Lower left corner in px.
     */
    private int mX;

    /**
     * Lower left corner in px.
     */
    private int mY;

    /**
     * Viewport width in px.
     */
    private int mWidth;

    /**
     * Viewport height in px.
     */
    private int mHeight;

    /**
     * Constructor.
     *
     * @param settings The backend render settings.
     */
    public ViewPortSetting(BackendRenderSettings settings) {
        super(settings);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ViewPortSetting that = (ViewPortSetting) o;
        if (mX != that.mX) return false;
        if (mY != that.mY) return false;
        if (mWidth != that.mWidth) return false;
        return mHeight == that.mHeight;
    }

    @Override
    public int hashCode() {
        int result = mX;
        result = 31 * result + mY;
        result = 31 * result + mWidth;
        result = 31 * result + mHeight;
        return result;
    }

    @Override
    public boolean handleAction(Action<RsActionId> action) {
        switch (action.getType()) {
            case VIEWPORT:
                mBackendSettings.setViewPort(mX, mY, mWidth, mHeight);
                return true;
        }
        return super.handleAction(action);
    }

    /**
     * Set new based on other.
     *
     * @param other The other setting.
     */
    public void set(ViewPortSetting other) {
        set(other.mX, other.mY, other.mWidth, other.mHeight);
    }

    /**
     * Set new value.
     *
     * @param x      Lower left x.
     * @param y      Lower left y
     * @param width  Width.
     * @param height Height.
     */
    public void set(int x, int y, int width, int height) {
        setValue(x, y, width, height);
        mHistory = History.SET;
    }

    /**
     * Inherit new based on other.
     *
     * @param other The other setting.
     */
    public void inherit(ViewPortSetting other) {
        inherit(other.mX, other.mY, other.mWidth, other.mHeight);
    }

    /**
     * Inherit new value.
     *
     * @param x      Lower left x.
     * @param y      Lower left y
     * @param width  Width.
     * @param height Height.
     */
    public void inherit(int x, int y, int width, int height) {
        setValue(x, y, width, height);
        mHistory = History.INHERITED;
    }

    /**
     * Get the lower left x value.
     *
     * @return The x value.
     */
    public int getX() {
        return mX;
    }

    /**
     * Get the lower left y value.
     *
     * @return The y value.
     */
    public int getY() {
        return mY;
    }

    /**
     * Get the width value.
     *
     * @return The width value.
     */
    public int getWidth() {
        return mWidth;
    }

    /**
     * Get the height value.
     *
     * @return The height value.
     */
    public int getHeight() {
        return mHeight;
    }


    /**
     * Set values.
     *
     * @param x      Lower left x.
     * @param y      Lower left y
     * @param width  Width.
     * @param height Height.
     */
    private void setValue(int x, int y, int width, int height) {
        mX = x;
        mY = y;
        mWidth = width;
        mHeight = height;
        addAction(mDirty);
    }
}

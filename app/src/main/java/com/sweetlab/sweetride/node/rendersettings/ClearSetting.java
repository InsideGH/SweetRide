package com.sweetlab.sweetride.node.rendersettings;

import com.sweetlab.sweetride.action.Action;
import com.sweetlab.sweetride.action.ActionThread;
import com.sweetlab.sweetride.context.BackendRenderSettings;

/**
 * Specific render setting.
 */
public class ClearSetting extends BaseSetting {
    /**
     * Change action.
     */
    private final Action<RsActionId> mDirty = new Action<>(this, RsActionId.CLEAR, ActionThread.MAIN);

    /**
     * Clear order in relation to other clear.
     */
    private int mOrder;

    /**
     * The clear bits.
     */
    private ClearBit[] mBits = new ClearBit[]{ClearBit.ZERO_BIT};

    /**
     * Clear mask.
     */
    private ClearMask mMask = new ClearMask(mBits);

    /**
     * Constructor.
     *
     * @param settings The backend render settings.
     */
    public ClearSetting(BackendRenderSettings settings) {
        super(settings);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClearSetting that = (ClearSetting) o;

        if (mOrder != that.mOrder) return false;
        return mMask.equals(that.mMask);

    }

    @Override
    public int hashCode() {
        int result = mOrder;
        result = 31 * result + mMask.hashCode();
        return result;
    }

    @Override
    public boolean handleAction(Action<RsActionId> action) {
        switch (action.getType()) {
            case CLEAR:
                mBackendSettings.setClear(mOrder, mMask.getGL());
                return true;
        }
        return super.handleAction(action);
    }

    /**
     * Set new based on other.
     *
     * @param other The other setting.
     */
    public void set(ClearSetting other) {
        set(other.mOrder, other.mBits);
    }

    /**
     * Set new values.
     *
     * @param order The clear order in relation to other clear.
     * @param bits  The bits.
     */
    public void set(int order, ClearBit... bits) {
        setValues(order, bits);
        mHistory = History.SET;
        addAction(mDirty);
    }

    /**
     * Inherit new based on other.
     *
     * @param other The other setting.
     */
    public void inherit(ClearSetting other) {
        inherit(other.mOrder, other.mBits);
    }

    /**
     * Inherit new value.
     *
     * @param order The clear order in relation to other clear.
     * @param bits  The bits.
     */
    public void inherit(int order, ClearBit... bits) {
        setValues(order, bits);
        mHistory = History.INHERITED;
        addAction(mDirty);
    }

    /**
     * Get clear bits.
     *
     * @return The clear bits.
     */
    public ClearBit[] getClearBits() {
        return mBits;
    }

    /**
     * Get the clear mask.
     *
     * @return The clear mask.
     */
    public ClearMask getClearMask() {
        return mMask;
    }

    /**
     * Get the clear order.
     *
     * @return The clear order.
     */
    public int getClearOrder() {
        return mOrder;
    }

    /**
     * Set values.
     *
     * @param order The clear order.
     * @param bits  The clear bits.
     */
    private void setValues(int order, ClearBit[] bits) {
        mOrder = order;

        mBits = new ClearBit[bits.length];
        System.arraycopy(bits, 0, mBits, 0, bits.length);

        mMask = new ClearMask(bits);
    }
}

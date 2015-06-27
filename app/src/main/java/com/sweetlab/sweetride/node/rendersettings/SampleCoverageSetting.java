package com.sweetlab.sweetride.node.rendersettings;

import com.sweetlab.sweetride.action.Action;
import com.sweetlab.sweetride.action.ActionThread;
import com.sweetlab.sweetride.context.BackendRenderSettings;

/**
 * Specific render setting.
 */
public class SampleCoverageSetting extends BaseSetting {
    /**
     * Change action.
     */
    private final Action<RsActionId> mDirty = new Action<>(this, RsActionId.SAMPLE_COVERAGE, ActionThread.MAIN);

    /**
     * Current value.
     */
    private boolean mSampleCoverage;

    /**
     * Constructor.
     *
     * @param settings The backend render settings.
     */
    public SampleCoverageSetting(BackendRenderSettings settings) {
        super(settings);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SampleCoverageSetting that = (SampleCoverageSetting) o;
        return mSampleCoverage == that.mSampleCoverage;
    }

    @Override
    public int hashCode() {
        return (mSampleCoverage ? 1 : 0);
    }

    @Override
    public boolean handleAction(Action<RsActionId> action) {
        switch (action.getType()) {
            case SAMPLE_COVERAGE:
                mBackendSettings.setSampleCoverage(mSampleCoverage);
                return true;
        }
        return super.handleAction(action);
    }

    /**
     * Set new based on other.
     *
     * @param other The other setting.
     */
    public void set(SampleCoverageSetting other) {
        set(other.mSampleCoverage);
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
    public void inherit(SampleCoverageSetting other) {
        inherit(other.mSampleCoverage);
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
        return mSampleCoverage;
    }

    /**
     * Set value.
     *
     * @param value The value.
     */
    private void setValue(boolean value) {
        mSampleCoverage = value;
        addAction(mDirty);
    }
}

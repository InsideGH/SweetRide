package com.sweetlab.sweetride.node.rendersettings;

import com.sweetlab.sweetride.action.Action;
import com.sweetlab.sweetride.action.ActionThread;
import com.sweetlab.sweetride.context.BackendRenderSettings;

/**
 * Specific render setting.
 */
public class SampleAlphaToCoverageSetting extends BaseSetting {
    /**
     * Change action.
     */
    private final Action<RsActionId> mDirty = new Action<>(this, RsActionId.SAMPLE_ALPHA_TO_COVERAGE, ActionThread.MAIN);

    /**
     * Current value.
     */
    private boolean mSampleAlphaToCoverage;

    /**
     * Constructor.
     *
     * @param settings The backend render settings.
     */
    public SampleAlphaToCoverageSetting(BackendRenderSettings settings) {
        super(settings);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SampleAlphaToCoverageSetting that = (SampleAlphaToCoverageSetting) o;
        return mSampleAlphaToCoverage == that.mSampleAlphaToCoverage;
    }

    @Override
    public int hashCode() {
        return (mSampleAlphaToCoverage ? 1 : 0);
    }

    @Override
    public boolean handleAction(Action<RsActionId> action) {
        switch (action.getType()) {
            case SAMPLE_ALPHA_TO_COVERAGE:
                mBackendSettings.setSampleAlphaToCoverage(mSampleAlphaToCoverage);
                return true;
        }
        return super.handleAction(action);
    }

    /**
     * Set new based on other.
     *
     * @param other The other setting.
     */
    public void set(SampleAlphaToCoverageSetting other) {
        set(other.mSampleAlphaToCoverage);
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
    public void inherit(SampleAlphaToCoverageSetting other) {
        inherit(other.mSampleAlphaToCoverage);
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
        return mSampleAlphaToCoverage;
    }

    /**
     * Set value.
     *
     * @param value The value.
     */
    private void setValue(boolean value) {
        mSampleAlphaToCoverage = value;
        addAction(mDirty);
    }
}

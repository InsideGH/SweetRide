package com.sweetlab.sweetride.node.rendersettings;

import com.sweetlab.sweetride.action.NoHandleNotifier;
import com.sweetlab.sweetride.context.BackendRenderSettings;

/**
 * The base class for a render setting.
 */
public abstract class BaseSetting extends NoHandleNotifier<RsActionId> implements HistoryState {
    /**
     * Default initially.
     */
    protected History mHistory = History.DEFAULT;

    /**
     * The backend render settings.
     */
    protected BackendRenderSettings mBackendSettings;

    /**
     * Constructor.
     *
     * @param setting Backend render settings to modify.
     */
    public BaseSetting(BackendRenderSettings setting) {
        mBackendSettings = setting;
    }

    @Override
    public History getHistory() {
        return mHistory;
    }
}

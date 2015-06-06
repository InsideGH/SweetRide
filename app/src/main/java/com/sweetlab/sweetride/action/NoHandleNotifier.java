package com.sweetlab.sweetride.action;

import com.sweetlab.sweetride.context.BackendContext;

/**
 * A action notifier that doesn't handle any actions. If requested
 * to handle actions, exception will be thrown.
 */
public class NoHandleNotifier extends ActionNotifier {
    @Override
    public boolean handleAction(Action action) {
        throw new RuntimeException("Class did not provide main handleAction method for " + action);
    }

    @Override
    public boolean handleAction(BackendContext context, Action action) {
        throw new RuntimeException("Class did not provide gl handleAction method for " + action);
    }

    @Override
    protected void onActionAdded(Action action) {

    }
}

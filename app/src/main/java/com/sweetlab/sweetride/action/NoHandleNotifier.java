package com.sweetlab.sweetride.action;

import com.sweetlab.sweetride.context.BackendContext;

/**
 * A action notifier that doesn't handle any actions. If requested
 * to handle actions, exception will be thrown.
 */
public class NoHandleNotifier extends ActionNotifier {
    @Override
    public void handleAction(Action action) {
        throw new RuntimeException("wtf");
    }

    @Override
    public void handleAction(BackendContext context, Action action) {
        throw new RuntimeException("wtf");
    }
}

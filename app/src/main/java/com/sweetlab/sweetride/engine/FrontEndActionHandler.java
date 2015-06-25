package com.sweetlab.sweetride.engine;

import com.sweetlab.sweetride.action.Action;
import com.sweetlab.sweetride.action.ActionNotifier;
import com.sweetlab.sweetride.action.ActionThread;

import java.util.ArrayList;
import java.util.List;

/**
 * Front end action handler.
 */
public class FrontEndActionHandler {
    /**
     * List of actions used while handling actions.
     */
    private final List<Action> mActions = new ArrayList<>();

    /**
     * Handle all actions the notifier has.
     *
     * @param notifier The notifier to handle actions on.
     * @return True if any action was handled, false if no actions where found.
     */
    public boolean handleActions(ActionNotifier notifier) {
        boolean actionHandled = false;

        /**
         * Reusing action list (mActions).
         */
        synchronized (mActions) {
            int actionCount = notifier.getActionCount();
            for (int i = 0; i < actionCount; i++) {
                Action action = notifier.getAction(i);
                if (action.getHandleThread().equals(ActionThread.MAIN)) {
                    mActions.add(action);
                }
            }

            for (Action action : mActions) {
                if (action.handleAction()) {
                    action.remove();
                    actionHandled = true;
                }
            }
            mActions.clear();
        }

        return actionHandled;
    }
}

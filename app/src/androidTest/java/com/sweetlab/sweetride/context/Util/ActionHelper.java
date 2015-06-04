package com.sweetlab.sweetride.context.Util;

import com.sweetlab.sweetride.action.Action;
import com.sweetlab.sweetride.action.ActionNotifier;
import com.sweetlab.sweetride.action.HandleThread;
import com.sweetlab.sweetride.context.BackendContext;

import java.util.ArrayList;
import java.util.List;

public class ActionHelper {
    /**
     * Handle all actions that are required to be handled on main thread.
     *
     * @param notifier The action notifier.
     */
    public static void handleMainThreadActions(ActionNotifier notifier) {
        List<Action> mainThreadActions = collectionActions(notifier, HandleThread.MAIN);
        /**
         * Handle the collected actions and then remove them.
         */
        for (Action action : mainThreadActions) {
            action.handleAction();
            action.remove();
        }
    }

    /**
     * Handle all actions that are required to be handled on gl thread.
     *
     * @param notifier The action notifier.
     */
    public static void handleGLThreadActions(ActionNotifier notifier, BackendContext context) {
        List<Action> glThreadActions = collectionActions(notifier, HandleThread.GL);
        /**
         * Handle the collected actions and then remove them.
         */
        for (Action action : glThreadActions) {
            action.handleAction(context);
            action.remove();
        }
    }

    private static List<Action> collectionActions(ActionNotifier notifier, HandleThread thread) {
        List<Action> list = new ArrayList<>();
        int actionCount = notifier.getActionCount();
        for (int i = 0; i < actionCount; i++) {
            Action action = notifier.getAction(i);
            if (action.getHandleThread().equals(thread)) {
                list.add(action);
            }
        }
        return list;
    }
}

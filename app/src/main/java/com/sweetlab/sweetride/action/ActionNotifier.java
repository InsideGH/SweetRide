package com.sweetlab.sweetride.action;

import java.util.ArrayList;
import java.util.List;

/**
 * Action notification handling in a graph of connected action notifiers.
 * Notifiers can be connected or disconnected from the parent (which is a notifier as well).
 * During connect and disconnect, any pending actions are handled (added or removed).
 * Actions can be added or removed which is then reflected to the top of the graph.
 */
public class ActionNotifier {
    /**
     * List of pending actions.
     */
    List<Action> mActions = new ArrayList<>();

    /**
     * List of parents.
     */
    List<ActionNotifier> mParents = new ArrayList<>();

    /**
     * Connect the provided notifier to notifier. Any pending actions in the notifier that is
     * connected will be reported to be added.
     *
     * @param notifier The notifier to connect.
     */
    public void connectNotifier(ActionNotifier notifier) {
        notifier.mParents.add(this);
        if (notifier.hasActions()) {
            int actionCount = notifier.getActionCount();
            for (int i = 0; i < actionCount; i++) {
                addAction(notifier.getAction(i));
            }
        }
    }

    /**
     * Disconnect the provided notifier. Any pending actions in the notifiers that is disconnected
     * will be reported to be removed.
     *
     * @param notifier The notifier to disconnect.
     */
    public void disconnectNotifier(ActionNotifier notifier) {
        notifier.mParents.remove(this);
        if (notifier.hasActions()) {
            int actionCount = notifier.getActionCount();
            for (int i = 0; i < actionCount; i++) {
                removeAction(notifier.getAction(i));
            }
        }
    }

    /**
     * Add action to pending actions. If action already exists it is moved to last.
     *
     * @param action The action.
     */
    public void addAction(Action action) {
        mActions.remove(action);
        mActions.add(action);
        reportAddAction(action);
    }

    /**
     * Remove action from pending actions.
     *
     * @param action The action to remove.
     */
    public void removeAction(Action action) {
        mActions.remove(action);
        reportRemoveAction(action);
    }

    /**
     * Get number of pending actions.
     *
     * @return Number of pending actions.
     */
    public int getActionCount() {
        return mActions.size();
    }

    /**
     * Get the action at index.
     *
     * @param index The index to fetch action from.
     * @return The action
     */
    public Action getAction(int index) {
        return mActions.get(index);
    }

    /**
     * Check if there are any pending actions.
     *
     * @return True if there are pending actions.
     */
    public boolean hasActions() {
        return !mActions.isEmpty();
    }

    /**
     * Report action add to all parents.
     *
     * @param action The action to add.
     */
    private void reportAddAction(Action action) {
        for (ActionNotifier parent : mParents) {
            parent.addAction(action);
        }
    }

    /**
     * Report action remove to all parents.
     *
     * @param action Action to remove.
     */
    private void reportRemoveAction(Action action) {
        for (ActionNotifier parent : mParents) {
            parent.removeAction(action);
        }
    }
}

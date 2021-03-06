package com.sweetlab.sweetride.action;

import com.sweetlab.sweetride.context.BackendContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Action notification handling in a graph of connected action notifiers.
 * Notifiers can be connected or disconnected from the parent (which is a notifier as well).
 * During connect and disconnect, any pending actions are added/removed.
 * Actions that are added/removed are reflected to the top of the graph.
 *
 * @param <T> Type of action.
 */
public abstract class ActionNotifier<T> {
    /**
     * List of pending actions.
     */
    private final List<Action<T>> mActions = new ArrayList<>();

    /**
     * List of parents.
     */
    private final List<ActionNotifier<T>> mParents = new ArrayList<>();

    /**
     * Connect the provided notifier. Any pending actions in the notifier that is
     * connected will be reported/added to parents.
     *
     * @param notifier The notifier to connect.
     */
    public void connectNotifier(ActionNotifier<T> notifier) {
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
     * will be reported/removed from parents.
     *
     * @param notifier The notifier to disconnect.
     */
    public void disconnectNotifier(ActionNotifier<T> notifier) {
        notifier.mParents.remove(this);
        if (notifier.hasActions()) {
            int actionCount = notifier.getActionCount();
            for (int i = 0; i < actionCount; i++) {
                removeAction(notifier.getAction(i));
            }
        }
    }

    /**
     * Add action to pending actions. If action already exists it is moved to last position.
     *
     * @param action The action to add.
     */
    public void addAction(Action<T> action) {
        mActions.remove(action);
        mActions.add(action);
        onActionAdded(action);
        reportAddAction(action);
    }

    /**
     * Remove action from pending actions.
     *
     * @param action The action to remove.
     */
    public void removeAction(Action<T> action) {
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
    public Action<T> getAction(int index) {
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
     * Handle action on the main thread. The action handle thread method tells if this
     * or the other handleAction method should be called.
     *
     * @param action Action to handle.
     * @return True if handled.
     */
    public abstract boolean handleAction(Action<T> action);

    /**
     * Handle the action on the GL thread. The action handle thread method tells if this
     * or the other handleAction method should be called.
     *
     * @param context Backend context.
     * @param action  Action to handle.
     * @return True if handled.
     */
    public abstract boolean handleAction(BackendContext context, Action<T> action);

    /**
     * Called every time an action has been added.
     *
     * @param action The action that has been added.
     */
    protected abstract void onActionAdded(Action<T> action);

    /**
     * Report action add to all parents all the way to top.
     *
     * @param action The action to add.
     */
    private void reportAddAction(Action<T> action) {
        for (ActionNotifier<T> parent : mParents) {
            parent.addAction(action);
        }
    }

    /**
     * Report action remove to all parents all the way to top.
     *
     * @param action Action to remove.
     */
    private void reportRemoveAction(Action<T> action) {
        for (ActionNotifier<T> parent : mParents) {
            parent.removeAction(action);
        }
    }
}

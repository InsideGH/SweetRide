package com.sweetlab.sweetride.action;

import com.sweetlab.sweetride.context.BackendContext;

/**
 * An action consists of two parts.
 * 1. The source the action originates from.
 * 2. The type of action.
 */
public class Action {
    /**
     * The source this action originates from.
     */
    private final ActionNotifier mSource;

    /**
     * The action id.
     */
    private final ActionId mId;

    /**
     * Which thread to handle the action on.
     */
    private final HandleThread mHandleThread;

    /**
     * Constructor.
     *
     * @param source     The source this action originates from.
     * @param id         The action id.
     * @param handleType The thread to handle the action on.
     */
    public Action(ActionNotifier source, ActionId id, HandleThread handleType) {
        mSource = source;
        mId = id;
        mHandleThread = handleType;
    }

    /**
     * Get the change source.
     *
     * @return The change source.
     */
    public ActionNotifier getSource() {
        return mSource;
    }

    /**
     * Get the change type.
     *
     * @return The type.
     */
    public ActionId getType() {
        return mId;
    }

    /**
     * Remove this action from the source it belong to.
     */
    public void remove() {
        mSource.removeAction(this);
    }

    /**
     * Get the action handle thread.
     *
     * @return The handle thread.
     */
    public HandleThread getHandleThread() {
        return mHandleThread;
    }

    /**
     * Handle the action on the main thread.
     */
    public void handleAction() {
        mSource.handleAction(this);
    }

    /**
     * Handle the action on the GL thread.
     *
     * @param context
     */
    public void handleAction(BackendContext context) {
        mSource.handleAction(context, this);
    }
}

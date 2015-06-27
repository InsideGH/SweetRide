package com.sweetlab.sweetride.action;

import com.sweetlab.sweetride.DebugOptions;
import com.sweetlab.sweetride.context.BackendContext;

/**
 * An action consists of two parts.
 * 1. The source the action originates from.
 * 2. The type of action.
 * 3. Which thread the action should execute on.
 *
 * @param <T> Type of action id.
 */
public class Action<T> {
    /**
     * The source this action originates from.
     */
    private final ActionNotifier<T> mSource;

    /**
     * The action id.
     */
    private final T mId;

    /**
     * Which thread to handle the action on.
     */
    private final ActionThread mActionThread;

    /**
     * Constructor.
     *
     * @param source     The source this action originates from.
     * @param id         The action id.
     * @param handleType The thread to handle the action on.
     */
    public Action(ActionNotifier<T> source, T id, ActionThread handleType) {
        mSource = source;
        mId = id;
        mActionThread = handleType;
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
    public T getType() {
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
    public ActionThread getHandleThread() {
        return mActionThread;
    }

    /**
     * Handle the action on the main thread.
     *
     * @return True if action is handled.
     */
    public boolean handleAction() {
        if (DebugOptions.DEBUG_ACTION) {
            if (!mActionThread.equals(ActionThread.MAIN)) {
                throw new RuntimeException("Trying to handle a gl action using main handle method");
            }
        }
        if (!mSource.handleAction(this)) {
            throw new RuntimeException("Unhandled main action detected " + this);
        }
        return true;
    }

    /**
     * Handle the action on the GL thread.
     *
     * @param context Backend context.
     */
    public boolean handleAction(BackendContext context) {
        if (DebugOptions.DEBUG_ACTION) {
            if (!mActionThread.equals(ActionThread.GL)) {
                throw new RuntimeException("Trying to handle a main action using backend handle method");
            }
        }
        if (!mSource.handleAction(context, this)) {
            throw new RuntimeException("Unhandled gl action detected " + this);
        }
        return true;
    }

    @Override
    public String toString() {
        return " id = " + mId.toString() + " source = " + mSource + " thread = " + mActionThread;
    }
}

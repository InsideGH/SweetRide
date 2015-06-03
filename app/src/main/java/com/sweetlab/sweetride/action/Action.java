package com.sweetlab.sweetride.action;

/**
 * An action consists of two parts.
 * 1. The source the action originates from.
 * 2. The type of action.
 */
public class Action {
    /**
     * The source this action originates from.
     */
    private ActionNotifier mSource;

    /**
     * The type of action.
     */
    private ActionType mType;

    /**
     * Constructor.
     *
     * @param source The source this action originates from.
     * @param type   The type of action.
     */
    public Action(ActionNotifier source, ActionType type) {
        mSource = source;
        mType = type;
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
    public ActionType getType() {
        return mType;
    }

    /**
     * Remove this action from the source it belong to.
     */
    public void remove() {
        mSource.removeAction(this);
    }
}

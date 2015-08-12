package com.sweetlab.sweetride.game.state;

/**
 * A state.
 */
public interface State<T> {
    /**
     * Called when the state is entered.
     *
     * @param owner The state owner.
     */
    void enter(T owner);

    /**
     * Called when the state is executed.
     *
     * @param owner The state owner.
     * @param dt    delta time since last update.
     */
    void execute(T owner, float dt);

    /**
     * Called when the state is leaved.
     *
     * @param owner The state owner.
     */
    void exit(T owner);
}

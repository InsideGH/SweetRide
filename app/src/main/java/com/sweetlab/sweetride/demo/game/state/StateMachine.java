package com.sweetlab.sweetride.demo.game.state;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * @param <T> The owner.
 */
public class StateMachine<T> {
    /**
     * The state and state machine owner.
     */
    private T mOwner;

    /**
     * The current state.
     */
    private State<T> mCurrentState;

    /**
     * The global state.
     */
    private State<T> mGlobalState;

    /**
     * Constructor.
     *
     * @param owner The state machine owner.
     */
    public StateMachine(@NonNull T owner) {
        mOwner = owner;
    }

    /**
     * Set the current state.
     *
     * @param currentState The current state.
     */
    public void setCurrentState(State<T> currentState) {
        mCurrentState = currentState;
    }

    /**
     * Set the global state.
     *
     * @param globalState The global state.
     */
    public void setGlobalState(State<T> globalState) {
        mGlobalState = globalState;
    }

    /**
     * Update state machine. If global state exists it will be executed before the
     * current state.
     *
     * @param dt Delta time.
     */
    public void update(float dt) {
        if (mGlobalState != null) {
            mGlobalState.execute(mOwner, dt);
        }
        if (mCurrentState != null) {
            mCurrentState.execute(mOwner, dt);
        }
    }

    /**
     * Change state.
     *
     * @param newState The new state.
     */
    public void changeState(@NonNull State<T> newState) {
        if (mCurrentState != null) {
            mCurrentState.exit(mOwner);
        }
        mCurrentState = newState;
        mCurrentState.enter(mOwner);
    }

    /**
     * Get the state machine owner.
     *
     * @return The owner.
     */
    public T getOwner() {
        return mOwner;
    }

    /**
     * Get the current state.
     *
     * @return The current state.
     */
    @Nullable
    public State<T> getCurrentState() {
        return mCurrentState;
    }

    /**
     * Get the global state.
     *
     * @return The global state.
     */
    @Nullable
    public State<T> getGlobalState() {
        return mGlobalState;
    }
}

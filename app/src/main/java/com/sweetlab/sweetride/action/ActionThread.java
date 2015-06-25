package com.sweetlab.sweetride.action;

/**
 * The handle thread. Decides which thread an action should be executed on.
 */
public enum ActionThread {
    /**
     * The action must be handled on the main thread.
     */
    MAIN,

    /**
     * The action must be handled on the GL thread.
     */
    GL
}

package com.sweetlab.sweetride.action;

/**
 * The handle thread.
 */
public enum HandleThread {
    /**
     * The action must be handled on the main thread.
     */
    MAIN,

    /**
     * The action must be handled on the GL thread.
     */
    GL
}

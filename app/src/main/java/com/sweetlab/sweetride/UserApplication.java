package com.sweetlab.sweetride;

import com.sweetlab.sweetride.node.Node;

/**
 * A user application.
 */
public abstract class UserApplication {
    /**
     * Called when the engine is initialized.
     *
     * @param engineRoot The engine root node.
     * @param width      The surface height in pixels.
     * @param height     The surface width in pixels.
     */
    public abstract void onInitialized(Node engineRoot, int width, int height);

    /**
     * Called when surface is changed.
     *
     * @param width  The surface height in pixels.
     * @param height The surface width in pixels.
     */
    public abstract void onSurfaceChanged(int width, int height);

    /**
     * Called each frame with delta time in seconds.
     *
     * @param dt Delta time in seconds.
     */
    public abstract void onUpdate(float dt);
}

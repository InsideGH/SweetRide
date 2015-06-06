package com.sweetlab.sweetride.node;

/**
 * Node controller that is called during node updates.
 */
public interface NodeController {
    /**
     * Update. Return value decides if controller should be kept or removed.
     *
     * @param dt Delta time since last frame.
     * @return True if keep, false if finished.
     */
    boolean onUpdate(float dt);
}

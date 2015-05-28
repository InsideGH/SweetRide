package com.sweetlab.sweetride.resource;

import com.sweetlab.sweetride.context.BackendContext;

/**
 * A backend frame buffer resource.
 */
public interface FrameBufferResource extends Resource {
    /**
     * Create this frame buffer resource.
     *
     * @param context Backend context.
     */
    void create(BackendContext context);

    /**
     * True if this frame buffer has been created.
     *
     * @return True if created.
     */
    boolean isCreated();
}

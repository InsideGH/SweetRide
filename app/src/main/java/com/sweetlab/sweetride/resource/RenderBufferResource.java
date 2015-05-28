package com.sweetlab.sweetride.resource;

import com.sweetlab.sweetride.context.BackendContext;

/**
 * A backend render buffer resource.
 */
public interface RenderBufferResource extends Resource {
    /**
     * Considered an invalid render buffer id.
     */
    int INVALID_RENDER_BUFFER_ID = 0;

    /**
     * Create this render buffer resource.
     *
     * @param context Backend context.
     */
    void create(BackendContext context);

    /**
     * True if this render buffer has been created.
     *
     * @return True if created.
     */
    boolean isCreated();
}

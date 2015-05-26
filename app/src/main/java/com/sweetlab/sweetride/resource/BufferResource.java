package com.sweetlab.sweetride.resource;

import com.sweetlab.sweetride.context.BackendContext;

/**
 * A backend buffer resource.
 */
public interface BufferResource extends Resource {
    /**
     * Create this buffer resource.
     *
     * @param context Backend context.
     */
    void create(BackendContext context);
}

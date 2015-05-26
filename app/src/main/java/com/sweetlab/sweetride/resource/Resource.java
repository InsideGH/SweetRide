package com.sweetlab.sweetride.resource;

import com.sweetlab.sweetride.context.BackendContext;

/**
 * A backend resource.
 */
public interface Resource {
    /**
     * Release the resource.
     */
    void release(BackendContext context);
}

package com.sweetlab.sweetride.resource;

import com.sweetlab.sweetride.context.BackendContext;

/**
 * A backend resource.
 */
public interface Resource {
    /**
     * Create this resource.
     *
     * @param context Backend context.
     */
    void create(BackendContext context);

    /**
     * True if been created.
     *
     * @return True if created.
     */
    boolean isCreated();

    /**
     * Release the resource.
     */
    void release(BackendContext context);

    /**
     * Get the generated name/id.
     *
     * @return The name/id.
     */
    int getId();
}

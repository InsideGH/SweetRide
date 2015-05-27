package com.sweetlab.sweetride.resource;

import com.sweetlab.sweetride.context.BackendContext;

import java.nio.Buffer;

/**
 * A backend buffer resource.
 */
public interface BufferResource extends Resource {
    /**
     * Invalid buffer id.
     */
    int INVALID_BUFFER_ID = -1;

    /**
     * Create this buffer resource.
     *
     * @param context Backend context.
     */
    void create(BackendContext context);

    /**
     * True if this data has been created.
     *
     * @return True if created.
     */
    boolean isCreated();

    /**
     * Get the generated buffer name/id.
     *
     * @return The buffer name/id.
     */
    int getBufferId();

    /**
     * Get the data.
     *
     * @return The data.
     */
    Buffer getData();

    /**
     * Get the total byte count of data.
     *
     * @return The total byte count.
     */
    int getTotalByteCount();

    /**
     * Get the buffer usage hint.
     *
     * @return The buffer usage hint.
     */
    int getBufferUsage();
}

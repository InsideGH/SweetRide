package com.sweetlab.sweetride.resource;

import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.context.BufferUsage;

import java.nio.Buffer;

/**
 * A backend buffer resource.
 */
public interface BufferResource extends Resource {
    /**
     * Get the buffer.
     *
     * @return The buffer.
     */
    Buffer getBuffer();

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
    BufferUsage getBufferUsage();

    /**
     * Load the buffer to gpu.
     */
    void load(BackendContext context);
}

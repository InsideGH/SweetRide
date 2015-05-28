package com.sweetlab.sweetride.resource;

import java.nio.Buffer;

/**
 * A backend buffer resource.
 */
public interface BufferResource extends Resource {
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

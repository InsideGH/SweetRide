package com.sweetlab.sweetride.attributedata;

import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.resource.BufferResource;

import java.nio.Buffer;

/**
 * A buffer resource to be used with shader program attributes.
 */
public interface AttributeData extends BufferResource {
    /**
     * Invalid buffer id.
     */
    int INVALID_BUFFER_ID = -1;

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

    /**
     * Create this data in the backend.
     */
    void create(BackendContext context);

    /**
     * True if this data has been created.
     *
     * @return True if created.
     */
    boolean isCreated();
}

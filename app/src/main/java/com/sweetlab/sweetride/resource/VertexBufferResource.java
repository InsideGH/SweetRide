package com.sweetlab.sweetride.resource;

import com.sweetlab.sweetride.attributedata.AttributePointer;

/**
 * A vertex buffer resource which is a buffer resource.
 */
public interface VertexBufferResource extends BufferResource {
    /**
     * Get number of attribute pointers.
     *
     * @return Number of attribute pointers.
     */
    int getAttributePointerCount();

    /**
     * Get attribute pointer at index.
     *
     * @param index The index to get from.
     * @return The attribute pointer.
     */
    AttributePointer getAttributePointer(int index);
}

package com.sweetlab.sweetride.resource;

import com.sweetlab.sweetride.action.GlobalActionId;
import com.sweetlab.sweetride.action.NoHandleNotifier;
import com.sweetlab.sweetride.attributedata.AttributePointer;

/**
 * A vertex buffer resource which is a buffer resource.
 */
public abstract class VertexBufferResource extends NoHandleNotifier<GlobalActionId> implements BufferResource {
    /**
     * Get number of attribute pointers.
     *
     * @return Number of attribute pointers.
     */
    public abstract int getAttributePointerCount();

    /**
     * Get attribute pointer at index.
     *
     * @param index The index to get from.
     * @return The attribute pointer.
     */
    public abstract AttributePointer getAttributePointer(int index);
}

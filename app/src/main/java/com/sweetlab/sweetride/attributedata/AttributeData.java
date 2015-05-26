package com.sweetlab.sweetride.attributedata;

import com.sweetlab.sweetride.resource.Resource;

import java.nio.Buffer;

/**
 * Represents attribute data resource.
 */
public interface AttributeData extends Resource {
    /**
     * Invalid buffer id.
     */
    int INVALID_BUFFER_ID = -1;

    /**
     * Get the name of the attribute.
     *
     * @return The name of the attribute.
     */
    String getName();

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
     * Get number of vertices.
     *
     * @return Number of vertices.
     */
    int getVertexCount();

    /**
     * Get the total byte count of data.
     *
     * @return The total byte count.
     */
    int getTotalByteCount();

    /**
     * Get the stride in bytes. Ths means the byte offset between consecutive vertices. 0 can be used in case data
     * is tightly packed.
     *
     * @return The stride.
     */
    int getStrideBytes();

    /**
     * Get the offset in bytes. This means the byte offset into data where to start. This is typically used in cases
     * of interleaved data.
     *
     * @return The byte offset.
     */
    int getOffsetBytes();

    /**
     * Get element count per vertex. For example, texture coordinates would mean a element count of 2 per vertex.
     *
     * @return The number of elements per vertex.
     */
    int getVertexSize();

    /**
     * Get number of bytes each vertex occupies.
     *
     * @return Byte size of a vertex.
     */
    int getVertexByteSize();

    /**
     * Get if data should be normalized or not.
     *
     * @return True if data should be normalized.
     */
    boolean getShouldNormalize();

    /**
     * Get the buffer usage hint.
     *
     * @return The buffer usage hint.
     */
    int getBufferUsage();

    /**
     * True if this data has been created.
     *
     * @return True if created.
     */
    boolean isCreated();
}

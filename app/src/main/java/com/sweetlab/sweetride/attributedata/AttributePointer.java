package com.sweetlab.sweetride.attributedata;

/**
 * Associated with attribute data, the pointer has additional information needed when using an
 * attribute in the shader program.
 */
public interface AttributePointer {
    /**
     * Get the name of the attribute in the shader.
     *
     * @return The name of the attribute.
     */
    String getName();
    
    /**
     * Get number of vertices in the data.
     *
     * @return Number of vertices.
     */
    int getVertexCount();

    /**
     * Get number of bytes each vertex occupies.
     *
     * @return Byte size of a vertex.
     */
    @SuppressWarnings("unused")
    int getVertexByteSize();

    /**
     * Get element count per vertex. For example, texture coordinates would mean a element count of 2 per vertex.
     *
     * @return The number of elements per vertex.
     */
    int getVertexSize();

    /**
     * Get the stride in bytes. Ths means the byte offset between consecutive vertices. 0 can be used in case data
     * is tightly packed.
     *
     * @return The stride.
     */
    int getStrideBytes();

    /**
     * Get if data should be normalized or not.
     *
     * @return True if data should be normalized.
     */
    boolean getShouldNormalize();

    /**
     * Get the offset in bytes. This means the byte offset into data where to start. This is typically used in cases
     * of interleaved data.
     *
     * @return The byte offset.
     */
    int getOffsetBytes();
}

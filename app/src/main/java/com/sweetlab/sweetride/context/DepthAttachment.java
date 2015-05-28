package com.sweetlab.sweetride.context;

/**
 * Depth attachment to a frame buffer.
 */
public interface DepthAttachment {
    /**
     * Get the texture type.
     *
     * @return The texture type.
     */
    int getType();

    /**
     * Get the generated name/id.
     *
     * @return The name/id.
     */
    int getId();
}

package com.sweetlab.sweetride.context;

/**
 * Color attachment to a frame buffer.
 */
public interface ColorAttachment {
    /**
     * Get the texture type.
     *
     * @return The texture type.
     */
    AttachmentType getAttachmentType();

    /**
     * Get the generated name/id.
     *
     * @return The name/id.
     */
    int getId();
}

package com.sweetlab.sweetride.resource;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;

import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.context.ColorAttachment;
import com.sweetlab.sweetride.context.DepthAttachment;

/**
 * A backend texture resource. Can also be used as an frame buffer color OR depth frame buffer
 * attachment.
 */
public interface TextureResource extends Resource, ColorAttachment, DepthAttachment {
    /**
     * Invalid texture id.
     */
    int INVALID_TEXTURE_ID = -1;

    /**
     * Get the shader uniform sampler name.
     *
     * @return The shader uniform sampler name.
     */
    String getName();

    /**
     * Create this texture resource.
     *
     * @param context Backend context.
     */
    void create(BackendContext context);

    /**
     * True if this texture has been created.
     *
     * @return True if created.
     */
    boolean isCreated();

    /**
     * Get the width of the texture.
     *
     * @return The width in pixels.
     */
    int getWidth();

    /**
     * Get the height of the texture.
     *
     * @return The height in pixels.
     */
    int getHeight();

    /**
     * Get the data. Null is permitted.
     *
     * @return The data, or null.
     */
    @Nullable
    Bitmap getData();

    /**
     * Get the GL format.
     *
     * @return The GL format.
     */
    int getTexelFormat();

    /**
     * Get the GL texel type.
     *
     * @return The type.
     */
    int getTexelType();

    /**
     * Get the texture type.
     *
     * @return The texture type.
     */
    int getType();
}

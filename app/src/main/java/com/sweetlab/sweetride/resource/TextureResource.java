package com.sweetlab.sweetride.resource;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;

import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.context.ColorAttachment;
import com.sweetlab.sweetride.context.DepthAttachment;
import com.sweetlab.sweetride.context.MagFilter;
import com.sweetlab.sweetride.context.MinFilter;
import com.sweetlab.sweetride.context.TexelFormat;
import com.sweetlab.sweetride.context.TexelType;
import com.sweetlab.sweetride.context.TextureType;

/**
 * A backend texture resource. Can also be used as an frame buffer color OR depth frame buffer
 * attachment.
 */
public interface TextureResource extends Resource, ColorAttachment, DepthAttachment {
    /**
     * Load the texture to gpu.
     *
     * @param context Backend context.
     */
    void load(BackendContext context);

    /**
     * Get the shader uniform sampler name.
     *
     * @return The shader uniform sampler name.
     */
    String getName();

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
    TexelFormat getTexelFormat();

    /**
     * Get the GL texel type.
     *
     * @return The type.
     */
    TexelType getTexelType();

    /**
     * Get the texture type.
     *
     * @return The texture type.
     */
    TextureType getTextureType();

    /**
     * Get the min filter.
     *
     * @return The min filter.
     */
    MinFilter getMinFilter();

    /**
     * Get the mag filter.
     *
     * @return The mag filter.
     */
    MagFilter getMagFilter();
}

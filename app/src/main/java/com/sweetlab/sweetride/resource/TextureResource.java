package com.sweetlab.sweetride.resource;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;

import com.sweetlab.sweetride.action.ActionNotifier;
import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.context.ColorAttachment;
import com.sweetlab.sweetride.context.DepthAttachment;
import com.sweetlab.sweetride.context.TexelFormat;
import com.sweetlab.sweetride.context.TexelType;
import com.sweetlab.sweetride.context.TextureMagFilterParam;
import com.sweetlab.sweetride.context.TextureMinFilterParam;
import com.sweetlab.sweetride.context.TextureType;

/**
 * A backend texture resource. Can also be used as an frame buffer color OR depth frame buffer
 * attachment.
 */
public abstract class TextureResource extends ActionNotifier implements Resource, ColorAttachment, DepthAttachment {
    /**
     * The default min filter.
     */
    protected TextureMinFilterParam DEFAULT_MIN_FILTER = TextureMinFilterParam.NEAREST_MIPMAP_LINEAR;

    /**
     * The default mag filter.
     */
    protected TextureMagFilterParam DEFAULT_MAG_FILTER = TextureMagFilterParam.LINEAR;

    /**
     * Load the texture to gpu.
     *
     * @param context Backend context.
     */
    public abstract void load(BackendContext context);

    /**
     * Get the shader uniform sampler name.
     *
     * @return The shader uniform sampler name.
     */
    public abstract String getName();

    /**
     * Get the width of the texture.
     *
     * @return The width in pixels.
     */
    public abstract int getWidth();

    /**
     * Get the height of the texture.
     *
     * @return The height in pixels.
     */
    public abstract int getHeight();

    /**
     * Get the data. Null is permitted.
     *
     * @return The data, or null.
     */
    @Nullable
    public abstract Bitmap getData();

    /**
     * Get the GL format.
     *
     * @return The GL format.
     */
    public abstract TexelFormat getTexelFormat();

    /**
     * Get the GL texel type.
     *
     * @return The type.
     */
    public abstract TexelType getTexelType();

    /**
     * Get the texture type.
     *
     * @return The texture type.
     */
    public abstract TextureType getTextureType();

    /**
     * Set the min and mag filters.
     *
     * @param minFilter The min filter
     * @param magFilter The mag filter
     */
    public abstract void setFilter(TextureMinFilterParam minFilter, TextureMagFilterParam magFilter);

    /**
     * Get the min filter.
     *
     * @return The min filter.
     */
    public abstract TextureMinFilterParam getMinFilter();

    /**
     * Get the mag filter.
     *
     * @return The mag filter.
     */
    public abstract TextureMagFilterParam getMagFilter();
}

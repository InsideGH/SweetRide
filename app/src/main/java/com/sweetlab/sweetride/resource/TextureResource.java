package com.sweetlab.sweetride.resource;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.support.annotation.Nullable;

import com.sweetlab.sweetride.context.ColorAttachment;
import com.sweetlab.sweetride.context.DepthAttachment;

/**
 * A backend texture resource. Can also be used as an frame buffer color OR depth frame buffer
 * attachment.
 */
public interface TextureResource extends Resource, ColorAttachment, DepthAttachment {
    /**
     * The default min filter.
     */
    int DEFAULT_MIN_FILTER = GLES20.GL_NEAREST_MIPMAP_LINEAR;

    /**
     * The default mag filter.
     */
    int DEFAULT_MAG_FILTER = GLES20.GL_LINEAR;

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

    /**
     * Set the min and mag filters.
     *
     * @param minFilter The min filter, for example GLES20.GL_NEAREST.
     * @param magFilter The mag filter, for example GLES20.GL_NEAREST.
     */
    void setFilter(int minFilter, int magFilter);

    /**
     * Get the min filter.
     *
     * @return The min filter.
     */
    int getMinFilter();

    /**
     * Get the mag filter.
     *
     * @return The mag filter.
     */
    int getMagFilter();
}

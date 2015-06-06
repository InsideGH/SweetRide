package com.sweetlab.sweetride.resource;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;

import com.sweetlab.sweetride.action.Action;
import com.sweetlab.sweetride.action.ActionId;
import com.sweetlab.sweetride.action.HandleThread;
import com.sweetlab.sweetride.action.NoHandleNotifier;
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
public abstract class TextureResource extends NoHandleNotifier implements Resource, ColorAttachment, DepthAttachment {
    /**
     * Constructor.
     */
    public TextureResource() {
        addAction(new Action(this, ActionId.TEXTURE_CREATE, HandleThread.GL));
        addAction(new Action(this, ActionId.TEXTURE_LOAD, HandleThread.GL));
    }

    @Override
    public boolean handleAction(BackendContext context, Action action) {
        switch (action.getType()) {
            case TEXTURE_CREATE:
                create(context);
                return true;
            case TEXTURE_LOAD:
                load(context);
                return true;
            default:
                return false;
        }
    }

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
     * Get the min filter.
     *
     * @return The min filter.
     */
    public abstract MinFilter getMinFilter();

    /**
     * Get the mag filter.
     *
     * @return The mag filter.
     */
    public abstract MagFilter getMagFilter();
}

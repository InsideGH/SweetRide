package com.sweetlab.sweetride.texture;

import android.graphics.Bitmap;

import com.sweetlab.sweetride.context.AttachmentType;
import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.context.MagFilter;
import com.sweetlab.sweetride.context.MinFilter;
import com.sweetlab.sweetride.context.ResourceManager;
import com.sweetlab.sweetride.context.TexelFormat;
import com.sweetlab.sweetride.context.TexelType;
import com.sweetlab.sweetride.context.TextureType;
import com.sweetlab.sweetride.context.TextureUnit2DTarget;
import com.sweetlab.sweetride.resource.TextureResource;

/**
 * An empty texture resource. It has format and type but no data.
 * This texture can be used as a destination to draw into using frame buffer.
 */
public class Empty2DTexture extends TextureResource {
    /**
     * The uniform sampler name in the shader program.
     */
    private final String mName;

    /**
     * The texture width boundary.
     */
    private final int mWidth;

    /**
     * The texture height boundary.
     */
    private final int mHeight;

    /**
     * The min filter.
     */
    private final MinFilter mMinFilter;

    /**
     * The mag filter.
     */
    private final MagFilter mMagFilter;

    /**
     * The generated texture name/id.
     */
    private int mTextureId = ResourceManager.INVALID_TEXTURE_ID;

    /**
     * Constructor.
     *
     * @param name      The uniform sampler name in the shader.
     * @param width     Width boundary.
     * @param height    Height boundary.
     * @param minFilter The min filter.
     * @param magFilter The mag filter.
     */
    public Empty2DTexture(String name, int width, int height, MinFilter minFilter, MagFilter magFilter) {
        mMinFilter = minFilter;
        mMagFilter = magFilter;
        mName = name;
        mWidth = width;
        mHeight = height;
    }

    @Override
    public void load(BackendContext context) {
        TextureUnit2DTarget texture2DTarget = context.getTextureUnitManager().getDefaultTextureUnit().getTexture2DTarget();
        texture2DTarget.load(this);
        texture2DTarget.setFilter(this, mMinFilter.getGlParam(), mMagFilter.getGlParam());
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public void create(BackendContext context) {
        mTextureId = context.getResourceManager().generateTexture();
    }

    @Override
    public boolean isCreated() {
        return mTextureId != ResourceManager.INVALID_TEXTURE_ID;
    }

    @Override
    public int getId() {
        return mTextureId;
    }

    @Override
    public int getWidth() {
        return mWidth;
    }

    @Override
    public int getHeight() {
        return mHeight;
    }

    @Override
    public Bitmap getData() {
        return null;
    }

    @Override
    public TexelFormat getTexelFormat() {
        return TexelFormat.RGB;
    }

    @Override
    public TexelType getTexelType() {
        return TexelType.UNSIGNED_SHORT_5_6_5;
    }

    @Override
    public TextureType getTextureType() {
        return TextureType.TEXTURE_2D;
    }

    @Override
    public AttachmentType getAttachmentType() {
        return AttachmentType.TEXTURE_2D;
    }

    @Override
    public MinFilter getMinFilter() {
        return mMinFilter;
    }

    @Override
    public MagFilter getMagFilter() {
        return mMagFilter;
    }

    @Override
    public void delete(BackendContext context) {
        context.getResourceManager().deleteTexture(mTextureId);
        mTextureId = ResourceManager.INVALID_TEXTURE_ID;
    }
}

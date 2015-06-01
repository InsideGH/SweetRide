package com.sweetlab.sweetride.texture;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.context.ResourceManager;
import com.sweetlab.sweetride.resource.TextureResource;

/**
 * An empty texture resource. It has format and type but no data.
 * This texture can be used as a destination to draw into using frame buffer.
 */
public class Empty2DTexture implements TextureResource {

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
     * The generated texture name/id.
     */
    private int mTextureId = ResourceManager.INVALID_TEXTURE_ID;

    /**
     * The min filter.
     */
    private int mMinFilter = DEFAULT_MIN_FILTER;

    /**
     * The mag filter.
     */
    private int mMagFilter = DEFAULT_MAG_FILTER;

    /**
     * Constructor.
     *
     * @param name   The uniform sampler name in the shader.
     * @param width  Width boundary.
     * @param height Height boundary.
     */
    public Empty2DTexture(String name, int width, int height) {
        mName = name;
        mWidth = width;
        mHeight = height;
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
    public int getTexelFormat() {
        return GLES20.GL_RGB;
    }

    @Override
    public int getTexelType() {
        return GLES20.GL_UNSIGNED_SHORT_5_6_5;
    }

    @Override
    public int getType() {
        return GLES20.GL_TEXTURE_2D;
    }

    @Override
    public void setFilter(int minFilter, int magFilter) {
        mMinFilter = minFilter;
        mMagFilter = magFilter;
    }

    @Override
    public int getMinFilter() {
        return mMinFilter;
    }

    @Override
    public int getMagFilter() {
        return mMagFilter;
    }

    @Override
    public void release(BackendContext context) {
        context.getResourceManager().deleteTexture(mTextureId);
        mTextureId = ResourceManager.INVALID_TEXTURE_ID;
    }
}

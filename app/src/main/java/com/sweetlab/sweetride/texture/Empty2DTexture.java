package com.sweetlab.sweetride.texture;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.sweetlab.sweetride.context.BackendContext;
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
    private int mBufferId;

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
        mBufferId = context.getResourceManager().generateTexture();
    }

    @Override
    public boolean isCreated() {
        return mBufferId != INVALID_TEXTURE_ID;
    }

    @Override
    public int getId() {
        return mBufferId;
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
    public void release(BackendContext context) {
        context.getResourceManager().deleteTexture(mBufferId);
    }
}

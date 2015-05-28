package com.sweetlab.sweetride.texture;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.sweetlab.sweetride.context.AndroidTextureHelper;
import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.context.ResourceManager;
import com.sweetlab.sweetride.resource.TextureResource;

/**
 * A 2D texture with bitmap as data source. The format,type, width and height is decided by the
 * bitmap.
 */
public class Texture2D implements TextureResource {
    /**
     * The name if the shader uniform sampler.
     */
    private final String mName;

    /**
     * The bitmap.
     */
    private final Bitmap mBitmap;

    /**
     * The texture id.
     */
    private int mTextureId;

    /**
     * Constructor.
     *
     * @param name   Shader uniform (sampler) name.
     * @param bitmap Bitmap.
     */
    public Texture2D(String name, Bitmap bitmap) {
        mName = name;
        mBitmap = bitmap;
    }

    @Override
    public void release(BackendContext context) {
        context.getResourceManager().deleteTexture(mTextureId);
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
        return mBitmap.getWidth();
    }

    @Override
    public int getHeight() {
        return mBitmap.getHeight();
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public Bitmap getData() {
        return mBitmap;
    }

    @Override
    public int getTexelFormat() {
        return AndroidTextureHelper.getTexelFormat(mBitmap);
    }

    @Override
    public int getTexelType() {
        return AndroidTextureHelper.getGLTexelType(mBitmap);
    }

    @Override
    public int getType() {
        return GLES20.GL_TEXTURE_2D;
    }
}

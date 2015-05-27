package com.sweetlab.sweetride.texture;

import android.graphics.Bitmap;

import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.resource.TextureResource;

/**
 * A 2D texture.
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
        return mTextureId != INVALID_TEXTURE_ID;
    }

    @Override
    public int getTextureId() {
        return mTextureId;
    }

    /**
     * Get the shader uniform sampler name.
     *
     * @return The shader uniform sampler name.
     */
    public String getName() {
        return mName;
    }

    /**
     * Get the bitmap.
     *
     * @return The bitmap.
     */
    public Bitmap getBitmap() {
        return mBitmap;
    }
}

package com.sweetlab.sweetride.texture;

import android.graphics.Bitmap;

import com.sweetlab.sweetride.action.Action;
import com.sweetlab.sweetride.action.ActionType;
import com.sweetlab.sweetride.context.AndroidTextureHelper;
import com.sweetlab.sweetride.context.AttachmentType;
import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.context.ResourceManager;
import com.sweetlab.sweetride.context.TexelFormat;
import com.sweetlab.sweetride.context.TexelType;
import com.sweetlab.sweetride.context.TextureMagFilterParam;
import com.sweetlab.sweetride.context.TextureMinFilterParam;
import com.sweetlab.sweetride.context.TextureType;
import com.sweetlab.sweetride.context.TextureUnit2DTarget;
import com.sweetlab.sweetride.resource.TextureResource;

/**
 * A 2D texture with bitmap as data source. The format,type, width and height is decided by the
 * bitmap.
 */
public class Texture2D extends TextureResource {
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
    private int mTextureId = ResourceManager.INVALID_TEXTURE_ID;

    /**
     * The min filter.
     */
    private TextureMinFilterParam mMinFilter = DEFAULT_MIN_FILTER;

    /**
     * The mag filter.
     */
    private TextureMagFilterParam mMagFilter = DEFAULT_MAG_FILTER;

    /**
     * Constructor.
     *
     * @param name   Shader uniform (sampler) name.
     * @param bitmap Bitmap.
     */
    public Texture2D(String name, Bitmap bitmap) {
        mName = name;
        mBitmap = bitmap;
        addAction(new Action(this, ActionType.CREATE));
        addAction(new Action(this, ActionType.LOAD));
    }

    @Override
    public void delete(BackendContext context) {
        context.getResourceManager().deleteTexture(mTextureId);
        mTextureId = ResourceManager.INVALID_TEXTURE_ID;
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
    public Bitmap getData() {
        return mBitmap;
    }

    @Override
    public TexelFormat getTexelFormat() {
        return AndroidTextureHelper.getTexelFormat(mBitmap);
    }

    @Override
    public TexelType getTexelType() {
        return AndroidTextureHelper.getGLTexelType(mBitmap);
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
    public void setFilter(TextureMinFilterParam minFilter, TextureMagFilterParam magFilter) {
        mMinFilter = minFilter;
        mMagFilter = magFilter;
    }

    @Override
    public TextureMinFilterParam getMinFilter() {
        return mMinFilter;
    }

    @Override
    public TextureMagFilterParam getMagFilter() {
        return mMagFilter;
    }
}

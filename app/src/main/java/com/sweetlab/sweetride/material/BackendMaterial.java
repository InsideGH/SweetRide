package com.sweetlab.sweetride.material;

import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.context.TextureUnit2DTarget;
import com.sweetlab.sweetride.resource.TextureResource;
import com.sweetlab.sweetride.shader.ShaderProgram;

import java.util.ArrayList;
import java.util.List;

/**
 * Backend material.
 */
public class BackendMaterial {
    /**
     * The shader program reference.
     */
    private ShaderProgram mShaderProgram;

    /**
     * The texture collection references.
     */
    private List<TextureResource> mTextures = new ArrayList<>();

    /**
     * Set the shader program.
     *
     * @param program The program.
     */
    public void setShaderProgram(ShaderProgram program) {
        mShaderProgram = program;
    }

    /**
     * Set the textures.
     *
     * @param list The textures.
     */
    public void setTextures(List<TextureResource> list) {
        mTextures.clear();
        mTextures.addAll(list);
    }

    /**
     * Create the material.
     *
     * @param context The backend context.
     */
    public void create(BackendContext context) {
        if (!mShaderProgram.isCreated()) {
            mShaderProgram.create(context);
        }
        for (TextureResource resource : mTextures) {
            if (!resource.isCreated()) {
                resource.create(context);
            }
        }
    }

    /**
     * Load material to gpu. Filters will be set as well.
     *
     * @param context Backend context.
     */
    public void load(BackendContext context) {
        TextureUnit2DTarget target = context.getTextureUnitManager().getDefaultTextureUnit().getTexture2DTarget();
        for (TextureResource texture : mTextures) {
            target.load(texture);
            target.setFilter(texture, texture.getMinFilter().getGlParam(), texture.getMagFilter().getGlParam());
        }
    }

    /**
     * Get the shader program.
     *
     * @return The program.
     */
    public ShaderProgram getShaderProgram() {
        return mShaderProgram;
    }

    /**
     * Get number of textures.
     *
     * @return Number of textures.
     */
    public int getTextureCount() {
        return mTextures.size();
    }

    /**
     * Get the texture at index.
     *
     * @param index Index to fetch from.
     * @return The texture.
     */
    public TextureResource getTexture(int index) {
        return mTextures.get(index);
    }
}

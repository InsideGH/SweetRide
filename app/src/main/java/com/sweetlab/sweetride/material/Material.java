package com.sweetlab.sweetride.material;

import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.resource.TextureResource;
import com.sweetlab.sweetride.shader.ShaderProgram;

import java.util.ArrayList;
import java.util.List;

/**
 * Material is an abstraction that contains a shader program and textures.
 */
public class Material {
    /**
     * List of textures.
     */
    private final List<TextureResource> mTextures = new ArrayList<>();

    /**
     * The shader program.
     */
    private ShaderProgram mShaderProgram;

    public void setShaderProgram(ShaderProgram program) {
        mShaderProgram = program;
    }

    /**
     * Add textures.
     *
     * @param texture Texture.
     */
    public void addTexture(TextureResource texture) {
        mTextures.add(texture);
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
}

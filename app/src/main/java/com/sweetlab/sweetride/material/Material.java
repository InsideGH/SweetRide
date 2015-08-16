package com.sweetlab.sweetride.material;

import com.sweetlab.sweetride.action.Action;
import com.sweetlab.sweetride.action.ActionThread;
import com.sweetlab.sweetride.action.GlobalActionId;
import com.sweetlab.sweetride.action.NoHandleNotifier;
import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.resource.TextureResource;
import com.sweetlab.sweetride.shader.ShaderProgram;

import java.util.ArrayList;
import java.util.List;

/**
 * Material is an abstraction that contains a shader program and textures.
 */
public class Material extends NoHandleNotifier<GlobalActionId> {
    /**
     * Shader program reference has changed.
     */
    private final Action<GlobalActionId> mProgramChange = new Action<>(this, GlobalActionId.MATERIAL_PROGRAM, ActionThread.MAIN);

    /**
     * Texture collection has changed.
     */
    private final Action<GlobalActionId> mTextureChange = new Action<>(this, GlobalActionId.MATERIAL_TEXTURES, ActionThread.MAIN);

    /**
     * List of textures.
     */
    private final List<TextureResource> mTextures = new ArrayList<>();

    /**
     * The backend material.
     */
    private final BackendMaterial mBackendMaterial = new BackendMaterial();

    /**
     * The shader program.
     */
    private ShaderProgram mShaderProgram;

    @Override
    public boolean handleAction(Action<GlobalActionId> action) {
        switch (action.getType()) {
            case MATERIAL_PROGRAM:
                mBackendMaterial.setShaderProgram(mShaderProgram);
                return true;
            case MATERIAL_TEXTURES:
                mBackendMaterial.setTextures(mTextures);
                return true;
            default:
                return false;
        }
    }

    /**
     * Set shader program to use.
     *
     * @param program Shader program.
     */
    public void setShaderProgram(ShaderProgram program) {
        if (mShaderProgram != null) {
            disconnectNotifier(mShaderProgram);
        }
        mShaderProgram = program;
        connectNotifier(mShaderProgram);
        addAction(mProgramChange);
    }

    /**
     * Add textures.
     *
     * @param texture Texture.
     */
    public void addTexture(TextureResource texture) {
        mTextures.add(texture);
        connectNotifier(texture);
        addAction(mTextureChange);
    }

    /**
     * Remove texture.
     *
     * @param texture Texture.
     */
    public void removeTexture(TextureResource texture) {
        if (mTextures.remove(texture)) {
            disconnectNotifier(texture);
            addAction(mTextureChange);
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

    /**
     * Create the material.
     *
     * @param context The backend context.
     */
    public void create(BackendContext context) {
        mBackendMaterial.create(context);
    }

    /**
     * Load material to gpu. Filters will be set as well.
     *
     * @param context Backend context.
     */
    public void load(BackendContext context) {
        mBackendMaterial.load(context);
    }

    /**
     * Get the backend material.
     *
     * @return The backend material.
     */
    public BackendMaterial getBackendMaterial() {
        return mBackendMaterial;
    }
}

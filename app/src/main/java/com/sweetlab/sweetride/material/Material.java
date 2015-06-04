package com.sweetlab.sweetride.material;

import com.sweetlab.sweetride.action.Action;
import com.sweetlab.sweetride.action.ActionId;
import com.sweetlab.sweetride.action.ActionNotifier;
import com.sweetlab.sweetride.action.HandleThread;
import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.context.TextureUnit2DTarget;
import com.sweetlab.sweetride.resource.TextureResource;
import com.sweetlab.sweetride.shader.ShaderProgram;

import java.util.ArrayList;
import java.util.List;

/**
 * Material is an abstraction that contains a shader program and textures.
 */
public class Material extends ActionNotifier {
    /**
     * Shader program reference has changed.
     */
    private final Action mProgramChange = new Action(this, ActionId.MATERIAL_PROGRAM, HandleThread.MAIN);

    /**
     * Texture collection has changed.
     */
    private final Action mTextureChange = new Action(this, ActionId.MATERIAL_TEXTURES, HandleThread.MAIN);

    /**
     * List of textures.
     */
    private final List<TextureResource> mTextures = new ArrayList<>();

    /**
     * The shader program.
     */
    private ShaderProgram mShaderProgram;

    /**
     * The shader program reference used by GL thread.
     */
    private ShaderProgram mShaderProgramGL;

    /**
     * The texture collection reference used by GL thread.
     */
    private List<TextureResource> mTexturesGL = new ArrayList<>();

    @Override
    public void handleAction(Action action) {
        switch (action.getType()) {
            case MATERIAL_PROGRAM:
                mShaderProgramGL = mShaderProgram;
                break;
            case MATERIAL_TEXTURES:
                mTexturesGL.clear();
                mTexturesGL.addAll(mTextures);
                break;
            default:
                throw new RuntimeException("wtf");
        }
    }

    @Override
    public void handleAction(BackendContext context, Action action) {
        throw new RuntimeException("wtf");
    }

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
        if (!mShaderProgramGL.isCreated()) {
            mShaderProgramGL.create(context);
        }
        for (TextureResource resource : mTexturesGL) {
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
        for (TextureResource texture : mTexturesGL) {
            target.load(texture);
            target.setFilter(texture, texture.getMinFilter().getGlParam(), texture.getMagFilter().getGlParam());
        }
    }
}

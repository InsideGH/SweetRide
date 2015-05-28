package com.sweetlab.sweetride.context;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.sweetlab.sweetride.resource.TextureResource;
import com.sweetlab.sweetride.shader.ShaderProgram;
import com.sweetlab.sweetride.shader.Uniform;
import com.sweetlab.sweetride.util.Util;

import java.nio.Buffer;

/**
 * The texture 2D target of a specific texture unit.
 */
public class TextureUnit2DTarget {
    /**
     * The texture 2D target.
     */
    private static final int TARGET = GLES20.GL_TEXTURE_2D;

    /**
     * The texture 2D binding.
     */
    private static final int BINDING = GLES20.GL_TEXTURE_BINDING_2D;

    /**
     * GL read buffer.
     */
    private final int[] mReadBuffer = new int[4];

    /**
     * The backend context.
     */
    private final BackendContext mContext;

    /**
     * The GL texture unit number of this target. This variable is used in various GL commands.
     */
    private final int mGLUnitNr;

    /**
     * Zero based texture unit number of this target. This variable is used to write the
     * texture unit number into the shader program uniform sampler.
     */
    private int[] mZeroBasedNr = new int[1];

    /**
     * Constructor.
     *
     * @param context     Backend context.
     * @param zeroBasedNr Zero based number 0 + (0, 1, 2, 3 ... etc).
     * @param glBasedNr   GL based texture unit number, GL_TEXTURE0 + (0, 1, 2, 3 ... etc)
     */
    public TextureUnit2DTarget(BackendContext context, int zeroBasedNr, int glBasedNr) {
        mContext = context;
        mZeroBasedNr[0] = zeroBasedNr;
        mGLUnitNr = glBasedNr;
    }

    /**
     * Load the texture resource to gpu.
     *
     * @param texture Texture to load.
     */
    public void load(TextureResource texture) {
        if (!isUnitActive()) {
            GLES20.glActiveTexture(mGLUnitNr);
        }

        int textureId = texture.getId();
        if (!isTextureBoundToTarget(textureId)) {
            GLES20.glBindTexture(TARGET, textureId);
        }

        texImage2D(TARGET, texture, 0);

        GLES20.glBindTexture(TARGET, 0);
    }

    /**
     * Set filtering parameters.
     *
     * @param texture Texture to set parameters to.
     * @param min     Min filter parameter.
     * @param mag     Mag filter parameter.
     */
    public void setFilter(TextureResource texture, int min, int mag) {
        if (!isUnitActive()) {
            GLES20.glActiveTexture(mGLUnitNr);
        }

        int textureId = texture.getId();
        if (!isTextureBoundToTarget(textureId)) {
            GLES20.glBindTexture(TARGET, textureId);
        }

        GLES20.glTexParameteri(TARGET, GLES20.GL_TEXTURE_MIN_FILTER, min);
        GLES20.glTexParameteri(TARGET, GLES20.GL_TEXTURE_MAG_FILTER, mag);

        GLES20.glBindTexture(TARGET, 0);
    }

    /**
     * Enable this texture in the shader program.
     *
     * @param program Shader program.
     * @param texture Texture.
     */
    public void enable(ShaderProgram program, TextureResource texture) {
        if (mContext.getState().readActiveProgram() != program.getId()) {
            throw new RuntimeException("Provided program during texture enable is not the currently active");
        }

        String name = texture.getName();
        Uniform uniform = program.getUniform(name);
        if (uniform != null) {
            if (!isUnitActive()) {
                GLES20.glActiveTexture(mGLUnitNr);
            }

            int textureId = texture.getId();
            if (!isTextureBoundToTarget(textureId)) {
                GLES20.glBindTexture(TARGET, textureId);
            }

            mContext.getUniformWriter().writeInt(program, name, mZeroBasedNr);
        }
    }

    /**
     * Disable the texture from this target.
     */
    public void disable(TextureResource texture) {
        if (!isUnitActive()) {
            GLES20.glActiveTexture(mGLUnitNr);
        }
        int textureId = texture.getId();
        if (isTextureBoundToTarget(textureId)) {
            GLES20.glBindTexture(TARGET, 0);
        }
    }

    /**
     * Check if unit this target belongs to is active.
     *
     * @return True if active.
     */
    private boolean isUnitActive() {
        return getActiveTextureUnit() == mGLUnitNr;
    }

    /**
     * Get active texture unit.
     *
     * @return The active texture unit.
     */
    private int getActiveTextureUnit() {
        GLES20.glGetIntegerv(GLES20.GL_ACTIVE_TEXTURE, mReadBuffer, 0);
        return mReadBuffer[0];
    }

    /**
     * Check if texture is bound to target.
     *
     * @param id Texture id.
     * @return True if texture is bound to target.
     */
    private boolean isTextureBoundToTarget(int id) {
        if (id > 0) {
            GLES20.glGetIntegerv(BINDING, mReadBuffer, 0);
            return mReadBuffer[0] == id;
        }
        return false;
    }

    /**
     * Push bitmap data to gpu. Can be used if the texture data is empty as well.
     *
     * @param target  The target texture of the active unit.
     * @param texture The texture resource.
     * @param level   Mipmap level, level 0 is base.
     */
    private void texImage2D(int target, TextureResource texture, int level) {
        final int texelFormat = texture.getTexelFormat();
        final int texelType = texture.getTexelType();
        final int width = texture.getWidth();
        final int height = texture.getHeight();

        Buffer rgbBuffer = null;
        Bitmap data = texture.getData();
        if (data != null) {
            rgbBuffer = Util.bitmapToBuffer(data);
        }

        GLES20.glTexImage2D(target, level, texelFormat, width, height, 0, texelFormat, texelType, rgbBuffer);
    }
}

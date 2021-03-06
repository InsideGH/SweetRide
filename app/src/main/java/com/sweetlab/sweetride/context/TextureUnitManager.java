package com.sweetlab.sweetride.context;

import android.opengl.GLES20;

import java.util.Stack;

/**
 * Manages the GL texture unit resources.
 */
public class TextureUnitManager {
    /**
     * Representation of available texture units.
     */
    private final Stack<TextureUnit> mStack = new Stack<>();

    /**
     * Max number of texture units available.
     */
    private final int mMaxNrTextureUnits;

    /**
     * The default texture unit.
     */
    private final TextureUnit mDefaultTextureUnit;

    /**
     * Constructor.
     *
     * @param context           Backend context.
     * @param maxNrTextureUnits Max number of texture units.
     */
    public TextureUnitManager(BackendContext context, int maxNrTextureUnits) {
        mMaxNrTextureUnits = maxNrTextureUnits;
        for (int i = mMaxNrTextureUnits - 1; i > 0; i--) {
            int glBasedNr = GLES20.GL_TEXTURE0 + i;
            mStack.push(new TextureUnit(context, i, glBasedNr));
        }

        mDefaultTextureUnit = new TextureUnit(context, 0, GLES20.GL_TEXTURE0);
    }

    /**
     * Get the default texture unit. No need to take and return this unit.
     *
     * @return The default texture unit.
     */
    public TextureUnit getDefaultTextureUnit() {
        return mDefaultTextureUnit;
    }

    /**
     * Take a texture unit from available texture units. Must be returned when finished.
     *
     * @return The taken texture unit.
     */
    public TextureUnit takeTextureUnit() {
        if (!mStack.isEmpty()) {
            return mStack.pop();
        }
        throw new RuntimeException("No more free texture units available");
    }

    /**
     * Return a texture unit to the available texture units. Must have been taken before.
     *
     * @param unit The texture unit to return.
     */
    public void returnTextureUnit(TextureUnit unit) {
        if (mStack.size() >= mMaxNrTextureUnits) {
            throw new RuntimeException("Trying to push back texture unit to full stack");
        }
        mStack.push(unit);
    }
}

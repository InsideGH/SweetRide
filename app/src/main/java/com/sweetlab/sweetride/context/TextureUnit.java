package com.sweetlab.sweetride.context;


/**
 * This is a representation of a GL texture unit.
 */
public class TextureUnit {
    /**
     * The zero based number, 0 + (0, 1, 2, 3 ... etc).
     */
    private final int mZeroBasedNr;
    /**
     * The GL based number, GL_TEXTURE0 + (0, 1, 2, 3 ... etc)
     */
    private final int mGLBasedNr;

    /**
     * This texture units texture 2d target.
     */
    private final TextureUnit2DTarget mTextureUnit2DTarget;

    /**
     * Constructor. Create texture unit given a zero based number and a GL based number.
     *
     * @param zeroBasedNr Zero based number 0 + (0, 1, 2, 3 ... etc).
     * @param glBasedNr   GL based number GL_TEXTURE0 + (0, 1, 2, 3 ... etc)
     */
    public TextureUnit(BackendContext context, int zeroBasedNr, int glBasedNr) {
        mZeroBasedNr = zeroBasedNr;
        mGLBasedNr = glBasedNr;
        mTextureUnit2DTarget = new TextureUnit2DTarget(context, zeroBasedNr, glBasedNr);
    }

    /**
     * Get the number of this texture unit.
     *
     * @return The zero based number, 0 + (0, 1, 2, 3 ... etc).
     */
    public int getZeroBasedNr() {
        return mZeroBasedNr;
    }

    /**
     * Get the number of this texture unit.
     *
     * @return The GL based number, GL_TEXTURE0 + (0, 1, 2, 3 ... etc).
     */
    public int getGLBasedNr() {
        return mGLBasedNr;
    }

    /**
     * Get the texture 2d target.
     *
     * @return The texture 2d target.
     */
    public TextureUnit2DTarget getTexture2DTarget() {
        return mTextureUnit2DTarget;
    }
}

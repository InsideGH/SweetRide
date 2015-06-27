package com.sweetlab.sweetride.node.rendersettings;

/**
 * Bitwise OR of masks that indicate the buffers to be cleared.
 */
public class ClearMask implements GLEnum {
    /**
     * The clear mask.
     */
    private int mGLMask;

    /**
     * Constructor. Set bits to 'clear mask'.
     *
     * @param bits Bits.
     */
    public ClearMask(ClearBit[] bits) {
        mGLMask = 0;
        for (ClearBit bit : bits) {
            mGLMask |= bit.getGL();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClearMask clearMask = (ClearMask) o;
        return mGLMask == clearMask.mGLMask;

    }

    @Override
    public int hashCode() {
        return mGLMask;
    }

    @Override
    public int getGL() {
        return mGLMask;
    }
}

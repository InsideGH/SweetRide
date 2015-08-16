package com.sweetlab.sweetride.demo.game.terrain.height;

/**
 * Height modifier that takes into account neighbours on the left, right, top and bottom side.
 */
public class PatchHeightModifier implements HeightModifier {
    /**
     * The patch to write data to.
     */
    private final PatchHeightData mDst;

    /**
     * The patch to the left of dst.
     */
    private final PatchHeightData mLeft;

    /**
     * The patch to the top of dst.
     */
    private final PatchHeightData mTop;

    /**
     * The patch to the right of dst.
     */
    private final PatchHeightData mRight;

    /**
     * The patch to the bottom of dst.
     */
    private final PatchHeightData mBottom;

    /**
     * Number of rows and columns in the patch.
     */
    private final int mPatchRowsCols;

    /**
     * The min height.
     */
    private float mMinHeight;

    /**
     * The max height.
     */
    private float mMaxHeight;

    /**
     * Constructor.
     *
     * @param dst    The patch to write data to.
     * @param left   The patch to the left of dst.
     * @param top    The patch to the top of dst.
     * @param right  The patch to the right of dst.
     * @param bottom The patch to the bottom of dst.
     */
    public PatchHeightModifier(PatchHeightData dst, PatchHeightData left, PatchHeightData top,
                               PatchHeightData right, PatchHeightData bottom) {
        mPatchRowsCols = (1 << dst.getSize());
        mDst = dst;
        mLeft = left;
        mTop = top;
        mRight = right;
        mBottom = bottom;
    }

    @Override
    public void set(int x, int z, float value) {
        mDst.set(x, z, value);
        setMinMax(value);
    }

    @Override
    public float get(int x, int z) {
        return mDst.get(x, z);
    }

    @Override
    public boolean isModifiable(int x, int z) {
        if (x == mPatchRowsCols && mLeft != null) {
            return false;
        } else if (x == 0 && mRight != null) {
            return false;
        } else if (z == 0 && mBottom != null) {
            return false;
        } else if (z == mPatchRowsCols && mTop != null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void copyUnModifiable(int x, int z) throws IllegalStateException {
        float value;
        if (x == mPatchRowsCols && mLeft != null) {
            value = mLeft.get(0, z);
            mDst.set(x, z, value);
        } else if (x == 0 && mRight != null) {
            value = mRight.get(mPatchRowsCols, z);
            mDst.set(x, z, value);
        } else if (z == 0 && mBottom != null) {
            value = mBottom.get(x, mPatchRowsCols);
            mDst.set(x, z, value);
        } else if (z == mPatchRowsCols && mTop != null) {
            value = mTop.get(x, 0);
            mDst.set(x, z, value);
        } else {
            throw new IllegalStateException("Cannot find unmodifiable value to copy");
        }
        setMinMax(value);
    }

    @Override
    public PatchHeightData getData() {
        return mDst;
    }

    @Override
    public float getMaxHeight() {
        return mMaxHeight;
    }

    @Override
    public float getMinHeight() {
        return mMinHeight;
    }

    /**
     * Set the min max value.
     *
     * @param value A new value.
     */
    private void setMinMax(float value) {
        if (value > mMaxHeight) {
            mMaxHeight = value;
        }
        if (value < mMinHeight) {
            mMinHeight = value;
        }
    }
}

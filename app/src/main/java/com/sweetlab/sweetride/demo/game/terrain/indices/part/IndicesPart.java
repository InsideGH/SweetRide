package com.sweetlab.sweetride.demo.game.terrain.indices.part;

/**
 * A piece of indices array for a specific part.
 */
public class IndicesPart {
    /**
     * The patch size.
     */
    private final int mPatchSize;

    /**
     * The center level.
     */
    private final int mCenterLevel;

    /**
     * The part level.
     */
    private final int mPartLevel;

    /**
     * The part in the patch this array belongs.
     */
    private final IndicesPartType mPartType;

    /**
     * The array data.
     */
    private final short[] mArray;

    /**
     * The identity.
     */
    private final int mHashCode;

    /**
     * Constructor.
     *
     * @param patchSize   The path size.
     * @param centerLevel The center level.
     * @param partLevel   The part level.
     * @param partType    The patch part.
     * @param array       The array data.
     */
    public IndicesPart(int patchSize, int centerLevel, int partLevel, IndicesPartType partType, short[] array) {
        mPatchSize = patchSize;
        mPartLevel = partLevel;
        mCenterLevel = centerLevel;
        mPartType = partType;
        mArray = array;
        mHashCode = IndicesPartHash.calcHash(patchSize, centerLevel, partLevel, partType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IndicesPart that = (IndicesPart) o;

        if (mPatchSize != that.mPatchSize) return false;
        if (mCenterLevel != that.mCenterLevel) return false;
        if (mPartLevel != that.mPartLevel) return false;
        return mPartType == that.mPartType;
    }

    @Override
    public int hashCode() {
        return mHashCode;
    }

    /**
     * The array data.
     *
     * @return The array data.
     */
    public short[] getArray() {
        return mArray;
    }
}

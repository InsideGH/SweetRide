package com.sweetlab.sweetride.demo.game.terrain.indices.combiner;

/**
 * Combines multiple pieces of indices parts into a single array.
 */
class PartCombiner implements AddOperation {
    /**
     * The combined data.
     */
    private short[] mData;

    /**
     * The index while combining.
     */
    private int mIndex;

    /**
     * Constructor.
     *
     * @param size Size of data.
     */
    public PartCombiner(int size) {
        mData = new short[size];
    }

    @Override
    public void addValue(short value) {
        mData[mIndex++] = value;
    }

    @Override
    public void addArray(short[] array) {
        System.arraycopy(array, 0, mData, mIndex, array.length);
        mIndex += array.length;
    }

    /**
     * Get the data.
     *
     * @return The data.
     */
    public short[] getData() {
        return mData;
    }
}

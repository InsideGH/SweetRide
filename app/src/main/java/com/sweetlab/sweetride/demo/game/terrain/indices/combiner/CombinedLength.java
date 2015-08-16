package com.sweetlab.sweetride.demo.game.terrain.indices.combiner;

/**
 * Calculates the length required for the combined data.
 */
class CombinedLength implements AddOperation {
    /**
     * The combined length.
     */
    private int mLength;

    @Override
    public void addValue(short value) {
        mLength++;
    }

    @Override
    public void addArray(short[] array) {
        mLength += array.length;
    }

    /**
     * Get the length.
     *
     * @return The length.
     */
    public int getLength() {
        return mLength;
    }
}

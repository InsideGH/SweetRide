package com.sweetlab.sweetride.demo.game.terrain.indices.combiner;

/**
 * An add operation.
 */
interface AddOperation {
    /**
     * Add the value.
     *
     * @param value
     */
    void addValue(short value);

    /**
     * Add the values in the array.
     *
     * @param array The array of values.
     */
    void addArray(short[] array);
}

package com.sweetlab.sweetride.demo.game.terrain.newtake;

import java.util.List;

/**
 * A 2D map where cells can be set and get. It's also possible
 * to get neighbour cells.
 *
 * @param <T> Type of content.
 */
public interface Map2D<T> extends Map2DSize {
    /**
     * Get number of rows.
     *
     * @return The number of rows.
     */
    int getNbrRows();

    /**
     * Get the component.
     *
     * @param z The position.
     * @param x The position.
     * @return The component or null if none.
     */
    T get(int z, int x);

    /**
     * Set a value.
     *
     * @param z     The position.
     * @param x     The position.
     * @param value The value.
     */
    void set(int z, int x, T value);

    /**
     * Get component left of position.
     *
     * @param z The position.
     * @param x The position.
     * @return The component or null if none.
     */
    T getLeft(int z, int x);

    /**
     * Get component right of position.
     *
     * @param z The position.
     * @param x The position.
     * @return The component or null if none.
     */
    T getRight(int z, int x);

    /**
     * Get component top of position.
     *
     * @param z The position.
     * @param x The position.
     * @return The component or null if none.
     */
    T getTop(int z, int x);

    /**
     * Get component bottom of position.
     *
     * @param z The position.
     * @param x The position.
     * @return The component or null if none.
     */
    T getBottom(int z, int x);

    /**
     * Shift the map. New positions will contain null values.
     *
     * @param z       Amount to shift vertically.
     * @param x       Amount to shift horizontally.
     * @param dropped Dropped items.
     */
    void shift(int z, int x, List<T> dropped);
}

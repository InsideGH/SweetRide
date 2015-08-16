package com.sweetlab.sweetride.demo.game.terrain.height;

/**
 * Height modifier which is used to modify height using 2 dimensional coordinates.
 */
public interface HeightModifier {
    /**
     * Set a value.
     *
     * @param x     X coordinate.
     * @param z     Z coordinate.
     * @param value The value.
     */
    void set(int x, int z, float value);

    /**
     * Get a value.
     *
     * @param x X coordinate.
     * @param z Z coordinate.
     * @return The value.
     */
    float get(int x, int z);

    /**
     * Check if a value is modifiable.
     *
     * @param x X coordinate.
     * @param z Z coordinate.
     * @return True if modifiable.
     */
    boolean isModifiable(int x, int z);

    /**
     * Copy unmodifiable value.
     *
     * @param x X coordinate.
     * @param z Z coordinate.
     */
    void copyUnModifiable(int x, int z);

    /**
     * Get the data.
     *
     * @return The data.
     */
    PatchHeightData getData();

    /**
     * Get the max height.
     *
     * @return Max height.
     */
    float getMaxHeight();

    /**
     * Get the min height.
     *
     * @return Min height.
     */
    float getMinHeight();
}

package com.sweetlab.sweetride.demo.game.terrain.newtake;

/**
 * Levels for different patches in the map. There is a center of the map.
 */
public class LevelMap extends AbstractMap2D<Integer> {
    /**
     * The center of the map, from left.
     */
    private static final int CENTER_X = 2;

    /**
     * The center of the map from top.
     */
    private static final int CENTER_Z = 2;

    /**
     * Minimum patch level.
     */
    private static final int MIN_PATCH_SIZE = 6;

    /**
     * The handmade level map.
     */
    private final Integer[][] mData = new Integer[][]
            {
                    {3, 3, 3, 3, 3},
                    {3, 0, 0, 0, 3},
                    {3, 0, 0, 0, 3},
                    {3, 0, 0, 0, 3},
                    {3, 3, 3, 3, 3},
            };

    /**
     * Max level in the map, i.e the largest number.
     */
    private final int mMaxLevel;

    /**
     * The required patch size.
     */
    private final int mPatchSize;

    /**
     * Constructor.
     */
    public LevelMap() {
        mMaxLevel = findMaxLevel();
        mPatchSize = Math.max(MIN_PATCH_SIZE, mMaxLevel + 1);
    }

    /**
     * Get the max level in the map.
     *
     * @return The max level.
     */
    public int getMaxLevel() {
        return mMaxLevel;
    }

    /**
     * Get the required patch size.
     *
     * @return The patch size.
     */
    public int getRequiredPatchSize() {
        return mPatchSize;
    }

    /**
     * Get the center.
     *
     * @return The center.
     */
    public int getCenterX() {
        return CENTER_X;
    }

    /**
     * Get the center.
     *
     * @return The center.
     */
    public int getCenterZ() {
        return CENTER_Z;
    }

    @Override
    protected Integer[][] getData() {
        return mData;
    }

    /**
     * Get the max level.
     *
     * @return The max level.
     */
    private int findMaxLevel() {
        int max = 0;
        for (int i = 0; i < mData.length; i++) {
            for (int j = 0; j < mData[i].length; j++) {
                if (mData[i][j] > max) {
                    max = mData[i][j];
                }
            }
        }
        return max;
    }
}
package com.sweetlab.sweetride.demo.game.terrain.indices.data;

/**
 * Hash value for a complete patch from a indices point of view.
 */
public class IndicesDataHash {
    /**
     * Calculate the hash value for a indices data.
     *
     * @param size        The patch size.
     * @param centerLevel The center level.
     * @param leftLevel   The left level.
     * @param rightLevel  The right level.
     * @param topLevel    The top level.
     * @param bottomLevel The bottom level.
     * @return The hash value.
     */
    public static Integer calcHash(int size, int centerLevel, int leftLevel, int rightLevel, int topLevel, int bottomLevel) {
        int result = size;
        result = 31 * result + centerLevel;
        result = 31 * result + leftLevel;
        result = 31 * result + rightLevel;
        result = 31 * result + topLevel;
        result = 31 * result + bottomLevel;
        return result;
    }
}

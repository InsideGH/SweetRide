package com.sweetlab.sweetride.demo.game.terrain.indices.part;

/**
 * Hash value for a part of a patch from a indices point of view.
 */
public class IndicesPartHash {
    /**
     * Calculate the hash value for a indices part.
     *
     * @param patchSize   The patch size.
     * @param centerLevel The center level.
     * @param partLevel   The part level.
     * @param partType    The part type.
     * @return The hash value.
     */
    public static int calcHash(int patchSize, int centerLevel, int partLevel, IndicesPartType partType) {
        int result = patchSize;
        result = 31 * result + centerLevel;
        result = 31 * result + partLevel;
        result = 31 * result + partType.hashCode();
        return result;
    }
}

package com.sweetlab.sweetride.demo.game.terrain.indices.part;

import java.util.HashMap;

/**
 * A cache of indices array parts. Not synchronised.
 */
public class IndicesPartCache {
    /**
     * The map/cache.
     */
    private static HashMap<Integer, IndicesPart> mMap = new HashMap<>();

    /**
     * Get a indices array part.
     *
     * @param part        The part type.
     * @param patchSize   The patch size.
     * @param centerLevel The center level.
     * @param partLevel   The part level.
     * @return The indices array part or null if none is found.
     */
    public static IndicesPart get(IndicesPartType part, int patchSize, int centerLevel, int partLevel) {
        return mMap.get(IndicesPartHash.calcHash(patchSize, centerLevel, partLevel, part));
    }

    /**
     * Put a indices array part into the cache.
     *
     * @param part The indices array part.
     */
    public static void put(IndicesPart part) {
        mMap.put(part.hashCode(), part);
    }
}

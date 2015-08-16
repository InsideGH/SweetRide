package com.sweetlab.sweetride.demo.game.terrain.indices;

import android.util.Log;

import com.sweetlab.sweetride.demo.game.terrain.newtake.TerrainLog;
import com.sweetlab.sweetride.demo.game.terrain.indices.data.IndicesData;
import com.sweetlab.sweetride.demo.game.terrain.indices.data.IndicesDataHash;

import java.util.HashMap;

/**
 * Cache with patch indices.
 * <p/>
 * Size of this cache is unbound. Here are some examples of how big this cache can get.
 * <p/>
 * <pre>
 *     PatchCell size       Vertices    Nbr Levels          Nbr combinations/entries
 *     2                5x5         1                   pow(1,5) = 1
 *     3                9x9         2                   pow(2,5) = 32
 *     4                17x17       3                   pow(3,5) = 243
 *     5                32x32       4                   pow(4,5) = 1024
 *     6                64x64       5                   pow(5,5) = 3125
 *     7                128x128     6                   pow(6,5) = 7776
 * </pre>
 */
public class PatchIndicesBufferCache {

    /**
     * The cache.
     */
    private HashMap<Integer, PatchIndicesBuffer> mMap = new HashMap<>();

    /**
     * Get patch indices buffer from cache.
     *
     * @param size        The patch size.
     * @param centerLevel The center level.
     * @param leftLevel   The left level.
     * @param rightLevel  The right level.
     * @param topLevel    The top level.
     * @param bottomLevel The bottom level.
     * @return The patch indices buffer, either from cache or newly created.
     */
    public synchronized PatchIndicesBuffer get(int size, int centerLevel, int leftLevel, int rightLevel, int topLevel, int bottomLevel) {
        Integer hash = IndicesDataHash.calcHash(size, centerLevel, leftLevel, rightLevel, topLevel, bottomLevel);
        PatchIndicesBuffer patchIndicesBuffer = mMap.get(hash);
        if (patchIndicesBuffer == null) {
            IndicesData data = new IndicesData(size, centerLevel, leftLevel, rightLevel, topLevel, bottomLevel);
            patchIndicesBuffer = new PatchIndicesBuffer(data);
            mMap.put(hash, patchIndicesBuffer);
            if (TerrainLog.LOG_PATCH_INDICES) {
                Log.d("Peter100", "PatchIndicesBufferCache.get null, creating indices " + " size " +
                        size + " center " + centerLevel + " left " + leftLevel + " right " +
                        rightLevel + " top " + topLevel + " bottom " + bottomLevel);
            }
        } else if (TerrainLog.LOG_PATCH_INDICES) {
            Log.d("Peter100", "PatchIndicesBufferCache.get from cache " + " size " +
                    size + " center " + centerLevel + " left " + leftLevel + " right " +
                    rightLevel + " top " + topLevel + " bottom " + bottomLevel);
        }
        return patchIndicesBuffer;
    }
}

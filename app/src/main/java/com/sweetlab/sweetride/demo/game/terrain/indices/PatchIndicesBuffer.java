package com.sweetlab.sweetride.demo.game.terrain.indices;

import com.sweetlab.sweetride.attributedata.IndicesBuffer;
import com.sweetlab.sweetride.context.BufferUsage;
import com.sweetlab.sweetride.demo.game.terrain.indices.data.IndicesData;

/**
 * A patch indices buffer with a unique hashcode.
 */
public class PatchIndicesBuffer extends IndicesBuffer {
    /**
     * The hashcode.
     */
    private int mHashCode;

    /**
     * Constructor.
     *
     * @param data Indices data.
     */
    public PatchIndicesBuffer(IndicesData data) {
        super(data.getIndices(), BufferUsage.STATIC);
        mHashCode = data.hashCode();
    }

    @Override
    public int hashCode() {
        return mHashCode;
    }
}

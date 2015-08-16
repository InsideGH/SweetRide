package com.sweetlab.sweetride.demo.game.terrain.indices.combiner;

import com.sweetlab.sweetride.demo.game.terrain.indices.part.IndicesPart;

import java.util.ArrayList;
import java.util.List;

/**
 * Combined indices parts.
 */
public class IndicesPartCombiner {
    /**
     * The combined indices.
     */
    private final short[] mIndices;

    /**
     * Builder.
     */
    public static class Builder {
        /**
         * List of strip arrays.
         */
        private List<short[]> mList = new ArrayList<>();

        /**
         * Add a indices part.
         *
         * @param indices The indices part.
         */
        public Builder addPart(IndicesPart indices) {
            mList.add(indices.getArray());
            return this;
        }

        /**
         * Build a combined indices.
         *
         * @return The combined indices.
         */
        public IndicesPartCombiner build() {
            Logic logic = new Logic(mList);

            CombinedLength combinedLength = new CombinedLength();
            logic.run(combinedLength);

            PartCombiner partCombiner = new PartCombiner(combinedLength.getLength());
            logic.run(partCombiner);

            return new IndicesPartCombiner(partCombiner.getData());
        }
    }

    /**
     * Constructor.
     *
     * @param indices The combined indices.
     */
    private IndicesPartCombiner(short[] indices) {
        mIndices = indices;
    }

    /**
     * Get the combined indices.
     *
     * @return The indices.
     */
    public short[] getIndices() {
        return mIndices;
    }
}

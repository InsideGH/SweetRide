package com.sweetlab.sweetride.demo.game.terrain.indices.combiner;

import java.util.List;

/**
 * The logic for combining multiple triangle strip indices arrays into one array.
 */
class Logic {
    /**
     * List of indices arrays to work with.
     */
    public final List<short[]> mList;

    /**
     * Constructor.
     *
     * @param list List of indices arrays to work with.
     */
    public Logic(List<short[]> list) {
        mList = list;
    }

    /**
     * Run the logic.
     *
     * @param addOperation The addOperation to use.
     */
    public void run(AddOperation addOperation) {
        int index = 0;
        final int listSize = mList.size();
        for (int i = 0; i < listSize; i++) {
            short[] shorts = mList.get(i);

            if (i > 0) {
                short firstValue = shorts[0];
                addOperation.addValue(firstValue);
                index++;
                if ((index % 2) == 1) {
                    addOperation.addValue(firstValue);
                    index++;
                }
            }

            addOperation.addArray(shorts);
            index += shorts.length;

            if (i < (listSize - 1)) {
                short lastValue = shorts[shorts.length - 1];
                addOperation.addValue(lastValue);
                index++;
            }
        }
    }
}

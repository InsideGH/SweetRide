package com.sweetlab.sweetride.demo.game.terrain.newtake;

/**
 * A 2D map with patches.
 */
public class PatchMap extends AbstractMap2D<Patch> {
    /**
     * The 2D data.
     */
    private final Patch[][] mData;

    /**
     * Constructor. Creates an empty map.
     *
     * @param size The size of the map.
     */
    public PatchMap(Map2DSize size) {
        int nbrRows = size.getNbrRows();
        mData = new Patch[nbrRows][];
        for (int i = 0; i < nbrRows; i++) {
            mData[i] = new Patch[size.getRowSize(i)];
        }
    }

    @Override
    protected Patch[][] getData() {
        return mData;
    }
}

package com.sweetlab.sweetride.demo.game.terrain.newtake;

/**
 * A 2D map size.
 */
public interface Map2DSize {
    /**
     * Get number of rows.
     *
     * @return The number of rows.
     */
    int getNbrRows();

    /**
     * Get the row size.
     *
     * @param row Row index.
     * @return The row size.
     */
    int getRowSize(int row);

}

package com.sweetlab.sweetride.demo.game.terrain.newtake;

/**
 * A un-bounded flat grid in x and z plane consisting of square cells with bounded size.
 */
public class Grid {
    /**
     * A cell size.
     */
    private final float mCellSize;

    /**
     * The half cell size.
     */
    private final float mCellSizeH;

    /**
     * Constructor.
     *
     * @param cellSize The size of a cell.
     */
    public Grid(float cellSize) {
        mCellSize = cellSize;
        mCellSizeH = cellSize / 2;
    }

    /**
     * Get the cell size.
     *
     * @return The cell size.
     */
    public float getCellSize() {
        return mCellSize;
    }

    /**
     * Get grid position.
     *
     * @param worldPos World space position.
     * @return The grid position.
     */
    public int convertToGridPos(float worldPos) {
        float absPos = Math.abs(worldPos);

        float left = absPos - mCellSizeH;
        left = Math.max(0, left);
        left /= mCellSize;
        int ceil = (int) Math.ceil(left);
        return worldPos < 0 ? -ceil : ceil;
    }

    /**
     * Get the world position.
     *
     * @param gridPos Grid space position.
     * @return The world position.
     */
    public float convertToWorldPos(int gridPos) {
        return gridPos * mCellSize;
    }
}

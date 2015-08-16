package com.sweetlab.sweetride.demo.game.terrain.newtake;

import java.util.List;

/**
 * An implementation of a 2D map. Extenders need to provide backing
 * data. Supports shifting.
 *
 * @param <T>
 */
public abstract class AbstractMap2D<T> implements Map2D<T> {

    @Override
    public int getNbrRows() {
        return getData().length;
    }

    @Override
    public int getRowSize(int row) {
        return getData()[row].length;
    }

    @Override
    public T get(int z, int x) {
        T[][] matrix = getData();
        int zLen = matrix.length;
        if (z < zLen && z >= 0) {
            int xLen = matrix[z].length;
            if (x < xLen && x >= 0) {
                return matrix[z][x];
            }
        }
        return null;
    }

    @Override
    public void set(int z, int x, T value) {
        T[][] data = getData();
        data[z][x] = value;
    }

    @Override
    public T getLeft(int z, int x) {
        return get(z, x - 1);
    }

    @Override
    public T getRight(int z, int x) {
        return get(z, x + 1);
    }

    @Override
    public T getTop(int z, int x) {
        return get(z - 1, x);
    }

    @Override
    public T getBottom(int z, int x) {
        return get(z + 1, x);
    }

    @Override
    public void shift(int z, int x, List<T> dropped) {
        T[][] data = getData();
        if (x != 0) {
            shiftMapHorizontally(data, x, dropped);
        }
        if (z != 0) {
            shiftMapVertically(data, z, dropped);
        }
    }

    /**
     * Get the backing data.
     *
     * @return The 2D array.
     */
    protected abstract T[][] getData();

    /**
     * This map vertically.
     *
     * @param map     The map.
     * @param amount  Amount to shift.
     * @param dropped Dropped items.
     */
    private void shiftMapVertically(T[][] map, int amount, List<T> dropped) {
        if (amount > 0) {
            for (int i = 0; i < amount; i++) {
                shiftUp(map, dropped);
            }
        } else {
            for (int i = 0; i < -amount; i++) {
                shiftDown(map, dropped);
            }
        }
    }

    /**
     * Shift map up one step.
     *
     * @param map     The map.
     * @param dropped Dropped items.
     */
    private void shiftUp(T[][] map, List<T> dropped) {
        final int lastRowIdx = map.length - 1;
        final T[] bottomRow = map[0];
        final T[] topRow = map[lastRowIdx];
        final int rowLength = topRow.length;

        for (int i = 0; i < rowLength; i++) {
            T cell = topRow[i];
            if (cell != null) {
                dropped.add(cell);
            }
        }

        for (int i = lastRowIdx; i > 0; i--) {
            T[] toRow = map[i];
            T[] fromRow = map[i - 1];
            System.arraycopy(fromRow, 0, toRow, 0, rowLength);
        }

        for (int i = 0; i < rowLength; i++) {
            bottomRow[i] = null;
        }
    }

    /**
     * Shift map down one step.
     *
     * @param map     The map.
     * @param dropped Dropped items.
     */
    private void shiftDown(T[][] map, List<T> dropped) {
        final int lastRowIdx = map.length - 1;
        final T[] bottomRow = map[0];
        final T[] topRow = map[lastRowIdx];
        final int rowLength = topRow.length;

        for (int i = 0; i < rowLength; i++) {
            T cell = bottomRow[i];
            if (cell != null) {
                dropped.add(cell);
            }
        }

        for (int i = 0; i < lastRowIdx; i++) {
            T[] toRow = map[i];
            T[] fromRow = map[i + 1];
            System.arraycopy(fromRow, 0, toRow, 0, rowLength);
        }

        for (int i = 0; i < rowLength; i++) {
            topRow[i] = null;
        }
    }

    /**
     * Shift map horizontally.
     *
     * @param map      The map.
     * @param colShift Amount to shift.
     * @param dropped  Dropped items.
     */
    private void shiftMapHorizontally(T[][] map, int colShift, List<T> dropped) {
        for (int row = 0; row < map.length; row++) {
            shiftArray(map[row], colShift, dropped);
        }
    }

    /**
     * Shift array.
     *
     * @param array    The array.
     * @param colShift Amount to shift.
     * @param dropped  Dropped items.
     */
    private void shiftArray(T[] array, int colShift, List<T> dropped) {
        if (colShift < 0) {
            for (int i = 0; i < -colShift; i++) {
                T cell = shiftRight(array);
                if (cell != null) {
                    dropped.add(cell);
                }
            }
        } else {
            for (int i = 0; i < colShift; i++) {
                T cell = shiftLeft(array);
                if (cell != null) {
                    dropped.add(cell);
                }
            }
        }
    }

    /**
     * Shift row left one step.
     *
     * @param array The array.
     * @return Dropped item.
     */
    private T shiftLeft(T[] array) {
        final int lastIdx = array.length - 1;
        final T removed = array[0];

        for (int j = 0; j < lastIdx; j++) {
            array[j] = array[j + 1];
        }

        array[lastIdx] = null;
        return removed;
    }

    /**
     * Shift array right one step.
     *
     * @param array The array.
     * @return Dropped item.
     */
    private T shiftRight(T[] array) {
        final int lastIdx = array.length - 1;
        final T removed = array[lastIdx];

        for (int j = lastIdx; j > 0; j--) {
            array[j] = array[j - 1];
        }

        array[0] = null;
        return removed;
    }
}

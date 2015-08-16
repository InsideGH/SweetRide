package com.sweetlab.sweetride.demo.game.terrain.indices.data;

import android.util.Log;

import com.sweetlab.sweetride.demo.game.terrain.newtake.TerrainLog;
import com.sweetlab.sweetride.demo.game.terrain.indices.combiner.IndicesPartCombiner;
import com.sweetlab.sweetride.demo.game.terrain.indices.part.IndicesPart;
import com.sweetlab.sweetride.demo.game.terrain.indices.part.IndicesPartCache;
import com.sweetlab.sweetride.demo.game.terrain.indices.part.IndicesPartType;

/**
 * PatchCell indices data. Contains indices for a complete patch, i.e center, left, right, top
 * bottom parts.
 */
public class IndicesData {
    /**
     * Identity.
     */
    private final int mHashCode;

    /**
     * Number of vertices along x and z axis in the vertices array (not level dependent).
     */
    private final int mArrayVertexCount;

    /**
     * Number of row, cols in the vertices array (not level dependent).
     */
    private final int mArrayRowColCount;

    /**
     * Number of vertices along x and z axis given center/patch level.
     */
    private final int mPatchVertexCount;

    /**
     * Number of patch rows/cols given center/patch level.
     */
    private final int mPatchRowColCount;

    /**
     * Level dependent step size to next vertex along x axis in vertex data array for center part.
     */
    private final int mCenterStepX;

    /**
     * Level dependent step size to next vertex along z axis in vertex data array for center part.
     */
    private final int mCenterStepZ;

    /**
     * The indices (includes top, left, right, bottom and center).
     */
    private final short[] mIndices;

    /**
     * The size of this patch.
     */
    private final int mSize;

    /**
     * The center level.
     */
    private final int mCenterLevel;

    /**
     * Constructor. Create a unique identifier (equals and hashcode) for indices data.
     *
     * @param size        Size of the vertices the indices are indexing into.
     * @param centerLevel The center level.
     * @param leftLevel   The left level.
     * @param rightLevel  The right level.
     * @param topLevel    The top level.
     * @param bottomLevel The bottom level.
     */
    public IndicesData(int size, int centerLevel, int leftLevel, int rightLevel, int topLevel, int bottomLevel) {
        verifyOrThrow(size, centerLevel, leftLevel, rightLevel, topLevel, bottomLevel);
        mSize = size;
        mCenterLevel = centerLevel;
        mHashCode = IndicesDataHash.calcHash(size, centerLevel, leftLevel, rightLevel, topLevel, bottomLevel);

        mArrayVertexCount = calcSideVertexCount(0);
        mArrayRowColCount = mArrayVertexCount - 1;

        mPatchVertexCount = calcSideVertexCount(centerLevel);
        mPatchRowColCount = mPatchVertexCount - 1;

        mCenterStepX = calcArrayStepX(centerLevel);
        mCenterStepZ = calcArrayStepZ(centerLevel);

        IndicesPartCombiner.Builder builder = new IndicesPartCombiner.Builder();
        builder.addPart(getPart(IndicesPartType.CENTER, centerLevel));
        builder.addPart(getPart(IndicesPartType.BOTTOM, bottomLevel));
        builder.addPart(getPart(IndicesPartType.TOP, topLevel));
        builder.addPart(getPart(IndicesPartType.RIGHT, rightLevel));
        builder.addPart(getPart(IndicesPartType.LEFT, leftLevel));
        mIndices = builder.build().getIndices();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IndicesData that = (IndicesData) o;

        return mHashCode == that.mHashCode;
    }

    @Override
    public int hashCode() {
        return mHashCode;
    }

    /**
     * Get the patch size.
     *
     * @return The patch size.
     */
    public int getSize() {
        return mSize;
    }

    /**
     * Get the triangle strip indices.
     *
     * @return The indices.
     */
    public short[] getIndices() {
        return mIndices;
    }

    /**
     * Get a indices array part.
     *
     * @param type      The array part type.
     * @param partLevel The array part level.
     * @return The part.
     */
    private IndicesPart getPart(IndicesPartType type, int partLevel) {
        IndicesPart part;
        synchronized (IndicesPartCache.class) {
            part = IndicesPartCache.get(type, mSize, mCenterLevel, partLevel);
            if (part == null) {
                if (TerrainLog.LOG_PATCH_INDICES_PARTS) {
                    Log.d("Peter100", "IndicesData.getPart null creating part type " + type + " size " + mSize + " level " + partLevel);
                }
                switch (type) {
                    case CENTER:
                        part = generateCenter(partLevel);
                        break;
                    case LEFT:
                        part = generateLeft(partLevel);
                        break;
                    case RIGHT:
                        part = generateRight(partLevel);
                        break;
                    case BOTTOM:
                        part = generateBottom(partLevel);
                        break;
                    case TOP:
                        part = generateTop(partLevel);
                        break;
                }
                IndicesPartCache.put(part);
            } else if (TerrainLog.LOG_PATCH_INDICES_PARTS) {
                Log.d("Peter100", "IndicesData.getPart cached type " + type + " size " + mSize + " level " + partLevel);
            }
        }
        return part;
    }

    /**
     * Generate center array part.
     * <pre>
     * Example size = 2 and level 0 ->
     *     24 - 23 - 22 - 21 - 20
     *      |    |    |    |    |
     *     19 - 18 - 17 - 16 - 15
     *      |    |    |    |    |
     *     14 - 13 - 12 - 11 - 10
     *      |    |    |    |    |
     *     9  -  8 -  7 -  6 -  5
     *      |    |    |    |    |
     *     4  -  3  - 2  - 1  - 0
     *
     * Center ->
     *          18 - 17 - 16
     *           |    |    |
     *          13 - 12 - 11
     *           |    |    |
     *           8 -  7 -  6
     *
     * Indices : 6, 11, 7, 12, 8, 13, (13,11), 11, 16, 12, 17, 13, 18
     *
     * </pre>
     *
     * @param centerLevel The center level.
     * @return The center indices array part.
     */
    private IndicesPart generateCenter(int centerLevel) {
        int centerSideVertexCount = mPatchVertexCount - 2;
        int centerSideRowColCount = centerSideVertexCount - 1;
        int nbrIndicesPerRow = centerSideVertexCount * 2;
        int nbrDegeneration = (centerSideRowColCount - 1) * 2;
        int nbrIndices = centerSideRowColCount * nbrIndicesPerRow + nbrDegeneration;
        short[] indices = new short[nbrIndices];

        int idx = 0;
        int startPos = mCenterStepX + mCenterStepZ;
        for (int row = 0; row < centerSideRowColCount; row++) {
            int rowPos = startPos + row * mCenterStepZ;
            /**
             * Not first row, duplicate/degenerate latest and next.
             */
            if (row > 0) {
                indices[idx] = indices[idx - 1];
                idx++;
                indices[idx++] = (short) rowPos;
            }
            /**
             * Create a strip for the row.
             */
            for (int x = 0; x < centerSideVertexCount; x++) {
                indices[idx++] = (short) rowPos;
                indices[idx++] = (short) (rowPos + mCenterStepZ);
                rowPos += mCenterStepX;
            }
        }
        return new IndicesPart(mSize, mCenterLevel, centerLevel, IndicesPartType.CENTER, indices);
    }

    /**
     * Generate bottom indices array part.
     * <pre>
     * Example center level 0 and bottom level 0 ->
     *       16 - 15 - 14 - 13 - 12 - 11 - 10
     * 8   -  7  - 6  - 5  - 4  - 3  - 2  - 1  - 0
     *
     * Example center level 0 and bottom level 1 ->
     *       16 - 15 - 14 - 13 - 12 - 11 - 10
     * 8         - 6       - 4       - 2       - 0
     *
     * Example center level 0 and bottom level 2,
     * (in reality, border level this low is not allowed) ->
     *       16 - 15 - 14 - 13 - 12 - 11 - 10
     * 8                   - 4                 - 0
     * </pre>
     * <p/>
     * Example : starts at 0/10 and ends at 16/8.
     *
     * @param bottomLevel Bottom level.
     * @return The bottom indices array part.
     */
    private IndicesPart generateBottom(int bottomLevel) {
        int borderRowColCount = calcSideVertexCount(bottomLevel) - 1;
        int borderStep = calcArrayStepX(bottomLevel);
        int centerStep = mCenterStepX;
        int borderPos = 0;
        int centerPos = mCenterStepX + mCenterStepZ;
        short[] shorts = genBorderStrip(borderRowColCount, borderStep, centerStep, borderPos, centerPos);
        return new IndicesPart(mSize, mCenterLevel, bottomLevel, IndicesPartType.BOTTOM, shorts);
    }

    /**
     * Generate top indices array part.
     * <pre>
     * Example center level 0 and top level 0 ->
     * 80  - 79 - 78 - 77 - 76 - 75 - 74 - 73 - 72
     *       70 - 69 - 68 - 67 - 66 - 65 - 64
     *
     * Example center level 0 and top level 1 ->
     * 80         78        76        74        72
     *       70 - 69 - 68 - 67 - 66 - 65 - 64
     *
     * Example center level 0 and top level 2,
     * (in reality, border level this low is not allowed) ->
     * 80                   76                  72
     *       70 - 69 - 68 - 67 - 66 - 65 - 64
     * </pre>
     * <p/>
     * Example : starts at 80/70 and ends at 64/72.
     *
     * @param topLevel Top level.
     * @return The top indices array part.
     */
    private IndicesPart generateTop(int topLevel) {
        int borderStartPos = mArrayVertexCount * mArrayVertexCount - 1;
        int centerStartPos = borderStartPos - mCenterStepX - mCenterStepZ;
        int borderStepSize = -calcArrayStepX(topLevel);
        int centerStepSize = -mCenterStepX;
        int borderRowColCount = calcSideVertexCount(topLevel) - 1;
        short[] shorts = genBorderStrip(borderRowColCount, borderStepSize, centerStepSize, borderStartPos, centerStartPos);
        return new IndicesPart(mSize, mCenterLevel, topLevel, IndicesPartType.TOP, shorts);
    }

    /**
     * Generate right indices array part.
     *
     * @param rightLevel Right level.
     * @return The right indices array part.
     */
    private IndicesPart generateRight(int rightLevel) {
        int borderStartPos = mArrayRowColCount * mArrayVertexCount;
        int centerStartPos = borderStartPos + mCenterStepX - mCenterStepZ;
        int borderStepSize = -calcArrayStepZ(rightLevel);
        int centerStepSize = -mCenterStepZ;
        int borderRowColCount = calcSideVertexCount(rightLevel) - 1;
        short[] shorts = genBorderStrip(borderRowColCount, borderStepSize, centerStepSize, borderStartPos, centerStartPos);
        return new IndicesPart(mSize, mCenterLevel, rightLevel, IndicesPartType.RIGHT, shorts);
    }

    /**
     * Generate left indices array part.
     *
     * @param leftLevel left level.
     * @return The right indices array part.
     */
    private IndicesPart generateLeft(int leftLevel) {
        int borderStartPos = mArrayRowColCount;
        int centerStartPos = borderStartPos - mCenterStepX + mCenterStepZ;
        int borderStepSize = calcArrayStepZ(leftLevel);
        int centerStepSize = mCenterStepZ;
        int borderRowColCount = calcSideVertexCount(leftLevel) - 1;
        short[] shorts = genBorderStrip(borderRowColCount, borderStepSize, centerStepSize, borderStartPos, centerStartPos);
        return new IndicesPart(mSize, mCenterLevel, leftLevel, IndicesPartType.LEFT, shorts);
    }

    /**
     * Generate a triangle strip array for a border.
     * <pre>
     * Example size = 3 and level 0 ->
     * 80  - 79 - 78 - 77 - 76 - 75 - 74 - 73 - 72
     * 71  - 70 - 69 - 68 - 67 - 66 - 65 - 64 - 63
     * 62  - 61 - 60 - 59 - 58 - 57 - 56 - 55 - 54
     * 53  - 52 - 51 - 50 - 49 - 48 - 47 - 46 - 45
     * 44  - 43 - 42 - 41 - 40 - 39 - 38 - 37 - 36
     * 35  - 34 - 33 - 32 - 31 - 30 - 29 - 28 - 27
     * 26  - 25 - 24 - 23 - 22 - 21 - 20 - 19 - 18
     * 17  - 16 - 15 - 14 - 13 - 12 - 11 - 10  - 9
     * 8   -  7  - 6  - 5  - 4  - 3  - 2  - 1  - 0
     * </pre>
     *
     * @param borderRowColCount Number of rows/cols the border has at a certain level.
     * @param borderStep        Step size for the border.
     * @param centerStep        Step size of the center.
     * @param borderPos         Start position in the vertices array for the border.
     * @param centerPos         Start position in the vertices array for the center.
     * @return Array of indices.
     */
    private short[] genBorderStrip(int borderRowColCount, int borderStep, int centerStep, int borderPos, int centerPos) {
        /**
         * Initialize storage and storage index.
         */
        final short[] indices = new short[calcNbrBorderIndices(borderRowColCount)];
        int idx = 0;

        /**
         * First part.
         */
        indices[idx++] = (short) borderPos;
        borderPos += borderStep;

        /**
         * Middle part.
         */
        int ratio = mPatchRowColCount / borderRowColCount;
        int partCount = borderRowColCount - 1;
        for (int part = 0; part < partCount; part++) {
            for (int i = 0; i < ratio; i++) {
                indices[idx++] = (short) centerPos;
                centerPos += centerStep;
                indices[idx++] = (short) borderPos;
            }
            borderPos += borderStep;
        }

        /**
         * Last part. Either just add the last border vertex or iterate through
         * that last part which includes the last border vertex.
         */
        final int count = ratio - 1;
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                indices[idx++] = (short) centerPos;
                centerPos += centerStep;
                indices[idx++] = (short) borderPos;
            }
        } else {
            indices[idx] = (short) borderPos;
        }
        return indices;
    }

    /**
     * Calculate number of indices required for a border (left, right, top or bottom).
     * <pre>
     * Example size = 3 and level 0 ->
     *     80  - 79 - 78 - 77 - 76 - 75 - 74 - 73 - 72
     *     71  - 70 - 69 - 68 - 67 - 66 - 65 - 64 - 63
     *     62  - 61 - 60 - 59 - 58 - 57 - 56 - 55 - 54
     *     53  - 52 - 51 - 50 - 49 - 48 - 47 - 46 - 45
     *     44  - 43 - 42 - 41 - 40 - 39 - 38 - 37 - 36
     *     35  - 34 - 33 - 32 - 31 - 30 - 29 - 28 - 27
     *     26  - 25 - 24 - 23 - 22 - 21 - 20 - 19 - 18
     *     17  - 16 - 15 - 14 - 13 - 12 - 11 - 10  - 9
     *     8   -  7  - 6  - 5  - 4  - 3  - 2  - 1  - 0
     *
     * Example center level 0 and border(bottom) level 0 ->
     *           16 - 15 - 14 - 13 - 12 - 11 - 10
     *     8   -  7  - 6  - 5  - 4  - 3  - 2  - 1  - 0
     *
     * Example center level 0 and border(bottom) level 1 ->
     *           16 - 15 - 14 - 13 - 12 - 11 - 10
     *     8         - 6       - 4       - 2       - 0
     *
     * Example center level 0 and border(bottom) level 2,
     * (in reality, border level this low is not allowed) ->
     *           16 - 15 - 14 - 13 - 12 - 11 - 10
     *     8                   - 4                 - 0
     * </pre>
     *
     * @param borderRowColCount Number of rows/cols in border.
     * @return Number of indices required.
     */
    private int calcNbrBorderIndices(int borderRowColCount) {
        /**
         * The columns/rows ratio between center and border.
         */
        final int ratio = mPatchRowColCount / borderRowColCount;

        /**
         * First part.
         */
        int nbrIndices = 1;

        /**
         * Middle parts.
         */
        final int partCount = (borderRowColCount - 1);
        nbrIndices += partCount * ratio * 2;

        /**
         * Last part (minimum 1).
         */
        nbrIndices += Math.max(1, (ratio - 1) * 2);

        return nbrIndices;
    }

    /**
     * Calculate number of vertices along x or z axis with level taken into account.
     *
     * @param level The level.
     * @return Number of vertices.
     */
    private int calcSideVertexCount(int level) {
        return ((1 << mSize) >> level) + 1;
    }

    /**
     * Calculate level dependent step size to next vertex along x axis in vertex data array.
     *
     * @param level The level
     * @return The x axis step size.
     */
    private int calcArrayStepX(int level) {
        return 1 << level;
    }

    /**
     * Calculate level dependent step size to next vertex along z axis in vertex data array.
     *
     * @param level The level
     * @return The z axis step size.
     */
    private int calcArrayStepZ(int level) {
        int quads = 1 << mSize;
        int vertices = quads + 1;
        return vertices << level;
    }

    /**
     * Check that everything is in order.
     *
     * @param size   Size of the vertices the indices are indexing into.
     * @param center The center level.
     * @param left   The left level.
     * @param right  The right level.
     * @param top    The top level.
     * @param bottom The bottom level.
     */
    private static void verifyOrThrow(int size, int center, int left, int right, int top, int bottom) {
        if ((1 << size) < 4) {
            throw new RuntimeException("Can't create indices for vertex matrix with less " +
                    "than 5 vertices along x and z axis");
        }

        if (((1 << size) >> center) < 4) {
            throw new RuntimeException("Can't create leveled indices for vertex matrix with less " +
                    "than 5 vertices along x and z axis");
        }

        if (left < center || right < center || top < center || bottom < center) {
            throw new RuntimeException("Can't create indices with higher border resolution " +
                    "than center, size " + size + " center " + center + " left " + left +
                    " right " + right + " top " + top + " bottom " + bottom);
        }
    }
}

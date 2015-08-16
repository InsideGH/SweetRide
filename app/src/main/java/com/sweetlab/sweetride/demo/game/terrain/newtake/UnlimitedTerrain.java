package com.sweetlab.sweetride.demo.game.terrain.newtake;

import com.sweetlab.sweetride.demo.game.terrain.height.MpdHeightGenerator;
import com.sweetlab.sweetride.demo.game.terrain.height.PatchHeightData;
import com.sweetlab.sweetride.demo.game.terrain.height.PatchHeightModifier;
import com.sweetlab.sweetride.demo.game.terrain.indices.PatchIndicesBuffer;
import com.sweetlab.sweetride.demo.game.terrain.indices.PatchIndicesBufferCache;
import com.sweetlab.sweetride.intersect.BoundingBox;
import com.sweetlab.sweetride.math.Vec3;
import com.sweetlab.sweetride.node.Node;
import com.sweetlab.sweetride.texture.Texture2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * UnlimitedTerrain node. The terrain has a fixed size but can be reposition anywhere in world space.
 * This gives the illusion of a unlimited terrain.
 */
public class UnlimitedTerrain extends Node {
    /**
     * Base seed for terrain randoms.
     */
    private static final long SEED = 128;

    /**
     * Pseudo random generator circular size.
     */
    private static final int CIRCULAR_SIZE = 1000;

    /**
     * The height generated roughness.
     */
    private static final float ROUGHNESS = 0.9f;

    /**
     * The height generated height maximum value.
     */
    private static final float RAND_RANGE = 1f;

    /**
     * The terrain randoms.
     */
    private final Randoms mRandoms = new Randoms(SEED, CIRCULAR_SIZE);

    /**
     * The height generator.
     */
    private final MpdHeightGenerator mHeightGenerator;

    /**
     * A patch indices cache.
     */
    private final PatchIndicesBufferCache mIndicesCache = new PatchIndicesBufferCache();

    /**
     * The base geometry pool.
     */
    private final PatchPool mPatchPool;

    /**
     * The patch size.
     */
    private final int mPatchSize;

    /**
     * The level map.
     */
    private final LevelMap mLevelMap;

    /**
     * The patch map.
     */
    private final PatchMap mPatchMap;

    /**
     * The position of the map in world space (grid based).
     */
    private int mGridPosX;

    /**
     * The position of the map in world space (grid based).
     */
    private int mGridPosZ;

    /**
     * The grid.
     */
    private final Grid mGrid;

    /**
     * The patch texture
     * TODO: this is just temporary to have something.
     */
    private final Texture2D mTexture;

    /**
     * Constructor. Creates a UnlimitedTerrain.
     *
     * @param gridPosX The world grid position.
     * @param gridPosZ The world grid position.
     * @param texture  Texture to use.
     */
    public UnlimitedTerrain(int gridPosX, int gridPosZ, Grid grid, Texture2D texture) {
        mGrid = grid;
        mTexture = texture;

        mLevelMap = new LevelMap();
        mPatchMap = new PatchMap(mLevelMap);
        mPatchSize = mLevelMap.getRequiredPatchSize();

        mHeightGenerator = new MpdHeightGenerator(mPatchSize, ROUGHNESS, RAND_RANGE);
        mPatchPool = new PatchPool(mPatchSize, grid.getCellSize(), grid.getCellSize());

        mGridPosX = gridPosX;
        mGridPosZ = gridPosZ;

        rePositionMap(gridPosZ, gridPosX);
    }

    /**
     * Get terrain position in world grid space.
     *
     * @return The x position.
     */
    public int getGridPosX() {
        return mGridPosX;
    }

    /**
     * Get terrain position in world grid space.
     *
     * @return The z position.
     */
    public int getGridPosZ() {
        return mGridPosZ;
    }

    /**
     * Reposition the map.
     *
     * @param mapGridPosX The world grid position.
     * @param mapGridPosZ The world grid position.
     */
    public void rePositionMap(int mapGridPosZ, int mapGridPosX) {
        reposition(mapGridPosZ, mapGridPosX);

        final int gridStartX = mapGridPosX - mLevelMap.getCenterX();
        int gridStartZ = mapGridPosZ - mLevelMap.getCenterZ();

        int nbrRows = mPatchMap.getNbrRows();
        for (int mapZ = 0; mapZ < nbrRows; mapZ++) {
            int rowSize = mPatchMap.getRowSize(mapZ);
            int gridCurrX = gridStartX;
            for (int mapX = 0; mapX < rowSize; mapX++) {
                Patch patch = mPatchMap.get(mapZ, mapX);
                if (patch == null) {
                    Random random = mRandoms.getRandom(mapGridPosZ, mapGridPosX);
                    Patch newPatch = createPatch(random, mapZ, mapX);

                    float worldX = mGrid.convertToWorldPos(gridCurrX);
                    float worldZ = mGrid.convertToWorldPos(gridStartZ);
                    newPatch.setTranslate(worldX, 0, worldZ);

                    newPatch.addTexture(mTexture);

                    mPatchMap.set(mapZ, mapX, newPatch);
                    addChild(newPatch);
                } else {
                    PatchIndicesBuffer indices = getIndices(mapZ, mapX);
                    patch.setIndicesBuffer(indices);
                }
                gridCurrX++;
            }
            gridStartZ++;
        }
    }

    /**
     * Shift the map.
     *
     * @param mapGridPosZ The position to reposition to.
     * @param mapGridPosX The position to reposition to.
     */
    private void reposition(int mapGridPosZ, int mapGridPosX) {
        final int transZ = -(mapGridPosZ - mGridPosZ);
        final int transX = mapGridPosX - mGridPosX;
        mGridPosZ = mapGridPosZ;
        mGridPosX = mapGridPosX;

        List<Patch> dropped = new ArrayList<>();
        mPatchMap.shift(transZ, transX, dropped);
        for (Patch patch : dropped) {
            mPatchPool.put(patch);
            removeChild(patch);
        }
    }

    /**
     * Create patch.
     *
     * @param random Random number to use.
     * @param mapZ   Map z position.
     * @param mapX   Map z position.
     * @return The created patch.
     */
    private Patch createPatch(Random random, int mapZ, int mapX) {
        Patch patch = mPatchPool.get();

        PatchIndicesBuffer indices = getIndices(mapZ, mapX);
        patch.setIndicesBuffer(indices);

        PatchHeightModifier heightData = createHeightData(random, mapZ, mapX);
        patch.setHeightData(heightData.getData());

        float hs = mGrid.getCellSize() / 2;
        Vec3 min = new Vec3(-hs, heightData.getMinHeight(), -hs);
        Vec3 max = new Vec3(hs, heightData.getMaxHeight(), hs);
        patch.setBoundingBox(new BoundingBox(min, max));
        return patch;
    }

    /**
     * Create midpoint displacement height.
     *
     * @param random The random to use.
     * @param mapZ   The map z index.
     * @param mapX   The map x index.
     * @return Height data modifier.
     */
    private PatchHeightModifier createHeightData(Random random, int mapZ, int mapX) {
        Patch left = mPatchMap.getLeft(mapZ, mapX);
        Patch right = mPatchMap.getRight(mapZ, mapX);
        Patch top = mPatchMap.getTop(mapZ, mapX);
        Patch bottom = mPatchMap.getBottom(mapZ, mapX);
        PatchHeightData leftHeight = left != null ? left.getHeightData() : null;
        PatchHeightData topHeight = top != null ? top.getHeightData() : null;
        PatchHeightData rightHeight = right != null ? right.getHeightData() : null;
        PatchHeightData bottomHeight = bottom != null ? bottom.getHeightData() : null;

        PatchHeightData dstHeight = new PatchHeightData(mPatchSize);
        PatchHeightModifier modifier = new PatchHeightModifier(dstHeight, leftHeight, topHeight, rightHeight, bottomHeight);
        mHeightGenerator.generate(modifier, random);

        return modifier;
    }

    /**
     * Get patch indices buffer.
     *
     * @param z Map z index.
     * @param x Map x index.
     * @return The indices buffer.
     */
    private PatchIndicesBuffer getIndices(int z, int x) {
        Integer center = mLevelMap.get(z, x);
        Integer left = mLevelMap.getLeft(z, x);
        Integer right = mLevelMap.getRight(z, x);
        Integer top = mLevelMap.getTop(z, x);
        Integer bottom = mLevelMap.getBottom(z, x);

        if (left == null || center > left) {
            left = center;
        }
        if (right == null || center > right) {
            right = center;
        }
        if (top == null || center > top) {
            top = center;
        }
        if (bottom == null || center > bottom) {
            bottom = center;
        }
        return mIndicesCache.get(mPatchSize, center, left, right, top, bottom);
    }
}

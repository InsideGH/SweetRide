package com.sweetlab.sweetride.demo.game.terrain.height;

import java.util.Random;

/**
 * Midpoint displacement height generator.
 */
public class MpdHeightGenerator {
    /**
     * Number or rows and columns in the patch at level 0.
     */
    private final int mPatchRowsCols;

    /**
     * Number or vertices in the patch at level 0.
     */
    private final int mPatchSideVertices;

    /**
     * The patch size.
     */
    private final int mSize;

    /**
     * The roughness.
     */
    private final float mRoughness;

    /**
     * The random range.
     */
    private final float mRandomRange;

    /**
     * Constructor.
     *
     * @param size      The size (width and depth) of the height.
     * @param roughness Higher value gives smoother height.
     * @param randRange Initial random range. Cannot be used to exactly control the height but more
     *                  of an indication.
     */
    public MpdHeightGenerator(int size, float roughness, float randRange) {
        mSize = size;
        mRoughness = roughness;
        mRandomRange = randRange;
        mPatchRowsCols = (1 << size);
        mPatchSideVertices = mPatchRowsCols + 1;
    }

    /**
     * Generate the height data.
     *
     * @param modifier The modifier.
     * @param random   The random to use.
     */
    public void generate(HeightModifier modifier, Random random) {
        float randRange = mRandomRange;

        initCorners(randRange, modifier, random);

        for (int iteration = 0; iteration < mSize; iteration++) {
            diamond(iteration, randRange, random, modifier);
            square(iteration, randRange, random, modifier);
            randRange *= Math.pow(2.0f, -mRoughness);
        }
    }

    /**
     * Initialize the 4 corners of the patch.
     *
     * @param randRange The current random range.
     * @param modifier  The height modifier.
     * @param random    The random.
     */
    private void initCorners(float randRange, HeightModifier modifier, Random random) {
        if (modifier.isModifiable(0, 0)) {
            modifier.set(0, 0, getRandomValue(randRange, random));
        } else {
            modifier.copyUnModifiable(0, 0);
        }

        if (modifier.isModifiable(0, mPatchRowsCols)) {
            modifier.set(0, mPatchRowsCols, getRandomValue(randRange, random));
        } else {
            modifier.copyUnModifiable(0, mPatchRowsCols);
        }

        if (modifier.isModifiable(mPatchRowsCols, 0)) {
            modifier.set(mPatchRowsCols, 0, getRandomValue(randRange, random));
        } else {
            modifier.copyUnModifiable(mPatchRowsCols, 0);
        }

        if (modifier.isModifiable(mPatchRowsCols, mPatchRowsCols)) {
            modifier.set(mPatchRowsCols, mPatchRowsCols, getRandomValue(randRange, random));
        } else {
            modifier.copyUnModifiable(mPatchRowsCols, mPatchRowsCols);
        }
    }

    /**
     * Create "diamonds (4 per each square)" structure by splitting the squares in the middle
     * and creating 4 diamond shapes. This is done by setting a value to the middle of each square.
     *
     * @param iteration The iteration number.
     * @param randRange The current random range.
     * @param random    The random.
     * @param modifier  The height modifier.
     */
    private void diamond(int iteration, float randRange, Random random, HeightModifier modifier) {
        final int stepSize = mPatchRowsCols >> iteration;
        final int stepSizeH = stepSize / 2;
        for (int z = 0; z < mPatchRowsCols; z += stepSize) {
            for (int x = 0; x < mPatchRowsCols; x += stepSize) {
                final float br = modifier.get(x, z);
                final float bl = modifier.get(x + stepSize, z);
                final float tr = modifier.get(x, z + stepSize);
                final float tl = modifier.get(x + stepSize, z + stepSize);
                final float value = getRandomValue(randRange, random) + (br + bl + tr + tl) / 4;
                modifier.set(x + stepSizeH, z + stepSizeH, value);
            }
        }
    }

    /**
     * Create a "square" structure out of each diamond. This is done by adding a new value
     * in the middle of the long-side of each diamond.
     *
     * @param iteration The iteration number.
     * @param randRange The current random range.
     * @param random    The random.
     * @param modifier  The height modifier.
     */
    private void square(int iteration, float randRange, Random random, HeightModifier modifier) {
        final int stepSize = mPatchRowsCols >> iteration;
        final int stepSizeH = stepSize / 2;
        boolean toggle = true;
        for (int z = 0; z < mPatchSideVertices; z += stepSizeH) {
            final boolean hasTop = z < (mPatchSideVertices - 1);
            final boolean hasBottom = z > 0;
            int x = toggle ? stepSizeH : 0;
            toggle = !toggle;
            for (; x < mPatchSideVertices; x += stepSize) {
                if (modifier.isModifiable(x, z)) {
                    final boolean hasRight = x > 0;
                    final boolean hasLeft = x < (mPatchSideVertices - 1);

                    int count = 0;
                    float value = 0;
                    if (hasRight) {
                        count++;
                        value += modifier.get(x - stepSizeH, z);
                    }
                    if (hasLeft) {
                        count++;
                        value += modifier.get(x + stepSizeH, z);
                    }
                    if (hasBottom) {
                        count++;
                        value += modifier.get(x, z - stepSizeH);
                    }
                    if (hasTop) {
                        count++;
                        value += modifier.get(x, z + stepSizeH);
                    }
                    modifier.set(x, z, (value / count) + getRandomValue(randRange, random));
                } else {
                    modifier.copyUnModifiable(x, z);
                }
            }
        }
    }

    /**
     * Get a random value.
     *
     * @return The random value.
     */
    private static float getRandomValue(float randRange, Random random) {
        return (((2 * randRange) * random.nextFloat()) - randRange);
    }
}

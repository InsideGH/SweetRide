package com.sweetlab.sweetride.demo.game.terrain.newtake;

import java.util.Random;

/**
 * Pseudo random terrain cell randoms. Same query guaranties the same random object.
 */
public class Randoms {
    /**
     * Quadrants of randoms seeds for dual pos values.
     */
    private final long[] mQuadrant1;

    /**
     * Quadrants of randoms seeds for one neg value.
     */
    private final long[] mQuadrant2;

    /**
     * Quadrants of randoms seeds for another neg value.
     */
    private final long[] mQuadrant3;

    /**
     * Quadrants of randoms seeds for both neg value.
     */
    private final long[] mQuadrant4;

    /**
     * Constructor.
     *
     * @param seed         The seed to use for generating random values.
     * @param circularSize The circular size, i.e wrap around.
     */
    public Randoms(long seed, int circularSize) {
        Random random = new Random(seed);
        mQuadrant1 = createRandoms(random, circularSize);
        mQuadrant2 = createRandoms(random, circularSize);
        mQuadrant3 = createRandoms(random, circularSize);
        mQuadrant4 = createRandoms(random, circularSize);
    }

    /**
     * Get the random for the specific terrain cell.
     *
     * @param z The cell x coordinate.
     * @param x The cell z coordinate.
     * @return The random.
     */
    public Random getRandom(int z, int x) {
        if (x >= 0 && z >= 0) {
            int pos = z + x;
            return new Random(mQuadrant1[pos % mQuadrant1.length]);
        } else if (x >= 0 && z < 0) {
            int pos = -z + x;
            return new Random(mQuadrant2[pos % mQuadrant2.length]);
        } else if (x < 0 && z >= 0) {
            int pos = z - x;
            return new Random(mQuadrant3[pos % mQuadrant3.length]);
        } else {
            int pos = -z - x;
            return new Random(mQuadrant4[pos % mQuadrant3.length]);
        }
    }

    /**
     * Create a array of random seeds using the base random.
     *
     * @param baseRandom Base random to generate seeds.
     * @param size       The size of the array.
     * @return The array of seeds.
     */
    private long[] createRandoms(Random baseRandom, int size) {
        long[] array = new long[size];
        for (int i = 0; i < size; i++) {
            array[i] = baseRandom.nextLong();
        }
        return array;
    }
}

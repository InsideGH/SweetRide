package com.sweetlab.sweetride.demo.game.terrain.height;

import android.util.Log;

import com.sweetlab.sweetride.attributedata.VertexData;

/**
 * Height data that can be modified and filled with values.
 */
public class PatchHeightData {
    /**
     * The vertex attribute size.
     */
    private static final int VERTEX_SIZE = 1;

    /**
     * Do not normalize vertex attribute data.
     */
    private static final boolean NORMALIZE = false;

    /**
     * The patch size.
     */
    private final int mSize;

    /**
     * The patch resolution along x and z axis (squarish).
     */
    private final int mResolution;

    /**
     * The patch data.
     */
    private final float[] mData;

    /**
     * Constructor.
     *
     * @param size The height data patch size.
     */
    public PatchHeightData(int size) {
        mSize = size;
        mResolution = (1 << size) + 1;
        mData = new float[mResolution * mResolution];
    }

    /**
     * Create vertex buffer from height.
     *
     * @return The vertex height buffer.
     */
    public VertexData createVertexData() {
        return new VertexData(mData, VERTEX_SIZE, NORMALIZE);
    }

    /**
     * Get the size.
     *
     * @return The size.
     */
    public int getSize() {
        return mSize;
    }

    /**
     * Get a value to the patch.
     *
     * @param x The x coordinate in patch space.
     * @param z The z coordinate in patch space.
     */
    public float get(int x, int z) {
        return mData[x + z * mResolution];
    }


    /**
     * Set a value to the patch.
     *
     * @param x     The x coordinate in patch space.
     * @param z     The z coordinate in patch space.
     * @param value The value.
     */
    public void set(int x, int z, float value) {
        mData[x + z * mResolution] = value;
    }

    /**
     * Add a value to the patch.
     *
     * @param x     The x coordinate in patch space.
     * @param z     The z coordinate in patch space.
     * @param value The value.
     */
    public void add(int x, int z, float value) {
        mData[x + z * mResolution] += value;
    }

    /**
     * Print the height to console log.
     */
    @SuppressWarnings("unused")
    public void printToLog() {
        for (int z = 0; z < mResolution; z++) {
            StringBuilder sb = new StringBuilder();
            for (int x = 0; x < mResolution; x++) {
                sb.append(String.format("% #06.4f ", get(x, z)));
            }
            Log.d("Peter100", sb.toString());
            Log.d("Peter100", "");
        }
    }
}

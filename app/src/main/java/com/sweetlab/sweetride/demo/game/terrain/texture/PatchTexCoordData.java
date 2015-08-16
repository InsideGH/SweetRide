package com.sweetlab.sweetride.demo.game.terrain.texture;

import com.sweetlab.sweetride.attributedata.TextureCoordData;

/**
 * Texture coordinates for to cover a patch.
 */
public class PatchTexCoordData {
    /**
     * The patch size.
     */
    private final int mSize;

    /**
     * The patch resolution along x and z axis (squarish).
     */
    private final int mResolution;

    /**
     * The texture coordinate data.
     */
    private final TextureCoordData mData;

    /**
     * Constructor. Create texture coordinates that are flipped around y axis to
     * take into account Android bitmap structure.
     * The origo is at the right and near point in the patch with x (s) axis
     * going to left and z (t) axis going from near to far.
     * <pre>
     *
     * This is the default OpenGL texture space.
     * 0,1          1,1
     *
     *  ^
     *  |
     * 0,0 --->     1,0
     *
     * This is the 'android' OpenGL texture space to avoid flipping bitmap.
     * 0,0 --->     1,0
     *  |
     *  *
     *
     * 0,1          1,1
     *
     * The patch vertices start maps to (1,1) and ends maps to (0,0).
     *
     * The parameters to this method are used as follows
     * sr-width,sn-depth --->                 1,sn-depth
     *  |
     *  *
     *
     * sr-width,1                  sr=(1-right),sn=(1-near)
     *
     * </pre>
     *
     * @param size  PatchCell size.
     * @param width Width in texture coordinate space (1 default).
     * @param depth Height in texture coordinate space (1 default).
     * @param right Right start texture coordinate (0 default)
     * @param near  Near start texture coordinate (0 default)
     */
    public PatchTexCoordData(int size, float width, float depth, float right, float near) {
        mSize = size;
        mResolution = (1 << size) + 1;

        final float stepX = width / (mResolution - 1);
        final float stepZ = depth / (mResolution - 1);


        final float startRight = 1.0f - right;
        final float startNear = 1.0f - near;

        float[] tex = new float[mResolution * mResolution * 2];
        int index = 0;
        for (int y = 0; y < mResolution; y++) {
            for (int x = 0; x < mResolution; x++) {
                tex[index++] = startRight - x * stepX;
                tex[index++] = startNear - y * stepZ;
            }
        }
        mData = new TextureCoordData(tex);
    }

    /**
     * Get the texture coordinates data.
     *
     * @return The texture coordinates data.
     */
    public TextureCoordData getData() {
        return mData;
    }
}

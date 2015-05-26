package com.sweetlab.sweetride.attributedata;

/**
 * Data to be used as color storage. Uses 4 components (rgba) and normalization set to false.
 */
public class ColorData extends VertexData {
    /**
     * Constructor. Create color data of size 4 and no normalization.
     *
     * @param data The data, will be copied.
     */
    public ColorData(float[] data) {
        super(data, 4, false);
    }
}

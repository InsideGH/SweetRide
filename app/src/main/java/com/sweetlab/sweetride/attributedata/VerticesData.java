package com.sweetlab.sweetride.attributedata;

/**
 * Data to be used as vertices storage. Uses 3 components (xyz) and normalization set to false.
 */
public class VerticesData extends VertexData {
    /**
     * Constructor. Create vertices data of vertex size 3 and no normalization.
     *
     * @param data The data, will be copied.
     */
    public VerticesData(float[] data) {
        super(data, 3, false);
    }
}

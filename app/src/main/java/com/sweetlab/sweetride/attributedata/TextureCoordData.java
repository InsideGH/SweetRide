package com.sweetlab.sweetride.attributedata;

/**
 * Data to be used as texture coordinates storage. Uses 2 components (st) and normalization set to
 * false.
 */
public class TextureCoordData extends VertexData {
    /**
     * Constructor. Create texture coordinate data of size 2 and no normalization.
     *
     * @param data The data, will be copied.
     */
    public TextureCoordData(float[] data) {
        super(data, 2, false);
    }
}

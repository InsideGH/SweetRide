package com.sweetlab.sweetride.demo.game.curve;

import com.sweetlab.sweetride.math.Matrix44;

/**
 * CatmullRom basis matrix.
 * <p/>
 * For theory, see CatmullRom.
 */
public class CatmullRomBasisMatrix extends Matrix44 {

    /**
     * Constructor. Creates a CatmullRom basis matrix.
     * <pre>
     * [ 0.0  0.0 -0.5  0.0    [1 0 -3 2    [0.0 -0.5  1.0 -0.5
     *   1.0  0 0  0.0 -0.5     0 0 3 -2     1.0  0.0 -2.5  1.5
     *   0.0  1.0  0.5  0.0  *  0 1 -2 1  =  0.0  0.5  2.0 -1.5  =  CatmullRom basis matrix.
     *   0.0  0.0  0.0  0.5]    0 0 -1 1]    0.0  0.0 -0.5  0.5]
     * </pre>
     */
    public CatmullRomBasisMatrix() {
        float[] values = new float[]{
                0.0f, 1.0f, 0.0f, 0.0f,
                -0.5f, 0.0f, 0.5f, 0.0f,
                1.0f, -2.5f, 2.0f, -0.5f,
                -0.5f, 1.5f, -1.5f, 0.5f
        };
        set(values);
    }
}

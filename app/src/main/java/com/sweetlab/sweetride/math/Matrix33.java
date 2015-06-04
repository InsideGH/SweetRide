package com.sweetlab.sweetride.math;


import android.annotation.SuppressLint;

/**
 * Old class that I have had around for a while. Should be cleaned up though.
 * <p/>
 * Matrix is holding the elements in a float array[16]. This is how the matrix
 * "looks" like, i.e. the positions. <br>
 * <br>
 * X Y Z <br>
 * 0 3 6 <br>
 * 1 4 7 <br>
 * 2 5 8 <br>
 * <br>
 * The X, Y, Z represents the directions of the different axises in our
 * coordinate system.
 */
public class Matrix33 {

    /**
     * The array holding matrix values
     */
    public final float[] m = new float[9];

    /**
     * Constructor creating uninitialized matrix.
     */
    public Matrix33() {
        setIdentity();
    }

    /**
     * Take the rotation part from a 4x4 matrix
     *
     * @param mat
     */
    public Matrix33(Matrix44 mat) {
        m[0] = mat.m[0];
        m[1] = mat.m[1];
        m[2] = mat.m[2];
        m[3] = mat.m[4];
        m[4] = mat.m[5];
        m[5] = mat.m[6];
        m[6] = mat.m[8];
        m[7] = mat.m[9];
        m[8] = mat.m[10];
    }

    /**
     * Set identity of this matrix
     */
    public void setIdentity() {
        m[1] = m[2] = m[3] = 0;
        m[5] = m[6] = m[7] = 0;

        m[0] = 1.0f; // x vector [0-2]
        m[4] = 1.0f; // y vector [3-5]
        m[8] = 1.0f; // z vector [6-8]
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String toString() {
        return String
                .format("[   X      Y      Z   ]\n" + "|% #06.2f % #06.2f % #06.2f |\n"
                                + "|% #06.2f % #06.2f % #06.2f |\n" + "|% #06.2f % #06.2f % #06.2f |\n"
                                + "-----------------------\n", m[0], m[3], m[6], m[1], m[4], m[7], m[2],
                        m[5], m[8]);
    }
}

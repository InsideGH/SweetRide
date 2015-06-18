package com.sweetlab.sweetride.math;


import android.annotation.SuppressLint;

/**
 * Old class that I have had around for a while. Should be cleaned up though.
 * <p/>
 * Matrix is holding the elements in a float array[16]. The translation part is
 * at position 12, 13, 14. This is how the matrix "looks" like, i.e. the
 * positions. <br>
 * <br>
 * X Y Z W <br>
 * 0 4 8 12 <br>
 * 1 5 9 13 <br>
 * 2 6 10 14 <br>
 * 3 7 11 15 <br>
 * <br>
 * The X, Y, Z represents the directions of the different axis in our coordinate
 * system. The W column represents the center of our coordinate system.
 */
public class Matrix44 {
    public final float[] m = new float[16];
    private final float[] tmpArray = new float[16];

    /**
     * Constructor creating uninitialized matrix.
     */
    public Matrix44() {
        setIdentity();
    }

    /**
     * Multiples matrices, prod = a * b. This is performing a post
     * multiplication of 'a' matrix with 'b' matrix (OpenGL style)
     *
     * @param prod - result
     * @param a    - left side
     * @param b    - right side
     */
    public static void mult(Matrix44 prod, final Matrix44 a, final Matrix44 b) {
        System.arraycopy(a.m, 0, prod.m, 0, a.m.length);
        for (int i = 0; i < 4; i++) {
            float ai0 = prod.m[i], ai1 = prod.m[4 + i], ai2 = prod.m[8 + i], ai3 = prod.m[12 + i];
            prod.m[i + 0] = ai0 * b.m[0] + ai1 * b.m[1] + ai2 * b.m[2] + ai3 * b.m[3];
            prod.m[i + 4] = ai0 * b.m[4] + ai1 * b.m[5] + ai2 * b.m[6] + ai3 * b.m[7];
            prod.m[i + 8] = ai0 * b.m[8] + ai1 * b.m[9] + ai2 * b.m[10] + ai3 * b.m[11];
            prod.m[i + 12] = ai0 * b.m[12] + ai1 * b.m[13] + ai2 * b.m[14] + ai3 * b.m[15];
        }
    }

    /**
     * Copies the array from src to this matrix.
     *
     * @param src
     * @return
     */
    public Matrix44 set(float[] src) {
        System.arraycopy(src, 0, m, 0, src.length);
        return this;
    }

    /**
     * Copies the array from src to this matrix.
     *
     * @param src
     */
    public void set(Matrix44 src) {
        System.arraycopy(src.m, 0, m, 0, src.m.length);
    }

    /**
     * Set identity of this matrix
     */
    public Matrix44 setIdentity() {
        m[1] = m[2] = m[3] = m[4] = 0;
        m[6] = m[7] = m[8] = m[9] = 0;
        m[11] = m[12] = m[13] = m[14] = 0;

        m[0] = 1.0f; // x vector [0-3]
        m[5] = 1.0f; // y vector [4-7]
        m[10] = 1.0f; // z vector [8-11]
        m[15] = 1.0f; // w vector [12,13,14,15]
        return this;
    }

    /**
     * Set the translation part [12,13,14] only in this matrix
     *
     * @param t - The translation part
     * @return
     */
    public Matrix44 setTranslate(Vec3 t) {
        m[12] = t.x;
        m[13] = t.y;
        m[14] = t.z;
        return this;
    }

    /**
     * Set the translation part [12,13,14] only in this matrix
     *
     * @return
     */
    public Matrix44 setTranslate(float x, float y, float z) {
        m[12] = x;
        m[13] = y;
        m[14] = z;
        return this;
    }

    /**
     * Makes this matrix a pure translation matrix. All previous values are
     * overwritten.
     *
     * @param x
     * @param y
     * @param z
     * @return this
     */
    public Matrix44 createTranslation(float x, float y, float z) {
        setIdentity();
        m[12] = x;
        m[13] = y;
        m[14] = z;
        return this;
    }

    /**
     * Makes this matrix a pure translation matrix. All previous values are
     * overwritten.
     *
     * @param trans
     * @return
     */
    public Matrix44 createTranslation(Vec3 trans) {
        setIdentity();
        m[12] = trans.x;
        m[13] = trans.y;
        m[14] = trans.z;
        return this;
    }

    /**
     * Translate this matrix by x,y,z.<br>
     * Equivalent to <b> mult(new Matrix44().setTranslate(x, y, z));
     *
     * @param x
     * @param y
     * @param z
     * @return this
     */
    public Matrix44 translate(float x, float y, float z) {
        m[12] = m[0] * x + m[4] * y + m[8] * z + m[12];
        m[13] = m[1] * x + m[5] * y + m[9] * z + m[13];
        m[14] = m[2] * x + m[6] * y + m[10] * z + m[14];
        return this;
    }

    /**
     * Translate this matrix by the values in trans vector.<br>
     * Equivalent to <b> mult(new Matrix44().setTranslate(x, y, z));
     *
     * @param trans
     * @return
     */
    public Matrix44 translate(Vec3 trans) {
        m[12] = m[0] * trans.x + m[4] * trans.y + m[8] * trans.z + m[12];
        m[13] = m[1] * trans.x + m[5] * trans.y + m[9] * trans.z + m[13];
        m[14] = m[2] * trans.x + m[6] * trans.y + m[10] * trans.z + m[14];
        return this;
    }

    /**
     * Makes this matrix a pure rotation matrix. All previous values are
     * overwritten.
     *
     * @param angle - in degrees
     * @param x
     * @param y
     * @param z
     * @return this
     */
    public Matrix44 setRotate(float angle, float x, float y, float z) {
        float mag, s, c;
        float xx, yy, zz, xy, yz, zx, xs, ys, zs, one_c;

        angle = (float) Math.toRadians(angle);

        s = (float) Math.sin(angle);
        c = (float) Math.cos(angle);

        mag = (float) Math.sqrt(x * x + y * y + z * z);

        // Identity matrix
        if (mag == 0.0f) {
            setIdentity();
            return this;
        }

        // Rotation matrix is normalized
        mag = 1 / mag;
        x *= mag;
        y *= mag;
        z *= mag;

        xx = x * x;
        yy = y * y;
        zz = z * z;
        xy = x * y;
        yz = y * z;
        zx = z * x;
        xs = x * s;
        ys = y * s;
        zs = z * s;
        one_c = 1.0f - c;

        m[0] = (one_c * xx) + c;
        m[4] = (one_c * xy) - zs;
        m[8] = (one_c * zx) + ys;
        m[12] = 0.0f;

        m[1] = (one_c * xy) + zs;
        m[5] = (one_c * yy) + c;
        m[9] = (one_c * yz) - xs;
        m[13] = 0.0f;

        m[2] = (one_c * zx) - ys;
        m[6] = (one_c * yz) + xs;
        m[10] = (one_c * zz) + c;
        m[14] = 0.0f;

        m[3] = 0.0f;
        m[7] = 0.0f;
        m[11] = 0.0f;
        m[15] = 1.0f;

        return this;
    }

    /**
     * Rotate this matrix by angle (radians) around axis x,y,z.
     *
     * @param angle - degrees
     * @param x
     * @param y
     * @param z
     * @return this
     */
    public Matrix44 rotate(float angle, float x, float y, float z) {
        float mag, s, c;
        float xx, yy, zz, xy, yz, zx, xs, ys, zs, one_c;

        angle = (float) Math.toRadians(angle);

        s = (float) Math.sin(angle);
        c = (float) Math.cos(angle);

        mag = (float) Math.sqrt(x * x + y * y + z * z);

        // Identity matrix
        if (mag == 0.0f) {
            setIdentity();
            return this;
        }

        // Rotation matrix is normalized
        mag = 1 / mag;
        x *= mag;
        y *= mag;
        z *= mag;

        xx = x * x;
        yy = y * y;
        zz = z * z;
        xy = x * y;
        yz = y * z;
        zx = z * x;
        xs = x * s;
        ys = y * s;
        zs = z * s;
        one_c = 1.0f - c;

        tmpArray[0] = (one_c * xx) + c;
        tmpArray[4] = (one_c * xy) - zs;
        tmpArray[8] = (one_c * zx) + ys;
        tmpArray[12] = 0.0f;

        tmpArray[1] = (one_c * xy) + zs;
        tmpArray[5] = (one_c * yy) + c;
        tmpArray[9] = (one_c * yz) - xs;
        tmpArray[13] = 0.0f;

        tmpArray[2] = (one_c * zx) - ys;
        tmpArray[6] = (one_c * yz) + xs;
        tmpArray[10] = (one_c * zz) + c;
        tmpArray[14] = 0.0f;

        tmpArray[3] = 0.0f;
        tmpArray[7] = 0.0f;
        tmpArray[11] = 0.0f;
        tmpArray[15] = 1.0f;

        for (int i = 0; i < 4; i++) {
            float ai0 = m[i], ai1 = m[4 + i], ai2 = m[8 + i], ai3 = m[12 + i];
            m[i + 0] = ai0 * tmpArray[0] + ai1 * tmpArray[1] + ai2 * tmpArray[2] + ai3
                    * tmpArray[3];
            m[i + 4] = ai0 * tmpArray[4] + ai1 * tmpArray[5] + ai2 * tmpArray[6] + ai3
                    * tmpArray[7];
            m[i + 8] = ai0 * tmpArray[8] + ai1 * tmpArray[9] + ai2 * tmpArray[10] + ai3
                    * tmpArray[11];
            m[i + 12] = ai0 * tmpArray[12] + ai1 * tmpArray[13] + ai2 * tmpArray[14] + ai3
                    * tmpArray[15];
        }

        return this;
    }

    /**
     * Makes this matrix a pure scale matrix. All previous values are
     * overwritten.
     *
     * @param x
     * @param y
     * @param z
     */
    public Matrix44 setScale(float x, float y, float z) {
        setIdentity();
        m[0] = x;
        m[5] = y;
        m[10] = z;
        return this;
    }

    /**
     * Scale this matrix.
     *
     * @param x
     * @param y
     * @param z
     * @return this
     */
    public Matrix44 scale(float x, float y, float z) {
        m[0] *= x;
        m[1] *= x;
        m[2] *= x;
        m[4] *= y;
        m[5] *= y;
        m[6] *= y;
        m[8] *= z;
        m[9] *= z;
        m[10] *= z;
        return this;
    }

    /**
     * Copies the 3x3 rotation part of this matrix into provided dst.
     *
     * @param dst - Destination 3x3 matrix.
     */
    public void getRotation(Matrix33 dst) {
        dst.m[0] = m[0];
        dst.m[1] = m[1];
        dst.m[2] = m[2];
        dst.m[3] = m[4];
        dst.m[4] = m[5];
        dst.m[5] = m[6];
        dst.m[6] = m[8];
        dst.m[7] = m[9];
        dst.m[8] = m[10];
    }

    /**
     * Sets the 3x3 rotation part of this matrix to the same values contained in
     * mat.
     *
     * @param mat
     * @return
     */
    public Matrix44 setRotation(Matrix33 mat) {
        m[0] = mat.m[0];
        m[1] = mat.m[1];
        m[2] = mat.m[2];
        m[4] = mat.m[3];
        m[5] = mat.m[4];
        m[6] = mat.m[5];
        m[8] = mat.m[6];
        m[9] = mat.m[7];
        m[10] = mat.m[8];
        return this;
    }

    /**
     * Writes the normal matrix into dst matrix. The normal matrix is the
     * transpose of this inverse.
     *
     * @param dst - The resulting normal matrix destination.
     */
    public void getNormal(Matrix33 dst) {
        float s0 = m[0] * m[5] - m[4] * m[1];
        float s1 = m[0] * m[9] - m[8] * m[1];
        float s2 = m[0] * m[13] - m[12] * m[1];
        float s3 = m[4] * m[9] - m[8] * m[5];
        float s4 = m[4] * m[13] - m[12] * m[5];
        float s5 = m[8] * m[13] - m[12] * m[9];

        float c0 = m[2] * m[7] - m[6] * m[3];
        float c1 = m[2] * m[11] - m[10] * m[3];
        float c2 = m[2] * m[15] - m[14] * m[3];
        float c3 = m[6] * m[11] - m[10] * m[7];
        float c4 = m[6] * m[15] - m[14] * m[7];
        float c5 = m[10] * m[15] - m[14] * m[11];

        float oneDivDet = s0 * c5 - s1 * c4 + s2 * c3 + s3 * c2 - s4 * c1 + s5 * c0;

        if (oneDivDet != 0) {
            oneDivDet = 1 / oneDivDet;
            dst.m[0] = (+m[5] * c5 - m[9] * c4 + m[13] * c3) * oneDivDet;
            dst.m[3] = (-m[1] * c5 + m[9] * c2 - m[13] * c1) * oneDivDet;
            dst.m[6] = (+m[1] * c4 - m[5] * c2 + m[13] * c0) * oneDivDet;

            dst.m[1] = (-m[4] * c5 + m[8] * c4 - m[12] * c3) * oneDivDet;
            dst.m[4] = (+m[0] * c5 - m[8] * c2 + m[12] * c1) * oneDivDet;
            dst.m[7] = (-m[0] * c4 + m[4] * c2 - m[12] * c0) * oneDivDet;

            dst.m[2] = (+m[7] * s5 - m[11] * s4 + m[15] * s3) * oneDivDet;
            dst.m[5] = (-m[3] * s5 + m[11] * s2 - m[15] * s1) * oneDivDet;
            dst.m[8] = (+m[3] * s4 - m[7] * s2 + m[15] * s0) * oneDivDet;
        }
    }

    /**
     * Sets the translation part of this matrix to dst vector.
     *
     * @return
     */
    public void get3x1Translation(Vec3 dst) {
        dst.set(m[12], m[13], m[14]);
    }

    /**
     * Set dst 4x4 matrix, with only the translation part of this matrix.
     *
     * @return
     */
    public void get4x4Translation(Matrix44 dst) {
        dst.createTranslation(m[12], m[13], m[14]);
    }

    /**
     * Set the X axis of this matrix (column)
     *
     * @param src
     * @return
     */
    public Matrix44 setXAxis(Vec3 src) {
        m[0] = src.x;
        m[1] = src.y;
        m[2] = src.z;
        return this;
    }

    /**
     * Set the X axis of this matrix (column)
     *
     * @param x
     * @param y
     * @param z
     * @return
     */
    public Matrix44 setXAxis(float x, float y, float z) {
        m[0] = x;
        m[1] = y;
        m[2] = z;
        return this;
    }

    /**
     * Set the Y axis of this matrix (column)
     *
     * @return
     */
    public Matrix44 setYAxis(Vec3 src) {
        m[4] = src.x;
        m[5] = src.y;
        m[6] = src.z;
        return this;
    }

    /**
     * Set the Y axis of this matrix (column)
     *
     * @param x
     * @param y
     * @param z
     * @return
     */
    public Matrix44 setYAxis(float x, float y, float z) {
        m[4] = x;
        m[5] = y;
        m[6] = z;
        return this;
    }

    /**
     * Set the Z axis of this matrix (column)
     *
     * @return
     */
    public Matrix44 setZAxis(Vec3 src) {
        setZAxis(src.x, src.y, src.z);
        return this;
    }

    /**
     * Set the Z axis of this matrix (column)
     *
     * @param x
     * @param y
     * @param z
     * @return
     */
    public Matrix44 setZAxis(float x, float y, float z) {
        m[8] = x;
        m[9] = y;
        m[10] = z;
        return this;
    }

    /**
     * Get the X axis from this matrix (column)
     *
     * @return
     */
    public void getXAxis(Vec3 dst) {
        dst.set(m[0], m[1], m[2]);
    }

    /**
     * Get the Y axis from this matrix (column)
     *
     * @return
     */
    public void getYAxis(Vec3 dst) {
        dst.set(m[4], m[5], m[6]);
    }

    /**
     * Get the Z axis from this matrix (column)
     *
     * @return
     */
    public void getZAxis(Vec3 dst) {
        dst.set(m[8], m[9], m[10]);
    }

    /**
     * Transpose this matrix
     *
     * @return
     */
    public Matrix44 transpose() {
        float tmp;
        tmp = m[1];
        m[1] = m[4];
        m[4] = tmp;

        tmp = m[2];
        m[2] = m[8];
        m[8] = tmp;

        tmp = m[3];
        m[3] = m[12];
        m[12] = tmp;

        tmp = m[6];
        m[6] = m[9];
        m[9] = tmp;

        tmp = m[7];
        m[7] = m[13];
        m[13] = tmp;

        tmp = m[11];
        m[11] = m[14];
        m[14] = tmp;
        return this;
    }

    /**
     * Based on "The Laplace Expansion Theorem: Computing the Determinants and
     * Inverses of Matrices" <br>
     * http://www.geometrictools.com/Documentation/LaplaceExpansionTheorem.pdf <br>
     * <br>
     * <p/>
     * <pre>
     * X Y Z W <br>
     * 0 4 8  12 <br>
     * 1 5 9  13 <br>
     * 2 6 10 14 <br>
     * 3 7 11 15 <br>
     * <br>
     * </pre>
     *
     * @return determinant
     */
    public float determinant() {
        float s0 = m[0] * m[5] - m[4] * m[1];
        float s1 = m[0] * m[9] - m[8] * m[1];
        float s2 = m[0] * m[13] - m[12] * m[1];
        float s3 = m[4] * m[9] - m[8] * m[5];
        float s4 = m[4] * m[13] - m[12] * m[5];
        float s5 = m[8] * m[13] - m[12] * m[9];

        float c0 = m[2] * m[7] - m[6] * m[3];
        float c1 = m[2] * m[11] - m[10] * m[3];
        float c2 = m[2] * m[15] - m[14] * m[3];
        float c3 = m[6] * m[11] - m[10] * m[7];
        float c4 = m[6] * m[15] - m[14] * m[7];
        float c5 = m[10] * m[15] - m[14] * m[11];

        return s0 * c5 - s1 * c4 + s2 * c3 + s3 * c2 - s4 * c1 + s5 * c0;
    }

    /**
     * Inverts this matrix.
     *
     * @return True if success
     */
    public boolean invert() {
        float s0 = m[0] * m[5] - m[4] * m[1];
        float s1 = m[0] * m[9] - m[8] * m[1];
        float s2 = m[0] * m[13] - m[12] * m[1];
        float s3 = m[4] * m[9] - m[8] * m[5];
        float s4 = m[4] * m[13] - m[12] * m[5];
        float s5 = m[8] * m[13] - m[12] * m[9];

        float c0 = m[2] * m[7] - m[6] * m[3];
        float c1 = m[2] * m[11] - m[10] * m[3];
        float c2 = m[2] * m[15] - m[14] * m[3];
        float c3 = m[6] * m[11] - m[10] * m[7];
        float c4 = m[6] * m[15] - m[14] * m[7];
        float c5 = m[10] * m[15] - m[14] * m[11];

        float oneDivDet = s0 * c5 - s1 * c4 + s2 * c3 + s3 * c2 - s4 * c1 + s5 * c0;

        if (oneDivDet != 0) {
            oneDivDet = 1 / oneDivDet;

            tmpArray[0] = (+m[5] * c5 - m[9] * c4 + m[13] * c3) * oneDivDet;
            tmpArray[1] = (-m[1] * c5 + m[9] * c2 - m[13] * c1) * oneDivDet;
            tmpArray[2] = (+m[1] * c4 - m[5] * c2 + m[13] * c0) * oneDivDet;
            tmpArray[3] = (-m[1] * c3 + m[5] * c1 - m[9] * c0) * oneDivDet;

            tmpArray[4] = (-m[4] * c5 + m[8] * c4 - m[12] * c3) * oneDivDet;
            tmpArray[5] = (+m[0] * c5 - m[8] * c2 + m[12] * c1) * oneDivDet;
            tmpArray[6] = (-m[0] * c4 + m[4] * c2 - m[12] * c0) * oneDivDet;
            tmpArray[7] = (+m[0] * c3 - m[4] * c1 + m[8] * c0) * oneDivDet;

            tmpArray[8] = (+m[7] * s5 - m[11] * s4 + m[15] * s3) * oneDivDet;
            tmpArray[9] = (-m[3] * s5 + m[11] * s2 - m[15] * s1) * oneDivDet;
            tmpArray[10] = (+m[3] * s4 - m[7] * s2 + m[15] * s0) * oneDivDet;
            tmpArray[11] = (-m[3] * s3 + m[7] * s1 - m[11] * s0) * oneDivDet;

            tmpArray[12] = (-m[6] * s5 + m[10] * s4 - m[14] * s3) * oneDivDet;
            tmpArray[13] = (+m[2] * s5 - m[10] * s2 + m[14] * s1) * oneDivDet;
            tmpArray[14] = (-m[2] * s4 + m[6] * s2 - m[14] * s0) * oneDivDet;
            tmpArray[15] = (+m[2] * s3 - m[6] * s1 + m[10] * s0) * oneDivDet;

            System.arraycopy(tmpArray, 0, m, 0, 16);
            return true;
        }
        return false;
    }

    /**
     * Multiplies this matrix with b matrix. This on left side and b on right
     * side. This is performing a post multiplication of this matrix with b
     * matrix (OpenGL style)
     *
     * @param b
     * @return This matrix
     */
    public Matrix44 mult(final Matrix44 b) {
        for (int i = 0; i < 4; i++) {
            float ai0 = m[i], ai1 = m[4 + i], ai2 = m[8 + i], ai3 = m[12 + i];
            m[i + 0] = ai0 * b.m[0] + ai1 * b.m[1] + ai2 * b.m[2] + ai3 * b.m[3];
            m[i + 4] = ai0 * b.m[4] + ai1 * b.m[5] + ai2 * b.m[6] + ai3 * b.m[7];
            m[i + 8] = ai0 * b.m[8] + ai1 * b.m[9] + ai2 * b.m[10] + ai3 * b.m[11];
            m[i + 12] = ai0 * b.m[12] + ai1 * b.m[13] + ai2 * b.m[14] + ai3 * b.m[15];
        }
        return this;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String toString() {
        return String.format("[   X      Y      Z      W   ]\n"
                        + "|% #06.4f % #06.4f % #06.4f % #06.4f |\n"
                        + "|% #06.4f % #06.4f % #06.4f % #06.4f |\n"
                        + "|% #06.4f % #06.4f % #06.4f % #06.4f |\n"
                        + "|% #06.4f % #06.4f % #06.4f % #06.4f |\n" + "------------------------------\n",
                m[0], m[4], m[8], m[12], m[1], m[5], m[9], m[13], m[2], m[6], m[10], m[14], m[3],
                m[7], m[11], m[15]);
    }
}
package com.sweetlab.sweetride.mesh;

import com.sweetlab.sweetride.math.Matrix44;

/**
 * A (aabb) bounding box.
 */
public class BoundingBox {
    /**
     * The min vector (x,y,z).
     */
    private float[] mMin = new float[3];

    /**
     * The max vector (x, y, z).
     */
    private float[] mMax = new float[3];

    /**
     * Create an empty bounding box.
     */
    public BoundingBox() {
        setEmpty();
    }

    /**
     * Expands the box by adding points to it.
     *
     * @param point Point to add.
     */
    public void addVertex(float[] point) {
        if (point[0] < mMin[0]) {
            mMin[0] = point[0];
        }
        if (point[0] > mMax[0]) {
            mMax[0] = point[0];
        }
        if (point[1] < mMin[1]) {
            mMin[1] = point[1];
        }
        if (point[1] > mMax[1]) {
            mMax[1] = point[1];
        }
        if (point[2] < mMin[2]) {
            mMin[2] = point[2];
        }
        if (point[2] > mMax[2]) {
            mMax[2] = point[2];
        }
    }

    /**
     * Expand the box by adding an array of 3 component vertices.
     *
     * @param vertices The array of vertices.
     */
    public void addVertices(float[] vertices) {
        if ((vertices.length % 3) != 0) {
            throw new RuntimeException("Provided vertices array length is not multiple of 3, len = " + vertices.length);
        }
        float[] tmp = new float[3];
        for (int i = 0; i < vertices.length; i += 3) {
            tmp[0] = vertices[i];
            tmp[1] = vertices[i + 1];
            tmp[2] = vertices[i + 2];
            addVertex(tmp);
        }
    }

    /**
     * Transform the bounding box and store result into provided bounding box.
     * <p/>
     * One way of doing this would be to transform the shape first and then recalculate
     * the box based on the transformed shape. This would be the most optimal from a box
     * size point of view but slow for large shapes.
     * <p/>
     * If the 8 corners of the box would have been stored, we could have transformed these and
     * then create a new box from these transformed points. This would lead to a larger box than
     * necessary though.
     * <p/>
     * A better way is to just store the min and max vector with (xMin, yMin, zMin) and (xMax,
     * yMax, zMax) elements. These are then used on coordination with the transformation matrix the
     * following way :
     * <p/>
     * p' = m * p <br>
     * <p/>
     * Here p would be any of vertices in the object. Need to figure out which of these
     * vertices would have the smallest or largest value after the transformation.
     * <p/>
     * This is the transformation matrix layout :
     * <p/>
     * X Y Z W <br>
     * 0 4 8 12 <br>
     * 1 5 9 13 <br>
     * 2 6 10 14 <br>
     * 3 7 11 15 <br>
     * <p/>
     * Lets assume that we calculate the x', y' and z' value for every vertex of the object
     * x[verticesCount]' = m0*x + m4*y + m8*z
     * y[verticesCount]' = m1*x + m5*y + m9*z
     * z[verticesCount]' = m2*x + m6*y + m10*z
     * after this we would need to expand the (emptied) box with these new x', y', z' values.
     * <p/>
     * Instead we could focus on the min/max points and directly optimize the matrix multiplication
     * to minimize and maximize the output from x', y' and z' by utilizing the sign of the matrix
     * elements.
     *
     * @param mat Transformation matrix.
     * @param dst Destination bounding box.
     */
    public void transform(Matrix44 mat, BoundingBox dst) {
        /**
         * Initialize with the translation part
         */
        dst.mMin[0] = dst.mMax[0] = mat.m[12];
        dst.mMin[1] = dst.mMax[1] = mat.m[13];
        dst.mMin[2] = dst.mMax[2] = mat.m[14];

        /**
         * Start minimizing and maximizing.
         */

        /** m0 * x */
        if (mat.m[0] > 0) {
            dst.mMin[0] += mat.m[0] * mMin[0];
            dst.mMax[0] += mat.m[0] * mMax[0];
        } else {
            dst.mMin[0] += mat.m[0] * mMax[0];
            dst.mMax[0] += mat.m[0] * mMin[0];
        }
        /** m4 * y */
        if (mat.m[4] > 0) {
            dst.mMin[0] += mat.m[4] * mMin[1];
            dst.mMax[0] += mat.m[4] * mMax[1];
        } else {
            dst.mMin[0] += mat.m[4] * mMax[1];
            dst.mMax[0] += mat.m[4] * mMin[1];
        }
        /** m8 * z */
        if (mat.m[8] > 0) {
            dst.mMin[0] += mat.m[8] * mMin[2];
            dst.mMax[0] += mat.m[8] * mMax[2];
        } else {
            dst.mMin[0] += mat.m[8] * mMax[2];
            dst.mMax[0] += mat.m[8] * mMin[2];
        }


        /** m1 * x */
        if (mat.m[1] > 0) {
            dst.mMin[1] += mat.m[1] * mMin[0];
            dst.mMax[1] += mat.m[1] * mMax[0];
        } else {
            dst.mMin[1] += mat.m[1] * mMax[0];
            dst.mMax[1] += mat.m[1] * mMin[0];
        }
        /** m5 * y */
        if (mat.m[5] > 0) {
            dst.mMin[1] += mat.m[5] * mMin[1];
            dst.mMax[1] += mat.m[5] * mMax[1];
        } else {
            dst.mMin[1] += mat.m[5] * mMax[1];
            dst.mMax[1] += mat.m[5] * mMin[1];
        }
        /** m9 * z */
        if (mat.m[9] > 0) {
            dst.mMin[1] += mat.m[9] * mMin[2];
            dst.mMax[1] += mat.m[9] * mMax[2];
        } else {
            dst.mMin[1] += mat.m[9] * mMax[2];
            dst.mMax[1] += mat.m[9] * mMin[2];
        }


        /** m2 * x */
        if (mat.m[2] > 0) {
            dst.mMin[2] += mat.m[2] * mMin[0];
            dst.mMax[2] += mat.m[2] * mMax[0];
        } else {
            dst.mMin[2] += mat.m[2] * mMax[0];
            dst.mMax[2] += mat.m[2] * mMin[0];
        }
        /** m6 * y */
        if (mat.m[6] > 0) {
            dst.mMin[2] += mat.m[6] * mMin[1];
            dst.mMax[2] += mat.m[6] * mMax[1];
        } else {
            dst.mMin[2] += mat.m[6] * mMax[1];
            dst.mMax[2] += mat.m[6] * mMin[1];
        }
        /** m10 * z */
        if (mat.m[10] > 0) {
            dst.mMin[2] += mat.m[10] * mMin[2];
            dst.mMax[2] += mat.m[10] * mMax[2];
        } else {
            dst.mMin[2] += mat.m[10] * mMax[2];
            dst.mMax[2] += mat.m[10] * mMin[2];
        }
    }

    /**
     * Reset the box.
     */
    public void setEmpty() {
        mMin[0] = mMin[1] = mMin[2] = Float.MAX_VALUE;
        mMax[0] = mMax[1] = mMax[2] = -Float.MAX_VALUE;
    }

    /**
     * The min point (x, y, z).
     *
     * @return The array by reference.
     */
    public float[] getMin() {
        return mMin;
    }

    /**
     * The max point (x, y, z).
     *
     * @return The array by reference.
     */
    public float[] getMax() {
        return mMax;
    }

    @Override
    public String toString() {
        return "Box min = " + mMin[0] + " " + mMin[1] + " " + mMin[2] + " max = " + mMax[0] + " " + mMax[1] + " " + mMax[2];
    }
}
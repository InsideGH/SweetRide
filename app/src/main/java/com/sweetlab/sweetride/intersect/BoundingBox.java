package com.sweetlab.sweetride.intersect;

import com.sweetlab.sweetride.math.Matrix44;
import com.sweetlab.sweetride.math.Vec3;

/**
 * A (aabb) bounding box.
 */
public class BoundingBox {
    /**
     * The min vector (x,y,z).
     */
    protected Vec3 mMin = new Vec3();

    /**
     * The max vector (x, y, z).
     */
    protected Vec3 mMax = new Vec3();

    /**
     * Create an empty bounding box.
     */
    public BoundingBox() {
        setEmpty();
    }

    /**
     * Create bounding box based of vertices.
     *
     * @param vertices The vertices.
     */
    public BoundingBox(float[] vertices) {
        setEmpty();
        addVertices(vertices);
    }

    /**
     * Create bounding box based on indices.
     *
     * @param vertices The vertices.
     * @param indices  The indices.
     */
    public BoundingBox(float[] vertices, short[] indices) {
        setEmpty();
        addVertices(vertices, indices);
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
     * @param dst Destination transformable bounding box.
     */
    public void transform(Matrix44 mat, TransformableBoundingBox dst) {
        /**
         * Initialize with the translation part
         */
        dst.mMin.x = dst.mMax.x = mat.m[12];
        dst.mMin.y = dst.mMax.y = mat.m[13];
        dst.mMin.z = dst.mMax.z = mat.m[14];

        /**
         * Start minimizing and maximizing.
         */

        /** m0 * x */
        if (mat.m[0] > 0) {
            dst.mMin.x += mat.m[0] * mMin.x;
            dst.mMax.x += mat.m[0] * mMax.x;
        } else {
            dst.mMin.x += mat.m[0] * mMax.x;
            dst.mMax.x += mat.m[0] * mMin.x;
        }
        /** m4 * y */
        if (mat.m[4] > 0) {
            dst.mMin.x += mat.m[4] * mMin.y;
            dst.mMax.x += mat.m[4] * mMax.y;
        } else {
            dst.mMin.x += mat.m[4] * mMax.y;
            dst.mMax.x += mat.m[4] * mMin.y;
        }
        /** m8 * z */
        if (mat.m[8] > 0) {
            dst.mMin.x += mat.m[8] * mMin.z;
            dst.mMax.x += mat.m[8] * mMax.z;
        } else {
            dst.mMin.x += mat.m[8] * mMax.z;
            dst.mMax.x += mat.m[8] * mMin.z;
        }


        /** m1 * x */
        if (mat.m[1] > 0) {
            dst.mMin.y += mat.m[1] * mMin.x;
            dst.mMax.y += mat.m[1] * mMax.x;
        } else {
            dst.mMin.y += mat.m[1] * mMax.x;
            dst.mMax.y += mat.m[1] * mMin.x;
        }
        /** m5 * y */
        if (mat.m[5] > 0) {
            dst.mMin.y += mat.m[5] * mMin.y;
            dst.mMax.y += mat.m[5] * mMax.y;
        } else {
            dst.mMin.y += mat.m[5] * mMax.y;
            dst.mMax.y += mat.m[5] * mMin.y;
        }
        /** m9 * z */
        if (mat.m[9] > 0) {
            dst.mMin.y += mat.m[9] * mMin.z;
            dst.mMax.y += mat.m[9] * mMax.z;
        } else {
            dst.mMin.y += mat.m[9] * mMax.z;
            dst.mMax.y += mat.m[9] * mMin.z;
        }


        /** m2 * x */
        if (mat.m[2] > 0) {
            dst.mMin.z += mat.m[2] * mMin.x;
            dst.mMax.z += mat.m[2] * mMax.x;
        } else {
            dst.mMin.z += mat.m[2] * mMax.x;
            dst.mMax.z += mat.m[2] * mMin.x;
        }
        /** m6 * y */
        if (mat.m[6] > 0) {
            dst.mMin.z += mat.m[6] * mMin.y;
            dst.mMax.z += mat.m[6] * mMax.y;
        } else {
            dst.mMin.z += mat.m[6] * mMax.y;
            dst.mMax.z += mat.m[6] * mMin.y;
        }
        /** m10 * z */
        if (mat.m[10] > 0) {
            dst.mMin.z += mat.m[10] * mMin.z;
            dst.mMax.z += mat.m[10] * mMax.z;
        } else {
            dst.mMin.z += mat.m[10] * mMax.z;
            dst.mMax.z += mat.m[10] * mMin.z;
        }
    }

    /**
     * Get the min point (x, y, z), stored into destination vector.
     *
     * @param dst The dst storage.
     */
    public void getMin(Vec3 dst) {
        dst.set(mMin);
    }

    /**
     * Get the max point (x, y, z), stored into destination vector.
     *
     * @param dst The dst storage.
     */
    public void getMax(Vec3 dst) {
        dst.set(mMax);
    }

    /**
     * Reset the box.
     */
    private void setEmpty() {
        mMin.set(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
        mMax.set(-Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE);
    }

    /**
     * Expand the box by adding an array of 3 component vertices.
     *
     * @param vertices The array of vertices.
     */
    private void addVertices(float[] vertices) {
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
     * Expand the box by adding an array of 3 component vertices selected by indices.
     *
     * @param vertices The vertices.
     * @param indices  The indices.
     */
    private void addVertices(float[] vertices, short[] indices) {
        float[] tmp = new float[3];
        for (short index : indices) {
            tmp[0] = vertices[index * 3];
            tmp[1] = vertices[index * 3 + 1];
            tmp[2] = vertices[index * 3 + 2];
            addVertex(tmp);
        }
    }

    /**
     * Expands the box by adding points to it.
     *
     * @param point Point to add.
     */
    private void addVertex(float[] point) {
        if (point[0] < mMin.x) {
            mMin.x = point[0];
        }
        if (point[0] > mMax.x) {
            mMax.x = point[0];
        }
        if (point[1] < mMin.y) {
            mMin.y = point[1];
        }
        if (point[1] > mMax.y) {
            mMax.y = point[1];
        }
        if (point[2] < mMin.z) {
            mMin.z = point[2];
        }
        if (point[2] > mMax.z) {
            mMax.z = point[2];
        }
    }

    @Override
    public String toString() {
        return "Box min = " + mMin.x + " " + mMin.y + " " + mMin.z + " max = " + mMax.x + " " + mMax.y + " " + mMax.z;
    }
}
package com.sweetlab.sweetride.intersect;

import android.annotation.SuppressLint;

import com.sweetlab.sweetride.action.Action;
import com.sweetlab.sweetride.action.ActionThread;
import com.sweetlab.sweetride.action.GlobalActionId;
import com.sweetlab.sweetride.action.NoHandleNotifier;
import com.sweetlab.sweetride.math.Matrix44;
import com.sweetlab.sweetride.math.Vec3;

/**
 * A (aabb) bounding box.
 */
public class BoundingBox extends NoHandleNotifier<GlobalActionId> {
    /**
     * Indices reference has changed.
     */
    private final Action<GlobalActionId> mBoxChanged = new Action<>(this, GlobalActionId.BOUNDING_BOX_UPDATE, ActionThread.MAIN);

    /**
     * The width vector.
     */
    private final Vec3 mWidthVec = new Vec3();

    /**
     * The height vector.
     */
    private final Vec3 mHeightVec = new Vec3();

    /**
     * The depth vector.
     */
    private final Vec3 mDepthVec = new Vec3();

    /**
     * The middle point.
     */
    private final Vec3 mMiddlePoint = new Vec3();

    /**
     * The min vector (x,y,z).
     */
    private final Vec3 mMin = new Vec3();

    /**
     * The max vector (x, y, z).
     */
    private final Vec3 mMax = new Vec3();

    /**
     * Temporary min vector.
     */
    private final Vec3 mMinTmp = new Vec3();

    /**
     * Temporary max vector.
     */
    private final Vec3 mMaxTmp = new Vec3();

    /**
     * Is box empty?
     */
    private boolean mIsEmpty;

    /**
     * Create an empty box.
     */
    public BoundingBox() {
        resetMinMax();
        /**
         * No need to set vectors, they are empty by default.
         */
        mIsEmpty = true;
    }

    /**
     * Create bounding box based of vertices.
     *
     * @param vertices The vertices.
     */
    public BoundingBox(float[] vertices) {
        resetMinMax();
        addVertices(vertices);
        setVectors(mMin, mMax);
        mIsEmpty = false;
    }

    /**
     * Create bounding box based on indices.
     *
     * @param vertices The vertices.
     * @param indices  The indices.
     */
    public BoundingBox(float[] vertices, short[] indices) {
        resetMinMax();
        addVertices(vertices, indices);
        setVectors(mMin, mMax);
        mIsEmpty = false;
    }

    /**
     * Create a bounding box with given size.
     *
     * @param min The min xyz values.
     * @param max The max xyz values.
     */
    public BoundingBox(Vec3 min, Vec3 max) {
        mMin.set(min);
        mMax.set(max);
        setVectors(mMin, mMax);
        mIsEmpty = false;
    }

    /**
     * Set bounding box from other box.
     *
     * @param src The source box.
     */
    public void set(BoundingBox src) {
        mMin.set(src.mMin);
        mMax.set(src.mMax);
        setVectors(mMin, mMax);
        mIsEmpty = src.mIsEmpty;
        addAction(mBoxChanged);
    }

    @Override
    public boolean handleAction(Action<GlobalActionId> action) {
        switch (action.getType()) {
            case BOUNDING_BOX_UPDATE:
                return true;
            default:
                return false;
        }
    }

    /**
     * Check if empty.
     *
     * @return True if empty.
     */
    public boolean isEmpty() {
        return mIsEmpty;
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
     */
    public void transform(Matrix44 mat) {
        /**
         * Initialize with the translation part
         */
        mMinTmp.set(mat.m[12], mat.m[13], mat.m[14]);
        mMaxTmp.set(mat.m[12], mat.m[13], mat.m[14]);

        /**
         * Start minimizing and maximizing.
         */
        /** m0 * x */
        if (mat.m[0] > 0) {
            mMinTmp.x += mat.m[0] * mMin.x;
            mMaxTmp.x += mat.m[0] * mMax.x;
        } else {
            mMinTmp.x += mat.m[0] * mMax.x;
            mMaxTmp.x += mat.m[0] * mMin.x;
        }
        /** m4 * y */
        if (mat.m[4] > 0) {
            mMinTmp.x += mat.m[4] * mMin.y;
            mMaxTmp.x += mat.m[4] * mMax.y;
        } else {
            mMinTmp.x += mat.m[4] * mMax.y;
            mMaxTmp.x += mat.m[4] * mMin.y;
        }
        /** m8 * z */
        if (mat.m[8] > 0) {
            mMinTmp.x += mat.m[8] * mMin.z;
            mMaxTmp.x += mat.m[8] * mMax.z;
        } else {
            mMinTmp.x += mat.m[8] * mMax.z;
            mMaxTmp.x += mat.m[8] * mMin.z;
        }


        /** m1 * x */
        if (mat.m[1] > 0) {
            mMinTmp.y += mat.m[1] * mMin.x;
            mMaxTmp.y += mat.m[1] * mMax.x;
        } else {
            mMinTmp.y += mat.m[1] * mMax.x;
            mMaxTmp.y += mat.m[1] * mMin.x;
        }
        /** m5 * y */
        if (mat.m[5] > 0) {
            mMinTmp.y += mat.m[5] * mMin.y;
            mMaxTmp.y += mat.m[5] * mMax.y;
        } else {
            mMinTmp.y += mat.m[5] * mMax.y;
            mMaxTmp.y += mat.m[5] * mMin.y;
        }
        /** m9 * z */
        if (mat.m[9] > 0) {
            mMinTmp.y += mat.m[9] * mMin.z;
            mMaxTmp.y += mat.m[9] * mMax.z;
        } else {
            mMinTmp.y += mat.m[9] * mMax.z;
            mMaxTmp.y += mat.m[9] * mMin.z;
        }


        /** m2 * x */
        if (mat.m[2] > 0) {
            mMinTmp.z += mat.m[2] * mMin.x;
            mMaxTmp.z += mat.m[2] * mMax.x;
        } else {
            mMinTmp.z += mat.m[2] * mMax.x;
            mMaxTmp.z += mat.m[2] * mMin.x;
        }
        /** m6 * y */
        if (mat.m[6] > 0) {
            mMinTmp.z += mat.m[6] * mMin.y;
            mMaxTmp.z += mat.m[6] * mMax.y;
        } else {
            mMinTmp.z += mat.m[6] * mMax.y;
            mMaxTmp.z += mat.m[6] * mMin.y;
        }
        /** m10 * z */
        if (mat.m[10] > 0) {
            mMinTmp.z += mat.m[10] * mMin.z;
            mMaxTmp.z += mat.m[10] * mMax.z;
        } else {
            mMinTmp.z += mat.m[10] * mMax.z;
            mMaxTmp.z += mat.m[10] * mMin.z;
        }

        mMin.set(mMinTmp);
        mMax.set(mMaxTmp);

        setVectors(mMin, mMax);
        addAction(mBoxChanged);
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
     * Get the width vector.
     *
     * @param dst The dst storage.
     */
    public void getWidthVec(Vec3 dst) {
        dst.set(mWidthVec);
    }

    /**
     * Get the height vector.
     *
     * @param dst The dst storage.
     */
    public void getHeightVec(Vec3 dst) {
        dst.set(mHeightVec);
    }

    /**
     * Get the depth vector.
     *
     * @param dst The dst storage.
     */
    public void getDepthVec(Vec3 dst) {
        dst.set(mDepthVec);
    }

    /**
     * Get the middle point.
     *
     * @param dst The dst storage.
     */
    public void getMiddlePoint(Vec3 dst) {
        dst.set(mMiddlePoint);
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
            expand(tmp);
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
            expand(tmp);
        }
    }

    /**
     * Expands the box by adding points to it.
     *
     * @param point Point to add.
     */
    private void expand(float[] point) {
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

    /**
     * Reset min max.
     */
    private void resetMinMax() {
        mMin.set(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
        mMax.set(-Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE);
    }

    /**
     * Set the vectors based on min and max values.
     *
     * @param min The box min values.
     * @param max The box max values.
     */
    private void setVectors(Vec3 min, Vec3 max) {
        float width = max.x - min.x;
        float height = max.y - min.y;
        float depth = max.z - min.z;

        mWidthVec.set(1, 0, 0).mult(width);
        mHeightVec.set(0, 1, 0).mult(height);
        mDepthVec.set(0, 0, 1).mult(depth);

        mMiddlePoint.set(min);
        mMiddlePoint.add(width / 2, height / 2, depth / 2);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String toString() {
        return String.format("% #06.2f % #06.2f % #06.2f , % #06.2f % #06.2f % #06.2f",
                mMin.x, mMin.y, mMin.z, mMax.x, mMax.y, mMax.z);
    }
}
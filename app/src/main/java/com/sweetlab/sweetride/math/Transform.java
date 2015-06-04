package com.sweetlab.sweetride.math;

import com.sweetlab.sweetride.action.Action;
import com.sweetlab.sweetride.action.ActionId;
import com.sweetlab.sweetride.action.ActionNotifier;
import com.sweetlab.sweetride.action.HandleThread;
import com.sweetlab.sweetride.context.BackendContext;

/**
 * Transform backed by {@link Matrix44} matrix.
 */
public class Transform extends ActionNotifier {

    /**
     * Transform has been updated.
     */
    private Action mTransformChanged = new Action(this, ActionId.TRANSFORM_UPDATED, HandleThread.MAIN);

    /**
     * The matrix
     */
    private Matrix44 mMatrix;

    /**
     * Constructor initializing this transform to identity.
     */
    public Transform() {
        mMatrix = new Matrix44();
    }

    @Override
    public void handleAction(Action action) {
        throw new RuntimeException("wtf");
    }

    @Override
    public void handleAction(BackendContext context, Action action) {
        throw new RuntimeException("wtf");
    }

    /**
     * Set identity to this transform.
     *
     * @return This transform
     */
    public Transform setIdentity() {
        mMatrix.setIdentity();
        addAction(mTransformChanged);
        return this;
    }

    /**
     * Set this transform to be a copy of source transform.
     *
     * @param sourceTransform
     */
    public void set(Transform sourceTransform) {
        mMatrix.set(sourceTransform.getMatrix());
        addAction(mTransformChanged);
    }

    /**
     * Set this transform to be a copy of the source matrix.
     *
     * @param srcMatrix
     */
    public void set(Matrix44 srcMatrix) {
        mMatrix.set(srcMatrix);
        addAction(mTransformChanged);
    }

    /**
     * Set translation to this transform.
     *
     * @param x
     * @param y
     * @param z
     */
    public void setTranslate(float x, float y, float z) {
        mMatrix.setTranslate(x, y, z);
        addAction(mTransformChanged);
    }

    /**
     * Apply translation.
     *
     * @param x
     * @param y
     * @param z
     */
    public void translate(float x, float y, float z) {
        mMatrix.translate(x, y, z);
        addAction(mTransformChanged);
    }

    /**
     * Set scale to this transform.
     *
     * @param x
     * @param y
     * @param z
     */
    public void setScale(float x, float y, float z) {
        mMatrix.setScale(x, y, z);
        addAction(mTransformChanged);
    }

    /**
     * Apply scale to this transform.
     *
     * @param x
     * @param y
     * @param z
     */
    public void scale(float x, float y, float z) {
        mMatrix.scale(x, y, z);
        addAction(mTransformChanged);
    }

    /**
     * Set rotation to this transform.
     *
     * @param x
     * @param y
     * @param z
     */
    public void setRotate(float angle, float x, float y, float z) {
        mMatrix.setRotate(angle, x, y, z);
        addAction(mTransformChanged);
    }

    /**
     * Apply rotation to this transform.
     *
     * @param x
     * @param y
     * @param z
     */
    public void rotate(float angle, float x, float y, float z) {
        mMatrix.rotate(angle, x, y, z);
        addAction(mTransformChanged);
    }

    /**
     * Multiples transforms, product = a * b. This is performing a post
     * multiplication of 'a' matrix with 'b' matrix (OpenGL style). The result
     * is stored in this transform.
     *
     * @param a - Left transform
     * @param b - Right transform
     */
    public void combine(Transform a, Transform b) {
        Matrix44.mult(mMatrix, a.getMatrix(), b.getMatrix());
        addAction(mTransformChanged);
    }

    /**
     * Multiples matrices, product = a * b. This is performing a post
     * multiplication of 'a' matrix with 'b' matrix (OpenGL style). The result
     * is stored in this transform.
     *
     * @param a - Left transform
     * @param b - Right transform
     */
    public void combine(Matrix44 a, Matrix44 b) {
        Matrix44.mult(mMatrix, a, b);
        addAction(mTransformChanged);
    }

    /**
     * Get the matrix of this transform. NOTE, it's a reference, handle with
     * care, i.e do not change it.
     *
     * @return - This transforms backing matrix.
     */
    public Matrix44 getMatrix() {
        return mMatrix;
    }

    /**
     * Write this transforms normal matrix into provided destination matrix.
     * This is the transpose of the inverse.
     */
    public void getNormal(Matrix33 dst) {
        mMatrix.getNormal(dst);
    }
}

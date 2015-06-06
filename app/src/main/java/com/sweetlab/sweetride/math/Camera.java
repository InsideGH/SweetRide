package com.sweetlab.sweetride.math;

import com.sweetlab.sweetride.action.Action;
import com.sweetlab.sweetride.action.ActionId;
import com.sweetlab.sweetride.action.HandleThread;
import com.sweetlab.sweetride.action.NoHandleNotifier;

/**
 * Camera. Three axis camera with a frustrum.
 * <p/>
 * Old class that I have had around for a while. Should be cleaned up though.
 */
public class Camera extends NoHandleNotifier {
    /**
     * Camera has been updated.
     */
    private Action mCameraUpdated = new Action(this, ActionId.CAMERA_UPDATED, HandleThread.MAIN);

    /**
     * World up is defined as this.
     */
    public static final Vec3 WORLD_UP = new Vec3(0, 1, 0);

    /**
     * Position in world.
     */
    private final Vec3 mPos = new Vec3(0, 0, 0);

    /**
     * Right vector.
     */
    private final Vec3 mRight = new Vec3(1, 0, 0);

    /**
     * Up vector.
     */
    private final Vec3 mUp = new Vec3(0, 1, 0);

    /**
     * Look vector.
     */
    private final Vec3 mLook = new Vec3(0, 0, -1);

    /**
     * The camera matrix, i.e the view matrix.
     */
    private final Matrix44 mViewMat = new Matrix44();

    /**
     * The inverted camera matrix, i.e the inverted view matrix.
     */
    private final Matrix44 mInvViewMat = new Matrix44();

    /**
     * The combination of the view and projection matrix.
     */
    private final Matrix44 mViewProjectionMat = new Matrix44();

    /**
     * The inverse of the combination of the view and projection matrix.
     */
    private final Matrix44 mInvViewProjectionMat = new Matrix44();

    /**
     * The frustrum.
     */
    private final Frustrum mFrustrum = new Frustrum();

    /**
     * Temporary vector.
     */
    private final Vec3 mTempVec = new Vec3();

    /**
     * Constructor.
     */
    public Camera() {
        connectNotifier(mFrustrum);
    }

    @Override
    protected void onActionAdded(Action action) {
        super.onActionAdded(action);
        switch (action.getType()) {
            case CAMERA_UPDATED:
            case FRUSTRUM_UPDATED:
                updateMatrices();
                break;
            default:
                break;
        }
    }

    /**
     * Look at.
     *
     * @param eye  Where the camera is positioned.
     * @param look The point to look at.
     */
    public void lookAt(Vec3 eye, Vec3 look) {
        lookAt(eye.x, eye.y, eye.z, look.x, look.y, look.z);
    }

    /**
     * Look at.
     *
     * @param eyeX    The world x position of the camera.
     * @param eyeY    The world y position of the camera.
     * @param eyeZ    The world z position of the camera.
     * @param lookAtX The world x position to look at.
     * @param lookAtY The world y position to look at.
     * @param lookAtZ The world x position to look at.
     */
    public void lookAt(float eyeX, float eyeY, float eyeZ, float lookAtX, float lookAtY, float lookAtZ) {
        /**
         * Set the position.
         */
        mPos.set(eyeX, eyeY, eyeZ);

        /**
         * Calculate the look vector from the camera position and point defined to look at.
         */
        mTempVec.set(lookAtX, lookAtY, lookAtZ);
        Vec3.sub(mTempVec, mPos, mLook);
        mLook.norm();

        /**
         * Calculate the right vector using cross between known world up and look.
         */
        Vec3.cross(WORLD_UP, mLook, mRight);
        mRight.norm();

        /**
         * Calculate up by using the known look and right vector.
         */
        Vec3.cross(mLook, mRight, mUp);
        mUp.norm();

        /**
         * Clear view matrix.
         */
        mViewMat.setIdentity();

        /**
         * Set the three axises in the matrix. Set the rotation parts of the camera matrix.
         * Note that we are negating the camera look vector since we don't want to apply a
         * negation to the rotation matrix.
         */
        mViewMat.setXAxis(mRight);
        mViewMat.setYAxis(mUp);
        mViewMat.setZAxis(-mLook.x, -mLook.y, -mLook.z);

        /**
         * Having orthogonal axises we can transpose instead of invert.
         */
        mViewMat.transpose();

        /**
         * Apply translation to the camera matrix. Note that we are negating it
         * since we want to translate world with the inverse.
         */
        mViewMat.translate(-mPos.x, -mPos.y, -mPos.z);

        /**
         * Add action that camera has been updated.
         */
        addAction(mCameraUpdated);
    }

    /**
     * Set identity. All matrices are reset.
     */
    public void setIdentity() {
        mViewMat.setIdentity();
        addAction(mCameraUpdated);
    }

    /**
     * Get the view matrix by reference.
     *
     * @return The view matrix.
     */
    public Matrix44 getViewMatrix() {
        return mViewMat;
    }

    /**
     * Get the inverted view matrix by reference.
     *
     * @return The inverted view matrix.
     */
    public Matrix44 getInvViewMatrix() {
        return mInvViewMat;
    }

    /**
     * Get the combined view and projection matrix by reference.
     *
     * @return The combined view and project matrix.
     */
    public Matrix44 getViewProjectionMatrix() {
        return mViewProjectionMat;
    }

    /**
     * Get the inverted combined view and projection matrix by reference.
     *
     * @return The combined view and projection matrix.
     */
    public Matrix44 getInvViewProjectionMatrix() {
        return mInvViewProjectionMat;
    }

    /**
     * Get the frustrum.
     *
     * @return The frustrum
     */
    public Frustrum getFrustrum() {
        return mFrustrum;
    }

    /**
     * Get camera position.
     *
     * @return The position in world.
     */
    public Vec3 getPosition() {
        return mPos;
    }

    /**
     * Updates all supported matrices.
     */
    protected void updateMatrices() {
        mInvViewMat.set(mViewMat);
        mInvViewMat.invert();

        Matrix44 projectionMatrix = mFrustrum.getProjectionMatrix();
        Matrix44.mult(mViewProjectionMat, projectionMatrix, mViewMat);

        Matrix44 invProjectionMatrix = mFrustrum.getInvProjectionMatrix();
        Matrix44.mult(mInvViewProjectionMat, mInvViewMat, invProjectionMatrix);
    }
}

package com.sweetlab.sweetride.camera;

import com.sweetlab.sweetride.action.Action;
import com.sweetlab.sweetride.action.ActionId;
import com.sweetlab.sweetride.action.ActionThread;
import com.sweetlab.sweetride.action.NoHandleNotifier;
import com.sweetlab.sweetride.intersect.Plane;
import com.sweetlab.sweetride.math.Matrix44;
import com.sweetlab.sweetride.math.Transform;
import com.sweetlab.sweetride.math.Vec3;

/**
 * Camera. Three axis camera with a frustrum.
 * <p/>
 * Old class that I have had around for a while. Should be cleaned up though.
 */
public class Camera extends NoHandleNotifier {
    /**
     * World up is defined as this.
     */
    public static final Vec3 WORLD_UP = new Vec3(0, 1, 0);

    /**
     * The default up vector.
     */
    public static final Vec3 sDefaultUp = new Vec3(0, 1, 0);

    /**
     * The default look vector.
     */
    public static final Vec3 sDefaultLook = new Vec3(0, 0, -1);

    /**
     * The default right vector.
     */
    public static final Vec3 sDefaultRight = new Vec3(1, 0, 0);

    /**
     * The default pos.
     */
    public static final Vec3 sDefaultPos = new Vec3(0, 0, 0);

    /**
     * Camera has been updated.
     */
    private Action mCameraUpdated = new Action(this, ActionId.CAMERA_UPDATED, ActionThread.MAIN);

    /**
     * Position in world.
     */
    private final Vec3 mPos = new Vec3(sDefaultPos);

    /**
     * Right vector.
     */
    private final Vec3 mRight = new Vec3(sDefaultRight);

    /**
     * Up vector.
     */
    private final Vec3 mUp = new Vec3(sDefaultUp);

    /**
     * Look vector.
     */
    private final Vec3 mLook = new Vec3(sDefaultLook);

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
     * Temporary vector3.
     */
    private final Vec3 mTmpVec3 = new Vec3();

    /**
     * The frustrum planes.
     */
    private FrustrumPlanes mFrustrumPlanes = new FrustrumPlanes();

    /**
     * Constructor.
     */
    public Camera() {
        connectNotifier(mFrustrum);
    }

    @Override
    public boolean handleAction(Action action) {
        if ((action.getType().equals(ActionId.CAMERA_UPDATED))) {
            return true;
        }
        return super.handleAction(action);
    }

    @Override
    protected void onActionAdded(Action action) {
        super.onActionAdded(action);
        switch (action.getType()) {
            case CAMERA_UPDATED:
            case FRUSTRUM_UPDATED:
                updateMatrices();
                mFrustrumPlanes.update(this);
                break;
            default:
                break;
        }
    }

    /**
     * Apply a transform to the camera.
     *
     * @param parent The parent transform.
     */
    public void applyTransform(Transform parent) {
        Matrix44 matrix = parent.getMatrix();
        /**
         * Transform the position.
         */
        mPos.x += matrix.m[12];
        mPos.y += matrix.m[13];
        mPos.z += matrix.m[14];

        /**
         * Transform the right.
         */
        mRight.transform33(matrix);
        mRight.norm();

        /**
         * Transform the look.
         */
        mLook.transform33(matrix);
        mLook.norm();

        /**
         * Transform the up.
         */
        mUp.transform33(matrix);
        mUp.norm();

        /**
         * Update camera.
         */
        updateCamera();
    }

    /**
     * Set camera look at.
     *
     * @param eye    Where the camera is positioned in world space.
     * @param lookAt The point to look at in world space.
     */
    public void lookAt(Vec3 eye, Vec3 lookAt) {
        lookAt(eye.x, eye.y, eye.z, lookAt.x, lookAt.y, lookAt.z);
    }

    /**
     * Set camera look at.
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
        mTmpVec3.set(lookAtX, lookAtY, lookAtZ);
        Vec3.sub(mTmpVec3, mPos, mLook);
        mLook.norm();

        /**
         * Calculate the right vector using cross between known world up and look.
         * This calculation can lead to gimbal lock resulting in a right vector being
         * zero which then makes up vector zero as well.
         */
        Vec3.cross(mLook, WORLD_UP, mRight);
        mRight.norm();

        /**
         * Calculate up by using the known look and right vector.
         */
        Vec3.cross(mRight, mLook, mUp);
        mUp.norm();

        /**
         * Update camera.
         */
        updateCamera();
    }

    /**
     * Set identity. All matrices are reset.
     */
    public void setIdentity() {
        mPos.set(sDefaultPos);
        mRight.set(sDefaultRight);
        mLook.set(sDefaultLook);
        mUp.set(sDefaultUp);
        updateCamera();
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
     * @param dst where to store look vector.
     */
    public void getPosition(Vec3 dst) {
        dst.set(mPos);
    }

    /**
     * Get camera look vector in world space.
     *
     * @param dst where to store look vector.
     */
    public void getLook(Vec3 dst) {
        dst.set(mLook);
    }

    /**
     * Get camera right vector in world space.
     *
     * @param dst where to store look vector.
     */
    public void getRight(Vec3 dst) {
        dst.set(mRight);
    }

    /**
     * Get camera up vector in world space.
     *
     * @param dst where to store look vector.
     */
    public void getUp(Vec3 dst) {
        dst.set(mUp);
    }

    /**
     * Get the near plane in world space.
     *
     * @return The near plane in world space.
     */
    public Plane getNearPlane() {
        return mFrustrumPlanes.getNearPlane();
    }

    /**
     * Get the far plane in world space.
     *
     * @return The far plane in world space.
     */
    public Plane getFarPlane() {
        return mFrustrumPlanes.getFarPlane();
    }

    /**
     * Get the top plan in world space.
     *
     * @return The top plan in world space.
     */
    public Plane getTopPlane() {
        return mFrustrumPlanes.getTopPlane();
    }

    /**
     * Get the bottom plan in world space.
     *
     * @return The top plan in world space.
     */
    public Plane getBottomPlane() {
        return mFrustrumPlanes.getBottomPlane();
    }

    /**
     * Get the right plan in world space.
     *
     * @return The right plan in world space.
     */
    public Plane getRightPlane() {
        return mFrustrumPlanes.getRightPlane();
    }

    /**
     * Get the left plan in world space.
     *
     * @return The left plane in world space.
     */
    public Plane getLeftPlane() {
        return mFrustrumPlanes.getLeftPlane();
    }

    /**
     * Update camera.
     */
    private void updateCamera() {
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
     * Updates all supported matrices.
     */
    private void updateMatrices() {
        /**
         * Update inverted view matrix.
         */
        mInvViewMat.set(mViewMat);
        mInvViewMat.invert();

        /**
         * Update view projection matrix.
         */
        Matrix44.mult(mViewProjectionMat, mFrustrum.getProjectionMatrix(), mViewMat);

        /**
         * Update inverted view projection matrix.
         */
        Matrix44.mult(mInvViewProjectionMat, mInvViewMat, mFrustrum.getInvProjectionMatrix());
    }
}

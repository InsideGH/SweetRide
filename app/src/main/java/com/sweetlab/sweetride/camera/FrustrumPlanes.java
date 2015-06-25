package com.sweetlab.sweetride.camera;

import com.sweetlab.sweetride.intersect.Plane;
import com.sweetlab.sweetride.math.Vec3;

/**
 * Camera frustrum planes in world space.
 */
public class FrustrumPlanes {
    /**
     * Plane normal.
     */
    private Vec3 mNormal = new Vec3();

    /**
     * Plane point.
     */
    private Vec3 mPoint = new Vec3();

    /**
     * Near field center.
     */
    private Vec3 mCenterVec = new Vec3();

    /**
     * Near field top/bottom/left/right.
     */
    private Vec3 mSideVec = new Vec3();

    /**
     * Near field offset to top/bottom/left/right.
     */
    private Vec3 mOffsetVec = new Vec3();

    /**
     * The look vector.
     */
    private Vec3 mLookVec = new Vec3();

    /**
     * The right vector.
     */
    private Vec3 mRightVec = new Vec3();

    /**
     * The up vector.
     */
    private Vec3 mUpVec = new Vec3();

    /**
     * The position.
     */
    private Vec3 mPos = new Vec3();

    /**
     * The near plane in world space.
     */
    private Plane mNearPlane;

    /**
     * The far plane in world space.
     */
    private Plane mFarPlane;

    /**
     * The top plane in world space.
     */
    private Plane mTopPlane;

    /**
     * The bottom plane in world space.
     */
    private Plane mBottomPlane;

    /**
     * The right plane in world space.
     */
    private Plane mRightPlane;

    /**
     * The left plane in world space.
     */
    private Plane mLeftPlane;

    /**
     * Near field distance.
     */
    private float mNearPos;

    /**
     * Far field distance.
     */
    private float mFarPos;

    /**
     * Near field top distance from center.
     */
    private float mTopPos;

    /**
     * Near field bottom distance from center.
     */
    private float mBottomPos;

    /**
     * Near field right distance from center.
     */
    private float mRightPos;

    /**
     * Near field left distance from center.
     */
    private float mLeftPos;

    /**
     * Get the near plane in world space.
     *
     * @return The near plane in world space.
     */
    public Plane getNearPlane() {
        return mNearPlane;
    }

    /**
     * Get the far plane in world space.
     *
     * @return The far plane in world space.
     */
    public Plane getFarPlane() {
        return mFarPlane;
    }

    /**
     * Get the top plan in world space.
     *
     * @return The top plan in world space.
     */
    public Plane getTopPlane() {
        return mTopPlane;
    }

    /**
     * Get the bottom plan in world space.
     *
     * @return The top plan in world space.
     */
    public Plane getBottomPlane() {
        return mBottomPlane;
    }

    /**
     * Get the right plan in world space.
     *
     * @return The right plan in world space.
     */
    public Plane getRightPlane() {
        return mRightPlane;
    }

    /**
     * Get the left plan in world space.
     *
     * @return The leftplan in world space.
     */
    public Plane getLeftPlane() {
        return mLeftPlane;
    }

    /**
     * Update frustrum planes.
     *
     * @param camera The camera.
     */
    public void update(Camera camera) {
        /**
         * Get stuff from camera.
         */
        camera.getLook(mLookVec);
        camera.getUp(mUpVec);
        camera.getRight(mRightVec);
        camera.getPosition(mPos);

        Frustrum frustrum = camera.getFrustrum();
        mNearPos = frustrum.getNear();
        mFarPos = frustrum.getFar();
        mTopPos = frustrum.getTop();
        mBottomPos = frustrum.getBottom();
        mRightPos = frustrum.getRight();
        mLeftPos = frustrum.getLeft();

        /**
         * In world space, create vector along camera look that is as long
         * as near field distance is. Note, this vector is is directional with a length
         * but no position.
         */
        mCenterVec.set(mLookVec);
        mCenterVec.mult(mNearPos);

        updateNearPlane();
        updateFarPlane();
        updateTopPlane();
        updateBottomPlane();
        updateLeftPlane();
        updateRightPlane();
    }

    /**
     * Update near plane.
     */
    private void updateNearPlane() {
        /**
         * From camera world pos, offset to near field.
         */
        mPoint.set(mLookVec);
        mPoint.mult(mNearPos);
        mPoint.add(mPos);

        /**
         * The normal is camera look.
         */
        mNearPlane = new Plane(mLookVec, mPoint);
    }

    /**
     * Update far plane.
     */
    private void updateFarPlane() {
        /**
         * The normal is negative camera look.
         */
        mNormal.set(mLookVec).neg();

        /**
         * From camera world pos, offset to far field.
         */
        mPoint.set(mLookVec);
        mPoint.mult(mFarPos);
        mPoint.add(mPos);

        mFarPlane = new Plane(mNormal, mPoint);
    }

    /**
     * Update top plane.
     */
    private void updateTopPlane() {
        /**
         * In world space, create vector along camera up that is as long
         * as near field top height is. Note, this vector is is directional with a length
         * but no position.
         */
        mOffsetVec.set(mUpVec);
        mOffsetVec.mult(mTopPos);

        /**
         * Add together the center and offset vector to reach the top side. Now we have a
         * directional vector that we can cross with camera right vector.
         */
        mSideVec.set(mCenterVec);
        mSideVec.add(mOffsetVec);

        Vec3.cross(mSideVec, mRightVec, mNormal);

        mTopPlane = new Plane(mNormal, mPos);
    }

    /**
     * Update bottom plane.
     */
    private void updateBottomPlane() {
        /**
         * In world space, create vector along camera up that is as long
         * as near field bottom height is. Note, this vector is is directional with a length
         * but no position.
         */
        mOffsetVec.set(mUpVec);
        mOffsetVec.mult(mBottomPos);

        /**
         * Add together the center and offset vector to reach the bottom side. Now we have a
         * directional vector that we can cross with camera right vector.
         */
        mSideVec.set(mCenterVec);
        mSideVec.add(mOffsetVec);

        Vec3.cross(mRightVec, mSideVec, mNormal);

        mBottomPlane = new Plane(mNormal, mPos);
    }

    /**
     * Update right plane.
     */
    private void updateRightPlane() {
        /**
         * In world space, create vector along camera right that is as long
         * as near field right width is. Note, this vector is is directional with a length
         * but no position.
         */
        mOffsetVec.set(mRightVec);
        mOffsetVec.mult(mRightPos);

        mSideVec.set(mCenterVec);
        mSideVec.add(mOffsetVec);

        /**
         * Add together the center and offset vector to reach the right side. Now we have a
         * directional vector that we can cross with camera up vector.
         */
        Vec3.cross(mUpVec, mSideVec, mNormal);

        mRightPlane = new Plane(mNormal, mPos);
    }

    /**
     * Update left plane.
     */
    private void updateLeftPlane() {
        /**
         * In world space, create vector along camera left that is as long
         * as near field left width is. Note, this vector is is directional with a length
         * but no position.
         */
        mOffsetVec.set(mRightVec);
        mOffsetVec.mult(mLeftPos);

        /**
         * Add together the center and offset vector to reach the left side. Now we have a
         * directional vector that we can cross with camera up vector.
         */
        mSideVec.set(mCenterVec);
        mSideVec.add(mOffsetVec);

        Vec3.cross(mSideVec, mUpVec, mNormal);

        mLeftPlane = new Plane(mNormal, mPos);
    }
}

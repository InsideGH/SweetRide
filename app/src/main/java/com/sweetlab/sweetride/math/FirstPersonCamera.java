package com.sweetlab.sweetride.math;

/**
 * First person (shooter) camera. Strafe left/right/forward/backward and
 * turn left/right and up/down supported.
 */
public class FirstPersonCamera extends Camera {
    /**
     * Temporary vector during update.
     */
    private Vec3 mTmp = new Vec3();

    /**
     * Right vector during update.
     */
    private Vec3 mDeltaRight = new Vec3();

    /**
     * Up vector during update.
     */
    private Vec3 mDeltaUp = new Vec3();

    /**
     * Look vector during update.
     */
    private Vec3 mDeltaLook = new Vec3();

    /**
     * Right vector during update.
     */
    private Vec3 mCameraRight = new Vec3();

    /**
     * Look vector during update.
     */
    private Vec3 mCameraLook = new Vec3();

    /**
     * Pos vector during update.
     */
    private Vec3 mDeltaPos = new Vec3();

    /**
     * Rotation matrix during update.
     */
    private Matrix44 mRotationMatrix = new Matrix44();

    /**
     * Transform to be applied to camera.
     */
    private Transform mTransform = new Transform();

    /**
     * Move and turn camera.
     *
     * @param x            Move left/right value.
     * @param z            Move forward/backward value.
     * @param angleAroundX Rotate around x axis.
     * @param angleAroundY Rotate around y axis.
     */
    public void update(float x, float z, float angleAroundX, float angleAroundY) {
        /**
         * Worth mentioning is that this method uses the camera apply transform which
         * applies a transform to existing camera (accumulating). That's why this method
         * always starts from default camera position and axis vectors and creates a transform
         * from that state.
         */

        /**
         * Get the cameras currently right vector.
         */
        getRight(mCameraRight);

        /**
         * Get the cameras currently look vector.
         */
        getLook(mCameraLook);

        /**
         * Reset position because we will add delta.
         */
        mDeltaPos.reset();

        /**
         * Move left/right along cameras current right vector.
         */
        mTmp.set(mCameraRight).mult(x);
        mDeltaPos.add(mTmp);

        /**
         * Move forward/backward along cameras current look vector.
         */
        mTmp.set(mCameraLook).mult(z);
        mDeltaPos.add(mTmp);

        /**
         * Get the default axis vectors because we will add delta rotation.
         */
        mDeltaRight.set(sDefaultRight);
        mDeltaUp.set(sDefaultUp);
        mDeltaLook.set(sDefaultLook);

        /**
         * Turn left/right, always around world up otherwise it feels weird.
         */
        mRotationMatrix.setRotate(angleAroundY, WORLD_UP.x, WORLD_UP.y, WORLD_UP.z);
        mDeltaLook.transform(mRotationMatrix);
        mDeltaRight.transform(mRotationMatrix);
        Vec3.cross(mDeltaRight, mDeltaLook, mDeltaUp);
        mDeltaUp.norm();

        /**
         * Look up/down around camera right vector.
         */
        mRotationMatrix.setRotate(angleAroundX, mCameraRight.x, mCameraRight.y, mCameraRight.z);
        mDeltaLook.transform(mRotationMatrix);
        mDeltaUp.transform(mRotationMatrix);
        Vec3.cross(mDeltaLook, mDeltaUp, mDeltaRight);
        mDeltaRight.norm();

        /**
         * Build transform matrix.
         */
        Matrix44 matrix = mTransform.getMatrix();
        matrix.setIdentity();
        matrix.setXAxis(mDeltaRight);
        matrix.setYAxis(mDeltaUp);
        matrix.setZAxis(mDeltaLook.neg());
        matrix.setTranslate(mDeltaPos);

        /**
         * Apply transform to camera.
         */
        applyTransform(mTransform);
    }
}

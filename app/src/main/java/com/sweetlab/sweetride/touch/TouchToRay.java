package com.sweetlab.sweetride.touch;

import com.sweetlab.sweetride.camera.Camera;
import com.sweetlab.sweetride.math.Matrix44;
import com.sweetlab.sweetride.math.Vec3;
import com.sweetlab.sweetride.math.Vec4;
import com.sweetlab.sweetride.intersect.Ray;

/**
 * Generates world space rays from android screen coordinates.
 */
public class TouchToRay {
    /**
     * Converter from android screen coordinates to gl ndc coordinates.
     */
    private final ScreenToNdcConverter mScreenToNdcConverter;

    /**
     * NDC near point.
     */
    private final Vec4 mNearPoint = new Vec4();

    /**
     * NDC far point.
     */
    private final Vec4 mFarPoint = new Vec4();

    /**
     * Ray position.
     */
    private final Vec3 mPosition = new Vec3();

    /**
     * Ray direction.
     */
    private final Vec3 mDirection = new Vec3();

    /**
     * Constructor.
     *
     * @param surfaceWidth  Android surface width.
     * @param surfaceHeight Android surface height.
     */
    public TouchToRay(int surfaceWidth, int surfaceHeight) {
        mScreenToNdcConverter = new ScreenToNdcConverter(surfaceWidth, surfaceHeight);
    }

    /**
     * Get ray based on android screen touch coordinates.
     *
     * @param screenX Touch x position.
     * @param screenY Touch y position.
     * @return Ray in world space.
     */
    public Ray getRay(Camera camera, int screenX, int screenY) {
        final float x = mScreenToNdcConverter.getX(screenX);
        final float y = mScreenToNdcConverter.getY(screenY);

        mNearPoint.set(x, y, -1, 1);
        mFarPoint.set(x, y, 1, 1);

        Matrix44 matrix = camera.getInvViewProjectionMatrix();
        mNearPoint.transform(matrix);
        mFarPoint.transform(matrix);
        mNearPoint.div(mNearPoint.w);
        mFarPoint.div(mFarPoint.w);

        Vec3.createVecFromPoints(mNearPoint, mFarPoint, mDirection);

        /**
         * The origin of the ray is somewhere on the cameras near plane in world space.
         */
        mPosition.set(mNearPoint.x, mNearPoint.y, mNearPoint.z);

        return new Ray(mPosition, mDirection);
    }
}

package com.sweetlab.sweetride.intersect;

import com.sweetlab.sweetride.math.Vec3;

/**
 * A ray has a start point and direction.
 * <p/>
 * p(t) = mOrigin + t * mDirection
 */
public class Ray {
    /**
     * Start point/origin of ray.
     */
    private final Vec3 mOrigin = new Vec3();

    /**
     * Contains direction of ray.
     */
    private final Vec3 mDirection = new Vec3();

    /**
     * Temporary vector used during calculations.
     */
    private final Vec3 mTmp = new Vec3();

    /**
     * Constructor.
     *
     * @param origin    Origin/Start point of ray.
     * @param direction Direction of ray.
     */
    public Ray(Vec3 origin, Vec3 direction) {
        mDirection.set(direction).norm();
        mOrigin.set(origin);
    }

    /**
     * Get the ray origin.
     *
     * @param dst Result written here.
     */
    public void getOrigin(Vec3 dst) {
        dst.set(mOrigin);
    }

    /**
     * Get the direction of the ray.
     *
     * @param dst Result written here.
     */
    public void getDirection(Vec3 dst) {
        dst.set(mDirection);
    }

    /**
     * Get point on ray at specific time.
     *
     * @param time   Time >= 0
     * @param result Where result is written.
     */
    public void getPointAtTime(float time, Vec3 result) {
        result.set(mOrigin);
        if (time > 0) {
            result.x += time * mDirection.x;
            result.y += time * mDirection.y;
            result.z += time * mDirection.z;
        }
    }

    /**
     * Get the closest point on the ray to the point. If closest point is behind ray,
     * ray origin is returned.
     * This is done by projecting vector from ray origin to point onto
     * the ray direction vector by using definition of dot product.
     * <pre>
     * Proof :
     *
     * closestPoint      = dir * (len(closestPoint) / len(dir)
     * cos(a)            = len(closestPoint) / len(vec)
     * len(vec) * cos(a) = len(closestPoint)
     * closestPoint      = dir * len(vec) * cos(a) / len(dir)
     * closestPoint      = dir * len(vec) * len(dir) * cos(a) / len(dir)^2
     * closestPoint      = dir * dot(vec, dir) / len(dir)^2
     * closestPoint      = dir * dot(vec, dir)
     * </pre>
     *
     * @param point        The point used to find closest point on ray.
     * @param closestPoint The resulting closest point found.
     */
    public void getClosestPoint(Vec3 point, Vec3 closestPoint) {
        /**
         * Create a vector from ray origin to point.
         */
        mTmp.set(point);
        mTmp.sub(mOrigin);

        /**
         * Calculate dot product, which is interpreted as t in ray equation.
         */
        float t = mDirection.dot(mTmp);

        /**
         * Calculate closest point by using ray equation.
         */
        closestPoint.set(mOrigin);
        if (t > 0) {
            closestPoint.x += t * mDirection.x;
            closestPoint.y += t * mDirection.y;
            closestPoint.z += t * mDirection.z;
        }
    }

    @Override
    public String toString() {
        return "origin = " + mOrigin + " direction = " + mDirection;
    }
}
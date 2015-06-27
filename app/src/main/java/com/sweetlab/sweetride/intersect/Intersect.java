package com.sweetlab.sweetride.intersect;

import com.sweetlab.sweetride.math.FloatUtil;
import com.sweetlab.sweetride.math.Vec3;

/**
 * Helps with various intersections. Idea is to create it once and use it a lot.
 */
public class Intersect {
    /**
     * Invalid intersection.
     */
    public static final float INVALID_INTERSECTION = Float.NaN;

    /**
     * Used in ray, ray intersection.
     */
    private final Vec3 mTmp = new Vec3();

    /**
     * Used in ray, ray intersection.
     */
    private final Vec3 mTmp2 = new Vec3();

    /**
     * Used in ray, ray intersection.
     */
    private final Vec3 mTmp3 = new Vec3();

    /**
     * Ray origin
     */
    private final Vec3 mRay1Origin = new Vec3();

    /**
     * Ray origin
     */
    private final Vec3 mRay2Origin = new Vec3();

    /**
     * Ray direction
     */
    private final Vec3 mRay1Direction = new Vec3();

    /**
     * Ray direction
     */
    private final Vec3 mRay2Direction = new Vec3();

    /**
     * Used a intersection point.
     */
    private final Vec3 mIntersectPoint = new Vec3();

    /**
     * Used to get plane normal and in bounding box intersection.
     */
    private final Vec3 mNormal = new Vec3();

    /**
     * Used in bounding box intersection
     */
    private final Vec3 mMin = new Vec3();

    /**
     * Used in bounding box intersection
     */
    private final Vec3 mMax = new Vec3();

    /**
     * Check if two rays intersect.
     * <pre>
     * o1 + t1 * d1               = o2 + t2 * d2
     * t1 * d1                    = o2 + t2 * d2 - o1
     * t1 * d1 x d2               = (o2 + t2 * d2 - o1) x d2
     * t1 * d1 x d2               = (t2 * d2) x d2 + (o2 - o1) x d2
     * t1 * d1 x d2               = (o2 - o1) x d2
     * t1 * (d1 x d2) . (d1 x d2) = ((o2 - o1) x d2) . (d1 x d2)
     * t1                         = ((o2 - o1) x d2) . (d1 x d2) / len(d1 x d2)^2
     * t2                         = ((o2 - o1) x d1) . (d1 x d2) / len(d1 x d2)^2
     * </pre>
     *
     * @param ray1 First ray.
     * @param ray2 Second ray.
     * @return T position of first ray where intersection occurs, or INVALID_INTERSECTION if
     * no intersection occurs.
     */
    public float intersect(Ray ray1, Ray ray2) {
        /**
         * Get ray information.
         */
        ray1.getOrigin(mRay1Origin);
        ray2.getOrigin(mRay2Origin);
        ray1.getDirection(mRay1Direction);
        ray2.getDirection(mRay2Direction);

        /**
         * tmp = (o2 - o1)
         */
        mTmp.set(mRay2Origin).sub(mRay1Origin);

        /**
         * tmp3 = (d1 x d2)
         */
        Vec3.cross(mRay1Direction, mRay2Direction, mTmp3);

        /**
         * denominator = len(d1 x d2)^2
         */
        final float denominator = mTmp3.lengthSq();

        /**
         * tmp2 = ((o2 - o1) x d2)
         */
        Vec3.cross(mTmp, mRay2Direction, mTmp2);

        /**
         * numerator1 = ((o2 - o1) x d2) . (d1 x d2)
         */
        final float numerator1 = mTmp2.dot(mTmp3);

        /**
         * tmp2 = ((o2 - o1) x d1)
         */
        Vec3.cross(mTmp, mRay1Direction, mTmp2);

        /**
         * numerator2 = ((o2 - o1) x d1) . (d1 x d2)
         */
        final float numerator2 = mTmp2.dot(mTmp3);

        /**
         * First check is to exclude parallel rays
         */
        if (denominator > FloatUtil.EPS) {
            final float t1 = numerator1 / denominator;
            final float t2 = numerator2 / denominator;

            final boolean t1IsZeroOrMore = ((t1 > 0) || (Math.abs(t1) < FloatUtil.EPS));
            final boolean t2IsZeroOrMore = ((t2 > 0) || (Math.abs(t2) < FloatUtil.EPS));

            /**
             * Second check is to exclude intersections with real negative values
             */
            if (t1IsZeroOrMore && t2IsZeroOrMore) {
                ray1.getPointAtTime(t1, mTmp);
                ray2.getPointAtTime(t2, mTmp2);

                /**
                 * Third check is to exclude skewed rays.
                 */
                if (Math.abs(mTmp.distance(mTmp2)) < FloatUtil.EPS) {
                    return t1;
                }
            }
        }
        return INVALID_INTERSECTION;
    }

    /**
     * <pre>
     *     Ray : p(t) = mOrigin + t * mDirection
     *     Plane : Ax + By + Cz  + D = 0 where D = -(p . planeNormal)
     *
     *     D                            = -(mOrigin + t * mDirection) . planeNormal
     *     D                            = -mOrigin . planeNormal -t * mDirection . planeNormal
     *     t * mDirection . planeNormal = -D -(mOrigin . planeNormal)
     *     t                            = (-D - (mOrigin . planeNormal)) / (mDirection . planeNormal)
     *
     * </pre>
     */
    public float intersect(Ray ray, Plane plane) {
        /**
         * Get plane distance.
         */
        float planeDistance = plane.getSignedDistToOrigo();

        /**
         * Get plane normal into tmp.
         */
        plane.getNormal(mNormal);

        /**
         * Find intersection.
         */
        return intersect(ray, mNormal, planeDistance);
    }

    /**
     * Get point where ray intersects plane.
     *
     * @param ray   Ray.
     * @param plane The plane.
     * @param point The result is written here if intersection is found.
     * @return True if intersects, false otherwise.
     */
    public boolean intersect(Ray ray, Plane plane, Vec3 point) {
        float t = intersect(ray, plane);
        if (!Float.isNaN(t)) {
            ray.getPointAtTime(t, point);
            return true;
        }
        return false;
    }

    /**
     * Find at what time ray intersects with plane defined by normal and distance.
     *
     * @param planeNormal   The plane normal.
     * @param planeDistance The plane distance.
     * @return Time or INVALID_INTERSECTION if no intersection is found.
     */
    public float intersect(Ray ray, Vec3 planeNormal, float planeDistance) {
        ray.getOrigin(mRay1Origin);
        ray.getDirection(mRay1Direction);

        /**
         * The denominator, (mDirection . planeNormal)
         */
        final float denominator = mRay1Direction.dot(planeNormal);

        /**
         * Return invalid intersection if the ray direction is not facing
         * plane normal direction in a way that intersection can happen
         * against plane front face.
         */
        if (denominator < 0) {
            /**
             * The numerator, (-D - (mOrigin . planeNormal))
             */
            final float numerator = -planeDistance - mRay1Origin.dot(planeNormal);

            /**
             * Calculate t, (-D - (mOrigin . planeNormal)) / (mDirection . planeNormal)
             * Calculate t, numerator / denominator.
             */
            float time = numerator / denominator;

            /**
             * Check that ray is not positioned on wrong side of plane.
             */
            if (((time > 0) || (Math.abs(time) < FloatUtil.EPS))) {
                return time;
            }
        }
        return INVALID_INTERSECTION;
    }

    /**
     * <pre>
     *
     *  Y (+)                      Z (-)
     *  |                        /
     *  |                      /
     *  |        G---------H
     *  |      / |        /|
     *  |    /   |      /  |
     *  |   C----|-----D   |
     *  |   |    E-----|---F
     *  |   |  |       |  |
     *  |   | |        | |
     *  |   A----------B
     *  |
     *  ------------------------------------ X (+)
     *  </pre>
     *
     * @param ray The ray.
     * @param box The bounding box to intersect test.
     * @return True if intersects, false otherwise.
     */
    public boolean intersects(Ray ray, BoundingBox box) {
        float time;

        box.getMin(mMin);
        box.getMax(mMax);

        /**
         * 'dcab' plane (front)
         */
        mNormal.set(0, 0, 1);
        time = intersect(ray, mNormal, mMax.z);
        if (!Float.isNaN(time)) {
            ray.getPointAtTime(time, mIntersectPoint);
            if (mIntersectPoint.x > mMin.x && mIntersectPoint.x < mMax.x &&
                    mIntersectPoint.y > mMin.y && mIntersectPoint.y < mMax.y) {
                return true;
            }
        }

        /**
         * 'dbfh' plane (right)
         */
        mNormal.set(1, 0, 0);
        time = intersect(ray, mNormal, mMax.x);
        if (!Float.isNaN(time)) {
            ray.getPointAtTime(time, mIntersectPoint);
            if (mIntersectPoint.y > mMin.y && mIntersectPoint.y < mMax.y &&
                    mIntersectPoint.z > mMin.z && mIntersectPoint.z < mMax.z) {
                return true;
            }
        }

        /**
         * 'dhgc' plane (top)
         */
        mNormal.set(0, 1, 0);
        time = intersect(ray, mNormal, mMax.y);
        if (!Float.isNaN(time)) {
            ray.getPointAtTime(time, mIntersectPoint);
            if (mIntersectPoint.x > mMin.x && mIntersectPoint.x < mMax.x &&
                    mIntersectPoint.z > mMin.z && mIntersectPoint.z < mMax.z) {
                return true;
            }
        }

        /**
         * 'efhg' plane (back)
         */
        mNormal.set(0, 0, -1);
        time = intersect(ray, mNormal, mMin.z);
        if (!Float.isNaN(time)) {
            ray.getPointAtTime(time, mIntersectPoint);
            if (mIntersectPoint.x > mMin.x && mIntersectPoint.x < mMax.x &&
                    mIntersectPoint.y > mMin.y && mIntersectPoint.y < mMax.y) {
                return true;
            }
        }

        /**
         * 'eacg' plane (left)
         */
        mNormal.set(-1, 0, 0);
        time = intersect(ray, mNormal, mMin.x);
        if (!Float.isNaN(time)) {
            ray.getPointAtTime(time, mIntersectPoint);
            if (mIntersectPoint.y > mMin.y && mIntersectPoint.y < mMax.y &&
                    mIntersectPoint.z > mMin.z && mIntersectPoint.z < mMax.z) {
                return true;
            }
        }

        /**
         * 'eabf' plane (bottom)
         */
        mNormal.set(0, -1, 0);
        time = intersect(ray, mNormal, mMin.y);
        if (!Float.isNaN(time)) {
            ray.getPointAtTime(time, mIntersectPoint);
            if (mIntersectPoint.x > mMin.x && mIntersectPoint.x < mMax.x &&
                    mIntersectPoint.z > mMin.z && mIntersectPoint.z < mMax.z) {
                return true;
            }
        }
        return false;
    }
}

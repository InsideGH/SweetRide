package com.sweetlab.sweetride.intersect;

import android.util.Log;

import com.sweetlab.sweetride.DebugOptions;
import com.sweetlab.sweetride.camera.Camera;
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
     * Origin/Start
     */
    private final Vec3 mStart1 = new Vec3();

    /**
     * Origin/Start
     */
    private final Vec3 mStart2 = new Vec3();

    /**
     * Direction
     */
    private final Vec3 mDirection1 = new Vec3();

    /**
     * Direction.
     */
    private final Vec3 mDirection2 = new Vec3();

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
        ray1.getOrigin(mStart1);
        ray2.getOrigin(mStart2);
        ray1.getDirection(mDirection1);
        ray2.getDirection(mDirection2);

        /**
         * tmp = (o2 - o1)
         */
        mTmp.set(mStart2).sub(mStart1);

        /**
         * tmp3 = (d1 x d2)
         */
        Vec3.cross(mDirection1, mDirection2, mTmp3);

        /**
         * denominator = len(d1 x d2)^2
         */
        final float denominator = mTmp3.lengthSq();

        /**
         * tmp2 = ((o2 - o1) x d2)
         */
        Vec3.cross(mTmp, mDirection2, mTmp2);

        /**
         * numerator1 = ((o2 - o1) x d2) . (d1 x d2)
         */
        final float numerator1 = mTmp2.dot(mTmp3);

        /**
         * tmp2 = ((o2 - o1) x d1)
         */
        Vec3.cross(mTmp, mDirection1, mTmp2);

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
        ray.getOrigin(mStart1);
        ray.getDirection(mDirection1);

        /**
         * The denominator, (mDirection . planeNormal)
         */
        final float denominator = mDirection1.dot(planeNormal);

        /**
         * Return invalid intersection if the ray direction is not facing
         * plane normal direction in a way that intersection can happen
         * against plane front face.
         */
        if (denominator < 0) {
            /**
             * The numerator, (-D - (mOrigin . planeNormal))
             */
            final float numerator = -planeDistance - mStart1.dot(planeNormal);

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
     * Find at what time line segment intersects with plane defined by normal and distance.
     *
     * @param planeNormal   The plane normal.
     * @param planeDistance The plane distance.
     * @return Time or INVALID_INTERSECTION if no intersection is found.
     */
    public float intersect(LineSegment lineSegment, Vec3 planeNormal, float planeDistance) {
        lineSegment.getStartPoint(mStart1);
        lineSegment.getDirection(mDirection1);

        /**
         * The denominator, (mDirection . planeNormal)
         */
        final float denominator = mDirection1.dot(planeNormal);

        /**
         * Return invalid intersection if the ray direction is not facing
         * plane normal direction in a way that intersection can happen
         * against plane front face.
         */
        /**
         * The numerator, (-D - (mOrigin . planeNormal))
         */
        final float numerator = -planeDistance - mStart1.dot(planeNormal);

        /**
         * Calculate t, (-D - (mOrigin . planeNormal)) / (mDirection . planeNormal)
         * Calculate t, numerator / denominator.
         */
        float time = numerator / denominator;

        /**
         * Check that intersection is within line segment boundaries.
         */
        if (((time > 0) || (Math.abs(time) < FloatUtil.EPS)) &&
                ((time < 1) || (Math.abs(time - 1) < FloatUtil.EPS))) {
            return time;
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
         * 'dcab' plane (front).
         * If plane is at pos z, plane distance is negative
         * If plane is at neg z, plane distance is positive.
         * Thus, invert.
         */
        mNormal.set(0, 0, 1);
        time = intersect(ray, mNormal, -mMax.z);
        if (!Float.isNaN(time)) {
            ray.getPointAtTime(time, mIntersectPoint);
            if (mIntersectPoint.x > mMin.x && mIntersectPoint.x < mMax.x &&
                    mIntersectPoint.y > mMin.y && mIntersectPoint.y < mMax.y) {
                if (DebugOptions.DEBUG_INTERSECT) {
                    Log.d("Peter100", "Intersect.intersects FRONT");
                }
                return true;
            }
        }

        /**
         * 'dbfh' plane (right)
         * If plane is at pos x, plane distance is negative
         * If plane is at neg x, plane distance is positive.
         * Thus, invert.
         */
        mNormal.set(1, 0, 0);
        time = intersect(ray, mNormal, -mMax.x);
        if (!Float.isNaN(time)) {
            ray.getPointAtTime(time, mIntersectPoint);
            if (mIntersectPoint.y > mMin.y && mIntersectPoint.y < mMax.y &&
                    mIntersectPoint.z > mMin.z && mIntersectPoint.z < mMax.z) {
                if (DebugOptions.DEBUG_INTERSECT) {
                    Log.d("Peter100", "Intersect.intersects RIGHT");
                }
                return true;
            }
        }

        /**
         * 'dhgc' plane (top)
         * If plane is at pos y, plane distance is negative
         * If plane is at neg y, plane distance is positive.
         * Thus, invert.
         */
        mNormal.set(0, 1, 0);
        time = intersect(ray, mNormal, -mMax.y);
        if (!Float.isNaN(time)) {
            ray.getPointAtTime(time, mIntersectPoint);
            if (mIntersectPoint.x > mMin.x && mIntersectPoint.x < mMax.x &&
                    mIntersectPoint.z > mMin.z && mIntersectPoint.z < mMax.z) {
                if (DebugOptions.DEBUG_INTERSECT) {
                    Log.d("Peter100", "Intersect.intersects TOP");
                }
                return true;
            }
        }

        /**
         * 'efhg' plane (back)
         * If plane is at pos z, plane distance is positive
         * If plane is at neg z, plane distance is negative.
         * Thus, use value as is.
         */
        mNormal.set(0, 0, -1);
        time = intersect(ray, mNormal, mMin.z);
        if (!Float.isNaN(time)) {
            ray.getPointAtTime(time, mIntersectPoint);
            if (mIntersectPoint.x > mMin.x && mIntersectPoint.x < mMax.x &&
                    mIntersectPoint.y > mMin.y && mIntersectPoint.y < mMax.y) {
                if (DebugOptions.DEBUG_INTERSECT) {
                    Log.d("Peter100", "Intersect.intersects BACK");
                }
                return true;
            }
        }

        /**
         * 'eacg' plane (left)
         * If plane is at pos x, plane distance is positive
         * If plane is at neg x, plane distance is negative.
         * Thus, use value as is.
         */
        mNormal.set(-1, 0, 0);
        time = intersect(ray, mNormal, mMin.x);
        if (!Float.isNaN(time)) {
            ray.getPointAtTime(time, mIntersectPoint);
            if (mIntersectPoint.y > mMin.y && mIntersectPoint.y < mMax.y &&
                    mIntersectPoint.z > mMin.z && mIntersectPoint.z < mMax.z) {
                if (DebugOptions.DEBUG_INTERSECT) {
                    Log.d("Peter100", "Intersect.intersects LEFT");
                }
                return true;
            }
        }

        /**
         * 'eabf' plane (bottom)
         * If plane is at pos y, plane distance is positive
         * If plane is at neg y, plane distance is negative.
         * Thus, use value as is.
         */
        mNormal.set(0, -1, 0);
        time = intersect(ray, mNormal, mMin.y);
        if (!Float.isNaN(time)) {
            ray.getPointAtTime(time, mIntersectPoint);
            if (mIntersectPoint.x > mMin.x && mIntersectPoint.x < mMax.x &&
                    mIntersectPoint.z > mMin.z && mIntersectPoint.z < mMax.z) {
                if (DebugOptions.DEBUG_INTERSECT) {
                    Log.d("Peter100", "Intersect.intersects BOTTOM");
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Check if line segment intersecting any view frustrum plane.
     *
     * @param line   Line segment to check.
     * @param camera Camera.
     * @return True if line segment is inside view frustrum.
     */
    @SuppressWarnings("unused")
    public boolean isVisible(LineSegment line, Camera camera) {
        Plane plane;

        plane = camera.getNearPlane();
        plane.getNormal(mNormal);
        if (!Float.isNaN(intersect(line, mNormal, plane.getSignedDistToOrigo()))) {
            return true;
        }

        plane = camera.getFarPlane();
        plane.getNormal(mNormal);
        if (!Float.isNaN(intersect(line, mNormal, plane.getSignedDistToOrigo()))) {
            return true;
        }

        plane = camera.getLeftPlane();
        plane.getNormal(mNormal);
        if (!Float.isNaN(intersect(line, mNormal, plane.getSignedDistToOrigo()))) {
            return true;
        }

        plane = camera.getRightPlane();
        plane.getNormal(mNormal);
        if (!Float.isNaN(intersect(line, mNormal, plane.getSignedDistToOrigo()))) {
            return true;
        }

        plane = camera.getTopPlane();
        plane.getNormal(mNormal);
        if (!Float.isNaN(intersect(line, mNormal, plane.getSignedDistToOrigo()))) {
            return true;
        }

        plane = camera.getBottomPlane();
        plane.getNormal(mNormal);
        //noinspection RedundantIfStatement
        if (!Float.isNaN(intersect(line, mNormal, plane.getSignedDistToOrigo()))) {
            return true;
        }
        return false;
    }
}

package com.sweetlab.sweetride.intersect;

import com.sweetlab.sweetride.math.Vec3;

/**
 * A hyper plane. The equation : Ax + By + Cz + D = 0 where D = -dot(p, n).
 * p - point in the plain.
 * n - normalized normal.
 */
public class Plane {
    /**
     * Signed distance from plane to origo.
     */
    final private float mD;

    /**
     * Plane normal.
     */
    final private Vec3 mNormal = new Vec3();

    /**
     * Constructor.
     *
     * @param normal The plane normal.
     * @param point  Point that is in the plane.
     */
    public Plane(Vec3 normal, Vec3 point) {
        mNormal.set(normal);
        mNormal.norm();
        mD = -point.dot(mNormal);
    }

    /**
     * Constructor. Create a plane from three points.
     *
     * @param p1 First point.
     * @param p2 Second point.
     * @param p3 Third point.
     */
    public Plane(Vec3 p1, Vec3 p2, Vec3 p3) {
        Vec3 v1 = new Vec3();
        Vec3 v2 = new Vec3();
        Vec3.createVecFromPoints(p1, p2, v1);
        Vec3.createVecFromPoints(p1, p3, v2);
        Vec3.cross(v1, v2, mNormal);
        mNormal.norm();
        mD = -p1.dot(mNormal);
    }

    /**
     * Get the normal.
     *
     * @param dst Destination.
     */
    public void getNormal(Vec3 dst) {
        dst.set(mNormal);
    }

    /**
     * Get perpendicular signed distance from plane to origo.
     *
     * @return The distance.
     */
    public float getSignedDistToOrigo() {
        return mD;
    }

    /**
     * Get the signed distance from the plane to a point.
     *
     * @param point The point.
     * @return The minimum distance.
     */
    public float getSignedDistToPoint(Vec3 point) {
        return point.dot(mNormal) + mD;
    }

    /**
     * Get the distance to the parallel plane that passes through origo.
     *
     * @return The distance.
     */
    public float getDistToOrigo() {
        return Math.abs(mD);
    }

    /**
     * Get the closest point in the plane to the point a.
     *
     * @param a          Point to find closes point in plane from.
     * @param planePoint The point in the plane.
     */
    public void getClosestPointInPlane(Vec3 a, Vec3 planePoint) {
        float distanceToPoint = getSignedDistToPoint(a);
        planePoint.set(a);
        planePoint.x -= distanceToPoint * mNormal.x;
        planePoint.y -= distanceToPoint * mNormal.y;
        planePoint.z -= distanceToPoint * mNormal.z;
    }

    @Override
    public String toString() {
        return "normal = " + mNormal + " distance to origo = " + mD;
    }
}

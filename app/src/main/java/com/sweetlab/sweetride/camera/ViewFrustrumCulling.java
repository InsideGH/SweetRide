package com.sweetlab.sweetride.camera;

import com.sweetlab.sweetride.geometry.Geometry;
import com.sweetlab.sweetride.intersect.BoundingBox;
import com.sweetlab.sweetride.intersect.Intersect;
import com.sweetlab.sweetride.intersect.LineSegment;
import com.sweetlab.sweetride.intersect.Plane;
import com.sweetlab.sweetride.math.Vec3;

/**
 * Provides visibility testing for points and geometries.
 */
public class ViewFrustrumCulling {
    /**
     * Bounding box max (x,y,z) values
     */
    @SuppressWarnings("unused")
    private final Vec3 mMax = new Vec3();

    /**
     * Bounding box min (x,y,z) values
     */
    @SuppressWarnings("unused")
    private final Vec3 mMin = new Vec3();

    /**
     * Bounding box width vector.
     */
    private final Vec3 mWidthVec = new Vec3();

    /**
     * Bounding box height vector.
     */
    private final Vec3 mHeightVec = new Vec3();

    /**
     * Bounding box depth vector.
     */
    private final Vec3 mDepthVec = new Vec3();

    /**
     * Bounding box middle point.
     */
    private final Vec3 mMiddlePoint = new Vec3();

    /**
     * Intersect.
     */
    private final Intersect mIntersect = new Intersect();

    /**
     * Temporary normal vector using during line segment intersection.
     */
    private final Vec3 mNormal = new Vec3();

    /**
     * Check if point is within view frustrum.
     *
     * @param point  Point to check.
     * @param camera Camera.
     * @return True if point is inside view frustrum.
     */
    public boolean isVisible(Vec3 point, Camera camera) {
        if (camera.getNearPlane().getSignedDistToPoint(point) < 0) {
            return false;
        }
        if (camera.getFarPlane().getSignedDistToPoint(point) < 0) {
            return false;
        }
        if (camera.getLeftPlane().getSignedDistToPoint(point) < 0) {
            return false;
        }
        if (camera.getRightPlane().getSignedDistToPoint(point) < 0) {
            return false;
        }
        if (camera.getTopPlane().getSignedDistToPoint(point) < 0) {
            return false;
        }
        //noinspection RedundantIfStatement
        if (camera.getBottomPlane().getSignedDistToPoint(point) < 0) {
            return false;
        }
        return true;
    }

    /**
     * Check if bounding box is in or partially in the view frustrum.
     *
     * @param box    The bounding box.
     * @param camera The camera.
     * @return True if inside view frustrum.
     */
    public boolean isVisible(BoundingBox box, Camera camera) {
        if (!isBoxVisible(camera.getNearPlane(), box)) {
            return false;
        }
        if (!isBoxVisible(camera.getFarPlane(), box)) {
            return false;
        }
        if (!isBoxVisible(camera.getRightPlane(), box)) {
            return false;
        }
        if (!isBoxVisible(camera.getLeftPlane(), box)) {
            return false;
        }
        if (!isBoxVisible(camera.getTopPlane(), box)) {
            return false;
        }
        //noinspection RedundantIfStatement
        if (!isBoxVisible(camera.getBottomPlane(), box)) {
            return false;
        }
        return true;
    }

    /**
     * Check if geometry is visible, i.e inside camera view frustrum.
     *
     * @param geometry Geometry to check.
     * @param camera   Camera to use.
     * @return True if visible. If no bounding box exists method return true.
     */
//    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isVisible(Geometry geometry, Camera camera) {
        BoundingBox box = geometry.getBoundingBox();
        return box.isEmpty() || isVisible(box, camera);
    }

    /**
     * Check if box is visible from plane point of view.
     *
     * @param plane The plane.
     * @param box   The box.
     * @return True if visible, false otherwise.
     */
    private boolean isBoxVisible(Plane plane, BoundingBox box) {
        box.getWidthVec(mWidthVec);
        box.getHeightVec(mHeightVec);
        box.getDepthVec(mDepthVec);
        box.getMiddlePoint(mMiddlePoint);
        plane.getNormal(mNormal);

        float radius = Math.abs(mWidthVec.dot(mNormal));
        radius += Math.abs(mHeightVec.dot(mNormal));
        radius += Math.abs(mDepthVec.dot(mNormal));
        radius /= 2;

        float signedDistToPoint = plane.getSignedDistToPoint(mMiddlePoint);
        return signedDistToPoint > -radius;
    }
}

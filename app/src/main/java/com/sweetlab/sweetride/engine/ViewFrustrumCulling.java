package com.sweetlab.sweetride.engine;

import com.sweetlab.sweetride.geometry.Geometry;
import com.sweetlab.sweetride.math.Camera;
import com.sweetlab.sweetride.math.Vec3;
import com.sweetlab.sweetride.mesh.BoundingBox;
import com.sweetlab.sweetride.mesh.TransformableBoundingBox;

/**
 * Provides visibility testing for points and geometries.
 */
public class ViewFrustrumCulling {
    /**
     * FLL corner
     */
    private static final int A = 0;

    /**
     * FLR corner
     */
    private static final int B = 1;

    /**
     * FUL corner
     */
    private static final int C = 2;

    /**
     * FUR corner
     */
    private static final int D = 3;

    /**
     * BLL corner
     */
    private static final int E = 4;

    /**
     * BLR corner
     */
    private static final int F = 5;

    /**
     * BUL corner
     */
    private static final int G = 6;

    /**
     * BUR corner
     */
    private static final int H = 7;

    /**
     * Number of corners in a bounding box.
     */
    private static final int NBR_BOX_CORNERS = 8;

    /**
     * Bounding box max (x,y,z) values
     */
    private Vec3 mMax = new Vec3();

    /**
     * Bounding box min (x,y,z) values
     */
    private Vec3 mMin = new Vec3();

    /**
     * Corners in bounding box.
     */
    private Vec3[] mPoints = new Vec3[NBR_BOX_CORNERS];

    /**
     * Constructor.
     */
    public ViewFrustrumCulling() {
        for (int i = 0; i < mPoints.length; i++) {
            mPoints[i] = new Vec3();
        }
    }

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
        buildPoints(box);
        for (Vec3 point : mPoints) {
            if (isVisible(point, camera)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if geometry is visible, i.e inside camera view frustrum.
     *
     * @param geometry Geometry to check.
     * @param camera   Camera to use.
     * @return True if visible. If no bounding box exists method return true.
     */
    public boolean isVisible(Geometry geometry, Camera camera) {
        TransformableBoundingBox box = geometry.getTransformableBoundingBox();
        return box == null || isVisible(box, camera);
    }

    /**
     * Fill the member array of corner points from bounding box.
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
     * @param box The bounding box.
     */
    private void buildPoints(BoundingBox box) {
        box.getMax(mMax);
        box.getMin(mMin);

        mPoints[A].set(mMin.x, mMin.y, mMax.z);
        mPoints[B].set(mMax.x, mMin.y, mMax.z);
        mPoints[C].set(mMin.x, mMax.y, mMax.z);
        mPoints[D].set(mMax.x, mMax.y, mMax.z);

        mPoints[E].set(mMin.x, mMin.y, mMin.z);
        mPoints[F].set(mMax.x, mMin.y, mMin.z);
        mPoints[G].set(mMin.x, mMax.y, mMin.z);
        mPoints[H].set(mMax.x, mMax.y, mMin.z);
    }
}

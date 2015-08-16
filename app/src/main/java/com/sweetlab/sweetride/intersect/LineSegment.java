package com.sweetlab.sweetride.intersect;

import com.sweetlab.sweetride.math.Vec3;

/**
 * A line has a start point, end point and direction. Note, this class is
 * mutable.
 * <p/>
 * p(t) = mStartPoint + t * mDirection
 */
public class LineSegment {
    /**
     * Start point/origin of line segment.
     */
    private final Vec3 mStartPoint = new Vec3();

    /**
     * End point/origin of line segment.
     */
    private final Vec3 mEndPoint = new Vec3();

    /**
     * Direction of line, not normalized.
     */
    private final Vec3 mDirection = new Vec3();

    /**
     * Temporary vector used during calculations.
     */
    private final Vec3 mTmp = new Vec3();

    /**
     * Constructor. Creates an empty line segment.
     */
    public LineSegment() {
    }

    /**
     * Reconfigure the line segment.
     *
     * @param startPoint Start point.
     * @param endPoint   End point.
     */
    public void set(Vec3 startPoint, Vec3 endPoint) {
        mDirection.set(endPoint).sub(startPoint);
        mStartPoint.set(startPoint);
        mEndPoint.set(endPoint);
    }

    /**
     * Get the line segment start point.
     *
     * @param dst Result written here.
     */
    public void getStartPoint(Vec3 dst) {
        dst.set(mStartPoint);
    }

    /**
     * Get the line segment end point.
     *
     * @param dst Result written here.
     */
    public void getEndPoint(Vec3 dst) {
        dst.set(mEndPoint);
    }

    /**
     * Get the direction of the line segment. Not normalized.
     *
     * @param dst Result written here.
     */
    public void getDirection(Vec3 dst) {
        dst.set(mDirection);
    }

    /**
     * Get point on line at specific time.
     *
     * @param time   Time >= 0
     * @param result Where result is written.
     */
    public void getPointAtTime(float time, Vec3 result) {
        result.set(mStartPoint);
        if (time > 0) {
            result.x += time * mDirection.x;
            result.y += time * mDirection.y;
            result.z += time * mDirection.z;
        }
    }

    /**
     * Get the closest point on the line to the point.
     * If closest point is before line start, line start is returned.
     * If closest point is after line end, line end is returned.
     * This is done by projecting vector from line origin to point onto
     * the line direction vector by using definition of dot product.
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
     * @param point        The point used to find closest point on line.
     * @param closestPoint The resulting closest point found.
     */
    public void getClosestPoint(Vec3 point, Vec3 closestPoint) {
        /**
         * Create a vector from line start to point.
         */
        mTmp.set(point);
        mTmp.sub(mStartPoint);

        /**
         * Calculate dot product, which is interpreted as t in line equation.
         */
        float t = mDirection.dot(mTmp);

        /**
         * Calculate closest point by using line equation.
         */
        if (t > 1) {
            closestPoint.set(mEndPoint);
        } else if (t < 0) {
            closestPoint.set(mStartPoint);
        } else {
            closestPoint.set(mStartPoint);
            closestPoint.x += t * mDirection.x;
            closestPoint.y += t * mDirection.y;
            closestPoint.z += t * mDirection.z;
        }
    }

    @Override
    public String toString() {
        return "start = " + mStartPoint + " end = " + mEndPoint + " direction = " + mDirection;
    }
}
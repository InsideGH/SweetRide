package com.sweetlab.sweetride.game.curve;

import com.sweetlab.sweetride.math.Vec3;

import java.util.ArrayList;
import java.util.List;

/**
 * A CatmullRom spline. See CatMullRomCurve for theory.
 */
public class CatmullRomSpline {
    /**
     * The curve segments in the spline.
     */
    private final List<Segment> mSegments = new ArrayList<>();

    /**
     * The spline length.
     */
    private final float mSplineLength;

    /**
     * Builder to create a Catmull-Rom spline. The first and last point will not be part
     * of the spline but only be used as ending points for tangents.
     */
    public static class Builder {
        /**
         * Default number of iterations used during curve length estimation.
         */
        private static final int RIEMANN_ITERATIONS = 20;

        /**
         * The points.
         */
        private final List<Vec3> mPoints = new ArrayList<>();

        /**
         * Number of iterations used during curve length estimation.
         */
        private int mRiemannIterations = RIEMANN_ITERATIONS;

        /**
         * CatmullRom spline builder.
         *
         * @param p0 First point, tangent control point.
         * @param p1 Second point, spline first point.
         * @param p2 Third point, spline second point.
         * @param p3 Fourth point, tangent control point or spline point if more points are added.
         */
        public Builder(Vec3 p0, Vec3 p1, Vec3 p2, Vec3 p3) {
            mPoints.add(p0);
            mPoints.add(p1);
            mPoints.add(p2);
            mPoints.add(p3);
        }

        /**
         * Add point.
         *
         * @param point The point.
         * @return This builder.
         */
        public Builder addPoint(Vec3 point) {
            mPoints.add(point);
            return this;
        }

        /**
         * Set number of Riemann iterations to use during curve length estimation. Default is
         * 20.
         *
         * @param count Number of iterations.
         * @return This builder.
         */
        public Builder setRiemannIterations(int count) {
            mRiemannIterations = count;
            return this;
        }

        /**
         * Build CatmullRom spline.
         *
         * @return The CatmullRom spline.
         */
        public CatmullRomSpline build() {
            return new CatmullRomSpline(mPoints, mRiemannIterations);
        }
    }

    /**
     * Number of points must be at least 4.
     *
     * @param points         The points.
     * @param iterationCount Iterations count used during curve length estimation.
     */
    private CatmullRomSpline(List<Vec3> points, int iterationCount) {
        float segmentStart = 0;
        float segmentEnd = 0;

        int size = points.size() - 3;
        for (int i = 0; i < size; i++) {
            CatmullRomCurve curve = new CatmullRomCurve(points.get(i), points.get(i + 1), points.get(i + 2), points.get(i + 3), iterationCount);
            segmentEnd = segmentStart + curve.getLength();
            mSegments.add(new Segment(curve, segmentStart, segmentEnd));
            segmentStart = segmentEnd;
        }

        mSplineLength = segmentEnd;
    }

    /**
     * Calculate point on curve.
     *
     * @param t      Time, ranging from 0 to 1 including.
     * @param result The resulting point.
     */
    public void calcInterpolatedPoint(float t, Vec3 result) {
        float splinePos = t * mSplineLength;
        Segment segment = findSegment(splinePos);
        float curveTime = (splinePos - segment.mStart) / segment.mCurve.getLength();
        segment.mCurve.calcInterpolatedPoint(curveTime, result);
    }

    /**
     * Get the curve length.
     *
     * @return The curve length.
     */
    public float getLength() {
        return mSplineLength;
    }

    /**
     * Find the segment containing the given spline position.
     *
     * @param splinePos The spline position.
     * @return The segment.
     */
    private Segment findSegment(float splinePos) {
        for (Segment segment : mSegments) {
            if (segment.mEnd >= splinePos) {
                return segment;
            }
        }
        throw new RuntimeException("Could not find segment at spline position " + splinePos);
    }

    /**
     * A CatmullRom curve segment in the spline.
     */
    private static class Segment {
        /**
         * The CatmullRom curve.
         */
        private final CatmullRomCurve mCurve;

        /**
         * The start position in the spline.
         */
        private float mStart;

        /**
         * The end position in the spline.
         */
        private float mEnd;

        /**
         * CatmullRom segment.
         *
         * @param curve The CatmullRom curve.
         * @param start The start of this curve in spline space position.
         * @param end   The end of this curve in spline space position.
         */
        public Segment(CatmullRomCurve curve, float start, float end) {
            mCurve = curve;
            mStart = start;
            mEnd = end;
        }
    }
}

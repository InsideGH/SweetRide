package com.sweetlab.sweetride.game.curve;

import com.sweetlab.sweetride.math.Matrix44;
import com.sweetlab.sweetride.math.Vec3;
import com.sweetlab.sweetride.math.Vec4;

/**
 * <pre>
 *     ** Cubic curve theory **
 *
 *     Parametric Q(t) = a + bt + ct^2 + dt^3
 *     where a, b, c, d are vectors.
 *
 *     Qx(t) = ax + bxt + cxt^2 + dxt^3
 *     Qy(t) = ay + byt + cyt^2 + dyt^3
 *     Qz(t) = az + bzt + czt^2 + dzt^3
 *
 *     Q(t) = [ ax bx cx dx
 *              ay by cy dy  * [1 t t^2 t^3]'
 *              az bz cz dz]
 *
 *     Q(t) = C * T(t)
 *
 *     C is constant thus the derivative (tangent) is
 *     Q'(t) = C * [0, 1, 2t 3t^2]'
 *
 *     A curve has 4 coefficients, thus we need 4 constraints (gx) to the curve
 *     start/end point and start/end derivative.
 *
 *     Q(t) = (a1 + b1t + c1t^2 + d1t^3) * g1
 *          + (a2 + b2t + c2t^2 + d2t^3) * g2
 *          + (a3 + b3t + c3t^2 + d3t^3) * g3
 *          + (a4 + b4t + c4t^2 + d4t^3) * g4
 *
 *     The above is a weighted sum of the geometric constraints. The ai + bit + cit^2 + dit^3
 *     are called the blending functions.
 *
 *     Q(t) = [g1 g2 g3 g4] [a1 b1 c1 d1 [1 t t^2 t^3]'
 *                           a2 b2 c2 d2
 *                           a3 b3 c3 d3
 *                           a4 b4 c4 d4]
 *     Q(t) = G*M*T(t)
 *
 *     where G = [g1 g2 g3 g4] = [(g1)x (g2)x (g3)x (g4)x
 *                                (g1)y (g2)y (g3)y (g4)y
 *                                (g1)z (g2)z (g3)z (g4)z]
 *
 *     The above is called the Geometry matrix.
 *
 *     where M = [a1 b1 c1 d1
 *                a2 b2 c2 d2
 *                a3 b3 c3 d3
 *                a4 b4 c4 d4]
 *
 *     The above is the basis matrix.
 * </pre>
 * <p/>
 * <p/>
 * <pre>
 *     ** Hermit curve theory **
 *
 *     Defined by two end points and the tangent directions in those end points.
 *
 *     H(t) = [P1 P2 T1 T2] * M * [1 t t^2 t^3]'
 *     Q(t) = H(t) = G*M*T(t), where G = [P1 P2 T1 T2].
 *
 *     H(0)  = [P1 P2 T1 T2] * M * [1 0 0 0] = P1
 *     H(1)  = [P1 P2 T1 T2] * M * [1 1 1 1] = P2
 *     H'(0) = [P1 P2 T1 T2] * M * [0 1 0 0] = T1
 *     H'(1) = [P1 P2 T1 T2] * M * [0 1 2 3] = T2
 *
 *     Rewriting the the four equations above to a single equation ->
 *
 *     [P1 P2 T1 T2] * M * [1 1 0 0
 *                          0 1 1 1
 *                          0 1 0 2
 *                          0 1 0 3] = [P1 P2 T1 T2]
 *
 *     Find M = inv([1 1 0 0    [1 0 -3 2
 *                   0 1 1 1     0 0 3 -2
 *                   0 1 0 2  =  0 1 -2 1
 *                   0 1 0 3]    0 0 -1 1]
 * </pre>
 * <p/>
 * <p/>
 * <pre>
 *     ** Catmull-Rom curve and spline theory **
 *
 *     Given n+1 points {P0, P1, ...Pn} with n>=3 a catmull-rom spline interpolates
 *     the points {P1, p2, ...Pn-1}
 *
 *     The tangent at each point Pi is given by
 *     Ti = 0.5 * P(i+1) - P(i-1)
 *
 *     Each piece Ci(t) of the spline is a Hermite curve, where 1 <= i <= n-2
 *
 *     (eq a) Ci(t) = [P(i) P(i+1) T(i) T(i+1)] * M * [1 t t^2 t^3]'
 *
 *     Instead of having both points and tangent defining the geometry matrix we
 *     would like to express the geometry matrix given four points. We can do this
 *     using the following matrix and the tangent definition (Ti = 0.5 * P(i+1) - P(i-1))
 *
 *     (eq b) [P(i) P(i+1) T(i) T(i+1)] = [P(i-1) P(i) P(i+1) P(i+2)] * [ 0.0  0.0 -0.5  0.0
 *                                                                        1.0  0 0  0.0 -0.5
 *                                                                        0.0  1.0  0.5  0.0
 *                                                                        0.0  0.0  0.0  0.5]
 *
 *     Substituting equation b into equation a one can see that the basis matrix
 *     for the catmull-rom spline (Mcr) is the matrix product of the above matrix and the
 *     Hermit basis matrix.
 *
 *     [ 0.0  0.0 -0.5  0.0    [1 0 -3 2    [0.0 -0.5  1.0 -0.5
 *       1.0  0 0  0.0 -0.5     0 0 3 -2     1.0  0.0 -2.5  1.5
 *       0.0  1.0  0.5  0.0  *  0 1 -2 1  =  0.0  0.5  2.0 -1.5  =  Mcr
 *       0.0  0.0  0.0  0.5]    0 0 -1 1]    0.0  0.0 -0.5  0.5]
 *
 *     So, a **piece** of a catmull-rom spline is
 *
 *     Ci(t) = [P(i-1) P(i) P(i+1) P(i+2)] * Mcr * [1 t t^2 t^3]'
 *
 *     where [P(i-1) P(i) P(i+1) P(i+2)] is the geometry matrix (G)
 *
 *     where G = [g1 g2 g3 g4] = [(g1)x (g2)x (g3)x (g4)x
 *                                (g1)y (g2)y (g3)y (g4)y
 *                                (g1)z (g2)z (g3)z (g4)z]
 *
 * </pre>
 */
public class CatmullRomCurve {
    /**
     * Defined curve time start.
     */
    private static final float CURVE_TIME0 = 0;

    /**
     * Defined curve time end.
     */
    private static final float CURVE_TIME1 = 1;

    /**
     * The CatmullRom basis matrix. Transposed since Matrix implementation is multiplying
     * with matrix on left side and vector on right side. This is not what we want when
     * calculating the combination between the Geometry (4x3) and Basis matrix (4x4). We want
     * the matrix on the right side and vector on the left side.
     * <p/>
     * <pre>
     *     (3 rows, 4 cols) * (4 rows, 4 cols) = (3 rows, 4 cols).
     *     Geometry (3x4) * Basis (4x4) = GM (3x4)
     * </pre>
     */
    private static final Matrix44 sBasisMatrix = new CatmullRomBasisMatrix().transpose();

    /**
     * The time vector.
     */
    private final Vec4 mTimeVector = new Vec4();

    /**
     * Result of combining first row (x values from all points) in Geometry matrix with
     * CatmullRom basis matrix. According to theory above.
     */
    private final Vec4 mDx;

    /**
     * Result of combining second row (y values from all points) in Geometry matrix with
     * CatmullRom basis matrix. According to theory above.
     */
    private final Vec4 mDy;

    /**
     * Result of combining third row (z values from all points) in Geometry matrix with
     * CatmullRom basis matrix. According to theory above.
     */
    private final Vec4 mDz;

    /**
     * The curve length approximation.
     */
    private final float mLength;

    /**
     * CatmullRom curve. The curve is between second point and third point. The first and fourth
     * point are only used as tangent estimations. Note that the tangent control points affect the
     * direction into and out from the second and third point. It also affect the spacing in a way.
     *
     * @param p0             First point, tangent control point.
     * @param p1             Second point.
     * @param p2             Third point.
     * @param p3             Fourth point.
     * @param iterationCount Iterations used to approximate curve length using Riemann sum.
     */
    public CatmullRomCurve(Vec3 p0, Vec3 p1, Vec3 p2, Vec3 p3, int iterationCount) {
        mDx = new Vec4(p0.x, p1.x, p2.x, p3.x).transform(sBasisMatrix);
        mDy = new Vec4(p0.y, p1.y, p2.y, p3.y).transform(sBasisMatrix);
        mDz = new Vec4(p0.z, p1.z, p2.z, p3.z).transform(sBasisMatrix);
        mLength = calcLength(iterationCount, CURVE_TIME0, CURVE_TIME1);
    }

    /**
     * Calculate point on curve.
     *
     * @param t      Time, ranging from 0 to 1 including.
     * @param result The resulting point.
     */
    public void calcInterpolatedPoint(float t, Vec3 result) {
        setTimeVector(t, mTimeVector);
        result.x = mTimeVector.dot(mDx);
        result.y = mTimeVector.dot(mDy);
        result.z = mTimeVector.dot(mDz);
    }

    /**
     * Get the curve length.
     *
     * @return The curve length.
     */
    public float getLength() {
        return mLength;
    }

    /**
     * Calculates the length of the curve using middle Riemann integration.
     *
     * @param n - Number of iterations
     * @param a - From time
     * @param b - To time
     * @return - The length
     */
    private float calcLength(int n, float a, float b) {
        final Vec4 derivedTimeVector = new Vec4();
        final float dx = (b - a) / n;
        final float dxHalf = dx * 0.5f;
        float length = 0;
        for (int i = 0; i < n; i++) {
            float time = a + dxHalf + i * dx;
            setDerivedTimeVector(time, derivedTimeVector);
            float px = derivedTimeVector.dot(mDx);
            float py = derivedTimeVector.dot(mDy);
            float pz = derivedTimeVector.dot(mDz);
            float height = (float) Math.sqrt(px * px + py * py + pz * pz);
            length += dx * height;
        }
        return length;
    }

    /**
     * Set the time vector.
     *
     * @param t          The time, range 0 to 1 including.
     * @param timeVector The updated time vector.
     */
    private static void setTimeVector(float t, Vec4 timeVector) {
        timeVector.x = 1;
        timeVector.y = t;
        timeVector.z = t * t;
        timeVector.w = timeVector.z * t;
    }

    /**
     * Set the derived time vector.
     *
     * @param t                 The time, range 0 to 1 including.
     * @param derivedTimeVector The updated time vector.
     */
    private static void setDerivedTimeVector(float t, Vec4 derivedTimeVector) {
        derivedTimeVector.x = 0;
        derivedTimeVector.y = 1;
        derivedTimeVector.z = 2 * t;
        derivedTimeVector.w = 3 * t * t;
    }
}

package com.sweetlab.sweetride.game.curve;

import com.sweetlab.sweetride.math.Vec3;

import java.util.ArrayList;
import java.util.List;

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
 *     ** Catmull-Rom spline theory **
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
 * </pre>
 */
public class CatmullRom {
    /**
     * Builder to create a Catmull-Rom spline. The first and last point will not be part
     * of the spline but only be used as ending points for tangents.
     */
    public static class Builder {
        private List<Vec3> mPoints = new ArrayList<>();

        /**
         * Add point by reference.
         *
         * @param point The point.
         * @return This builder.
         */
        public Builder addPoint(Vec3 point) {
            mPoints.add(point);
            return this;
        }

        public CatmullRom build() {
            return new CatmullRom(mPoints);
        }
    }

    /**
     * Number of points must be at least 4.
     */
    public CatmullRom(List<Vec3> points) {
        if (points.size() < 4) {
            throw new RuntimeException("Can't create a spline with less than 4 points");
        }
        /**
         * 4 points, n = 3
         * first = 1
         * last = 1
         * pieces = 1
         *
         * 5 points, n = 4
         * first = 1
         * last = 2
         * pieces = 2;
         *
         * 6 points, n = 5
         * first = 1
         * last = 3
         * pieces = 3;
         *
         * 7 points, n = 6
         * first = 1
         * last = 4
         * pieces = 4;
         */
    }
}

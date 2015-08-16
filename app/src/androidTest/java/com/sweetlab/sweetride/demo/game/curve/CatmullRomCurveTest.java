package com.sweetlab.sweetride.demo.game.curve;

import com.sweetlab.sweetride.math.Vec3;

import junit.framework.TestCase;

/**
 * Test CatmullRom curve.
 */
public class CatmullRomCurveTest extends TestCase {

    public void testCalcInterpolatedPointStraightLine() throws Exception {
        Vec3 p0 = new Vec3(0, 0, 0);
        Vec3 p1 = new Vec3(1, 0, 0);
        Vec3 p2 = new Vec3(2, 0, 0);
        Vec3 p3 = new Vec3(3, 0, 0);
        CatmullRomCurve catmullRomCurve = new CatmullRomCurve(p0, p1, p2, p3, 20);

        Vec3 result = new Vec3();
        catmullRomCurve.calcInterpolatedPoint(0, result);
        assertEquals(1, result.x, 0.0001f);
        assertEquals(0, result.y, 0.0001f);
        catmullRomCurve.calcInterpolatedPoint(1, result);
        assertEquals(2, result.x, 0.0001f);
        assertEquals(0, result.y, 0.0001f);
    }

    public void testCalcInterpolatedPointSinusLike() throws Exception {
        Vec3 p0 = new Vec3(0, 0, 0);
        Vec3 p1 = new Vec3(1, 1, 0);
        Vec3 p2 = new Vec3(2, 0, 0);
        Vec3 p3 = new Vec3(3, -1, 0);
        CatmullRomCurve catmullRomCurve = new CatmullRomCurve(p0, p1, p2, p3, 20);

        Vec3 result = new Vec3();
        catmullRomCurve.calcInterpolatedPoint(0, result);
        assertEquals(1, result.x, 0.0001f);
        assertEquals(1, result.y, 0.0001f);
        catmullRomCurve.calcInterpolatedPoint(1, result);
        assertEquals(2, result.x, 0.0001f);
        assertEquals(0, result.y, 0.0001f);
    }
    public void testCalcLengthStraightLine() throws Exception {
        Vec3 p0 = new Vec3(0, 0, 0);
        Vec3 p1 = new Vec3(1, 0, 0);
        Vec3 p2 = new Vec3(2, 0, 0);
        Vec3 p3 = new Vec3(3, 0, 0);
        CatmullRomCurve catmullRomCurve = new CatmullRomCurve(p0, p1, p2, p3, 20);
        assertEquals(1, catmullRomCurve.getLength(), 0.0001f);
    }

    public void testCalcLengthSinusLike() throws Exception {
        Vec3 p0 = new Vec3(0, 0, 0);
        Vec3 p1 = new Vec3(1, 1, 0);
        Vec3 p2 = new Vec3(2, 0, 0);
        Vec3 p3 = new Vec3(3, -1, 0);
        CatmullRomCurve catmullRomCurve = new CatmullRomCurve(p0, p1, p2, p3, 20);
        assertTrue(Math.sqrt(2) < catmullRomCurve.getLength());
        assertTrue(2 > catmullRomCurve.getLength());
    }
}
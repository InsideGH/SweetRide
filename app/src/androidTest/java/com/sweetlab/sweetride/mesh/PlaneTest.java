package com.sweetlab.sweetride.mesh;

import com.sweetlab.sweetride.math.Vec3;

import junit.framework.TestCase;

/**
 * Plane test.
 */
public class PlaneTest extends TestCase {
    /**
     * Float compare.
     */
    private static final float EPS = 10e-6f;

    /**
     * Temp vec.
     */
    private Vec3 mOtherPoint = new Vec3();

    public void setUp() throws Exception {
        super.setUp();

    }

    public void test1() {
        Vec3 normal = new Vec3(0, -1, 0);
        Vec3 pointInPlane = new Vec3(0, 1, 0);

        Plane plane = new Plane(normal, pointInPlane);

        mOtherPoint.set(0, 5, 0);
        assertEquals(-4, plane.getSignedDistToPoint(mOtherPoint), EPS);
    }

    public void test2() {
        Vec3 normal = new Vec3(0, 1, 0);
        Vec3 pointInPlane = new Vec3(0, 1, 0);

        Plane plane = new Plane(normal, pointInPlane);

        mOtherPoint.set(0, 5, 0);
        assertEquals(4, plane.getSignedDistToPoint(mOtherPoint), EPS);
    }

    public void test3() {
        Vec3 normal = new Vec3(0, 1, 0);
        Vec3 pointInPlane = new Vec3(0, 1, 0);

        Plane plane = new Plane(normal, pointInPlane);

        mOtherPoint.set(5, -5, 0);
        Vec3 result = new Vec3();
        plane.getClosestPointInPlane(mOtherPoint, result);
    }

}
package com.sweetlab.sweetride.mesh;

import com.sweetlab.sweetride.demo.demo.mesh.CubeMesh;
import com.sweetlab.sweetride.demo.demo.mesh.QuadMesh;
import com.sweetlab.sweetride.geometry.Geometry;
import com.sweetlab.sweetride.intersect.BoundingBox;
import com.sweetlab.sweetride.intersect.Intersect;
import com.sweetlab.sweetride.intersect.Plane;
import com.sweetlab.sweetride.intersect.Ray;
import com.sweetlab.sweetride.math.FloatUtil;
import com.sweetlab.sweetride.math.Vec3;

import junit.framework.TestCase;

import java.util.Random;

/**
 * Test intersection.
 */
public class IntersectTest extends TestCase {

    private Intersect mIntersect;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mIntersect = new Intersect();
    }

    public void testClosestPoint1() {
        Vec3 origin = new Vec3(0, -10, 0);
        Vec3 direction = new Vec3(0, 1, 0);
        Ray ray = new Ray(origin, direction);

        Vec3 result = new Vec3();

        ray.getClosestPoint(new Vec3(200, -2, 0), result);
        assertTrue(result.equals(new Vec3(0, -2, 0)));
    }

    public void testClosestPoint2() {
        Vec3 origin = new Vec3(0, -10, 0);
        Vec3 direction = new Vec3(1, 1, 0);
        Ray ray = new Ray(origin, direction);

        Vec3 result = new Vec3();

        ray.getClosestPoint(new Vec3(10, 0, 0), result);
        assertTrue(result.equals(new Vec3(10, 0, 0)));
    }

    public void testRayIntersectionValid1() {
        Ray r1 = new Ray(new Vec3(), new Vec3(0, 345, 0));
        Ray r2 = new Ray(new Vec3(2234, 5, 0), new Vec3(-1, 0, 0));

        float intersects = mIntersect.intersect(r1, r2);
        assertEquals(5, intersects, FloatUtil.EPS);
        intersects = mIntersect.intersect(r2, r1);
        assertEquals(2234, intersects, FloatUtil.EPS);
    }

    public void testRayIntersectionNoIntersection() {
        Ray r1 = new Ray(new Vec3(), new Vec3(0, 345, 0));
        Ray r2 = new Ray(new Vec3(2234, 5, 0), new Vec3(1, 0, 0));

        float intersects = mIntersect.intersect(r1, r2);
        assertEquals(Intersect.INVALID_INTERSECTION, intersects);
        intersects = mIntersect.intersect(r2, r1);
        assertEquals(Intersect.INVALID_INTERSECTION, intersects);
    }

    public void testRayIntersectionParallel() {
        Ray r1 = new Ray(new Vec3(), new Vec3(0, 345, 0));
        Ray r2 = new Ray(new Vec3(), new Vec3(0, 345, 0));

        float intersects = mIntersect.intersect(r1, r2);
        assertEquals(Intersect.INVALID_INTERSECTION, intersects);
        intersects = mIntersect.intersect(r2, r1);
        assertEquals(Intersect.INVALID_INTERSECTION, intersects);
    }

    public void testRayIntersectionValid2() {
        Ray r1 = new Ray(new Vec3(), new Vec3(0, 345, 0));
        Ray r2 = new Ray(new Vec3(1, 1, 0), new Vec3(-1, -1, 0));

        float intersects = mIntersect.intersect(r1, r2);
        assertEquals(0, intersects, FloatUtil.EPS);

        intersects = mIntersect.intersect(r2, r1);
        assertEquals(Math.sqrt(2), intersects, FloatUtil.EPS);
    }

    public void testRayIntersectionValid3() {
        Ray r1 = new Ray(new Vec3(), new Vec3(0, 1, 0));
        Ray r2 = new Ray(new Vec3(2, 0, 0), new Vec3(-1, 0, 0));

        float intersects = mIntersect.intersect(r1, r2);
        assertEquals(0, intersects, FloatUtil.EPS);

        intersects = mIntersect.intersect(r2, r1);
        assertEquals(2, intersects, FloatUtil.EPS);
    }

    public void testRayIntersectionSkewed() {
        Ray r1 = new Ray(new Vec3(), new Vec3(0, 1, 0));
        Ray r2 = new Ray(new Vec3(2, 0, -10), new Vec3(-1, 0, 0));

        float intersects = mIntersect.intersect(r1, r2);
        assertEquals(Intersect.INVALID_INTERSECTION, intersects);

        intersects = mIntersect.intersect(r2, r1);
        assertEquals(Intersect.INVALID_INTERSECTION, intersects);
    }

    public void testRayHitBack() {
        Ray r1 = new Ray(new Vec3(), new Vec3(0, 1, 0));
        Plane plane = new Plane(new Vec3(0, 1, 0), new Vec3(0, 10, 0));

        float v = mIntersect.intersect(r1, plane);
        assertEquals(Float.NaN, v, FloatUtil.EPS);
    }

    /**
     * Ray origin at origo, beam up.
     * Plane at y 10, normal down.
     */
    public void testRayHitFront() {
        Ray r1 = new Ray(new Vec3(), new Vec3(0, 1, 0));
        Plane plane = new Plane(new Vec3(0, -1, 0), new Vec3(0, 10, 0));

        float v = mIntersect.intersect(r1, plane);
        assertEquals(10, v, FloatUtil.EPS);

        Vec3 pos = new Vec3();
        mIntersect.intersect(r1, plane, pos);
        assertTrue(pos.equals(new Vec3(0, 10, 0)));
    }

    /**
     * Ray at 10 y, beam up.
     * Plane below ray origin (at 2 y).
     */
    public void testRayNoHit() {
        Ray r1 = new Ray(new Vec3(0, 10, 0), new Vec3(0, 1, 0));
        Plane plane = new Plane(new Vec3(0, 1, 0), new Vec3(0, 2, 0));

        float v = mIntersect.intersect(r1, plane);
        assertEquals(Float.NaN, v, FloatUtil.EPS);

        Vec3 pos = new Vec3();
        mIntersect.intersect(r1, plane, pos);
        assertTrue(pos.equals(new Vec3()));
    }

    /**
     * Ray at 7 y, beam down.
     * Plane below ray origin at 1, normal up.
     */
    public void testRayHitFront2() {
        Ray r1 = new Ray(new Vec3(5, 7, 2), new Vec3(0, -1, 0));
        Plane plane = new Plane(new Vec3(0, 1, 0), new Vec3(30, 1, -60));

        float v = mIntersect.intersect(r1, plane);
        assertEquals(6, v, FloatUtil.EPS);

        Vec3 pos = new Vec3();
        mIntersect.intersect(r1, plane, pos);
        assertTrue(pos.equals(new Vec3(5, 1, 2)));
    }

    /**
     * Ray origin below plane, beam down.
     * Plane above ray origin, normal up.
     */
    public void testRayNoHit2() {
        Ray r1 = new Ray(new Vec3(5, -7, 2), new Vec3(0, -1, 0));
        Plane plane = new Plane(new Vec3(0, 1, 0), new Vec3(30, 1, -60));

        float v = mIntersect.intersect(r1, plane);
        assertEquals(Float.NaN, v, FloatUtil.EPS);

        Vec3 pos = new Vec3(234, 234, 35);
        mIntersect.intersect(r1, plane, pos);
        assertTrue(pos.equals(new Vec3(234, 234, 35)));
    }

    /**
     * Ray at 3 behind origo, beam forward.
     * Plane 45 degrees positive x/z through origo.
     */
    public void testRayHitFront3() {
        Ray r1 = new Ray(new Vec3(0, 0, 3), new Vec3(0, 0, -1));
        Plane plane = new Plane(new Vec3(1, 0, 1), new Vec3(0, 0, 0));

        float v = mIntersect.intersect(r1, plane);
        assertEquals(3, v, FloatUtil.EPS);

        Vec3 pos = new Vec3();
        mIntersect.intersect(r1, plane, pos);
        assertTrue(pos.equals(new Vec3(0, 0, 0)));
    }

    /**
     * Ray at 3 behind, 3 right of origo, beam forward.
     * Plane 45 degrees positive x/z through origo.
     */
    public void testRayHitFront4() {
        Plane plane = new Plane(new Vec3(1, 0, 1), new Vec3(0, 0, 0));

        Ray r1 = new Ray(new Vec3(3, 0, 3), new Vec3(0, 0, -1));
        float v = mIntersect.intersect(r1, plane);
        assertEquals(6, v, FloatUtil.EPS);

        Vec3 pos = new Vec3();
        mIntersect.intersect(r1, plane, pos);
        assertTrue(pos.equals(new Vec3(3, 0, -3)));
    }

    public void testRayHitBox1() {
        Geometry geometry = new Geometry();
        geometry.setMesh(new QuadMesh(1, 1, "", ""));
        BoundingBox box = geometry.getBoundingBox();

        Ray ray;
        ray = new Ray(new Vec3(0, 0, 3), new Vec3(0, 0, -1));
        assertTrue(mIntersect.intersects(ray, box));

        ray = new Ray(new Vec3(0, 0, 3), new Vec3(0, 0, 1));
        assertFalse(mIntersect.intersects(ray, box));

        ray = new Ray(new Vec3(0, 0, -3), new Vec3(0, 0, 1));
        assertTrue(mIntersect.intersects(ray, box));

        ray = new Ray(new Vec3(0, 0, -3), new Vec3(0, 0, -1));
        assertFalse(mIntersect.intersects(ray, box));

        geometry.getModelTransform().translate(10, 0, 0);

        ray = new Ray(new Vec3(0, 0, 3), new Vec3(0, 0, -1));
        assertFalse(mIntersect.intersects(ray, box));

        ray = new Ray(new Vec3(0, 0, 3), new Vec3(0, 0, 1));
        assertFalse(mIntersect.intersects(ray, box));

        ray = new Ray(new Vec3(0, 0, -3), new Vec3(0, 0, 1));
        assertFalse(mIntersect.intersects(ray, box));

        ray = new Ray(new Vec3(0, 0, -3), new Vec3(0, 0, -1));
        assertFalse(mIntersect.intersects(ray, box));

    }

    public void testRayHitBox2() {
        Geometry geometry = new Geometry();
        geometry.setMesh(new QuadMesh(1, 1, "", ""));
        BoundingBox box = geometry.getBoundingBox();

        /**
         * Rotate 90 deg around y and then translate along x (which is world -z)
         */
        geometry.getModelTransform().rotate(90, 0, 1, 0);
        geometry.getModelTransform().translate(10, 0, 0);

        Ray ray;
        ray = new Ray(new Vec3(0, 0, 3), new Vec3(0, 0, -1));
        assertFalse(mIntersect.intersects(ray, box));

        ray = new Ray(new Vec3(5, 0, -10.49f), new Vec3(-1, 0, 0));
        assertTrue(mIntersect.intersects(ray, box));

        ray = new Ray(new Vec3(5, 0, -9.51f), new Vec3(-1, 0, 0));
        assertTrue(mIntersect.intersects(ray, box));

        ray = new Ray(new Vec3(5, 0, -10.49f), new Vec3(1, 0, 0));
        assertFalse(mIntersect.intersects(ray, box));

        ray = new Ray(new Vec3(5, 0, -9.51f), new Vec3(1, 0, 0));
        assertFalse(mIntersect.intersects(ray, box));
    }

    public void testHitCube() {
        Random random = new Random(342);
        for (int i = 0; i < 50; i++) {
            float x = (random.nextFloat() - 0.5f) * 100;
            float y = (random.nextFloat() - 0.5f) * 100;
            float z = (random.nextFloat() - 0.5f) * 100;
            hitCube(new Vec3(x, y, z));
        }
    }

    /**
     * Hit cube box from all sides.
     *
     * @param cubeCenter The cube center.
     */
    private void hitCube(Vec3 cubeCenter) {
        Geometry geometry = new Geometry();
        geometry.setMesh(new CubeMesh("", ""));
        geometry.getModelTransform().translate(cubeCenter.x, cubeCenter.y, cubeCenter.z);
        geometry.getModelTransform().rotate(cubeCenter.x, cubeCenter.x, cubeCenter.y, cubeCenter.z);
        BoundingBox box = geometry.getBoundingBox();

        Ray ray;
        Vec3 rayOrigin = new Vec3();

        // Hit front
        rayOrigin.set(cubeCenter).add(0, 0, 3);
        ray = new Ray(rayOrigin, new Vec3(0, 0, -1));
        assertTrue(mIntersect.intersects(ray, box));

        // Hit right
        rayOrigin.set(cubeCenter).add(3, 0, 0);
        ray = new Ray(rayOrigin, new Vec3(-1, 0, 0));
        assertTrue(mIntersect.intersects(ray, box));

        // Hit left
        rayOrigin.set(cubeCenter).add(-3, 0, 0);
        ray = new Ray(rayOrigin, new Vec3(1, 0, 0));
        assertTrue(mIntersect.intersects(ray, box));

        // Hit top
        rayOrigin.set(cubeCenter).add(0, 3, 0);
        ray = new Ray(rayOrigin, new Vec3(0, -1, 0));
        assertTrue(mIntersect.intersects(ray, box));

        // Hit bottom
        rayOrigin.set(cubeCenter).add(0, -3, 0);
        ray = new Ray(rayOrigin, new Vec3(0, 1, 0));
        assertTrue(mIntersect.intersects(ray, box));
    }
}
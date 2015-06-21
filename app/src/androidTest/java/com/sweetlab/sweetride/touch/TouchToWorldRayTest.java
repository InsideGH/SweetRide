package com.sweetlab.sweetride.touch;

import android.test.AndroidTestCase;

import com.sweetlab.sweetride.demo.AndroidRenderNode;
import com.sweetlab.sweetride.math.Camera;
import com.sweetlab.sweetride.math.FloatUtil;
import com.sweetlab.sweetride.math.Frustrum;
import com.sweetlab.sweetride.math.Vec3;
import com.sweetlab.sweetride.ray.Ray;

/**
 * Test touch to ray generation. The ray generated originates somewhere from camera
 * near field rectangle (in world space), almost camera position.
 */
public class TouchToWorldRayTest extends AndroidTestCase {

    private Vec3 mOrigin = new Vec3();
    private Vec3 mDirection = new Vec3();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testProj1() {
        Camera camera = new Camera();
        camera.lookAt(0, 0, 3, 0, 0, -1);
        camera.getFrustrum().setPerspectiveProjection(90, Frustrum.FovType.AUTO_FIT, 0.1f, 10, 1080, 1920);

        TouchToWorldRay touchToWorldRay = new TouchToWorldRay(1080, 1920);

        Ray ray = touchToWorldRay.getRay(camera, 540, 960);

        ray.getOrigin(mOrigin);
        ray.getDirection(mDirection);
        assertEquals(2.9f, mOrigin.z, FloatUtil.EPS);
        assertTrue(mDirection.equals(new Vec3(0, 0, -1)));
    }

    public void testProj2() {
        Camera camera = new Camera();
        camera.lookAt(30, 0, 0, 0, 0, 0);
        camera.getFrustrum().setPerspectiveProjection(90, Frustrum.FovType.AUTO_FIT, 0.1f, 10, 1080, 1920);

        TouchToWorldRay touchToWorldRay = new TouchToWorldRay(1080, 1920);

        Ray ray = touchToWorldRay.getRay(camera, 540, 960);

        ray.getOrigin(mOrigin);
        ray.getDirection(mDirection);
        assertEquals(29.9f, mOrigin.x, FloatUtil.EPS);
        assertTrue(mDirection.equals(new Vec3(-1, 0, 0)));
    }

    public void testProj3() {
        Camera camera = new Camera();
        camera.lookAt(0, 0, -30, 0, 0, 0);
        camera.getFrustrum().setPerspectiveProjection(90, Frustrum.FovType.AUTO_FIT, 0.1f, 10, 1080, 1920);

        TouchToWorldRay touchToWorldRay = new TouchToWorldRay(1080, 1920);

        Ray ray = touchToWorldRay.getRay(camera, 540, 960);

        ray.getOrigin(mOrigin);
        ray.getDirection(mDirection);
        assertEquals(-29.9f, mOrigin.z, FloatUtil.EPS);
        assertTrue(mDirection.equals(new Vec3(0, 0, 1)));
    }

    public void testProj4() {
        Camera camera = new Camera();
        camera.lookAt(0, 0, 3, 0, 0, -1);
        camera.getFrustrum().setPerspectiveProjection(90, Frustrum.FovType.AUTO_FIT, 0.1f, 10, 1080, 1920);

        TouchToWorldRay touchToWorldRay = new TouchToWorldRay(1080, 1920);

        Ray ray = touchToWorldRay.getRay(camera, 0, 960);

        float left = camera.getFrustrum().getLeft();
        float near = camera.getFrustrum().getNear();

        /**
         * Touch at left/center. The ray should strike the left frustrum plane.
         */
        Vec3 rayDirection = new Vec3();
        Vec3.createVecFromPoints(new Vec3(0, 0, 0), new Vec3(left, 0, -near), rayDirection);
        rayDirection.norm();

        ray.getOrigin(mOrigin);
        ray.getDirection(mDirection);
        assertEquals(2.9f, mOrigin.z, FloatUtil.EPS);
        assertTrue(mDirection.equals(rayDirection));
    }

    public void testOrtho1() {
        AndroidRenderNode androidRenderNode = new AndroidRenderNode(1080, 1920);
        Camera camera = androidRenderNode.getCamera();

        TouchToWorldRay touchToWorldRay = new TouchToWorldRay(1080, 1920);

        Ray ray = touchToWorldRay.getRay(camera, 540, 960);

        ray.getOrigin(mOrigin);
        ray.getDirection(mDirection);
        assertEquals(2.9f, mOrigin.z, FloatUtil.EPS);
    }

    public void testOrtho2() {
        AndroidRenderNode androidRenderNode = new AndroidRenderNode(1080, 1920);
        Camera camera = androidRenderNode.getCamera();

        TouchToWorldRay touchToWorldRay = new TouchToWorldRay(1080, 1920);

        Ray ray = touchToWorldRay.getRay(camera, 0, 960);

        float left = camera.getFrustrum().getLeft();
        float near = camera.getFrustrum().getNear();

        /**
         * Touch at left/center. The ray should strike the left frustrum plane.
         */
        Vec3 rayDirection = new Vec3();
        Vec3.createVecFromPoints(new Vec3(0, 0, 0), new Vec3(left, 0, -near), rayDirection);
        rayDirection.norm();

        ray.getOrigin(mOrigin);
        ray.getDirection(mDirection);
        assertEquals(2.9f, mOrigin.z, FloatUtil.EPS);
        assertTrue(mDirection.equals(rayDirection));
    }
}
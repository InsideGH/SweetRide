package com.sweetlab.sweetride.math;

import android.test.AndroidTestCase;

import com.sweetlab.sweetride.engine.FrontEndActionHandler;

public class CameraTest extends AndroidTestCase {
    /**
     * Float compare.
     */
    private static final float EPS = 10e-6f;

    private Camera mCamera;
    private FrontEndActionHandler mActionHandler;

    private Vec3 mLook = new Vec3();
    private Vec3 mRight = new Vec3();
    private Vec3 mUp = new Vec3();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mCamera = new Camera();
        mActionHandler = new FrontEndActionHandler();
    }

    public void test1() {
        mCamera.lookAt(0, 0, 3, 0, 0, 0);
        mCamera.getFrustrum().setPerspectiveProjection(90, Frustrum.FovType.AUTO_FIT, 0.1f, 10, 1920, 1080);

        mActionHandler.handleActions(mCamera);

        mCamera.getLook(mLook);
        assertEquals(0, mLook.x, EPS);
        assertEquals(0, mLook.y, EPS);
        assertEquals(-1, mLook.z, EPS);

        mCamera.getRight(mRight);
        assertEquals(1, mRight.x, EPS);
        assertEquals(0, mRight.y, EPS);
        assertEquals(0, mRight.z, EPS);

        mCamera.getUp(mUp);
        assertEquals(0, mUp.x, EPS);
        assertEquals(1, mUp.y, EPS);
        assertEquals(0, mUp.z, EPS);
    }

    public void test2() {
        mCamera.lookAt(0, 0, -3, 0, 0, 0);
        mCamera.getFrustrum().setPerspectiveProjection(90, Frustrum.FovType.AUTO_FIT, 0.1f, 10, 1920, 1080);

        mActionHandler.handleActions(mCamera);

        mCamera.getLook(mLook);
        assertEquals(0, mLook.x, EPS);
        assertEquals(0, mLook.y, EPS);
        assertEquals(1, mLook.z, EPS);

        mCamera.getRight(mRight);
        assertEquals(-1, mRight.x, EPS);
        assertEquals(0, mRight.y, EPS);
        assertEquals(0, mRight.z, EPS);

        mCamera.getUp(mUp);
        assertEquals(0, mUp.x, EPS);
        assertEquals(1, mUp.y, EPS);
        assertEquals(0, mUp.z, EPS);
    }

    public void test3() {
        mCamera.lookAt(0, 0, 3, 3, 0, 0);
        mCamera.getFrustrum().setPerspectiveProjection(90, Frustrum.FovType.AUTO_FIT, 0.1f, 10, 1920, 1080);

        mActionHandler.handleActions(mCamera);

        mCamera.getLook(mLook);
        assertEquals((1/Math.sqrt(2)), mLook.x, EPS);
        assertEquals(0, mLook.y, EPS);
        assertEquals(-(1/Math.sqrt(2)), mLook.z, EPS);

        mCamera.getRight(mRight);
        assertEquals((1/Math.sqrt(2)), mRight.x, EPS);
        assertEquals(0, mRight.y, EPS);
        assertEquals((1/Math.sqrt(2)), mRight.z, EPS);

        mCamera.getUp(mUp);
        assertEquals(0, mUp.x, EPS);
        assertEquals(1, mUp.y, EPS);
        assertEquals(0, mUp.z, EPS);
    }

}
package com.sweetlab.sweetride.math;

import android.test.AndroidTestCase;

import com.sweetlab.sweetride.camera.Camera;
import com.sweetlab.sweetride.camera.Frustrum;
import com.sweetlab.sweetride.engine.FrontEndActionHandler;

public class CameraTest extends AndroidTestCase {
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
        assertEquals(0, mLook.x, FloatUtil.EPS);
        assertEquals(0, mLook.y, FloatUtil.EPS);
        assertEquals(-1, mLook.z, FloatUtil.EPS);

        mCamera.getRight(mRight);
        assertEquals(1, mRight.x, FloatUtil.EPS);
        assertEquals(0, mRight.y, FloatUtil.EPS);
        assertEquals(0, mRight.z, FloatUtil.EPS);

        mCamera.getUp(mUp);
        assertEquals(0, mUp.x, FloatUtil.EPS);
        assertEquals(1, mUp.y, FloatUtil.EPS);
        assertEquals(0, mUp.z, FloatUtil.EPS);
    }

    public void test2() {
        mCamera.lookAt(0, 0, -3, 0, 0, 0);
        mCamera.getFrustrum().setPerspectiveProjection(90, Frustrum.FovType.AUTO_FIT, 0.1f, 10, 1920, 1080);

        mActionHandler.handleActions(mCamera);

        mCamera.getLook(mLook);
        assertEquals(0, mLook.x, FloatUtil.EPS);
        assertEquals(0, mLook.y, FloatUtil.EPS);
        assertEquals(1, mLook.z, FloatUtil.EPS);

        mCamera.getRight(mRight);
        assertEquals(-1, mRight.x, FloatUtil.EPS);
        assertEquals(0, mRight.y, FloatUtil.EPS);
        assertEquals(0, mRight.z, FloatUtil.EPS);

        mCamera.getUp(mUp);
        assertEquals(0, mUp.x, FloatUtil.EPS);
        assertEquals(1, mUp.y, FloatUtil.EPS);
        assertEquals(0, mUp.z, FloatUtil.EPS);
    }

    public void test3() {
        mCamera.lookAt(0, 0, 3, 3, 0, 0);
        mCamera.getFrustrum().setPerspectiveProjection(90, Frustrum.FovType.AUTO_FIT, 0.1f, 10, 1920, 1080);

        mActionHandler.handleActions(mCamera);

        mCamera.getLook(mLook);
        assertEquals((1/Math.sqrt(2)), mLook.x, FloatUtil.EPS);
        assertEquals(0, mLook.y, FloatUtil.EPS);
        assertEquals(-(1/Math.sqrt(2)), mLook.z, FloatUtil.EPS);

        mCamera.getRight(mRight);
        assertEquals((1/Math.sqrt(2)), mRight.x, FloatUtil.EPS);
        assertEquals(0, mRight.y, FloatUtil.EPS);
        assertEquals((1/Math.sqrt(2)), mRight.z, FloatUtil.EPS);

        mCamera.getUp(mUp);
        assertEquals(0, mUp.x, FloatUtil.EPS);
        assertEquals(1, mUp.y, FloatUtil.EPS);
        assertEquals(0, mUp.z, FloatUtil.EPS);
    }

}
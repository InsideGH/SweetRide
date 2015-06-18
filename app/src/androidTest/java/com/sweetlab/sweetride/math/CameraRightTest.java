package com.sweetlab.sweetride.math;

import android.test.AndroidTestCase;

import com.sweetlab.sweetride.engine.FrontEndActionHandler;
import com.sweetlab.sweetride.mesh.Plane;

/**
 * Test camera.
 */
public class CameraRightTest extends AndroidTestCase {
    /**
     * Surface width
     */
    private static final int WIDTH = 1920;

    /**
     * Surface height.
     */
    private static final int HEIGHT = 1080;

    /**
     * Near field distance.
     */
    private static final float NEAR = 0.1f;

    /**
     * Far field distance.
     */
    private static final float FAR = 10f;

    /**
     * Action handler.
     */
    private FrontEndActionHandler mActionHandler = new FrontEndActionHandler();

    /**
     * Normal holder.
     */
    private Vec3 mNormal = new Vec3();

    /**
     * The camera.
     */
    private Camera mCamera;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mCamera = new Camera();
        /**
         * Test works only for 90 degrees field of view. The width is set higher than height to assure
         * the field of view is locked in horizontal mode. Could have set FovType to horizontal lock as
         * well.
         */
        mCamera.getFrustrum().setPerspectiveProjection(90, Frustrum.FovType.AUTO_FIT, NEAR, FAR, WIDTH, HEIGHT);
    }

    /**
     * Camera at origo, looking 45 to left.
     * Normal should point left.
     */
    public void testRightPlaneLeft45() {
        mCamera.lookAt(0, 0, 0, -1, 0, -1);
        mActionHandler.handleActions(mCamera);
        Plane rightPlane = mCamera.getRightPlane();
        rightPlane.getNormal(mNormal);
        assertEquals(-1, mNormal.x, FloatUtil.EPS);
        assertEquals(0, mNormal.y, FloatUtil.EPS);
        assertEquals(0, mNormal.z, FloatUtil.EPS);
        float signedDistToOrigo = rightPlane.getSignedDistToOrigo();
        assertEquals(0, signedDistToOrigo, FloatUtil.EPS);
    }

    /**
     * Camera at origo, looking 45 degrees to right.
     * Normal should point into screen.
     */
    public void testRightPlaneRight45() {
        mCamera.lookAt(0, 0, 0, 1, 0, -1);
        mActionHandler.handleActions(mCamera);
        Plane rightPlane = mCamera.getRightPlane();
        rightPlane.getNormal(mNormal);
        assertEquals(0, mNormal.x, FloatUtil.EPS);
        assertEquals(0, mNormal.y, FloatUtil.EPS);
        assertEquals(-1, mNormal.z, FloatUtil.EPS);
        float signedDistToOrigo = rightPlane.getSignedDistToOrigo();
        assertEquals(0, signedDistToOrigo, FloatUtil.EPS);
    }

    /**
     * Camera at origo, looking into screen.
     * Normal should point somewhat left into screen.
     */
    public void testRightPlaneDownZ() {
        mCamera.lookAt(0, 0, 0, 0, 0, -1);
        mActionHandler.handleActions(mCamera);
        Plane rightPlane = mCamera.getRightPlane();
        rightPlane.getNormal(mNormal);
        assertEquals(-((float) 1 / Math.sqrt(2)), mNormal.x, FloatUtil.EPS);
        assertEquals(0, mNormal.y, FloatUtil.EPS);
        assertEquals(-((float) 1 / Math.sqrt(2)), mNormal.z, FloatUtil.EPS);
        float signedDistToOrigo = rightPlane.getSignedDistToOrigo();
        assertEquals(0, signedDistToOrigo, FloatUtil.EPS);
    }

    /**
     * Camera at origo, looking right along positive x.
     * Normal should point somewhat right into screen.
     */
    public void testRightPlaneLookRight() {
        mCamera.lookAt(0, 0, 0, 1, 0, 0);
        mActionHandler.handleActions(mCamera);
        Plane rightPlane = mCamera.getRightPlane();
        rightPlane.getNormal(mNormal);
        assertEquals(((float) 1 / Math.sqrt(2)), mNormal.x, FloatUtil.EPS);
        assertEquals(0, mNormal.y, FloatUtil.EPS);
        assertEquals(-((float) 1 / Math.sqrt(2)), mNormal.z, FloatUtil.EPS);
        float signedDistToOrigo = rightPlane.getSignedDistToOrigo();
        assertEquals(0, signedDistToOrigo, FloatUtil.EPS);
    }

    /**
     * Camera at origo, looking left along negative x.
     * Normal should point left and out from screen.
     */
    public void testRightPlaneLookLeft() {
        mCamera.lookAt(0, 0, 0, -1, 0, 0);
        mActionHandler.handleActions(mCamera);
        Plane rightPlane = mCamera.getRightPlane();
        rightPlane.getNormal(mNormal);
        assertEquals(-((float) 1 / Math.sqrt(2)), mNormal.x, FloatUtil.EPS);
        assertEquals(0, mNormal.y, FloatUtil.EPS);
        assertEquals(((float) 1 / Math.sqrt(2)), mNormal.z, FloatUtil.EPS);
        float signedDistToOrigo = rightPlane.getSignedDistToOrigo();
        assertEquals(0, signedDistToOrigo, FloatUtil.EPS);
    }

    /**
     * Camera at origo, looking back.
     * Normal should point right/back.
     */
    public void testRightPlaneBackZ() {
        mCamera.lookAt(0, 0, 0, 0, 0, 1);
        mActionHandler.handleActions(mCamera);
        Plane rightPlane = mCamera.getRightPlane();
        rightPlane.getNormal(mNormal);
        assertEquals(((float) 1 / Math.sqrt(2)), mNormal.x, FloatUtil.EPS);
        assertEquals(0, mNormal.y, FloatUtil.EPS);
        assertEquals(((float) 1 / Math.sqrt(2)), mNormal.z, FloatUtil.EPS);
        float signedDistToOrigo = rightPlane.getSignedDistToOrigo();
        assertEquals(0, signedDistToOrigo, FloatUtil.EPS);
    }

    /**
     * Camera at origo, looking into screen from 3 distance from origo.
     */
    public void testRightPlaneDownZTranslatedBack() {
        mCamera.lookAt(0, 0, 3, 0, 0, 1);
        mActionHandler.handleActions(mCamera);
        Plane rightPlane = mCamera.getRightPlane();
        rightPlane.getNormal(mNormal);
        assertEquals(-((float) 1 / Math.sqrt(2)), mNormal.x, FloatUtil.EPS);
        assertEquals(0, mNormal.y, FloatUtil.EPS);
        assertEquals(-((float) 1 / Math.sqrt(2)), mNormal.z, FloatUtil.EPS);
        float signedDistToOrigo = rightPlane.getSignedDistToOrigo();
        assertEquals(3 * (((float) 1 / Math.sqrt(2))), signedDistToOrigo, FloatUtil.EPS);
    }

    /**
     * Camera offset back and left, looking into screen.
     * This should make the right plane intersect origo since the offset is
     * matching field of view.
     */
    public void testRightPlaneDownZTranslatedBackAndDown() {
        mCamera.lookAt(-3, 0, 3, -3, 0, -3);
        mActionHandler.handleActions(mCamera);
        Plane rightPlane = mCamera.getRightPlane();
        rightPlane.getNormal(mNormal);
        assertEquals(-((float) 1 / Math.sqrt(2)), mNormal.x, FloatUtil.EPS);
        assertEquals(0, mNormal.y, FloatUtil.EPS);
        assertEquals(-((float) 1 / Math.sqrt(2)), mNormal.z, FloatUtil.EPS);
        float signedDistToOrigo = rightPlane.getSignedDistToOrigo();
        assertEquals(0, signedDistToOrigo, FloatUtil.EPS);
    }
}
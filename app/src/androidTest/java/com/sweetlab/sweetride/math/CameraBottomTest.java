package com.sweetlab.sweetride.math;

import android.test.AndroidTestCase;

import com.sweetlab.sweetride.camera.Camera;
import com.sweetlab.sweetride.camera.Frustrum;
import com.sweetlab.sweetride.engine.FrontEndActionHandler;
import com.sweetlab.sweetride.intersect.Plane;

/**
 * Test camera.
 */
public class CameraBottomTest extends AndroidTestCase {
    /**
     * Surface width
     */
    private static final int WIDTH = 1080;

    /**
     * Surface height.
     */
    private static final int HEIGHT = 1920;

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
         * Test works only for 90 degrees field of view.
         */
        mCamera.getFrustrum().setPerspectiveProjection(90, Frustrum.FovType.AUTO_FIT, NEAR, FAR, WIDTH, HEIGHT);
    }

    /**
     * Camera at origo, looking down 45 degrees.
     * Normal should point into screen.
     */
    public void testBottomPlaneDown45() {
        mCamera.lookAt(0, 0, 0, 0, -1, -1);
        mActionHandler.handleActions(mCamera);
        Plane bottomPlane = mCamera.getBottomPlane();
        bottomPlane.getNormal(mNormal);
        assertEquals(0, mNormal.x, FloatUtil.EPS);
        assertEquals(0, mNormal.y, FloatUtil.EPS);
        assertEquals(-1, mNormal.z, FloatUtil.EPS);
        float signedDistToOrigo = bottomPlane.getSignedDistToOrigo();
        assertEquals(0, signedDistToOrigo, FloatUtil.EPS);
    }

    /**
     * Camera at origo, looking up 45 degrees.
     * Normal should point up.
     */
    public void testBottomPlaneUp45() {
        mCamera.lookAt(0, 0, 0, 0, 1, -1);
        mActionHandler.handleActions(mCamera);
        Plane bottomPlane = mCamera.getBottomPlane();
        bottomPlane.getNormal(mNormal);
        assertEquals(0, mNormal.x, FloatUtil.EPS);
        assertEquals(1, mNormal.y, FloatUtil.EPS);
        assertEquals(0, mNormal.z, FloatUtil.EPS);
        float signedDistToOrigo = bottomPlane.getSignedDistToOrigo();
        assertEquals(0, signedDistToOrigo, FloatUtil.EPS);
    }

    /**
     * Camera at origo, looking into screen.
     * Normal should point up/into screen.
     */
    public void testBottomPlaneDownZ() {
        mCamera.lookAt(0, 0, 0, 0, 0, -1);
        mActionHandler.handleActions(mCamera);
        Plane bottomPlane = mCamera.getBottomPlane();
        bottomPlane.getNormal(mNormal);
        assertEquals(0, mNormal.x, FloatUtil.EPS);
        assertEquals(((float) 1 / Math.sqrt(2)), mNormal.y, FloatUtil.EPS);
        assertEquals(-((float) 1 / Math.sqrt(2)), mNormal.z, FloatUtil.EPS);
        float signedDistToOrigo = bottomPlane.getSignedDistToOrigo();
        assertEquals(0, signedDistToOrigo, FloatUtil.EPS);
    }

    /**
     * Camera at origo, looking right.
     * Normal should point up/right.
     */
    public void testBottomPlaneLookRight() {
        mCamera.lookAt(0, 0, 0, 1, 0, 0);
        mActionHandler.handleActions(mCamera);
        Plane bottomPlane = mCamera.getBottomPlane();
        bottomPlane.getNormal(mNormal);
        assertEquals(((float) 1 / Math.sqrt(2)), mNormal.x, FloatUtil.EPS);
        assertEquals(((float) 1 / Math.sqrt(2)), mNormal.y, FloatUtil.EPS);
        assertEquals(0, mNormal.z, FloatUtil.EPS);
        float signedDistToOrigo = bottomPlane.getSignedDistToOrigo();
        assertEquals(0, signedDistToOrigo, FloatUtil.EPS);
    }

    /**
     * Camera at origo, looking left.
     * Normal should point up/left.
     */
    public void testBottomPlaneLookLeft() {
        mCamera.lookAt(0, 0, 0, -1, 0, 0);
        mActionHandler.handleActions(mCamera);
        Plane bottomPlane = mCamera.getBottomPlane();
        bottomPlane.getNormal(mNormal);
        assertEquals(-((float) 1 / Math.sqrt(2)), mNormal.x, FloatUtil.EPS);
        assertEquals(((float) 1 / Math.sqrt(2)), mNormal.y, FloatUtil.EPS);
        assertEquals(0, mNormal.z, FloatUtil.EPS);
        float signedDistToOrigo = bottomPlane.getSignedDistToOrigo();
        assertEquals(0, signedDistToOrigo, FloatUtil.EPS);
    }

    /**
     * Camera at origo, looking back.
     * Normal should point up/back.
     */
    public void testBottomPlaneBackZ() {
        mCamera.lookAt(0, 0, 0, 0, 0, 1);
        mActionHandler.handleActions(mCamera);
        Plane bottomPlane = mCamera.getBottomPlane();
        bottomPlane.getNormal(mNormal);
        assertEquals(0, mNormal.x, FloatUtil.EPS);
        assertEquals(((float) 1 / Math.sqrt(2)), mNormal.y, FloatUtil.EPS);
        assertEquals(((float) 1 / Math.sqrt(2)), mNormal.z, FloatUtil.EPS);
        float signedDistToOrigo = bottomPlane.getSignedDistToOrigo();
        assertEquals(0, signedDistToOrigo, FloatUtil.EPS);
    }

    /**
     * Camera moved back 3, looking into screen.
     */
    public void testBottomPlaneDownZTranslatedBack() {
        mCamera.lookAt(0, 0, 3, 0, 0, -1);
        mActionHandler.handleActions(mCamera);
        Plane bottomPlane = mCamera.getBottomPlane();
        bottomPlane.getNormal(mNormal);
        assertEquals(0, mNormal.x, FloatUtil.EPS);
        assertEquals(((float) 1 / Math.sqrt(2)), mNormal.y, FloatUtil.EPS);
        assertEquals(-((float) 1 / Math.sqrt(2)), mNormal.z, FloatUtil.EPS);
        float signedDistToOrigo = bottomPlane.getSignedDistToOrigo();
        assertEquals(3 * (((float) 1 / Math.sqrt(2))), signedDistToOrigo, FloatUtil.EPS);
    }

    /**
     * Camera offset back and up, looking into screen.
     * This should make the bottom plane intersect origo since the offset is
     * matching field of view.
     */
    public void testBottomPlaneDownZTranslatedBackAndDown() {
        mCamera.lookAt(0, 3, 3, 0, 3, -3);
        mActionHandler.handleActions(mCamera);
        Plane bottomPlane = mCamera.getBottomPlane();
        bottomPlane.getNormal(mNormal);
        assertEquals(0, mNormal.x, FloatUtil.EPS);
        assertEquals(((float) 1 / Math.sqrt(2)), mNormal.y, FloatUtil.EPS);
        assertEquals(-((float) 1 / Math.sqrt(2)), mNormal.z, FloatUtil.EPS);
        float signedDistToOrigo = bottomPlane.getSignedDistToOrigo();
        assertEquals(0, signedDistToOrigo, FloatUtil.EPS);
    }
}
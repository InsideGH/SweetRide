package com.sweetlab.sweetride.math;

import android.test.AndroidTestCase;

import com.sweetlab.sweetride.engine.FrontEndActionHandler;
import com.sweetlab.sweetride.mesh.Plane;

/**
 * Test camera.
 */
public class CameraTopTest extends AndroidTestCase {
    /**
     * Surface width
     */
    private static final int WIDTH = 1080;

    /**
     * Surface height.
     */
    private static final int HEIGHT = 1920;

    /**
     * Float compare.
     */
    private static final float EPS = 10e-6f;

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
     * Camera at origo, looking 45 degrees downwards into screen.
     * Normal should point down.
     */
    public void testTopPlaneDown45() {
        mCamera.lookAt(0, 0, 0, 0, -1, -1);
        mActionHandler.handleActions(mCamera);
        Plane topPlane = mCamera.getTopPlane();
        topPlane.getNormal(mNormal);
        assertEquals(0, mNormal.x, EPS);
        assertEquals(-1, mNormal.y, EPS);
        assertEquals(0, mNormal.z, EPS);
        float signedDistToOrigo = topPlane.getSignedDistToOrigo();
        assertEquals(0, signedDistToOrigo, EPS);
    }

    /**
     * Camera at origo, looking 45 degrees upwards into screen.
     * Normal should point into screen.
     */
    public void testTopPlaneUp45() {
        mCamera.lookAt(0, 0, 0, 0, 1, -1);
        mActionHandler.handleActions(mCamera);
        Plane topPlane = mCamera.getTopPlane();
        topPlane.getNormal(mNormal);
        assertEquals(0, mNormal.x, EPS);
        assertEquals(0, mNormal.y, EPS);
        assertEquals(-1, mNormal.z, EPS);
        float signedDistToOrigo = topPlane.getSignedDistToOrigo();
        assertEquals(0, signedDistToOrigo, EPS);
    }

    /**
     * Camera at origo, looking into screen.
     * Normal should point down/into screen.
     */
    public void testTopPlaneDownZ() {
        mCamera.lookAt(0, 0, 0, 0, 0, -1);
        mActionHandler.handleActions(mCamera);
        Plane topPlane = mCamera.getTopPlane();
        topPlane.getNormal(mNormal);
        assertEquals(0, mNormal.x, EPS);
        assertEquals(-((float) 1 / Math.sqrt(2)), mNormal.y, EPS);
        assertEquals(-((float) 1 / Math.sqrt(2)), mNormal.z, EPS);
        float signedDistToOrigo = topPlane.getSignedDistToOrigo();
        assertEquals(0, signedDistToOrigo, EPS);
    }

    /**
     * Camera at origo, looking right.
     * Normal should point right/down.
     */
    public void testTopPlaneLookRight() {
        mCamera.lookAt(0, 0, 0, 1, 0, 0);
        mActionHandler.handleActions(mCamera);
        Plane topPlane = mCamera.getTopPlane();
        topPlane.getNormal(mNormal);
        assertEquals(((float) 1 / Math.sqrt(2)), mNormal.x, EPS);
        assertEquals(-((float) 1 / Math.sqrt(2)), mNormal.y, EPS);
        assertEquals(0, mNormal.z, EPS);
        float signedDistToOrigo = topPlane.getSignedDistToOrigo();
        assertEquals(0, signedDistToOrigo, EPS);
    }

    /**
     * Camera at origo, looking left.
     * Normal should point down/left
     */
    public void testTopPlaneLookLeft() {
        mCamera.lookAt(0, 0, 0, -1, 0, 0);
        mActionHandler.handleActions(mCamera);
        Plane topPlane = mCamera.getTopPlane();
        topPlane.getNormal(mNormal);
        assertEquals(-((float) 1 / Math.sqrt(2)), mNormal.x, EPS);
        assertEquals(-((float) 1 / Math.sqrt(2)), mNormal.y, EPS);
        assertEquals(0, mNormal.z, EPS);
        float signedDistToOrigo = topPlane.getSignedDistToOrigo();
        assertEquals(0, signedDistToOrigo, EPS);
    }

    /**
     * Camera at origo, looking back.
     * Normal should point down/back.
     */
    public void testTopPlaneBackZ() {
        mCamera.lookAt(0, 0, 0, 0, 0, 1);
        mActionHandler.handleActions(mCamera);
        Plane topPlane = mCamera.getTopPlane();
        topPlane.getNormal(mNormal);
        assertEquals(0, mNormal.x, EPS);
        assertEquals(-((float) 1 / Math.sqrt(2)), mNormal.y, EPS);
        assertEquals(((float) 1 / Math.sqrt(2)), mNormal.z, EPS);
        float signedDistToOrigo = topPlane.getSignedDistToOrigo();
        assertEquals(0, signedDistToOrigo, EPS);
    }

    /**
     * Camera moved back 3 from origo looking into screen.
     * Normal should point down/into screen.
     */
    public void testTopPlaneDownZTranslatedBack() {
        mCamera.lookAt(0, 0, 3, 0, 0, -1);
        mActionHandler.handleActions(mCamera);
        Plane topPlane = mCamera.getTopPlane();
        topPlane.getNormal(mNormal);
        assertEquals(0, mNormal.x, EPS);
        assertEquals(-((float) 1 / Math.sqrt(2)), mNormal.y, EPS);
        assertEquals(-((float) 1 / Math.sqrt(2)), mNormal.z, EPS);
        float signedDistToOrigo = topPlane.getSignedDistToOrigo();
        assertEquals(3 * (((float) 1 / Math.sqrt(2))), signedDistToOrigo, EPS);
    }

    /**
     * Camera offset back and down, looking into screen.
     * This should make the top plane intersect origo since the offset is
     * matching field of view.
     * Normal should point down/into screen and touch origo.
     */
    public void testTopPlaneDownZTranslatedBackAndDown() {
        mCamera.lookAt(0, -3, 3, 0, -3, -3);
        mActionHandler.handleActions(mCamera);
        Plane topPlane = mCamera.getTopPlane();
        topPlane.getNormal(mNormal);
        assertEquals(0, mNormal.x, EPS);
        assertEquals(-((float) 1 / Math.sqrt(2)), mNormal.y, EPS);
        assertEquals(-((float) 1 / Math.sqrt(2)), mNormal.z, EPS);
        float signedDistToOrigo = topPlane.getSignedDistToOrigo();
        assertEquals(0, signedDistToOrigo, EPS);
    }
}
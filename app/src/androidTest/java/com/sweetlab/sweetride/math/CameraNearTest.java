package com.sweetlab.sweetride.math;

import android.test.AndroidTestCase;

import com.sweetlab.sweetride.engine.FrontEndActionHandler;
import com.sweetlab.sweetride.mesh.Plane;

import java.util.Random;

/**
 * Test camera.
 */
public class CameraNearTest extends AndroidTestCase {
    /**
     * Surface width
     */
    private static final int WIDTH = 1080;

    /**
     * Surface height.
     */
    private static final int HEIGHT = 1920;

    /**
     * Positive random limit.
     */
    private static final int RAND_POS_LIMIT = 1000;
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
     * Random.
     */
    private Random mRandom = new Random(666);

    /**
     * The camera.
     */
    private Camera mCamera;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mCamera = new Camera();
        mCamera.getFrustrum().setPerspectiveProjection(90, Frustrum.FovType.AUTO_FIT, NEAR, FAR, WIDTH, HEIGHT);
    }

    /**
     * Camera at origo, looking down down the z axis.
     */
    public void testNearPlaneDownZ() {
        float z = getPositiveRand(1);
        mCamera.lookAt(0, 0, 0, 0, 0, z);
        mActionHandler.handleActions(mCamera);

        Plane nearPlane = mCamera.getNearPlane();
        nearPlane.getNormal(mNormal);
        assertEquals(0, mNormal.x, EPS);
        assertEquals(0, mNormal.y, EPS);
        assertEquals(1, mNormal.z, EPS);
        float signedDistToOrigo = nearPlane.getSignedDistToOrigo();
        // Origo is back side
        assertEquals(-NEAR, signedDistToOrigo, EPS);
    }

    /**
     * Camera at origo, looking back along the z axis.
     */
    public void testNearPlaneBackZ() {
        float z = -getPositiveRand(1);
        mCamera.lookAt(0, 0, 0, 0, 0, z);
        mActionHandler.handleActions(mCamera);

        Plane nearPlane = mCamera.getNearPlane();
        nearPlane.getNormal(mNormal);
        assertEquals(0, mNormal.x, EPS);
        assertEquals(0, mNormal.y, EPS);
        assertEquals(-1, mNormal.z, EPS);
        float signedDistToOrigo = nearPlane.getSignedDistToOrigo();
        // Origo is back side
        assertEquals(-NEAR, signedDistToOrigo, EPS);
    }

    /**
     * Camera at origo, looking up along the y axis.
     */
    public void testNearPlaneUpY() {
        float y = getPositiveRand(1);
        mCamera.lookAt(0, 0, 0, 0, y, 0);
        mActionHandler.handleActions(mCamera);

        Plane nearPlane = mCamera.getNearPlane();
        nearPlane.getNormal(mNormal);
        assertEquals(0, mNormal.x, EPS);
        assertEquals(1, mNormal.y, EPS);
        assertEquals(0, mNormal.z, EPS);
        float signedDistToOrigo = nearPlane.getSignedDistToOrigo();
        // Origo is back side
        assertEquals(-NEAR, signedDistToOrigo, EPS);
    }

    /**
     * Camera at origo, looking down along the y axis.
     */
    public void testNearPlaneDownY() {
        float y = -getPositiveRand(1);
        mCamera.lookAt(0, 0, 0, 0, y, 0);
        mActionHandler.handleActions(mCamera);

        Plane nearPlane = mCamera.getNearPlane();
        nearPlane.getNormal(mNormal);
        assertEquals(0, mNormal.x, EPS);
        assertEquals(-1, mNormal.y, EPS);
        assertEquals(0, mNormal.z, EPS);
        float signedDistToOrigo = nearPlane.getSignedDistToOrigo();
        // Origo is back side
        assertEquals(-NEAR, signedDistToOrigo, EPS);
    }

    /**
     * Camera at origo, looking right along x axis.
     */
    public void testNearPlanePosX() {
        float x = getPositiveRand(1);
        mCamera.lookAt(0, 0, 0, x, 0, 0);
        mActionHandler.handleActions(mCamera);

        Plane nearPlane = mCamera.getNearPlane();
        nearPlane.getNormal(mNormal);
        assertEquals(1, mNormal.x, EPS);
        assertEquals(0, mNormal.y, EPS);
        assertEquals(0, mNormal.z, EPS);
        float signedDistToOrigo = nearPlane.getSignedDistToOrigo();
        // Origo is back side
        assertEquals(-NEAR, signedDistToOrigo, EPS);
    }

    /**
     * Camera at origo, looking left along x axis.
     */
    public void testNearPlaneNegX() {
        float x = -getPositiveRand(1);
        mCamera.lookAt(0, 0, 0, x, 0, 0);
        mActionHandler.handleActions(mCamera);

        Plane nearPlane = mCamera.getNearPlane();
        nearPlane.getNormal(mNormal);
        assertEquals(-1, mNormal.x, EPS);
        assertEquals(0, mNormal.y, EPS);
        assertEquals(0, mNormal.z, EPS);
        float signedDistToOrigo = nearPlane.getSignedDistToOrigo();
        // Origo is back side
        assertEquals(-NEAR, signedDistToOrigo, EPS);
    }

    /**
     * Camera at -20 z, looking at >30 z axis.
     */
    public void testNearPlaneDownZTranslated() {
        float z = getPositiveRand(30);
        mCamera.lookAt(0, 0, -20, 0, 0, z);
        mActionHandler.handleActions(mCamera);

        Plane nearPlane = mCamera.getNearPlane();
        nearPlane.getNormal(mNormal);
        assertEquals(0, mNormal.x, EPS);
        assertEquals(0, mNormal.y, EPS);
        assertEquals(1, mNormal.z, EPS);
        float signedDistToOrigo = nearPlane.getSignedDistToOrigo();
        // Origo is on front side
        assertEquals(20 - NEAR, signedDistToOrigo, EPS);
    }

    /**
     * Camera moved down the z axis, looking back along the z axis.
     */
    public void testNearPlaneBackZTranslated() {
        float z = -getPositiveRand(30);
        mCamera.lookAt(0, 0, 20, 0, 0, z);
        mActionHandler.handleActions(mCamera);

        Plane nearPlane = mCamera.getNearPlane();
        nearPlane.getNormal(mNormal);
        assertEquals(0, mNormal.x, EPS);
        assertEquals(0, mNormal.y, EPS);
        assertEquals(-1, mNormal.z, EPS);
        float signedDistToOrigo = nearPlane.getSignedDistToOrigo();
        // Origo is on front side
        assertEquals(20 - NEAR, signedDistToOrigo, EPS);
    }

    /**
     * Camera moved up along y axis, looking up along the y axis.
     */
    public void testNearPlaneUpYTranslated() {
        float y = getPositiveRand(30);
        mCamera.lookAt(0, 20, 0, 0, y, 0);
        mActionHandler.handleActions(mCamera);

        Plane nearPlane = mCamera.getNearPlane();
        nearPlane.getNormal(mNormal);
        assertEquals(0, mNormal.x, EPS);
        assertEquals(1, mNormal.y, EPS);
        assertEquals(0, mNormal.z, EPS);
        float signedDistToOrigo = nearPlane.getSignedDistToOrigo();
        // Origo is on back side
        assertEquals(-20 - NEAR, signedDistToOrigo, EPS);
    }

    /**
     * Camera moved up along y axis, looking down along the y axis.
     */
    public void testNearPlaneDownYTranslated() {
        float y = -getPositiveRand(30);
        mCamera.lookAt(0, 20, 0, 0, y, 0);
        mActionHandler.handleActions(mCamera);

        Plane nearPlane = mCamera.getNearPlane();
        nearPlane.getNormal(mNormal);
        assertEquals(0, mNormal.x, EPS);
        assertEquals(-1, mNormal.y, EPS);
        assertEquals(0, mNormal.z, EPS);
        float signedDistToOrigo = nearPlane.getSignedDistToOrigo();
        // Origo is on front side
        assertEquals(20 - NEAR, signedDistToOrigo, EPS);
    }

    /**
     * Camera moved right along x axis, looking right along x axis.
     */
    public void testNearPlanePosXTranslated() {
        float x = getPositiveRand(30);
        mCamera.lookAt(20, 0, 0, x, 0, 0);
        mActionHandler.handleActions(mCamera);

        Plane nearPlane = mCamera.getNearPlane();
        nearPlane.getNormal(mNormal);
        assertEquals(1, mNormal.x, EPS);
        assertEquals(0, mNormal.y, EPS);
        assertEquals(0, mNormal.z, EPS);
        float signedDistToOrigo = nearPlane.getSignedDistToOrigo();
        // Origo is on back side
        assertEquals(-20 - NEAR, signedDistToOrigo, EPS);
    }

    /**
     * Camera moved right along x axis, looking left along x axis.
     */
    public void testNearPlaneNegXTranslated() {
        float x = -getPositiveRand(30);
        mCamera.lookAt(20, 0, 0, x, 0, 0);
        mActionHandler.handleActions(mCamera);

        Plane nearPlane = mCamera.getNearPlane();
        nearPlane.getNormal(mNormal);
        assertEquals(-1, mNormal.x, EPS);
        assertEquals(0, mNormal.y, EPS);
        assertEquals(0, mNormal.z, EPS);
        float signedDistToOrigo = nearPlane.getSignedDistToOrigo();
        // Origo is on front side
        assertEquals(20 - NEAR, signedDistToOrigo, EPS);
    }

    public void testTurned() {
        float rand = getPositiveRand(30);
        Vec3 look = new Vec3(rand, rand, 0).norm();

        mCamera.lookAt(0, 0, 0, rand, rand, 0);
        mActionHandler.handleActions(mCamera);

        Plane nearPlane = mCamera.getNearPlane();
        nearPlane.getNormal(mNormal);
        assertEquals(look.x, mNormal.x, EPS);
        assertEquals(look.y, mNormal.y, EPS);
        assertEquals(look.z, mNormal.z, EPS);
        float signedDistToOrigo = nearPlane.getSignedDistToOrigo();
        // Origo is on back side
        assertEquals(-NEAR, signedDistToOrigo, EPS);
    }

    public void testTurnedMoved() {
        float x = getPositiveRand(30);
        float y = x;
        float z = 0;
        Vec3 pos = new Vec3(x, y, z);
        Vec3 lookPos = new Vec3(x + 1, y + 1, z);
        Vec3 lookVector = new Vec3(lookPos);
        lookVector.norm();

        mCamera.lookAt(pos, lookPos);
        mActionHandler.handleActions(mCamera);

        Plane nearPlane = mCamera.getNearPlane();
        nearPlane.getNormal(mNormal);
        assertEquals(lookVector.x, mNormal.x, EPS);
        assertEquals(lookVector.y, mNormal.y, EPS);
        assertEquals(lookVector.z, mNormal.z, EPS);
    }

    /**
     * A positive non zero random number.
     *
     * @param min Min value including.
     * @return A >= min random number.
     */
    public int getPositiveRand(int min) {
        return mRandom.nextInt(RAND_POS_LIMIT) + min;
    }
}
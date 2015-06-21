package com.sweetlab.sweetride.math;

import android.test.AndroidTestCase;

import com.sweetlab.sweetride.engine.FrontEndActionHandler;
import com.sweetlab.sweetride.plane.Plane;

import java.util.Random;

/**
 * Test camera.
 */
public class CameraFarTest extends AndroidTestCase {
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
    public void testFarPlaneDownZ() {
        float z = getPositiveRand(1);
        mCamera.lookAt(0, 0, 0, 0, 0, z);
        mActionHandler.handleActions(mCamera);

        Plane plane = mCamera.getFarPlane();
        plane.getNormal(mNormal);
        assertEquals(0, mNormal.x, FloatUtil.EPS);
        assertEquals(0, mNormal.y, FloatUtil.EPS);
        assertEquals(-1, mNormal.z, FloatUtil.EPS);
        float signedDistToOrigo = plane.getSignedDistToOrigo();
        // Origo is front side of plane
        assertEquals(FAR, signedDistToOrigo, FloatUtil.EPS);
    }

    /**
     * Camera at origo, looking back along the z axis.
     */
    public void testFarPlaneBackZ() {
        float z = -getPositiveRand(1);
        mCamera.lookAt(0, 0, 0, 0, 0, z);
        mActionHandler.handleActions(mCamera);

        Plane plane = mCamera.getFarPlane();
        plane.getNormal(mNormal);
        assertEquals(0, mNormal.x, FloatUtil.EPS);
        assertEquals(0, mNormal.y, FloatUtil.EPS);
        assertEquals(1, mNormal.z, FloatUtil.EPS);
        float signedDistToOrigo = plane.getSignedDistToOrigo();
        // Origo is front side
        assertEquals(FAR, signedDistToOrigo, FloatUtil.EPS);
    }

    /**
     * Camera at origo, looking up along the y axis.
     */
    public void testFarPlaneUpY() {
        float y = getPositiveRand(1);
        mCamera.lookAt(0, 0, 0, 0, y, 0);
        mActionHandler.handleActions(mCamera);

        Plane plane = mCamera.getFarPlane();
        plane.getNormal(mNormal);
        assertEquals(0, mNormal.x, FloatUtil.EPS);
        assertEquals(-1, mNormal.y, FloatUtil.EPS);
        assertEquals(0, mNormal.z, FloatUtil.EPS);
        float signedDistToOrigo = plane.getSignedDistToOrigo();
        // Origo is front side
        assertEquals(FAR, signedDistToOrigo, FloatUtil.EPS);
    }

    /**
     * Camera at origo, looking down along the y axis.
     */
    public void testFarPlaneDownY() {
        float y = -getPositiveRand(1);
        mCamera.lookAt(0, 0, 0, 0, y, 0);
        mActionHandler.handleActions(mCamera);

        Plane plane = mCamera.getFarPlane();
        plane.getNormal(mNormal);
        assertEquals(0, mNormal.x, FloatUtil.EPS);
        assertEquals(1, mNormal.y, FloatUtil.EPS);
        assertEquals(0, mNormal.z, FloatUtil.EPS);
        float signedDistToOrigo = plane.getSignedDistToOrigo();
        // Origo is front side
        assertEquals(FAR, signedDistToOrigo, FloatUtil.EPS);
    }

    /**
     * Camera at origo, looking right along x axis.
     */
    public void testFarPlanePosX() {
        float x = getPositiveRand(1);
        mCamera.lookAt(0, 0, 0, x, 0, 0);
        mActionHandler.handleActions(mCamera);

        Plane plane = mCamera.getFarPlane();
        plane.getNormal(mNormal);
        assertEquals(-1, mNormal.x, FloatUtil.EPS);
        assertEquals(0, mNormal.y, FloatUtil.EPS);
        assertEquals(0, mNormal.z, FloatUtil.EPS);
        float signedDistToOrigo = plane.getSignedDistToOrigo();
        // Origo is front side
        assertEquals(FAR, signedDistToOrigo, FloatUtil.EPS);
    }

    /**
     * Camera at origo, looking left along x axis.
     */
    public void testFarPlaneNegX() {
        float x = -getPositiveRand(1);
        mCamera.lookAt(0, 0, 0, x, 0, 0);
        mActionHandler.handleActions(mCamera);

        Plane plane = mCamera.getFarPlane();
        plane.getNormal(mNormal);
        assertEquals(1, mNormal.x, FloatUtil.EPS);
        assertEquals(0, mNormal.y, FloatUtil.EPS);
        assertEquals(0, mNormal.z, FloatUtil.EPS);
        float signedDistToOrigo = plane.getSignedDistToOrigo();
        // Origo is front side
        assertEquals(FAR, signedDistToOrigo, FloatUtil.EPS);
    }

    /**
     * Camera at -20 z, looking at >30 z axis.
     */
    public void testFarPlaneDownZTranslated() {
        float z = getPositiveRand(30);
        mCamera.lookAt(0, 0, -20, 0, 0, z);
        mActionHandler.handleActions(mCamera);

        Plane plane = mCamera.getFarPlane();
        plane.getNormal(mNormal);
        assertEquals(0, mNormal.x, FloatUtil.EPS);
        assertEquals(0, mNormal.y, FloatUtil.EPS);
        assertEquals(-1, mNormal.z, FloatUtil.EPS);
        float signedDistToOrigo = plane.getSignedDistToOrigo();
        // Origo is on back side since the FAR field is 10 and the camera is at -20.
        assertEquals(-20 + FAR, signedDistToOrigo, FloatUtil.EPS);
    }

    /**
     * Camera moved down the z axis, looking back along the z axis.
     */
    public void testFarPlaneBackZTranslated() {
        float z = -getPositiveRand(30);
        mCamera.lookAt(0, 0, 20, 0, 0, z);
        mActionHandler.handleActions(mCamera);

        Plane plane = mCamera.getFarPlane();
        plane.getNormal(mNormal);
        assertEquals(0, mNormal.x, FloatUtil.EPS);
        assertEquals(0, mNormal.y, FloatUtil.EPS);
        assertEquals(1, mNormal.z, FloatUtil.EPS);
        float signedDistToOrigo = plane.getSignedDistToOrigo();
        // Origo is on back side since the FAR field is 10 and the camera is at 20.
        assertEquals(-(20 - FAR), signedDistToOrigo, FloatUtil.EPS);
    }

    /**
     * Camera moved up along y axis, looking up along the y axis.
     */
    public void testFarPlaneUpYTranslated() {
        float y = getPositiveRand(30);
        mCamera.lookAt(0, 20, 0, 0, y, 0);
        mActionHandler.handleActions(mCamera);

        Plane plane = mCamera.getFarPlane();
        plane.getNormal(mNormal);
        assertEquals(0, mNormal.x, FloatUtil.EPS);
        assertEquals(-1, mNormal.y, FloatUtil.EPS);
        assertEquals(0, mNormal.z, FloatUtil.EPS);
        float signedDistToOrigo = plane.getSignedDistToOrigo();
        // Origo is on front side
        assertEquals(20 + FAR, signedDistToOrigo, FloatUtil.EPS);
    }

    /**
     * Camera moved up along y axis, looking down along the y axis.
     */
    public void testFarPlaneDownYTranslated() {
        float y = -getPositiveRand(30);
        mCamera.lookAt(0, 20, 0, 0, y, 0);
        mActionHandler.handleActions(mCamera);

        Plane plane = mCamera.getFarPlane();
        plane.getNormal(mNormal);
        assertEquals(0, mNormal.x, FloatUtil.EPS);
        assertEquals(1, mNormal.y, FloatUtil.EPS);
        assertEquals(0, mNormal.z, FloatUtil.EPS);
        float signedDistToOrigo = plane.getSignedDistToOrigo();
        // Origo is on back side since camera is at 20...far at 10....looking down the y axis.
        assertEquals(-(20 - FAR), signedDistToOrigo, FloatUtil.EPS);
    }

    /**
     * Camera moved right along x axis, looking right along x axis.
     */
    public void testFarPlanePosXTranslated() {
        float x = getPositiveRand(30);
        mCamera.lookAt(20, 0, 0, x, 0, 0);
        mActionHandler.handleActions(mCamera);

        Plane plane = mCamera.getFarPlane();
        plane.getNormal(mNormal);
        assertEquals(-1, mNormal.x, FloatUtil.EPS);
        assertEquals(0, mNormal.y, FloatUtil.EPS);
        assertEquals(0, mNormal.z, FloatUtil.EPS);
        float signedDistToOrigo = plane.getSignedDistToOrigo();
        // Origo is on front side
        assertEquals(20 + FAR, signedDistToOrigo, FloatUtil.EPS);
    }

    /**
     * Camera moved right along x axis, looking left along x axis.
     */
    public void testFarPlaneNegXTranslated() {
        float x = -getPositiveRand(30);
        mCamera.lookAt(20, 0, 0, x, 0, 0);
        mActionHandler.handleActions(mCamera);

        Plane plane = mCamera.getFarPlane();
        plane.getNormal(mNormal);
        assertEquals(1, mNormal.x, FloatUtil.EPS);
        assertEquals(0, mNormal.y, FloatUtil.EPS);
        assertEquals(0, mNormal.z, FloatUtil.EPS);
        float signedDistToOrigo = plane.getSignedDistToOrigo();
        // Origo is on back side
        assertEquals(-(20 - FAR), signedDistToOrigo, FloatUtil.EPS);
    }

    public void testTurned() {
        float rand = getPositiveRand(30);
        Vec3 look = new Vec3(rand, rand, 0).norm().neg();

        mCamera.lookAt(0, 0, 0, rand, rand, 0);
        mActionHandler.handleActions(mCamera);

        Plane plane = mCamera.getFarPlane();
        plane.getNormal(mNormal);
        assertEquals(look.x, mNormal.x, FloatUtil.EPS);
        assertEquals(look.y, mNormal.y, FloatUtil.EPS);
        assertEquals(look.z, mNormal.z, FloatUtil.EPS);
        float signedDistToOrigo = plane.getSignedDistToOrigo();
        // Origo is on front side
        assertEquals(FAR, signedDistToOrigo, FloatUtil.EPS);
    }

    public void testTurnedMoved() {
        float x = getPositiveRand(30);
        float y = x;
        float z = 0;
        Vec3 pos = new Vec3(x, y, z);
        Vec3 lookPos = new Vec3(x + 1, y + 1, z);
        Vec3 lookVector = new Vec3(lookPos).neg();
        lookVector.norm();

        mCamera.lookAt(pos, lookPos);
        mActionHandler.handleActions(mCamera);

        Plane plane = mCamera.getFarPlane();
        plane.getNormal(mNormal);
        assertEquals(lookVector.x, mNormal.x, FloatUtil.EPS);
        assertEquals(lookVector.y, mNormal.y, FloatUtil.EPS);
        assertEquals(lookVector.z, mNormal.z, FloatUtil.EPS);
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
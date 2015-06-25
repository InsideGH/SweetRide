package com.sweetlab.sweetride.math;

import android.test.AndroidTestCase;

import com.sweetlab.sweetride.camera.FirstPersonCamera;

/**
 * First person camera test.
 */
public class FirstPersonCameraTest extends AndroidTestCase {

    private FirstPersonCamera mCamera;

    private Vec3 mPos = new Vec3();
    private Vec3 mLook = new Vec3();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mCamera = new FirstPersonCamera();
    }

    public void testMove() {
        mCamera.getPosition(mPos);
        assertTrue(mPos.equals(new Vec3(0, 0, 0)));

        mCamera.update(3, 0, 0, 0);
        mCamera.getPosition(mPos);
        assertTrue(mPos.equals(new Vec3(3, 0, 0)));

        mCamera.update(0, 4, 0, 0);
        mCamera.getPosition(mPos);
        assertTrue(mPos.equals(new Vec3(3, 0, -4)));

        mCamera.update(1, 1, 0, 0);
        mCamera.getPosition(mPos);
        assertTrue(mPos.equals(new Vec3(4, 0, -5)));
    }


    public void testLookUp() {
        /**
         * Look up 45 deg
         */
        mCamera.update(0, 0, 45, 0);
        mCamera.getPosition(mPos);
        mCamera.getLook(mLook);

        assertTrue(mPos.equals(new Vec3(0, 0, 0)));
        assertTrue(mLook.equals(new Vec3(0, 1, -1).norm()));

        /**
         * Move forward along hypotenuse of triangle sqrt(1^2 + 1^2) and we end up at
         * y = 1 and z = -1.
         */
        mCamera.update(0, 1 * ((float) Math.sqrt(2)), 0, 0);
        mCamera.getPosition(mPos);
        assertTrue(mPos.equals(new Vec3(0, 1, -1)));

        /**
         * Turn around world y axis 180 degrees, still at same position.
         */
        mCamera.update(0, 0, 0, 180);
        mCamera.getPosition(mPos);
        assertTrue(mPos.equals(new Vec3(0, 1, -1)));

        /**
         * Look down 45 deg, i.e undoing the look up, still at same pos.
         */
        mCamera.update(0, 0, -45, 0);
        mCamera.getPosition(mPos);
        assertTrue(mPos.equals(new Vec3(0, 1, -1)));

        /**
         * Move forward (along look) and we should end up just above origo at
         * y = 1.
         */
        mCamera.update(0, 1, 0, 0);
        mCamera.getPosition(mPos);
        assertTrue(mPos.equals(new Vec3(0, 1, 0)));
    }

    public void testLookLeft() {
        /**
         * Look up 45 deg
         */
        mCamera.update(0, 0, 0, 90);
        mCamera.getPosition(mPos);
        mCamera.getLook(mLook);

        /**
         * Pos unchanged and looking left.
         */
        assertTrue(mPos.equals(new Vec3()));
        assertTrue(mLook.equals(new Vec3(-1, 0, 0)));
    }
}
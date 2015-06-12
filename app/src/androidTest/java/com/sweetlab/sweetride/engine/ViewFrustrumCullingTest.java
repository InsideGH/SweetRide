package com.sweetlab.sweetride.engine;

import android.test.AndroidTestCase;

import com.sweetlab.sweetride.math.Camera;
import com.sweetlab.sweetride.math.Frustrum;
import com.sweetlab.sweetride.math.Vec3;

/**
 * Test view frustrum culling.
 */
public class ViewFrustrumCullingTest extends AndroidTestCase {

    private ViewFrustrumCulling mCuller;
    private Camera mCamera;
    private FrontEndActionHandler mActionHandler;
    private Vec3 mPoint = new Vec3();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mCamera = new Camera();
        mCuller = new ViewFrustrumCulling();
        mActionHandler = new FrontEndActionHandler();
    }

    public void testNearFar() {
        mCamera.lookAt(0, 0, 3, 0, 0, 0);
        mCamera.getFrustrum().setPerspectiveProjection(90, Frustrum.FovType.AUTO_FIT, 0.1f, 10, 1920, 1080);
        mActionHandler.handleActions(mCamera);

        /**
         * Test backwards
         */
        mPoint.set(0, 0, 0);
        assertTrue(mCuller.isVisible(mPoint, mCamera));
        mPoint.set(0, 0, 1);
        assertTrue(mCuller.isVisible(mPoint, mCamera));
        mPoint.set(0, 0, 2);
        assertTrue(mCuller.isVisible(mPoint, mCamera));
        mPoint.set(0, 0, 2.8f);
        assertTrue(mCuller.isVisible(mPoint, mCamera));
        mPoint.set(0, 0, 2.9f);
        assertTrue(mCuller.isVisible(mPoint, mCamera));
        mPoint.set(0, 0, 2.91f);
        assertFalse(mCuller.isVisible(mPoint, mCamera));

        /**
         * Test forward.
         */
        mPoint.set(0, 0, -1);
        assertTrue(mCuller.isVisible(mPoint, mCamera));
        mPoint.set(0, 0, -2);
        assertTrue(mCuller.isVisible(mPoint, mCamera));
        mPoint.set(0, 0, -3);
        assertTrue(mCuller.isVisible(mPoint, mCamera));
        mPoint.set(0, 0, -4);
        assertTrue(mCuller.isVisible(mPoint, mCamera));
        mPoint.set(0, 0, -5);
        assertTrue(mCuller.isVisible(mPoint, mCamera));
        mPoint.set(0, 0, -6);
        assertTrue(mCuller.isVisible(mPoint, mCamera));
        mPoint.set(0, 0, -7);
        assertTrue(mCuller.isVisible(mPoint, mCamera));
        mPoint.set(0, 0, -8);
        assertFalse(mCuller.isVisible(mPoint, mCamera));

    }

    public void testLeftRight() {
        mCamera.lookAt(0, 0, 3, 0, 0, 0);
        mCamera.getFrustrum().setPerspectiveProjection(90, Frustrum.FovType.AUTO_FIT, 0.1f, 10, 1920, 1080);
        mActionHandler.handleActions(mCamera);

        /**
         * Test left.
         */
        mPoint.set(0, 0, 0);
        assertTrue(mCuller.isVisible(mPoint, mCamera));
        mPoint.set(-1, 0, 0);
        assertTrue(mCuller.isVisible(mPoint, mCamera));
        mPoint.set(-2, 0, 0);
        assertTrue(mCuller.isVisible(mPoint, mCamera));
        mPoint.set(-3, 0, 0);
        assertTrue(mCuller.isVisible(mPoint, mCamera));
        mPoint.set(-3.1f, 0, 0);
        assertFalse(mCuller.isVisible(mPoint, mCamera));
        mPoint.set(-4, 0, 0);
        assertFalse(mCuller.isVisible(mPoint, mCamera));

        /**
         * Test right
         */
        mPoint.set(0, 0, 0);
        assertTrue(mCuller.isVisible(mPoint, mCamera));
        mPoint.set(1, 0, 0);
        assertTrue(mCuller.isVisible(mPoint, mCamera));
        mPoint.set(2, 0, 0);
        assertTrue(mCuller.isVisible(mPoint, mCamera));
        mPoint.set(3, 0, 0);
        assertTrue(mCuller.isVisible(mPoint, mCamera));
        mPoint.set(3.1f, 0, 0);
        assertFalse(mCuller.isVisible(mPoint, mCamera));
        mPoint.set(4, 0, 0);
        assertFalse(mCuller.isVisible(mPoint, mCamera));
    }


    public void testUpDown() {
        mCamera.lookAt(0, 0, 3, 0, 0, 0);
        mCamera.getFrustrum().setPerspectiveProjection(90, Frustrum.FovType.VERTICAL_LOCK, 0.1f, 10, 1920, 1080);
        mActionHandler.handleActions(mCamera);

        /**
         * Test up.
         */
        mPoint.set(0, 0, 0);
        assertTrue(mCuller.isVisible(mPoint, mCamera));
        mPoint.set(0, 1, 0);
        assertTrue(mCuller.isVisible(mPoint, mCamera));
        mPoint.set(0, 2, 0);
        assertTrue(mCuller.isVisible(mPoint, mCamera));
        mPoint.set(0, 3, 0);
        assertTrue(mCuller.isVisible(mPoint, mCamera));
        mPoint.set(0, 3.1f, 0);
        assertFalse(mCuller.isVisible(mPoint, mCamera));

        /**
         * Test down.
         */
        mPoint.set(0, 0, 0);
        assertTrue(mCuller.isVisible(mPoint, mCamera));
        mPoint.set(0, -1, 0);
        assertTrue(mCuller.isVisible(mPoint, mCamera));
        mPoint.set(0, -2, 0);
        assertTrue(mCuller.isVisible(mPoint, mCamera));
        mPoint.set(0, -3, 0);
        assertTrue(mCuller.isVisible(mPoint, mCamera));
        mPoint.set(0, -3.1f, 0);
        assertFalse(mCuller.isVisible(mPoint, mCamera));
    }

    public void testCorners() {
        mCamera.lookAt(0, 0, 0, 0, 0, -1);

        float far = 10f;
        mCamera.getFrustrum().setPerspectiveProjection(90, Frustrum.FovType.AUTO_FIT, 0.1f, far, 1920, 1080);
        mActionHandler.handleActions(mCamera);

        /**
         * Having far at 10 and a field of view of 90 deg leads to a triangle of 10*10*sqrt(200).
         */

        /**
         * Test bottom left corner
         */
        mPoint.set(-10, 0, -10);
        assertTrue(mCuller.isVisible(mPoint, mCamera));
        mPoint.set(-11f, 0, -10);
        assertFalse(mCuller.isVisible(mPoint, mCamera));
        mPoint.set(-10f, 0, -11);
        assertFalse(mCuller.isVisible(mPoint, mCamera));

        /**
         * Test bottom right corner
         */
        mPoint.set(10, 0, -10);
        assertTrue(mCuller.isVisible(mPoint, mCamera));
        mPoint.set(11f, 0, -10);
        assertFalse(mCuller.isVisible(mPoint, mCamera));
        mPoint.set(10f, 0, -11);
        assertFalse(mCuller.isVisible(mPoint, mCamera));
    }

    public void testCameraBackwards() {
        mCamera.lookAt(0, 0, 0, 0, 0, 1);
        mCamera.getFrustrum().setPerspectiveProjection(90, Frustrum.FovType.VERTICAL_LOCK, 0.1f, 10, 1920, 1080);
        mActionHandler.handleActions(mCamera);

        mPoint.set(0, 0, 2);
        assertTrue(mCuller.isVisible(mPoint, mCamera));
        mPoint.set(0, 0, -2);
        assertFalse(mCuller.isVisible(mPoint, mCamera));
    }
}
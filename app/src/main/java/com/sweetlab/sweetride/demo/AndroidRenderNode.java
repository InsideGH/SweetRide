package com.sweetlab.sweetride.demo;

import com.sweetlab.sweetride.engine.DefaultRenderNode;
import com.sweetlab.sweetride.math.Camera;

/**
 * This is based on default render node, i.e it renders to default system window.
 * <p/>
 * It uses a orthographic camera. Add nodes to this render node.
 * The camera is setup with a frustrum that maps to android screen coordinates.
 */
public class AndroidRenderNode extends DefaultRenderNode {
    /**
     * Near field distance.
     */
    private static final float NEAR = 0.1f;

    /**
     * Near field distance.
     */
    private static final float FAR = 10f;

    /**
     * Camera distance from origo.
     */
    private static final float CAMERA_DISTANCE = 3f;

    /**
     * Head up display right value.
     */
    private final int mRight;

    /**
     * Head up display left value.
     */
    private final int mLeft;

    /**
     * Head up display top value.
     */
    private final int mTop;

    /**
     * Head up display bottom value.
     */
    private final int mBottom;

    /**
     * Orthogonal camera.
     */
    private Camera mCamera = new Camera();

    /**
     * Constructor.
     */
    public AndroidRenderNode(int surfaceWidth, int surfaceHeight) {
        mLeft = 0;
        mTop = 0;
        mRight = surfaceWidth;
        mBottom = surfaceHeight;
        mCamera.getFrustrum().setOrthographicProjection(mLeft, mRight, mBottom, mTop, NEAR, FAR, surfaceWidth, surfaceHeight);
        mCamera.lookAt(0, 0, CAMERA_DISTANCE, 0, 0, 0);
        setCamera(mCamera);
    }
}

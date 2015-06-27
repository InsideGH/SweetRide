package com.sweetlab.sweetride.rendernode;

import com.sweetlab.sweetride.camera.Camera;

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
     * Constructor.
     */
    public AndroidRenderNode(int surfaceWidth, int surfaceHeight) {
        int left = 0;
        int top = 0;
        Camera camera = new Camera();
        camera.getFrustrum().setOrthographicProjection(left, surfaceWidth, surfaceHeight, top, NEAR, FAR, surfaceWidth, surfaceHeight);
        camera.lookAt(0, 0, CAMERA_DISTANCE, 0, 0, 0);
        setCamera(camera);
    }
}

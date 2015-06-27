package com.sweetlab.sweetride.engine.rendernode;

import android.support.annotation.Nullable;

import com.sweetlab.sweetride.action.Action;
import com.sweetlab.sweetride.action.ActionId;
import com.sweetlab.sweetride.action.ActionThread;
import com.sweetlab.sweetride.camera.Camera;
import com.sweetlab.sweetride.node.Node;
import com.sweetlab.sweetride.node.NodeVisitor;
import com.sweetlab.sweetride.renderer.GeometryRenderer;

/**
 * A render node. Provides way of rendering geometry. Nodes can be added to this render node.
 */
public class RenderNode extends Node {
    /**
     * Action when camera has been set.
     */
    private final Action mCameraSet = new Action(this, ActionId.RENDER_NODE_CAMERA, ActionThread.MAIN);
    /**
     * The renderer to use.
     */
    private GeometryRenderer mGeometryRenderer;

    /**
     * The camera.
     */
    private Camera mCamera;

    /**
     * If view frustrum culling is enabled or not.
     */
    private boolean mIsViewFrustrumCullingEnabled;

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean handleAction(Action action) {
        if (super.handleAction(action)) {
            return true;
        }
        switch (action.getType()) {
            case RENDER_NODE_CAMERA:
                return true;
            default:
                return false;
        }
    }

    /**
     * Set the renderer to use.
     *
     * @param geometryRenderer Renderer.
     */
    public void setRenderer(GeometryRenderer geometryRenderer) {
        mGeometryRenderer = geometryRenderer;
    }

    /**
     * Set the camera.
     *
     * @param camera The camera.
     */
    public void setCamera(Camera camera) {
        if (mCamera != null) {
            disconnectNotifier(mCamera);
        }
        mCamera = camera;
        connectNotifier(mCamera);
        addAction(mCameraSet);
    }

    @Nullable
    @Override
    public Camera getCamera() {
        return mCamera;
    }

    /**
     * Get the renderer.
     *
     * @return The renderer.
     */
    public GeometryRenderer getRenderer() {
        return mGeometryRenderer;
    }

    /**
     * Set if view frustrum culling should be enabled or not.
     *
     * @param enable True if enabled.
     */
    public void enableViewFrustrumCulling(boolean enable) {
        mIsViewFrustrumCullingEnabled = enable;
    }

    /**
     * Check if view frustrum culling is enabled.
     *
     * @return True if enabled.
     */
    public boolean isViewFrustrumCullingEnabled() {
        return mIsViewFrustrumCullingEnabled;
    }
}

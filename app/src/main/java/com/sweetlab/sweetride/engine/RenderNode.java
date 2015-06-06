package com.sweetlab.sweetride.engine;

import android.support.annotation.Nullable;

import com.sweetlab.sweetride.action.Action;
import com.sweetlab.sweetride.action.ActionId;
import com.sweetlab.sweetride.action.HandleThread;
import com.sweetlab.sweetride.geometry.Geometry;
import com.sweetlab.sweetride.math.Camera;
import com.sweetlab.sweetride.node.Node;
import com.sweetlab.sweetride.node.NodeVisitor;
import com.sweetlab.sweetride.node.ReusableVisitor;
import com.sweetlab.sweetride.renderer.Renderer;

import java.util.List;

/**
 * A render node. Provides way of rendering geometry. Nodes can be added to this render node.
 */
public class RenderNode extends Node {
    /**
     * Action when camera has been set.
     */
    private Action mCameraSet = new Action(this, ActionId.RENDER_NODE_CAMERA, HandleThread.MAIN);
    /**
     * The renderer to use.
     */
    private Renderer mRenderer;

    /**
     * A reusable geometry extractor.
     */
    private ReusableVisitor<List<Geometry>> mCollector = new GeometryCollector();

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean handleAction(Action action) {
        super.handleAction(action);
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
     * @param renderer Renderer.
     */
    public void setRenderer(Renderer renderer) {
        mRenderer = renderer;
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

    /**
     * Get the camera, or null if not set.
     *
     * @return The camera.
     */
    @Nullable
    public Camera getCamera() {
        return mCamera;
    }

    /**
     * Get the renderer.
     *
     * @return The renderer.
     */
    public Renderer getRenderer() {
        return mRenderer;
    }

    /**
     * Collect all geometries by traversing the graph.
     *
     * @return The geometries.
     */
    public List<Geometry> collectGeometries() {
        mCollector.clearResult();
        accept(mCollector);
        return mCollector.getResult();
    }
}

package com.sweetlab.sweetride.node;

import com.sweetlab.sweetride.DebugOptions;
import com.sweetlab.sweetride.action.Action;
import com.sweetlab.sweetride.action.ActionThread;
import com.sweetlab.sweetride.action.GlobalActionId;
import com.sweetlab.sweetride.action.NoHandleNotifier;
import com.sweetlab.sweetride.camera.Camera;
import com.sweetlab.sweetride.camera.LowerLeftBox;
import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.math.Transform;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A general node.
 */
public class Node extends NoHandleNotifier<GlobalActionId> {
    /**
     * Action when world is dirty.
     */
    private final Action<GlobalActionId> mWorldDirty = new Action<>(this, GlobalActionId.NODE_WORLD_DIRTY, ActionThread.MAIN);

    /**
     * Action when camera has been set.
     */
    private final Action<GlobalActionId> mCameraSet = new Action<>(this, GlobalActionId.NODE_CAMERA, ActionThread.MAIN);

    /**
     * List of children.
     */
    private final List<Node> mChildren = new ArrayList<>();

    /**
     * The model transform.
     */
    private final Transform mModelTransform = new Transform();

    /**
     * The world transform.
     */
    private final MarkTransform mWorldTransform = new MarkTransform();

    /**
     * List of node controllers.
     */
    private final List<NodeController> mNodeControllers = new ArrayList<>();

    /**
     * The render settings.
     */
    private final RenderSettings mRenderSettings = new RenderSettings();

    /**
     * The parent.
     */
    private Node mParent;

    /**
     * The camera.
     */
    private Camera mCamera;

    /**
     * Constructor.
     */
    public Node() {
        connectNotifier(mModelTransform);
        connectNotifier(mRenderSettings);
    }

    @Override
    public boolean handleAction(Action<GlobalActionId> action) {
        switch (action.getType()) {
            case NODE_WORLD_DIRTY:
            case NODE_CAMERA:
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onActionAdded(Action<GlobalActionId> action) {
        super.onActionAdded(action);
        switch (action.getType()) {
            case NODE_WORLD_DIRTY:
                mWorldTransform.mark();
                break;

            case FRUSTRUM_UPDATED:
                LowerLeftBox vp = mCamera.getFrustrum().getViewPort();
                mRenderSettings.setViewPort(vp.getX(), vp.getY(), vp.getWidth(), vp.getHeight());
            case CAMERA_UPDATED:
            case TRANSFORM_UPDATED:
            case NODE_CAMERA:
                setWorldDirty();
                break;

            case RENDER_SETTINGS_DIRTY:
                invalidateWorldRenderSettings();
                break;
        }
    }

    /**
     * Draw this node.
     *
     * @param context The backend context.
     */
    public void draw(BackendContext context) {
        mRenderSettings.useSettings(context);
    }

    /**
     * Add a child.
     *
     * @param child Child to add.
     */
    public void addChild(Node child) {
        if (!mChildren.contains(child)) {
            mChildren.add(child);
            child.mParent = this;
            child.setWorldDirty();
            child.mRenderSettings.inherit(mRenderSettings);
        }
    }

    /**
     * Remove child.
     *
     * @param child Child to remove.
     */
    public void removeChild(Node child) {
        if (mChildren.remove(child)) {
            child.mParent = null;
        } else if (DebugOptions.DEBUG_NODE) {
            throw new RuntimeException("Trying to remove a child that doesn't exist");
        }
    }

    /**
     * Add node controller.
     *
     * @param nodeController The node controller.
     */
    public void addNodeController(NodeController nodeController) {
        mNodeControllers.add(nodeController);
    }

    /**
     * Get number of children.
     *
     * @return Number of children.
     */
    public int getChildCount() {
        return mChildren.size();
    }

    /**
     * Get child at index.
     *
     * @return The node.
     */
    public Node getChild(int index) {
        return mChildren.get(index);
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
     * Find camera by searching upwards in the graph.
     *
     * @return The camera or null if not found.
     */
    public Camera findCamera() {
        if (mCamera != null) {
            return mCamera;
        } else if (mParent != null) {
            return mParent.findCamera();
        } else {
            return null;
        }
    }

    /**
     * Get the model transform.
     *
     * @return The model transform.
     */
    public Transform getModelTransform() {
        return mModelTransform;
    }

    /**
     * Get the world transform.
     *
     * @return The world transform.
     */
    public Transform getWorldTransform() {
        if (mWorldTransform.isMarked()) {
            if (mParent != null) {
                mWorldTransform.combine(mParent.getWorldTransform(), mModelTransform);
            } else {
                mWorldTransform.set(mModelTransform);
            }
            mWorldTransform.clearMark();
        }
        return mWorldTransform;
    }

    /**
     * Accepts a node visitor.
     *
     * @param visitor The visitor.
     */
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Set world dirty.
     */
    public void setWorldDirty() {
        addAction(mWorldDirty);
        for (Node child : mChildren) {
            child.setWorldDirty();
        }
    }

    /**
     * Invalidate world render settings by forcing children nodes to
     * inherit parent render settings.
     */
    public void invalidateWorldRenderSettings() {
        for (Node child : mChildren) {
            child.mRenderSettings.inherit(mRenderSettings);
        }
    }

    /**
     * Called by the engine, each frame. All controllers all updated prior to onUpdate is called. Using iterator
     * because nodes can be added/removed during application update.
     *
     * @param dt Delta time between frames.
     */
    public void update(float dt) {
        if (!mNodeControllers.isEmpty()) {
            Iterator<NodeController> iterator = mNodeControllers.iterator();
            while (iterator.hasNext()) {
                if (!iterator.next().onUpdate(dt)) {
                    iterator.remove();
                }
            }
        }

        if (onUpdate(dt)) {
            Iterator<Node> iterator = mChildren.iterator();
            //noinspection WhileLoopReplaceableByForEach
            while (iterator.hasNext()) {
                iterator.next().update(dt);
            }
        }
    }

    /**
     * Get render settings.
     *
     * @return The render settings.
     */
    public RenderSettings getRenderSettings() {
        return mRenderSettings;
    }

    /**
     * Use render settings.
     *
     * @param context Backend context.
     */
    public void useRenderSettings(BackendContext context) {
        mRenderSettings.useSettings(context);
    }

    /**
     * Update is called each frame by the engine.
     *
     * @param dt Delta time between frames.
     * @return Return false to stop updates to children.
     */
    public boolean onUpdate(float dt) {
        return true;
    }
}

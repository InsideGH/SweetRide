package com.sweetlab.sweetride.node;

import com.sweetlab.sweetride.action.Action;
import com.sweetlab.sweetride.action.GlobalActionId;
import com.sweetlab.sweetride.action.ActionThread;
import com.sweetlab.sweetride.action.NoHandleNotifier;
import com.sweetlab.sweetride.camera.Camera;
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
     * The parent.
     */
    private Node mParent;

    /**
     * Constructor.
     */
    public Node() {
        connectNotifier(mModelTransform);
    }

    @Override
    public boolean handleAction(Action<GlobalActionId> action) {
        switch (action.getType()) {
            case NODE_WORLD_DIRTY:
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
            case CAMERA_UPDATED:
            case TRANSFORM_UPDATED:
            case RENDER_NODE_CAMERA:
                setWorldDirty();
                break;
        }
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
     * Find camera by searching upwards in the graph.
     *
     * @return The camera or null if not found.
     */
    public Camera findCamera() {
        Camera camera = getCamera();
        if (camera != null) {
            return camera;
        } else if (mParent != null) {
            return mParent.findCamera();
        } else {
            return null;
        }
    }

    /**
     * Get camera.
     *
     * @return The camera or null if no camera exist.
     */
    public Camera getCamera() {
        return null;
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
     * Update is called each frame by the engine.
     *
     * @param dt Delta time between frames.
     * @return Return false to stop updates to children.
     */
    public boolean onUpdate(float dt) {
        return true;
    }
}

package com.sweetlab.sweetride.node;

import com.sweetlab.sweetride.action.Action;
import com.sweetlab.sweetride.action.ActionId;
import com.sweetlab.sweetride.action.HandleThread;
import com.sweetlab.sweetride.action.NoHandleNotifier;
import com.sweetlab.sweetride.math.Camera;
import com.sweetlab.sweetride.math.Transform;

import java.util.ArrayList;
import java.util.List;

/**
 * A general node.
 */
public class Node extends NoHandleNotifier {
    /**
     * Action when world is dirty.
     */
    private Action mWorldDirty = new Action(this, ActionId.NODE_WORLD_DIRTY, HandleThread.MAIN);

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
     * The camera.
     */
    protected Camera mCamera;

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
    public boolean handleAction(Action action) {
        switch (action.getType()) {
            case NODE_WORLD_DIRTY:
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onActionAdded(Action action) {
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
}

package com.sweetlab.sweetride.node;

import com.sweetlab.sweetride.action.Action;
import com.sweetlab.sweetride.action.ActionNotifier;
import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.math.Camera;

import java.util.ArrayList;
import java.util.List;

/**
 * A general node.
 */
public class Node extends ActionNotifier {
    /**
     * The camera.
     */
    protected Camera mCamera;

    /**
     * List of children.
     */
    private List<Node> mChildren = new ArrayList<>();

    /**
     * The parent.
     */
    private Node mParent;

    /**
     * Add a child.
     *
     * @param node Child to add.
     */
    public void addChild(Node node) {
        if (!mChildren.contains(node)) {
            mChildren.add(node);
            node.mParent = this;
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
     * Accepts a node visitor.
     *
     * @param visitor The visitor.
     */
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void handleAction(Action action) {
        throw new RuntimeException("wtf");
    }

    @Override
    public void handleAction(BackendContext context, Action action) {
        throw new RuntimeException("wtf");
    }
}

package com.sweetlab.sweetride.node;

import java.util.ArrayList;
import java.util.List;

/**
 * A general node.
 */
public class Node {

    /**
     * List of children.
     */
    private List<Node> mChildren = new ArrayList<>();

    /**
     * Add a child.
     *
     * @param node Child to add.
     */
    public void addChild(Node node) {
        mChildren.add(node);
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
     * Accepts a node visitor.
     *
     * @param visitor The visitor.
     */
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}

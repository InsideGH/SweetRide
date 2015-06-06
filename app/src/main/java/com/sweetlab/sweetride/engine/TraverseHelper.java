package com.sweetlab.sweetride.engine;

import com.sweetlab.sweetride.node.Node;
import com.sweetlab.sweetride.node.NodeVisitor;

public class TraverseHelper {
    /**
     * Traverse depth first using visitor.
     *
     * @param node
     * @param visitor
     */
    public static void depthFirst(Node node, NodeVisitor visitor) {
        int childCount = node.getChildCount();
        for (int i = 0; i < childCount; i++) {
            node.getChild(i).accept(visitor);
        }
    }
}

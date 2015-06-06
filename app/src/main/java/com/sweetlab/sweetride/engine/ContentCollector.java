package com.sweetlab.sweetride.engine;

import com.sweetlab.sweetride.geometry.Geometry;
import com.sweetlab.sweetride.node.Node;
import com.sweetlab.sweetride.node.NodeVisitor;

/**
 * Collect graph content by doing a depth first traverse.
 */
public class ContentCollector implements NodeVisitor {
    /**
     * The provided content storage.
     */
    private GraphContent mContent;

    /**
     * Collect all children in graph using depth first traversal.
     *
     * @param parent  The parent to start traversing from.
     * @param content The content storage.
     */
    public void collect(Node parent, GraphContent content) {
        mContent = content;
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            parent.getChild(i).accept(this);
        }
    }

    @Override
    public void visit(Node node) {
        mContent.add(node);
        TraverseHelper.depthFirst(node, this);
    }

    @Override
    public void visit(Geometry geometry) {
        mContent.add(geometry);
        TraverseHelper.depthFirst(geometry, this);
    }

    @Override
    public void visit(RenderNode renderNode) {
        mContent.add(renderNode);
        TraverseHelper.depthFirst(renderNode, this);
    }
}

package com.sweetlab.sweetride.engine.frame.update;

import com.sweetlab.sweetride.engine.TraverseHelper;
import com.sweetlab.sweetride.geometry.Geometry;
import com.sweetlab.sweetride.node.Node;
import com.sweetlab.sweetride.node.NodeVisitor;
import com.sweetlab.sweetride.rendernode.RenderNode;

/**
 * Collect graph content by doing a depth first traverse.
 */
public class GraphContentCollector {
    /**
     * The provided graph content storage.
     */
    private GraphContent mContent;

    /**
     * The internal node visitor.
     */
    private NodeVisitor mInternalVisitor = new InternalVisitor();

    /**
     * Collect all children in graph using depth first traversal.
     *
     * @param parent  The parent to start traversing from.
     * @param content The content storage.
     */
    public void collect(Node parent, GraphContent content) {
        mContent = content;
        parent.accept(mInternalVisitor);
    }

    /**
     * The internal node visitor.
     */
    private class InternalVisitor implements NodeVisitor {
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
}

package com.sweetlab.sweetride.node;

import com.sweetlab.sweetride.rendernode.RenderNode;
import com.sweetlab.sweetride.geometry.Geometry;

/**
 * Node visitor.
 */
public interface NodeVisitor {
    /**
     * Visit a node.
     *
     * @param node The node that is visited.
     */
    void visit(Node node);

    /**
     * Visit a geometry node.
     *
     * @param geometry The geometry node that is visited.
     */
    void visit(Geometry geometry);

    /**
     * Visit the render node.
     *
     * @param renderNode The render node.
     */
    void visit(RenderNode renderNode);
}

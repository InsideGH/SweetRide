package com.sweetlab.sweetride.engine.rendernode;

import com.sweetlab.sweetride.engine.TraverseHelper;
import com.sweetlab.sweetride.geometry.Geometry;
import com.sweetlab.sweetride.node.Node;
import com.sweetlab.sweetride.node.NodeVisitor;

import java.util.List;

/**
 * Collect geometry for a specific render node. This collector ignores sub branches that
 * contains other render nodes.
 */
public class RenderGroupCollector implements NodeVisitor {
    /**
     * The provided storage to collect into.
     */
    private List<Geometry> mContent;

    /**
     * Collect all geometries belonging to this render node. Ignores other render node
     * sub-branches.
     *
     * @param parent Parent to start traversing from.
     * @param list   List of geometries.
     */
    public void collect(RenderNode parent, List<Geometry> list) {
        mContent = list;
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            parent.getChild(i).accept(this);
        }
    }

    @Override
    public void visit(Node node) {
        /**
         * Visiting a node, forget it and continue.
         */
        TraverseHelper.depthFirst(node, this);
    }

    @Override
    public void visit(Geometry geometry) {
        /**
         * Visiting geometry, remember it and continue.
         */
        mContent.add(geometry);
        TraverseHelper.depthFirst(geometry, this);
    }

    @Override
    public void visit(RenderNode renderNode) {
        /**
         * Visiting another render node, do not go down this road.
         */
    }
}

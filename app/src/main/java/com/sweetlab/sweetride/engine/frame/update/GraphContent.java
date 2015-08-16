package com.sweetlab.sweetride.engine.frame.update;

import com.sweetlab.sweetride.node.Node;
import com.sweetlab.sweetride.pool.Poolable;
import com.sweetlab.sweetride.rendernode.RenderNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Poolable graph content.
 */
public class GraphContent implements Poolable {
    /**
     * List of nodes.
     */
    private final List<Node> mNodes = new ArrayList<>();

    /**
     * List of render nodes.
     */
    private final List<RenderNode> mRenderNodes = new ArrayList<>();

    @Override
    public void reset() {
        mNodes.clear();
        mRenderNodes.clear();
    }

    /**
     * Add a node.
     *
     * @param node The node.
     */
    public void add(Node node) {
        mNodes.add(node);
    }

    /**
     * Add a render node.
     *
     * @param renderNode The render node.
     */
    public void add(RenderNode renderNode) {
        mRenderNodes.add(renderNode);
    }

    /**
     * Get the collected nodes and geometry nodes (excluding render nodes) by reference.
     *
     * @return The nodes by reference.
     */
    public List<Node> getNodes() {
        return mNodes;
    }

    /**
     * Get the collected render nodes by reference.
     *
     * @return The render nodes by reference.
     */
    public List<RenderNode> getRenderNodes() {
        return mRenderNodes;
    }
}

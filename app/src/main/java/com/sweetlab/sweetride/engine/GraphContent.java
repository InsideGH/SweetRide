package com.sweetlab.sweetride.engine;

import com.sweetlab.sweetride.engine.rendernode.RenderNode;
import com.sweetlab.sweetride.geometry.Geometry;
import com.sweetlab.sweetride.node.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Reusable graph content.
 */
public class GraphContent implements Reusable {
    /**
     * List of nodes.
     */
    private List<Node> mNodes = new ArrayList<>();

    /**
     * List of geometry nodes.
     */
    private List<Geometry> mGeometries = new ArrayList<>();

    /**
     * List of render nodes.
     */
    private List<RenderNode> mRenderNodes = new ArrayList<>();

    @Override
    public void reset() {
        mNodes.clear();
        mGeometries.clear();
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
     * Add a geometry.
     *
     * @param geometry The geometry.
     */
    public void add(Geometry geometry) {
        mGeometries.add(geometry);
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
     * Get the collected nodes.
     *
     * @return The nodes.
     */
    public List<Node> getNodes() {
        return mNodes;
    }

    /**
     * Get the collected geometries.
     *
     * @return The geometries.
     */
    public List<Geometry> getGeometries() {
        return mGeometries;
    }

    /**
     * Get the collected render nodes.
     *
     * @return The render nodes.
     */
    public List<RenderNode> getRenderNodes() {
        return mRenderNodes;
    }
}

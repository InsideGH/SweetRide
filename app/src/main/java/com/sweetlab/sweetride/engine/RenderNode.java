package com.sweetlab.sweetride.engine;

import com.sweetlab.sweetride.geometry.Geometry;
import com.sweetlab.sweetride.node.Node;
import com.sweetlab.sweetride.node.NodeVisitor;
import com.sweetlab.sweetride.node.ReusableVisitor;
import com.sweetlab.sweetride.renderer.Renderer;

import java.util.List;

/**
 * A render node. Provides way of rendering geometry.
 */
public class RenderNode extends Node {
    /**
     * The renderer to use.
     */
    private Renderer mRenderer;

    /**
     * A reusable geometry extractor.
     */
    private ReusableVisitor<List<Geometry>> mCollector = new GeometryCollector();

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Set the renderer to use.
     *
     * @param renderer Renderer.
     */
    public void setRenderer(Renderer renderer) {
        mRenderer = renderer;
    }

    /**
     * Get the renderer.
     *
     * @return The renderer.
     */
    public Renderer getRenderer() {
        return mRenderer;
    }

    /**
     * Collect all geometries by traversing the graph.
     *
     * @return The geometries.
     */
    public List<Geometry> collectGeometries() {
        mCollector.clearResult();
        accept(mCollector);
        return mCollector.getResult();
    }
}

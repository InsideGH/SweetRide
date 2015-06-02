package com.sweetlab.sweetride.engine;

import com.sweetlab.sweetride.geometry.Geometry;
import com.sweetlab.sweetride.node.Node;
import com.sweetlab.sweetride.node.ReusableVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Reusable 'depth first' geometry extractor. Geometries are added along the traverse
 * down the tree.
 */
public class GeometryCollector implements ReusableVisitor<List<Geometry>> {
    /**
     * The result.
     */
    private List<Geometry> mGeometries = new ArrayList<>();

    @Override
    public void clearResult() {
        mGeometries.clear();
    }

    @Override
    public List<Geometry> getResult() {
        return mGeometries;
    }

    @Override
    public void visit(Node node) {
        traverseDepthFirst(node);
    }

    @Override
    public void visit(Geometry geometry) {
        mGeometries.add(geometry);
        traverseDepthFirst(geometry);
    }

    @Override
    public void visit(RenderNode renderNode) {
        traverseDepthFirst(renderNode);
    }

    /**
     * Traverse depth first.
     *
     * @param node Node
     */
    private void traverseDepthFirst(Node node) {
        int childCount = node.getChildCount();
        for (int i = 0; i < childCount; i++) {
            node.getChild(i).accept(this);
        }
    }
}

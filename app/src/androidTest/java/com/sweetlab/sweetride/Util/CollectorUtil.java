package com.sweetlab.sweetride.Util;

import com.sweetlab.sweetride.engine.frame.update.GraphContent;
import com.sweetlab.sweetride.engine.frame.update.GraphContentCollector;
import com.sweetlab.sweetride.geometry.Geometry;
import com.sweetlab.sweetride.node.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Some collectors used by test.
 */
public class CollectorUtil {
    /**
     * Collect geometries.
     *
     * @param node Node to collect children from.
     * @return List of geometries.
     */
    public static List<Geometry> collectGeometries(Node node) {
        GraphContentCollector graphContentCollector = new GraphContentCollector();
        GraphContent graphContent = new GraphContent();
        graphContentCollector.collect(node, graphContent);
        List<Geometry> geometries = new ArrayList<>();
        List<Node> nodes = graphContent.getNodes();
        for (Node n : nodes) {
            /**
             * Since this is a test specific method I'm lazy and using
             * instance of here since the GraphContentCollector is collecting
             * both nodes and geometries into a common list. In real scenario
             * this is what is needed/wanted but in some test cases only the
             * geometries are wanted.
             */
            if (n instanceof Geometry) {
                geometries.add((Geometry) n);
            }
        }
        return geometries;
    }

    /**
     * Collect all Nodes/RenderNodes/Geometries.
     *
     * @param node The root to start from.
     * @return The list of collected node.
     */
    public static List<Node> collectNodes(Node node) {
        GraphContentCollector graphContentCollector = new GraphContentCollector();
        GraphContent graphContent = new GraphContent();
        graphContentCollector.collect(node, graphContent);
        return graphContent.getNodes();
    }
}

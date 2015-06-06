package com.sweetlab.sweetride.Util;

import com.sweetlab.sweetride.engine.ContentCollector;
import com.sweetlab.sweetride.engine.GraphContent;
import com.sweetlab.sweetride.engine.RenderNode;
import com.sweetlab.sweetride.geometry.Geometry;

import java.util.List;

/**
 * Some collectors.
 */
public class CollectorUtil {
    /**
     * Collect geometries.
     *
     * @param node Node to collect children from.
     * @return List of geometries.
     */
    public static List<Geometry> collectGeometries(RenderNode node) {
        ContentCollector contentCollector = new ContentCollector();
        GraphContent graphContent = new GraphContent();
        contentCollector.collect(node, graphContent);
        return graphContent.getGeometries();
    }
}

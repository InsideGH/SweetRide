package com.sweetlab.sweetride.engine;

import com.sweetlab.sweetride.geometry.Geometry;
import com.sweetlab.sweetride.renderer.GeometryRenderer;

import java.util.List;

public class RenderNodeTask {
    /**
     * List of geometries to render.
     */
    private final List<Geometry> mGeometries;

    /**
     * Renderer to use.
     */
    private final GeometryRenderer mRenderer;

    /**
     * Constructor.
     *
     * @param renderer   Renderer to use.
     * @param geometries List of geometries to render.
     */
    public RenderNodeTask(GeometryRenderer renderer, List<Geometry> geometries) {
        mRenderer = renderer;
        mGeometries = geometries;
    }

    /**
     * Get geometries.
     *
     * @return The list of geometries.
     */
    public List<Geometry> getGeometries() {
        return mGeometries;
    }

    /**
     * Get renderer.
     *
     * @return The renderer.
     */
    public GeometryRenderer getRenderer() {
        return mRenderer;
    }
}

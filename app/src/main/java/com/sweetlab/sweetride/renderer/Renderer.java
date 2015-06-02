package com.sweetlab.sweetride.renderer;

import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.geometry.Geometry;

import java.util.List;

/**
 * Renderer that renders geometry.
 */
public interface Renderer {
    /**
     * Render the provided list of geometries.
     *
     * @param context Backend context.
     * @param list    List of geometries to render.
     */
    void render(BackendContext context, List<Geometry> list);
}

package com.sweetlab.sweetride.renderer;

import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.geometry.Geometry;

import java.util.List;

/**
 * Default renderer. Renders to system window.
 */
public class DefaultGeometryRenderer implements GeometryRenderer {

    @Override
    public void render(BackendContext context, List<Geometry> list) {
        context.getFrameBufferTarget().useWindowFrameBuffer();
        for (Geometry geometry : list) {
            geometry.draw(context);
        }
    }
}

package com.sweetlab.sweetride.engine;

import com.sweetlab.sweetride.context.BackendActionHandler;
import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.geometry.Geometry;

import java.util.List;

/**
 * Draw frame.
 */
public class FrameDraw {
    /**
     * Draw list of render node tasks.
     *
     * @param context Backend context.
     * @param list    List of render node tasks.
     */
    public void draw(BackendContext context, List<RenderNodeTask> list) {
        /**
         * Get backend action handler.
         */
        final BackendActionHandler actionHandler = context.getActionHandler();

        for (RenderNodeTask renderNodeTask : list) {
            /**
             * Handle all GL actions.
             */
            final List<Geometry> geometries = renderNodeTask.getGeometries();
            for (Geometry geometry : geometries) {
                actionHandler.handleActions(geometry);
            }

            /**
             * Render geometries.
             */
            renderNodeTask.getRenderer().render(context, geometries);
        }
    }
}

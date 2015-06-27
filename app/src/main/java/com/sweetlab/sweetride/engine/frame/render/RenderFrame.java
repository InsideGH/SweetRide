package com.sweetlab.sweetride.engine.frame.render;

import com.sweetlab.sweetride.DebugOptions;
import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.engine.frame.RenderTask;
import com.sweetlab.sweetride.pool.RenderTaskPool;
import com.sweetlab.sweetride.util.Util;

import java.util.ArrayDeque;

/**
 * Render frame.
 */
public class RenderFrame {

    /**
     * The render task pool to recycle render tasks.
     */
    private final RenderTaskPool mTaskPool;

    /**
     * Constructor.
     *
     * @param taskPool Pool of render tasks object.
     */
    public RenderFrame(RenderTaskPool taskPool) {
        mTaskPool = taskPool;
    }

    /**
     * Render node tasks.
     *
     * @param context Backend context.
     * @param queue   Queue of render node tasks.
     */
    public void render(BackendContext context, ArrayDeque<RenderTask> queue) {
        while (!queue.isEmpty()) {
            RenderTask renderTask = queue.removeFirst();
            renderTask.handleActions(context);
            renderTask.render(context);

            /**
             * Recycle the render task.
             */
            mTaskPool.put(renderTask);

            /**
             * Check for errors.
             */
            if (DebugOptions.DEBUG_DRAW) {
                if (Util.hasGlError()) {
                    throw new RuntimeException("GL error during render");
                }
            }
        }
    }
}

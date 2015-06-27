package com.sweetlab.sweetride.engine.frame.update;

import com.sweetlab.sweetride.engine.frame.RenderTask;
import com.sweetlab.sweetride.pool.RenderTaskPool;
import com.sweetlab.sweetride.renderer.NodeRenderer;
import com.sweetlab.sweetride.rendernode.RenderNode;

import java.util.ArrayDeque;
import java.util.List;

/**
 * Creates render content based on graph content.
 */
public class RenderContentCreator {
    /**
     * A render node group geometry collector that is reusable.
     */
    private final RenderNodeContentCollector mGroupCollector = new RenderNodeContentCollector();

    /**
     * A pool of render tasks.
     */
    private final RenderTaskPool mTaskPool;

    /**
     * Constructor.
     *
     * @param taskPool The render task pool.
     */
    public RenderContentCreator(RenderTaskPool taskPool) {
        mTaskPool = taskPool;
    }

    /**
     * Create a list of render tasks.
     *
     * @param content     Graph content.
     * @param renderQueue The render queue.
     */
    public void create(GraphContent content, ArrayDeque<RenderTask> renderQueue) {
        /**
         * For each render node, collect nodes and place in a render node task and add
         * task to render queue.
         */
        List<RenderNode> renderNodes = content.getRenderNodes();
        for (RenderNode renderNode : renderNodes) {
            final NodeRenderer renderer = renderNode.getRenderer();
            if (renderer != null) {
                mGroupCollector.collect(renderNode);

                RenderTask task = mTaskPool.get();
                task.set(renderer, mGroupCollector.getResult());
                renderQueue.addLast(task);

                mGroupCollector.reset();
            }
        }
    }
}

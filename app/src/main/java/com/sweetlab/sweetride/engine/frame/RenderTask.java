package com.sweetlab.sweetride.engine.frame;

import com.sweetlab.sweetride.context.BackendActionHandler;
import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.node.Node;
import com.sweetlab.sweetride.pool.Reusable;
import com.sweetlab.sweetride.renderer.NodeRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * A task container used for rendering. Has nodes and a renderer.
 */
public class RenderTask implements Reusable {
    /**
     * List of nodes to render.
     */
    private final List<Node> mNodes = new ArrayList<>();

    /**
     * Renderer to use.
     */
    private NodeRenderer mRenderer;

    @Override
    public void reset() {
        mRenderer = null;
        mNodes.clear();
    }

    /**
     * Set task information.
     *
     * @param renderer Renderer to use.
     * @param nodes    List of nodes to render, shallow copy is created.
     */
    public void set(NodeRenderer renderer, List<Node> nodes) {
        mRenderer = renderer;
        mNodes.addAll(nodes);
    }

    /**
     * Handle all GL actions.
     *
     * @param context Backend context.
     */
    public void handleActions(BackendContext context) {
        BackendActionHandler actionHandler = context.getActionHandler();
        for (Node node : mNodes) {
            actionHandler.handleActions(node);
        }
    }

    /**
     * Render nodes.
     *
     * @param context Backend context.
     */
    public void render(BackendContext context) {
        mRenderer.render(context, mNodes);
    }
}

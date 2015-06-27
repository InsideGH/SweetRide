package com.sweetlab.sweetride.renderer;

import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.node.Node;

import java.util.List;

/**
 * Renderer that renders nodes.
 */
public interface NodeRenderer {
    /**
     * Render the provided list of nodes.
     *
     * @param context Backend context.
     * @param list    List of nodes to render.
     */
    void render(BackendContext context, List<Node> list);
}

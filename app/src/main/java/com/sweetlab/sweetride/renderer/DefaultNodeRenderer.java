package com.sweetlab.sweetride.renderer;

import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.node.Node;

import java.util.List;

/**
 * Default renderer. Renders to system window.
 */
public class DefaultNodeRenderer implements NodeRenderer {
    @Override
    public void render(BackendContext context, List<Node> list) {
        context.getFrameBufferTarget().useWindowFrameBuffer();
        for (Node node : list) {
            node.draw(context);
        }
    }
}

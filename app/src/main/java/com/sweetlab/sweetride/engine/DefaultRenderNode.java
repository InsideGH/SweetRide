package com.sweetlab.sweetride.engine;

import com.sweetlab.sweetride.renderer.DefaultRenderer;

/**
 * Default render node. Renders geometry to system window. Nodes can be added to this
 * render node.
 */
public class DefaultRenderNode extends RenderNode {
    /**
     * Constructor. Will create a default renderer that renders geometry to system window.
     */
    public DefaultRenderNode() {
        setRenderer(new DefaultRenderer());
    }
}

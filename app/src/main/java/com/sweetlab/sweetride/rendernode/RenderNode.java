package com.sweetlab.sweetride.rendernode;

import com.sweetlab.sweetride.node.Node;
import com.sweetlab.sweetride.node.NodeVisitor;
import com.sweetlab.sweetride.renderer.NodeRenderer;

/**
 * A render node. Provides way of rendering geometry. Nodes can be added to this render node.
 */
public class RenderNode extends Node {
    /**
     * The renderer to use.
     */
    private NodeRenderer mNodeRenderer;

    /**
     * If view frustrum culling is enabled or not.
     */
    private boolean mIsViewFrustrumCullingEnabled;

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Set the renderer to use.
     *
     * @param nodeRenderer Renderer.
     */
    public void setRenderer(NodeRenderer nodeRenderer) {
        mNodeRenderer = nodeRenderer;
    }

    /**
     * Get the renderer.
     *
     * @return The renderer.
     */
    public NodeRenderer getRenderer() {
        return mNodeRenderer;
    }

    /**
     * Set if view frustrum culling should be enabled or not.
     *
     * @param enable True if enabled.
     */
    public void enableViewFrustrumCulling(boolean enable) {
        mIsViewFrustrumCullingEnabled = enable;
    }

    /**
     * Check if view frustrum culling is enabled.
     *
     * @return True if enabled.
     */
    public boolean isViewFrustrumCullingEnabled() {
        return mIsViewFrustrumCullingEnabled;
    }
}

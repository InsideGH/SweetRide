package com.sweetlab.sweetride.engine.frame.update;

import com.sweetlab.sweetride.camera.Camera;
import com.sweetlab.sweetride.camera.ViewFrustrumCulling;
import com.sweetlab.sweetride.engine.TraverseHelper;
import com.sweetlab.sweetride.geometry.Geometry;
import com.sweetlab.sweetride.node.Node;
import com.sweetlab.sweetride.node.NodeVisitor;
import com.sweetlab.sweetride.pool.Poolable;
import com.sweetlab.sweetride.rendernode.RenderNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Collect nodes for a specific render node. This collector ignores sub branches that
 * contains other render nodes. If view frustrum culling is enabled in the render node
 * geometries will be culled and not collected if they are outside view frustrum.
 */
public class RenderNodeContentCollector implements Poolable {
    /**
     * View frustrum culling.
     */
    private final ViewFrustrumCulling mViewFrustrumCulling = new ViewFrustrumCulling();

    /**
     * The provided storage to collect into.
     */
    private List<Node> mContent = new ArrayList<>();

    /**
     * The parent render node.
     */
    private RenderNode mParent;

    /**
     * Internal node visitor.
     */
    private InternalVisitor mVisitor = new InternalVisitor();

    @Override
    public void reset() {
        mContent.clear();
        mParent = null;
    }

    /**
     * Collect all geometries belonging to this render node (including this). Ignores other render node
     * sub-branches.
     *
     * @param parent Parent to start traversing from.
     */
    public void collect(RenderNode parent) {
        mParent = parent;
        mContent.add(parent);
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            parent.getChild(i).accept(mVisitor);
        }
    }

    /**
     * Get content by reference.
     *
     * @return The content by reference.
     */
    public List<Node> getResult() {
        return mContent;
    }

    /**
     * Internal node visitor.
     */
    private class InternalVisitor implements NodeVisitor {
        @Override
        public void visit(Node node) {
            mContent.add(node);
            TraverseHelper.depthFirst(node, this);
        }

        @Override
        public void visit(Geometry geometry) {
            if (mParent.isViewFrustrumCullingEnabled()) {
                Camera camera = geometry.findCamera();
                if (camera != null && mViewFrustrumCulling.isVisible(geometry, camera)) {
                    mContent.add(geometry);
                }
            } else {
                mContent.add(geometry);
            }
            TraverseHelper.depthFirst(geometry, this);
        }

        @Override
        public void visit(RenderNode renderNode) {
            /**
             * Visiting another render node, do not continue.
             */
        }
    }
}

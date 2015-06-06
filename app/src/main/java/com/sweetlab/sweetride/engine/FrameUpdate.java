package com.sweetlab.sweetride.engine;

import android.os.SystemClock;

import com.sweetlab.sweetride.UserApplication;
import com.sweetlab.sweetride.geometry.Geometry;
import com.sweetlab.sweetride.node.Node;
import com.sweetlab.sweetride.pool.GraphContentPool;
import com.sweetlab.sweetride.renderer.GeometryRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * A frame update.
 */
public class FrameUpdate {
    /**
     * Delta time.
     */
    private DeltaTime mDeltaTime = new DeltaTime();

    /**
     * A pool of graph content storage objects.
     */
    private GraphContentPool mGraphContentPool = new GraphContentPool();

    /**
     * Graph content collector.
     */
    private ContentCollector mContentCollector = new ContentCollector();

    /**
     * A render node group geometry collector.
     */
    private RenderGroupCollector mGroupCollector = new RenderGroupCollector();

    /**
     * The action handler
     */
    private FrontEndActionHandler mActionHandler = new FrontEndActionHandler();

    /**
     * A frame update.
     *
     * @param application The user application.
     * @param root        The root node to start traverse from.
     * @return Render content.
     */
    public List<RenderNodeTask> update(UserApplication application, Node root) {
        /**
         * Run application and graph update.
         */
        userUpdate(application, root);

        /**
         * Get a graph content storage from pool.
         */
        GraphContent graphContent = mGraphContentPool.get();

        /**
         * Collect graph content into graph content storage.
         */
        mContentCollector.collect(root, graphContent);

        /**
         * Handle all actions.
         */
        handleActions(graphContent);

        /**
         * Create render content.
         */
        List<RenderNodeTask> renderList = createRenderContent(graphContent);

        /**
         * Pool the graph content storage.
         */
        mGraphContentPool.put(graphContent);

        /**
         * Return render content.
         */
        return renderList;
    }


    /**
     * Update user application and graph.
     */
    private void userUpdate(UserApplication application, Node root) {
        /**
         * Delta time since last frame.
         */
        float delta = mDeltaTime.get(SystemClock.uptimeMillis()) / 1000f;

        /**
         * Update application.
         */
        application.onUpdate(delta);

        /**
         * Update graph.
         */
        root.update(delta);
    }

    /**
     * Handle all actions generated during user update.
     */
    private void handleActions(GraphContent content) {
        List<RenderNode> renderNodes = content.getRenderNodes();
        for (Node node : renderNodes) {
            mActionHandler.handleActions(node);
        }
        List<Node> nodes = content.getNodes();
        for (Node node : nodes) {
            mActionHandler.handleActions(node);
        }
        List<Geometry> geometries = content.getGeometries();
        for (Node node : geometries) {
            mActionHandler.handleActions(node);
        }
    }

    /**
     * Create a list render tasks.
     *
     * @param content Graph content.
     */
    private List<RenderNodeTask> createRenderContent(GraphContent content) {
        /**
         * A list of render node tasks.
         */
        List<RenderNodeTask> renderList = new ArrayList<>();

        /**
         * For each render node, collect geometries and place in a render node task.
         */
        List<RenderNode> renderNodes = content.getRenderNodes();
        for (RenderNode renderNode : renderNodes) {
            final GeometryRenderer renderer = renderNode.getRenderer();
            if (renderer != null) {
                List<Geometry> list = new ArrayList<>();
                mGroupCollector.collect(renderNode, list);
                renderList.add(new RenderNodeTask(renderer, list));
            }
        }
        return renderList;
    }
}

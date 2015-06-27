package com.sweetlab.sweetride.engine.frame.update;

import android.os.SystemClock;

import com.sweetlab.sweetride.UserApplication;
import com.sweetlab.sweetride.engine.FrontEndActionHandler;
import com.sweetlab.sweetride.engine.frame.RenderTask;
import com.sweetlab.sweetride.node.Node;
import com.sweetlab.sweetride.pool.GraphContentPool;
import com.sweetlab.sweetride.pool.RenderTaskPool;
import com.sweetlab.sweetride.rendernode.RenderNode;

import java.util.ArrayDeque;
import java.util.List;

/**
 * A frame update.
 */
public class UpdateFrame {
    /**
     * Delta time.
     */
    private final DeltaTime mDeltaTime = new DeltaTime();

    /**
     * A pool of graph content storage objects.
     */
    private final GraphContentPool mGraphContentPool = new GraphContentPool();

    /**
     * Graph content collector.
     */
    private final GraphContentCollector mGraphContentCollector = new GraphContentCollector();

    /**
     * The action handler
     */
    private final FrontEndActionHandler mActionHandler = new FrontEndActionHandler();

    /**
     * The render content generator.
     */
    private final RenderContentCreator mRenderContentCreator;


    public UpdateFrame(RenderTaskPool taskPool) {
        mRenderContentCreator = new RenderContentCreator(taskPool);
    }

    /**
     * A frame update.
     *
     * @param application The user application.
     * @param root        The root node to start traverse from.
     * @param renderQueue The render queue.
     */
    public void update(UserApplication application, Node root, ArrayDeque<RenderTask> renderQueue) {
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
        mGraphContentCollector.collect(root, graphContent);

        /**
         * Handle all actions.
         */
        handleActions(graphContent);

        /**
         * Create render content.
         */
        mRenderContentCreator.create(graphContent, renderQueue);

        /**
         * Pool the graph content storage.
         */
        mGraphContentPool.put(graphContent);
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
    }
}

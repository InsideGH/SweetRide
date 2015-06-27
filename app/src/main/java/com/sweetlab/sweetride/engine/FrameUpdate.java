package com.sweetlab.sweetride.engine;

import android.os.SystemClock;

import com.sweetlab.sweetride.UserApplication;
import com.sweetlab.sweetride.camera.ViewFrustrumCulling;
import com.sweetlab.sweetride.engine.rendernode.RenderGroupCollector;
import com.sweetlab.sweetride.engine.rendernode.RenderNode;
import com.sweetlab.sweetride.engine.rendernode.RenderNodeTask;
import com.sweetlab.sweetride.geometry.Geometry;
import com.sweetlab.sweetride.camera.Camera;
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
    private final DeltaTime mDeltaTime = new DeltaTime();

    /**
     * A pool of graph content storage objects.
     */
    private final GraphContentPool mGraphContentPool = new GraphContentPool();

    /**
     * Graph content collector.
     */
    private final ContentCollector mContentCollector = new ContentCollector();

    /**
     * A render node group geometry collector.
     */
    private final RenderGroupCollector mGroupCollector = new RenderGroupCollector();

    /**
     * The action handler
     */
    private final FrontEndActionHandler mActionHandler = new FrontEndActionHandler();

    /**
     * View frustrum culling.
     */
    private final ViewFrustrumCulling mViewFrustrumCulling = new ViewFrustrumCulling();

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
     * Create a list of render tasks.
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
                List<Geometry> list = getGeometries(renderNode);

                Camera camera = renderNode.getCamera();
                if (camera != null && renderNode.isViewFrustrumCullingEnabled()) {
                    removeInvisible(list, camera);
                }

                renderList.add(new RenderNodeTask(renderer, list));
            }
        }
        return renderList;
    }

    /**
     * Get geometries in render node.
     *
     * @param renderNode Render node to fetch geometries from.
     * @return List of geometries.
     */
    private List<Geometry> getGeometries(RenderNode renderNode) {
        List<Geometry> list = new ArrayList<>();
        mGroupCollector.collect(renderNode, list);
        return list;
    }

    /**
     * Remove invisible geometries.
     *
     * @param list   List of geometries.
     * @param camera Camera.
     */
    private void removeInvisible(List<Geometry> list, Camera camera) {
        final int lastIndex = list.size() - 1;
        for (int i = lastIndex; i > -1; i--) {
            if (!mViewFrustrumCulling.isVisible(list.get(i), camera)) {
                list.remove(i);
            }
        }
    }
}

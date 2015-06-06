package com.sweetlab.sweetride.engine;

import android.test.AndroidTestCase;

import com.sweetlab.sweetride.geometry.Geometry;
import com.sweetlab.sweetride.math.Camera;
import com.sweetlab.sweetride.node.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Test updating a graph.
 */
public class GraphUpdateTest extends AndroidTestCase {
    /**
     * Number of frames to run.
     */
    private static final int NBR_FRAMES = 10;

    /**
     * Float compare.
     */
    private static final float EPS = 10e-6f;

    /**
     *
     */
    private Random mRand = new Random(666);

    /**
     * Number of application nodes.
     */
    private static final int APP_NODES = 100;

    /**
     * Number of application geometry nodes.
     */
    private static final int APP_GEOMETRIES = 100;

    /**
     * Number of application render nodes.
     */
    private static final int APP_RENDER_NODES = 20;

    /**
     * The engine root.
     */
    private Node mEngineRoot = new Node();

    /**
     * Application render nodes.
     */
    private TestRenderNode[] mAppRenderNodes = new TestRenderNode[APP_RENDER_NODES];

    /**
     * Application nodes.
     */
    private TestNode[] mAppNodes = new TestNode[APP_NODES];

    /**
     * Application nodes.
     */
    private TestGeometry[] mAppGeometries = new TestGeometry[APP_GEOMETRIES];

    /**
     * Delta used during update.
     */
    private float mDelta;

    /**
     * Front end action handler.
     */
    FrontEndActionHandler mActionHandler = new FrontEndActionHandler();

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        List<Node> nodes = new ArrayList<>();

        for (int i = 0; i < APP_RENDER_NODES; i++) {
            mAppRenderNodes[i] = new TestRenderNode();
            mAppRenderNodes[i].setCamera(new Camera());
            nodes.add(mAppRenderNodes[i]);
        }

        for (int i = 0; i < APP_NODES; i++) {
            mAppNodes[i] = new TestNode();
            nodes.add(mAppNodes[i]);
        }

        for (int i = 0; i < APP_GEOMETRIES; i++) {
            mAppGeometries[i] = new TestGeometry();
            nodes.add(mAppGeometries[i]);
        }

        Collections.shuffle(nodes, mRand);

        List<Node> connected = new ArrayList<>();
        while (!nodes.isEmpty()) {
            Node nodeToConnect = nodes.remove(0);
            if (connected.isEmpty()) {
                mEngineRoot.addChild(nodeToConnect);
            } else {
                Node nodeInGraph = connected.get(mRand.nextInt(connected.size()));
                nodeInGraph.addChild(nodeToConnect);
            }
            connected.add(nodeToConnect);
        }
    }

    public void testUpdate() {
        for (int i = 0; i < NBR_FRAMES; i++) {
            mDelta = i;
            mEngineRoot.update(mDelta);
            for (TestNode node : mAppNodes) {
                assertEquals(i + 1, node.mUpdateCount);
            }
            for (TestGeometry node : mAppGeometries) {
                assertEquals(i + 1, node.mUpdateCount);
            }
            for (TestRenderNode node : mAppRenderNodes) {
                assertEquals(i + 1, node.mUpdateCount);
            }
        }
    }

    public void testHandleActions() {
        assertFalse(mEngineRoot.hasActions());
        mActionHandler.handleActions(mEngineRoot);
        assertFalse(mEngineRoot.hasActions());

        GraphContent collect = new GraphContent();
        new ContentCollector().collect(mEngineRoot, collect);
        List<Geometry> geometries = collect.getGeometries();
        List<RenderNode> renderNodes = collect.getRenderNodes();
        List<Node> nodes = collect.getNodes();

        assertEquals(APP_GEOMETRIES, geometries.size());
        assertEquals(APP_NODES, nodes.size());
        assertEquals(APP_RENDER_NODES, renderNodes.size());

        boolean foundActions = false;
        for (Node node : renderNodes) {
            boolean handledAction = mActionHandler.handleActions(node);
            if (handledAction) {
                foundActions = true;
            }
        }
        for (Node node : nodes) {
            boolean handledAction = mActionHandler.handleActions(node);
            if (handledAction) {
                foundActions = true;
            }
        }
        for (Node node : geometries) {
            boolean handledAction = mActionHandler.handleActions(node);
            if (handledAction) {
                foundActions = true;
            }
        }
        assertTrue(foundActions);

        for (Node node : renderNodes) {
            assertFalse(node.hasActions());
        }
        for (Node node : nodes) {
            assertFalse(node.hasActions());
        }
        for (Node node : geometries) {
            assertFalse(node.hasActions());
        }
    }

    public void testRenderNodes() {
        GraphContent collect = new GraphContent();
        new ContentCollector().collect(mEngineRoot, collect);
        List<Geometry> geometries = collect.getGeometries();
        List<RenderNode> renderNodes = collect.getRenderNodes();
        List<Node> nodes = collect.getNodes();

        assertEquals(APP_GEOMETRIES, geometries.size());
        assertEquals(APP_NODES, nodes.size());
        assertEquals(APP_RENDER_NODES, renderNodes.size());

        /**
         * Might be so that geometries are added without having a render node as parent.
         * So we need to exclude these from the test.
         */
        int excludeCount = 0;
        for (Geometry geometry : geometries) {
            if (geometry.findCamera() == null) {
                excludeCount++;
            }
        }

        int geometryCount = 0;
        for (RenderNode renderNode : renderNodes) {
            List<Geometry> subBranch = new ArrayList<>();
            new RenderGroupCollector().collect(renderNode, subBranch);
            geometryCount += subBranch.size();
        }

        assertEquals(APP_GEOMETRIES - excludeCount, geometryCount);
    }

    /**
     * Render node that counts updates.
     */
    private class TestRenderNode extends DefaultRenderNode {
        public int mUpdateCount;

        @Override
        public boolean onUpdate(float dt) {
            mUpdateCount++;
            assertEquals(dt, mDelta, EPS);
            return super.onUpdate(dt);
        }
    }

    /**
     * Node that counts updates.
     */
    private class TestNode extends Node {
        public int mUpdateCount;

        @Override
        public boolean onUpdate(float dt) {
            mUpdateCount++;
            assertEquals(dt, mDelta, EPS);
            return super.onUpdate(dt);
        }
    }

    /**
     * Geometry that counts updates.
     */
    private class TestGeometry extends Geometry {
        public int mUpdateCount;

        @Override
        public boolean onUpdate(float dt) {
            mUpdateCount++;
            assertEquals(dt, mDelta, EPS);
            return super.onUpdate(dt);
        }
    }
}

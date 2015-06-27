package com.sweetlab.sweetride.engine;

import android.test.AndroidTestCase;

import com.sweetlab.sweetride.UserApplication;
import com.sweetlab.sweetride.engine.frame.RenderTask;
import com.sweetlab.sweetride.engine.frame.update.UpdateFrame;
import com.sweetlab.sweetride.geometry.Geometry;
import com.sweetlab.sweetride.node.Node;
import com.sweetlab.sweetride.pool.RenderTaskPool;
import com.sweetlab.sweetride.rendernode.DefaultRenderNode;

import java.util.ArrayDeque;

/**
 * A frame update test.
 */
public class UpdateFrameTest extends AndroidTestCase {
    /**
     * Pool used by frame update.
     */
    private RenderTaskPool mTaskPool = new RenderTaskPool();

    /**
     * A frame update.
     */
    private UpdateFrame mUpdateFrame = new UpdateFrame(mTaskPool);

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testUpdate() {
        TestApplication application = new TestApplication();
        TestEngineNode engineRoot = new TestEngineNode();

        /**
         * First render node branch, 2 geometries and 1 node.
         */
        TestRenderNode appRenderNode1 = new TestRenderNode();
        TestNode appNode1 = new TestNode();
        TestGeometry appNode2 = new TestGeometry();
        TestGeometry appNode3 = new TestGeometry();

        /**
         * Second render node branch, 1 geometry.
         */
        TestRenderNode appRenderNode2 = new TestRenderNode();
        TestGeometry appNode4 = new TestGeometry();

        /**
         * Connect all in a single line.
         */
        appRenderNode1.addChild(appNode1);
        appNode1.addChild(appNode2);
        appNode2.addChild(appNode3);

        appNode3.addChild(appRenderNode2);
        appRenderNode2.addChild(appNode4);

        /**
         * Add top to engine root.
         */
        engineRoot.addChild(appRenderNode1);

        for (int i = 0; i < 10; i++) {
            assertEquals(i, engineRoot.mUpdateCount);
            assertEquals(i, application.mUpdateCount);
            assertEquals(i, appRenderNode1.mUpdateCount);
            assertEquals(i, appNode1.mUpdateCount);
            assertEquals(i, appNode2.mUpdateCount);
            assertEquals(i, appNode3.mUpdateCount);
            assertEquals(i, appRenderNode2.mUpdateCount);
            assertEquals(i, appNode4.mUpdateCount);
            ArrayDeque<RenderTask> queue = new ArrayDeque<>();
            mUpdateFrame.update(application, engineRoot, queue);

            assertNotNull(queue);
            assertFalse(queue.isEmpty());
            assertEquals(2, queue.size());
        }
    }

    private class TestEngineNode extends Node {
        /**
         * Update count.
         */
        public int mUpdateCount;

        @Override
        public boolean onUpdate(float dt) {
            mUpdateCount++;
            return super.onUpdate(dt);
        }
    }

    private class TestRenderNode extends DefaultRenderNode {
        /**
         * Update count.
         */
        public int mUpdateCount;

        @Override
        public boolean onUpdate(float dt) {
            mUpdateCount++;
            return super.onUpdate(dt);
        }
    }

    private class TestNode extends Node {
        /**
         * Update count.
         */
        public int mUpdateCount;

        @Override
        public boolean onUpdate(float dt) {
            mUpdateCount++;
            return super.onUpdate(dt);
        }
    }

    private class TestGeometry extends Geometry {
        /**
         * Update count.
         */
        public int mUpdateCount;

        @Override
        public boolean onUpdate(float dt) {
            mUpdateCount++;
            return super.onUpdate(dt);
        }
    }

    private class TestApplication extends UserApplication {
        /**
         * Update count.
         */
        public int mUpdateCount;

        @Override
        public void onInitialized(Node engineRoot, int width, int height) {
        }

        @Override
        public void onSurfaceChanged(int width, int height) {

        }

        @Override
        public void onUpdate(float dt) {
            mUpdateCount++;
        }
    }
}

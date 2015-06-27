package com.sweetlab.sweetride.engine.frame.update;

import com.sweetlab.sweetride.engine.frame.RenderTask;
import com.sweetlab.sweetride.node.Node;
import com.sweetlab.sweetride.pool.RenderTaskPool;
import com.sweetlab.sweetride.renderer.DefaultNodeRenderer;
import com.sweetlab.sweetride.rendernode.RenderNode;

import junit.framework.TestCase;

import java.util.ArrayDeque;

/**
 * Test creating render content.
 */
public class RenderContentCreatorTest extends TestCase {

    private RenderTaskPool mRenderTaskPool;
    private RenderContentCreator mRenderContentCreator;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mRenderTaskPool = new RenderTaskPool();
        mRenderContentCreator = new RenderContentCreator(mRenderTaskPool);
    }

    public void testCreate() throws Exception {
        RenderNode rn0 = new RenderNode();
        RenderNode rn1 = new RenderNode();
        RenderNode rn2 = new RenderNode();
        RenderNode rn3 = new RenderNode();
        RenderNode rn4 = new RenderNode();

        Node n0 = new Node();
        Node n1 = new Node();
        Node n2 = new Node();
        Node n3 = new Node();
        Node n4 = new Node();
        Node n5 = new Node();
        Node n6 = new Node();
        Node n7 = new Node();
        Node n8 = new Node();
        Node n9 = new Node();

        /**
         * Rn0 + n0 + n1 + n2
         */
        rn0.addChild(n0);
        n0.addChild(n1);
        n0.addChild(n2);

        /**
         * n2 + (rn1 + n3 + n4)
         */
        n2.addChild(rn1);
        rn1.addChild(n3);
        n3.addChild(n4);

        /**
         * n4 + (rn2 + n5 + n6 + n7)
         */
        n4.addChild(rn2);
        rn2.addChild(n5);
        rn2.addChild(n6);
        rn2.addChild(n7);

        /**
         * n6 + (rn3)
         */
        n6.addChild(rn3);

        /**
         * rn3 + (rn4 + n8 + n9)
         */
        rn3.addChild(rn4);
        rn4.addChild(n8);
        n8.addChild(n9);

        GraphContentCollector graphContentCollector = new GraphContentCollector();
        GraphContent content = new GraphContent();
        graphContentCollector.collect(rn0, content);

        ArrayDeque<RenderTask> queue = new ArrayDeque<>();
        mRenderContentCreator.create(content, queue);

        // No render content created since no renderer(s) where configured.
        assertEquals(0, queue.size());

        rn0.setRenderer(new DefaultNodeRenderer());
        mRenderContentCreator.create(content, queue);
        assertEquals(1, queue.size());
        for (RenderTask task : queue) {
            mRenderTaskPool.put(task);
        }
        queue.clear();

        rn1.setRenderer(new DefaultNodeRenderer());
        mRenderContentCreator.create(content, queue);
        assertEquals(2, queue.size());
        for (RenderTask task : queue) {
            mRenderTaskPool.put(task);
        }
        queue.clear();

        rn2.setRenderer(new DefaultNodeRenderer());
        mRenderContentCreator.create(content, queue);
        assertEquals(3, queue.size());
        for (RenderTask task : queue) {
            mRenderTaskPool.put(task);
        }
        queue.clear();

        rn3.setRenderer(new DefaultNodeRenderer());
        mRenderContentCreator.create(content, queue);
        assertEquals(4, queue.size());
        for (RenderTask task : queue) {
            mRenderTaskPool.put(task);
        }
        queue.clear();

        rn4.setRenderer(new DefaultNodeRenderer());
        mRenderContentCreator.create(content, queue);
        assertEquals(5, queue.size());
        for (RenderTask task : queue) {
            mRenderTaskPool.put(task);
        }
        queue.clear();
    }
}
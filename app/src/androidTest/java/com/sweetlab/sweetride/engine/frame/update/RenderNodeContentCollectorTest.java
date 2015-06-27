package com.sweetlab.sweetride.engine.frame.update;

import com.sweetlab.sweetride.node.Node;
import com.sweetlab.sweetride.rendernode.RenderNode;

import junit.framework.TestCase;

/**
 * Test getting content from a render node branch.
 */
public class RenderNodeContentCollectorTest extends TestCase {

    private RenderNodeContentCollector mCollector;
    private RenderNode mRn0;
    private RenderNode mRn1;
    private RenderNode mRn2;
    private RenderNode mRn3;
    private RenderNode mRn4;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mCollector = new RenderNodeContentCollector();
        
        mRn0 = new RenderNode();
        mRn1 = new RenderNode();
        mRn2 = new RenderNode();
        mRn3 = new RenderNode();
        mRn4 = new RenderNode();

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
        mRn0.addChild(n0);
        n0.addChild(n1);
        n0.addChild(n2);

        /**
         * n2 + (rn1 + n3 + n4)
         */
        n2.addChild(mRn1);
        mRn1.addChild(n3);
        n3.addChild(n4);

        /**
         * n4 + (rn2 + n5 + n6 + n7)
         */
        n4.addChild(mRn2);
        mRn2.addChild(n5);
        mRn2.addChild(n6);
        mRn2.addChild(n7);

        /**
         * n6 + (rn3)
         */
        n6.addChild(mRn3);

        /**
         * rn3 + (rn4 + n8 + n9)
         */
        mRn3.addChild(mRn4);
        mRn4.addChild(n8);
        n8.addChild(n9);
    }

    public void testCollect() throws Exception {
        mCollector.collect(mRn0);

        /**
         * Rn0 + n0 + n1 + n2
         */
        assertEquals(4, mCollector.getResult().size());
        mCollector.reset();
        assertEquals(0, mCollector.getResult().size());

        /**
         * n2 + (rn1 + n3 + n4)
         */
        mCollector.collect(mRn1);
        assertEquals(3, mCollector.getResult().size());
        mCollector.reset();

        /**
         * n4 + (rn2 + n5 + n6 + n7)
         */
        mCollector.collect(mRn2);
        assertEquals(4, mCollector.getResult().size());
        mCollector.reset();

        /**
         * n6 + (rn3)
         */
        mCollector.collect(mRn3);
        assertEquals(1, mCollector.getResult().size());
        mCollector.reset();

        /**
         * rn3 + (rn4 + n8 + n9)
         */
        mCollector.collect(mRn4);
        assertEquals(3, mCollector.getResult().size());
        mCollector.reset();
    }
}
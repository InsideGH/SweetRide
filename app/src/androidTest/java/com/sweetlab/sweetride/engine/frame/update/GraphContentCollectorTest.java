package com.sweetlab.sweetride.engine.frame.update;

import com.sweetlab.sweetride.node.Node;
import com.sweetlab.sweetride.rendernode.RenderNode;

import junit.framework.TestCase;

import java.util.List;

/**
 * Test collecting graph content.
 */
public class GraphContentCollectorTest extends TestCase {

    private GraphContentCollector mGraphContentCollector;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mGraphContentCollector = new GraphContentCollector();
    }

    public void testCollect() throws Exception {
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

        GraphContent graphContent = new GraphContent();

        mGraphContentCollector.collect(rn0, graphContent);

        List<RenderNode> renderNodes = graphContent.getRenderNodes();
        assertEquals(5, renderNodes.size());

        List<Node> nodes = graphContent.getNodes();
        assertEquals(10, nodes.size());
    }
}
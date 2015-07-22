package com.sweetlab.sweetride.engine.frame.update;

import com.sweetlab.sweetride.node.Node;
import com.sweetlab.sweetride.rendernode.RenderNode;

import junit.framework.TestCase;

import java.util.List;

/**
 * Test graph content container.
 */
public class GraphContentTest extends TestCase {
    /**
     * The object under test.
     */
    private GraphContent mGraphContent;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mGraphContent = new GraphContent();
    }

    public void testReset() throws Exception {
        List<Node> nodes = mGraphContent.getNodes();
        assertEquals(0, nodes.size());
        List<RenderNode> renderNodes = mGraphContent.getRenderNodes();
        assertEquals(0, renderNodes.size());

        fill(mGraphContent);
        nodes = mGraphContent.getNodes();
        assertEquals(50 * 50, nodes.size());
        renderNodes = mGraphContent.getRenderNodes();
        assertEquals(50, renderNodes.size());

        // Get is by reference.
        mGraphContent.reset();
        assertEquals(0, nodes.size());

        renderNodes = mGraphContent.getRenderNodes();
        assertEquals(0, renderNodes.size());
    }

    public void testAdd() throws Exception {
        fill(mGraphContent);
        List<Node> nodes = mGraphContent.getNodes();
        assertEquals(50 * 50, nodes.size());
        List<RenderNode> renderNodes = mGraphContent.getRenderNodes();
        assertEquals(50, renderNodes.size());
    }

    private void fill(GraphContent graphContent) {
        for (int i = 0; i < 50; i++) {
            graphContent.add(new RenderNode());
            for (int j = 0; j < 50; j++) {
                graphContent.add(new Node());
            }
        }
    }
}
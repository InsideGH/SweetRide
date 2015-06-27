package com.sweetlab.sweetride.node;

import android.test.AndroidTestCase;

import com.sweetlab.sweetride.camera.Camera;
import com.sweetlab.sweetride.camera.Frustrum;
import com.sweetlab.sweetride.rendernode.RenderNode;
import com.sweetlab.sweetride.node.rendersettings.ClearBit;

/**
 * Test render settings using nodes.
 */
public class RenderSettingsNodeTest extends AndroidTestCase {

    public void testOneBranch() {
        Node nodeA = new Node();
        Node nodeB = new Node();
        Node nodeC = new Node();
        Node nodeD = new Node();
        Node nodeE = new Node();
        Node nodeF = new Node();

        nodeA.addChild(nodeB);
        nodeB.addChild(nodeC);
        nodeC.addChild(nodeD);
        nodeD.addChild(nodeE);
        nodeE.addChild(nodeF);

        /**
         * Check that all is false
         */
        assertFalse(nodeA.getRenderSettings().getBlend());
        assertFalse(nodeB.getRenderSettings().getBlend());
        assertFalse(nodeC.getRenderSettings().getBlend());
        assertFalse(nodeD.getRenderSettings().getBlend());
        assertFalse(nodeE.getRenderSettings().getBlend());
        assertFalse(nodeF.getRenderSettings().getBlend());

        /**
         * Set true at top, will rippled down to all.
         */
        nodeA.getRenderSettings().setBlend(true);

        assertTrue(nodeA.getRenderSettings().getBlend());
        assertTrue(nodeB.getRenderSettings().getBlend());
        assertTrue(nodeC.getRenderSettings().getBlend());
        assertTrue(nodeD.getRenderSettings().getBlend());
        assertTrue(nodeE.getRenderSettings().getBlend());
        assertTrue(nodeF.getRenderSettings().getBlend());

        /**
         * Set C and below to false.
         */
        nodeC.getRenderSettings().setBlend(false);

        assertTrue(nodeA.getRenderSettings().getBlend());
        assertTrue(nodeB.getRenderSettings().getBlend());
        assertFalse(nodeC.getRenderSettings().getBlend());
        assertFalse(nodeD.getRenderSettings().getBlend());
        assertFalse(nodeE.getRenderSettings().getBlend());
        assertFalse(nodeF.getRenderSettings().getBlend());

        /**
         * Set the last to true, this should be kept
         */
        nodeF.getRenderSettings().setBlend(true);
        /**
         * Set C to false which will ripple down to
         * all that haven't explicitly set a value.
         */
        nodeC.getRenderSettings().setBlend(false);

        assertTrue(nodeA.getRenderSettings().getBlend());
        assertTrue(nodeB.getRenderSettings().getBlend());
        assertFalse(nodeC.getRenderSettings().getBlend());
        assertFalse(nodeD.getRenderSettings().getBlend());
        assertFalse(nodeE.getRenderSettings().getBlend());
        assertTrue(nodeF.getRenderSettings().getBlend());
    }

    public void testTree() throws Exception {
        Node root = new Node();
        Node a = new Node();
        Node b = new Node();
        Node c = new Node();
        Node d = new Node();
        Node e = new Node();
        Node f = new Node();

        root.addChild(a);
        root.addChild(b);
        root.addChild(c);
        c.addChild(d);
        c.addChild(f);
        d.addChild(e);


        /**
         * Check that all is false
         */
        assertFalse(a.getRenderSettings().getBlend());
        assertFalse(b.getRenderSettings().getBlend());
        assertFalse(c.getRenderSettings().getBlend());
        assertFalse(d.getRenderSettings().getBlend());
        assertFalse(e.getRenderSettings().getBlend());
        assertFalse(f.getRenderSettings().getBlend());

        /**
         * Set true at top, will rippled down to all.
         */
        a.getRenderSettings().setBlend(true);

        assertTrue(a.getRenderSettings().getBlend());
        assertFalse(b.getRenderSettings().getBlend());
        assertFalse(c.getRenderSettings().getBlend());
        assertFalse(d.getRenderSettings().getBlend());
        assertFalse(e.getRenderSettings().getBlend());
        assertFalse(f.getRenderSettings().getBlend());

        c.getRenderSettings().setBlend(true);

        assertTrue(a.getRenderSettings().getBlend());
        assertFalse(b.getRenderSettings().getBlend());
        assertTrue(c.getRenderSettings().getBlend());
        assertTrue(d.getRenderSettings().getBlend());
        assertTrue(e.getRenderSettings().getBlend());
        assertTrue(f.getRenderSettings().getBlend());

        d.getRenderSettings().setBlend(false);

        assertTrue(a.getRenderSettings().getBlend());
        assertFalse(b.getRenderSettings().getBlend());
        assertTrue(c.getRenderSettings().getBlend());
        assertFalse(d.getRenderSettings().getBlend());
        assertFalse(e.getRenderSettings().getBlend());
        assertTrue(f.getRenderSettings().getBlend());
    }

    public void testViewPort() {
        RenderNode root = new RenderNode();
        root.setCamera(new Camera());

        Node a = new Node();
        Node b = new Node();
        Node c = new Node();
        Node d = new Node();
        Node e = new Node();
        Node f = new Node();

        root.addChild(a);
        root.addChild(b);
        root.addChild(c);
        c.addChild(d);
        c.addChild(f);
        d.addChild(e);

        assertEquals(0, e.getRenderSettings().getViewPortWidth());
        assertEquals(0, e.getRenderSettings().getViewPortHeight());

        root.findCamera().getFrustrum().setPerspectiveProjection(90, Frustrum.FovType.AUTO_FIT, 0.1f, 10, 1080, 1920);
        assertEquals(1080, e.getRenderSettings().getViewPortWidth());
        assertEquals(1920, e.getRenderSettings().getViewPortHeight());

        root.setCamera(new Camera());
        assertEquals(1080, e.getRenderSettings().getViewPortWidth());
        assertEquals(1920, e.getRenderSettings().getViewPortHeight());

        root.findCamera().getFrustrum().setPerspectiveProjection(90, Frustrum.FovType.AUTO_FIT, 0.1f, 10, 10, 19);
        assertEquals(10, e.getRenderSettings().getViewPortWidth());
        assertEquals(19, e.getRenderSettings().getViewPortHeight());

        Camera camera = new Camera();
        camera.getFrustrum().setPerspectiveProjection(90, Frustrum.FovType.AUTO_FIT, 0.1f, 10, 100, 190);
        root.setCamera(camera);
        assertEquals(100, e.getRenderSettings().getViewPortWidth());
        assertEquals(190, e.getRenderSettings().getViewPortHeight());
    }

    public void testAddChild() {
        Node root = new Node();
        Node a = new Node();
        Node b = new Node();
        Node c = new Node();
        Node d = new Node();
        Node e = new Node();
        Node f = new Node();

        root.getRenderSettings().setStencilTest(true);
        c.getRenderSettings().setScissorTest(true);

        root.addChild(a);
        root.addChild(b);
        root.addChild(c);
        c.addChild(d);
        c.addChild(f);
        d.addChild(e);

        assertTrue(root.getRenderSettings().getStencilTest());
        assertFalse(root.getRenderSettings().getScissorTest());

        assertTrue(a.getRenderSettings().getStencilTest());
        assertFalse(a.getRenderSettings().getScissorTest());

        assertTrue(b.getRenderSettings().getStencilTest());
        assertFalse(b.getRenderSettings().getScissorTest());

        assertTrue(c.getRenderSettings().getStencilTest());
        assertTrue(c.getRenderSettings().getScissorTest());

        assertTrue(d.getRenderSettings().getStencilTest());
        assertTrue(d.getRenderSettings().getScissorTest());

        assertTrue(e.getRenderSettings().getStencilTest());
        assertTrue(e.getRenderSettings().getScissorTest());

        assertTrue(f.getRenderSettings().getStencilTest());
        assertTrue(f.getRenderSettings().getScissorTest());

        d.removeChild(e);
        assertTrue(e.getRenderSettings().getStencilTest());
        assertTrue(e.getRenderSettings().getScissorTest());

        e.getRenderSettings().setStencilTest(false);
        e.getRenderSettings().setScissorTest(false);

        d.addChild(e);
        assertFalse(e.getRenderSettings().getStencilTest());
        assertFalse(e.getRenderSettings().getScissorTest());
    }

    public void testClear() {
        RenderNode root = new RenderNode();
        root.setCamera(new Camera());
        root.findCamera().getFrustrum().setPerspectiveProjection(90, Frustrum.FovType.AUTO_FIT, 0.1f, 10, 1080, 1920);

        root.getRenderSettings().setClear(1, ClearBit.COLOR_BUFFER_BIT);

        Node a = new Node();
        Node b = new Node();
        Node c = new Node();
        Node d = new Node();
        Node e = new Node();
        Node f = new Node();

        root.addChild(a);
        root.addChild(b);
        root.addChild(c);
        c.addChild(d);
        c.addChild(f);
        d.addChild(e);

        assertEquals(1, root.getRenderSettings().getClearOrder());
        assertEquals(1, a.getRenderSettings().getClearOrder());
        assertEquals(1, b.getRenderSettings().getClearOrder());
        assertEquals(1, c.getRenderSettings().getClearOrder());
        assertEquals(1, d.getRenderSettings().getClearOrder());
        assertEquals(1, e.getRenderSettings().getClearOrder());
        assertEquals(1, f.getRenderSettings().getClearOrder());

        RenderNode root2 = new RenderNode();
        Node a2 = new Node();
        Node b2 = new Node();
        Node c2 = new Node();
        Node d2 = new Node();
        Node e2 = new Node();
        Node f2 = new Node();

        root2.addChild(a2);
        root2.addChild(b2);
        root2.addChild(c2);
        c2.addChild(d2);
        c2.addChild(f2);
        d2.addChild(e2);

        root2.getRenderSettings().setClear(2, ClearBit.DEPTH_BUFFER_BIT);
        e.addChild(root2);

        assertEquals(2, root2.getRenderSettings().getClearOrder());
        assertEquals(2, a2.getRenderSettings().getClearOrder());
        assertEquals(2, b2.getRenderSettings().getClearOrder());
        assertEquals(2, c2.getRenderSettings().getClearOrder());
        assertEquals(2, d2.getRenderSettings().getClearOrder());
        assertEquals(2, e2.getRenderSettings().getClearOrder());
        assertEquals(2, f2.getRenderSettings().getClearOrder());
    }
}
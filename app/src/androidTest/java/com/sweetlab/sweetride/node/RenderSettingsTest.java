package com.sweetlab.sweetride.node;

import android.test.AndroidTestCase;

import com.sweetlab.sweetride.engine.FrontEndActionHandler;
import com.sweetlab.sweetride.node.rendersettings.BlendDstFact;
import com.sweetlab.sweetride.node.rendersettings.BlendEquationFunc;
import com.sweetlab.sweetride.node.rendersettings.BlendSrcFact;
import com.sweetlab.sweetride.node.rendersettings.ClearBit;
import com.sweetlab.sweetride.math.FloatUtil;

/**
 * Test render settings.
 */
public class RenderSettingsTest extends AndroidTestCase {
    private FrontEndActionHandler mActionHandler = new FrontEndActionHandler();
    private RenderSettings mRS1;
    private RenderSettings mRS2;

    public void setUp() throws Exception {
        super.setUp();
        mRS1 = new RenderSettings();
        mRS2 = new RenderSettings();
        assertFalse(mRS1.hasActions());
    }

    public void testHandleAction() throws Exception {
        mActionHandler.handleActions(mRS1);
    }

    public void testSetBlend() throws Exception {
        mRS1.setBlend(true);
        assertTrue(mRS1.getBlend());
        mActionHandler.handleActions(mRS1);
    }

    public void testGetBlend() throws Exception {
        assertFalse(mRS1.getBlend());
    }

    public void testSetCullFace() throws Exception {
        mRS1.setCullFace(true);
        assertTrue(mRS1.getCullFace());
    }

    public void testGetCullFace() throws Exception {
        assertFalse(mRS1.getCullFace());
    }

    public void testSetDepthTest() throws Exception {
        mRS1.setDepthTest(true);
        assertTrue(mRS1.getDepthTest());
    }

    public void testGetDepthTest() throws Exception {
        assertFalse(mRS1.getDepthTest());
    }

    public void testSetDither() throws Exception {
        mRS1.setDither(false);
        assertFalse(mRS1.getDither());
    }

    public void testGetDither() throws Exception {
        assertTrue(mRS1.getDither());
    }

    public void testSetPolygonOffsetFill() throws Exception {
        mRS1.setPolygonOffsetFill(true);
        assertTrue(mRS1.getPolygonOffsetFill());
    }

    public void testGetPolygonOffsetFill() throws Exception {
        assertFalse(mRS1.getPolygonOffsetFill());
    }

    public void testSetSampleAlphaToCoverage() throws Exception {
        mRS1.setSampleAlphaToCoverage(true);
        assertTrue(mRS1.getSampleAlphaToCoverage());
    }

    public void testGetSampleAlphaToCoverage() throws Exception {
        assertFalse(mRS1.getSampleAlphaToCoverage());
    }

    public void testSetSampleCoverage() throws Exception {
        mRS1.setSampleCoverage(true);
        assertTrue(mRS1.getSampleCoverage());
    }

    public void testGetSampleCoverage() throws Exception {
        assertFalse(mRS1.getSampleCoverage());
    }

    public void testSetScissorTest() throws Exception {
        mRS1.setScissorTest(true);
        assertTrue(mRS1.getScissorTest());
    }

    public void testGetScissorTest() throws Exception {
        assertFalse(mRS1.getScissorTest());
    }

    public void testSetStencilTest() throws Exception {
        mRS1.setStencilTest(true);
        assertTrue(mRS1.getStencilTest());
    }

    public void testGetStencilTest() throws Exception {
        assertFalse(mRS1.getStencilTest());
    }

    public void testSetBlendEqFunc() throws Exception {
        mRS1.setBlendEqFunc(BlendEquationFunc.REVERSE_SUBTRACT);
        assertEquals(BlendEquationFunc.REVERSE_SUBTRACT, mRS1.getBlendEqFunc());
    }

    public void testGetBlendEqFunc() throws Exception {
        assertEquals(BlendEquationFunc.ADD, mRS1.getBlendEqFunc());
    }

    public void testSetBlendFact() throws Exception {
        mRS1.setBlendFact(BlendSrcFact.CONSTANT_ALPHA, BlendDstFact.CONSTANT_COLOR);
        assertEquals(BlendSrcFact.CONSTANT_ALPHA, mRS1.getBlendSrcFact());
        assertEquals(BlendDstFact.CONSTANT_COLOR, mRS1.getBlendDstFact());
    }

    public void testGetBlendSrcFact() throws Exception {
        assertEquals(BlendSrcFact.ONE, mRS1.getBlendSrcFact());
    }

    public void testGetBlendDstFact() throws Exception {
        assertEquals(BlendSrcFact.ONE, mRS1.getBlendSrcFact());
    }

    public void testSetClear() throws Exception {
        mRS1.setClear(22, ClearBit.COLOR_BUFFER_BIT, ClearBit.DEPTH_BUFFER_BIT);
        assertEquals(22, mRS1.getClearOrder());
        assertEquals(2, mRS1.getClearBits().length);
        assertEquals(ClearBit.COLOR_BUFFER_BIT, mRS1.getClearBits()[0]);
        assertEquals(ClearBit.DEPTH_BUFFER_BIT, mRS1.getClearBits()[1]);
    }

    public void testGetClearOrder() throws Exception {
        assertEquals(0, mRS1.getClearOrder());
    }

    public void testGetClearBits() throws Exception {
        assertNotNull(mRS1.getClearBits());
        assertEquals(1, mRS1.getClearBits().length);
        assertEquals(ClearBit.ZERO_BIT, mRS1.getClearBits()[0]);
    }

    public void testGetClearMask() throws Exception {
        assertEquals(0, mRS1.getClearMask().getGL());
    }

    public void testSetClearColor() throws Exception {
        mRS1.setClearColor(new float[]{0.2f, 0.5f, 0.3f, 1});
        assertEquals(0.2, mRS1.getClearColor()[0], FloatUtil.EPS);
        assertEquals(0.5f, mRS1.getClearColor()[1], FloatUtil.EPS);
        assertEquals(0.3f, mRS1.getClearColor()[2], FloatUtil.EPS);
        assertEquals(1, mRS1.getClearColor()[3], FloatUtil.EPS);
    }

    public void testGetClearColor() throws Exception {
        assertEquals(0, mRS1.getClearColor()[0], FloatUtil.EPS);
        assertEquals(0, mRS1.getClearColor()[1], FloatUtil.EPS);
        assertEquals(0, mRS1.getClearColor()[2], FloatUtil.EPS);
        assertEquals(0, mRS1.getClearColor()[3], FloatUtil.EPS);
    }

    public void testSetClearDepth() throws Exception {
        mRS1.setClearDepth(0.5f);
        assertEquals(0.5f, mRS1.getClearDepth(), FloatUtil.EPS);
    }

    public void testGetClearDepth() throws Exception {
        assertEquals(1, mRS1.getClearDepth(), FloatUtil.EPS);
    }

    public void testSetClearStencil() throws Exception {
        mRS1.setClearStencil(3);
        assertEquals(3, mRS1.getClearStencil());
    }

    public void testGetClearStencil() throws Exception {
        assertEquals(0, mRS1.getClearStencil());
    }

    public void testGetViewPort() throws Exception {
        assertEquals(0, mRS1.getViewPortX());
        assertEquals(0, mRS1.getViewPortY());
        assertEquals(0, mRS1.getViewPortWidth());
        assertEquals(0, mRS1.getViewPortHeight());
    }

    public void testSetViewPort() throws Exception {
        mRS1.setViewPort(30,40,1000,2000);
        assertEquals(30, mRS1.getViewPortX());
        assertEquals(40, mRS1.getViewPortY());
        assertEquals(1000, mRS1.getViewPortWidth());
        assertEquals(2000, mRS1.getViewPortHeight());
    }

    public void testInheritIntoDefault() throws Exception {
        setA(mRS1);
        mRS2.inherit(mRS1);
        verifyA(mRS2);
    }

    public void testInheritIntoInherit() throws Exception {
        setA(mRS1);
        mRS2.inherit(mRS1);
        verifyA(mRS2);
        setB(mRS1);
        verifyB(mRS1);
        mRS2.inherit(mRS1);
        verifyB(mRS2);
    }

    public void testInheritIntoDefaultAndSet() throws Exception {
        setHalfA(mRS1);
        setOtherHalfA(mRS2);
        mRS2.inherit(mRS1);
        verifyHalfA(mRS2);
        verifyOtherHalfA(mRS2);
    }

    public void testInheritMixed() throws Exception {
        mRS1.setClear(1, ClearBit.COLOR_BUFFER_BIT, ClearBit.DEPTH_BUFFER_BIT);
        mRS1.setClearColor(new float[]{1, 0, 0, 1});
        mRS1.setBlend(true);
        mRS1.setBlendFact(BlendSrcFact.SRC_ALPHA, BlendDstFact.ONE_MINUS_SRC_ALPHA);
        mRS1.setViewPort(10, 20, 1920, 1080);

        mRS2.setBlend(false);
        mRS2.setBlendFact(BlendSrcFact.DST_COLOR, BlendDstFact.CONSTANT_COLOR);
        mRS2.setViewPort(100, 200, 19200, 10800);

        mRS2.inherit(mRS1);
        assertEquals(1, mRS2.getClearOrder());
        assertEquals(2, mRS2.getClearBits().length);
        assertEquals(ClearBit.COLOR_BUFFER_BIT, mRS2.getClearBits()[0]);
        assertEquals(ClearBit.DEPTH_BUFFER_BIT, mRS2.getClearBits()[1]);

        assertEquals(1, mRS2.getClearColor()[0], FloatUtil.EPS);
        assertEquals(0, mRS2.getClearColor()[1], FloatUtil.EPS);
        assertEquals(0, mRS2.getClearColor()[2], FloatUtil.EPS);
        assertEquals(1, mRS2.getClearColor()[3], FloatUtil.EPS);

        assertFalse(mRS2.getBlend());
        assertEquals(BlendSrcFact.DST_COLOR, mRS2.getBlendSrcFact());
        assertEquals(BlendDstFact.CONSTANT_COLOR, mRS2.getBlendDstFact());

        assertEquals(100, mRS2.getViewPortX());
        assertEquals(200, mRS2.getViewPortY());
        assertEquals(19200, mRS2.getViewPortWidth());
        assertEquals(10800, mRS2.getViewPortHeight());
    }

    public void testInheritIntoSet() throws Exception {
        setA(mRS1);
        setB(mRS2);
        mRS2.inherit(mRS1);
        verifyB(mRS2);
    }

    public void testUseSettings() throws Exception {

    }

    private static void setA(RenderSettings rs) {
        rs.setBlend(true);
        rs.setCullFace(true);
        rs.setDepthTest(true);
        rs.setDither(false);
        rs.setPolygonOffsetFill(true);
        rs.setSampleAlphaToCoverage(true);
        rs.setSampleCoverage(true);
        rs.setScissorTest(true);
        rs.setStencilTest(true);
        rs.setBlendEqFunc(BlendEquationFunc.REVERSE_SUBTRACT);
        rs.setBlendFact(BlendSrcFact.SRC_ALPHA, BlendDstFact.ONE_MINUS_SRC_ALPHA);
        rs.setClear(1, ClearBit.COLOR_BUFFER_BIT, ClearBit.DEPTH_BUFFER_BIT);
        rs.setClearColor(new float[]{1, 0, 0, 1});
        rs.setClearDepth(0.5f);
        rs.setClearStencil(3);
        rs.setViewPort(10,20, 1920, 1080);
    }

    private static void verifyA(RenderSettings rs) {
        assertTrue(rs.getBlend());
        assertTrue(rs.getCullFace());
        assertTrue(rs.getDepthTest());
        assertFalse(rs.getDither());
        assertTrue(rs.getPolygonOffsetFill());
        assertTrue(rs.getSampleAlphaToCoverage());
        assertTrue(rs.getSampleCoverage());
        assertTrue(rs.getScissorTest());
        assertTrue(rs.getStencilTest());
        assertEquals(BlendEquationFunc.REVERSE_SUBTRACT, rs.getBlendEqFunc());
        assertEquals(BlendSrcFact.SRC_ALPHA, rs.getBlendSrcFact());
        assertEquals(BlendDstFact.ONE_MINUS_SRC_ALPHA, rs.getBlendDstFact());
        assertEquals(1, rs.getClearOrder());
        assertEquals(2, rs.getClearBits().length);
        assertEquals(ClearBit.COLOR_BUFFER_BIT, rs.getClearBits()[0]);
        assertEquals(ClearBit.DEPTH_BUFFER_BIT, rs.getClearBits()[1]);
        assertEquals(1, rs.getClearColor()[0], FloatUtil.EPS);
        assertEquals(0, rs.getClearColor()[1], FloatUtil.EPS);
        assertEquals(0, rs.getClearColor()[2], FloatUtil.EPS);
        assertEquals(1, rs.getClearColor()[3], FloatUtil.EPS);
        assertEquals(0.5f, rs.getClearDepth(), FloatUtil.EPS);
        assertEquals(3, rs.getClearStencil());
        assertEquals(10, rs.getViewPortX());
        assertEquals(20, rs.getViewPortY());
        assertEquals(1920, rs.getViewPortWidth());
        assertEquals(1080, rs.getViewPortHeight());
    }

    private static void setB(RenderSettings rs) {
        rs.setBlend(true);
        rs.setCullFace(false);
        rs.setDepthTest(true);
        rs.setDither(true);
        rs.setPolygonOffsetFill(true);
        rs.setSampleAlphaToCoverage(false);
        rs.setSampleCoverage(true);
        rs.setScissorTest(false);
        rs.setStencilTest(true);
        rs.setBlendEqFunc(BlendEquationFunc.SUBTRACT);
        rs.setBlendFact(BlendSrcFact.SRC_ALPHA_SATURATE, BlendDstFact.ONE_MINUS_SRC_COLOR);
        rs.setClear(10, ClearBit.COLOR_BUFFER_BIT, ClearBit.DEPTH_BUFFER_BIT, ClearBit.STENCIL_BUFFER_BIT);
        rs.setClearColor(new float[]{1f, 0.5f, 0.3f, 1f});
        rs.setClearDepth(0.25f);
        rs.setClearStencil(6);
        rs.setViewPort(33, 44, 1280, 720);
    }

    private static void verifyB(RenderSettings rs) {
        assertTrue(rs.getBlend());
        assertFalse(rs.getCullFace());
        assertTrue(rs.getDepthTest());
        assertTrue(rs.getDither());
        assertTrue(rs.getPolygonOffsetFill());
        assertFalse(rs.getSampleAlphaToCoverage());
        assertTrue(rs.getSampleCoverage());
        assertFalse(rs.getScissorTest());
        assertTrue(rs.getStencilTest());
        assertEquals(BlendEquationFunc.SUBTRACT, rs.getBlendEqFunc());
        assertEquals(BlendSrcFact.SRC_ALPHA_SATURATE, rs.getBlendSrcFact());
        assertEquals(BlendDstFact.ONE_MINUS_SRC_COLOR, rs.getBlendDstFact());
        assertEquals(10, rs.getClearOrder());
        assertEquals(3, rs.getClearBits().length);
        assertEquals(ClearBit.COLOR_BUFFER_BIT, rs.getClearBits()[0]);
        assertEquals(ClearBit.DEPTH_BUFFER_BIT, rs.getClearBits()[1]);
        assertEquals(ClearBit.STENCIL_BUFFER_BIT, rs.getClearBits()[2]);
        assertEquals(1f, rs.getClearColor()[0], FloatUtil.EPS);
        assertEquals(0.5f, rs.getClearColor()[1], FloatUtil.EPS);
        assertEquals(0.3f, rs.getClearColor()[2], FloatUtil.EPS);
        assertEquals(1f, rs.getClearColor()[3], FloatUtil.EPS);
        assertEquals(0.25f, rs.getClearDepth(), FloatUtil.EPS);
        assertEquals(6, rs.getClearStencil());
        assertEquals(33, rs.getViewPortX());
        assertEquals(44, rs.getViewPortY());
        assertEquals(1280, rs.getViewPortWidth());
        assertEquals(720, rs.getViewPortHeight());
    }

    private static void setHalfA(RenderSettings rs) {
        rs.setBlend(true);
        rs.setDepthTest(true);
        rs.setPolygonOffsetFill(true);
        rs.setSampleCoverage(true);
        rs.setStencilTest(true);
        rs.setBlendFact(BlendSrcFact.SRC_ALPHA, BlendDstFact.ONE_MINUS_SRC_ALPHA);
        rs.setClearColor(new float[]{1, 0, 0, 1});
        rs.setClearStencil(3);
    }

    private static void verifyHalfA(RenderSettings rs) {
        assertTrue(rs.getBlend());
        assertTrue(rs.getDepthTest());
        assertTrue(rs.getPolygonOffsetFill());
        assertTrue(rs.getSampleCoverage());
        assertTrue(rs.getStencilTest());
        assertEquals(BlendSrcFact.SRC_ALPHA, rs.getBlendSrcFact());
        assertEquals(BlendDstFact.ONE_MINUS_SRC_ALPHA, rs.getBlendDstFact());
        assertEquals(1, rs.getClearColor()[0], FloatUtil.EPS);
        assertEquals(0, rs.getClearColor()[1], FloatUtil.EPS);
        assertEquals(0, rs.getClearColor()[2], FloatUtil.EPS);
        assertEquals(1, rs.getClearColor()[3], FloatUtil.EPS);
        assertEquals(3, rs.getClearStencil());
    }

    private static void setOtherHalfA(RenderSettings rs) {
        rs.setCullFace(true);
        rs.setDither(false);
        rs.setSampleAlphaToCoverage(true);
        rs.setScissorTest(true);
        rs.setBlendEqFunc(BlendEquationFunc.REVERSE_SUBTRACT);
        rs.setClear(1, ClearBit.COLOR_BUFFER_BIT, ClearBit.DEPTH_BUFFER_BIT);
        rs.setClearDepth(0.5f);
        rs.setViewPort(10, 20, 1920, 1080);
    }

    private static void verifyOtherHalfA(RenderSettings rs) {
        assertTrue(rs.getCullFace());
        assertFalse(rs.getDither());
        assertTrue(rs.getSampleAlphaToCoverage());
        assertTrue(rs.getScissorTest());
        assertEquals(BlendEquationFunc.REVERSE_SUBTRACT, rs.getBlendEqFunc());
        assertEquals(1, rs.getClearOrder());
        assertEquals(2, rs.getClearBits().length);
        assertEquals(ClearBit.COLOR_BUFFER_BIT, rs.getClearBits()[0]);
        assertEquals(ClearBit.DEPTH_BUFFER_BIT, rs.getClearBits()[1]);
        assertEquals(0.5f, rs.getClearDepth(), FloatUtil.EPS);
        assertEquals(3, rs.getClearStencil());
        assertEquals(10, rs.getViewPortX());
        assertEquals(20, rs.getViewPortY());
        assertEquals(1920, rs.getViewPortWidth());
        assertEquals(1080, rs.getViewPortHeight());
    }
}
package com.sweetlab.sweetride.node.rendersettings;

import android.test.AndroidTestCase;

import com.sweetlab.sweetride.context.BackendRenderSettings;
import com.sweetlab.sweetride.math.FloatUtil;

/**
 * Test render setting.
 */
public class SettingTest extends AndroidTestCase {

    private BackendRenderSettings mSetting = new BackendRenderSettings();

    public void testBlend() {
        /**
         * Default.
         */
        BlendSetting setting = new BlendSetting(mSetting);
        assertEquals(History.DEFAULT, setting.getHistory());
        assertFalse(setting.get());
        assertFalse(setting.hasActions());

        /**
         * Set true
         */
        setting.set(true);
        assertEquals(History.SET, setting.getHistory());
        assertTrue(setting.get());

        /**
         * Inherit false
         */
        setting.inherit(false);
        assertEquals(History.INHERITED, setting.getHistory());
        assertFalse(setting.get());

        /**
         * Set true and test equals with default false.
         */
        setting.set(true);
        BlendSetting newSetting = new BlendSetting(mSetting);
        assertFalse(setting.equals(newSetting));
        assertEquals(History.SET, setting.getHistory());

        /**
         * Test setting with new setting that is default false.
         */
        setting.set(newSetting);
        assertEquals(History.SET, setting.getHistory());
        assertFalse(setting.get());
        assertTrue(setting.equals(newSetting));

        /**
         * Set something different from default and test.
         */
        setting.set(true);
        assertTrue(setting.getActionCount() == 1);
        assertEquals(RsActionId.BLEND, setting.getAction(0).getType());
    }

    public void testCullFace() {
        /**
         * Default.
         */
        CullFaceSetting setting = new CullFaceSetting(mSetting);
        assertEquals(History.DEFAULT, setting.getHistory());
        assertFalse(setting.get());
        assertFalse(setting.hasActions());

        /**
         * Set true
         */
        setting.set(true);
        assertEquals(History.SET, setting.getHistory());
        assertTrue(setting.get());

        /**
         * Inherit false
         */
        setting.inherit(false);
        assertEquals(History.INHERITED, setting.getHistory());
        assertFalse(setting.get());

        /**
         * Set true and test equals with default false.
         */
        setting.set(true);
        CullFaceSetting newSetting = new CullFaceSetting(mSetting);
        assertFalse(setting.equals(newSetting));
        assertEquals(History.SET, setting.getHistory());

        /**
         * Test setting with new setting that is default false.
         */
        setting.set(newSetting);
        assertEquals(History.SET, setting.getHistory());
        assertFalse(setting.get());
        assertTrue(setting.equals(newSetting));

        /**
         * Set something different from default and test.
         */
        setting.set(true);
        assertTrue(setting.getActionCount() == 1);
        assertEquals(RsActionId.CULL_FACE, setting.getAction(0).getType());
    }

    public void testDepthTest() {
        /**
         * Default.
         */
        DepthTestSetting setting = new DepthTestSetting(mSetting);
        assertEquals(History.DEFAULT, setting.getHistory());
        assertFalse(setting.get());
        assertFalse(setting.hasActions());

        /**
         * Set true
         */
        setting.set(true);
        assertEquals(History.SET, setting.getHistory());
        assertTrue(setting.get());

        /**
         * Inherit false
         */
        setting.inherit(false);
        assertEquals(History.INHERITED, setting.getHistory());
        assertFalse(setting.get());

        /**
         * Set true and test equals with default false.
         */
        setting.set(true);
        DepthTestSetting newSetting = new DepthTestSetting(mSetting);
        assertFalse(setting.equals(newSetting));
        assertEquals(History.SET, setting.getHistory());

        /**
         * Test setting with new setting that is default false.
         */
        setting.set(newSetting);
        assertEquals(History.SET, setting.getHistory());
        assertFalse(setting.get());
        assertTrue(setting.equals(newSetting));

        /**
         * Set something different from default and test.
         */
        setting.set(true);
        assertTrue(setting.getActionCount() == 1);
        assertEquals(RsActionId.DEPTH_TEST, setting.getAction(0).getType());
    }

    public void testDither() {
        /**
         * Default.
         */
        DitherSetting setting = new DitherSetting(mSetting);
        assertEquals(History.DEFAULT, setting.getHistory());
        assertTrue(setting.get());
        assertFalse(setting.hasActions());

        /**
         * Set false
         */
        setting.set(false);
        assertEquals(History.SET, setting.getHistory());
        assertFalse(setting.get());

        /**
         * Inherit true
         */
        setting.inherit(true);
        assertEquals(History.INHERITED, setting.getHistory());
        assertTrue(setting.get());

        /**
         * Set false and test equals with default true.
         */
        setting.set(false);
        DitherSetting newSetting = new DitherSetting(mSetting);
        assertFalse(setting.equals(newSetting));
        assertEquals(History.SET, setting.getHistory());

        /**
         * Test setting with new setting that is default true.
         */
        setting.set(newSetting);
        assertEquals(History.SET, setting.getHistory());
        assertTrue(setting.get());
        assertTrue(setting.equals(newSetting));

        /**
         * Set something different from default and test.
         */
        setting.set(false);
        assertTrue(setting.getActionCount() == 1);
        assertEquals(RsActionId.DITHER, setting.getAction(0).getType());
    }

    public void testPolygonOffsetFill() {
        /**
         * Default.
         */
        PolygonOffsetFillSetting setting = new PolygonOffsetFillSetting(mSetting);
        assertEquals(History.DEFAULT, setting.getHistory());
        assertFalse(setting.get());
        assertFalse(setting.hasActions());

        /**
         * Set true
         */
        setting.set(true);
        assertEquals(History.SET, setting.getHistory());
        assertTrue(setting.get());

        /**
         * Inherit false
         */
        setting.inherit(false);
        assertEquals(History.INHERITED, setting.getHistory());
        assertFalse(setting.get());

        /**
         * Set true and test equals with default false.
         */
        setting.set(true);
        PolygonOffsetFillSetting newSetting = new PolygonOffsetFillSetting(mSetting);
        assertFalse(setting.equals(newSetting));
        assertEquals(History.SET, setting.getHistory());

        /**
         * Test setting with new setting that is default false.
         */
        setting.set(newSetting);
        assertEquals(History.SET, setting.getHistory());
        assertFalse(setting.get());
        assertTrue(setting.equals(newSetting));

        /**
         * Set something different from default and test.
         */
        setting.set(true);
        assertTrue(setting.getActionCount() == 1);
        assertEquals(RsActionId.POLYGON_OFFSET_FILL, setting.getAction(0).getType());
    }

    public void testSampleAlphaToCoverage() {
        /**
         * Default.
         */
        SampleAlphaToCoverageSetting setting = new SampleAlphaToCoverageSetting(mSetting);
        assertEquals(History.DEFAULT, setting.getHistory());
        assertFalse(setting.get());
        assertFalse(setting.hasActions());

        /**
         * Set true
         */
        setting.set(true);
        assertEquals(History.SET, setting.getHistory());
        assertTrue(setting.get());

        /**
         * Inherit false
         */
        setting.inherit(false);
        assertEquals(History.INHERITED, setting.getHistory());
        assertFalse(setting.get());

        /**
         * Set true and test equals with default false.
         */
        setting.set(true);
        SampleAlphaToCoverageSetting newSetting = new SampleAlphaToCoverageSetting(mSetting);
        assertFalse(setting.equals(newSetting));
        assertEquals(History.SET, setting.getHistory());

        /**
         * Test setting with new setting that is default false.
         */
        setting.set(newSetting);
        assertEquals(History.SET, setting.getHistory());
        assertFalse(setting.get());
        assertTrue(setting.equals(newSetting));

        /**
         * Set something different from default and test.
         */
        setting.set(true);
        assertTrue(setting.getActionCount() == 1);
        assertEquals(RsActionId.SAMPLE_ALPHA_TO_COVERAGE, setting.getAction(0).getType());
    }

    public void testSampleCoverage() {
        /**
         * Default.
         */
        SampleCoverageSetting setting = new SampleCoverageSetting(mSetting);
        assertEquals(History.DEFAULT, setting.getHistory());
        assertFalse(setting.get());
        assertFalse(setting.hasActions());

        /**
         * Set true
         */
        setting.set(true);
        assertEquals(History.SET, setting.getHistory());
        assertTrue(setting.get());

        /**
         * Inherit false
         */
        setting.inherit(false);
        assertEquals(History.INHERITED, setting.getHistory());
        assertFalse(setting.get());

        /**
         * Set true and test equals with default false.
         */
        setting.set(true);
        SampleCoverageSetting newSetting = new SampleCoverageSetting(mSetting);
        assertFalse(setting.equals(newSetting));
        assertEquals(History.SET, setting.getHistory());

        /**
         * Test setting with new setting that is default false.
         */
        setting.set(newSetting);
        assertEquals(History.SET, setting.getHistory());
        assertFalse(setting.get());
        assertTrue(setting.equals(newSetting));

        /**
         * Set something different from default and test.
         */
        setting.set(true);
        assertTrue(setting.getActionCount() == 1);
        assertEquals(RsActionId.SAMPLE_COVERAGE, setting.getAction(0).getType());
    }

    public void testScissorTest() {
        /**
         * Default.
         */
        ScissorTestSetting setting = new ScissorTestSetting(mSetting);
        assertEquals(History.DEFAULT, setting.getHistory());
        assertFalse(setting.get());
        assertFalse(setting.hasActions());

        /**
         * Set true
         */
        setting.set(true);
        assertEquals(History.SET, setting.getHistory());
        assertTrue(setting.get());

        /**
         * Inherit false
         */
        setting.inherit(false);
        assertEquals(History.INHERITED, setting.getHistory());
        assertFalse(setting.get());

        /**
         * Set true and test equals with default false.
         */
        setting.set(true);
        ScissorTestSetting newSetting = new ScissorTestSetting(mSetting);
        assertFalse(setting.equals(newSetting));
        assertEquals(History.SET, setting.getHistory());

        /**
         * Test setting with new setting that is default false.
         */
        setting.set(newSetting);
        assertEquals(History.SET, setting.getHistory());
        assertFalse(setting.get());
        assertTrue(setting.equals(newSetting));

        /**
         * Set something different from default and test.
         */
        setting.set(true);
        assertTrue(setting.getActionCount() == 1);
        assertEquals(RsActionId.SCISSOR_TEST, setting.getAction(0).getType());
    }

    public void testStencilTest() {
        /**
         * Default.
         */
        StencilTestSetting setting = new StencilTestSetting(mSetting);
        assertEquals(History.DEFAULT, setting.getHistory());
        assertFalse(setting.get());
        assertFalse(setting.hasActions());

        /**
         * Set true
         */
        setting.set(true);
        assertEquals(History.SET, setting.getHistory());
        assertTrue(setting.get());

        /**
         * Inherit false
         */
        setting.inherit(false);
        assertEquals(History.INHERITED, setting.getHistory());
        assertFalse(setting.get());

        /**
         * Set true and test equals with default false.
         */
        setting.set(true);
        StencilTestSetting newSetting = new StencilTestSetting(mSetting);
        assertFalse(setting.equals(newSetting));
        assertEquals(History.SET, setting.getHistory());

        /**
         * Test setting with new setting that is default false.
         */
        setting.set(newSetting);
        assertEquals(History.SET, setting.getHistory());
        assertFalse(setting.get());
        assertTrue(setting.equals(newSetting));

        /**
         * Set something different from default and test.
         */
        setting.set(true);
        assertTrue(setting.getActionCount() == 1);
        assertEquals(RsActionId.STENCIL_TEST, setting.getAction(0).getType());
    }

    public void testBlendEquation() {
        /**
         * Default.
         */
        BlendEquationSetting setting = new BlendEquationSetting(mSetting);
        assertEquals(History.DEFAULT, setting.getHistory());
        assertTrue(setting.get().equals(BlendEquationFunc.ADD));
        assertFalse(setting.hasActions());

        /**
         * Set different.
         */
        setting.set(BlendEquationFunc.REVERSE_SUBTRACT);
        assertEquals(History.SET, setting.getHistory());
        assertTrue(setting.get().equals(BlendEquationFunc.REVERSE_SUBTRACT));

        /**
         * Inherit different
         */
        setting.inherit(BlendEquationFunc.SUBTRACT);
        assertEquals(History.INHERITED, setting.getHistory());
        assertTrue(setting.get().equals(BlendEquationFunc.SUBTRACT));

        /**
         * Test equals with default.
         */
        BlendEquationSetting newSetting = new BlendEquationSetting(mSetting);
        assertFalse(setting.equals(newSetting));

        /**
         * Test setting with new setting that is default.
         */
        setting.set(newSetting);
        assertEquals(History.SET, setting.getHistory());
        assertTrue(setting.get().equals(BlendEquationFunc.ADD));
        assertTrue(setting.equals(newSetting));

        /**
         * Set something different from default and test.
         */
        setting.set(BlendEquationFunc.SUBTRACT);
        assertTrue(setting.getActionCount() == 1);
        assertEquals(RsActionId.BLEND_EQUATION_FUNC, setting.getAction(0).getType());
    }

    public void testBlendFactors() {
        /**
         * Default.
         */
        BlendFactorsSetting setting = new BlendFactorsSetting(mSetting);
        assertEquals(History.DEFAULT, setting.getHistory());
        assertTrue(setting.getSrcFactor().equals(BlendSrcFact.ONE));
        assertTrue(setting.getDstFactor().equals(BlendDstFact.ZERO));
        assertFalse(setting.hasActions());

        /**
         * Set different.
         */
        setting.set(BlendSrcFact.CONSTANT_ALPHA, BlendDstFact.ZERO);
        assertEquals(History.SET, setting.getHistory());
        assertTrue(setting.getSrcFactor().equals(BlendSrcFact.CONSTANT_ALPHA));
        assertTrue(setting.getDstFactor().equals(BlendDstFact.ZERO));


        /**
         * Inherit different
         */
        setting.inherit(BlendSrcFact.CONSTANT_COLOR, BlendDstFact.SRC_COLOR);
        assertEquals(History.INHERITED, setting.getHistory());
        assertTrue(setting.getSrcFactor().equals(BlendSrcFact.CONSTANT_COLOR));
        assertTrue(setting.getDstFactor().equals(BlendDstFact.SRC_COLOR));

        /**
         * Test equals with default.
         */
        BlendFactorsSetting newSetting = new BlendFactorsSetting(mSetting);
        assertFalse(setting.equals(newSetting));

        /**
         * Test setting with new setting that is default.
         */
        setting.set(newSetting);
        assertEquals(History.SET, setting.getHistory());
        assertTrue(setting.getSrcFactor().equals(BlendSrcFact.ONE));
        assertTrue(setting.getDstFactor().equals(BlendDstFact.ZERO));

        /**
         * Set something different from default and test.
         */
        setting.set(BlendSrcFact.CONSTANT_ALPHA, BlendDstFact.SRC_COLOR);
        assertTrue(setting.getActionCount() == 1);
        assertEquals(RsActionId.BLEND_FACT, setting.getAction(0).getType());
    }

    public void testClear() {
        /**
         * Default.
         */
        ClearSetting setting = new ClearSetting(mSetting);
        assertEquals(History.DEFAULT, setting.getHistory());
        assertEquals(0, setting.getClearOrder());
        assertNotNull(setting.getClearBits());
        assertEquals(1, setting.getClearBits().length);
        assertEquals(ClearBit.ZERO_BIT, setting.getClearBits()[0]);
        assertEquals(0, setting.getClearMask().getGL());
        assertFalse(setting.hasActions());

        /**
         * Set different.
         */
        setting.set(33, ClearBit.COLOR_BUFFER_BIT, ClearBit.DEPTH_BUFFER_BIT);
        assertEquals(History.SET, setting.getHistory());
        assertEquals(33, setting.getClearOrder());
        assertNotNull(setting.getClearBits());
        assertEquals(2, setting.getClearBits().length);
        assertEquals(ClearBit.COLOR_BUFFER_BIT, setting.getClearBits()[0]);
        assertEquals(ClearBit.DEPTH_BUFFER_BIT, setting.getClearBits()[1]);

        /**
         * Inherit different
         */
        setting.inherit(44, ClearBit.DEPTH_BUFFER_BIT);
        assertEquals(History.INHERITED, setting.getHistory());
        assertEquals(44, setting.getClearOrder());
        assertNotNull(setting.getClearBits());
        assertEquals(1, setting.getClearBits().length);
        assertEquals(ClearBit.DEPTH_BUFFER_BIT, setting.getClearBits()[0]);

        /**
         * Test equals with default.
         */
        ClearSetting newSetting = new ClearSetting(mSetting);
        assertFalse(setting.equals(newSetting));

        /**
         * Test setting with new setting that is default.
         */
        setting.set(newSetting);
        assertEquals(History.SET, setting.getHistory());
        assertEquals(0, setting.getClearOrder());
        assertNotNull(setting.getClearBits());
        assertEquals(1, setting.getClearBits().length);
        assertEquals(ClearBit.ZERO_BIT, setting.getClearBits()[0]);
        assertEquals(0, setting.getClearMask().getGL());

        /**
         * Set something different from default and test.
         */
        setting.set(333, ClearBit.COLOR_BUFFER_BIT, ClearBit.DEPTH_BUFFER_BIT, ClearBit.STENCIL_BUFFER_BIT);
        assertTrue(setting.getActionCount() == 1);
        assertEquals(333, setting.getClearOrder());
        assertNotNull(setting.getClearBits());
        assertEquals(3, setting.getClearBits().length);
        assertEquals(ClearBit.COLOR_BUFFER_BIT, setting.getClearBits()[0]);
        assertEquals(ClearBit.DEPTH_BUFFER_BIT, setting.getClearBits()[1]);
        assertEquals(ClearBit.STENCIL_BUFFER_BIT, setting.getClearBits()[2]);
        assertTrue(setting.hasActions());
        assertEquals(RsActionId.CLEAR, setting.getAction(0).getType());
    }


    public void testClearColor() {
        /**
         * Default.
         */
        ClearColorSetting setting = new ClearColorSetting(mSetting);
        assertEquals(History.DEFAULT, setting.getHistory());
        assertNotNull(setting.getClearColor());

        ClearColorSetting other = new ClearColorSetting(mSetting);

        other.set(new float[]{0, 0, 0, 0});
        assertTrue(setting.equals(other));

        other.set(new float[]{0, 0, 0.1f, 0});
        assertFalse(setting.equals(other));

        assertFalse(setting.hasActions());

        /**
         * Set different.
         */
        setting.set(new float[]{1, 0, 0, 1});
        assertEquals(History.SET, setting.getHistory());
        assertFalse(setting.equals(other));
        other.set(new float[]{1, 0, 0, 1});
        assertTrue(setting.equals(other));

        /**
         * Inherit different
         */
        setting.inherit(new float[]{1, 1, 0, 1});
        assertEquals(History.INHERITED, setting.getHistory());
        assertFalse(setting.equals(other));
        other.set(new float[]{1, 1, 0, 1});
        assertTrue(setting.equals(other));

        /**
         * Test equals with default.
         */
        ClearColorSetting newSetting = new ClearColorSetting(mSetting);
        assertFalse(setting.equals(newSetting));

        /**
         * Test setting with new setting that is default.
         */
        setting.set(newSetting);
        assertEquals(History.SET, setting.getHistory());
        assertTrue(setting.equals(newSetting));

        /**
         * Set something different from default and test.
         */
        setting.set(new float[]{1, 0, 0, 1});
        assertTrue(setting.getActionCount() == 1);
        assertEquals(1, setting.getClearColor()[0], FloatUtil.EPS);
        assertEquals(0, setting.getClearColor()[1], FloatUtil.EPS);
        assertEquals(0, setting.getClearColor()[2], FloatUtil.EPS);
        assertEquals(1, setting.getClearColor()[3], FloatUtil.EPS);
        assertEquals(RsActionId.CLEAR_COLOR, setting.getAction(0).getType());
    }

    public void testClearDepth() {
        /**
         * Default.
         */
        ClearDepthSetting setting = new ClearDepthSetting(mSetting);
        assertEquals(History.DEFAULT, setting.getHistory());
        assertEquals(1, setting.getClearDepth(), FloatUtil.EPS);
        assertFalse(setting.hasActions());

        /**
         * Set true
         */
        setting.set(0.5f);
        assertEquals(History.SET, setting.getHistory());
        assertEquals(0.5f, setting.getClearDepth(), FloatUtil.EPS);

        /**
         * Inherit false
         */
        setting.inherit(0.7f);
        assertEquals(History.INHERITED, setting.getHistory());
        assertEquals(0.7f, setting.getClearDepth(), FloatUtil.EPS);

        /**
         * Set true and test equals with default false.
         */
        setting.set(0.8f);
        ClearDepthSetting newSetting = new ClearDepthSetting(mSetting);
        assertFalse(setting.equals(newSetting));
        assertEquals(History.SET, setting.getHistory());

        /**
         * Test setting with new setting that is default false.
         */
        setting.set(newSetting);
        assertEquals(History.SET, setting.getHistory());
        assertEquals(1f, setting.getClearDepth(), FloatUtil.EPS);
        assertTrue(setting.equals(newSetting));

        /**
         * Set something different from default and test.
         */
        setting.set(0.24f);
        assertTrue(setting.getActionCount() == 1);
        assertEquals(RsActionId.CLEAR_DEPTH, setting.getAction(0).getType());
    }

    public void testClearStencil() {
        /**
         * Default.
         */
        ClearStencilSetting setting = new ClearStencilSetting(mSetting);
        assertEquals(History.DEFAULT, setting.getHistory());
        assertEquals(0, setting.getClearStencil());
        assertFalse(setting.hasActions());

        /**
         * Set true
         */
        setting.set(5);
        assertEquals(History.SET, setting.getHistory());
        assertEquals(5, setting.getClearStencil());

        /**
         * Inherit false
         */
        setting.inherit(7);
        assertEquals(History.INHERITED, setting.getHistory());
        assertEquals(7, setting.getClearStencil());

        /**
         * Set true and test equals with default false.
         */
        setting.set(8);
        ClearStencilSetting newSetting = new ClearStencilSetting(mSetting);
        assertFalse(setting.equals(newSetting));
        assertEquals(History.SET, setting.getHistory());

        /**
         * Test setting with new setting that is default false.
         */
        setting.set(newSetting);
        assertEquals(History.SET, setting.getHistory());
        assertEquals(0f, setting.getClearStencil(), FloatUtil.EPS);
        assertTrue(setting.equals(newSetting));

        /**
         * Set something different from default and test.
         */
        setting.set(24);
        assertTrue(setting.getActionCount() == 1);
        assertEquals(RsActionId.CLEAR_STENCIL, setting.getAction(0).getType());
    }
}
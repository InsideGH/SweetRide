package com.sweetlab.sweetride.touch;

import android.graphics.RectF;

import com.sweetlab.sweetride.math.FloatUtil;

import junit.framework.TestCase;

/**
 * Test coordinate converter.
 */
public class CoordinateConverterTest extends TestCase {

    public void testX1() {
        float x;
        RectF from = new RectF(0, 0, 1080, 1920);
        RectF to = new RectF(-1, 1, 1, -1);
        CoordinateConverter conv = new CoordinateConverter(from, to);

        x = conv.getX(0);
        assertEquals(-1, x, FloatUtil.EPS);

        x = conv.getX(540);
        assertEquals(0, x, FloatUtil.EPS);

        x = conv.getX(1080);
        assertEquals(1, x, FloatUtil.EPS);
    }

    public void testY1() {
        float y;
        RectF from = new RectF(0, 0, 1080, 1920);
        RectF to = new RectF(-1, 1, 1, -1);
        CoordinateConverter conv = new CoordinateConverter(from, to);

        y = conv.getY(0);
        assertEquals(1, y, FloatUtil.EPS);

        y = conv.getY(960);
        assertEquals(0, y, FloatUtil.EPS);

        y = conv.getY(1920);
        assertEquals(-1, y, FloatUtil.EPS);
    }

    public void testX2() {
        float x;
        RectF from = new RectF(-1000, -1000, 1000, 1000);
        RectF to = new RectF(-1, 1, 1, -1);
        CoordinateConverter conv = new CoordinateConverter(from, to);

        x = conv.getX(-1000);
        assertEquals(-1, x, FloatUtil.EPS);

        x = conv.getX(-500);
        assertEquals(-0.5f, x, FloatUtil.EPS);

        x = conv.getX(0);
        assertEquals(0, x, FloatUtil.EPS);

        x = conv.getX(500);
        assertEquals(0.5f, x, FloatUtil.EPS);

        x = conv.getX(1000);
        assertEquals(1, x, FloatUtil.EPS);
    }

    public void testY2() {
        float y;
        RectF from = new RectF(-1000, -1000, 1000, 1000);
        RectF to = new RectF(-1, 1, 1, -1);
        CoordinateConverter conv = new CoordinateConverter(from, to);

        y = conv.getY(-1000);
        assertEquals(1, y, FloatUtil.EPS);

        y = conv.getY(-500);
        assertEquals(0.5f, y, FloatUtil.EPS);

        y = conv.getY(0);
        assertEquals(0, y, FloatUtil.EPS);

        y = conv.getY(500);
        assertEquals(-0.5f, y, FloatUtil.EPS);

        y = conv.getY(1000);
        assertEquals(-1, y, FloatUtil.EPS);
    }

    public void testX3() {
        float x;
        RectF from = new RectF(-3000, 4000, -1000, 5000);
        RectF to = new RectF(-1, 1, 1, -1);
        CoordinateConverter conv = new CoordinateConverter(from, to);

        x = conv.getX(-3000);
        assertEquals(-1, x, FloatUtil.EPS);

        x = conv.getX(-2500);
        assertEquals(-0.5f, x, FloatUtil.EPS);

        x = conv.getX(-2000);
        assertEquals(0, x, FloatUtil.EPS);

        x = conv.getX(-1500);
        assertEquals(0.5f, x, FloatUtil.EPS);

        x = conv.getX(-1000);
        assertEquals(1, x, FloatUtil.EPS);
    }

    public void testY3() {
        float y;
        RectF from = new RectF(-3000, 4000, -1000, 5000);
        RectF to = new RectF(-1, 1, 1, -1);
        CoordinateConverter conv = new CoordinateConverter(from, to);

        y = conv.getY(4000);
        assertEquals(1, y, FloatUtil.EPS);

        y = conv.getY(4250);
        assertEquals(0.5f, y, FloatUtil.EPS);

        y = conv.getY(4500);
        assertEquals(0, y, FloatUtil.EPS);

        y = conv.getY(4750);
        assertEquals(-0.5f, y, FloatUtil.EPS);

        y = conv.getY(5000);
        assertEquals(-1, y, FloatUtil.EPS);
    }

    public void testY4() {
        float y;
        RectF from = new RectF(-3000, 4000, -1000, 5000);
        RectF to = new RectF(-1, 10, 1, -30);
        CoordinateConverter conv = new CoordinateConverter(from, to);

        y = conv.getY(4000);
        assertEquals(10, y, FloatUtil.EPS);

        y = conv.getY(4250);
        assertEquals(0f, y, FloatUtil.EPS);

        y = conv.getY(4500);
        assertEquals(-10f, y, FloatUtil.EPS);

        y = conv.getY(4750);
        assertEquals(-20f, y, FloatUtil.EPS);

        y = conv.getY(5000);
        assertEquals(-30f, y, FloatUtil.EPS);
    }

    public void testY5() {
        float y;
        RectF from = new RectF(-3000, 4000, -1000, 5000);
        RectF to = new RectF(-1, -10, 1, 30);
        CoordinateConverter conv = new CoordinateConverter(from, to);

        y = conv.getY(4000);
        assertEquals(-10, y, FloatUtil.EPS);

        y = conv.getY(4250);
        assertEquals(0f, y, FloatUtil.EPS);

        y = conv.getY(4500);
        assertEquals(10f, y, FloatUtil.EPS);

        y = conv.getY(4750);
        assertEquals(20f, y, FloatUtil.EPS);

        y = conv.getY(5000);
        assertEquals(30f, y, FloatUtil.EPS);
    }

}
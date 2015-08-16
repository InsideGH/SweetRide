package com.sweetlab.sweetride.demo.game.curve;

import android.util.Log;

import com.sweetlab.sweetride.math.Vec3;

import junit.framework.TestCase;

/**
 * Test CatmullRom spline.
 */
public class CatmullRomSplineTest extends TestCase {

    public void testCalcInterpolatedPoint() throws Exception {
        Vec3 p0 = new Vec3(-1, -1, 0);
        Vec3 p1 = new Vec3(0, 0, 0); // spline start.
        Vec3 p2 = new Vec3(1, 1, 0);
        Vec3 p3 = new Vec3(2, 0, 0);
        Vec3 p4 = new Vec3(3, 20, 0);
        Vec3 p5 = new Vec3(1, 2, 0);
        Vec3 p6 = new Vec3(0, 0, 0); // spline end.
        Vec3 p7 = new Vec3(1, -1, 0);

        CatmullRomSpline.Builder builder = new CatmullRomSpline.Builder(p0, p1, p2, p3);
        builder.addPoint(p4);
        builder.addPoint(p5);
        builder.addPoint(p6);
        builder.addPoint(p7);
        builder.setRiemannIterations(40);

        CatmullRomSpline spline = builder.build();
        Vec3 result = new Vec3();
        spline.calcInterpolatedPoint(0, result);
        assertEquals(0, result.x, 0.001f);
        assertEquals(0, result.y, 0.001f);
        spline.calcInterpolatedPoint(1, result);
        assertEquals(0, result.x, 0.001f);
        assertEquals(0, result.y, 0.001f);
//        printToExcel(spline);
    }

    public void testCalcInterpolatedPoint2() throws Exception {
        Vec3 p0 = new Vec3(-2, -1, 0);
        Vec3 p1 = new Vec3(0, 0, 0); // spline start
        Vec3 p2 = new Vec3(2, 1, 0);
        Vec3 p3 = new Vec3(12, 3, 0); // spline end
        Vec3 p4 = new Vec3(14, 3, 0);

        CatmullRomSpline.Builder builder = new CatmullRomSpline.Builder(p0, p1, p2, p3);
        builder.addPoint(p4);
        builder.setRiemannIterations(40);

        CatmullRomSpline spline = builder.build();
        Vec3 result = new Vec3();
        spline.calcInterpolatedPoint(0, result);
        assertEquals(0, result.x, 0.001f);
        assertEquals(0, result.y, 0.001f);
        spline.calcInterpolatedPoint(1, result);
        assertEquals(12, result.x, 0.001f);
        assertEquals(3, result.y, 0.001f);
//        printToExcel(spline);
    }

    public void testCalcInterpolatedCircle() throws Exception {
        Vec3 p0 = new Vec3(0, -1, 0);
        Vec3 p1 = new Vec3(1, 0, 0); // spline start.
        Vec3 p2 = new Vec3(0, 1, 0);
        Vec3 p3 = new Vec3(-1, 0, 0);
        Vec3 p4 = new Vec3(0, -1, 0);
        Vec3 p5 = new Vec3(1, 0, 0); // spline end.
        Vec3 p6 = new Vec3(0, 1, 0);

        CatmullRomSpline.Builder builder = new CatmullRomSpline.Builder(p0, p1, p2, p3);
        builder.addPoint(p4);
        builder.addPoint(p5);
        builder.addPoint(p6);
        builder.setRiemannIterations(40);

        CatmullRomSpline spline = builder.build();
        Vec3 result = new Vec3();

        spline.calcInterpolatedPoint(0, result);
        assertEquals(1, result.x, 0.001f);
        assertEquals(0, result.y, 0.001f);

        spline.calcInterpolatedPoint(0.25f, result);
        assertEquals(0, result.x, 0.001f);
        assertEquals(1, result.y, 0.001f);

        spline.calcInterpolatedPoint(0.5f, result);
        assertEquals(-1, result.x, 0.001f);
        assertEquals(0, result.y, 0.001f);

        spline.calcInterpolatedPoint(0.75f, result);
        assertEquals(0, result.x, 0.001f);
        assertEquals(-1, result.y, 0.001f);

        spline.calcInterpolatedPoint(1.0f, result);
        assertEquals(1, result.x, 0.001f);
        assertEquals(0, result.y, 0.001f);

//        printToExcel(spline);
    }

    public void testLength() throws Exception {
        Vec3 p0 = new Vec3(0, 0, 0);
        Vec3 p1 = new Vec3(1, 0, 0);
        Vec3 p2 = new Vec3(2, 0, 0);
        Vec3 p3 = new Vec3(3, 0, 0);
        Vec3 p4 = new Vec3(4, 0, 0);
        Vec3 p5 = new Vec3(5, 0, 0);
        Vec3 p6 = new Vec3(6, 0, 0);

        CatmullRomSpline.Builder builder = new CatmullRomSpline.Builder(p0, p1, p2, p3);
        builder.addPoint(p4);
        builder.addPoint(p5);
        builder.addPoint(p6);
        CatmullRomSpline spline= builder.build();
        assertEquals(4, spline.getLength(), 0.0001f);
    }

    /**
     * Method used to print values and plot in excel-like program.
     *
     * @param spline The spline.
     */
    private void printToExcel(CatmullRomSpline spline) {
        Log.d("Peter100", "length " + spline.getLength());
        Vec3 result = new Vec3();
        for (int i = 0; i < 50; i++) {
            float t = i / 49.0f;
            spline.calcInterpolatedPoint(t, result);
            Log.d("Peter100", "" + result.x + " " + result.y);
        }
    }
}
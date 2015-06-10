package com.sweetlab.sweetride.mesh;

import com.sweetlab.sweetride.Util.BufferTestUtil;
import com.sweetlab.sweetride.math.Matrix44;

import junit.framework.TestCase;

/**
 * Test bounding box.
 */
public class BoundingBoxTest extends TestCase {
    /**
     * Float compare.
     */
    private static final float EPS = 10e-6f;

    /**
     * The bounding box.
     */
    private BoundingBox mBoundingBox;

    public void setUp() throws Exception {
        super.setUp();
        mBoundingBox = new BoundingBox();
        float[] quadStrip = BufferTestUtil.createQuadStrip(1, 2, 0, 0);
        mBoundingBox.addVertices(quadStrip);
    }

    public void testSet() {
        float[] min = mBoundingBox.getMin();
        assertEquals(-0.5f, min[0], EPS);
        assertEquals(-1.0f, min[1], EPS);
        assertEquals(0.0f, min[2], EPS);
        float[] max = mBoundingBox.getMax();
        assertEquals(0.5f, max[0], EPS);
        assertEquals(1.0f, max[1], EPS);
        assertEquals(0.0f, max[2], EPS);
    }

    /**
     * Test transform around z axis.
     */
    public void testTransformZAxis() {
        BoundingBox transformedBox = new BoundingBox();
        Matrix44 mat = new Matrix44().setRotate(90, 0, 0, 1);
        mBoundingBox.transform(mat, transformedBox);
        float[] min = transformedBox.getMin();
        assertEquals(-1.0f, min[0], EPS);
        assertEquals(-0.5f, min[1], EPS);
        assertEquals(0.0f, min[2], EPS);
        float[] max = transformedBox.getMax();
        assertEquals(1.0f, max[0], EPS);
        assertEquals(0.5f, max[1], EPS);
        assertEquals(0.0f, max[2], EPS);
    }

    /**
     * Test transform around x axis.
     */
    public void testTransformXAxis() {
        BoundingBox transformedBox = new BoundingBox();
        Matrix44 mat = new Matrix44().setRotate(90, 1, 0, 0);
        mBoundingBox.transform(mat, transformedBox);
        float[] min = transformedBox.getMin();
        assertEquals(-0.5f, min[0], EPS);
        assertEquals(0f, min[1], EPS);
        assertEquals(-1f, min[2], EPS);
        float[] max = transformedBox.getMax();
        assertEquals(0.5f, max[0], EPS);
        assertEquals(0f, max[1], EPS);
        assertEquals(1.0f, max[2], EPS);
    }

    /**
     * Test transform around y axis.
     */
    public void testTransformYAxis() {
        BoundingBox transformedBox = new BoundingBox();
        Matrix44 mat = new Matrix44().setRotate(90, 0, 1, 0);
        mBoundingBox.transform(mat, transformedBox);
        float[] min = transformedBox.getMin();
        assertEquals(0f, min[0], EPS);
        assertEquals(-1f, min[1], EPS);
        assertEquals(-0.5f, min[2], EPS);
        float[] max = transformedBox.getMax();
        assertEquals(0f, max[0], EPS);
        assertEquals(1f, max[1], EPS);
        assertEquals(0.5f, max[2], EPS);
    }

    /**
     * Test transform translate.
     */
    public void testTransformTranslate() {
        BoundingBox transformedBox = new BoundingBox();
        Matrix44 mat = new Matrix44().setTranslate(33, 44, 55);
        mBoundingBox.transform(mat, transformedBox);
        float[] min = transformedBox.getMin();
        assertEquals(-0.5f + 33, min[0], EPS);
        assertEquals(-1.0f + 44, min[1], EPS);
        assertEquals(0.0f + 55, min[2], EPS);
        float[] max = transformedBox.getMax();
        assertEquals(0.5f + 33, max[0], EPS);
        assertEquals(1.0f + 44, max[1], EPS);
        assertEquals(0.0f + 55, max[2], EPS);
    }

    /**
     * Test transform translate, rotate.
     */
    public void testTransformTranslateRotate() {
        BoundingBox transformedBox = new BoundingBox();
        Matrix44 mat = new Matrix44().setTranslate(33, 44, 55);
        mat.rotate(90, 0, 0, 1);

        mBoundingBox.transform(mat, transformedBox);

        float[] min = transformedBox.getMin();
        assertEquals(-1.0f + 33, min[0], EPS);
        assertEquals(-0.5f + 44, min[1], EPS);
        assertEquals(0.0f + 55, min[2], EPS);
        float[] max = transformedBox.getMax();
        assertEquals(1.0f + 33, max[0], EPS);
        assertEquals(0.5f + 44, max[1], EPS);
        assertEquals(0.0f + 55, max[2], EPS);
    }

    /**
     * Test transform rotate, translate.
     */
    public void testTransformRotateTranslate() {
        BoundingBox transformedBox = new BoundingBox();
        /**
         * Rotate first...
         */
        Matrix44 mat = new Matrix44().setRotate(90, 0, 0, 1);
        /**
         * Translate with rotated coordinate space. So 1 step along the x axis is actually
         * 1 step along the positive y axis. And 30 steps along the y axis is actually 30 steps
         * along the negative x axis. And 100 steps along the positive z axis is the same.
         */
        mat.translate(1, 30, 100);

        mBoundingBox.transform(mat, transformedBox);

        float[] min = transformedBox.getMin();
        assertEquals(-1.0f - 30, min[0], EPS);
        assertEquals(-0.5f + 1, min[1], EPS);
        assertEquals(0.0f + 100, min[2], EPS);
        float[] max = transformedBox.getMax();
        assertEquals(1.0f - 30, max[0], EPS);
        assertEquals(0.5f + 1, max[1], EPS);
        assertEquals(0.0f + 100, max[2], EPS);
    }

    /**
     * Test transform scale.
     */
    public void testTransformScale() {
        BoundingBox transformedBox = new BoundingBox();
        /**
         * Scale y axis.
         */
        Matrix44 mat = new Matrix44().setScale(1, 0.5f, 1);

        mBoundingBox.transform(mat, transformedBox);

        float[] min = transformedBox.getMin();
        assertEquals(-0.5f, min[0], EPS);
        assertEquals(-0.5f, min[1], EPS);
        assertEquals(0.0f, min[2], EPS);
        float[] max = transformedBox.getMax();
        assertEquals(0.5f, max[0], EPS);
        assertEquals(0.5f, max[1], EPS);
        assertEquals(0.0f, max[2], EPS);
    }

    /**
     * Test transform scale.
     */
    public void testTransformScaleTranslate() {
        BoundingBox transformedBox = new BoundingBox();
        /**
         * Scale y axis.
         */
        Matrix44 mat = new Matrix44().setScale(1, 0.5f, 1);
        mat.translate(10,20,40);
        mBoundingBox.transform(mat, transformedBox);

        float[] min = transformedBox.getMin();
        assertEquals(-0.5f + 10, min[0], EPS);
        assertEquals(-0.5f + 10, min[1], EPS);
        assertEquals(0.0f + 40, min[2], EPS);
        float[] max = transformedBox.getMax();
        assertEquals(0.5f + 10, max[0], EPS);
        assertEquals(0.5f + 10, max[1], EPS);
        assertEquals(0.0f + 40, max[2], EPS);
    }

}
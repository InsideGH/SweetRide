package com.sweetlab.sweetride.mesh;

import com.sweetlab.sweetride.Util.BufferTestUtil;
import com.sweetlab.sweetride.math.FloatUtil;
import com.sweetlab.sweetride.math.Matrix44;
import com.sweetlab.sweetride.math.Vec3;

import junit.framework.TestCase;

/**
 * Test bounding box.
 */
public class BoundingBoxTest extends TestCase {
    /**
     * The bounding box.
     */
    private BoundingBox mBoundingBox;

    /**
     * Temporary min values.
     */
    private Vec3 mMin = new Vec3();

    /**
     * Temporary max values.
     */
    private Vec3 mMax = new Vec3();

    public void setUp() throws Exception {
        super.setUp();
        float[] quadStrip = BufferTestUtil.createQuadStrip(1, 2, 0, 0);
        mBoundingBox = new BoundingBox(quadStrip);
    }

    /**
     * Test set array of vertices.
     */
    public void testSet() {
        mBoundingBox.getMin(mMin);
        assertEquals(-0.5f, mMin.x, FloatUtil.EPS);
        assertEquals(-1.0f, mMin.y, FloatUtil.EPS);
        assertEquals(0.0f, mMin.z, FloatUtil.EPS);
        mBoundingBox.getMax(mMax);
        assertEquals(0.5f, mMax.x, FloatUtil.EPS);
        assertEquals(1.0f, mMax.y, FloatUtil.EPS);
        assertEquals(0.0f, mMax.z, FloatUtil.EPS);
    }

    /**
     * Test set array by indices.
     */
    public void testSetIndices() {
        BoundingBox boundingBox = new BoundingBox(BufferTestUtil.createQuadStrip(1, 2, 0, 0), new short[]{0, 1, 2, 3});
        boundingBox.getMin(mMin);
        assertEquals(-0.5f, mMin.x, FloatUtil.EPS);
        assertEquals(-1.0f, mMin.y, FloatUtil.EPS);
        assertEquals(0.0f, mMin.z, FloatUtil.EPS);
        boundingBox.getMax(mMax);
        assertEquals(0.5f, mMax.x, FloatUtil.EPS);
        assertEquals(1.0f, mMax.y, FloatUtil.EPS);
        assertEquals(0.0f, mMax.z, FloatUtil.EPS);
    }

    /**
     * Test transform around z axis.
     */
    public void testTransformZAxis() {
        TransformableBoundingBox transformedBox = new TransformableBoundingBox();
        Matrix44 mat = new Matrix44().setRotate(90, 0, 0, 1);
        mBoundingBox.transform(mat, transformedBox);
        transformedBox.getMin(mMin);
        assertEquals(-1.0f, mMin.x, FloatUtil.EPS);
        assertEquals(-0.5f, mMin.y, FloatUtil.EPS);
        assertEquals(0.0f, mMin.z, FloatUtil.EPS);
        transformedBox.getMax(mMax);
        assertEquals(1.0f, mMax.x, FloatUtil.EPS);
        assertEquals(0.5f, mMax.y, FloatUtil.EPS);
        assertEquals(0.0f, mMax.z, FloatUtil.EPS);
    }

    /**
     * Test transform around x axis.
     */
    public void testTransformXAxis() {
        TransformableBoundingBox transformedBox = new TransformableBoundingBox();
        Matrix44 mat = new Matrix44().setRotate(90, 1, 0, 0);
        mBoundingBox.transform(mat, transformedBox);
        transformedBox.getMin(mMin);
        assertEquals(-0.5f, mMin.x, FloatUtil.EPS);
        assertEquals(0f, mMin.y, FloatUtil.EPS);
        assertEquals(-1f, mMin.z, FloatUtil.EPS);
        transformedBox.getMax(mMax);
        assertEquals(0.5f, mMax.x, FloatUtil.EPS);
        assertEquals(0f, mMax.y, FloatUtil.EPS);
        assertEquals(1.0f, mMax.z, FloatUtil.EPS);
    }

    /**
     * Test transform around y axis.
     */
    public void testTransformYAxis() {
        TransformableBoundingBox transformedBox = new TransformableBoundingBox();
        Matrix44 mat = new Matrix44().setRotate(90, 0, 1, 0);
        mBoundingBox.transform(mat, transformedBox);
        transformedBox.getMin(mMin);
        assertEquals(0f, mMin.x, FloatUtil.EPS);
        assertEquals(-1f, mMin.y, FloatUtil.EPS);
        assertEquals(-0.5f, mMin.z, FloatUtil.EPS);
        transformedBox.getMax(mMax);
        assertEquals(0f, mMax.x, FloatUtil.EPS);
        assertEquals(1f, mMax.y, FloatUtil.EPS);
        assertEquals(0.5f, mMax.z, FloatUtil.EPS);
    }

    /**
     * Test transform translate.
     */
    public void testTransformTranslate() {
        TransformableBoundingBox transformedBox = new TransformableBoundingBox();
        Matrix44 mat = new Matrix44().setTranslate(33, 44, 55);
        mBoundingBox.transform(mat, transformedBox);
        transformedBox.getMin(mMin);
        assertEquals(-0.5f + 33, mMin.x, FloatUtil.EPS);
        assertEquals(-1.0f + 44, mMin.y, FloatUtil.EPS);
        assertEquals(0.0f + 55, mMin.z, FloatUtil.EPS);
        transformedBox.getMax(mMax);
        assertEquals(0.5f + 33, mMax.x, FloatUtil.EPS);
        assertEquals(1.0f + 44, mMax.y, FloatUtil.EPS);
        assertEquals(0.0f + 55, mMax.z, FloatUtil.EPS);
    }

    /**
     * Test transform translate, rotate.
     */
    public void testTransformTranslateRotate() {
        TransformableBoundingBox transformedBox = new TransformableBoundingBox();
        Matrix44 mat = new Matrix44().setTranslate(33, 44, 55);
        mat.rotate(90, 0, 0, 1);

        mBoundingBox.transform(mat, transformedBox);

        transformedBox.getMin(mMin);
        assertEquals(-1.0f + 33, mMin.x, FloatUtil.EPS);
        assertEquals(-0.5f + 44, mMin.y, FloatUtil.EPS);
        assertEquals(0.0f + 55, mMin.z, FloatUtil.EPS);
        transformedBox.getMax(mMax);
        assertEquals(1.0f + 33, mMax.x, FloatUtil.EPS);
        assertEquals(0.5f + 44, mMax.y, FloatUtil.EPS);
        assertEquals(0.0f + 55, mMax.z, FloatUtil.EPS);
    }

    /**
     * Test transform rotate, translate.
     */
    public void testTransformRotateTranslate() {
        TransformableBoundingBox transformedBox = new TransformableBoundingBox();
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

        transformedBox.getMin(mMin);
        assertEquals(-1.0f - 30, mMin.x, FloatUtil.EPS);
        assertEquals(-0.5f + 1, mMin.y, FloatUtil.EPS);
        assertEquals(0.0f + 100, mMin.z, FloatUtil.EPS);
        transformedBox.getMax(mMax);
        assertEquals(1.0f - 30, mMax.x, FloatUtil.EPS);
        assertEquals(0.5f + 1, mMax.y, FloatUtil.EPS);
        assertEquals(0.0f + 100, mMax.z, FloatUtil.EPS);
    }

    /**
     * Test transform scale.
     */
    public void testTransformScale() {
        TransformableBoundingBox transformedBox = new TransformableBoundingBox();
        /**
         * Scale y axis.
         */
        Matrix44 mat = new Matrix44().setScale(1, 0.5f, 1);

        mBoundingBox.transform(mat, transformedBox);

        transformedBox.getMin(mMin);
        assertEquals(-0.5f, mMin.x, FloatUtil.EPS);
        assertEquals(-0.5f, mMin.y, FloatUtil.EPS);
        assertEquals(0.0f, mMin.z, FloatUtil.EPS);
        transformedBox.getMax(mMax);
        assertEquals(0.5f, mMax.x, FloatUtil.EPS);
        assertEquals(0.5f, mMax.y, FloatUtil.EPS);
        assertEquals(0.0f, mMax.z, FloatUtil.EPS);
    }

    /**
     * Test transform scale.
     */
    public void testTransformScaleTranslate() {
        TransformableBoundingBox transformedBox = new TransformableBoundingBox();
        /**
         * Scale y axis.
         */
        Matrix44 mat = new Matrix44().setScale(1, 0.5f, 1);
        mat.translate(10, 20, 40);
        mBoundingBox.transform(mat, transformedBox);

        transformedBox.getMin(mMin);
        assertEquals(-0.5f + 10, mMin.x, FloatUtil.EPS);
        assertEquals(-0.5f + 10, mMin.y, FloatUtil.EPS);
        assertEquals(0.0f + 40, mMin.z, FloatUtil.EPS);
        transformedBox.getMax(mMax);
        assertEquals(0.5f + 10, mMax.x, FloatUtil.EPS);
        assertEquals(0.5f + 10, mMax.y, FloatUtil.EPS);
        assertEquals(0.0f + 40, mMax.z, FloatUtil.EPS);
    }

}
package com.sweetlab.sweetride.context;

import com.sweetlab.sweetride.context.Util.ActionHelper;
import com.sweetlab.sweetride.context.Util.BufferTestUtil;
import com.sweetlab.sweetride.context.Util.DrawTestUtil;
import com.sweetlab.sweetride.context.Util.ProgramTestUtil;
import com.sweetlab.sweetride.context.Util.Verify;
import com.sweetlab.sweetride.material.Material;
import com.sweetlab.sweetride.mesh.Mesh;
import com.sweetlab.sweetride.testframework.OpenGLTestCase;
import com.sweetlab.sweetride.testframework.ResultRunnable;

public class ArrayInterleavedMultiBuf_MatMesh_notifier extends OpenGLTestCase {
    /**
     * Backend context.
     */
    private BackendContext mContext;

    /**
     * Create material for all four triangles.
     */
    private Material mLeftMaterial;
    private Material mRightMaterial;
    private Material mTopMaterial;
    private Material mBottomMaterial;

    /**
     * Create mesh for all four triangles.
     */
    private Mesh mLeftMesh;
    private Mesh mRightMesh;
    private Mesh mTopMesh;
    private Mesh mBottomMesh;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        /**
         * Create left resources. Complete setup with color shader and mesh with interleaved
         * vertices and color data.
         */
        mLeftMaterial = new Material();
        mLeftMaterial.setShaderProgram(ProgramTestUtil.createNdcColor());
        ActionHelper.handleMainThreadActions(mLeftMaterial);

        mLeftMesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mLeftMesh.addVertexBuffer(BufferTestUtil.createInterleavedLeftTriangleWithColor());
        ActionHelper.handleMainThreadActions(mLeftMesh);

        /**
         * Create top resources. Complete setup, with red-shader and mesh with vertices.
         */
        mTopMaterial = new Material();
        mTopMaterial.setShaderProgram(ProgramTestUtil.createNdcRed());
        ActionHelper.handleMainThreadActions(mTopMaterial);

        mTopMesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mTopMesh.addVertexBuffer(BufferTestUtil.createTopTriangle());
        ActionHelper.handleMainThreadActions(mTopMesh);

        /**
         * Create right resources. Complete setup with color shader and mesh with interleaved
         * vertices and color data.
         */
        mRightMaterial = new Material();
        mRightMaterial.setShaderProgram(ProgramTestUtil.createNdcColor());
        ActionHelper.handleMainThreadActions(mRightMaterial);

        mRightMesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mRightMesh.addVertexBuffer(BufferTestUtil.createInterleavedRightTriangleWithColor());
        ActionHelper.handleMainThreadActions(mRightMesh);

        /**
         * Create bottom resources. Complete setup with color shader and mesh with separate
         * vertex buffers for vertices and color attributes.
         */
        mBottomMaterial = new Material();
        mBottomMaterial.setShaderProgram(ProgramTestUtil.createNdcColor());
        ActionHelper.handleMainThreadActions(mBottomMaterial);

        mBottomMesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mBottomMesh.addVertexBuffer(BufferTestUtil.createBottomTriangle());
        mBottomMesh.addVertexBuffer(BufferTestUtil.createColorBuffer());
        ActionHelper.handleMainThreadActions(mBottomMesh);

        setTestInfo("smooth, red, smooth, smooth material/mesh");

        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                mContext = getBackendContext();

                ActionHelper.handleGLThreadActions(mLeftMaterial, mContext);
                ActionHelper.handleGLThreadActions(mTopMaterial, mContext);
                ActionHelper.handleGLThreadActions(mRightMaterial, mContext);
                ActionHelper.handleGLThreadActions(mBottomMaterial, mContext);
                ActionHelper.handleGLThreadActions(mLeftMesh, mContext);
                ActionHelper.handleGLThreadActions(mTopMesh, mContext);
                ActionHelper.handleGLThreadActions(mRightMesh, mContext);
                ActionHelper.handleGLThreadActions(mBottomMesh, mContext);
                assertFalse(mLeftMaterial.hasActions());
                assertFalse(mTopMaterial.hasActions());
                assertFalse(mRightMaterial.hasActions());
                assertFalse(mBottomMaterial.hasActions());
                assertFalse(mLeftMesh.hasActions());
                assertFalse(mTopMesh.hasActions());
                assertFalse(mRightMesh.hasActions());
                assertFalse(mBottomMesh.hasActions());
                return null;
            }
        });
    }

    /**
     * Draw using both interleaved and non-interleaved vertex buffers.
     *
     * @throws Exception
     */
    public void testArrayInterleavedMultiBuf_MatMesh_notifier() throws Exception {
        runOnDrawFrame(new ResultRunnable() {
            @Override
            public Object run() {
                /**
                 * Clear screen.
                 */
                clearScreen(0.5f, 0.5f, 0.5f, 1.0f);

                /**
                 * This triangle should be smooth colored on left side.
                 */
                DrawTestUtil.drawUsingMaterialAndMesh(mContext, mLeftMaterial, mLeftMesh);

                /**
                 * This triangle should be red on top side.
                 */
                DrawTestUtil.drawUsingMaterialAndMesh(mContext, mTopMaterial, mTopMesh);

                /**
                 * This triangle should be smooth colored on right side.
                 */
                DrawTestUtil.drawUsingMaterialAndMesh(mContext, mRightMaterial, mRightMesh);

                /**
                 * This triangle should be smooth colored on bottom side.
                 */
                DrawTestUtil.drawUsingMaterialAndMesh(mContext, mBottomMaterial, mBottomMesh);

                return null;
            }
        });
        sleepOnDrawFrame(Verify.TERMINATE_TIME);
    }
}
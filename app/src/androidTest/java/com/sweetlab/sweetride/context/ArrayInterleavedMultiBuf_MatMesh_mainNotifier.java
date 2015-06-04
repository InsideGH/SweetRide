package com.sweetlab.sweetride.context;

import com.sweetlab.sweetride.context.Util.ActionHelper;
import com.sweetlab.sweetride.context.Util.BufferTestUtil;
import com.sweetlab.sweetride.context.Util.DrawTestUtil;
import com.sweetlab.sweetride.context.Util.ProgramTestUtil;
import com.sweetlab.sweetride.material.Material;
import com.sweetlab.sweetride.mesh.Mesh;
import com.sweetlab.sweetride.testframework.OpenGLTestCase;
import com.sweetlab.sweetride.testframework.ResultRunnable;

public class ArrayInterleavedMultiBuf_MatMesh_mainNotifier extends OpenGLTestCase {
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

                /**
                 * Create all materials.
                 */
                mLeftMaterial.create(mContext);
                mTopMaterial.create(mContext);
                mRightMaterial.create(mContext);
                mBottomMaterial.create(mContext);

                /**
                 * Create all meshes.
                 */
                mLeftMesh.create(mContext);
                mTopMesh.create(mContext);
                mRightMesh.create(mContext);
                mBottomMesh.create(mContext);

                /**
                 * Load all meshes. (materials doesn't need loading since there is no
                 * textures added.
                 */
                mLeftMesh.load(mContext);
                mRightMesh.load(mContext);
                mTopMesh.load(mContext);
                mBottomMesh.load(mContext);
                return null;
            }
        });
    }

    /**
     * Draw using both interleaved and non-interleaved vertex buffers.
     *
     * @throws Exception
     */
    public void testDrawInterleavedTriangle() throws Exception {
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
        sleepOnDrawFrame(2000);
    }
}
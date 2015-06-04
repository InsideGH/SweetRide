package com.sweetlab.sweetride.context;

import com.sweetlab.sweetride.context.Util.ActionHelper;
import com.sweetlab.sweetride.context.Util.BufferTestUtil;
import com.sweetlab.sweetride.context.Util.DrawTestUtil;
import com.sweetlab.sweetride.context.Util.ProgramTestUtil;
import com.sweetlab.sweetride.material.Material;
import com.sweetlab.sweetride.mesh.Mesh;
import com.sweetlab.sweetride.testframework.OpenGLTestCase;
import com.sweetlab.sweetride.testframework.ResultRunnable;

public class ArrayMultiBuf_MatMesh_mainNotifier extends OpenGLTestCase {
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
         * Create left resources. Complete setup with shader that takes color attribute data
         * and vertex buffer that loads color data.
         */
        mLeftMaterial = new Material();
        mLeftMaterial.setShaderProgram(ProgramTestUtil.createNdcColor());
        ActionHelper.handleMainThreadActions(mLeftMaterial);

        mLeftMesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mLeftMesh.addVertexBuffer(BufferTestUtil.createLeftTriangle());
        mLeftMesh.addVertexBuffer(BufferTestUtil.createColorBuffer());
        ActionHelper.handleMainThreadActions(mLeftMesh);


        /**
         * Create top resources. Not complete setup, the actual color buffer is missing which
         * produces a black triangle.
         */
        mTopMaterial = new Material();
        mTopMaterial.setShaderProgram(ProgramTestUtil.createNdcColor());
        ActionHelper.handleMainThreadActions(mTopMaterial);

        mTopMesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mTopMesh.addVertexBuffer(BufferTestUtil.createTopTriangle());
        ActionHelper.handleMainThreadActions(mTopMesh);


        /**
         * Create right resources. Misconstrued setup. Using a shader that just paints red but
         * adding vertex buffer with color buffer. The vertex buffer is ignored and the triangle
         * is painted red.
         */

        mRightMaterial = new Material();
        mRightMaterial.setShaderProgram(ProgramTestUtil.createNdcRed());
        ActionHelper.handleMainThreadActions(mRightMaterial);

        mRightMesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mRightMesh.addVertexBuffer(BufferTestUtil.createRightTriangle());
        mRightMesh.addVertexBuffer(BufferTestUtil.createColorBuffer());
        ActionHelper.handleMainThreadActions(mRightMesh);


        /**
         * Create bottom resources. Complete setup with shader that takes color attribute data
         * and vertex buffer that loads color data.
         */
        mBottomMaterial = new Material();
        mBottomMaterial.setShaderProgram(ProgramTestUtil.createNdcColor());
        ActionHelper.handleMainThreadActions(mBottomMaterial);

        mBottomMesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mBottomMesh.addVertexBuffer(BufferTestUtil.createBottomTriangle());
        mBottomMesh.addVertexBuffer(BufferTestUtil.createColorBuffer());
        ActionHelper.handleMainThreadActions(mBottomMesh);

        setTestInfo("smooth, black, red, smooth material/mesh");

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

    public void testArrayMultiBuf_MatMesh_mainNotifier() throws Exception {
        runOnDrawFrame(new ResultRunnable() {
            @Override
            public Object run() {
                /**
                 * Clear screen.
                 */
                clearScreen(0.5f, 0.5f, 0.5f, 1.0f);

                /**
                 * Clear screen.
                 */
                clearScreen(0.5f, 0.5f, 0.5f, 1.0f);

                /**
                 * This left triangle should be smooth colored.
                 */
                DrawTestUtil.drawUsingMaterialAndMesh(mContext, mLeftMaterial, mLeftMesh);

                /**
                 * This top triangle should be black since we haven't specified any color buffer and
                 * after a draw attributes are disabled.
                 */
                DrawTestUtil.drawUsingMaterialAndMesh(mContext, mTopMaterial, mTopMesh);

                /**
                 * This shader doesn't know about colors, meaning that we wont find any
                 * attribute. Right triangle should be red.
                 */
                DrawTestUtil.drawUsingMaterialAndMesh(mContext, mRightMaterial, mRightMesh);

                /**
                 * This triangle should be smooth colored.
                 */
                DrawTestUtil.drawUsingMaterialAndMesh(mContext, mBottomMaterial, mBottomMesh);

                return null;
            }
        });
        sleepOnDrawFrame(2000);
    }
}
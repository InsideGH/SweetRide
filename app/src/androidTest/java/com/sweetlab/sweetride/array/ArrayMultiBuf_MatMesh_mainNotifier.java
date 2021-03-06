package com.sweetlab.sweetride.array;

import com.sweetlab.sweetride.Util.BackendRenderSettingsUtil;
import com.sweetlab.sweetride.Util.BufferTestUtil;
import com.sweetlab.sweetride.Util.DrawTestUtil;
import com.sweetlab.sweetride.Util.ProgramTestUtil;
import com.sweetlab.sweetride.Util.Verify;
import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.context.MeshDrawingMode;
import com.sweetlab.sweetride.engine.FrontEndActionHandler;
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

    /**
     * Front end action handler.
     */
    FrontEndActionHandler mActionHandler = new FrontEndActionHandler();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        /**
         * Create left resources. Complete setup with shader that takes color attribute data
         * and vertex buffer that loads color data.
         */
        mLeftMaterial = new Material();
        mLeftMaterial.setShaderProgram(ProgramTestUtil.createNdcColor());
        mActionHandler.handleActions(mLeftMaterial);

        mLeftMesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mLeftMesh.addVertexBuffer(BufferTestUtil.createLeftTriangle());
        mLeftMesh.addVertexBuffer(BufferTestUtil.createColorBuffer());
        mActionHandler.handleActions(mLeftMesh);


        /**
         * Create top resources. Not complete setup, the actual color buffer is missing which
         * produces a black triangle.
         */
        mTopMaterial = new Material();
        mTopMaterial.setShaderProgram(ProgramTestUtil.createNdcColor());
        mActionHandler.handleActions(mTopMaterial);

        mTopMesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mTopMesh.addVertexBuffer(BufferTestUtil.createTopTriangle());
        mActionHandler.handleActions(mTopMesh);


        /**
         * Create right resources. Misconstrued setup. Using a shader that just paints red but
         * adding vertex buffer with color buffer. The vertex buffer is ignored and the triangle
         * is painted red.
         */

        mRightMaterial = new Material();
        mRightMaterial.setShaderProgram(ProgramTestUtil.createNdcRed());
        mActionHandler.handleActions(mRightMaterial);

        mRightMesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mRightMesh.addVertexBuffer(BufferTestUtil.createRightTriangle());
        mRightMesh.addVertexBuffer(BufferTestUtil.createColorBuffer());
        mActionHandler.handleActions(mRightMesh);


        /**
         * Create bottom resources. Complete setup with shader that takes color attribute data
         * and vertex buffer that loads color data.
         */
        mBottomMaterial = new Material();
        mBottomMaterial.setShaderProgram(ProgramTestUtil.createNdcColor());
        mActionHandler.handleActions(mBottomMaterial);

        mBottomMesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mBottomMesh.addVertexBuffer(BufferTestUtil.createBottomTriangle());
        mBottomMesh.addVertexBuffer(BufferTestUtil.createColorBuffer());
        mActionHandler.handleActions(mBottomMesh);

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
                mContext.getRenderState().useSettings(BackendRenderSettingsUtil.getDefaultGrey(getSurfaceWidth(), getSurfaceHeight())).clear();

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
        sleepOnDrawFrame(Verify.TERMINATE_TIME);
    }
}
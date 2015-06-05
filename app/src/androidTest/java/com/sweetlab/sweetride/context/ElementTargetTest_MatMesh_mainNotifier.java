package com.sweetlab.sweetride.context;

import android.opengl.GLES20;

import com.sweetlab.sweetride.attributedata.IndicesBuffer;
import com.sweetlab.sweetride.context.Util.ActionHelper;
import com.sweetlab.sweetride.context.Util.BufferTestUtil;
import com.sweetlab.sweetride.context.Util.DrawTestUtil;
import com.sweetlab.sweetride.context.Util.ProgramTestUtil;
import com.sweetlab.sweetride.context.Util.Verify;
import com.sweetlab.sweetride.material.Material;
import com.sweetlab.sweetride.mesh.Mesh;
import com.sweetlab.sweetride.testframework.OpenGLTestCase;
import com.sweetlab.sweetride.testframework.ResultRunnable;

public class ElementTargetTest_MatMesh_mainNotifier extends OpenGLTestCase {
    /**
     * Backend context.
     */
    private BackendContext mContext;

    /**
     * Red, blue material.
     */
    private Material mBlueMaterial;
    private Material mRedMaterial;

    /**
     * The various positions of the mesh.
     */
    private Mesh mLeftMesh;
    private Mesh mRightMesh;
    private Mesh mTopMesh;
    private Mesh mBottomMesh;

    /**
     * The indices buffer.
     */
    private IndicesBuffer mIndicesBuffer;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        /**
         * Create indices buffer.
         */
        mIndicesBuffer = new IndicesBuffer(BufferTestUtil.createTriangleIndices(), GLES20.GL_STATIC_DRAW);

        /**
         * Create materials. No need for GL thread.
         */
        mRedMaterial = new Material();
        mRedMaterial.setShaderProgram(ProgramTestUtil.createNdcRed());
        ActionHelper.handleMainThreadActions(mRedMaterial);

        mBlueMaterial = new Material();
        mBlueMaterial.setShaderProgram(ProgramTestUtil.createNdcBlue());
        ActionHelper.handleMainThreadActions(mBlueMaterial);

        mLeftMesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mLeftMesh.addVertexBuffer(BufferTestUtil.createLeftTriangle());
        mLeftMesh.setIndicesBuffer(mIndicesBuffer);
        ActionHelper.handleMainThreadActions(mLeftMesh);

        mTopMesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mTopMesh.addVertexBuffer(BufferTestUtil.createTopTriangle());
        ActionHelper.handleMainThreadActions(mTopMesh);

        mRightMesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mRightMesh.addVertexBuffer(BufferTestUtil.createRightTriangle());
        mRightMesh.setIndicesBuffer(mIndicesBuffer);
        ActionHelper.handleMainThreadActions(mRightMesh);

        mBottomMesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mBottomMesh.addVertexBuffer(BufferTestUtil.createBottomTriangle());
        ActionHelper.handleMainThreadActions(mBottomMesh);

        setTestInfo("indices red, blue, red, blue material/mesh");

        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                mContext = getBackendContext();

                /**
                 * Create all materials.
                 */
                mRedMaterial.create(mContext);
                mBlueMaterial.create(mContext);

                /**
                 * Create all meshes
                 */
                mLeftMesh.create(mContext);
                mRightMesh.create(mContext);
                mTopMesh.create(mContext);
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

    public void testElementTargetTest_MatMesh_mainNotifier() throws Exception {
        runOnDrawFrame(new ResultRunnable() {
            @Override
            public Object run() {
                /**
                 * Clear screen.
                 */
                clearScreen(0.5f, 0.5f, 0.5f, 1.0f);

                /**
                 * Draw the triangles.
                 */
                DrawTestUtil.drawUsingMaterialAndMesh(mContext, mRedMaterial, mLeftMesh);
                DrawTestUtil.drawUsingMaterialAndMesh(mContext, mBlueMaterial, mTopMesh);
                DrawTestUtil.drawUsingMaterialAndMesh(mContext, mRedMaterial, mRightMesh);
                DrawTestUtil.drawUsingMaterialAndMesh(mContext, mBlueMaterial, mBottomMesh);

                return null;
            }
        });
        sleepOnDrawFrame(Verify.TERMINATE_TIME);
    }
}
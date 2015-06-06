package com.sweetlab.sweetride.context;

import android.opengl.GLES20;

import com.sweetlab.sweetride.attributedata.IndicesBuffer;
import com.sweetlab.sweetride.Util.BufferTestUtil;
import com.sweetlab.sweetride.Util.DrawTestUtil;
import com.sweetlab.sweetride.Util.ProgramTestUtil;
import com.sweetlab.sweetride.Util.Verify;
import com.sweetlab.sweetride.engine.FrontEndActionHandler;
import com.sweetlab.sweetride.material.Material;
import com.sweetlab.sweetride.mesh.Mesh;
import com.sweetlab.sweetride.testframework.OpenGLTestCase;
import com.sweetlab.sweetride.testframework.ResultRunnable;

public class ElementTargetTest_MatMesh_notifier extends OpenGLTestCase {
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

    /**
     * Front end action handler.
     */
    FrontEndActionHandler mActionHandler = new FrontEndActionHandler();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        /**
         * Create indices buffer.
         */
        mIndicesBuffer = new IndicesBuffer(BufferTestUtil.createTriangleIndices(), BufferUsage.STATIC);

        /**
         * Create materials. No need for GL thread.
         */
        mRedMaterial = new Material();
        mRedMaterial.setShaderProgram(ProgramTestUtil.createNdcRed());
        mActionHandler.handleActions(mRedMaterial);

        mBlueMaterial = new Material();
        mBlueMaterial.setShaderProgram(ProgramTestUtil.createNdcBlue());
        mActionHandler.handleActions(mBlueMaterial);

        mLeftMesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mLeftMesh.addVertexBuffer(BufferTestUtil.createLeftTriangle());
        mLeftMesh.setIndicesBuffer(mIndicesBuffer);
        mActionHandler.handleActions(mLeftMesh);

        mTopMesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mTopMesh.addVertexBuffer(BufferTestUtil.createTopTriangle());
        mActionHandler.handleActions(mTopMesh);

        mRightMesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mRightMesh.addVertexBuffer(BufferTestUtil.createRightTriangle());
        mRightMesh.setIndicesBuffer(mIndicesBuffer);
        mActionHandler.handleActions(mRightMesh);

        mBottomMesh = new Mesh(MeshDrawingMode.TRIANGLES);
        mBottomMesh.addVertexBuffer(BufferTestUtil.createBottomTriangle());
        mActionHandler.handleActions(mBottomMesh);

        setTestInfo("indices red, blue, red, blue material/mesh");

        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                mContext = getBackendContext();

                mContext.getActionHandler().handleActions(mRedMaterial);
                mContext.getActionHandler().handleActions(mBlueMaterial);
                mContext.getActionHandler().handleActions(mLeftMesh);
                mContext.getActionHandler().handleActions(mRightMesh);
                mContext.getActionHandler().handleActions(mTopMesh);
                mContext.getActionHandler().handleActions(mBottomMesh);
                assertFalse(mRedMaterial.hasActions());
                assertFalse(mBlueMaterial.hasActions());
                assertFalse(mLeftMesh.hasActions());
                assertFalse(mRightMesh.hasActions());
                assertFalse(mTopMesh.hasActions());
                assertFalse(mBottomMesh.hasActions());
                return null;
            }
        });
    }

    public void testElementTargetTest_MatMesh_notifier() throws Exception {
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
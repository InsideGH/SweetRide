package com.sweetlab.sweetride.context;

import com.sweetlab.sweetride.shader.FragmentShader;
import com.sweetlab.sweetride.shader.VertexShader;
import com.sweetlab.sweetride.testframework.OpenGLTestCase;
import com.sweetlab.sweetride.testframework.ResultRunnable;

/**
 * Test shader program linking.
 */
public class ProgramLinkerTest extends OpenGLTestCase {

    /**
     * Valid vertex shader.
     */
    private VertexShader mValidVertex;

    /**
     * Valid fragment shader.
     */
    private FragmentShader mValidFragment;

    /**
     * Invalid vertex shader.
     */
    private VertexShader mInValidVertex;

    /**
     * Invalid fragment shader.
     */
    private FragmentShader mInValidFragment;

    /**
     * Backend context.
     */
    private BackendContext mBackendContext;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mValidVertex = new VertexShader(ShaderCompilerTest.VALID_VERTEX_SOURCE);
        mValidFragment = new FragmentShader(ShaderCompilerTest.VALID_FRAGMENT_SOURCE);
        mInValidVertex = new VertexShader(ShaderCompilerTest.INVALID_VERTEX_SOURCE);
        mInValidFragment = new FragmentShader(ShaderCompilerTest.INVALID_FRAGMENT_SOURCE);

        runOnDrawFrame(new ResultRunnable() {
            @Override
            public Object run() {
                mBackendContext = getBackendContext();
                mValidVertex.create(mBackendContext);
                mValidFragment.create(mBackendContext);
                mInValidVertex.create(mBackendContext);
                mInValidFragment.create(mBackendContext);
                return null;
            }
        });
    }

    public void testLink() throws Exception {
        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                int link = mBackendContext.getLinker().link(mValidVertex, mValidFragment);
                assertTrue(link > ResourceManager.INVALID_PROGRAM_ID);
                return null;
            }
        });

        runOnDrawFrame(new ResultRunnable() {
            @Override
            public Object run() {
                int link = mBackendContext.getLinker().link(mValidVertex, mValidFragment);
                assertTrue(link > ResourceManager.INVALID_PROGRAM_ID);
                return null;
            }
        });

        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                int link = mBackendContext.getLinker().link(mInValidVertex, mValidFragment);
                assertTrue(link == ResourceManager.INVALID_PROGRAM_ID);
                return null;
            }
        });

        runOnDrawFrame(new ResultRunnable() {
            @Override
            public Object run() {
                int link = mBackendContext.getLinker().link(mInValidVertex, mValidFragment);
                assertTrue(link == ResourceManager.INVALID_PROGRAM_ID);
                return null;
            }
        });

        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                int link = mBackendContext.getLinker().link(mValidVertex, mInValidFragment);
                assertTrue(link == ResourceManager.INVALID_PROGRAM_ID);
                return null;
            }
        });

        runOnDrawFrame(new ResultRunnable() {
            @Override
            public Object run() {
                int link = mBackendContext.getLinker().link(mValidVertex, mInValidFragment);
                assertTrue(link == ResourceManager.INVALID_PROGRAM_ID);
                return null;
            }
        });

        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                int link = mBackendContext.getLinker().link(mInValidVertex, mInValidFragment);
                assertTrue(link == ResourceManager.INVALID_PROGRAM_ID);
                return null;
            }
        });

        runOnDrawFrame(new ResultRunnable() {
            @Override
            public Object run() {
                int link = mBackendContext.getLinker().link(mInValidVertex, mInValidFragment);
                assertTrue(link == ResourceManager.INVALID_PROGRAM_ID);
                return null;
            }
        });
    }
}
package com.sweetlab.sweetride.shader;

import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.context.ShaderCompiler;
import com.sweetlab.sweetride.context.ShaderCompilerTest;
import com.sweetlab.sweetride.testframework.OpenGLTestCase;
import com.sweetlab.sweetride.testframework.ResultRunnable;

public class VertexShaderTest extends OpenGLTestCase {
    /**
     * Valid shader 1.
     */
    private BaseShader mValid1;

    /**
     * Valid shader 2.
     */
    private BaseShader mValid2;

    /**
     * Invalid shader 3.
     */
    private BaseShader mInvalid3;

    /**
     * Backend context.
     */
    private BackendContext mContext;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                // Assures that backend context is not null.
                mContext = getBackendContext();
                return null;
            }
        });

        mValid1 = new VertexShader(ShaderCompilerTest.VALID_VERTEX_SOURCE);
        mValid2 = new VertexShader(ShaderCompilerTest.VALID_VERTEX_SOURCE);
        mInvalid3 = new VertexShader(ShaderCompilerTest.INVALID_VERTEX_SOURCE);
    }

    public void testRelease() throws Exception {
        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                mValid1.compile(mContext);
                mValid1.release(mContext);
                assertFalse(mValid1.isCompiled());
                assertEquals(ShaderCompiler.INVALID_ID, mValid1.getId());
                return null;
            }
        });

        assertFalse(mValid2.isCompiled());
        runOnDrawFrame(new ResultRunnable() {
            @Override
            public Object run() {
                mValid2.compile(mContext);
                mValid2.release(mContext);
                assertFalse(mValid2.isCompiled());
                assertEquals(ShaderCompiler.INVALID_ID, mValid2.getId());
                return null;
            }
        });
    }

    @SuppressWarnings({"EmptyMethod"})
    public void testCompile() throws Exception {
        /**
         * This is covered in the other tests.
         */
    }

    public void testIsCompiled() throws Exception {
        assertFalse(mValid1.isCompiled());
        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                mValid1.compile(mContext);
                assertTrue(mValid1.isCompiled());
                return null;
            }
        });

        assertFalse(mValid2.isCompiled());
        runOnDrawFrame(new ResultRunnable() {
            @Override
            public Object run() {
                mValid2.compile(mContext);
                assertTrue(mValid2.isCompiled());
                return null;
            }
        });

        assertFalse(mInvalid3.isCompiled());
        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                mInvalid3.compile(mContext);
                assertFalse(mInvalid3.isCompiled());
                return null;
            }
        });
    }

    public void testGetId() throws Exception {
        assertEquals(ShaderCompiler.INVALID_ID, mValid1.getId());
        int id1 = (int) runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                mValid1.compile(mContext);
                int id = mValid1.getId();
                assertNotSame(ShaderCompiler.INVALID_ID, id);
                return id;
            }
        });

        assertFalse(mValid2.isCompiled());
        int id2 = (int) runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                mValid2.compile(mContext);
                int id = mValid2.getId();
                assertNotSame(ShaderCompiler.INVALID_ID, id);
                return id;
            }
        });

        assertNotSame(id1, id2);
    }
}
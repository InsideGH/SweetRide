package com.sweetlab.sweetride.context;

import com.sweetlab.sweetride.shader.BaseShader;
import com.sweetlab.sweetride.shader.FragmentShader;
import com.sweetlab.sweetride.testframework.OpenGLTestCase;
import com.sweetlab.sweetride.testframework.ResultRunnable;

public class FragmentShaderTest extends OpenGLTestCase {
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
        mValid1 = new FragmentShader(ShaderCompilerTest.VALID_FRAGMENT_SOURCE);
        mValid2 = new FragmentShader(ShaderCompilerTest.VALID_FRAGMENT_SOURCE);
        mInvalid3 = new FragmentShader(ShaderCompilerTest.INVALID_FRAGMENT_SOURCE);
    }

    public void testRelease() throws Exception {
        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                mValid1.create(mContext);
                mValid1.delete(mContext);
                assertFalse(mValid1.isCreated());
                assertEquals(ResourceManager.INVALID_SHADER_ID, mValid1.getId());
                return null;
            }
        });

        assertFalse(mValid2.isCreated());
        runOnDrawFrame(new ResultRunnable() {
            @Override
            public Object run() {
                mValid2.create(mContext);
                mValid2.delete(mContext);
                assertFalse(mValid2.isCreated());
                assertEquals(ResourceManager.INVALID_SHADER_ID, mValid2.getId());
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
        assertFalse(mValid1.isCreated());
        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                mValid1.create(mContext);
                assertTrue(mValid1.isCreated());
                return null;
            }
        });

        assertFalse(mValid2.isCreated());
        runOnDrawFrame(new ResultRunnable() {
            @Override
            public Object run() {
                mValid2.create(mContext);
                assertTrue(mValid2.isCreated());
                return null;
            }
        });

        assertFalse(mInvalid3.isCreated());
        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                mInvalid3.create(mContext);
                assertFalse(mInvalid3.isCreated());
                return null;
            }
        });
    }

    public void testGetId() throws Exception {
        assertEquals(ResourceManager.INVALID_SHADER_ID, mValid1.getId());
        int id1 = (int) runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                mValid1.create(mContext);
                int id = mValid1.getId();
                assertNotSame(ResourceManager.INVALID_SHADER_ID, id);
                return id;
            }
        });

        assertFalse(mValid2.isCreated());
        int id2 = (int) runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                mValid2.create(mContext);
                int id = mValid2.getId();
                assertNotSame(ResourceManager.INVALID_SHADER_ID, id);
                return id;
            }
        });

        assertNotSame(id1, id2);
    }
}
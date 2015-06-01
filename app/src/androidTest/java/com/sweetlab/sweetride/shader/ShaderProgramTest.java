package com.sweetlab.sweetride.shader;

import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.context.ResourceManager;
import com.sweetlab.sweetride.context.ShaderCompilerTest;
import com.sweetlab.sweetride.testframework.OpenGLTestCase;
import com.sweetlab.sweetride.testframework.ResultRunnable;

/**
 * Test shader program.
 */
public class ShaderProgramTest extends OpenGLTestCase {
    /**
     * Valid vertex shader.
     */
    private VertexShader mValidVert;

    /**
     * Valid fragment shader.
     */
    private FragmentShader mValidFrag;

    /**
     * Invalid vertex shader.
     */
    private VertexShader mInvalidVert;

    /**
     * Invalid fragment shader.
     */
    private FragmentShader mInvalidFrag;

    /**
     * Valid shader program.
     */
    private ShaderProgram mValidProg;

    /**
     * Invalid shader program 1.
     */
    private ShaderProgram mInvalidProg1;

    /**
     * Invalid shader program 2.
     */
    private ShaderProgram mInvalidProg2;

    /**
     * Invalid shader program 3.
     */
    private ShaderProgram mInvalidProg3;

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

        mValidVert = new VertexShader(ShaderCompilerTest.VALID_VERTEX_SOURCE);
        mValidFrag = new FragmentShader(ShaderCompilerTest.VALID_FRAGMENT_SOURCE);
        mInvalidVert = new VertexShader(ShaderCompilerTest.INVALID_VERTEX_SOURCE);
        mInvalidFrag = new FragmentShader(ShaderCompilerTest.INVALID_FRAGMENT_SOURCE);

        mValidProg = new ShaderProgram(mValidVert, mValidFrag);
        mInvalidProg1 = new ShaderProgram(mValidVert, mInvalidFrag);
        mInvalidProg2 = new ShaderProgram(mInvalidVert, mValidFrag);
        mInvalidProg3 = new ShaderProgram(mInvalidVert, mInvalidFrag);

        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                mValidVert.create(mContext);
                mValidFrag.create(mContext);
                mInvalidVert.create(mContext);
                mInvalidFrag.create(mContext);
                return null;
            }
        });
    }

    public void testLink() throws Exception {
        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                mValidProg.create(mContext);
                assertTrue(mValidProg.isCreated());
                assertTrue(mValidProg.getId() > ResourceManager.INVALID_PROGRAM_ID);
                return null;
            }
        });
        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                mInvalidProg1.create(mContext);
                assertFalse(mInvalidProg1.isCreated());
                assertTrue(mInvalidProg1.getId() == ResourceManager.INVALID_PROGRAM_ID);
                return null;
            }
        });
        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                mInvalidProg2.create(mContext);
                assertFalse(mInvalidProg2.isCreated());
                assertTrue(mInvalidProg2.getId() == ResourceManager.INVALID_PROGRAM_ID);
                return null;
            }
        });
        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                mInvalidProg3.create(mContext);
                assertFalse(mInvalidProg3.isCreated());
                assertTrue(mInvalidProg3.getId() == ResourceManager.INVALID_PROGRAM_ID);
                return null;
            }
        });
    }

    @SuppressWarnings({"EmptyMethod"})
    public void testIsLinked() throws Exception {
        /**
         * This is tested in other tests.
         */
    }

    public void testGetAttribute() throws Exception {
        runOnDrawFrame(new ResultRunnable() {
            @Override
            public Object run() {
                mValidProg.create(mContext);
                int attributeCount = mValidProg.getAttributeCount();
                assertEquals(3, attributeCount);

                Attribute a_pos = mValidProg.getAttribute("a_Pos");
                Attribute a_color = mValidProg.getAttribute("a_Color");
                Attribute a_texCoord = mValidProg.getAttribute("a_TexCoord");

                assertTrue(a_pos.getLocation() != -1);
                assertTrue(a_color.getLocation() != -1);
                assertTrue(a_texCoord.getLocation() != -1);

                return null;
            }
        });
    }

    public void testGetUniform() throws Exception {
        runOnDrawFrame(new ResultRunnable() {
            @Override
            public Object run() {
                mValidProg.create(mContext);
                int uniformCount = mValidProg.getUniformCount();
                assertEquals(4, uniformCount);

                Uniform u_worldViewProjMat = mValidProg.getUniform("u_WorldViewProjMat");
                Uniform s_texture0 = mValidProg.getUniform("s_Texture0");
                Uniform u_amountRed = mValidProg.getUniform("u_AmountRed");
                Uniform u_amountRedInt = mValidProg.getUniform("u_AmountRedInt");

                assertTrue(u_worldViewProjMat.getLocation() != -1);
                assertTrue(s_texture0.getLocation() != -1);
                assertTrue(u_amountRed.getLocation() != -1);
                assertTrue(u_amountRedInt.getLocation() != -1);

                return null;
            }
        });
    }

    public void testRelease() throws Exception {
        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                mValidProg.create(mContext);
                mValidProg.delete(mContext);
                assertFalse(mValidProg.isCreated());
                assertTrue(mValidProg.getId() == ResourceManager.INVALID_PROGRAM_ID);
                return null;
            }
        });
    }
}
package com.sweetlab.sweetride.context;

import android.opengl.GLES20;

import com.sweetlab.sweetride.shader.FragmentShader;
import com.sweetlab.sweetride.shader.ShaderProgram;
import com.sweetlab.sweetride.shader.ProgramUniform;
import com.sweetlab.sweetride.shader.VertexShader;
import com.sweetlab.sweetride.testframework.OpenGLTestCase;
import com.sweetlab.sweetride.testframework.ResultRunnable;

import java.util.Map;

public class ProgramUniformExtractorTest extends OpenGLTestCase {
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
    }

    /**
     * Test extract uniforms from a shader program.
     *
     * @throws Exception
     */
    public void testUniformExtractorTest() throws Exception {
        final VertexShader vertexShader = new VertexShader(ShaderCompilerTest.VALID_VERTEX_SOURCE);
        final FragmentShader fragmentShader = new FragmentShader(ShaderCompilerTest.VALID_FRAGMENT_SOURCE);
        final ShaderProgram program = new ShaderProgram(vertexShader, fragmentShader);

        runOnDrawFrame(new ResultRunnable() {
            @Override
            public Object run() {
                vertexShader.create(mContext);
                fragmentShader.create(mContext);
                program.create(mContext);
                Map<String, ProgramUniform> map = mContext.getUniformExtractor().extract(program);

                assertEquals(4, map.size());

                ProgramUniform u_worldViewProjMat = map.get("u_WorldViewProjMat");
                assertNotNull(u_worldViewProjMat);
                assertEquals(u_worldViewProjMat.getType(), GLES20.GL_FLOAT_MAT4);

                ProgramUniform s_texture0 = map.get("s_Texture0");
                assertNotNull(s_texture0);
                assertEquals(s_texture0.getType(), GLES20.GL_SAMPLER_2D);

                ProgramUniform u_amountRed = map.get("u_AmountRed");
                assertNotNull(u_amountRed);
                assertEquals(u_amountRed.getType(), GLES20.GL_FLOAT);

                ProgramUniform u_amountRedInt = map.get("u_AmountRedInt");
                assertNotNull(u_amountRedInt);
                assertEquals(u_amountRedInt.getType(), GLES20.GL_INT);

                return null;
            }
        });
    }
}
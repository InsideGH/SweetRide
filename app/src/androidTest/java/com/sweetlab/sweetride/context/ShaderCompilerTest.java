package com.sweetlab.sweetride.context;

import com.sweetlab.sweetride.testframework.OpenGLTestCase;
import com.sweetlab.sweetride.testframework.ResultRunnable;

public class ShaderCompilerTest extends OpenGLTestCase {

    public static final String VALID_VERTEX_SOURCE =
            "attribute vec4 a_Pos; \n" +
                    "attribute vec4 a_Color; " +
                    "attribute vec2 a_TexCoord; " +
                    "uniform mat4 u_WorldViewProjMat; " +
                    "varying vec4 v_Color; " +
                    "varying vec2 v_TexCoord; " +
                    "" +
                    "void main() { " +
                    "    v_TexCoord = a_TexCoord; " +
                    "    v_Color = a_Color; " +
                    "    gl_Position = u_WorldViewProjMat * a_Pos; " +
                    "} ";

    public static final String VALID_FRAGMENT_SOURCE =
            "precision mediump float;\n" +
                    "uniform sampler2D s_Texture0;\n" +
                    "uniform float u_AmountRed;\n" +
                    "uniform int u_AmountRedInt;\n" +
                    "varying vec4 v_Color;\n" +
                    "varying vec2 v_TexCoord;\n" +
                    "\n" +
                    "void main() {\n" +
                    "\tgl_FragColor = v_Color + vec4(u_AmountRed, u_AmountRed, u_AmountRed, u_AmountRed);\n" +
                    "\tgl_FragColor += texture2D(s_Texture0, v_TexCoord);\n" +
                    "\tgl_FragColor.a *= float(u_AmountRedInt);\n" +
                    "}";

    public static final String INVALID_VERTEX_SOURCE =
            "atribute vec4 a_Pos; \n" +
                    "attribute vec4 a_Color; " +
                    "attribute vec2 a_TexCoord; " +
                    "uniform mat4 u_WorldViewProjMat; " +
                    "varying vec4 v_Color; " +
                    "varying vec2 v_TexCoord; " +
                    "" +
                    "void main() { " +
                    "    v_TexCoord = a_TexCoord; " +
                    "    v_Color = a_Color; " +
                    "    gl_Position = u_WorldViewProjMat * a_Pos; " +
                    "} ";

    public static final String INVALID_FRAGMENT_SOURCE =
            "preision mediump float;\n" +
                    "uniform sampler2D s_Texture0;\n" +
                    "uniform float u_AmountRed;\n" +
                    "varying vec4 v_Color;\n" +
                    "varying vec2 v_TexCoord;\n" +
                    "\n" +
                    "void main() {\n" +
                    "\tgl_FragColor = v_Color + vec4(u_AmountRed, u_AmountRed, u_AmountRed, u_AmountRed);\n" +
                    "\tgl_FragColor += texture2D(s_Texture0, v_TexCoord);\n" +
                    "\tgl_FragColor.a *= u_AmountRed;\n" +
                    "}";

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

    public void testCompile() throws Exception {
        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                int i = mContext.getCompiler().compileVertexShader(VALID_VERTEX_SOURCE);
                assertTrue(i > ResourceManager.INVALID_SHADER_ID);
                return null;
            }
        });

        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                int i = mContext.getCompiler().compileFragmentShader(VALID_FRAGMENT_SOURCE);
                assertTrue(i > ResourceManager.INVALID_SHADER_ID);
                return null;
            }
        });

        runOnDrawFrame(new ResultRunnable() {
            @Override
            public Object run() {
                int i = mContext.getCompiler().compileVertexShader(VALID_VERTEX_SOURCE);
                assertTrue(i > ResourceManager.INVALID_SHADER_ID);
                return null;
            }
        });

        runOnDrawFrame(new ResultRunnable() {
            @Override
            public Object run() {
                int i = mContext.getCompiler().compileFragmentShader(VALID_FRAGMENT_SOURCE);
                assertTrue(i > ResourceManager.INVALID_SHADER_ID);
                return null;
            }
        });

        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                int i = mContext.getCompiler().compileVertexShader(INVALID_VERTEX_SOURCE);
                assertTrue(i == ResourceManager.INVALID_SHADER_ID);
                return null;
            }
        });

        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                int i = mContext.getCompiler().compileFragmentShader(INVALID_FRAGMENT_SOURCE);
                assertTrue(i == ResourceManager.INVALID_SHADER_ID);
                return null;
            }
        });
    }
}
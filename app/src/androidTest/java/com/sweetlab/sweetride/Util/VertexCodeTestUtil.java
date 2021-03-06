package com.sweetlab.sweetride.Util;

public class VertexCodeTestUtil {
    /**
     * Vertex code. Plain NDC.
     * attribute vec4 a_Pos
     */
    public static final String VERTEX_NDC =
            "attribute vec4 a_Pos; \n" +
                    "void main() { " +
                    "    gl_Position = a_Pos;" +
                    "} ";

    /**
     * Vertex code. Plain NDC. Color attribute.
     */
    public static final String VERTEX_NDC_COLOR =
            "attribute vec4 a_Pos; \n" +
                    "attribute vec4 a_Color; \n" +
                    "varying vec4 v_Color; \n" +
                    "void main() { " +
                    "    v_Color = a_Color;" +
                    "    gl_Position = a_Pos;" +
                    "} ";

    /**
     * Vertex code. Plain NDC. Single texture coord.
     */
    public static final String VERTEX_NDC_ONE_TEXCOORD =
            "attribute vec4 a_Pos; \n" +
                    "attribute vec2 a_texCoord;\n" +
                    "varying vec2 v_texCoord;\n" +
                    "void main() { " +
                    "    v_texCoord = a_texCoord;\n" +
                    "    gl_Position = a_Pos; " +
                    "} ";

    /**
     * Vertex code. Camera. Single texture coord.
     */
    public static final String VERTEX_CAMERA_ONE_TEXCOORD =
            "attribute vec4 a_Pos; \n" +
                    "attribute vec2 a_texCoord;\n" +
                    "uniform mat4 u_Cam; \n" +
                    "varying vec2 v_texCoord;\n" +
                    "void main() { " +
                    "    v_texCoord = a_texCoord;\n" +
                    "    gl_Position = u_Cam * a_Pos; " +
                    "} ";

    /**
     * Vertex code. Engine supported uniform. Single texture coord.
     */
    public static final String VERTEX_ENGINE_ONE_TEXCOORD =
            "attribute vec4 a_Pos; \n" +
                    "attribute vec2 a_texCoord;\n" +
                    "uniform mat4 u_worldViewProjMat; \n" +
                    "varying vec2 v_texCoord;\n" +
                    "void main() { " +
                    "    v_texCoord = a_texCoord;\n" +
                    "    gl_Position = u_worldViewProjMat * a_Pos; " +
                    "} ";
}

package com.sweetlab.sweetride.context.Util;

public class FragCodeTestUtil {
    /**
     * Fragment code. Constant red color.
     */
    public static final String FRAGMENT_RED =
            "precision mediump float;\n" +
                    "void main() {\n" +
                    "\tgl_FragColor = vec4(1.0, 0.0, 0.0, 1.0);\n" +
                    "}";

    /**
     * Fragment code. Constant blue color.
     */
    public static final String FRAGMENT_BLUE =
            "precision mediump float;\n" +
                    "void main() {\n" +
                    "\tgl_FragColor = vec4(0.0, 0.0, 1.0, 1.0);\n" +
                    "}";

    /**
     * Fragment code, Color varying.
     */
    public static final String FRAGMENT_COLOR =
            "precision mediump float;\n" +
                    "varying vec4 v_Color; \n" +
                    "void main() {\n" +
                    "\tgl_FragColor = v_Color;\n" +
                    "}";

    /**
     * Fragment code. Single texture.
     */
    public static final String FRAGMENT_ONE_TEX =
            "precision mediump float;\n" +
                    "varying vec2 v_texCoord;\n" +
                    "uniform sampler2D s_texture;\n" +
                    "void main() {\n" +
                    "vec4 color = texture2D(s_texture, v_texCoord);\n" +
                    "gl_FragColor = color;\n" +
                    "}";

    /**
     * Fragment code. Two textures.
     */
    public static final String FRAGMENT_TWO_TEX =
            "precision mediump float;\n" +
                    "varying vec2 v_texCoord;\n" +
                    "uniform sampler2D s_texture;\n" +
                    "uniform sampler2D s_textureChess;\n" +
                    "void main() {\n" +
                    "vec4 color = texture2D(s_texture, v_texCoord);\n" +
                    "vec4 offset = texture2D(s_textureChess, v_texCoord);\n" +
                    "gl_FragColor = color + offset;\n" +
                    "}";
}

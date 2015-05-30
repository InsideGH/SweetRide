package com.sweetlab.sweetride.context.Util;

import com.sweetlab.sweetride.shader.FragmentShader;
import com.sweetlab.sweetride.shader.ShaderProgram;
import com.sweetlab.sweetride.shader.VertexShader;

public class ProgramTestUtil {
    /**
     * Create a NDC shader program that colors red.
     *
     * @return The shader program.
     */
    public static ShaderProgram createNdcRed() {
        return new ShaderProgram(new VertexShader(VertexCodeTestUtil.VERTEX_NDC), new FragmentShader(FragCodeTestUtil.FRAGMENT_RED));
    }

    /**
     * Create a NDC shader program that colors blue.
     *
     * @return The shader program.
     */
    public static ShaderProgram createNdcBlue() {
        return new ShaderProgram(new VertexShader(VertexCodeTestUtil.VERTEX_NDC), new FragmentShader(FragCodeTestUtil.FRAGMENT_BLUE));
    }

    /**
     * Create a NDC shader program that uses color attributes.
     *
     * @return The shader program.
     */
    public static ShaderProgram createNdcColor() {
        return new ShaderProgram(new VertexShader(VertexCodeTestUtil.VERTEX_NDC_COLOR), new FragmentShader(FragCodeTestUtil.FRAGMENT_COLOR));
    }

    /**
     * Create a NDC shader program that uses one texture.
     *
     * @return The shader program.
     */
    public static ShaderProgram createNdcOneTexCoordOneTexture() {
        return new ShaderProgram(new VertexShader(VertexCodeTestUtil.VERTEX_NDC_ONE_TEXCOORD), new FragmentShader(FragCodeTestUtil.FRAGMENT_ONE_TEX));
    }

    public static ShaderProgram createNdcOneTexCoordTwoTextures() {
        return new ShaderProgram(new VertexShader(VertexCodeTestUtil.VERTEX_NDC_ONE_TEXCOORD), new FragmentShader(FragCodeTestUtil.FRAGMENT_TWO_TEX));
    }
}

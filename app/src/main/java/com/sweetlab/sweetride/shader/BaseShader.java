package com.sweetlab.sweetride.shader;

import com.sweetlab.sweetride.resource.Resource;

/**
 * Base shader class.
 */
public abstract class BaseShader implements Resource {
    /**
     * Shader source code.
     */
    protected final String mSource;

    /**
     * Constructor.
     *
     * @param source The shader source.
     */
    public BaseShader(String source) {
        mSource = source;
    }

    /**
     * Get the shader source code.
     *
     * @return The source.
     */
    public String getSource() {
        return mSource;
    }
}

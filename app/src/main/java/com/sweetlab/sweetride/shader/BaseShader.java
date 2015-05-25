package com.sweetlab.sweetride.shader;

import com.sweetlab.sweetride.context.BackendContext;
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

    @Override
    public abstract void release(BackendContext context);

    /**
     * Get the shader source code.
     *
     * @return The source.
     */
    public String getSource() {
        return mSource;
    }

    /**
     * Compile shader.
     *
     * @param context Context.
     */
    public abstract void compile(BackendContext context);

    /**
     * Check if compiled.
     *
     * @return True if compiled.
     */
    public abstract boolean isCompiled();

    /**
     * Get the compiled id.
     *
     * @return The compiled id.
     */
    public abstract int getId();
}

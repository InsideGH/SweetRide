package com.sweetlab.sweetride.shader;

import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.context.ShaderCompiler;

/**
 * Vertex shader.
 */
public class VertexShader extends BaseShader {
    /**
     * The compiled id.
     */
    private int mId;

    /**
     * Constructor.
     *
     * @param source Vertex source code.
     */
    public VertexShader(String source) {
        super(source);
        mId = ShaderCompiler.INVALID_ID;
    }

    @Override
    public void release(BackendContext context) {
        context.getResourceRelease().releaseShader(this);
        mId = ShaderCompiler.INVALID_ID;
    }

    @Override
    public void compile(BackendContext context) {
        mId = context.getCompiler().compileVertexShader(mSource);
    }

    @Override
    public boolean isCompiled() {
        return mId != ShaderCompiler.INVALID_ID;
    }

    @Override
    public int getId() {
        return mId;
    }
}

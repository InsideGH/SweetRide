package com.sweetlab.sweetride.shader;

import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.context.ShaderCompiler;

/**
 * Fragment shader.
 */
public class FragmentShader extends BaseShader {
    /**
     * The compiled id.
     */
    private int mId;

    /**
     * Constructor.
     *
     * @param source Fragment source code.
     */
    public FragmentShader(String source) {
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
        mId = context.getCompiler().compileFragmentShader(mSource);
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

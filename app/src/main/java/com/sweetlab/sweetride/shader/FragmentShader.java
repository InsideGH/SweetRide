package com.sweetlab.sweetride.shader;

import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.context.ResourceManager;

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
        mId = ResourceManager.INVALID_SHADER_ID;
    }

    @Override
    public void release(BackendContext context) {
        context.getResourceManager().deleteShader(mId);
        mId = ResourceManager.INVALID_SHADER_ID;
    }

    @Override
    public void create(BackendContext context) {
        mId = context.getCompiler().compileFragmentShader(mSource);
    }

    @Override
    public boolean isCreated() {
        return mId != ResourceManager.INVALID_SHADER_ID;
    }

    @Override
    public int getId() {
        return mId;
    }
}

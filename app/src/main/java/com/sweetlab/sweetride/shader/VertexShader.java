package com.sweetlab.sweetride.shader;

import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.context.ResourceManager;

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
        mId = ResourceManager.INVALID_SHADER_ID;
    }

    @Override
    public void delete(BackendContext context) {
        context.getResourceManager().deleteShader(mId);
        mId = ResourceManager.INVALID_SHADER_ID;
    }

    @Override
    public void create(BackendContext context) {
        mId = context.getCompiler().compileVertexShader(mSource);
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

package com.sweetlab.sweetride.shader;

import com.sweetlab.sweetride.action.Action;
import com.sweetlab.sweetride.action.GlobalActionId;
import com.sweetlab.sweetride.action.ActionThread;
import com.sweetlab.sweetride.action.NoHandleNotifier;
import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.context.ResourceManager;
import com.sweetlab.sweetride.resource.Resource;

import java.util.Map;

/**
 * Shader program.
 */
public class ShaderProgram extends NoHandleNotifier<GlobalActionId> implements Resource {
    /**
     * The vertex shader.
     */
    private final VertexShader mVertexShader;

    /**
     * The fragment shader.
     */
    private final FragmentShader mFragmentShader;

    /**
     * The attributes.
     */
    private Map<String, Attribute> mAttributes;

    /**
     * The uniforms.
     */
    private Map<String, ProgramUniform> mUniforms;

    /**
     * The linked id.
     */
    private int mId = ResourceManager.INVALID_PROGRAM_ID;

    /**
     * Constructor.
     *
     * @param vertexShader   Vertex shader.
     * @param fragmentShader Fragment shader.
     */
    public ShaderProgram(VertexShader vertexShader,
                         FragmentShader fragmentShader) {
        mVertexShader = vertexShader;
        mFragmentShader = fragmentShader;
        connectNotifier(mVertexShader);
        connectNotifier(mFragmentShader);
        addAction(new Action<>(this, GlobalActionId.PROGRAM_CREATE, ActionThread.GL));
    }

    @Override
    public void create(BackendContext context) {
        mId = context.getLinker().link(mVertexShader, mFragmentShader);
        if (mId != ResourceManager.INVALID_PROGRAM_ID) {
            mAttributes = context.getAttributeExtractor().extract(this);
            mUniforms = context.getUniformExtractor().extract(this);
        }
    }

    @Override
    public void delete(BackendContext context) {
        if (mVertexShader != null) {
            mVertexShader.delete(context);
        }
        if (mFragmentShader != null) {
            mFragmentShader.delete(context);
        }
        context.getResourceManager().deleteProgram(mId);
        mId = ResourceManager.INVALID_PROGRAM_ID;
    }

    @Override
    public boolean isCreated() {
        return mId != ResourceManager.INVALID_PROGRAM_ID;
    }

    @Override
    public int getId() {
        return mId;
    }

    @Override
    public boolean handleAction(BackendContext context, Action action) {
        if (action.getType().equals(GlobalActionId.PROGRAM_CREATE)) {
            create(context);
            return true;
        }
        return false;
    }

    /**
     * Get vertex shader.
     *
     * @return The vertex shader.
     */
    public VertexShader getVertexShader() {
        return mVertexShader;
    }

    /**
     * Get fragment shader.
     *
     * @return The fragment shader.
     */
    public FragmentShader getFragmentShader() {
        return mFragmentShader;
    }


    /**
     * Get number of attributes.
     *
     * @return Number of attributes.
     */
    public int getAttributeCount() {
        return mAttributes.size();
    }

    /**
     * Get number of uniforms.
     *
     * @return Number of uniforms.
     */
    public int getUniformCount() {
        return mUniforms.size();
    }

    /**
     * Get uniform.
     *
     * @param name Name of uniform.
     * @return The uniform.
     */
    public ProgramUniform getUniform(String name) {
        return mUniforms.get(name);
    }

    /**
     * Get attribute.
     *
     * @param name Name of attribute.
     * @return The attribute.
     */
    public Attribute getAttribute(String name) {
        return mAttributes.get(name);
    }
}

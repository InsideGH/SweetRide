package com.sweetlab.sweetride.shader;

import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.context.ProgramLinker;
import com.sweetlab.sweetride.resource.Resource;

import java.util.Map;

/**
 * Shader program.
 */
public class ShaderProgram implements Resource {
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
    private Map<String, Uniform> mUniforms;

    /**
     * The linked id.
     */
    private int mId = ProgramLinker.INVALID_ID;

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
    }

    @Override
    public void release(BackendContext context) {
        context.getResourceRelease().releaseProgram(this);
        mId = ProgramLinker.INVALID_ID;
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
     * Link this shader.
     *
     * @param context Backend context.
     */
    public void link(BackendContext context) {
        mId = context.getLinker().link(mVertexShader, mFragmentShader);
        if (mId != ProgramLinker.INVALID_ID) {
            mAttributes = context.getAttributeExtractor().extract(this);
            mUniforms = context.getUniformExtractor().extract(this);
        }
    }

    /**
     * Check if linked.
     *
     * @return True if linked.
     */
    public boolean isLinked() {
        return mId != ProgramLinker.INVALID_ID;
    }

    /**
     * Get the id of linked shader program.
     *
     * @return The linked id.
     */
    public int getId() {
        return mId;
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
    public Uniform getUniform(String name) {
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

package com.sweetlab.sweetride.context;

import android.opengl.GLES20;

/**
 * Backend context. Must be created on GL thread.
 */
public class BackendContext {
    private ShaderCompiler mShaderCompiler;
    private ProgramLinker mProgramLinker;
    private UniformWriter mUniformWriter;
    private AttributeExtractor mAttributeExtractor;
    private UniformExtractor mUniformExtractor;
    private GLES20State mGLES20State;
    private ArrayTarget mArrayTarget;
    private ResourceManager mResourceManager;
    private ElementTarget mElementTarget;
    private TextureUnitManager mTextureUnitManager;

    /**
     * Max number of texture units.
     */
    private int mMaxNumberTextureUnits;

    /**
     * Constructor. Must be created on GL thread.
     */
    public BackendContext() {
        int[] buf = new int[1];
        GLES20.glGetIntegerv(GLES20.GL_MAX_TEXTURE_IMAGE_UNITS, buf, 0);
        mMaxNumberTextureUnits = buf[0];
    }

    /**
     * Get the shader compiler.
     *
     * @return The compiler.
     */
    public ShaderCompiler getCompiler() {
        if (mShaderCompiler == null) {
            mShaderCompiler = new ShaderCompiler(this);
        }
        return mShaderCompiler;
    }

    /**
     * Get the program linker.
     *
     * @return The linker.
     */
    public ProgramLinker getLinker() {
        if (mProgramLinker == null) {
            mProgramLinker = new ProgramLinker(this);
        }
        return mProgramLinker;
    }

    /**
     * Get shader program attribute extractor.
     *
     * @return The extractor.
     */
    public AttributeExtractor getAttributeExtractor() {
        if (mAttributeExtractor == null) {
            mAttributeExtractor = new AttributeExtractor(this);
        }
        return mAttributeExtractor;
    }

    /**
     * Get shader program uniform extractor.
     *
     * @return The extractor.
     */
    public UniformExtractor getUniformExtractor() {
        if (mUniformExtractor == null) {
            mUniformExtractor = new UniformExtractor(this);
        }
        return mUniformExtractor;
    }

    /**
     * Get uniform writer.
     *
     * @return The uniform writer.
     */
    public UniformWriter getUniformWriter() {
        if (mUniformWriter == null) {
            mUniformWriter = new UniformWriter(this);
        }
        return mUniformWriter;
    }

    /**
     * Get the state.
     *
     * @return The state.
     */
    public GLES20State getState() {
        if (mGLES20State == null) {
            mGLES20State = new GLES20State(this);
        }
        return mGLES20State;
    }

    /**
     * Get the array target.
     *
     * @return The array target.
     */
    public ArrayTarget getArrayTarget() {
        if (mArrayTarget == null) {
            mArrayTarget = new ArrayTarget(this);
        }
        return mArrayTarget;
    }

    /**
     * Get the buffer manager.
     *
     * @return The buffer manager.
     */
    public ResourceManager getResourceManager() {
        if (mResourceManager == null) {
            mResourceManager = new ResourceManager(this);
        }
        return mResourceManager;
    }

    /**
     * Get the element target.
     *
     * @return The element target.
     */
    public ElementTarget getElementTarget() {
        if (mElementTarget == null) {
            mElementTarget = new ElementTarget(this);
        }
        return mElementTarget;
    }

    /**
     * Get the texture unit manager.
     *
     * @return The texture unit manager.
     */
    public TextureUnitManager getTextureUnitManager() {
        if (mTextureUnitManager == null) {
            mTextureUnitManager = new TextureUnitManager(this, mMaxNumberTextureUnits);
        }
        return mTextureUnitManager;
    }
}

package com.sweetlab.sweetride.context;

/**
 * Backend context. Must be created with GL context available.
 */
public class BackendContext {
    /**
     * GL HW capabilities
     */
    private final Capabilities mCapabilities;

    /**
     * Shader compiler.
     */
    private ShaderCompiler mShaderCompiler;

    /**
     * Program linker
     */
    private ProgramLinker mProgramLinker;

    /**
     * Uniform writer.
     */
    private UniformWriter mUniformWriter;

    /**
     * Attribute extractor.
     */
    private AttributeExtractor mAttributeExtractor;

    /**
     * Uniform extractor.
     */
    private UniformExtractor mUniformExtractor;

    /**
     * GL program state.
     */
    private GLES20State mGLES20State;

    /**
     * Array target.
     */
    private ArrayTarget mArrayTarget;

    /**
     * Resource manager.
     */
    private ResourceManager mResourceManager;

    /**
     * Element target.
     */
    private ElementTarget mElementTarget;

    /**
     * Texture unit manager.
     */
    private TextureUnitManager mTextureUnitManager;

    /**
     * Render buffer target.
     */
    private RenderBufferTarget mRenderBufferTarget;

    /**
     * Frame buffer target.
     */
    private FrameBufferTarget mFrameBufferTarget;

    /**
     * The backend action handler.
     */
    private BackendActionHandler mBackendActionHandler;

    /**
     * Max number of texture units.
     */
    private int mMaxNumberTextureUnits;

    /**
     * Constructor. Must be created with GL context available.
     */
    public BackendContext() {
        mCapabilities = new Capabilities();
        mMaxNumberTextureUnits = mCapabilities.getMaxNumberTextureUnits();
    }

    /**
     * Get GL capabilities.
     *
     * @return The capabilities.
     */
    public Capabilities getCapabilities() {
        return mCapabilities;
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
     * Get the program state.
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

    /**
     * Get the render buffer target.
     *
     * @return The render buffer target.
     */
    public RenderBufferTarget getRenderBufferTarget() {
        if (mRenderBufferTarget == null) {
            mRenderBufferTarget = new RenderBufferTarget(this);
        }
        return mRenderBufferTarget;
    }

    /**
     * Get the render buffer target.
     *
     * @return The render buffer target.
     */
    public FrameBufferTarget getFrameBufferTarget() {
        if (mFrameBufferTarget == null) {
            mFrameBufferTarget = new FrameBufferTarget(this);
        }
        return mFrameBufferTarget;
    }

    /**
     * Get the backend action handler.
     *
     * @return The backend action handler.
     */
    public BackendActionHandler getActionHandler() {
        if (mBackendActionHandler == null) {
            mBackendActionHandler = new BackendActionHandler(this);
        }
        return mBackendActionHandler;
    }
}

package com.sweetlab.sweetride.context;

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
     * Create a instance of context.
     *
     * @return The backend context.
     */
    public static BackendContext createInstance() {
        BackendContext context = new BackendContext();
        context.init();
        return context;
    }

    /**
     * Initialize all sub modules.
     */
    private void init() {
        /**
         * Warning, do not use the backend context in any of the resource constructors below. This
         * restriction is important because not all resources are initialized at that point.
         *
         * Keeping this only restriction makes it safe because the backend context is not exposed
         * until the whole initialization is performed.
         *
         * Order below is arbitrary.
         */
        mShaderCompiler = new ShaderCompiler(this);
        mProgramLinker = new ProgramLinker(this);
        mUniformWriter = new UniformWriter(this);
        mAttributeExtractor = new AttributeExtractor(this);
        mUniformExtractor = new UniformExtractor(this);
        mGLES20State = new GLES20State(this);
        mArrayTarget = new ArrayTarget(this);
        mResourceManager = new ResourceManager(this);
        mElementTarget = new ElementTarget(this);
        mTextureUnitManager = new TextureUnitManager(this);
    }

    /**
     * Private constructor due to circular dependencies.
     */
    private BackendContext() {
    }

    /**
     * Get the shader compiler.
     *
     * @return The compiler.
     */
    public ShaderCompiler getCompiler() {
        return mShaderCompiler;
    }

    /**
     * Get the program linker.
     *
     * @return The linker.
     */
    public ProgramLinker getLinker() {
        return mProgramLinker;
    }

    /**
     * Get shader program attribute extractor.
     *
     * @return The extractor.
     */
    public AttributeExtractor getAttributeExtractor() {
        return mAttributeExtractor;
    }

    /**
     * Get shader program uniform extractor.
     *
     * @return The extractor.
     */
    public UniformExtractor getUniformExtractor() {
        return mUniformExtractor;
    }

    /**
     * Get uniform writer.
     *
     * @return The uniform writer.
     */
    public UniformWriter getUniformWriter() {
        return mUniformWriter;
    }

    /**
     * Get the state.
     *
     * @return The state.
     */
    public GLES20State getState() {
        return mGLES20State;
    }

    /**
     * Get the array target.
     *
     * @return The array target.
     */
    public ArrayTarget getArrayTarget() {
        return mArrayTarget;
    }

    /**
     * Get the buffer manager.
     *
     * @return The buffer manager.
     */
    public ResourceManager getResourceManager() {
        return mResourceManager;
    }

    /**
     * Get the element target.
     *
     * @return The element target.
     */
    public ElementTarget getElementTarget() {
        return mElementTarget;
    }

    /**
     * Get the texture unit manager.
     *
     * @return The texture unit manager.
     */
    public TextureUnitManager getTextureUnitManager() {
        return mTextureUnitManager;
    }
}

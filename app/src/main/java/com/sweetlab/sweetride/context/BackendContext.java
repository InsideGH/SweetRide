package com.sweetlab.sweetride.context;

/**
 * Backend context.
 */
public class BackendContext {
    private ShaderCompiler mShaderCompiler;
    private ProgramLinker mProgramLinker;
    private UniformWriter mUniformWriter;
    private AttributeExtractor mAttributeExtractor;
    private UniformExtractor mUniformExtractor;
    private GLES20State mGLES20State;
    private ResourceRelease mResourceRelease;
    private ArrayTarget mArrayTarget;
    private BufferManager mBufferManager;

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
        mShaderCompiler = new ShaderCompiler(this);
        mProgramLinker = new ProgramLinker(this);
        mUniformWriter = new UniformWriter(this);
        mAttributeExtractor = new AttributeExtractor(this);
        mUniformExtractor = new UniformExtractor(this);
        mResourceRelease = new ResourceRelease(this);
        mGLES20State = new GLES20State(this);
        mArrayTarget = new ArrayTarget(this);
        mBufferManager = new BufferManager(this);
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
     * Get the resource releaser.
     *
     * @return The resource releaser.
     */
    public ResourceRelease getResourceRelease() {
        return mResourceRelease;
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
    public BufferManager getBufferManager() {
        return mBufferManager;
    }
}

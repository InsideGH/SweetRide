# SweetRide
Android 3d graphics engine built on opengl es 2.0. Code ui in main thread, let engine draw in gl thread.

Haven't worked on this for 2 month, uploading this to have it safe until I decide/feel like
working on it again. To many other things ongoing...

So, a 5 minute description as follows:

Don't mess around in gl thread. Instead, do all you stuff in main UI thread and let the engine handle all the 
opengl-es 2.0 stuff.

First, you need a RenderNode parent to draw anything. The RenderNode must be equiped with a NodeRenderer. 
There is a DefaultRenderNode that does this for typical onscreen rendering. Want to render to a Texture instead,
then just add a texture renderer instead that implements the NodeRenderer (and modify the frame buffer target). 
Want to render for a virtual helmet, just create a renderer that renders left, right offseted and distortion
compensated (not implemented though).

Attach Nodes or Geometries in a graph. Want to draw something, create a Geometry (a node) and attach it
to the graph.

Want to move around stuff? Modify the Nodes transform. The engine will figure out the combined world transform 
based on parent transforms. It will deal with bounding box transformation as well.

Want to enable/disable some gl specific stuff like blend, cullface etc. Fetch the Nodes RenderSettings and 
modify what you want. All children will inherit settings (unless they explicitly decided for something else).

Add a touch listener to do some ray picking.

Each Node has a Transform, RenderSettings and support for NodeControllers and a Camera.

The Transform supports basic stuff like translation, rotation scale, combining etc etc.

The RenderSettings supports stuff like blend, cullface, depthtest, dither, polygonoffset, clear, clearcolor, 
viewport, scissor...etc etc etc.

A node controller can be attached to the Node and get updates each frame until it decides to die.

The camera supports usual lookAt plus a transform (post multiplied) and it has a Frustrum where perspective or 
orthographic projection can be set.

Each Geometry has Uniform support, Mesh and Material. The Mesh can have Bounding box, IndicesBuffer, VertexBuffers. 

The VertexBuffer is created with a supplied VertexData.

The Material can have a Shaderprogram and Textures.

Yeah, there are a a lot of testcases as well.

Last thing, started to work on a demo game. Just got the point where 
* HUD is shown with strafe and turn controls.
* Fps camera is used to "fly" and "turn" around. It takes move and turn input, creates a transform and uses the 
  Camera post transform method to apply it.
* Patch based fractal terrain generation giving the illusion of a unlimited world terrain. Depending on distance
  to camera, different levels/detail of the terrain is rendered. Each terrain patch is rendered using
  a single triangle strip. Different neightbouring patches can have different detail levels. Man, it took time to 
  get this right.
* Catmull-Rom curve for a future tunnel to fly in.

To use, extend the EngineView (which is a GLSurfaceView). Implement the required createUserApplication method. The
engine will callback the applications init method when it's ready, giving the reference to the engine root graph 
node. Then the only thing to do is to attach stuff to the graph.

Oh yeah, it's totaly possible to use the BackendContext OpenGL HAL layer as well to get a little bit higher up 
from the raw gl commands. The BackendContext is what the engine is using. Can only be used from gl thread. 
Includes stuff like ->

    private final Capabilities mCapabilities;
    private final RenderState mRenderSettings;
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
    private RenderBufferTarget mRenderBufferTarget;
    private FrameBufferTarget mFrameBufferTarget;
    private BackendActionHandler mBackendActionHandler;



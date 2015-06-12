package com.sweetlab.sweetride.tryouts.stars;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;

import com.sweetlab.sweetride.R;
import com.sweetlab.sweetride.attributedata.VertexBuffer;
import com.sweetlab.sweetride.attributedata.VerticesData;
import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.context.BufferUsage;
import com.sweetlab.sweetride.context.MagFilter;
import com.sweetlab.sweetride.context.MinFilter;
import com.sweetlab.sweetride.context.TextureUnit;
import com.sweetlab.sweetride.math.Camera;
import com.sweetlab.sweetride.math.Frustrum;
import com.sweetlab.sweetride.resource.TextureResource;
import com.sweetlab.sweetride.shader.Attribute;
import com.sweetlab.sweetride.shader.FragmentShader;
import com.sweetlab.sweetride.shader.ShaderProgram;
import com.sweetlab.sweetride.shader.VertexShader;
import com.sweetlab.sweetride.testframework.OpenGLTestCase;
import com.sweetlab.sweetride.testframework.ResultRunnable;
import com.sweetlab.sweetride.texture.Texture2D;

public class StarFieldTest extends OpenGLTestCase {

    public static final String VERTEX_SRC =
            "attribute vec4 a_Pos; \n" +
                    "uniform mat4 u_worldViewProjMat; \n" +
                    "uniform float timeStamp; \n" +
                    "void main() { " +
                    "    vec4 newPos = a_Pos; \n" +
                    "    newPos.z -= timeStamp; \n" +
                    "    gl_PointSize = 30.0 + newPos.z*3.0; \n" +
                    "    gl_Position = u_worldViewProjMat * newPos; " +
                    "} ";

    /**
     * Fragment code. Single texture.
     */
    public static final String FRAGMENT_SRC =
            "precision mediump float;\n" +
                    "uniform sampler2D s_texture;\n" +
                    "void main() {\n" +
                    "vec4 color = texture2D(s_texture, gl_PointCoord);\n" +
                    "gl_FragColor = color;\n" +
                    "}";

    /**
     * Backend context
     */
    private BackendContext mContext;

    /**
     * The star bitmap
     */
    private Bitmap mStarBitmap;

    /**
     * The star texture.
     */
    private Texture2D mStarTexture;

    /**
     * The star shader.
     */
    private ShaderProgram mShaderProgram;

    /**
     * The stars
     */
    private VertexBuffer mVertexBuffer;

    /**
     * The camera.
     */
    private Camera mCamera = new Camera();

    /**
     * TimeStamp.
     */
    private float[] mTimeStamp = new float[]{0};

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mContext = getBackendContext();

        mStarBitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.star);
        mStarTexture = new Texture2D("s_texture", mStarBitmap, MinFilter.NEAREST, MagFilter.NEAREST);

        mCamera.lookAt(0, 0, 3, 0, 0, 0);
        mCamera.getFrustrum().setPerspectiveProjection(90, Frustrum.FovType.AUTO_FIT, 0.1f, 100f, getSurfaceWidth(), getSurfaceHeight());

        float[] starPositions = createStarPositions();
        mVertexBuffer = new VertexBuffer("a_Pos", new VerticesData(starPositions), BufferUsage.STATIC);
    }

    public void test() throws Exception {
        VertexShader vertexShader = new VertexShader(VERTEX_SRC);
        FragmentShader fragmentShader = new FragmentShader(FRAGMENT_SRC);
        mShaderProgram = new ShaderProgram(vertexShader, fragmentShader);

        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                mShaderProgram.create(mContext);
                mVertexBuffer.create(mContext);
                mVertexBuffer.load(mContext);
                mStarTexture.create(mContext);
                mStarTexture.load(mContext);
                return null;
            }
        });

        for (int i = 0; i < 200; i++) {
            runOnDrawFrame(new ResultRunnable() {
                @Override
                public Object run() {
                    clearScreen(0, 0, 0, 1);
                    mTimeStamp[0] += 0.05f;
                    draw(mContext, mShaderProgram, mStarTexture, mVertexBuffer);
                    return null;
                }
            });
        }
//        sleepOnDrawFrame(4000);
    }

    private void draw(BackendContext context, ShaderProgram program, TextureResource texture, VertexBuffer... buffers) {
        int vertexCount = buffers[0].getAttributePointer(0).getVertexCount();

        for (VertexBuffer buffer : buffers) {
            if (vertexCount != buffer.getAttributePointer(0).getVertexCount()) {
                throw new RuntimeException("Trying to drawArraySeparateBuffers using vertex buffers of different sizes");
            }
            context.getArrayTarget().enableAttribute(program, buffer);
        }

        context.getState().useProgram(program);

        TextureUnit textureUnit1 = context.getTextureUnitManager().takeTextureUnit();
        textureUnit1.getTexture2DTarget().enable(program, texture);

        context.getUniformWriter().writeFloat(program, "u_worldViewProjMat", mCamera.getViewProjectionMatrix().m);
        context.getUniformWriter().writeFloat(program, "timeStamp", mTimeStamp);
        context.getArrayTarget().draw(GLES20.GL_POINTS, 0, vertexCount);

        textureUnit1.getTexture2DTarget().disable(texture);
        context.getTextureUnitManager().returnTextureUnit(textureUnit1);

        for (VertexBuffer buffer : buffers) {
            Attribute attribute = program.getAttribute(buffer.getAttributePointer(0).getName());
            if (attribute != null) {
                context.getArrayTarget().disableAttribute(attribute);
            }
        }
    }

    private float[] createStarPositions() {
        return new float[]{
                -1, 0, 0,
                0, 0, 0,
                1, 0, 0,
                -1, 1, 0,
                0, 1, 0,
                1, 1, 0,
                -1, -1, 0,
                0, -1, 0,
                1, -1, 0,

        };
    }
}

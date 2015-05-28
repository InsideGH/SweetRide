package com.sweetlab.sweetride.context;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.opengl.GLES20;

import com.sweetlab.sweetride.attributedata.AttributePointer;
import com.sweetlab.sweetride.attributedata.IndicesBuffer;
import com.sweetlab.sweetride.attributedata.InterleavedVertexBuffer;
import com.sweetlab.sweetride.attributedata.TextureCoordData;
import com.sweetlab.sweetride.attributedata.VerticesData;
import com.sweetlab.sweetride.resource.BufferResource;
import com.sweetlab.sweetride.resource.TextureResource;
import com.sweetlab.sweetride.shader.Attribute;
import com.sweetlab.sweetride.shader.FragmentShader;
import com.sweetlab.sweetride.shader.ShaderProgram;
import com.sweetlab.sweetride.shader.VertexShader;
import com.sweetlab.sweetride.testframework.OpenGLTestCase;
import com.sweetlab.sweetride.testframework.ResultRunnable;
import com.sweetlab.sweetride.texture.Texture2D;

/**
 * Test using textures.
 */
public class TextureUnitManagerTest extends OpenGLTestCase {

    private static final String VERTEX_CODE =
            "attribute vec4 a_Pos; \n" +
                    "attribute vec2 a_texCoord;\n" +
                    "varying vec2 v_texCoord;\n" +
                    "void main() { " +
                    "    v_texCoord = a_texCoord;\n" +
                    "    gl_Position = a_Pos; " +
                    "} ";

    private static final String FRAGMENT_CODE =
            "precision mediump float;\n" +
                    "varying vec2 v_texCoord;\n" +
                    "uniform sampler2D s_texture;\n" +
                    "void main() {\n" +
                    "vec4 color = texture2D(s_texture, v_texCoord);\n" +
                    "gl_FragColor = color;\n" +
                    "}";


    /**
     * Shader program with texture sampler.
     */
    private ShaderProgram mShaderProgram;

    /**
     * Indices buffer.
     */
    private IndicesBuffer mIb;

    /**
     * Interleaved texture buffer with vertices and texture coordinates.
     */
    private InterleavedVertexBuffer mVertexBuffer;

    /**
     * A texture with 4 colors/pixels.
     */
    private TextureResource mTexture;

    /**
     * The backend context.
     */
    private BackendContext mContext;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mIb = new IndicesBuffer(new short[]{0, 1, 2, 3}, GLES20.GL_STATIC_DRAW);
        mShaderProgram = new ShaderProgram(new VertexShader(VERTEX_CODE), new FragmentShader(FRAGMENT_CODE));

        InterleavedVertexBuffer.Builder builder = new InterleavedVertexBuffer.Builder(GLES20.GL_STATIC_DRAW);
        builder.add("a_Pos", new VerticesData(createQuadStrip(0.5f, 0.5f, 0, 0)));
        builder.add("a_texCoord", new TextureCoordData(createQuadTextureCoords()));
        mVertexBuffer = builder.build();

        mTexture = new Texture2D("s_texture", createBitmap(Bitmap.Config.ARGB_8888));

        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                mContext = getBackendContext();

                /**
                 * Link the shader program.
                 */
                mShaderProgram.create(mContext);

                /**
                 * Create indices buffer.
                 */
                mIb.create(mContext);

                /**
                 * Create vertex buffer.
                 */
                mVertexBuffer.create(mContext);

                /**
                 * Create texture.
                 */
                mTexture.create(mContext);

                /**
                 * Load indices to gpu.
                 */
                mContext.getElementTarget().load(mIb);

                /**
                 * Load interleaved vertex buffer to gpu.
                 */
                mContext.getArrayTarget().load(mVertexBuffer);

                /**
                 * Load texture and set filter.
                 */
                mContext.getTextureUnitManager().getDefaultTextureUnit().getTexture2DTarget().load(mTexture);
                mContext.getTextureUnitManager().getDefaultTextureUnit().getTexture2DTarget().setFilter(mTexture, GLES20.GL_NEAREST, GLES20.GL_NEAREST);
                return null;
            }
        });

    }

    /**
     * Draw textures quad interleaved.
     *
     * @throws Exception
     */
    public void testSomething() throws Exception {
        runOnDrawFrame(new ResultRunnable() {
            @Override
            public Object run() {
                /**
                 * Clear screen.
                 */
                clearScreen();

                /**
                 * This quad should be drawn centered.
                 */
                drawArrayInterleaved(mContext, mShaderProgram, mVertexBuffer, mTexture);
                return null;
            }
        });
        sleepOnDrawFrame(2000);
    }

    /**
     * Draw with a shader program and a interleaved vertex buffer.
     *
     * @param context      Backend context.
     * @param program      Shader program.
     * @param vertexBuffer Interleaved vertex buffer.
     * @param texture      Texture.
     */
    private static void drawArrayInterleaved(BackendContext context, ShaderProgram program, InterleavedVertexBuffer vertexBuffer, TextureResource texture) {
        BufferResource attributeData = vertexBuffer.getAttributeData();
        int pointerCount = vertexBuffer.getAttributePointerCount();

        for (int i = 0; i < pointerCount; i++) {
            AttributePointer pointer = vertexBuffer.getAttributePointer(i);
            Attribute attribute = program.getAttribute(pointer.getName());
            if (attribute != null) {
                context.getArrayTarget().enableAttribute(attribute, attributeData, pointer);
            }
        }

        context.getState().useProgram(program);

        TextureUnit textureUnit = context.getTextureUnitManager().takeTextureUnit();
        textureUnit.getTexture2DTarget().enable(program, texture);

        int vertexCount = vertexBuffer.getAttributePointer(0).getVertexCount();
        context.getArrayTarget().draw(GLES20.GL_TRIANGLE_STRIP, 0, vertexCount);

        textureUnit.getTexture2DTarget().disable(texture);
        context.getTextureUnitManager().returnTextureUnit(textureUnit);


        for (int i = 0; i < pointerCount; i++) {
            AttributePointer pointer = vertexBuffer.getAttributePointer(i);
            Attribute attribute = program.getAttribute(pointer.getName());
            if (attribute != null) {
                context.getArrayTarget().disableAttribute(attribute);
            }
        }
    }

    /**
     * Create vertices for a quad.
     *
     * @param width  Width of quad.
     * @param height Height of quad.
     * @param transX Any inbound translation x.
     * @param transY Any inbound translation y.
     * @return Array of vertices.
     */
    private float[] createQuadStrip(float width, float height, float transX, float transY) {
        float[] data = new float[4 * 3];
        float w = width * 0.5f;
        float h = height * 0.5f;

        data[0] = -w + transX;
        data[1] = -h + transY;
        data[2] = 0;

        data[3] = +w + transX;
        data[4] = -h + transY;
        data[5] = 0;

        data[6] = -w + transX;
        data[7] = +h + transY;
        data[8] = 0;

        data[9] = +w + transX;
        data[10] = +h + transY;
        data[11] = 0;

        return data;
    }

    /**
     * Create a bitmap with some color.
     *
     * @param config Bitmap config.
     * @return A bitmap.
     */
    private Bitmap createBitmap(Bitmap.Config config) {
        return Bitmap.createBitmap(new int[]{Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW}, 2, 2, config);
    }

    /**
     * Create flipped texture coordinates.
     *
     * @return Flipped texture coordinates.
     */
    private float[] createQuadTextureCoords() {
        float[] tex = new float[]{0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f};
        for (int i = 1; i < tex.length; i += 2) {
            tex[i] = 1 - tex[i];
        }
        return tex;
    }
}
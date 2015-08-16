package com.sweetlab.sweetride.demo.game.terrain.newtake;

import com.sweetlab.sweetride.attributedata.InterleavedVertexBuffer;
import com.sweetlab.sweetride.context.BufferUsage;
import com.sweetlab.sweetride.context.MeshDrawingMode;
import com.sweetlab.sweetride.demo.game.terrain.texture.PatchTexCoordData;
import com.sweetlab.sweetride.demo.game.terrain.vertices.PatchVerticesData;
import com.sweetlab.sweetride.geometry.Geometry;
import com.sweetlab.sweetride.material.Material;
import com.sweetlab.sweetride.mesh.Mesh;
import com.sweetlab.sweetride.shader.FragmentShader;
import com.sweetlab.sweetride.shader.ShaderProgram;
import com.sweetlab.sweetride.shader.VertexShader;

/**
 * A factory for creating base geometries that are constructed with common parts
 * as well as unique parts.
 * <p/>
 * The common parts are
 * Shader program.
 * Vertex (grid) data.
 * Texture coordinate data.
 * Indices data (if possible).
 * <p/>
 * The unique parts are
 * Geometry.
 * Material with shader shader program.
 * Mesh.
 * Indices data (if necessary).
 */
public final class BaseGeometryFactory {
    /**
     * Vertex shader code.
     */
    public static final String VERTEX_CODE =
            "attribute vec4 a_Pos; \n" +
                    "attribute float a_Height; \n" +
                    "attribute vec2 a_texCoord;\n" +
                    "varying vec2 v_texCoord;\n" +
                    "uniform mat4 u_worldViewProjMat; \n" +
                    "void main() { " +
                    "    v_texCoord = a_texCoord;\n" +
                    "    vec4 pos = a_Pos;\n" +
                    "    pos.y = pos.y + a_Height;\n" +
                    "    gl_Position = u_worldViewProjMat * pos; " +
                    "} ";

    /**
     * Fragment shader code.
     */
    public static final String FRAGMENT_CODE =
            "precision mediump float;\n" +
                    "varying vec2 v_texCoord;\n" +
                    "uniform sampler2D s_texture;\n" +
                    "void main() {\n" +
                    "vec4 color = texture2D(s_texture, v_texCoord);\n" +
                    "gl_FragColor = color;\n" +
                    "}";

    /**
     * The vertices attribute in the shader.
     */
    private static final String ATTRIBUTE_VERTICES = "a_Pos";

    /**
     * The texture coordinates attribute in the shader.
     */
    private static final String ATTRIBUTE_TEXTURE_COORD = "a_texCoord";

    /**
     * The texture coordinates width.
     */
    private static final float TEXTURE_WIDTH = 1f;

    /**
     * The texture coordinates height.
     */
    private static final float TEXTURE_HEIGHT = 1f;

    /**
     * The texture coordinates position from right.
     */
    private static final float TEXTURE_RIGHT_POS = 0;

    /**
     * The texture coordinates position from near.
     */
    private static final float TEXTURE_NEAR_POS = 0;

    /**
     * The base vertex buffer consisting of vertices and texture coordinates.
     */
    private final InterleavedVertexBuffer mBaseVertexBuffer;

    /**
     * The shader program.
     */
    private final ShaderProgram mShaderProgram;

    /**
     * Constructor.
     *
     * @param size  The size of the patch.
     * @param width The patch width.
     * @param depth The patch height.
     */
    public BaseGeometryFactory(int size, float width, float depth) {
        PatchVerticesData verticesData = new PatchVerticesData(size, width, depth);
        PatchTexCoordData textureCoordData = new PatchTexCoordData(size, TEXTURE_WIDTH, TEXTURE_HEIGHT, TEXTURE_RIGHT_POS, TEXTURE_NEAR_POS);
        InterleavedVertexBuffer.Builder builder = new InterleavedVertexBuffer.Builder(BufferUsage.STATIC);
        builder.add(ATTRIBUTE_VERTICES, verticesData.getData());
        builder.add(ATTRIBUTE_TEXTURE_COORD, textureCoordData.getData());
        mBaseVertexBuffer = builder.build();

        mShaderProgram = new ShaderProgram(new VertexShader(VERTEX_CODE), new FragmentShader(FRAGMENT_CODE));
    }

    /**
     * Create base geometry. It contains
     * <p/>
     * Material with shader
     * Mesh with vertices, texture coordinates and indices.
     *
     * @return The geometry.
     */
    public Geometry createBaseGeometry() {
        Geometry geometry = new Geometry();
        geometry.setMesh(createBaseMesh());
        geometry.setMaterial(createBaseMaterial());

//        geometry.enableDrawBoundingBox(true);

        return geometry;
    }

    /**
     * Create a base mesh. The base mesh contains vertices, texture coordinates and indices. If
     * a mesh with same spec exist in cache it will be returned, otherwise a new mesh is created
     * and returned and cached for future use as well.
     *
     * @return The base mesh.
     */
    private Mesh createBaseMesh() {
        Mesh mesh = new Mesh(MeshDrawingMode.TRIANGLE_STRIP);
        mesh.addVertexBuffer(mBaseVertexBuffer);
        return mesh;
    }

    /**
     * Create base material. The material has a shader program.
     *
     * @return The base material.
     */
    private Material createBaseMaterial() {
        Material material = new Material();
        material.setShaderProgram(mShaderProgram);
        return material;
    }
}
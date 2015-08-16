package com.sweetlab.sweetride.geometry;

import com.sweetlab.sweetride.attributedata.VertexBuffer;
import com.sweetlab.sweetride.attributedata.VerticesData;
import com.sweetlab.sweetride.context.BufferUsage;
import com.sweetlab.sweetride.context.MeshDrawingMode;
import com.sweetlab.sweetride.intersect.BoundingBox;
import com.sweetlab.sweetride.material.Material;
import com.sweetlab.sweetride.math.Vec3;
import com.sweetlab.sweetride.mesh.Mesh;
import com.sweetlab.sweetride.shader.FragmentShader;
import com.sweetlab.sweetride.shader.ShaderProgram;
import com.sweetlab.sweetride.shader.VertexShader;

/**
 * This is a geometry drawing bounding box in debugging purposes.
 */
public class BoxLineGeometry extends Geometry {
    /**
     * The name of the vertices attribute in shader.
     */
    private static final String ATTRIBUTE_NAME = "a_Pos";

    /**
     * The vertex shader source.
     */
    private static final String VERTEX_SRC =
            "attribute vec4 a_Pos; \n" +
                    "uniform mat4 u_worldViewProjMat; \n" +
                    "void main() { " +
                    "    gl_Position = u_worldViewProjMat * a_Pos; " +
                    "} ";

    /**
     * The fragment shader source.
     */
    private static final String FRAGMENT_SRC =
            "precision mediump float;\n" +
                    "void main() {\n" +
                    "gl_FragColor = vec4(1.0, 1.0, 1.0, 1.0);\n" +
                    "}";

    /**
     * FLL corner
     */
    private static final int A = 0;

    /**
     * FLR corner
     */
    private static final int B = 1;

    /**
     * FUL corner
     */
    private static final int C = 2;

    /**
     * FUR corner
     */
    private static final int D = 3;

    /**
     * BLL corner
     */
    private static final int E = 4;

    /**
     * BLR corner
     */
    private static final int F = 5;

    /**
     * BUL corner
     */
    private static final int G = 6;

    /**
     * BUR corner
     */
    private static final int H = 7;

    /**
     * Number of corners in a bounding box.
     */
    private static final int NBR_BOX_CORNERS = 8;

    /**
     * Number of lines in a box.
     */
    private static final int NBR_LINES = 12;

    /**
     * Bounding box max (x,y,z) values
     */
    private final Vec3 mMax = new Vec3();

    /**
     * Bounding box min (x,y,z) values
     */
    private final Vec3 mMin = new Vec3();

    /**
     * Corners in bounding box.
     */
    private final Vec3[] mPoints = new Vec3[NBR_BOX_CORNERS];

    /**
     * Constructor. Empty geometry until updateBox is called.
     */
    public BoxLineGeometry() {
        Material material = new Material();
        material.setShaderProgram(new ShaderProgram(new VertexShader(VERTEX_SRC), new FragmentShader(FRAGMENT_SRC)));
        setMaterial(material);
    }

    /**
     * Update with a new box.
     *
     * @param box The new box.
     */
    public void updateBox(BoundingBox box) {
        buildPoints(box);
        Mesh mesh = new Mesh(MeshDrawingMode.LINES);
        VertexBuffer vb = new VertexBuffer(ATTRIBUTE_NAME, createVertices(), BufferUsage.STATIC);
        mesh.addVertexBuffer(vb);
        setMesh(mesh);
    }

    /**
     * Fill the member array of corner points from bounding box.
     * <pre>
     *
     *  Y (+)                      Z (-)
     *  |                        /
     *  |                      /
     *  |        G---------H
     *  |      / |        /|
     *  |    /   |      /  |
     *  |   C----|-----D   |
     *  |   |    E-----|---F
     *  |   |  |       |  |
     *  |   | |        | |
     *  |   A----------B
     *  |
     *  ------------------------------------ X (+)
     *  </pre>
     *
     * @param box The bounding box.
     */
    private void buildPoints(BoundingBox box) {
        for (int i = 0; i < mPoints.length; i++) {
            mPoints[i] = new Vec3();
        }

        box.getMax(mMax);
        box.getMin(mMin);

        mPoints[A].set(mMin.x, mMin.y, mMax.z);
        mPoints[B].set(mMax.x, mMin.y, mMax.z);
        mPoints[C].set(mMin.x, mMax.y, mMax.z);
        mPoints[D].set(mMax.x, mMax.y, mMax.z);

        mPoints[E].set(mMin.x, mMin.y, mMin.z);
        mPoints[F].set(mMax.x, mMin.y, mMin.z);
        mPoints[G].set(mMin.x, mMax.y, mMin.z);
        mPoints[H].set(mMax.x, mMax.y, mMin.z);
    }

    /**
     * Create vertices.
     *
     * @return The vertices array.
     */
    private VerticesData createVertices() {
        float[] data = new float[NBR_LINES * 2 * 3]; // 72
        /**
         * Line AB.
         */
        data[0] = mPoints[A].x;
        data[1] = mPoints[A].y;
        data[2] = mPoints[A].z;
        data[3] = mPoints[B].x;
        data[4] = mPoints[B].y;
        data[5] = mPoints[B].z;

        /**
         * Line AC.
         */
        data[6] = mPoints[A].x;
        data[7] = mPoints[A].y;
        data[8] = mPoints[A].z;
        data[9] = mPoints[C].x;
        data[10] = mPoints[C].y;
        data[11] = mPoints[C].z;

        /**
         * Line BD.
         */
        data[12] = mPoints[B].x;
        data[13] = mPoints[B].y;
        data[14] = mPoints[B].z;
        data[15] = mPoints[D].x;
        data[16] = mPoints[D].y;
        data[17] = mPoints[D].z;

        /**
         * Line CD.
         */
        data[18] = mPoints[C].x;
        data[19] = mPoints[C].y;
        data[20] = mPoints[C].z;
        data[21] = mPoints[D].x;
        data[22] = mPoints[D].y;
        data[23] = mPoints[D].z;

        /**
         * Line AE.
         */
        data[24] = mPoints[A].x;
        data[25] = mPoints[A].y;
        data[26] = mPoints[A].z;
        data[27] = mPoints[E].x;
        data[28] = mPoints[E].y;
        data[29] = mPoints[E].z;

        /**
         * Line BF.
         */
        data[30] = mPoints[B].x;
        data[31] = mPoints[B].y;
        data[32] = mPoints[B].z;
        data[33] = mPoints[F].x;
        data[34] = mPoints[F].y;
        data[35] = mPoints[F].z;

        /**
         * Line CG.
         */
        data[36] = mPoints[C].x;
        data[37] = mPoints[C].y;
        data[38] = mPoints[C].z;
        data[39] = mPoints[G].x;
        data[40] = mPoints[G].y;
        data[41] = mPoints[G].z;

        /**
         * Line DH.
         */
        data[42] = mPoints[D].x;
        data[43] = mPoints[D].y;
        data[44] = mPoints[D].z;
        data[45] = mPoints[H].x;
        data[46] = mPoints[H].y;
        data[47] = mPoints[H].z;

        /**
         * Line EG.
         */
        data[48] = mPoints[E].x;
        data[49] = mPoints[E].y;
        data[50] = mPoints[E].z;
        data[51] = mPoints[G].x;
        data[52] = mPoints[G].y;
        data[53] = mPoints[G].z;

        /**
         * Line FH.
         */
        data[54] = mPoints[F].x;
        data[55] = mPoints[F].y;
        data[56] = mPoints[F].z;
        data[57] = mPoints[H].x;
        data[58] = mPoints[H].y;
        data[59] = mPoints[H].z;

        /**
         * Line EF.
         */
        data[60] = mPoints[E].x;
        data[61] = mPoints[E].y;
        data[62] = mPoints[E].z;
        data[63] = mPoints[F].x;
        data[64] = mPoints[F].y;
        data[65] = mPoints[F].z;

        /**
         * Line GH.
         */
        data[66] = mPoints[G].x;
        data[67] = mPoints[G].y;
        data[68] = mPoints[G].z;
        data[69] = mPoints[H].x;
        data[70] = mPoints[H].y;
        data[71] = mPoints[H].z;

        return new VerticesData(data);
    }
}

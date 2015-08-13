package com.sweetlab.sweetride.game.terrain;

/**
 * Flat grid in the XZ plane. Model space position and size is configurable along with
 * number of vertices along x and z axis.
 */
public class FlatGrid {
    /**
     * Number of x vertices.
     */
    private final int mNbrVerticesX;

    /**
     * Number of z vertices.
     */
    private final int mNbrVerticesZ;

    /**
     * The data array starting with right near position and ending with far left position.
     */
    private final float[] mVertices;

    /**
     * The triangle strip indices.
     */
    private final short[] mIndices;

    /**
     * Constructor. Creates a flat grid of vertices in the XZ plane with triangle strip indices.
     *
     * @param left         Left boundary in model space.
     * @param right        Right boundary in model space.
     * @param near         Near boundary in model space.
     * @param far          Far boundary in model space.
     * @param nbrVerticesX Number of vertices along x axis.
     * @param nbrVerticesZ Number of vertices along z axis.
     */
    public FlatGrid(float left, float right, float near, float far, int nbrVerticesX, int nbrVerticesZ) {
        mVertices = generateVertices(left, right, near, far, nbrVerticesX, nbrVerticesZ);
        mIndices = generateIndices(nbrVerticesX, nbrVerticesZ);
        mNbrVerticesX = nbrVerticesX;
        mNbrVerticesZ = nbrVerticesZ;
    }

    /**
     * Get the float array for the vertices. The first 3 float values in array corresponds to
     * near right corner. The last 3 float values in the array corresponds to the far
     * left corner.
     *
     * @return The vertices.
     */
    public float[] getVertices() {
        return mVertices;
    }

    /**
     * Get the triangle strip indices. The strip starts at near right corner and ends at far
     * left corner. The indices are in CCW orientation (default OpenGL).
     *
     * @return The indices.
     */
    public short[] getIndices() {
        return mIndices;
    }

    /**
     * Get number of vertices along x axis.
     *
     * @return Number of vertices.
     */
    public int getNbrVerticesX() {
        return mNbrVerticesX;
    }

    /**
     * Get number of vertices along z axis.
     *
     * @return Number of vertices.
     */
    public int getNbrVerticesZ() {
        return mNbrVerticesZ;
    }

    /**
     * Generate a 3D flat grid in xz plane with defined number of vertices and model space
     * position and size.
     *
     * @param left         Left boundary in model space.
     * @param right        Right boundary in model space.
     * @param near         Near boundary in model space.
     * @param far          Far boundary in model space.
     * @param nbrVerticesX Number of vertices along x axis.
     * @param nbrVerticesZ Number of vertices along z axis.
     * @return Vertices.
     */
    private static float[] generateVertices(float left, float right, float near, float far, int nbrVerticesX, int nbrVerticesZ) {
        final float width = right - left;
        final float depth = near - far;
        final float stepX = width / (nbrVerticesX - 1);
        final float stepZ = depth / (nbrVerticesZ - 1);

        float[] vertices = new float[nbrVerticesX * nbrVerticesZ * 3];
        int index = 0;
        for (int y = 0; y < nbrVerticesZ; y++) {
            for (int x = 0; x < nbrVerticesX; x++) {
                vertices[index++] = right - x * stepX;
                vertices[index++] = 0;
                vertices[index++] = near - y * stepZ;
            }
        }
        return vertices;
    }

    /**
     * Generate triangle strip indices in CCW orientation (default OpenGL).
     *
     * @return Triangle strip indices.
     */
    private static short[] generateIndices(int nbrVerticesX, int nbrVerticesZ) {
        int rowCount = (nbrVerticesZ - 1);
        int nbrIndicesPerRow = nbrVerticesX * 2;
        int nbrDegeneration = (rowCount - 1) * 2;
        int nbrIndices = rowCount * nbrIndicesPerRow + nbrDegeneration;
        short[] indices = new short[nbrIndices];

        int idx = 0;
        int dataPos = 0;
        for (int row = 0; row < rowCount; row++) {
            /**
             * Not first row, duplicate/degenerate latest and next.
             */
            if (row > 0) {
                indices[idx] = indices[idx - 1];
                idx++;
                indices[idx++] = (short) dataPos;
            }
            /**
             * Create a strip for the row.
             */
            for (int x = 0; x < nbrVerticesX; x++) {
                indices[idx++] = (short) dataPos;
                indices[idx++] = (short) (dataPos + nbrVerticesX);
                dataPos++;
            }
        }
        return indices;
    }
}

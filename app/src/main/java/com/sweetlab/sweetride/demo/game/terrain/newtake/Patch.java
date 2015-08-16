package com.sweetlab.sweetride.demo.game.terrain.newtake;

import com.sweetlab.sweetride.attributedata.VertexBuffer;
import com.sweetlab.sweetride.attributedata.VertexData;
import com.sweetlab.sweetride.context.BufferUsage;
import com.sweetlab.sweetride.demo.game.terrain.height.PatchHeightData;
import com.sweetlab.sweetride.demo.game.terrain.indices.PatchIndicesBuffer;
import com.sweetlab.sweetride.geometry.Geometry;
import com.sweetlab.sweetride.intersect.BoundingBox;
import com.sweetlab.sweetride.material.Material;
import com.sweetlab.sweetride.mesh.Mesh;
import com.sweetlab.sweetride.node.Node;
import com.sweetlab.sweetride.pool.Poolable;
import com.sweetlab.sweetride.texture.Texture2D;

/**
 * A poolable patch. The reused parts are
 * <p/>
 * 1) Height
 * 2) Indices
 * <p/>
 * The following actions are performed during pooling
 * 1) Height vertex data is removed.
 * 2) Indices are set to null.
 * 3) Geometry model transform is set to identity.
 * 4) Mesh bounding box is set to null.
 * 5) All textures are removed.
 */
public class Patch extends Node implements Poolable {
    /**
     * The height attribute in the shader.
     */
    private static final String HEIGHT_ATTRIBUTE = "a_Height";

    /**
     * The geometry.
     */
    private final Geometry mGeometry;

    /**
     * The height data.
     */
    private PatchHeightData mHeightData;

    /**
     * The height vertex buffer, held to be able to remove when pooled.
     */
    private VertexBuffer mHeightVertexBuffer;

    /**
     * Constructor.
     *
     * @param geometry The geometry.
     */
    public Patch(Geometry geometry) {
        mGeometry = geometry;
        addChild(geometry);
    }

    @Override
    public void reset() {
        mGeometry.getModelTransform().setIdentity();
        Mesh mesh = mGeometry.getMesh();
        if (mesh != null) {
            mesh.setIndicesBuffer(null);
            mesh.removeVertexBuffer(mHeightVertexBuffer);
            mesh.setBoundingBox(null);
        } else {
            throw new RuntimeException("Patch mesh null while resetting, can't be.");
        }
        Material material = mGeometry.getMaterial();
        if (material != null) {
            while (material.getTextureCount() > 0) {
                material.removeTexture(material.getTexture(0));
            }
        } else {
            throw new RuntimeException("Patch material null while resetting, can't be.");
        }
    }

    /**
     * Set indices buffer.
     *
     * @param indices The indices buffer.
     */
    public void setIndicesBuffer(PatchIndicesBuffer indices) {
        Mesh mesh = mGeometry.getMesh();
        if (mesh != null) {
            mesh.setIndicesBuffer(indices);
        } else {
            throw new RuntimeException("Patch mesh null while setting indices, can't be");
        }
    }

    /**
     * The the patch translate.
     *
     * @param x x position.
     * @param y y position.
     * @param z z position.
     */
    public void setTranslate(float x, int y, float z) {
        mGeometry.getModelTransform().setTranslate(x, y, z);
    }

    /**
     * Set height data.
     *
     * @param heightData The height data.
     */
    public void setHeightData(PatchHeightData heightData) {
        mHeightData = heightData;
        VertexData vertexData = heightData.createVertexData();
        mHeightVertexBuffer = new VertexBuffer(HEIGHT_ATTRIBUTE, vertexData, BufferUsage.STATIC);
        Mesh mesh = mGeometry.getMesh();
        if (mesh != null) {
            mesh.addVertexBuffer(mHeightVertexBuffer);
        }
    }

    /**
     * Get the height data.
     *
     * @return The height data.
     */
    public PatchHeightData getHeightData() {
        return mHeightData;
    }

    /**
     * Add texture.
     *
     * @param texture The texture.
     */
    public void addTexture(Texture2D texture) {
        Material material = mGeometry.getMaterial();
        if (material != null) {
            material.addTexture(texture);
        }
    }

    /**
     * Set the bounding box.
     *
     * @param box The bounding box.
     */
    public void setBoundingBox(BoundingBox box) {
        Mesh mesh = mGeometry.getMesh();
        if (mesh != null) {
            mesh.setBoundingBox(box);
        }
    }
}

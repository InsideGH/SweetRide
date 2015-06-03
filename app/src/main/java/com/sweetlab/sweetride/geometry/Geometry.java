package com.sweetlab.sweetride.geometry;

import android.support.annotation.Nullable;

import com.sweetlab.sweetride.DebugOptions;
import com.sweetlab.sweetride.attributedata.IndicesBuffer;
import com.sweetlab.sweetride.context.ArrayTarget;
import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.context.ElementTarget;
import com.sweetlab.sweetride.context.TextureUnit;
import com.sweetlab.sweetride.context.TextureUnitManager;
import com.sweetlab.sweetride.material.Material;
import com.sweetlab.sweetride.mesh.Mesh;
import com.sweetlab.sweetride.node.Node;
import com.sweetlab.sweetride.node.NodeVisitor;
import com.sweetlab.sweetride.shader.ShaderProgram;

import java.util.ArrayList;
import java.util.List;

/**
 * Geometry is an abstraction that contains one material and one one mesh. Geometry is a self
 * contained object in the sense that it has all information necessary to be able to perform
 * drawing.
 */
public class Geometry extends Node {
    /**
     * The mesh.
     */
    private Mesh mMesh;

    /**
     * The material.
     */
    private Material mMaterial;

    /**
     * This is a temporary list of taken texture units used during drawing.
     */
    private List<TextureUnit> mTextureUnits = new ArrayList<>();

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Set mesh. Null allowed.
     *
     * @param mesh The mesh.
     */
    public void setMesh(@Nullable Mesh mesh) {
        if (mMesh != null) {
            disconnectNotifier(mMesh);
        }
        mMesh = mesh;
        connectNotifier(mMesh);
    }

    /**
     * Set material. Null allowed.
     *
     * @param material The material.
     */
    public void setMaterial(@Nullable Material material) {
        if (mMaterial != null) {
            disconnectNotifier(mMaterial);
        }
        mMaterial = material;
        connectNotifier(mMaterial);
    }

    /**
     * Get the mesh.
     *
     * @return The mesh or null.
     */
    @Nullable
    public Mesh getMesh() {
        return mMesh;
    }

    /**
     * Get the material.
     *
     * @return The material or null.
     */
    @Nullable
    public Material getMaterial() {
        return mMaterial;
    }

    /**
     * Create the geometry resources.
     *
     * @param context The backend context.
     */
    public void create(BackendContext context) {
        if (mMaterial != null) {
            mMaterial.create(context);
        }
        if (mMesh != null) {
            mMesh.create(context);
        }
    }

    /**
     * Load the geometry to gpu.
     *
     * @param context The backend context.
     */
    public void load(BackendContext context) {
        if (mMaterial != null) {
            mMaterial.load(context);
        }
        if (mMesh != null) {
            mMesh.load(context);
        }
    }

    /**
     * Draw the geometry. The drawing will be performed on outside decided framebuffer or
     * default window system (frame buffer).
     *
     * @param context The backend context.
     */
    public void draw(BackendContext context) {
        if (isDrawable()) {
            /**
             * Enable attributes.
             */
            enableAttributes(context);

            /**
             * Use shader program.
             */
            context.getState().useProgram(mMaterial.getShaderProgram());

            /**
             * Enable textures.
             */
            enableTextures(context);

            /**
             * Draw.
             */
            drawGeometry(context);

            /**
             * Disable textures.
             */
            disableTextures(context);

            /**
             * Disable attributes.
             */
            disableAttributes(context);
        }
    }

    /**
     * Enable attributes.
     *
     * @param context Backend context.
     */
    private void enableAttributes(BackendContext context) {
        final int vbCount = mMesh.getVertexBufferCount();
        final ArrayTarget arrayTarget = context.getArrayTarget();
        final ShaderProgram shaderProgram = mMaterial.getShaderProgram();
        for (int i = 0; i < vbCount; i++) {
            arrayTarget.enableAttribute(shaderProgram, mMesh.getVertexBuffer(i));
        }
    }

    /**
     * Disable attributes.
     *
     * @param context Backend context.
     */
    private void disableAttributes(BackendContext context) {
        final int vbCount = mMesh.getVertexBufferCount();
        final ArrayTarget arrayTarget = context.getArrayTarget();
        final ShaderProgram shaderProgram = mMaterial.getShaderProgram();
        for (int i = 0; i < vbCount; i++) {
            arrayTarget.disableAttribute(shaderProgram, mMesh.getVertexBuffer(i));
        }
    }

    /**
     * Enable textures.
     *
     * @param context Backend context.
     */
    private void enableTextures(BackendContext context) {
        final ShaderProgram program = mMaterial.getShaderProgram();
        final TextureUnitManager textureUnitManager = context.getTextureUnitManager();
        final int textureCount = mMaterial.getTextureCount();
        for (int i = 0; i < textureCount; i++) {
            TextureUnit textureUnit = textureUnitManager.takeTextureUnit();
            mTextureUnits.add(textureUnit);
            textureUnit.getTexture2DTarget().enable(program, mMaterial.getTexture(i));
        }
    }

    /**
     * Disable textures.
     *
     * @param context Backend context.
     */
    private void disableTextures(BackendContext context) {
        final int textureCount = mMaterial.getTextureCount();
        final TextureUnitManager textureUnitManager = context.getTextureUnitManager();
        for (int i = (textureCount - 1); i > -1; i--) {
            TextureUnit textureUnit = mTextureUnits.get(i);
            textureUnit.getTexture2DTarget().disable(mMaterial.getTexture(i));
            textureUnitManager.returnTextureUnit(textureUnit);
        }
        if (DebugOptions.DEBUG_GEOMETRY) {
            if (mTextureUnits.size() != textureCount) {
                throw new RuntimeException("Mismatch between number of textures and texture units = " + mTextureUnits.size() + " texture count = " + textureCount);
            }
        }
        mTextureUnits.clear();
    }

    /**
     * Draw using array or elements target.
     *
     * @param context Backend context.
     */
    private void drawGeometry(BackendContext context) {
        final IndicesBuffer ib = mMesh.getIndicesBuffer();
        if (ib == null) {
            context.getArrayTarget().draw(mMesh.getMode().getGlMode(), 0, mMesh.getVertexCount());
        } else {
            ElementTarget elementTarget = context.getElementTarget();
            elementTarget.enableElements(ib);
            elementTarget.draw(mMesh.getMode().getGlMode(), 0, ib.getIndicesCount());
            elementTarget.disableElements();
        }
    }

    /**
     * Check if precondition for drawing are fulfilled.
     *
     * @return True if drawable.
     */
    private boolean isDrawable() {
        if (mMaterial != null && mMesh != null) {
            if (mMaterial.getShaderProgram() != null && mMesh.getVertexBufferCount() > 0) {
                if (DebugOptions.DEBUG_GEOMETRY) {
                    if (!mMaterial.getShaderProgram().isCreated()) {
                        throw new RuntimeException("Trying to draw with a program that is not created");
                    }
                    int count;

                    count = mMaterial.getTextureCount();
                    for (int i = 0; i < count; i++) {
                        if (!mMaterial.getTexture(i).isCreated()) {
                            throw new RuntimeException("Trying to draw with a texture that is not created");
                        }
                    }

                    count = mMesh.getVertexBufferCount();
                    for (int i = 0; i < count; i++) {
                        if (!mMesh.getVertexBuffer(i).isCreated()) {
                            throw new RuntimeException("Trying to draw with a vertex buffer that is not created");
                        }
                    }

                    IndicesBuffer indicesBuffer = mMesh.getIndicesBuffer();
                    if (indicesBuffer != null && !indicesBuffer.isCreated()) {
                        throw new RuntimeException("Trying to draw with a indices buffer that is not created");
                    }
                }
                return true;
            }
        }
        return false;
    }
}

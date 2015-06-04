package com.sweetlab.sweetride.geometry;

import android.support.annotation.Nullable;

import com.sweetlab.sweetride.DebugOptions;
import com.sweetlab.sweetride.action.Action;
import com.sweetlab.sweetride.action.ActionId;
import com.sweetlab.sweetride.action.HandleThread;
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
     * Mesh reference has changed.
     */
    private final Action mMeshChange = new Action(this, ActionId.GEOMETRY_MESH, HandleThread.MAIN);

    /**
     * Material reference has changed.
     */
    private final Action mMaterialChange = new Action(this, ActionId.GEOMETRY_MATERIAL, HandleThread.MAIN);

    /**
     * This is a temporary list of taken texture units used during drawing.
     */
    private final List<TextureUnit> mTextureUnits = new ArrayList<>();

    /**
     * The mesh.
     */
    private Mesh mMesh;

    /**
     * The material.
     */
    private Material mMaterial;

    /**
     * The mesh reference used by GL thread.
     */
    private Mesh mMeshGL;

    /**
     * The material reference used by GL thread.
     */
    private Material mMaterialGL;

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void handleAction(Action action) {
        switch (action.getType()) {
            case GEOMETRY_MESH:
                mMeshGL = mMesh;
                break;
            case GEOMETRY_MATERIAL:
                mMaterialGL = mMaterial;
                break;
            default:
                throw new RuntimeException("wtf");
        }
    }

    @Override
    public void handleAction(BackendContext context, Action action) {
        throw new RuntimeException("wtf");
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
        addAction(mMeshChange);
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
        addAction(mMaterialChange);
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
        if (mMaterialGL != null) {
            mMaterialGL.create(context);
        }
        if (mMeshGL != null) {
            mMeshGL.create(context);
        }
    }

    /**
     * Load the geometry to gpu.
     *
     * @param context The backend context.
     */
    public void load(BackendContext context) {
        if (mMaterialGL != null) {
            mMaterialGL.load(context);
        }
        if (mMeshGL != null) {
            mMeshGL.load(context);
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
            context.getState().useProgram(mMaterialGL.getShaderProgram());

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
        final int vbCount = mMeshGL.getVertexBufferCount();
        final ArrayTarget arrayTarget = context.getArrayTarget();
        final ShaderProgram shaderProgram = mMaterialGL.getShaderProgram();
        for (int i = 0; i < vbCount; i++) {
            arrayTarget.enableAttribute(shaderProgram, mMeshGL.getVertexBuffer(i));
        }
    }

    /**
     * Disable attributes.
     *
     * @param context Backend context.
     */
    private void disableAttributes(BackendContext context) {
        final int vbCount = mMeshGL.getVertexBufferCount();
        final ArrayTarget arrayTarget = context.getArrayTarget();
        final ShaderProgram shaderProgram = mMaterialGL.getShaderProgram();
        for (int i = 0; i < vbCount; i++) {
            arrayTarget.disableAttribute(shaderProgram, mMeshGL.getVertexBuffer(i));
        }
    }

    /**
     * Enable textures.
     *
     * @param context Backend context.
     */
    private void enableTextures(BackendContext context) {
        final ShaderProgram program = mMaterialGL.getShaderProgram();
        final TextureUnitManager textureUnitManager = context.getTextureUnitManager();
        final int textureCount = mMaterialGL.getTextureCount();
        for (int i = 0; i < textureCount; i++) {
            TextureUnit textureUnit = textureUnitManager.takeTextureUnit();
            mTextureUnits.add(textureUnit);
            textureUnit.getTexture2DTarget().enable(program, mMaterialGL.getTexture(i));
        }
    }

    /**
     * Disable textures.
     *
     * @param context Backend context.
     */
    private void disableTextures(BackendContext context) {
        final int textureCount = mMaterialGL.getTextureCount();
        final TextureUnitManager textureUnitManager = context.getTextureUnitManager();
        for (int i = (textureCount - 1); i > -1; i--) {
            TextureUnit textureUnit = mTextureUnits.get(i);
            textureUnit.getTexture2DTarget().disable(mMaterialGL.getTexture(i));
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
        final IndicesBuffer ib = mMeshGL.getIndicesBuffer();
        if (ib == null) {
            context.getArrayTarget().draw(mMeshGL.getMode().getGlMode(), 0, mMeshGL.getVertexCount());
        } else {
            ElementTarget elementTarget = context.getElementTarget();
            elementTarget.enableElements(ib);
            elementTarget.draw(mMeshGL.getMode().getGlMode(), 0, ib.getIndicesCount());
            elementTarget.disableElements();
        }
    }

    /**
     * Check if precondition for drawing are fulfilled.
     *
     * @return True if drawable.
     */
    private boolean isDrawable() {
        if (mMaterialGL != null && mMeshGL != null) {
            if (mMaterialGL.getShaderProgram() != null && mMeshGL.getVertexBufferCount() > 0) {
                if (DebugOptions.DEBUG_GEOMETRY) {
                    if (!mMaterialGL.getShaderProgram().isCreated()) {
                        throw new RuntimeException("Trying to draw with a program that is not created");
                    }
                    int count;

                    count = mMaterialGL.getTextureCount();
                    for (int i = 0; i < count; i++) {
                        if (!mMaterialGL.getTexture(i).isCreated()) {
                            throw new RuntimeException("Trying to draw with a texture that is not created");
                        }
                    }

                    count = mMeshGL.getVertexBufferCount();
                    for (int i = 0; i < count; i++) {
                        if (!mMeshGL.getVertexBuffer(i).isCreated()) {
                            throw new RuntimeException("Trying to draw with a vertex buffer that is not created");
                        }
                    }

                    IndicesBuffer indicesBuffer = mMeshGL.getIndicesBuffer();
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

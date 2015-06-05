package com.sweetlab.sweetride.geometry;

import android.support.annotation.Nullable;

import com.sweetlab.sweetride.action.Action;
import com.sweetlab.sweetride.action.ActionId;
import com.sweetlab.sweetride.action.HandleThread;
import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.material.Material;
import com.sweetlab.sweetride.mesh.Mesh;
import com.sweetlab.sweetride.node.Node;
import com.sweetlab.sweetride.node.NodeVisitor;
import com.sweetlab.sweetride.uniform.CustomUniform;

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
     * Custom uniform collection has changed.
     */
    private final Action mUniformCollectionChange = new Action(this, ActionId.GEOMETRY_CUSTOM_UNIFORM, HandleThread.MAIN);

    /**
     * List of custom uniforms.
     */
    private final List<CustomUniform> mCustomUniforms = new ArrayList<>();

    /**
     * The backend geometry.
     */
    private final BackendGeometry mBackendGeometry = new BackendGeometry();

    /**
     * The mesh.
     */
    private Mesh mMesh;

    /**
     * The material.
     */
    private Material mMaterial;

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void handleAction(Action action) {
        switch (action.getType()) {
            case GEOMETRY_MESH:
                mBackendGeometry.setMesh(mMesh.getBackendMesh());
                break;
            case GEOMETRY_MATERIAL:
                mBackendGeometry.setMaterial(mMaterial.getBackendMaterial());
                break;
            case GEOMETRY_CUSTOM_UNIFORM:
                mBackendGeometry.setCustomUniforms(mCustomUniforms);
                break;
            default:
                throw new RuntimeException("wtf");
        }
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
     * Add custom uniform.
     *
     * @param uniform Uniform to add.
     */
    public void addUniform(CustomUniform uniform) {
        mCustomUniforms.add(uniform);
        connectNotifier(uniform);
        addAction(mUniformCollectionChange);
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
        mBackendGeometry.create(context);
    }

    /**
     * Load the geometry to gpu.
     *
     * @param context The backend context.
     */
    public void load(BackendContext context) {
        mBackendGeometry.load(context);
    }

    /**
     * Draw the geometry. The drawing will be performed on outside decided framebuffer or
     * default window system (frame buffer).
     *
     * @param context The backend context.
     */
    public void draw(BackendContext context) {
        mBackendGeometry.draw(context);
    }
}

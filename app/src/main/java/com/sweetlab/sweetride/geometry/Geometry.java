package com.sweetlab.sweetride.geometry;

import android.support.annotation.Nullable;

import com.sweetlab.sweetride.action.Action;
import com.sweetlab.sweetride.action.ActionId;
import com.sweetlab.sweetride.action.HandleThread;
import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.engine.EngineUniform;
import com.sweetlab.sweetride.engine.EngineUniformCache;
import com.sweetlab.sweetride.material.Material;
import com.sweetlab.sweetride.math.Camera;
import com.sweetlab.sweetride.math.Matrix44;
import com.sweetlab.sweetride.mesh.Mesh;
import com.sweetlab.sweetride.node.Node;
import com.sweetlab.sweetride.node.NodeVisitor;
import com.sweetlab.sweetride.shader.ShaderProgram;
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
     * Engine uniform has changed.
     */
    private final Action mEngineUniformChange = new Action(this, ActionId.GEOMETRY_ENGINE_UNIFORM, HandleThread.MAIN);

    /**
     * List of custom uniforms.
     */
    private final List<CustomUniform> mCustomUniforms = new ArrayList<>();

    /**
     * The backend geometry.
     */
    private final BackendGeometry mBackendGeometry = new BackendGeometry();

    /**
     * The supported engine uniforms.
     */
    private final EngineUniformCache mEngineUniformCache = new EngineUniformCache();

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
    protected void onActionAdded(Action action) {
        super.onActionAdded(action);
        switch (action.getType()) {
            case NODE_WORLD_DIRTY:
                addAction(mEngineUniformChange);
                break;
        }
    }

    @Override
    public boolean handleAction(Action action) {
        if (super.handleAction(action)) {
            return true;
        }
        switch (action.getType()) {
            case GEOMETRY_ENGINE_UNIFORM:
                if (mMaterial != null) {
                    ShaderProgram shaderProgram = mMaterial.getShaderProgram();
                    if (shaderProgram != null) {
                        List<EngineUniform> engineUniforms = invalidateEngineUniforms(shaderProgram);
                        mBackendGeometry.setEngineUniforms(engineUniforms);
                    }
                }
                return true;
            case GEOMETRY_MESH:
                mBackendGeometry.setMesh(mMesh.getBackendMesh());
                return true;
            case GEOMETRY_MATERIAL:
                mBackendGeometry.setMaterial(mMaterial.getBackendMaterial());
                return true;
            case GEOMETRY_CUSTOM_UNIFORM:
                mBackendGeometry.setCustomUniforms(mCustomUniforms);
                return true;
            default:
                return false;
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

    /**
     * Invalidate active engine uniforms.
     *
     * @param program Shader program.
     * @return List of active engine uniforms.
     */
    private List<EngineUniform> invalidateEngineUniforms(ShaderProgram program) {
        List<EngineUniform> engineUniforms = mEngineUniformCache.getEngineUniforms(program);
        if (!engineUniforms.isEmpty()) {
            Camera camera = findCamera();
            for (EngineUniform uniform : engineUniforms) {
                switch (uniform) {
                    case MODEL_MATRIX:
                        uniform.getMatrix().set(getModelTransform().getMatrix());
                        break;
                    case WORLD_MATRIX:
                        uniform.getMatrix().set(getWorldTransform().getMatrix());
                        break;
                    case VIEW_MATRIX:
                        if (camera != null) {
                            uniform.getMatrix().set(camera.getViewMatrix());
                        }
                        break;
                    case PROJECTION_MATRIX:
                        if (camera != null) {
                            uniform.getMatrix().set(camera.getFrustrum().getProjectionMatrix());
                        }
                        break;
                    case WORLD_VIEW_MATRIX:
                        if (camera != null) {
                            Matrix44 world = getWorldTransform().getMatrix();
                            Matrix44 view = camera.getViewMatrix();
                            /** worldView = view * world */
                            Matrix44.mult(uniform.getMatrix(), view, world);
                        }
                        break;
                    case WORLD_VIEW_PROJECTION_MATRIX:
                        if (camera != null) {
                            Matrix44 world = getWorldTransform().getMatrix();
                            /** viewProj = proj * view */
                            Matrix44 viewProj = camera.getViewProjectionMatrix();
                            /** worldViewProj = viewProj * world = proj * view * world */
                            Matrix44.mult(uniform.getMatrix(), viewProj, world);
                        }
                        break;
                }
            }
        }
        return engineUniforms;
    }
}

package com.sweetlab.sweetride.geometry;

import android.support.annotation.Nullable;
import android.view.MotionEvent;

import com.sweetlab.sweetride.action.Action;
import com.sweetlab.sweetride.action.ActionThread;
import com.sweetlab.sweetride.action.GlobalActionId;
import com.sweetlab.sweetride.camera.Camera;
import com.sweetlab.sweetride.camera.LowerLeftBox;
import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.engine.uniform.EngineUniform;
import com.sweetlab.sweetride.engine.uniform.EngineUniformCache;
import com.sweetlab.sweetride.intersect.BoundingBox;
import com.sweetlab.sweetride.material.Material;
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
    private final Action<GlobalActionId> mMeshChange = new Action<>(this, GlobalActionId.GEOMETRY_MESH, ActionThread.MAIN);

    /**
     * Material reference has changed.
     */
    private final Action<GlobalActionId> mMaterialChange = new Action<>(this, GlobalActionId.GEOMETRY_MATERIAL, ActionThread.MAIN);

    /**
     * Custom uniform collection has changed.
     */
    private final Action<GlobalActionId> mUniformCollectionChange = new Action<>(this, GlobalActionId.GEOMETRY_CUSTOM_UNIFORM, ActionThread.MAIN);

    /**
     * Engine uniform has changed.
     */
    private final Action<GlobalActionId> mEngineUniformChange = new Action<>(this, GlobalActionId.GEOMETRY_ENGINE_UNIFORM, ActionThread.MAIN);

    /**
     * List of custom uniforms.
     */
    private final List<CustomUniform> mCustomUniforms = new ArrayList<>();

    /**
     * The supported engine uniforms.
     */
    private final EngineUniformCache mEngineUniformCache = new EngineUniformCache();

    /**
     * The geometry bounding box.
     */
    private final BoundingBox mBoundingBox = new BoundingBox();

    /**
     * The backend geometry.
     */
    private final BackendGeometry mBackendGeometry = new BackendGeometry();

    /**
     * On touch listeners.
     */
    private final List<OnTouchListener> mOnTouchListeners = new ArrayList<>();

    /**
     * The touch motion event handler.
     */
    private TouchEventHandler mTouchEventHandler;

    /**
     * The mesh.
     */
    private Mesh mMesh;

    /**
     * The material.
     */
    private Material mMaterial;

    /**
     * For debugging, draw the bounding box.
     */
    private BoxLineGeometry mBoxLineGeometry;

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    protected void onActionAdded(Action<GlobalActionId> action) {
        super.onActionAdded(action);
        switch (action.getType()) {
            case NODE_TRANSFORM_UPDATED:
                addAction(mEngineUniformChange);
                if (mMesh != null) {
                    BoundingBox meshBox = mMesh.getBoundingBox();
                    if (meshBox != null) {
                        /**
                         * Transform has changed, transform the geometry bounding box.
                         */
                        mBoundingBox.set(meshBox);
                        mBoundingBox.transform(getModelTransform().getMatrix());
                    }
                }
                break;

            case NODE_FRUSTRUM_UPDATED:
                LowerLeftBox viewPort = findCamera().getFrustrum().getViewPort();
                mTouchEventHandler = new TouchEventHandler(viewPort.getWidth(), viewPort.getHeight());
                break;

            case GEOMETRY_MESH:
                /**
                 * Geometry has a new mesh.
                 */
                if (mMesh != null) {
                    BoundingBox meshBox = mMesh.getBoundingBox();
                    if (meshBox != null) {
                        /**
                         * Take new mesh bounding box and transform the geometry bounding box.
                         */
                        mBoundingBox.set(meshBox);
                        mBoundingBox.transform(getModelTransform().getMatrix());

                        /**
                         * If bounding box is to be drawn, update with potentially new mesh
                         * box.
                         */
                        if (mBoxLineGeometry != null) {
                            mBoxLineGeometry.updateBox(meshBox);
                        }
                    }
                }
                break;

            case MESH_BOUNDING_BOX:
                /**
                 * Mesh has a new bounding box.
                 */
                if (mBoxLineGeometry != null) {
                    mBoxLineGeometry.updateBox(mMesh.getBoundingBox());
                }
                break;
        }
    }

    @Override
    public boolean handleAction(Action<GlobalActionId> action) {
        if (super.handleAction(action)) {
            return true;
        }
        switch (action.getType()) {
            case GEOMETRY_ENGINE_UNIFORM:
                if (mMaterial != null) {
                    ShaderProgram shaderProgram = mMaterial.getShaderProgram();
                    if (shaderProgram != null) {
                        List<EngineUniform> engineUniforms = mEngineUniformCache.getEngineUniforms(shaderProgram);
                        if (!engineUniforms.isEmpty()) {
                            updateEngineUniforms(engineUniforms);
                        }
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

    @Override
    public boolean onTouch(MotionEvent event) {
        if (!mOnTouchListeners.isEmpty()) {
            Camera camera = findCamera();
            if (camera != null) {
                if (mTouchEventHandler.onTouchEvent(event, camera, mBoundingBox, mOnTouchListeners)) {
                    return true;
                }
            }
        }
        return super.onTouch(event);
    }

    @Override
    public void draw(BackendContext context) {
        super.draw(context);
        mBackendGeometry.draw(context);
    }

    /**
     * Enable drawing of the bounding box.
     *
     * @param enable True if bounding box should be drawn.
     */
    public void enableDrawBoundingBox(boolean enable) {
        if (enable) {
            if (mBoxLineGeometry == null) {
                mBoxLineGeometry = new BoxLineGeometry();
                addChild(mBoxLineGeometry);
            }
            if (mMesh != null) {
                BoundingBox meshBox = mMesh.getBoundingBox();
                if (meshBox != null) {
                    mBoxLineGeometry.updateBox(meshBox);
                }
            }
        } else {
            if (mBoxLineGeometry != null) {
                removeChild(mBoxLineGeometry);
                mBoxLineGeometry = null;
            }
        }
    }

    /**
     * Get this geometries bounding box. The box can be empty.
     *
     * @return The geometries bounding box.
     */
    public BoundingBox getBoundingBox() {
        return mBoundingBox;
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
     * Add on touch listener.
     *
     * @param touchListener The listener to add.
     */
    public void addOnTouchListener(OnTouchListener touchListener) {
        mOnTouchListeners.add(touchListener);
    }

    /**
     * Remove on touch listener.
     *
     * @param touchListener The listener to remove.
     */
    public void removeOnTouchListener(OnTouchListener touchListener) {
        mOnTouchListeners.remove(touchListener);
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
     * Update active engine uniforms from node/camera content.
     *
     * @param engineUniforms List of active engine uniforms.
     */
    private void updateEngineUniforms(List<EngineUniform> engineUniforms) {
        Camera camera = findCamera();
        for (EngineUniform uniform : engineUniforms) {
            switch (uniform.getType()) {
                case MODEL:
                    uniform.getMatrix().set(getModelTransform().getMatrix());
                    break;
                case WORLD:
                    uniform.getMatrix().set(getWorldTransform().getMatrix());
                    break;
                case VIEW:
                    if (camera != null) {
                        uniform.getMatrix().set(camera.getViewMatrix());
                    }
                    break;
                case PROJECTION:
                    if (camera != null) {
                        uniform.getMatrix().set(camera.getFrustrum().getProjectionMatrix());
                    }
                    break;
                case WORLD_VIEW:
                    if (camera != null) {
                        Matrix44 world = getWorldTransform().getMatrix();
                        Matrix44 view = camera.getViewMatrix();
                        /** worldView = view * world */
                        Matrix44.mult(uniform.getMatrix(), view, world);
                    }
                    break;
                case WORLD_VIEW_PROJECTION:
                    if (camera != null) {
                        Matrix44 world = getWorldTransform().getMatrix();
                        /** viewProj = proj * view */
                        Matrix44 viewProj = camera.getViewProjectionMatrix();
                        /** worldViewProj = viewProj * world = proj * view * world */
                        Matrix44.mult(uniform.getMatrix(), viewProj, world);
                    }
                    break;
                default:
                    throw new RuntimeException("trying to update unknown engine uniform");
            }
        }
    }
}

package com.sweetlab.sweetride.action;

/**
 * Global Action id.
 */
public enum GlobalActionId {
    /**
     * Render node camera reference changed.
     */
    NODE_CAMERA_SET,

    /**
     * Node action for whole graph that transform got dirty for a reason (several reasons).
     */
    NODE_TRANSFORM_UPDATED,

    /**
     * Node action for whole graph that frustrum has been updated.
     */
    NODE_FRUSTRUM_UPDATED,

    /**
     * Geometry mesh reference changed.
     */
    GEOMETRY_MESH,

    /**
     * Geometry material reference changed.
     */
    GEOMETRY_MATERIAL,

    /**
     * Geometry custom uniform collection changed.
     */
    GEOMETRY_CUSTOM_UNIFORM,

    /**
     * Engine uniforms has changed.
     */
    GEOMETRY_ENGINE_UNIFORM,

    /**
     * Mesh vertex buffer collection changed.
     */
    MESH_BUFFER,

    /**
     * Mesh indices buffer reference changed.
     */
    MESH_INDICES,

    /**
     * Material shader program reference changed.
     */
    MATERIAL_PROGRAM,

    /**
     * Material textures collection changed.
     */
    MATERIAL_TEXTURES,

    /**
     * Create shader backend resource.
     */
    SHADER_CREATE,

    /**
     * Create program backend resource.
     */
    PROGRAM_CREATE,

    /**
     * Create texture backend resource.
     */
    TEXTURE_CREATE,

    /**
     * Load texture in backend.
     */
    TEXTURE_LOAD,

    /**
     * Create indices backend resource.
     */
    INDICES_CREATE,

    /**
     * Load indices in backend.
     */
    INDICES_LOAD,

    /**
     * Create interleaved vertex buffer backend resource.
     */
    INTERLEAVED_BUFFER_CREATE,

    /**
     * Load buffer in backend.
     */
    INTERLEAVED_BUFFER_LOAD,

    /**
     * Create vertex buffer backend resource.
     */
    VERTEX_BUFFER_CREATE,

    /**
     * Load vertex buffer in backend.
     */
    VERTEX_BUFFER_LOAD,

    /**
     * Create render buffer backend resource.
     */
    RENDER_BUFFER_CREATE,

    /**
     * Create frame buffer backend resource.
     */
    FRAME_BUFFER_CREATE,

    /**
     * Camera has been updated.
     */
    CAMERA_UPDATED,

    /**
     * Frustrum has been updated.
     */
    FRUSTRUM_UPDATED,

    /**
     * Transform has been updated.
     */
    TRANSFORM_UPDATED,

    /**
     * Uniform has been updated.
     */
    CUSTOM_UNIFORM_UPDATE,

    /**
     * Render settings updated.
     */
    RENDER_SETTINGS_DIRTY,
}

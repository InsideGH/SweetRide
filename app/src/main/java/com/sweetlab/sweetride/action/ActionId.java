package com.sweetlab.sweetride.action;

/**
 * Action id.
 */
public enum ActionId {
    /**
     * Geometry mesh reference has changed.
     */
    GEOMETRY_MESH,

    /**
     * Geometry material reference has changed.
     */
    GEOMETRY_MATERIAL,

    /**
     * Mesh vertex buffer collection has changed.
     */
    MESH_BUFFER,

    /**
     * Mesh indices buffer reference has changed.
     */
    MESH_INDICES,

    /**
     * Material shader program reference has changed.
     */
    MATERIAL_PROGRAM,

    /**
     * Material textures collection has changed.
     */
    MATERIAL_TEXTURES,

    /**
     * Create the shader backend resource.
     */
    SHADER_CREATE,

    /**
     * Create the shader program backend resource.
     */
    PROGRAM_CREATE,

    /**
     * Create the texture backend resource.
     */
    TEXTURE_CREATE,

    /**
     * Load the texture in the backend.
     */
    TEXTURE_LOAD,

    /**
     * Create the indices backend resource.
     */
    INDICES_CREATE,

    /**
     * Load the indices in the backend.
     */
    INDICES_LOAD,

    /**
     * Create interleaved vertex buffer backend resource.
     */
    INTERLEAVED_BUFFER_CREATE,

    /**
     * Load the buffer in the backend.
     */
    INTERLEAVED_BUFFER_LOAD,

    /**
     * Create vertex buffer backend resource.
     */
    VERTEX_BUFFER_CREATE,

    /**
     * Load vertex buffer in the backend.
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
}

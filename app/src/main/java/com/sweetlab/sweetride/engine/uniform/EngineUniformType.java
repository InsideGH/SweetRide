package com.sweetlab.sweetride.engine.uniform;

/**
 * Type of engine uniforms.
 */
public enum EngineUniformType {
    /**
     * A nodes model transform.
     */
    MODEL,

    /**
     * An aggregation of the nodes model transform with all parent node transforms.
     */
    WORLD,

    /**
     * A nodes camera view transform.
     */
    VIEW,

    /**
     * A nodes cameras frustrum transform.
     */
    PROJECTION,

    /**
     * An aggregation of the nodes world and camera view transform.
     */
    WORLD_VIEW,

    /**
     * An aggregation of the nodes world and camera view and frustrum transform.
     */
    WORLD_VIEW_PROJECTION
}

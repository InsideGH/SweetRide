package com.sweetlab.sweetride.resource;

import com.sweetlab.sweetride.context.BackendContext;

/**
 * A backend texture resource.
 */
public interface TextureResource extends Resource {
    /**
     * Invalid texture id.
     */
    int INVALID_TEXTURE_ID = -1;

    /**
     * Create this texture resource.
     *
     * @param context Backend context.
     */
    void create(BackendContext context);

    /**
     * True if this texture has been created.
     *
     * @return True if created.
     */
    boolean isCreated();

    /**
     * Get the generated texture name/id.
     *
     * @return The texture name/id.
     */
    int getTextureId();
}

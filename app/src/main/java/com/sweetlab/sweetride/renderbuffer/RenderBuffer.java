package com.sweetlab.sweetride.renderbuffer;

import com.sweetlab.sweetride.DebugOptions;
import com.sweetlab.sweetride.action.Action;
import com.sweetlab.sweetride.action.GlobalActionId;
import com.sweetlab.sweetride.action.ActionThread;
import com.sweetlab.sweetride.action.NoHandleNotifier;
import com.sweetlab.sweetride.context.AttachmentType;
import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.context.DepthAttachment;
import com.sweetlab.sweetride.context.ResourceManager;
import com.sweetlab.sweetride.context.StencilAttachment;
import com.sweetlab.sweetride.resource.Resource;

/**
 * A render buffer is a storage for a single image that can be attached to a frame buffer to be used
 * as a render destination or source of reading. Possible attachment points to a frame buffer are
 * depth and stencil.
 */
public class RenderBuffer extends NoHandleNotifier<GlobalActionId> implements Resource, DepthAttachment, StencilAttachment {
    /**
     * The internal format (GL_RGB565, GL_RGBA4, GL_RGB5_A1, GL_DEPTH_COMPONENT16, GL_STENCIL_INDEX8).
     */
    private final int mFormat;

    /**
     * The width in pixels.
     */
    private final int mWidth;

    /**
     * The height in pixels.
     */
    private final int mHeight;

    /**
     * The render buffer id.
     */
    private int mBufferId;

    /**
     * Constructor.
     *
     * @param format Must be one of GL_RGB565, GL_RGBA4, GL_RGB5_A1, GL_DEPTH_COMPONENT16, GL_STENCIL_INDEX8
     * @param width  Width of the render buffer in pixels.
     * @param height Height if the render buffer in pixels.
     */
    public RenderBuffer(int format, int width, int height) {
        mFormat = format;
        mWidth = width;
        mHeight = height;
        addAction(new Action<>(this, GlobalActionId.RENDER_BUFFER_CREATE, ActionThread.GL));
    }

    @Override
    public void create(BackendContext context) {
        if (DebugOptions.DEBUG_RENDER_BUFFER) {
            int max = context.getCapabilities().getMaxRenderBufferSize();
            if (mWidth > max || mHeight > max) {
                throw new RuntimeException("Render buffer dimensions are to large, w = " + mWidth + " h = " + mHeight + " where max is " + max);
            }
        }
        mBufferId = context.getResourceManager().generateRenderBuffer();
    }

    @Override
    public boolean isCreated() {
        return mBufferId != ResourceManager.INVALID_RENDER_BUFFER_ID;
    }

    @Override
    public void delete(BackendContext context) {
        context.getResourceManager().deleteRenderBuffer(mBufferId);
    }

    @Override
    public int getId() {
        return mBufferId;
    }

    @Override
    public AttachmentType getAttachmentType() {
        return AttachmentType.RENDER_BUFFER;
    }

    @Override
    public boolean handleAction(BackendContext context, Action<GlobalActionId> action) {
        switch (action.getType()) {
            case RENDER_BUFFER_CREATE:
                create(context);
                return true;
        }
        return false;
    }

    /**
     * Get the internal format.
     *
     * @return The format, one of GL_RGB565, GL_RGBA4, GL_RGB5_A1, GL_DEPTH_COMPONENT16, GL_STENCIL_INDEX8.
     */
    public int getFormat() {
        return mFormat;
    }

    /**
     * Get the width.
     *
     * @return The width in pixels.
     */
    public int getWidth() {
        return mWidth;
    }

    /**
     * Get the height.
     *
     * @return The height in pixels.
     */
    public int getHeight() {
        return mHeight;
    }
}

package com.sweetlab.sweetride.framebuffer;

import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.context.FrameBufferTarget;
import com.sweetlab.sweetride.resource.FrameBufferResource;

/**
 * Represents a frame buffer resource.
 */
public class FrameBuffer implements FrameBufferResource {
    /**
     * The frame buffer name/id.
     */
    private int mBufferId;

    @Override
    public void create(BackendContext context) {
        mBufferId = context.getResourceManager().generateFrameBuffer();
    }

    @Override
    public boolean isCreated() {
        return mBufferId > FrameBufferTarget.WINDOW_FRAMEBUFFER;
    }

    @Override
    public int getId() {
        return mBufferId;
    }

    @Override
    public void release(BackendContext context) {
        context.getResourceManager().deleteFrameBuffer(mBufferId);
    }
}

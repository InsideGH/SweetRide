package com.sweetlab.sweetride.framebuffer;

import com.sweetlab.sweetride.action.Action;
import com.sweetlab.sweetride.action.GlobalActionId;
import com.sweetlab.sweetride.action.ActionThread;
import com.sweetlab.sweetride.action.NoHandleNotifier;
import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.context.FrameBufferTarget;
import com.sweetlab.sweetride.resource.Resource;

/**
 * Represents a frame buffer resource.
 */
public class FrameBuffer extends NoHandleNotifier<GlobalActionId> implements Resource {
    /**
     * The frame buffer name/id.
     */
    private int mBufferId;

    /**
     * Constructor.
     */
    public FrameBuffer() {
        addAction(new Action<>(this, GlobalActionId.FRAME_BUFFER_CREATE, ActionThread.GL));
    }

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
    public void delete(BackendContext context) {
        context.getResourceManager().deleteFrameBuffer(mBufferId);
    }

    @Override
    public boolean handleAction(BackendContext context, Action<GlobalActionId> action) {
        switch (action.getType()) {
            case FRAME_BUFFER_CREATE:
                create(context);
                return true;
        }
        return false;
    }
}

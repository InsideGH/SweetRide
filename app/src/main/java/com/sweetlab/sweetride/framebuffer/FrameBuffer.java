package com.sweetlab.sweetride.framebuffer;

import com.sweetlab.sweetride.action.Action;
import com.sweetlab.sweetride.action.ActionId;
import com.sweetlab.sweetride.action.ActionNotifier;
import com.sweetlab.sweetride.action.HandleThread;
import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.context.FrameBufferTarget;
import com.sweetlab.sweetride.resource.Resource;

/**
 * Represents a frame buffer resource.
 */
public class FrameBuffer extends ActionNotifier implements Resource {
    /**
     * The frame buffer name/id.
     */
    private int mBufferId;

    public FrameBuffer() {
        addAction(new Action(this, ActionId.FRAME_BUFFER_CREATE, HandleThread.GL));
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
    public void handleAction(Action action) {
        throw new RuntimeException("wtf");
    }

    @Override
    public void handleAction(BackendContext context, Action action) {
        switch (action.getType()) {
            case FRAME_BUFFER_CREATE:
                create(context);
                break;
        }
    }
}

package com.sweetlab.sweetride.engine.frame;

import com.sweetlab.sweetride.UserApplication;
import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.engine.frame.render.RenderFrame;
import com.sweetlab.sweetride.engine.frame.update.UpdateFrame;
import com.sweetlab.sweetride.node.Node;
import com.sweetlab.sweetride.pool.RenderTaskPool;

import java.util.ArrayDeque;

/**
 * A single complete frame. Call update from main thread and render from GL thread. Has an internal
 * queue that is shared between update and render.
 */
public class Frame {
    /**
     * The render queue. Update phase adds to queue and render phase removes from queue.
     */
    private final ArrayDeque<RenderTask> mRenderQueue = new ArrayDeque<>();

    /**
     * The frame update.
     */
    private final UpdateFrame mUpdate;

    /**
     * The frame render.
     */
    private final RenderFrame mRender;

    /**
     * Constructor.
     */
    public Frame() {
        RenderTaskPool taskPool = new RenderTaskPool();
        mUpdate = new UpdateFrame(taskPool);
        mRender = new RenderFrame(taskPool);
    }

    /**
     * Make a frame update on main thread. Call from main thread.
     *
     * @param application Application to update.
     * @param root        Root node of scene graph to start update on.
     */
    public void update(UserApplication application, Node root) {
        mUpdate.update(application, root, mRenderQueue);
    }

    /**
     * Render the frame on GL thread. Call from GL thread.
     *
     * @param context The backend context.
     */
    public void render(BackendContext context) {
        mRender.render(context, mRenderQueue);
    }
}

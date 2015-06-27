package com.sweetlab.sweetride.pool;

import com.sweetlab.sweetride.engine.frame.RenderTask;

import java.util.Stack;

/**
 * A pool of render task objects.
 */
public class RenderTaskPool implements Pool<RenderTask> {
    /**
     * The pool content.
     */
    private final Stack<RenderTask> mStack = new Stack<>();

    @Override
    public synchronized RenderTask get() {
        if (mStack.isEmpty()) {
            return new RenderTask();
        }
        return mStack.pop();
    }

    @Override
    public synchronized void put(RenderTask object) {
        object.reset();
        mStack.push(object);
    }
}

package com.sweetlab.sweetride.pool;

import com.sweetlab.sweetride.engine.GraphContent;

import java.util.Stack;

/**
 * A pool of graph content objects.
 */
public class GraphContentPool implements Pool<GraphContent> {
    /**
     * The pool content.
     */
    private final Stack<GraphContent> mStack = new Stack<>();

    @Override
    public synchronized GraphContent get() {
        if (mStack.isEmpty()) {
            return new GraphContent();
        }
        return mStack.pop();
    }

    @Override
    public synchronized void put(GraphContent object) {
        object.reset();
        mStack.push(object);
    }
}

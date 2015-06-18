package com.sweetlab.sweetride.math;

import com.sweetlab.sweetride.pool.Pool;

import java.util.Stack;

/**
 * Vec3 pool.
 */
public class Vec3Pool implements Pool<Vec3> {

    private Stack<Vec3> mStack = new Stack<>();

    @Override
    public Vec3 get() {
        if (mStack.isEmpty()) {
            return new Vec3();
        }
        return mStack.pop();
    }

    @Override
    public void put(Vec3 object) {
        object.reset();
        mStack.push(object);
    }
}

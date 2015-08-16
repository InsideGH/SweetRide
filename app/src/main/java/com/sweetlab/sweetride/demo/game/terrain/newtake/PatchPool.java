package com.sweetlab.sweetride.demo.game.terrain.newtake;

import com.sweetlab.sweetride.geometry.Geometry;
import com.sweetlab.sweetride.pool.Pool;

import java.util.Stack;

/**
 * A patch pool.
 */
public class PatchPool implements Pool<Patch> {
    /**
     * The pool stack.
     */
    private final Stack<Patch> mStack = new Stack<>();

    /**
     * The geometry factory.
     */
    private final BaseGeometryFactory mFactory;

    /**
     * Constructor.
     *
     * @param size  Patch size.
     * @param width Patch width in gl world space.
     * @param depth Patch depth in gl world space.
     */
    public PatchPool(int size, float width, float depth) {
        mFactory = new BaseGeometryFactory(size, width, depth);
    }

    @Override
    public Patch get() {
        if (mStack.isEmpty()) {
            Geometry baseGeometry = mFactory.createBaseGeometry();
            return new Patch(baseGeometry);
        }
        return mStack.pop();
    }

    @Override
    public void put(Patch object) {
        object.reset();
        mStack.push(object);
    }
}

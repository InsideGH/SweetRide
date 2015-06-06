package com.sweetlab.sweetride.pool;

import com.sweetlab.sweetride.engine.Reusable;

/**
 * Pool of reusable objects.
 *
 * @param <T> Type of object.
 */
public interface Pool<T extends Reusable> {
    /**
     * Get from pool.
     *
     * @return Object from pool. If empty, new object is created.
     */
    T get();

    /**
     * Put back into pool. Object will be reset.
     *
     * @param object Object to put back.
     */
    void put(T object);
}

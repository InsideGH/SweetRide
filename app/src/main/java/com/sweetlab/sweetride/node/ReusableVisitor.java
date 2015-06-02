package com.sweetlab.sweetride.node;

/**
 * A node visitor that provides a way to clear previous results.
 *
 * @param <T> The result
 */
public interface ReusableVisitor<T> extends ResultVisitor<T> {
    /**
     * Clear previous result.
     */
    void clearResult();
}

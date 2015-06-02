package com.sweetlab.sweetride.node;

/**
 * A node visitor that provides a way to get result when finished visiting.
 *
 * @param <T> The result
 */
public interface ResultVisitor<T> extends NodeVisitor {
    T getResult();
}

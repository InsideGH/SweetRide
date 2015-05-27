package com.sweetlab.sweetride.context;

import android.opengl.GLES20;

import com.sweetlab.sweetride.DebugOptions;
import com.sweetlab.sweetride.resource.BufferResource;
import com.sweetlab.sweetride.util.Util;

import java.nio.Buffer;

/**
 * The element target.
 */
public class ElementTarget {
    /**
     * The element target.
     */
    private static final int TARGET = GLES20.GL_ELEMENT_ARRAY_BUFFER;

    /**
     * The array target buffer binding.
     */
    private static final int BINDING = GLES20.GL_ELEMENT_ARRAY_BUFFER_BINDING;

    /**
     * Buffer to read GL information.
     */
    private int[] mReadBuffer = new int[4];

    /**
     * The backend context.
     */
    private final BackendContext mContext;

    /**
     * Constructor.
     *
     * @param backendContext The backend context.
     */
    public ElementTarget(BackendContext backendContext) {
        mContext = backendContext;
    }

    /**
     * Load indices buffer to gpu.
     *
     * @param indicesBuffer Indices buffer.
     */
    public void load(BufferResource indicesBuffer) {
        if (DebugOptions.DEBUG_ELEMENT_TARGET) {
            if (!indicesBuffer.isCreated()) {
                throw new RuntimeException("Trying to load indices data that has not been created");
            }
        }
        final int bufferId = indicesBuffer.getBufferId();
        if (!isBufferBound(bufferId)) {
            GLES20.glBindBuffer(TARGET, bufferId);
        }

        final Buffer data = indicesBuffer.getData();
        final int totalByteCount = indicesBuffer.getTotalByteCount();
        final int bufferUsage = indicesBuffer.getBufferUsage();

        GLES20.glBufferData(TARGET, totalByteCount, data, bufferUsage);
    }

    /**
     * Enable indices buffer to elements target.
     *
     * @param indicesBuffer Indices buffer.
     */
    public void enableElements(BufferResource indicesBuffer) {
        /**
         * Check if buffer needs to be bound.
         */
        final int bufferId = indicesBuffer.getBufferId();
        if (!isBufferBound(bufferId)) {
            GLES20.glBindBuffer(TARGET, bufferId);
        }
    }

    /**
     * Disable indices buffer from elements target.
     */
    public void disableElements() {
        if (!isAnyBufferBound()) {
            throw new RuntimeException("Can't disable elements with not bound buffer object to target GL_ELEMENT_ARRAY_BUFFER");
        }
        unBindBuffer();
    }

    /**
     * Draw using elements target.
     *
     * @param mode   Drawing mode.
     * @param offset Offset into the indices.
     * @param count  Number of indices to draw with.
     */
    public void draw(int mode, int offset, int count) {
        if (!isAnyBufferBound()) {
            throw new RuntimeException("Can't draw using elements without elements buffer object bound");
        }
        if (count * Util.BYTES_PER_SHORT > getBoundBufferSize()) {
            throw new RuntimeException("Trying to draw with element count larger than available count = " + count + " buffer size = " + getBoundBufferSize());
        }
        GLES20.glDrawElements(mode, count, GLES20.GL_UNSIGNED_SHORT, offset);
    }

    /**
     * Get if target has any buffer bound.
     *
     * @return True if target has buffer bound.
     */
    private boolean isAnyBufferBound() {
        GLES20.glGetIntegerv(BINDING, mReadBuffer, 0);
        return mReadBuffer[0] != 0;
    }

    /**
     * Remove any binding to target.
     */
    private void unBindBuffer() {
        GLES20.glBindBuffer(TARGET, 0);
    }

    /**
     * Get bound buffer size.
     *
     * @return The bound buffer size.
     */
    private int getBoundBufferSize() {
        if (!isAnyBufferBound()) {
            throw new RuntimeException("Can't read buffer size when no buffer object is bound to target GL_ELEMENT_ARRAY_BUFFER");
        }
        GLES20.glGetBufferParameteriv(TARGET, GLES20.GL_BUFFER_SIZE, mReadBuffer, 0);
        return mReadBuffer[0];
    }

    /**
     * Get if buffer id is bound to target.
     *
     * @param id The buffer id.
     * @return True if bound.
     */
    private boolean isBufferBound(int id) {
        if (id > 0) {
            GLES20.glGetIntegerv(BINDING, mReadBuffer, 0);
            return mReadBuffer[0] == id;
        }
        return false;
    }
}

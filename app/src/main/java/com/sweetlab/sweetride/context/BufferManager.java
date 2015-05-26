package com.sweetlab.sweetride.context;

import android.opengl.GLES20;

/**
 * Managing buffers.
 */
public class BufferManager {

    /**
     * The backend context.
     */
    private final BackendContext mContext;

    /**
     * Used during buffer generation.
     */
    private int[] mBuf = new int[1];

    public BufferManager(BackendContext backendContext) {
        mContext = backendContext;
    }

    /**
     * Generate a buffer name/id.
     *
     * @return The generated buffer.
     */
    public int generateBuffer() {
        GLES20.glGenBuffers(1, mBuf, 0);
        return mBuf[0];
    }

    /**
     * Delete a previously generated name/id.
     *
     * @param id Buffer id to delete.
     */
    public void deleteBuffer(int id) {
        mBuf[0] = id;
        GLES20.glDeleteBuffers(1, mBuf, 0);
    }
}

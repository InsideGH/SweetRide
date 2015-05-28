package com.sweetlab.sweetride.context;

import android.opengl.GLES20;

import com.sweetlab.sweetride.renderbuffer.RenderBuffer;

/**
 * Render buffer target.
 */
public class RenderBufferTarget {
    /**
     * Binding with this value unbinds any previously bound render buffer.
     */
    private static final int DISABLE_TARGET = 0;
    /**
     * The render buffer target.
     */
    private static final int TARGET = GLES20.GL_RENDERBUFFER;

    /**
     * The render buffer binding.
     */
    private static final int BINDING = GLES20.GL_RENDERBUFFER_BINDING;

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
     * @param context The backend context.
     */
    public RenderBufferTarget(BackendContext context) {
        mContext = context;
    }

    /**
     * Enable render buffer.
     *
     * @param buffer Render buffer.
     */
    public void enable(RenderBuffer buffer) {
        int id = buffer.getId();
        if (!isRenderBufferBound(id)) {
            GLES20.glBindRenderbuffer(TARGET, id);
        }

        int format = buffer.getFormat();
        int width = buffer.getWidth();
        int height = buffer.getHeight();
        GLES20.glRenderbufferStorage(TARGET, format, width, height);
    }

    /**
     * Disable the render buffer target.
     */
    public void disable() {
        GLES20.glBindRenderbuffer(TARGET, DISABLE_TARGET);
    }

    /**
     * Get if render buffer id is bound to target.
     *
     * @param id The render buffer id.
     * @return True if bound.
     */
    public boolean isRenderBufferBound(int id) {
        if (id > 0) {
            GLES20.glGetIntegerv(BINDING, mReadBuffer, 0);
            return mReadBuffer[0] == id;
        }
        return false;
    }
}

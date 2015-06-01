package com.sweetlab.sweetride.context;

import android.opengl.GLES20;

import com.sweetlab.sweetride.framebuffer.FrameBuffer;

/**
 * The frame buffer target.
 */
public class FrameBufferTarget {
    /**
     * The frame buffer id reserved by the window system.
     */
    public static final int WINDOW_FRAMEBUFFER = 0;

    /**
     * The frame buffer target.
     */
    private static final int TARGET = GLES20.GL_FRAMEBUFFER;

    /**
     * The frame buffer binding.
     */
    private static final int BINDING = GLES20.GL_FRAMEBUFFER_BINDING;

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
    public FrameBufferTarget(BackendContext context) {
        mContext = context;
    }

    /**
     * Use the window frame buffer.
     */
    public void useWindowFrameBuffer() {
        GLES20.glBindFramebuffer(TARGET, WINDOW_FRAMEBUFFER);
    }

    /**
     * Use specific frame buffer.
     *
     * @param buffer The frame buffer.
     */
    public void useFrameBuffer(FrameBuffer buffer) {
        int id = buffer.getId();
        if (!isFrameBufferBound(id)) {
            GLES20.glBindFramebuffer(TARGET, id);
        }
    }

    /**
     * Set the color attachment.
     *
     * @param attachment The color attachment.
     */
    public void setColorAttachment(ColorAttachment attachment) {
        GLES20.glFramebufferTexture2D(TARGET, GLES20.GL_COLOR_ATTACHMENT0, attachment.getAttachmentType().getGlType(), attachment.getId(), 0);
    }

    /**
     * Set the depth attachment.
     *
     * @param attachment The depth attachment.
     */
    public void setDepthAttachment(DepthAttachment attachment) {
        GLES20.glFramebufferRenderbuffer(TARGET, GLES20.GL_DEPTH_ATTACHMENT, attachment.getAttachmentType().getGlType(), attachment.getId());
    }

    /**
     * Check for frame buffer completeness.
     *
     * @return True if complete.
     */
    public boolean checkIfComplete() {
        int status = GLES20.glCheckFramebufferStatus(TARGET);
        return status == GLES20.GL_FRAMEBUFFER_COMPLETE;
    }

    /**
     * Get if frame buffer id is bound to target.
     *
     * @param id The frame buffer id.
     * @return True if bound.
     */
    private boolean isFrameBufferBound(int id) {
        if (id > 0) {
            GLES20.glGetIntegerv(BINDING, mReadBuffer, 0);
            return mReadBuffer[0] == id;
        }
        return false;
    }
}

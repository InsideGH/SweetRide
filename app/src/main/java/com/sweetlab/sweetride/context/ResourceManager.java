package com.sweetlab.sweetride.context;

import android.opengl.GLES20;

import com.sweetlab.sweetride.DebugOptions;

/**
 * Managing resources.
 */
public class ResourceManager {
    /**
     * Considered an invalid render buffer id.
     */
    public static int INVALID_RENDER_BUFFER_ID = 0;

    /**
     * Invalid program id.
     */
    public static final int INVALID_PROGRAM_ID = 0;

    /**
     * Invalid texture id.
     */
    public static int INVALID_TEXTURE_ID = -1;

    /**
     * Invalid shader id.
     */
    public static final int INVALID_SHADER_ID = 0;

    /**
     * Invalid buffer id.
     */
    public static int INVALID_BUFFER_ID = -1;

    /**
     * The backend context.
     */
    private final BackendContext mContext;

    /**
     * Used during buffer generation.
     */
    private int[] mBuf = new int[1];

    public ResourceManager(BackendContext backendContext) {
        mContext = backendContext;
    }

    /**
     * Create a shader.
     *
     * @param type Either GLES20.GL_VERTEX_SHADER or GLES20.GL_FRAGMENT_SHADER
     * @return The created shader.
     */
    public int createShader(int type) {
        return GLES20.glCreateShader(type);
    }

    /**
     * Delete shader.
     *
     * @param id The shader id to delete.
     */
    public void deleteShader(int id) {
        GLES20.glDeleteShader(id);
    }

    /**
     * Create a shader program.
     *
     * @return The shader program.
     */
    public int createProgram() {
        return GLES20.glCreateProgram();
    }

    /**
     * Delete shader program.
     *
     * @param id The shader program.
     */
    public void deleteProgram(int id) {
        GLES20.glDeleteProgram(id);
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
     * Delete a previously generated buffer.
     *
     * @param id Buffer id to delete.
     */
    public void deleteBuffer(int id) {
        mBuf[0] = id;
        GLES20.glDeleteBuffers(1, mBuf, 0);
    }

    /**
     * Generate a texture name/id.
     *
     * @return The generated texture.
     */
    public int generateTexture() {
        GLES20.glGenTextures(1, mBuf, 0);
        return mBuf[0];
    }

    /**
     * Delete a previously generated texture.
     *
     * @param id Texture to delete.
     */
    public void deleteTexture(int id) {
        mBuf[0] = id;
        GLES20.glDeleteTextures(1, mBuf, 0);
    }

    /**
     * Generate a frame buffer name/id.
     *
     * @return The generated frame buffer.
     */
    public int generateFrameBuffer() {
        GLES20.glGenFramebuffers(1, mBuf, 0);
        return mBuf[0];
    }

    /**
     * Delete a previously generated frame buffer.
     *
     * @param id Frame buffer to delete.
     */
    public void deleteFrameBuffer(int id) {
        mBuf[0] = id;
        GLES20.glDeleteFramebuffers(1, mBuf, 0);
    }

    /**
     * Generate a render buffer name/id.
     *
     * @return The generated render buffer.
     */
    public int generateRenderBuffer() {
        GLES20.glGenRenderbuffers(1, mBuf, 0);
        if (DebugOptions.DEBUG_RESOURCE_MANAGER) {
            if (mBuf[0] == INVALID_RENDER_BUFFER_ID) {
                throw new RuntimeException("Generated an invalid render buffer id " + mBuf[0]);
            }
        }
        return mBuf[0];
    }

    /**
     * Delete a previously generated render buffer.
     *
     * @param id Render buffer to delete.
     */
    public void deleteRenderBuffer(int id) {
        mBuf[0] = id;
        GLES20.glDeleteRenderbuffers(1, mBuf, 0);
    }
}

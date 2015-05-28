package com.sweetlab.sweetride.context;

import android.opengl.GLES20;

import com.sweetlab.sweetride.shader.BaseShader;
import com.sweetlab.sweetride.shader.FragmentShader;
import com.sweetlab.sweetride.shader.ShaderProgram;
import com.sweetlab.sweetride.shader.VertexShader;

/**
 * Managing buffers.
 */
public class ResourceManager {
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
     * Release shader.
     *
     * @param shader The shader to release.
     */
    public void releaseShader(BaseShader shader) {
        GLES20.glDeleteShader(shader.getId());
    }

    /**
     * Release shader program. Also releases the underlying vertex and fragment shader.
     *
     * @param shaderProgram The shader program.
     */
    public void releaseProgram(ShaderProgram shaderProgram) {
        VertexShader vertexShader = shaderProgram.getVertexShader();
        if (vertexShader != null) {
            vertexShader.release(mContext);
        }
        FragmentShader fragmentShader = shaderProgram.getFragmentShader();
        if (fragmentShader != null) {
            fragmentShader.release(mContext);
        }
        GLES20.glDeleteProgram(shaderProgram.getId());
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

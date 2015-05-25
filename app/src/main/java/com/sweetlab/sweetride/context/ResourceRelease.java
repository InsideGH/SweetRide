package com.sweetlab.sweetride.context;

import android.opengl.GLES20;

import com.sweetlab.sweetride.shader.BaseShader;
import com.sweetlab.sweetride.shader.FragmentShader;
import com.sweetlab.sweetride.shader.ShaderProgram;
import com.sweetlab.sweetride.shader.VertexShader;

/**
 * Resource release.
 */
public class ResourceRelease {
    /**
     * The backend context.
     */
    private final BackendContext mContext;

    /**
     * Constructor.
     *
     * @param context Backend context.
     */
    public ResourceRelease(BackendContext context) {
        mContext = context;
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
}

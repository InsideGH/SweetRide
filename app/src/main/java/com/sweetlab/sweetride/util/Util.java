package com.sweetlab.sweetride.util;

import android.opengl.GLES20;
import android.util.Log;

/**
 * Utility class.
 */
public class Util {

    /**
     * Check for any GL errors.
     *
     * @return True if any errors.
     */
    public static boolean hasGlError() {
        int error;
        boolean foundError = false;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            foundError = true;
            Log.e("Peter100", "glError " + error);
        }
        return foundError;
    }
}

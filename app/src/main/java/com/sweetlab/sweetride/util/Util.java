package com.sweetlab.sweetride.util;

import android.opengl.GLES20;
import android.util.Log;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Utility class.
 */
public class Util {
    public static final int BYTES_PER_FLOAT = Float.SIZE / 8;
    public static final int BYTES_PER_SHORT = Short.SIZE / 8;
    public static final int BYTES_PER_INT = Integer.SIZE / 8;

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

    /**
     * Create a byte buffer of specified size.
     *
     * @param byteSize The byte size.
     * @return A byte buffer.
     */
    public static ByteBuffer allocByteBuffer(int byteSize) {
        ByteBuffer bb = ByteBuffer.allocateDirect(byteSize);
        bb.order(ByteOrder.nativeOrder());
        return bb;
    }

    /**
     * Create a buffer from data.
     *
     * @param data      Data to put in buffer.
     * @param byteCount Number of bytes to put.
     * @return The buffer.
     */
    public static Buffer createBuffer(float[] data, int byteCount) {
        FloatBuffer floatBuffer = allocByteBuffer(byteCount).asFloatBuffer();
        floatBuffer.clear();
        floatBuffer.put(data);
        floatBuffer.rewind();
        return floatBuffer;
    }
}

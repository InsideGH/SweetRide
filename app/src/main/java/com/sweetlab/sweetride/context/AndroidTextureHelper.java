package com.sweetlab.sweetride.context;

import android.graphics.Bitmap;
import android.opengl.GLES20;

/**
 * Helper class to bridge between Android bitmap and GL format and type.
 */
public class AndroidTextureHelper {
    /**
     * Get the GL texture texel format from bitmap config.
     *
     * @param bitmap Bitmap to check.
     * @return GL texture format.
     */
    public static int getTexelFormat(Bitmap bitmap) {
        /**
         * GLES20 formats.
         * GLES20.GL_RGBA;
         * GLES20.GL_RGB;
         * GLES20.GL_LUMINANCE_ALPHA;
         * GLES20.GL_LUMINANCE;
         * GLES20.GL_ALPHA;
         */
        Bitmap.Config config = bitmap.getConfig();
        switch (config) {
            case ARGB_8888:
                return GLES20.GL_RGBA;
            case RGB_565:
                return GLES20.GL_RGB;
            case ALPHA_8:
            case ARGB_4444:
                throw new RuntimeException("Unsupported bitmap config type in getTexelFormat" + config);
            default:
                throw new RuntimeException("Unknown bitmap config type getTexelFormat " + config);
        }
    }

    /**
     * Get the GL texture texel type from bitmap config.
     *
     * @param bitmap Bitmap to check.
     * @return
     */
    public static int getGLTexelType(Bitmap bitmap) {
        /**
         * GLES20 types
         * GLES20.GL_UNSIGNED_BYTE;
         * GLES20.GL_UNSIGNED_SHORT_4_4_4_4;
         * GLES20.GL_UNSIGNED_SHORT_5_5_5_1;
         * GLES20.GL_UNSIGNED_SHORT_5_6_5;
         */
        Bitmap.Config config = bitmap.getConfig();
        switch (config) {
            case ARGB_8888:
                return GLES20.GL_UNSIGNED_BYTE;
            case RGB_565:
                return GLES20.GL_UNSIGNED_SHORT_5_6_5;
            case ALPHA_8:
            case ARGB_4444:
                throw new RuntimeException("Unsupported bitmap config type getTexelType" + config);
            default:
                throw new RuntimeException("Unknown bitmap config type getTexelType" + config);
        }
    }
}

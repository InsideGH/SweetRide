package com.sweetlab.sweetride.context;

import android.opengl.GLES20;

/**
 * Helper to collect GLES20 specifics in one place.
 */
public class GLES20Helper {
    /**
     * All static.
     */
    private GLES20Helper() {
    }

    /**
     * Helper to get readable type.
     *
     * @param type GLES20 type.
     * @return Readable type.
     */
    public static String getReadableType(int type) {
        switch (type) {
            case GLES20.GL_FLOAT_VEC2:
                return "vec2";
            case GLES20.GL_FLOAT_VEC3:
                return "vec3";
            case GLES20.GL_FLOAT_VEC4:
                return "vec4";
            case GLES20.GL_FLOAT:
                return "float";
            case GLES20.GL_SAMPLER_2D:
                return "sampler2d";
            case GLES20.GL_FLOAT_MAT3:
                return "matrix33";
            case GLES20.GL_FLOAT_MAT4:
                return "matrix44";
            default:
                throw new RuntimeException("Can't figure out readable from type " + type);
        }
    }

    /**
     * Get the element count based on GLES20 type.
     *
     * @param type GLES20 type.
     * @return Number of elements.
     */
    public static int getTypeCount(int type) {
        int size = -1;
        switch (type) {
            case GLES20.GL_FLOAT_VEC2:
                size = 2;
                break;
            case GLES20.GL_FLOAT_VEC3:
                size = 3;
                break;
            case GLES20.GL_FLOAT_VEC4:
                size = 4;
                break;
            case GLES20.GL_FLOAT:
                size = 1;
                break;
            case GLES20.GL_SAMPLER_2D:
                break;
            case GLES20.GL_FLOAT_MAT3:
                size = 9;
                break;
            case GLES20.GL_FLOAT_MAT4:
                size = 16;
                break;
            default:
                throw new RuntimeException("Can't figure out size from type " + type);
        }
        return size;
    }
}

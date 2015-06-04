package com.sweetlab.sweetride.context;

import android.opengl.GLES20;

/**
 * Helper to collect GL specifics in one place.
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
     * @param type GL type (GL_FLOAT, GL_FLOAT_VEC2, GL_FLOAT_VEC3, GL_FLOAT_VEC4, GL_FLOAT_MAT2, GL_FLOAT_MAT3, or GL_FLOAT_MAT4).
     * @return Readable type.
     */
    public static String getReadableType(int type) {
        switch (type) {
            case GLES20.GL_INT_VEC2:
                return "int_vec2";
            case GLES20.GL_INT_VEC3:
                return "int_vec3";
            case GLES20.GL_INT_VEC4:
                return "int_vec4";
            case GLES20.GL_INT:
                return "int";
            case GLES20.GL_FLOAT_VEC2:
                return "float_vec2";
            case GLES20.GL_FLOAT_VEC3:
                return "float_vec3";
            case GLES20.GL_FLOAT_VEC4:
                return "float_vec4";
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
     * Get the element count based on GL type.
     *
     * @param type GL type (GL_FLOAT, GL_FLOAT_VEC2, GL_FLOAT_VEC3, GL_FLOAT_VEC4, GL_FLOAT_MAT2, GL_FLOAT_MAT3, or GL_FLOAT_MAT4).
     * @return Number of elements.
     */
    public static int getTypeElementCount(int type) {
        switch (type) {
            case GLES20.GL_INT:
            case GLES20.GL_FLOAT:
            case GLES20.GL_SAMPLER_2D:
                return 1;
            case GLES20.GL_INT_VEC2:
            case GLES20.GL_FLOAT_VEC2:
                return 2;
            case GLES20.GL_INT_VEC3:
            case GLES20.GL_FLOAT_VEC3:
                return 3;
            case GLES20.GL_INT_VEC4:
            case GLES20.GL_FLOAT_VEC4:
                return 4;
            case GLES20.GL_FLOAT_MAT3:
                return 9;
            case GLES20.GL_FLOAT_MAT4:
                return 16;
            default:
                throw new RuntimeException("Can't figure out element count from type " + type);
        }
    }

    /**
     * Get type family.
     *
     * @param type GL_FLOAT, GL_FLOAT_VEC2, GL_FLOAT_VEC3, GL_FLOAT_VEC4, GL_FLOAT_MAT2, GL_FLOAT_MAT3, or GL_FLOAT_MAT4.
     * @return Type of family.
     */
    public static int getTypeFamily(int type) {
        switch (type) {
            case GLES20.GL_FLOAT:
            case GLES20.GL_FLOAT_VEC2:
            case GLES20.GL_FLOAT_VEC3:
            case GLES20.GL_FLOAT_VEC4:
            case GLES20.GL_FLOAT_MAT3:
            case GLES20.GL_FLOAT_MAT4:
                return GLES20.GL_FLOAT;
        }
        throw new RuntimeException("Can't figure out family type from type " + type);
    }

}

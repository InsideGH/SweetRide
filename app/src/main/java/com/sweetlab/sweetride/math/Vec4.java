package com.sweetlab.sweetride.math;

import android.annotation.SuppressLint;

/**
 * Old vec4 class that I have had around for a while. Should be cleaned up though.
 */
public class Vec4 {
    public float x;
    public float y;
    public float z;
    public float w;

    /**
     * Create vector with x, y, z components
     */
    public Vec4(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    /**
     * Create a vector with same x, y, z components as input
     *
     * @param
     */
    public Vec4(Vec4 from) {
        x = from.x;
        y = from.y;
        z = from.z;
        w = from.w;
    }

    /**
     * Return a new vector which is the negate of vec
     *
     * @param vec
     */
    public static Vec4 neg(Vec4 vec) {
        return new Vec4(vec).neg();
    }

    /**
     * Returns a new vector which is v1 + v2 vector
     *
     * @param
     * @return New vector
     */
    public static Vec4 add(final Vec4 v1, final Vec4 v2) {
        return new Vec4(v1).add(v2);
    }

    /**
     * Return a new vector which is v1 - v2 vector.
     *
     * @param
     * @return New vector
     */
    public static Vec4 sub(final Vec4 v1, final Vec4 v2) {
        return new Vec4(v1).sub(v2);
    }

    /**
     * Does dst = v1 -v2
     *
     * @param v1
     * @param v2
     * @param dst
     */
    public static void sub(final Vec4 v1, final Vec4 v2, Vec4 dst) {
        dst.x = v1.x - v2.x;
        dst.y = v1.y - v2.y;
        dst.z = v1.z - v2.z;
        dst.w = v1.w - v2.w;
    }

    /**
     * Returns a new vector which is scalar*vec
     *
     * @param vec
     * @param scalar
     * @return
     */
    public static Vec4 mult(Vec4 vec, float scalar) {
        return new Vec4(vec).mult(scalar);
    }

    /**
     * Returns a new vector which is vec divided by scalar
     *
     * @param vec
     * @param scalar
     * @return
     */
    public static Vec4 div(Vec4 vec, float scalar) {
        return new Vec4(vec).div(scalar);
    }

    /**
     * Set this vector.
     *
     * @param from
     */
    public void set(Vec4 from) {
        x = from.x;
        y = from.y;
        z = from.z;
        w = from.w;
    }

    /**
     * Set this vector
     *
     * @param x
     * @param y
     * @param z
     * @return
     */
    public Vec4 set(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        return this;
    }

    /**
     * Negates this vector
     *
     * @param
     * @return This
     */
    public Vec4 neg() {
        x = -x;
        y = -y;
        z = -z;
        w = -w;
        return this;
    }

    /**
     * This vector + v2 vector
     *
     * @param
     * @return This
     */
    public Vec4 add(final Vec4 v2) {
        this.x += v2.x;
        this.y += v2.y;
        this.z += v2.z;
        this.w += v2.w;
        return this;
    }

    /**
     * This vector - v2 vector
     *
     * @param
     * @return This
     */
    public Vec4 sub(final Vec4 v2) {
        this.x -= v2.x;
        this.y -= v2.y;
        this.z -= v2.z;
        this.w -= v2.w;
        return this;
    }

    /**
     * This vector multiplied with scalar
     *
     * @param
     * @return This
     */
    public Vec4 mult(float scalar) {
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
        this.w *= scalar;
        return this;
    }

    /**
     * This vector divided with scalar (no check for division by 0)
     *
     * @param
     * @return This
     */
    public Vec4 div(float scalar) {
        scalar = 1 / scalar;
        mult(scalar);
        return this;
    }

    /**
     * Length of vector squared
     *
     * @param
     * @return Length of this vector squared
     */
    public float lengthSq() {
        return x * x + y * y + z * z;
    }

    /**
     * Length of vector
     *
     * @param
     * @return Length of this vector
     */
    public float length() {
        return (float) Math.sqrt(lengthSq());
    }

    /**
     * Dot product between this and v2
     *
     * @param
     * @return Dot product
     */
    public float dot(final Vec4 v2) {
        return x * v2.x + y * v2.y + z * v2.z + w * v2.w;
    }

    /**
     * The angle in radians between vectors
     *
     * @param
     * @return Angle between this and v2 vector
     */
    public float angle(final Vec4 v2) {
        return (float) Math.acos(dot(v2) / (this.length() * v2.length()));
    }

    /**
     * This vector projected onto v2
     *
     * @param
     * @return New projected vector
     */
    public Vec4 project(final Vec4 v2) {
        float len = dot(v2) / v2.lengthSq();
        return new Vec4(v2).mult(len);
    }

    /**
     * Normalize this vector
     *
     * @param
     * @return This
     */
    public Vec4 norm() {
        float oneDivLen = 1 / length();
        x *= oneDivLen;
        y *= oneDivLen;
        z *= oneDivLen;
        w *= oneDivLen;
        return this;
    }

    /**
     * Return the distance between this point and v2 point
     *
     * @param v2
     * @return
     */
    public float distance(final Vec4 v2) {
        return Vec4.sub(this, v2).length();
    }

    /**
     * Transform this vector. Matrix on left side and vector on right side. This
     * is OpenGL style.
     *
     * @param mat
     */
    public Vec4 transform(Matrix44 mat) {
        float xnew = x * mat.m[0] + y * mat.m[4] + z * mat.m[8] + mat.m[12];
        float ynew = x * mat.m[1] + y * mat.m[5] + z * mat.m[9] + mat.m[13];
        float znew = x * mat.m[2] + y * mat.m[6] + z * mat.m[10] + mat.m[14];
        float wnew = x * mat.m[3] + y * mat.m[7] + z * mat.m[11] + mat.m[15];

        x = xnew;
        y = ynew;
        z = znew;
        w = wnew;

        return this;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String toString() {
        return String.format("[% #06.2f % #06.2f % #06.2f % #06.2f]]", x, y, z, w);
    }
}

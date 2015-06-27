package com.sweetlab.sweetride.math;

import com.sweetlab.sweetride.pool.Reusable;

/**
 * Old vec3 class that I have had around for a while. Should be cleaned up though.
 */
public class Vec3 implements Reusable {
    public float x;
    public float y;
    public float z;

    /**
     * Create vector with x, y, z components
     */
    public Vec3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Create a vector with same x, y, z components as input.
     */
    public Vec3(final Vec3 from) {
        x = from.x;
        y = from.y;
        z = from.z;
    }

    public Vec3() {

    }

    /**
     * Compare this vector with other vector.
     *
     * @param other The other vector.
     * @return True of equal values.
     */
    public boolean equals(Vec3 other) {
        return Math.abs(x - other.x) < FloatUtil.EPS && Math.abs(y - other.y) < FloatUtil.EPS &&
                Math.abs(z - other.z) < FloatUtil.EPS;
    }

    /**
     * Return a new vector which is the negate of vec
     */
    public static Vec3 neg(Vec3 vec) {
        return new Vec3(vec).neg();
    }

    /**
     * Returns a new vector which is v1 + v2 vector
     */
    public static Vec3 add(final Vec3 v1, final Vec3 v2) {
        return new Vec3(v1).add(v2);
    }

    /**
     * Return a new vector which is v1 - v2 vector.
     */
    public static Vec3 sub(final Vec3 v1, final Vec3 v2) {
        return new Vec3(v1).sub(v2);
    }

    /**
     * Does dst = v1 -v2
     */
    public static void sub(final Vec3 v1, final Vec3 v2, Vec3 dst) {
        dst.x = v1.x - v2.x;
        dst.y = v1.y - v2.y;
        dst.z = v1.z - v2.z;
    }

    /**
     * Returns a new vector which is scalar*vec
     */
    public static Vec3 mult(Vec3 vec, float scalar) {
        return new Vec3(vec).mult(scalar);
    }

    /**
     * Returns a new vector which is vec divided by scalar
     */
    public static Vec3 div(Vec3 vec, float scalar) {
        return new Vec3(vec).div(scalar);
    }

    /**
     * Return a new normalised vector based on vec
     */
    public static Vec3 norm(Vec3 vec) {
        return new Vec3(vec).norm();
    }

    /**
     * Calculates the cross prod between v1 and v2 and stores result in dst
     */
    public static void cross(final Vec3 v1, final Vec3 v2, Vec3 dst) {
        dst.x = v1.y * v2.z - v1.z * v2.y;
        dst.y = v1.z * v2.x - v1.x * v2.z;
        dst.z = v1.x * v2.y - v1.y * v2.x;
    }

    /**
     * Returns a new transformed vector
     */
    public static Vec3 transform(Matrix44 mat, Vec3 vec) {
        return new Vec3(vec).transform(mat);
    }

    /**
     * Create 3 component vector from p1 to p2. Store vector into vec.
     *
     * @param p1  Vector start point.
     * @param p2  Vector end point.
     * @param vec Store result vector into this.
     */
    public static void createVecFromPoints(Vec3 p1, Vec3 p2, Vec3 vec) {
        vec.set(p2.x - p1.x, p2.y - p1.y, p2.z - p1.z);
    }

    /**
     * Create 4 component vector from p1 to p2. Store vector into vec. The fourth component
     * is not touched.
     *
     * @param p1  Vector start point.
     * @param p2  Vector end point.
     * @param vec Store result vector into this.
     */
    public static void createVecFromPoints(Vec4 p1, Vec4 p2, Vec3 vec) {
        vec.set(p2.x - p1.x, p2.y - p1.y, p2.z - p1.z);
    }

    public Vec3 set(Vec3 other) {
        x = other.x;
        y = other.y;
        z = other.z;
        return this;
    }

    /**
     * Set this vector
     */
    public Vec3 set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    /**
     * Negates this vector
     */
    public Vec3 neg() {
        x = -x;
        y = -y;
        z = -z;
        return this;
    }

    /**
     * This vector + v2 vector
     */
    public Vec3 add(final Vec3 v2) {
        this.x += v2.x;
        this.y += v2.y;
        this.z += v2.z;
        return this;
    }

    /**
     * This vector - v2 vector
     */
    public Vec3 sub(final Vec3 v2) {
        x -= v2.x;
        y -= v2.y;
        z -= v2.z;
        return this;
    }

    /**
     * This vector multiplied with scalar
     */
    public Vec3 mult(float scalar) {
        x *= scalar;
        y *= scalar;
        z *= scalar;
        return this;
    }

    /**
     * This vector divided with scalar (no check for division by 0)
     */
    public Vec3 div(float scalar) {
        scalar = 1 / scalar;
        mult(scalar);
        return this;
    }

    /**
     * Length of vector squared
     */
    public float lengthSq() {
        return x * x + y * y + z * z;
    }

    /**
     * Length of vector
     */
    public float length() {
        return (float) Math.sqrt(lengthSq());
    }

    /**
     * Dot product between this and v2
     */
    public float dot(final Vec3 v2) {
        return x * v2.x + y * v2.y + z * v2.z;
    }

    /**
     * The angle in radians between vectors
     */
    public float angle(final Vec3 v2) {
        return (float) Math.acos(dot(v2) / (this.length() * v2.length()));
    }

    /**
     * This vector projected onto v2. New vector returned.
     */
    public Vec3 project(final Vec3 v2) {
        float len = dot(v2) / v2.length();
        return new Vec3(v2).mult(len);
    }

    /**
     * Normalize this vector
     */
    public Vec3 norm() {
        float len = length();
        if (len > 0) {
            float oneDivLen = 1 / len;
            x *= oneDivLen;
            y *= oneDivLen;
            z *= oneDivLen;
        }
        return this;
    }

    /**
     * Returns a new vector which is the cross product between this and other
     */
    public Vec3 cross(final Vec3 other) {
        return new Vec3(y * other.z - z * other.y, z * other.x - x * other.z, x * other.y - y
                * other.x);

    }

    /**
     * Return the distance between this point and v2 point
     */
    public float distance(final Vec3 v2) {
        return Vec3.sub(this, v2).length();
    }

    /**
     * Transform this vector. Matrix on left side and vector on right side. This
     * is OpenGL style.
     */
    public Vec3 transform(Matrix44 mat) {
        float xNew = x * mat.m[0] + y * mat.m[4] + z * mat.m[8] + mat.m[12];
        float yNew = x * mat.m[1] + y * mat.m[5] + z * mat.m[9] + mat.m[13];
        float zNew = x * mat.m[2] + y * mat.m[6] + z * mat.m[10] + mat.m[14];

        x = xNew;
        y = yNew;
        z = zNew;

        return this;
    }

    /**
     * Transform this vector. Matrix on left side and vector on right side. This
     * is OpenGL style. Ignore translation part, just transform rotation.
     */
    public Vec3 transform33(Matrix44 mat) {
        float xNew = x * mat.m[0] + y * mat.m[4] + z * mat.m[8];
        float yNew = x * mat.m[1] + y * mat.m[5] + z * mat.m[9];
        float zNew = x * mat.m[2] + y * mat.m[6] + z * mat.m[10];

        x = xNew;
        y = yNew;
        z = zNew;

        return this;
    }

    /**
     * Transform this vector. Matrix on left side and vector on right side. This
     * is OpenGL style.
     */
    public Vec3 transform(Matrix33 mat) {
        float xNew = x * mat.m[0] + y * mat.m[3] + z * mat.m[6];
        float yNew = x * mat.m[1] + y * mat.m[4] + z * mat.m[7];
        float zNew = x * mat.m[2] + y * mat.m[5] + z * mat.m[8];

        x = xNew;
        y = yNew;
        z = zNew;

        return this;
    }

    @Override
    public String toString() {
        return String.format("[% #06.4f % #06.4f % #06.4f]", x, y, z);
    }

    @Override
    public void reset() {
        x = y = z = 0;
    }
}

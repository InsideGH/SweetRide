package com.sweetlab.sweetride.math;

/**
 * Old vec3 class that I have had around for a while. Should be cleaned up though.
 */
public class Vec3 {
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
     * Create a vector with same x, y, z components as input
     *
     * @param
     */
    public Vec3(final Vec3 from) {
        x = from.x;
        y = from.y;
        z = from.z;
    }

    public Vec3() {

    }

    /**
     * Return a new vector which is the negate of vec
     *
     * @param vec
     */
    public static Vec3 neg(Vec3 vec) {
        return new Vec3(vec).neg();
    }

    /**
     * Returns a new vector which is v1 + v2 vector
     *
     * @param
     * @return New vector
     */
    public static Vec3 add(final Vec3 v1, final Vec3 v2) {
        return new Vec3(v1).add(v2);
    }

    /**
     * Return a new vector which is v1 - v2 vector.
     *
     * @param
     * @return New vector
     */
    public static Vec3 sub(final Vec3 v1, final Vec3 v2) {
        return new Vec3(v1).sub(v2);
    }

    /**
     * Does dst = v1 -v2
     *
     * @param v1
     * @param v2
     * @param dst
     */
    public static void sub(final Vec3 v1, final Vec3 v2, Vec3 dst) {
        dst.x = v1.x - v2.x;
        dst.y = v1.y - v2.y;
        dst.z = v1.z - v2.z;
    }

    /**
     * Returns a new vector which is scalar*vec
     *
     * @param vec
     * @param scalar
     * @return
     */
    public static Vec3 mult(Vec3 vec, float scalar) {
        return new Vec3(vec).mult(scalar);
    }

    /**
     * Returns a new vector which is vec divided by scalar
     *
     * @param vec
     * @param scalar
     * @return
     */
    public static Vec3 div(Vec3 vec, float scalar) {
        return new Vec3(vec).div(scalar);
    }

    /**
     * Return a new normalised vector based on vec
     *
     * @param vec
     * @return - a new normalised vector
     */
    public static Vec3 norm(Vec3 vec) {
        return new Vec3(vec).norm();
    }

    /**
     * Calculates the cross prod between v1 and v2 and stores result in dst
     *
     * @param v1
     * @param v2
     * @param dst
     */
    public static void cross(final Vec3 v1, final Vec3 v2, Vec3 dst) {
        dst.x = v1.y * v2.z - v1.z * v2.y;
        dst.y = v1.z * v2.x - v1.x * v2.z;
        dst.z = v1.x * v2.y - v1.y * v2.x;
    }

    /**
     * Returns a new transformed vector
     *
     * @param mat
     * @param vec
     * @return
     */
    public static Vec3 transform(Matrix44 mat, Vec3 vec) {
        return new Vec3(vec).transform(mat);
    }

    public static void createVecFromPoints(Vec3 p1, Vec3 p2, Vec3 vec) {
        vec.set(p2.x - p1.x, p2.y - p1.y, p2.z - p1.z);
    }

    public static void createVecFromPoints(Vec4 p1, Vec4 p2, Vec3 vec) {
        vec.set(p2.x - p1.x, p2.y - p1.y, p2.z - p1.z);
    }

    public void set(Vec3 other) {
        x = other.x;
        y = other.y;
        z = other.z;
    }

    /**
     * Set this vector
     *
     * @param x
     * @param y
     * @param z
     * @return
     */
    public Vec3 set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    /**
     * Negates this vector
     *
     * @param
     * @return This
     */
    public Vec3 neg() {
        x = -x;
        y = -y;
        z = -z;
        return this;
    }

    /**
     * This vector + v2 vector
     *
     * @param
     * @return This
     */
    public Vec3 add(final Vec3 v2) {
        this.x += v2.x;
        this.y += v2.y;
        this.z += v2.z;
        return this;
    }

    /**
     * This vector - v2 vector
     *
     * @param
     * @return This
     */
    public Vec3 sub(final Vec3 v2) {
        x -= v2.x;
        y -= v2.y;
        z -= v2.z;
        return this;
    }

    /**
     * This vector multiplied with scalar
     *
     * @param
     * @return This
     */
    public Vec3 mult(float scalar) {
        x *= scalar;
        y *= scalar;
        z *= scalar;
        return this;
    }

    /**
     * This vector divided with scalar (no check for division by 0)
     *
     * @param
     * @return This
     */
    public Vec3 div(float scalar) {
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
    public float dot(final Vec3 v2) {
        return x * v2.x + y * v2.y + z * v2.z;
    }

    /**
     * The angle in radians between vectors
     *
     * @param
     * @return Angle between this and v2 vector
     */
    public float angle(final Vec3 v2) {
        return (float) Math.acos(dot(v2) / (this.length() * v2.length()));
    }

    /**
     * This vector projected onto v2
     *
     * @param
     * @return New projected vector
     */
    public Vec3 project(final Vec3 v2) {
        float len = dot(v2) / v2.length();
        return new Vec3(v2).mult(len);
    }

    /**
     * Normalize this vector
     *
     * @param
     * @return This
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
     *
     * @param other
     * @return
     */
    public Vec3 cross(final Vec3 other) {
        return new Vec3(y * other.z - z * other.y, z * other.x - x * other.z, x * other.y - y
                * other.x);

    }

    /**
     * Return the distance between this point and v2 point
     *
     * @param v2
     * @return
     */
    public float distance(final Vec3 v2) {
        return Vec3.sub(this, v2).length();
    }

    /**
     * Transform this vector. Matrix on left side and vector on right side. This
     * is OpenGL style.
     *
     * @param mat
     */
    public Vec3 transform(Matrix44 mat) {
        float xnew = x * mat.m[0] + y * mat.m[4] + z * mat.m[8] + mat.m[12];
        float ynew = x * mat.m[1] + y * mat.m[5] + z * mat.m[9] + mat.m[13];
        float znew = x * mat.m[2] + y * mat.m[6] + z * mat.m[10] + mat.m[14];

        x = xnew;
        y = ynew;
        z = znew;

        return this;
    }

    /**
     * Transform this vector. Matrix on left side and vector on right side. This
     * is OpenGL style.
     *
     * @param mat
     */
    public Vec3 transform(Matrix33 mat) {
        float xnew = x * mat.m[0] + y * mat.m[3] + z * mat.m[6];
        float ynew = x * mat.m[1] + y * mat.m[4] + z * mat.m[7];
        float znew = x * mat.m[2] + y * mat.m[5] + z * mat.m[8];

        x = xnew;
        y = ynew;
        z = znew;

        return this;
    }

    @Override
    public String toString() {
        return String.format("[% #06.2f % #06.2f % #06.2f]", x, y, z);
    }
}
